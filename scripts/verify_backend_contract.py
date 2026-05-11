#!/usr/bin/env python3
from __future__ import annotations

import argparse
import json
import re
import sys
import urllib.error
import urllib.request
from dataclasses import dataclass
from pathlib import Path


@dataclass(frozen=True)
class Endpoint:
    method: str
    path: str


@dataclass(frozen=True)
class DtoPair:
    name: str
    android_path: str
    backend_path: str


def fetch_openapi(url: str) -> dict:
    with urllib.request.urlopen(url) as response:
        return json.load(response)


def read_text(path: Path) -> str:
    return path.read_text(encoding="utf-8")


def parse_constants(content: str) -> dict[str, str]:
    constants: dict[str, str] = {}
    for name, value in re.findall(r'private const val (\w+) = "([^"]+)"', content):
        constants[name] = value
    return constants


def expand_path(raw_path: str, constants: dict[str, str]) -> str:
    expanded = raw_path
    for key, value in constants.items():
        expanded = expanded.replace(f"${key}", value)
    expanded = re.sub(r"\$\{[^}]+\}", "{param}", expanded)
    expanded = re.sub(r"\$[A-Za-z_][A-Za-z0-9_]*", "{param}", expanded)
    expanded = expanded.replace('"', "").strip()
    expanded = expanded if expanded.startswith("/") else f"/{expanded}"
    expanded = re.sub(r"/+", "/", expanded)
    return expanded


def collect_android_endpoints(api_endpoint_file: Path) -> list[Endpoint]:
    content = read_text(api_endpoint_file)
    constants = parse_constants(content)
    pattern = re.compile(
        r"override val path = (?P<path>\"[^\"]+\"|[^\n;]+?)\s*;\s*override val method = HttpMethod\.(?P<method>\w+)",
        re.MULTILINE,
    )
    endpoints: set[Endpoint] = set()
    for match in pattern.finditer(content):
        path = expand_path(match.group("path"), constants)
        method = match.group("method").upper()
        endpoints.add(Endpoint(method=method, path=path))
    return sorted(endpoints, key=lambda item: (item.path, item.method))


def normalize_path(path: str) -> str:
    normalized = path if path.startswith("/") else f"/{path}"
    normalized = re.sub(r"/+", "/", normalized)
    return normalized.rstrip("/") or "/"


def path_segments(path: str) -> list[str]:
    trimmed = normalize_path(path).strip("/")
    return [] if not trimmed else trimmed.split("/")


def is_placeholder(segment: str) -> bool:
    return segment.startswith("{") and segment.endswith("}")


def paths_compatible(android_path: str, backend_path: str) -> bool:
    android_segments = path_segments(android_path)
    backend_segments = path_segments(backend_path)
    if len(android_segments) != len(backend_segments):
        return False
    for android_segment, backend_segment in zip(android_segments, backend_segments):
        if android_segment == backend_segment:
            continue
        if is_placeholder(android_segment) or is_placeholder(backend_segment):
            continue
        return False
    return True


def parse_enum_entries(path: Path) -> list[str]:
    content = read_text(path)
    match = re.search(r"enum class \w+[^{]*\{(?P<body>.*?)\n\}", content, re.DOTALL)
    if not match:
        raise ValueError(f"Enum block not found in {path}")
    body = match.group("body").split(";")[0]
    entries: list[str] = []
    for raw_line in body.splitlines():
        line = raw_line.strip()
        if not line or line.startswith("//"):
            continue
        line = line.rstrip(",")
        token = re.match(r"([A-Z0-9_]+)", line)
        if token:
            entries.append(token.group(1))
    return entries


def extract_data_class_body(content: str, class_name: str) -> str:
    marker = f"data class {class_name}("
    start = content.find(marker)
    if start == -1:
        raise ValueError(f"Data class {class_name} not found")
    index = start + len(marker)
    depth = 1
    while index < len(content):
        char = content[index]
        if char == "(":
            depth += 1
        elif char == ")":
            depth -= 1
            if depth == 0:
                return content[start + len(marker):index]
        index += 1
    raise ValueError(f"Unclosed data class body for {class_name}")


