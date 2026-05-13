#!/usr/bin/env python3
from __future__ import annotations

import argparse
import json
import os
import re
import sys
import urllib.error
import urllib.request
from dataclasses import dataclass
from pathlib import Path


BUYER_EMAIL = os.environ.get("BUYER_EMAIL", "raka.pratama@shopme.local")
BUYER_PASSWORD = os.environ.get("BUYER_PASSWORD", "Demo123!")
SELLER_EMAIL = os.environ.get("SELLER_EMAIL", "ayu.lestari@shopme.local")
SELLER_PASSWORD = os.environ.get("SELLER_PASSWORD", "Demo123!")


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
    path_without_query = path.split("?", 1)[0]
    normalized = path_without_query if path_without_query.startswith("/") else f"/{path_without_query}"
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


def request_bytes(url: str, method: str = "GET", body: bytes | None = None, headers: dict[str, str] | None = None) -> tuple[int, bytes]:
    request = urllib.request.Request(url, data=body, headers=headers or {}, method=method)
    try:
        with urllib.request.urlopen(request) as response:
            return response.status, response.read()
    except urllib.error.HTTPError as error:
        return error.code, error.read()


def assert_envelope(payload: dict, label: str) -> list[str]:
    failures: list[str] = []
    required = {"timestamp", "status", "code", "message", "traceId"}
    missing = sorted(required.difference(payload.keys()))
    if missing:
        failures.append(f"{label}: missing envelope keys {missing}")
    return failures


def first_list_item(payload: dict) -> dict | None:
    data = payload.get("data")
    if isinstance(data, list) and data:
        first = data[0]
        return first if isinstance(first, dict) else None
    return None


def expect_status(
    failures: list[str],
    label: str,
    actual_status: int,
    expected_statuses: set[int],
) -> None:
    if actual_status not in expected_statuses:
        expected = "/".join(str(item) for item in sorted(expected_statuses))
        failures.append(f"{label} returned {actual_status}, expected {expected}")


