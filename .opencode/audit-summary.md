# Forensic Audit Summary — Meeting Notes

## Goal
Read every `.kt` file in `domain/`, `data/`, and `nav/` modules and analyze across 10 audit categories (crash, coroutines, memory, architecture, networking, database, security, performance, quality, testing).

## Coverage
| Module | Source | Test | Total |
|--------|--------|------|-------|
| domain/ | ~110 files (models, repos, params, use cases) | 4 | ~114 |
| data/ | ~100 files (base, di, remote, mapper, sync, realtime, repo, utils, fake, mock, request, response, dto) | 14 | ~114 |
| nav/ | ~42 files (routes, nav actions, extensions) | 1 | ~43 |
| **Total** | **~252** | **~19** | **~271** |

## Issues Found (Preliminary)

### CRASH (3)
| ID | Severity | File | Class/Func | Issue |
|----|----------|------|------------|-------|
| C-01 | HIGH | DomainMapper.kt:99 | `OrderSummaryResponse.toDomain()` | `BigDecimal.toDouble()` on `totalPrice` — precision loss for large values |
| C-02 | MEDIUM | DomainMapper.kt:121 | `OrderSummaryResponse.toDomain()` | Hardcoded `OrderItemStatus.AVAILABLE` — ignores actual item status from API |
| C-03 | MEDIUM | DomainMapper.kt:474 | `Unit.toDomain()` | Always returns `Register(success = true)` regardless of actual API response |

### COROUTINES (2)
| ID | Severity | File | Issue |
|----|----------|------|-------|
| R-01 | MEDIUM | OfflineMutationSyncManager.kt:41 | `CoroutineScope(SupervisorJob() + Dispatchers.IO)` never cancelled — leaks background work |
| R-02 | MEDIUM | ShopmeRealtimeGatewayImpl.kt:148 | WebSocket reconnect coroutine in unbounded scope — no lifecycle awareness |

### MEMORY (1)
| ID | Severity | File | Issue |
|----|----------|------|-------|
| M-01 | LOW | DomainMapper.kt | Single 520-line mapper file with 50+ extension functions — maintenance burden |

### ARCHITECTURE (4)
| ID | Severity | File | Issue |
|----|----------|------|-------|
| A-01 | HIGH | ResponseMapper.kt | Entire file is dead code — `toResponse()` functions are `private` and never called |
| A-02 | MEDIUM | ApiEndPoint.kt:168 | `Order.UpdateStatus` appends `status.name` to URL path without encoding |
| A-03 | LOW | ChatListResponse.kt:15 | Uses `var` (mutable) — inconsistent with all other response DTOs using `val` |
| A-04 | LOW | PendingMutationAction.kt | Serialization discriminator `"type"` requires all subtypes to be non-sealed in serialization |

### NETWORKING (2)
| ID | Severity | File | Issue |
|----|----------|------|-------|
| N-01 | MEDIUM | ApiEndPoint.kt:227 | `Media.PresignUpload` — URL params `scope`/`contentType` not encoded |
| N-02 | LOW | ApiEndPoint.kt:263 | `toQueryValue()` extension exists but is unused by `Media.Download` |

### DATABASE (1)
| ID | Severity | File | Issue |
|----|----------|------|-------|
| D-01 | LOW | PendingMutationStore.kt:57 | Deduplication deserializes + iterates all pending records in memory |

### SECURITY (3)
| ID | Severity | File | Issue |
|----|----------|------|-------|
| S-01 | HIGH | ShopmeRealtimeGatewayImpl.kt:102 | Auth token passed in WebSocket URL query param — logged by OkHttp/Chucker |
| S-02 | MEDIUM | ShopmeRealtimeGatewayImpl.kt:39 | ChuckerInterceptor used on WebSocket — should be debug-only |
| S-03 | LOW | LoginRequest.kt, RegisterRequest.kt | Uses `var` instead of `val` — mutable request bodies |

### PERFORMANCE (1)
| ID | Severity | File | Issue |
|----|----------|------|-------|
| P-01 | LOW | OfflineMutationSyncManager.kt:84 | Sequential processing of all pending mutations — no parallel batching |

### QUALITY (3)
| ID | Severity | File | Issue |
|----|----------|------|-------|
| Q-01 | LOW | ApiEndPoint.kt | Inconsistent formatting — single-line vs multi-line endpoints mixed |
| Q-02 | LOW | LoginRequest.kt:1 | Wrong file comment header says `RegisterRequest.kt` |
| Q-03 | LOW | AddressDeleteRequest.kt:1 | Wrong file comment header says `CartInquiryRequest.kt` |

### TESTING (3)
| ID | Severity | File | Issue |
|----|----------|------|-------|
| T-01 | MEDIUM | — | `OfflineMutationSyncManager` and `PendingMutationStore` have no tests |
| T-02 | MEDIUM | — | `AuthRepositoryImpl`, `AppRepositoryImpl`, `MediaRepositoryImpl` have no tests |
| T-03 | LOW | Nav tests | Only 1 nav test file (`CustomerNavActionsTest`) — no seller nav route tests |

## Notable Observations
- **Serialization consistency**: Request/response DTOs use kotlinx.serialization consistently
- **API versioning**: No API version prefix (`v1/`) anywhere — all endpoints use `api/{resource}` bare
- **Price types**: Domain uses `BigDecimal` (Food, Cart), but `Double` on Order — precision mismatch
- **ChatPhoto field**: `Customer.photo: String` (non-nullable) vs `CustomerResponse.photo: String?` (nullable)
- **Nav pattern**: All route composables follow identical template pattern via `BaseRoute` + `BaseScreen`
- **Offline support**: Only cart, profile, seller availability/payment mutations are queued — order creation has no offline queue
- **Reconnect**: WebSocket has 2-second reconnect delay with no exponential backoff

## Next Steps
1. Generate detailed fix recommendations per issue
2. Optionally create fix implementations via `coding_task_run`