def parse_data_class_fields(path: Path, class_name: str) -> list[str]:
    content = read_text(path)
    body = extract_data_class_body(content, class_name)
    fields: list[str] = []
    for raw_line in body.splitlines():
        line = raw_line.strip()
        if not line or line.startswith("@"):
            continue
        field = re.search(r"\b(?:val|var)\s+(\w+)\s*:", line)
        if field:
            fields.append(field.group(1))
    return fields


def request_json(url: str, method: str = "GET", body: dict | None = None, token: str | None = None) -> tuple[int, dict]:
    headers = {"Content-Type": "application/json"}
    if token:
        headers["Authorization"] = f"Bearer {token}"
    payload = None if body is None else json.dumps(body).encode("utf-8")
    request = urllib.request.Request(url, data=payload, headers=headers, method=method)
    try:
        with urllib.request.urlopen(request) as response:
            return response.status, json.load(response)
    except urllib.error.HTTPError as error:
        return error.code, json.loads(error.read().decode("utf-8"))


def assert_envelope(payload: dict, label: str) -> list[str]:
    failures: list[str] = []
    required = {"timestamp", "status", "code", "message", "traceId"}
    missing = sorted(required.difference(payload.keys()))
    if missing:
        failures.append(f"{label}: missing envelope keys {missing}")
    return failures


def verify_runtime(base_url: str) -> list[str]:
    failures: list[str] = []

    invalid_status, invalid_login = request_json(
        f"{base_url}/api/auth/login",
        method="POST",
        body={"email": "buyer.demo@shopme.local", "password": "wrong-password"},
    )
    if invalid_status not in {400, 401}:
        failures.append(f"login invalid credentials returned {invalid_status}, expected 400/401")
    failures.extend(assert_envelope(invalid_login, "invalid login"))

    buyer_status, buyer_login = request_json(
        f"{base_url}/api/auth/login",
        method="POST",
        body={"email": "buyer.demo@shopme.local", "password": "Demo123!"},
    )
    if buyer_status != 200:
        failures.append(f"buyer login returned {buyer_status}")
        return failures
    failures.extend(assert_envelope(buyer_login, "buyer login"))

    seller_status, seller_login = request_json(
        f"{base_url}/api/auth/login",
        method="POST",
        body={"email": "seller.demo@shopme.local", "password": "Demo123!"},
    )
    if seller_status != 200:
        failures.append(f"seller login returned {seller_status}")
        return failures
    failures.extend(assert_envelope(seller_login, "seller login"))

    buyer_token = buyer_login.get("data", {}).get("accessToken", "")
    seller_token = seller_login.get("data", {}).get("accessToken", "")
    if not buyer_token:
        failures.append("buyer login missing token")
        return failures
    if not seller_token:
        failures.append("seller login missing token")
        return failures

    seller_profile_status, seller_profile_payload = request_json(
        f"{base_url}/api/seller/profile",
        method="GET",
        token=seller_token,
    )
    if seller_profile_status != 200:
        failures.append(f"GET /api/seller/profile returned {seller_profile_status}")
        failures.extend(assert_envelope(seller_profile_payload, "GET /api/seller/profile"))
        return failures
    failures.extend(assert_envelope(seller_profile_payload, "GET /api/seller/profile"))

    seller_cafe_id = seller_profile_payload.get("data", {}).get("cafeId")
    if not seller_cafe_id:
        failures.append("seller profile missing cafeId")
        return failures

    smoke_cases = [
        ("POST", "/api/splash", None, {
            "token": buyer_token,
            "body": {
                "fcmToken": "demo-fcm-token",
                "platform": "android",
                "deviceId": "android-sync-checker",
                "appVersionName": "1.0.0",
                "appVersionCode": 1,
                "deviceInfo": {
                    "androidId": "android-sync-checker",
                    "brand": "Google",
                    "manufacturer": "Google",
                    "model": "sdk_gphone64_arm64",
                    "device": "emu64a",
                    "hardware": "ranchu",
                    "supportedAbis": "arm64-v8a,x86_64",
                    "sdkVersion": 34,
                    "androidVersion": "14",
                    "screenWidth": 1080,
                    "screenHeight": 2400,
                    "densityDpi": 420,
                    "locale": "id-ID",
                    "timezone": "Asia/Jakarta",
                    "batteryLevel": 85,
                    "networkType": "wifi",
                    "freeStorageMb": 1024,
                    "freeRamMb": 2048,
                    "appVersionName": "1.0.0",
                    "appVersionCode": 1,
                    "firstInstallTime": 0,
                    "lastUpdateTime": 0
                },
                "createdAt": "2026-05-11T00:00:00Z"
            },
        }),
        ("GET", "/api/customer", None, {"token": buyer_token}),
        ("GET", "/api/address", None, {"token": buyer_token}),
        ("GET", "/api/village", None, {"token": buyer_token}),
        ("GET", "/api/foods", None, {"token": buyer_token}),
        ("GET", "/api/cart", None, {"token": buyer_token}),
        ("GET", "/api/order", None, {"token": buyer_token}),
        ("GET", "/api/notifications", None, {"token": buyer_token}),
        ("GET", "/api/chat/list", None, {"token": buyer_token}),
        ("GET", f"/api/cafe/{seller_cafe_id}", None, {"token": seller_token}),
        ("GET", f"/api/cafe/address/{seller_cafe_id}", None, {"token": seller_token}),
        ("GET", "/api/seller/payment-methods", None, {"token": seller_token}),
        ("GET", "/api/seller/orders", None, {"token": seller_token}),
    ]

    for method, path, _, options in smoke_cases:
        status, payload = request_json(
            f"{base_url}{path}",
            method=method,
            body=options.get("body"),
            token=options.get("token"),
        )
        if status != 200:
            failures.append(f"{method} {path} returned {status}")
        failures.extend(assert_envelope(payload, f"{method} {path}"))

    forbidden_status, forbidden_payload = request_json(
        f"{base_url}/api/config",
        method="POST",
        body={
            "platform": "android",
            "minVersion": 1,
            "latestVersion": 1,
            "forceUpdate": False,
            "maintenanceMode": False,
            "maintenanceMessage": None,
        },
        token=buyer_token,
    )
    if forbidden_status != 403:
        failures.append(f"POST /api/config as buyer returned {forbidden_status}, expected 403")
    failures.extend(assert_envelope(forbidden_payload, "forbidden config"))

    return failures


