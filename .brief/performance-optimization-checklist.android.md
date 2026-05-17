# Android Shopme Performance Optimization Checklist

## Purpose

Checklist ini memecah optimasi Android Shopme menjadi backlog yang finite dan bisa dibuktikan.

Dokumen ini fokus ke:

- perceived performance
- rendering cost
- network efficiency
- image upload/render cost
- cache/offline behavior
- runtime stability saat data besar

## Baseline Rule

Sebelum item optimasi ditandai selesai:

- build Android harus hijau
- flow utama tidak boleh regress
- verifikasi ke backend Docker tetap hijau
- perubahan harus punya bukti ukuran dampak, walau sederhana

## Success Metrics

Target praktis v1 optimasi:

- home, detail, cart, chat list, seller dashboard terasa responsif pada dataset realistis
- first content render lebih cepat dari baseline sebelumnya
- scrolling list utama tidak terasa patah pada device uji normal
- tidak ada decode/render image besar di main thread
- request list tidak menarik payload lebih besar dari yang dibutuhkan screen

## Workstream 1. Image Upload And Render Cost

- [ ] Audit semua titik upload, resolve, decode, dan render image di Android
- [ ] Pastikan decode/render image besar tidak terjadi di main thread
- [ ] Pisahkan penggunaan image list/thumbnail dari image detail/full
- [ ] Tambahkan guard untuk image invalid, oversized, atau empty
- [ ] Hindari decode ulang image yang sama pada screen yang sama
- [ ] Audit avatar chat, product list, cafe list, dan seller store image

Evidence minimum:

- daftar titik decode yang sudah ditutup
- tidak ada screen utama yang decode image besar saat compose awal tanpa proteksi

## Workstream 2. Feed And List Rendering

- [ ] Audit `Home`, `Search`, `Order`, `ChatList`, `Notif`, `SellerOrder`, `SellerProductList`
- [ ] Pastikan item list memakai key stabil
- [ ] Hindari state besar yang memicu recomposition seluruh screen
- [ ] Audit nested scroll, nested lazy, dan layout berat
- [ ] Pastikan shimmer list tidak lebih berat dari konten aslinya
- [ ] Audit pagination append agar tidak redraw seluruh dataset

Evidence minimum:

- daftar list screen yang sudah diaudit
- catatan fix recomposition/layout berat yang ditutup

## Workstream 3. Network And Payload Efficiency

- [ ] Audit screen yang eager-load terlalu banyak API sekaligus
- [ ] Pastikan screen hanya memuat data yang benar-benar dipakai
- [ ] Audit retry yang bisa memicu duplicate fetch
- [ ] Pastikan pagination dipakai pada screen list besar
- [ ] Audit polling/refresh yang berlebihan setelah event realtime

Evidence minimum:

- daftar endpoint per screen yang sudah dikurangi atau dirapikan
- tidak ada refresh penuh yang tidak perlu saat delta kecil masuk

## Workstream 4. Cache And Offline

- [ ] Tetapkan screen yang wajib `show cached first`
- [ ] Terapkan cache untuk home feed
- [ ] Terapkan cache ringkas untuk profile, chat list, order ringkas, seller dashboard ringkas bila layak
- [ ] Pastikan fallback offline tidak memblok loading tanpa akhir
- [ ] Audit invalidation cache saat favorite, cart, order, atau profile berubah

Evidence minimum:

- screen penting masih usable saat backend lambat atau request gagal
- cache stale tetap punya recovery path yang jelas

## Workstream 5. Compose State And Side Effect Hygiene

- [ ] Audit `LaunchedEffect`, `snapshotFlow`, dan observer yang bisa duplicate work
- [ ] Audit `rememberSaveable` yang menyimpan object berat atau tidak saveable
- [ ] Pastikan event realtime tidak memicu reload screen penuh tanpa alasan
- [ ] Pisahkan state content, loading, processing, dan one-shot event dengan jelas

Evidence minimum:

- tidak ada crash/state loop dari side effect yang tidak terkendali
- screen utama tidak memproses ulang data besar setiap recomposition

## Workstream 6. Chat And Notification Performance

- [ ] Saat event websocket masuk, update hanya bagian state yang relevan
- [ ] Hindari reload seluruh chat list untuk satu pesan baru bila delta bisa dipakai
- [ ] Hindari reload seluruh notification list untuk satu notif baru bila delta bisa dipakai
- [ ] Audit unread badge update agar incremental

Evidence minimum:

- chat dan notif tetap realtime tanpa refresh berat yang berulang

## Workstream 7. Loading UX

- [ ] Pastikan shimmer hanya untuk content loading
- [ ] Pastikan action submit tetap memakai processing indicator ringan
- [ ] Pastikan skeleton mengikuti bentuk konten asli
- [ ] Hindari shimmer yang lebih mahal dari kontennya sendiri

Evidence minimum:

- tidak ada screen data besar yang memakai loading visual berlebihan

## Workstream 8. Verification

- [ ] Tambahkan benchmark/check sederhana untuk titik berat utama
- [ ] Tambahkan unit test/integration test untuk cache behavior penting
- [ ] Pertahankan build dan verifier backend integration tetap hijau

Evidence minimum:

- `./gradlew -g .gradle-home assembleDebug testDebugUnitTest`
- `bash ./verify-backend-integration.sh`

## Priority Order

Kerjakan urutan ini lebih dulu:

1. image upload/render cost
2. list rendering home/search/chat/order
3. cache `show cached first`
4. realtime delta update
5. state/side-effect hygiene

## Final Exit Criteria

Optimasi Android dianggap layak bila:

- screen buyer dan seller utama terasa lebih ringan pada dataset realistis
- tidak ada regress integrasi backend
- cache/offline baseline aman
- list utama tidak lagi menarik atau merender data secara boros
