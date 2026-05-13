# Shopme Android AI-Agent Rules

## Active Scope

- Android project root: `D:\Boys\Android\shopme-app`
- Backend counterpart: `D:\Boys\Backend\shopme-backend`
- Active memory source: `.ai-agent/MEMORY.md`
- Brief files under `.brief/` are historical planning documents. If a brief conflicts with `.ai-agent/MEMORY.md`, trust `.ai-agent/MEMORY.md` and current source code.

## Mandatory Memory Rule

Before and after any meaningful Android or cross-repo Shopme work:

- read `.ai-agent/MEMORY.md`
- update `.ai-agent/MEMORY.md` with newly learned function, DTO, mapper, endpoint, ViewModel, test, and flow facts
- record unverified assumptions as explicit gaps

Do not leave Shopme knowledge only in chat history.

Every added project capability, tool, workflow, CI/CD step, deployment path, secret contract, generated credential location, and verification result must be written into `.ai-agent/MEMORY.md` in the same working session. Chat-only notes are not acceptable project memory.

## Current Media Contract

Photos/images are not an active base64 contract.

Current Android behavior:

- local `content://` image is compressed to a temporary JPEG file via `uriToCompressedJpegFile`
- Android calls `POST /api/media/presign-upload`
- Android uploads the JPEG bytes with HTTP `PUT` to the presigned URL
- Android uses the returned `originalUrl` as the media reference in cafe/product/profile request DTOs
- existing remote `http://` or `https://` image references are reused without upload
- rendering uses Coil `AsyncImage` through `SmartImage`

Current backend behavior:

- backend supports presigned upload, multipart upload, and `GET /api/media/{variant}?key=...`
- backend stores managed image keys and resolves response DTOs to media URLs
- inline `data:` media payloads are rejected by backend

Rules:

- do not reintroduce base64 as the active media contract
- do not decode or render base64 in UI unless current source code is changed intentionally
- when a local image is selected, upload it first and submit the returned media URL/reference
- keep list screens on thumbnail/medium URLs when backend provides variants

## End-To-End Completion Standard

An Android change is not complete until the agent can state:

- affected UI route, screen, ViewModel, event/effect, use case, repository, remote datasource, DTO, and backend endpoint
- success, loading, empty, retry, validation, unauthorized, forbidden, conflict, and server-error behavior where relevant
- mapper and nullability impact
- buyer/seller role impact
- verification performed: unit test, integration script, backend smoke, or documented manual/runtime check

## Contract And Error Rules

- Preserve backend envelope fields: `status`, `code`, `message`, `data`, `timestamp`.
- `401` maps to forced/session logout behavior when the screen uses `handleSessionError`.
- `403` must not be treated as token expiry.
- `409` and validation failures should show business-readable validation dialogs.
- Do not mask contract drift with fake fallback data.
- Keep Android DTOs aligned with backend DTOs for enum names, nullable fields, timestamps, pagination, and media URLs.