def verify_endpoints(android_root: Path, openapi: dict) -> list[str]:
    api_file = android_root / "data/src/main/java/com/mtv/app/shopme/data/remote/api/ApiEndPoint.kt"
    android_endpoints = collect_android_endpoints(api_file)
    backend_paths = openapi.get("paths", {})
    failures: list[str] = []

    for endpoint in android_endpoints:
        matched = False
        for backend_path, operations in backend_paths.items():
            if endpoint.method.lower() not in operations:
                continue
            if paths_compatible(endpoint.path, backend_path):
                matched = True
                break
        if not matched:
            failures.append(f"missing backend endpoint for {endpoint.method} {endpoint.path}")

    return failures


def verify_enums(android_root: Path, backend_root: Path) -> list[str]:
    enum_pairs = [
        ("FoodCategory", "domain/src/main/java/com/mtv/app/shopme/domain/model/FoodCategory.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/model/FoodCategory.kt"),
        ("FoodStatus", "domain/src/main/java/com/mtv/app/shopme/domain/model/FoodStatus.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/utils/FoodStatus.kt"),
        ("OrderStatus", "domain/src/main/java/com/mtv/app/shopme/domain/model/OrderStatus.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/model/OrderStatus.kt"),
        ("OrderItemStatus", "domain/src/main/java/com/mtv/app/shopme/domain/model/OrderItemStatus.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/model/OrderItemStatus.kt"),
        ("PaymentMethod", "domain/src/main/java/com/mtv/app/shopme/domain/model/PaymentMethod.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/model/PaymentMethod.kt"),
        ("PaymentStatus", "domain/src/main/java/com/mtv/app/shopme/domain/model/PaymentStatus.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/model/PaymentStatus.kt"),
    ]
    failures: list[str] = []

    for name, android_relative, backend_relative in enum_pairs:
        android_entries = parse_enum_entries(android_root / android_relative)
        backend_entries = parse_enum_entries(backend_root / backend_relative)
        if android_entries != backend_entries:
            failures.append(
                f"enum mismatch for {name}: android={android_entries} backend={backend_entries}"
            )

    return failures


