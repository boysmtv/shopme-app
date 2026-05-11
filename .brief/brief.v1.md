# Android Shopme Integration Brief

## Objective

Pastikan aplikasi Android `shopme` benar-benar sinkron dengan backend `shopme` yang berjalan di Docker, tanpa asumsi payload palsu, tanpa mismatch enum/field/nullability, dan tanpa flow UI yang hanya terlihat benar tetapi gagal saat memukul API nyata.

## Scope

Project ini adalah client Android untuk backend:

- Android repo: `/Users/dedywijaya/Work/Google/learn/project/android-mtv-based-app-shopme`
- Backend repo: `/Users/dedywijaya/Work/Google/learn/project/backend-mtv-shopme`

Brief ini fokus pada validasi integrasi Android terhadap backend yang hidup di Docker, bukan sekadar validasi UI lokal atau preview Compose.

## Problem Statement

Android harus dianggap berhasil hanya bila:

- request yang dikirim ke backend valid secara URL, method, auth, body, enum, dan tipe data
- response backend bisa dimapping Android tanpa crash, tanpa fallback palsu, dan tanpa data penting hilang
- flow buyer dan seller bisa dipakai menggunakan data nyata dari backend Docker
- environment Android mudah diarahkan ke backend review yang aktif

Masalah yang harus dihindari:

- hardcode payload atau model yang tidak sesuai kontrak backend
- field Android yang masih mengasumsikan bentuk lama, misalnya image list vs string tunggal, config nullability, atau enum label yang tidak cocok
- flow seller yang tampak hidup di UI tetapi gagal saat role, cafe, atau payment method belum sinkron
- flow buyer yang gagal saat address, cart, order, notification, atau chat memakai data backend nyata

## Integration Source Of Truth

Android wajib mengikuti kontrak backend aktual berikut:

- `POST /api/splash`
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/forgot-password`
- `POST /api/auth/verify-otp`
- `POST /api/auth/reset-password`
- `POST /api/auth/change-password`
- `GET/PUT/DELETE /api/customer`
- `GET/PUT /api/customer/notification-preferences`
- `POST/PUT /api/customer/pin`
- `GET/POST/PUT/PATCH/DELETE /api/address`
- `GET /api/village`
- `GET /api/cafe`
- `GET /api/cafe/{id}`
- `POST /api/cafe`
- `PUT /api/cafe/{id}`
- `GET/POST /api/cafe/address`
- `GET /api/seller/profile`
- `GET/PUT /api/seller/payment-methods`
- `PUT /api/seller/availability`
- `GET /api/seller/orders`
- `GET /api/seller/orders/{id}`
- `GET /api/foods`
- `GET /api/foods/{id}`
- `GET /api/foods/cafe/{id}`
- `GET /api/foods/category/{category}`
- `GET /api/foods/search`
- `POST/PUT/DELETE /api/foods`
- `GET/POST/PUT/DELETE /api/cart`
- `DELETE /api/cart/cafe/{cafeId}`
- `DELETE /api/cart/clear`
- `GET /api/order/session`
- `POST /api/order`
- `GET /api/order`
- `GET /api/order/{id}`
- `POST /api/order/{id}/confirm-transfer`
- `PATCH /api/order/{id}/{statusOrder}`
- `GET /api/notifications`
- `GET /api/notifications/unread-count`
- `PUT /api/notifications/{id}/read`
- `PUT /api/notifications/read-all`
- `DELETE /api/notifications/{id}`
- `GET /api/chat/list`
- `GET /api/chat?id={conversationId}`
- `POST /api/chat/message`
- `POST /api/chat/read`

## Required Android Outcomes

### 1. Runtime Environment

- Android dapat diarahkan ke backend Docker yang aktif tanpa edit manual berulang.
- Base URL debug harus jelas dan cocok untuk environment review aktif.
- Jika backend berjalan di host LAN tertentu, Android harus tetap konsisten membaca `SHOPME_BASE_URL` atau fallback yang telah ditentukan tim.

### 2. Contract Fidelity

- DTO request/response Android harus cocok dengan payload backend aktual.
- Nullable field harus aman bila backend mengirim `null`, terutama pada `config`, image, address, notification, chat, dan seller profile.
- Enum Android harus mengikuti backend apa adanya, terutama:
  - `FoodCategory`: `FOOD`, `DRINK`, `SNACK`, `PRODUCT`, `SERVICE`, `OTHER`
  - status order
  - payment method/status
- Mapping model domain tidak boleh menghilangkan field penting yang dipakai UI.

### 3. Buyer Flow

Flow berikut harus valid terhadap backend Docker:

- splash
- login/register
- customer profile
- edit profile
- notification preferences
- PIN bootstrap dan change PIN
- village dan address CRUD
- home food list
- food detail
- cafe detail
- search
- cart add/update/delete/clear
- checkout session
- create order
- order list/detail
- transfer confirmation
- notifications
- chat customer

### 4. Seller Flow

Flow berikut harus valid terhadap backend Docker:

- seller eligibility dari profile customer
- create cafe
- update store
- update cafe address
- seller profile
- seller availability
- seller payment methods
- seller product list
- product add/edit/delete
- seller orders list/detail
- seller notifications
- seller chat

### 5. Media And Image Handling

- Android harus bisa menampilkan string image dari backend sebagai model yang benar.
- Jangan asumsikan backend selalu mengirim list image di tempat yang sekarang mengirim string tunggal.
- Upload image lokal harus tetap diserialisasi ke format yang backend bisa simpan dan render ulang.

## Demo And Validation Baseline

Android harus kompatibel dengan demo data backend berikut:

- script backend: `/Users/dedywijaya/Work/Google/learn/project/backend-mtv-shopme/seed-demo-data.sh`
- buyer demo: `buyer.demo@shopme.local / Demo123!`
- seller demo: `seller.demo@shopme.local / Demo123!`

Setelah backend Docker aktif dan demo script dijalankan, Android harus bisa:

- login buyer dan melihat home dengan produk nyata
- membuka detail cafe dan detail food tanpa mismatch field
- membuat, melihat, dan melanjutkan order buyer
- membaca notification dan chat buyer
- login seller dan melihat profile store, products, incoming order, notification, dan chat

## Acceptance Criteria

- tidak ada crash parsing untuk payload backend Docker yang sekarang aktif
- tidak ada request Android yang gagal hanya karena nama field, tipe data, enum, atau nullability mismatch
- buyer flow utama berjalan dengan demo data nyata
- seller flow utama berjalan dengan demo data nyata
- Android tidak bergantung pada mock lokal untuk flow yang sudah punya endpoint backend
- perubahan integrasi Android menjaga kompatibilitas dengan backend test guide dan smoke flow backend

## Validation Evidence Expected

- build Android berhasil
- smoke runtime terhadap backend Docker dilakukan dengan akun demo buyer dan seller
- mismatch contract yang ditemukan dicatat eksplisit
- jika ada penyesuaian DTO/mapping/UI, alasannya harus langsung ditautkan ke payload backend nyata

## Non Goals

- redesign UI besar yang tidak terkait integrasi
- menambah endpoint backend baru tanpa kebutuhan kontrak yang jelas
- mempertahankan mock lama jika endpoint backend nyata sudah tersedia