def verify_runtime(base_url: str, android_root: Path) -> list[str]:
    failures: list[str] = []
    sample_image_path = android_root / "app/src/main/res/mipmap-mdpi/ic_launcher.webp"
    sample_image_bytes = sample_image_path.read_bytes()

    invalid_status, invalid_login = request_json(
        f"{base_url}/api/auth/login",
        method="POST",
        body={"email": BUYER_EMAIL, "password": "wrong-password"},
    )
    expect_status(failures, "login invalid credentials", invalid_status, {400, 401})
    failures.extend(assert_envelope(invalid_login, "invalid login"))

    unauthorized_status, unauthorized_payload = request_json(
        f"{base_url}/api/customer",
        method="GET",
    )
    expect_status(failures, "GET /api/customer without token", unauthorized_status, {401})
    failures.extend(assert_envelope(unauthorized_payload, "GET /api/customer without token"))

    buyer_status, buyer_login = request_json(
        f"{base_url}/api/auth/login",
        method="POST",
        body={"email": BUYER_EMAIL, "password": BUYER_PASSWORD},
    )
    if buyer_status != 200:
        failures.append(f"buyer login returned {buyer_status}")
        return failures
    failures.extend(assert_envelope(buyer_login, "buyer login"))

    seller_status, seller_login = request_json(
        f"{base_url}/api/auth/login",
        method="POST",
        body={"email": SELLER_EMAIL, "password": SELLER_PASSWORD},
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

    invalid_token_status, invalid_token_payload = request_json(
        f"{base_url}/api/customer",
        method="GET",
        token="invalid-token",
    )
    expect_status(failures, "GET /api/customer with invalid token", invalid_token_status, {401})
    failures.extend(assert_envelope(invalid_token_payload, "GET /api/customer with invalid token"))

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

    media_ticket_status, media_ticket_payload = request_json(
        f"{base_url}/api/media/presign-upload?scope=verifier-profile&contentType=image/webp",
        method="POST",
        token=buyer_token,
    )
    expect_status(failures, "POST /api/media/presign-upload", media_ticket_status, {201})
    failures.extend(assert_envelope(media_ticket_payload, "POST /api/media/presign-upload"))
    media_ticket_data = media_ticket_payload.get("data", {})
    upload_url = media_ticket_data.get("uploadUrl") if isinstance(media_ticket_data, dict) else None
    original_url = media_ticket_data.get("originalUrl") if isinstance(media_ticket_data, dict) else None
    upload_method = media_ticket_data.get("uploadMethod") if isinstance(media_ticket_data, dict) else None
    if not isinstance(upload_url, str) or not upload_url:
        failures.append("POST /api/media/presign-upload missing uploadUrl")
    if not isinstance(original_url, str) or not original_url:
        failures.append("POST /api/media/presign-upload missing originalUrl")
    if upload_method != "PUT":
        failures.append(f"POST /api/media/presign-upload unexpected uploadMethod={upload_method!r}")

    if isinstance(upload_url, str) and upload_url:
        upload_status, upload_response = request_bytes(
            upload_url,
            method="PUT",
            body=sample_image_bytes,
            headers={"Content-Type": "image/webp"},
        )
        expect_status(failures, "PUT presigned media upload", upload_status, {200, 201, 204})
        if upload_status not in {200, 201, 204} and upload_response:
            failures.append(
                f"PUT presigned media upload returned body {upload_response.decode('utf-8', errors='replace')[:200]}"
            )

    if isinstance(original_url, str) and original_url:
        original_status, original_bytes = request_bytes(original_url, method="GET")
        expect_status(failures, "GET uploaded media originalUrl", original_status, {200})
        if original_status == 200 and len(original_bytes) == 0:
            failures.append("GET uploaded media originalUrl returned empty body")

    buyer_profile_status, buyer_profile_payload = request_json(
        f"{base_url}/api/customer",
        method="GET",
        token=buyer_token,
    )
    if buyer_profile_status != 200:
        failures.append(f"GET /api/customer returned {buyer_profile_status}")
        failures.extend(assert_envelope(buyer_profile_payload, "GET /api/customer"))
        return failures
    failures.extend(assert_envelope(buyer_profile_payload, "GET /api/customer"))

    buyer_profile_data = buyer_profile_payload.get("data", {})
    if isinstance(original_url, str) and original_url and isinstance(buyer_profile_data, dict):
        profile_update_status, profile_update_payload = request_json(
            f"{base_url}/api/customer",
            method="PUT",
            token=buyer_token,
            body={
                "name": buyer_profile_data.get("name", "Verifier Buyer"),
                "phone": buyer_profile_data.get("phone") or "08123456789",
                "photo": original_url,
                "fcmToken": "demo-fcm-token",
            },
        )
        expect_status(failures, "PUT /api/customer with uploaded media", profile_update_status, {200})
        failures.extend(assert_envelope(profile_update_payload, "PUT /api/customer with uploaded media"))

        updated_profile_status, updated_profile_payload = request_json(
            f"{base_url}/api/customer",
            method="GET",
            token=buyer_token,
        )
        expect_status(failures, "GET /api/customer after media profile update", updated_profile_status, {200})
        failures.extend(assert_envelope(updated_profile_payload, "GET /api/customer after media profile update"))
        updated_profile_data = updated_profile_payload.get("data")
        if not isinstance(updated_profile_data, dict):
            failures.append("GET /api/customer after media profile update missing data object")
            updated_photo = None
        else:
            updated_photo = updated_profile_data.get("photo")
        if not isinstance(updated_photo, str) or "/api/media/medium?key=" not in updated_photo:
            failures.append(f"updated customer profile photo is not a managed medium media URL: {updated_photo!r}")

    read_cases = [
        ("POST", "/api/splash", None, {
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
        ("GET", "/api/support", None, {"token": buyer_token}),
        ("GET", "/api/support/chat", None, {"token": buyer_token}),
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

    captured_payloads: dict[str, dict] = {}

    for method, path, _, options in read_cases:
        status, payload = request_json(
            f"{base_url}{path}",
            method=method,
            body=options.get("body"),
            token=options.get("token"),
        )
        if status != 200:
            failures.append(f"{method} {path} returned {status}")
        failures.extend(assert_envelope(payload, f"{method} {path}"))
        captured_payloads[path] = payload

    support_payload = captured_payloads.get("/api/support", {})
    support_data = support_payload.get("data", {})
    if not isinstance(support_data, dict):
        failures.append("GET /api/support missing data object")
        return failures
    required_support_fields = {
        "phone",
        "email",
        "whatsapp",
        "whatsappMessageTemplate",
        "emailSubject",
        "emailBodyTemplate",
        "operationalStartHour",
        "operationalEndHour",
        "operationalTimezone",
        "operationalHoursLabel",
        "statusOnlineLabel",
        "statusOfflineLabel",
        "liveChatTitle",
        "liveChatStatusOnlineLabel",
        "sellerOnboardingTitle",
        "sellerOnboardingDescription",
        "sellerOnboardingFooter",
        "faq",
        "bootstrapMessages",
        "sellerTerms",
    }
    missing_support_fields = sorted(required_support_fields.difference(support_data.keys()))
    if missing_support_fields:
        failures.append(f"GET /api/support missing fields {missing_support_fields}")
    if not isinstance(support_data.get("faq"), list) or not support_data.get("faq"):
        failures.append("GET /api/support faq is empty")
    if not isinstance(support_data.get("bootstrapMessages"), list) or not support_data.get("bootstrapMessages"):
        failures.append("GET /api/support bootstrapMessages is empty")
    if not isinstance(support_data.get("sellerTerms"), list) or not support_data.get("sellerTerms"):
        failures.append("GET /api/support sellerTerms is empty")

    support_chat_payload = captured_payloads.get("/api/support/chat", {})
    support_chat_data = support_chat_payload.get("data", {})
    if not isinstance(support_chat_data, dict):
        failures.append("GET /api/support/chat missing data object")
    elif not isinstance(support_chat_data.get("messages"), list) or not support_chat_data.get("messages"):
        failures.append("GET /api/support/chat messages is empty")

    support_chat_message = "android verifier support ping"
    support_chat_send_status, support_chat_send_payload = request_json(
        f"{base_url}/api/support/chat",
        method="POST",
        token=buyer_token,
        body={"message": support_chat_message},
    )
    expect_status(failures, "POST /api/support/chat", support_chat_send_status, {200})
    failures.extend(assert_envelope(support_chat_send_payload, "POST /api/support/chat"))
    send_data = support_chat_send_payload.get("data", {})
    if not isinstance(send_data, dict) or not isinstance(send_data.get("messages"), list):
        failures.append("POST /api/support/chat missing messages list")
    elif not any(
        isinstance(item, dict) and item.get("message") == support_chat_message
        for item in send_data["messages"]
    ):
        failures.append("POST /api/support/chat did not echo sent support message in conversation")

    foods_payload = captured_payloads.get("/api/foods", {})
    food_item = first_list_item(foods_payload)
    if not food_item:
        failures.append("GET /api/foods returned no items for buyer runtime flow")
        return failures

    food_id = food_item.get("id")
    if not isinstance(food_id, str) or not food_id:
        failures.append("GET /api/foods first item missing id")
        return failures

    food_detail_status, food_detail_payload = request_json(
        f"{base_url}/api/foods/{food_id}",
        method="GET",
        token=buyer_token,
    )
    expect_status(failures, f"GET /api/foods/{food_id}", food_detail_status, {200})
    failures.extend(assert_envelope(food_detail_payload, f"GET /api/foods/{food_id}"))

    variant_payload = []
    food_detail_data = food_detail_payload.get("data", {})
    if isinstance(food_detail_data, dict):
        variants = food_detail_data.get("variants")
        if isinstance(variants, list):
            for variant in variants:
                if not isinstance(variant, dict):
                    continue
                options = variant.get("options")
                option = options[0] if isinstance(options, list) and options else None
                variant_id = variant.get("id")
                option_id = option.get("id") if isinstance(option, dict) else None
                if isinstance(variant_id, str) and variant_id and isinstance(option_id, str) and option_id:
                    variant_payload.append({
                        "variantId": variant_id,
                        "optionId": option_id,
                    })

    cart_clear_status, cart_clear_payload = request_json(
        f"{base_url}/api/cart/clear",
        method="DELETE",
        token=buyer_token,
    )
    expect_status(failures, "DELETE /api/cart/clear", cart_clear_status, {200})
    failures.extend(assert_envelope(cart_clear_payload, "DELETE /api/cart/clear"))

    cart_add_status, cart_add_payload = request_json(
        f"{base_url}/api/cart",
        method="POST",
        token=buyer_token,
        body={
            "foodId": food_id,
            "variants": variant_payload,
            "quantity": 1,
            "note": "android buyer flow",
        },
    )
    expect_status(failures, "POST /api/cart", cart_add_status, {201})
    failures.extend(assert_envelope(cart_add_payload, "POST /api/cart"))

    cart_list_status, cart_list_payload = request_json(
        f"{base_url}/api/cart",
        method="GET",
        token=buyer_token,
    )
    expect_status(failures, "GET /api/cart after add", cart_list_status, {200})
    failures.extend(assert_envelope(cart_list_payload, "GET /api/cart after add"))

    cart_item = first_list_item(cart_list_payload)
    if not cart_item:
        failures.append("GET /api/cart after add returned no items")
        return failures

    cart_id = cart_item.get("id")
    if not isinstance(cart_id, str) or not cart_id:
        failures.append("GET /api/cart after add missing cart item id")
        return failures

    session_status, session_payload = request_json(
        f"{base_url}/api/order/session",
        method="GET",
        token=buyer_token,
    )
    expect_status(failures, "GET /api/order/session", session_status, {200})
    failures.extend(assert_envelope(session_payload, "GET /api/order/session"))

    checkout_token = session_payload.get("data", {}).get("token")
    if not isinstance(checkout_token, str) or not checkout_token:
        failures.append("GET /api/order/session missing checkout token")
        return failures

    before_order_list_status, before_order_list_payload = request_json(
        f"{base_url}/api/order",
        method="GET",
        token=buyer_token,
    )
    expect_status(failures, "GET /api/order before create", before_order_list_status, {200})
    failures.extend(assert_envelope(before_order_list_payload, "GET /api/order before create"))
    before_order_ids = {
        item.get("id")
        for item in before_order_list_payload.get("data", [])
        if isinstance(item, dict) and isinstance(item.get("id"), str)
    }

    create_order_status, create_order_payload = request_json(
        f"{base_url}/api/order",
        method="POST",
        token=buyer_token,
        body={
            "cartId": [cart_id],
            "payment": "TRANSFER",
            "token": checkout_token,
        },
    )
    expect_status(failures, "POST /api/order", create_order_status, {201})
    failures.extend(assert_envelope(create_order_payload, "POST /api/order"))

    order_list_status, order_list_payload = request_json(
        f"{base_url}/api/order",
        method="GET",
        token=buyer_token,
    )
    expect_status(failures, "GET /api/order after create", order_list_status, {200})
    failures.extend(assert_envelope(order_list_payload, "GET /api/order after create"))

    order_items = order_list_payload.get("data")
    if not isinstance(order_items, list):
        failures.append("GET /api/order after create missing order list data")
        return failures

    order_id = None
    for item in order_items:
        if not isinstance(item, dict):
            continue
        candidate_id = item.get("id")
        if isinstance(candidate_id, str) and candidate_id not in before_order_ids:
            order_id = candidate_id
            break

    if not isinstance(order_id, str) or not order_id:
        failures.append("GET /api/order after create missing newly created order id")
        return failures

    order_detail_status, order_detail_payload = request_json(
        f"{base_url}/api/order/{order_id}",
        method="GET",
        token=buyer_token,
    )
    expect_status(failures, f"GET /api/order/{order_id}", order_detail_status, {200})
    failures.extend(assert_envelope(order_detail_payload, f"GET /api/order/{order_id}"))

    confirm_transfer_status, confirm_transfer_payload = request_json(
        f"{base_url}/api/order/{order_id}/confirm-transfer",
        method="POST",
        token=buyer_token,
    )
    expect_status(failures, f"POST /api/order/{order_id}/confirm-transfer", confirm_transfer_status, {200})
    failures.extend(assert_envelope(confirm_transfer_payload, f"POST /api/order/{order_id}/confirm-transfer"))

    ensure_conversation_status, ensure_conversation_payload = request_json(
        f"{base_url}/api/chat/conversation?cafeId={seller_cafe_id}",
        method="POST",
        token=buyer_token,
    )
    expect_status(failures, "POST /api/chat/conversation", ensure_conversation_status, {200})
    failures.extend(assert_envelope(ensure_conversation_payload, "POST /api/chat/conversation"))
    ensured_conversation_id = ensure_conversation_payload.get("data", {}).get("id")
    if not isinstance(ensured_conversation_id, str) or not ensured_conversation_id:
        failures.append("POST /api/chat/conversation missing conversation id")

    notifications_status, notifications_payload = request_json(
        f"{base_url}/api/notifications",
        method="GET",
        token=buyer_token,
    )
    expect_status(failures, "GET /api/notifications after order", notifications_status, {200})
    failures.extend(assert_envelope(notifications_payload, "GET /api/notifications after order"))

    chat_list_status, chat_list_payload = request_json(
        f"{base_url}/api/chat/list",
        method="GET",
        token=buyer_token,
    )
    expect_status(failures, "GET /api/chat/list after order", chat_list_status, {200})
    failures.extend(assert_envelope(chat_list_payload, "GET /api/chat/list after order"))

    chat_list = chat_list_payload.get("data", {}).get("chatList")
    conversation_id = None
    if isinstance(chat_list, list) and isinstance(ensured_conversation_id, str) and ensured_conversation_id:
        if any(
            isinstance(item, dict) and item.get("id") == ensured_conversation_id
            for item in chat_list
        ):
            conversation_id = ensured_conversation_id
        else:
            failures.append("GET /api/chat/list after order missing ensured conversation")
    if isinstance(chat_list, list) and chat_list:
        conversation_id = conversation_id or (chat_list[0].get("id") if isinstance(chat_list[0], dict) else None)
        if isinstance(conversation_id, str) and conversation_id:
            chat_detail_status, chat_detail_payload = request_json(
                f"{base_url}/api/chat?id={conversation_id}",
                method="GET",
                token=buyer_token,
            )
            expect_status(failures, "GET /api/chat buyer detail", chat_detail_status, {200})
            failures.extend(assert_envelope(chat_detail_payload, "GET /api/chat buyer detail"))

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
    expect_status(failures, "POST /api/config as buyer", forbidden_status, {403})
    failures.extend(assert_envelope(forbidden_payload, "forbidden config"))

    seller_payment_status, seller_payment_payload = request_json(
        f"{base_url}/api/seller/payment-methods",
        method="PUT",
        token=seller_token,
        body={
            "cashEnabled": True,
            "bankEnabled": True,
            "bankNumber": "1234567890",
            "ovoEnabled": False,
            "ovoNumber": None,
            "danaEnabled": True,
            "danaNumber": "0811111111",
            "gopayEnabled": False,
            "gopayNumber": None,
        },
    )
    expect_status(failures, "PUT /api/seller/payment-methods", seller_payment_status, {200})
    failures.extend(assert_envelope(seller_payment_payload, "PUT /api/seller/payment-methods"))

    seller_availability_status, seller_availability_payload = request_json(
        f"{base_url}/api/seller/availability",
        method="PUT",
        token=seller_token,
        body={"isOnline": True},
    )
    expect_status(failures, "PUT /api/seller/availability", seller_availability_status, {200})
    failures.extend(assert_envelope(seller_availability_payload, "PUT /api/seller/availability"))

    seller_orders_refresh_status, seller_orders_refresh_payload = request_json(
        f"{base_url}/api/seller/orders",
        method="GET",
        token=seller_token,
    )
    expect_status(failures, "GET /api/seller/orders after buyer checkout", seller_orders_refresh_status, {200})
    failures.extend(assert_envelope(seller_orders_refresh_payload, "GET /api/seller/orders after buyer checkout"))

    seller_order_id = None
    for item in seller_orders_refresh_payload.get("data", []):
        if not isinstance(item, dict):
            continue
        candidate_id = item.get("orderId")
        if isinstance(candidate_id, str) and candidate_id:
            seller_order_id = candidate_id
            if candidate_id == order_id:
                break

    if not isinstance(seller_order_id, str) or not seller_order_id:
        failures.append("GET /api/seller/orders after buyer checkout missing seller order id")
        return failures

    seller_order_detail_status, seller_order_detail_payload = request_json(
        f"{base_url}/api/seller/orders/{seller_order_id}",
        method="GET",
        token=seller_token,
    )
    expect_status(failures, f"GET /api/seller/orders/{seller_order_id}", seller_order_detail_status, {200})
    failures.extend(assert_envelope(seller_order_detail_payload, f"GET /api/seller/orders/{seller_order_id}"))

    seller_chat_list_status, seller_chat_list_payload = request_json(
        f"{base_url}/api/chat/list?asSeller=true",
        method="GET",
        token=seller_token,
    )
    expect_status(failures, "GET /api/chat/list?asSeller=true", seller_chat_list_status, {200})
    failures.extend(assert_envelope(seller_chat_list_payload, "GET /api/chat/list?asSeller=true"))

    if isinstance(conversation_id, str) and conversation_id:
        seller_chat_detail_status, seller_chat_detail_payload = request_json(
            f"{base_url}/api/chat?id={conversation_id}&asSeller=true",
            method="GET",
            token=seller_token,
        )
        expect_status(failures, "GET /api/chat seller detail", seller_chat_detail_status, {200})
        failures.extend(assert_envelope(seller_chat_detail_payload, "GET /api/chat seller detail"))

        seller_mark_read_status, seller_mark_read_payload = request_json(
            f"{base_url}/api/chat/read?asSeller=true",
            method="POST",
            token=seller_token,
            body={"id": conversation_id, "message": ""},
        )
        expect_status(failures, "POST /api/chat/read?asSeller=true", seller_mark_read_status, {200})
        failures.extend(assert_envelope(seller_mark_read_payload, "POST /api/chat/read?asSeller=true"))

        buyer_clear_chat_status, buyer_clear_chat_payload = request_json(
            f"{base_url}/api/chat",
            method="DELETE",
            token=buyer_token,
        )
        expect_status(failures, "DELETE /api/chat", buyer_clear_chat_status, {200})
        failures.extend(assert_envelope(buyer_clear_chat_payload, "DELETE /api/chat"))

        buyer_chat_list_empty_status, buyer_chat_list_empty_payload = request_json(
            f"{base_url}/api/chat/list",
            method="GET",
            token=buyer_token,
        )
        expect_status(failures, "GET /api/chat/list after clear", buyer_chat_list_empty_status, {200})
        failures.extend(assert_envelope(buyer_chat_list_empty_payload, "GET /api/chat/list after clear"))

        buyer_chat_list_after_clear = buyer_chat_list_empty_payload.get("data", {}).get("chatList")
        if buyer_chat_list_after_clear != []:
            failures.append("DELETE /api/chat did not clear buyer chat list")

        seller_chat_list_empty_status, seller_chat_list_empty_payload = request_json(
            f"{base_url}/api/chat/list?asSeller=true",
            method="GET",
            token=seller_token,
        )
        expect_status(failures, "GET /api/chat/list?asSeller=true after clear", seller_chat_list_empty_status, {200})
        failures.extend(assert_envelope(seller_chat_list_empty_payload, "GET /api/chat/list?asSeller=true after clear"))

        seller_chat_list_after_clear = seller_chat_list_empty_payload.get("data", {}).get("chatList")
        if not isinstance(seller_chat_list_after_clear, list):
            failures.append("GET /api/chat/list?asSeller=true after clear missing chatList")
        elif any(
            isinstance(item, dict) and item.get("id") == conversation_id
            for item in seller_chat_list_after_clear
        ):
            failures.append("DELETE /api/chat did not clear seller chat list for shared conversation")

        seller_clear_chat_status, seller_clear_chat_payload = request_json(
            f"{base_url}/api/chat?asSeller=true",
            method="DELETE",
            token=seller_token,
        )
        expect_status(failures, "DELETE /api/chat?asSeller=true", seller_clear_chat_status, {200})
        failures.extend(assert_envelope(seller_clear_chat_payload, "DELETE /api/chat?asSeller=true"))

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
        DtoPair("SupportCenterResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/SupportCenterResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/SupportCenterResponse.kt"),
        DtoPair("SupportFaqResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/SupportCenterResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/SupportCenterResponse.kt"),
        DtoPair("SupportBootstrapMessageResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/SupportCenterResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/SupportCenterResponse.kt"),
        DtoPair("SupportSellerTermResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/SupportCenterResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/SupportCenterResponse.kt"),
        DtoPair("SupportChatMessageRequest", "data/src/main/java/com/mtv/app/shopme/data/remote/request/SupportChatMessageRequest.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/request/SupportChatMessageRequest.kt"),
        DtoPair("SupportChatResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/SupportChatResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/SupportChatResponse.kt"),
        DtoPair("SupportChatMessageResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/SupportChatResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/SupportChatResponse.kt"),
        DtoPair("MediaUploadResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/MediaUploadResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/MediaUploadResponse.kt"),
        DtoPair("AddressResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/AddressResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/AddressResponse.kt"),
        DtoPair("VillageResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/VillageResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/VillageResponse.kt"),
        DtoPair("AppConfigResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/AppConfigResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/AppConfigResponse.kt"),
        DtoPair("CafeAddressResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/CafeAddressResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/CafeAddressResponse.kt"),
        DtoPair("FoodResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/FoodResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/FoodResponse.kt"),
        DtoPair("ChatListItemResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/ChatListItemResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/ChatResponse.kt"),
        DtoPair("OrderSummaryResponse", "data/src/main/java/com/mtv/app/shopme/data/remote/response/OrderResponse.kt", "shopme-common/src/main/kotlin/com/mtv/be/common/dto/response/OrderResponse.kt"),
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
        failures.extend(verify_runtime(base_url, android_root))

    if failures:
        print("Contract verification failed:")
        for failure in failures:
            print(f"- {failure}")
        return 1

    print("Contract verification passed.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