def verify_dtos(android_root: Path, backend_root: Path) -> list[str]:
    dto_pairs = [
        DtoPair("LoginRequest", "data/src/main/java/com/mtv/app/shopme/data/remote/request/LoginRequest.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/request/LoginRequest.kt"),
        DtoPair("RegisterRequest", "data/src/main/java/com/mtv/app/shopme/data/remote/request/RegisterRequest.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/request/RegisterRequest.kt"),
        DtoPair("ForgotPasswordRequest", "data/src/main/java/com/mtv/app/shopme/data/remote/request/ForgotPasswordRequest.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/request/ForgotPasswordRequest.kt"),
        DtoPair("VerifyOtpRequest", "data/src/main/java/com/mtv/app/shopme/data/remote/request/VerifyOtpRequest.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/request/VerifyOtpRequest.kt"),
        DtoPair("ResetPasswordRequest", "data/src/main/java/com/mtv/app/shopme/data/remote/request/ResetPasswordRequest.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/request/ResetPasswordRequest.kt"),
        DtoPair("ChangePasswordRequest", "data/src/main/java/com/mtv/app/shopme/data/remote/request/ChangePasswordRequest.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/request/ChangePasswordRequest.kt"),
        DtoPair("ChangePinRequest", "data/src/main/java/com/mtv/app/shopme/data/remote/request/ChangePinRequest.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/request/ChangePinRequest.kt"),
        DtoPair("AddressAddRequest", "data/src/main/java/com/mtv/app/shopme/data/remote/request/AddressAddRequest.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/request/AddressAddRequest.kt"),
        DtoPair("CafeAddRequest", "data/src/main/java/com/mtv/app/shopme/data/remote/request/CafeAddRequest.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/request/CafeAddRequest.kt"),
        DtoPair("SellerAvailabilityRequest", "data/src/main/java/com/mtv/app/shopme/data/remote/request/SellerAvailabilityRequest.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/request/SellerAvailabilityRequest.kt"),
        DtoPair("SellerPaymentMethodRequest", "data/src/main/java/com/mtv/app/shopme/data/remote/request/SellerPaymentMethodRequest.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/request/SellerPaymentMethodRequest.kt"),
        DtoPair("SplashRequest", "data/src/main/java/com/mtv/app/shopme/data/remote/request/SplashRequest.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/request/SplashRequest.kt"),
        DtoPair("LoginResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/LoginResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/LoginResponse.kt"),
        DtoPair("SplashResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/SplashResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/SplashResponse.kt"),
        DtoPair("AddressResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/AddressResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/AddressResponse.kt"),
        DtoPair("VillageResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/VillageResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/VillageResponse.kt"),
        DtoPair("AppConfigResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/AppConfigResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/AppConfigResponse.kt"),
        DtoPair("CafeAddressResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/CafeAddressResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/CafeAddressResponse.kt"),
        DtoPair("FoodResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/FoodResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/FoodResponse.kt"),
        DtoPair("OrderResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/OrderResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/OrderResponse.kt"),
    ]
    failures: list[str] = []

    for pair in dto_pairs:
        android_fields = parse_data_class_fields(android_root / pair.android_path, pair.name)
        backend_fields = parse_data_class_fields(backend_root / pair.backend_path, pair.name)
        if set(android_fields) != set(backend_fields):
            failures.append(
                f"dto mismatch for {pair.name}: android={android_fields} backend={backend_fields}"
            )

    return failures


def main() -> int:
    parser = argparse.ArgumentParser(description="Verify Android Shopme contract against backend Shopme.")
    parser.add_argument("--android-root", default=".", help="Path to Android repo root")
    parser.add_argument("--backend-root", required=True, help="Path to backend repo root")
    parser.add_argument("--openapi-url", default="http://127.0.0.1:8080/v3/api-docs", help="OpenAPI URL")
    parser.add_argument("--skip-runtime", action="store_true", help="Skip live runtime probes")
    args = parser.parse_args()

    android_root = Path(args.android_root).resolve()
    backend_root = Path(args.backend_root).resolve()

    openapi = fetch_openapi(args.openapi_url)
    failures: list[str] = []
    failures.extend(verify_endpoints(android_root, openapi))
    failures.extend(verify_enums(android_root, backend_root))
    failures.extend(verify_dtos(android_root, backend_root))
    if not args.skip_runtime:
        base_url = args.openapi_url.removesuffix("/v3/api-docs")
        failures.extend(verify_runtime(base_url))

    if failures:
        print("Contract verification failed:")
        for failure in failures:
            print(f"- {failure}")
        return 1

    print("Contract verification passed.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
