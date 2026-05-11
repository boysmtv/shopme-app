# Android Shopme V2 Implementation Checklist

## Purpose

Checklist ini mengubah `brief.v2.md` Android menjadi backlog implementasi yang finite.

Status baseline saat dokumen ini dibuat:

- Android sudah tervalidasi ke backend Docker nyata
- `ai-agent core` run `android-kotlin-compose-brief-v2` sudah `approved`
- validation path utama sudah ada, tetapi seluruh roadmap v2 belum otomatis dianggap selesai

## Completion Rule

Sebuah item hanya boleh ditandai selesai bila:

- perubahan kode sudah ada
- test yang relevan sudah ada atau diperbarui
- verifikasi runtime terhadap backend Docker nyata sudah dilakukan bila item menyentuh integrasi
- hasilnya tidak bertentangan dengan `brief.v2.md`

## Workstream 1. Contract And Runtime Stabilization

- [ ] Audit semua DTO request/response yang dipakai flow buyer dan seller terhadap payload backend aktif
- [ ] Audit semua mapper domain untuk enum, nullability, partial data, dan image field
- [ ] Pastikan semua error response backend penting (`400/401/403/404/409/422/500`) termapping jelas di Android
- [ ] Pastikan `401` dan `403` menghasilkan perilaku UI dan navigasi yang berbeda
- [ ] Samakan parsing timestamp/timezone untuk order, notifications, chat, dan createdAt umum
- [ ] Pastikan list screen tidak mengasumsikan response non-paginated akan selalu kecil

Evidence minimum:

- contract verification hijau
- unit test mapper/error handling diperbarui
- catatan mismatch yang ditemukan dan ditutup

## Workstream 2. Buyer Flow Hardening

- [ ] Splash dan bootstrap config tervalidasi untuk state normal, maintenance, force update, dan invalid session
- [ ] Register, login, forgot password, verify OTP, reset password, change password, dan PIN flow tervalidasi penuh
- [ ] Customer profile, notification preferences, delete account, village, dan address CRUD tervalidasi penuh
- [ ] Food list, food detail, cafe detail, dan search tervalidasi penuh
- [ ] Cart add/update/delete/clear tervalidasi penuh
- [ ] Checkout session, create order, order detail, dan transfer confirmation tervalidasi penuh
- [ ] Buyer notifications dan buyer chat tervalidasi penuh
- [ ] Empty state, partial state, retry state, dan blocked state buyer flow konsisten

Evidence minimum:

- smoke flow buyer ke backend Docker lolos
- tidak ada screen buyer yang berhenti di loading tanpa recovery
- error message buyer sesuai error backend nyata

## Workstream 3. Seller Flow Hardening

- [ ] Seller eligibility dan seller profile tervalidasi penuh
- [ ] Create cafe tervalidasi penuh termasuk transisi user menjadi seller
- [ ] Edit store, cafe address, dan availability tervalidasi penuh
- [ ] Seller payment methods tervalidasi penuh
- [ ] Seller product list dan product CRUD tervalidasi penuh
- [ ] Seller orders list/detail tervalidasi penuh
- [ ] Seller notifications dan seller chat tervalidasi penuh
- [ ] State seller tanpa cafe atau setup parsial tertangani dengan benar

Evidence minimum:

- smoke flow seller ke backend Docker lolos
- tidak ada halaman seller yang technically terbuka tetapi secara fungsi tidak usable
- semua role/ownership rejection seller muncul benar di UI

## Workstream 4. Media, Base64, And Performance

- [ ] Semua field foto di Android diperlakukan sebagai `base64 string`
- [ ] Tentukan dan implementasikan format final media yang didukung
- [ ] Pastikan preview, compress, resize, encode, dan render image konsisten
- [ ] Tambahkan guard untuk payload gambar besar
- [ ] Pastikan image kosong atau invalid tidak menyebabkan crash
- [ ] Siapkan isolasi kode media agar migrasi kontrak masa depan lebih mudah

Evidence minimum:

- flow create/update/read image berjalan terhadap backend nyata
- tidak ada crash parsing/render image base64

## Workstream 5. Auth, Security, And Session Lifecycle

- [ ] Token storage dan refresh flow diaudit penuh
- [ ] Session expired, token invalid, dan refresh failure menghasilkan recovery path yang benar
- [ ] Endpoint publik vs protected vs role-specific dipetakan benar di client
- [ ] Logout manual dan logout paksa konsisten
- [ ] Data sensitif tidak bocor ke log debug

Evidence minimum:

- skenario auth failure diuji terhadap backend nyata
- security boundary customer/seller/admin tervalidasi

## Workstream 6. Regression, UAT, And Release Gate

- [ ] Pisahkan regression test untuk contract, mapper, repository, dan flow utama
- [ ] Pertahankan `verify-backend-integration.sh` sebagai gate integrasi utama
- [ ] Tambahkan checklist UAT buyer
- [ ] Tambahkan checklist UAT seller
- [ ] Tambahkan checklist poor network / retry / recovery
- [ ] Tambahkan checklist empty data / partial data
- [ ] Pastikan real device matrix minimum terdokumentasi

Evidence minimum:

- `verify-backend-integration.sh` hijau
- build Android hijau
- test Android yang relevan hijau
- UAT minimum terdokumentasi

## Explicit Release Gates

Release Android v2 tidak boleh dianggap aman bila salah satu ini belum hijau:

- [ ] buyer auth/profile/address/cart/order/notification/chat
- [ ] seller profile/cafe/product/payment method/order/notification/chat
- [ ] error response mapping utama
- [ ] auth dan role boundary
- [ ] base64 image flow
- [ ] contract verification ke backend Docker

## Deferred Until Proven Necessary

- [ ] pagination UI lanjutan bila backend belum benar-benar mengaktifkan pagination
- [ ] caching lanjutan di luar kebutuhan stabilitas runtime
- [ ] instrumentation/E2E device suite penuh

## Final Exit Criteria

Android v2 baru boleh dianggap selesai bila:

- semua workstream prioritas tinggi di atas sudah selesai
- semua flow buyer dan seller utama sudah terbukti bekerja ke backend Docker
- tidak ada fitur utama yang hanya lolos mock tetapi gagal di runtime nyata
- `ai-agent core` Android tetap `approved` setelah perubahan terakhir
