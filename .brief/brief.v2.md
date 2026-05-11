# Android Shopme Development Brief V2

## Objective

Arah v2 untuk Android adalah menaikkan aplikasi dari sekadar "sudah terhubung ke backend Docker" menjadi produk yang stabil, jelas perilakunya saat error, dan siap dipakai untuk flow buyer dan seller secara lebih lengkap.

Brief ini merangkum hasil analisa dari kondisi Android saat ini, gap yang masih layak dikembangkan, dan prioritas kerja berikutnya agar Android tetap sinkron dengan backend `shopme`.

V2 ini juga harus dianggap sebagai mandat kerja untuk `ai-agent`: agent wajib memahami konteks penuh Android, backend, integrasi Docker, demo data, contract verifier, test flow, dan dependency antar fitur sebelum menyimpulkan bahwa pekerjaan sudah selesai.

## Current Baseline

Hal yang sudah baik dan harus dipertahankan:

- Android sudah tervalidasi terhadap backend Docker yang nyata.
- Contract check dasar sudah ada dan build/test utama sudah hijau.
- Error backend utama sudah lebih benar diteruskan ke layer Android.
- Buyer dan seller flow inti sudah punya baseline demo data backend.

Artinya, fokus v2 bukan lagi membuktikan koneksi dasar, tetapi mengeraskan kualitas runtime, UX error, dan coverage flow nyata.

## AI-Agent Execution Mandate

Untuk brief v2 ini, `ai-agent` harus bekerja dengan standar berikut:

- memahami struktur dan tujuan project Android `shopme`
- memahami struktur dan tujuan backend `shopme`
- memahami hubungan kontrak antara Android, backend, Docker runtime, demo data, smoke test, dan verifier
- tidak menganggap fitur selesai hanya karena build hijau; agent wajib membuktikan flow runtime yang relevan benar-benar bekerja
- mengetes semua flow utama Android yang bergantung pada backend nyata
- memverifikasi bahwa semua API backend yang dipakai Android pada flow utama menghasilkan success response pada skenario yang memang seharusnya sukses
- memverifikasi juga error response, auth boundary, dan role boundary pada skenario gagal yang memang seharusnya gagal

Agent tidak boleh bekerja hanya pada satu file atau satu modul tanpa memahami dampaknya ke keseluruhan proyek.

## Main Development Opportunities

### 1. Strengthen Runtime Error UX

Saat ini error backend sudah lebih sinkron, tetapi UX error di Android masih bisa dibuat jauh lebih konsisten.

Pengembangan yang disarankan:

- samakan pola tampilan error untuk `401`, `403`, `404`, `409`, `422`, dan timeout/network
- tampilkan pesan yang lebih operasional untuk user, tanpa kehilangan pesan backend asli
- bedakan antara error yang bisa di-retry, error validasi form, dan error state bisnis
- pastikan seluruh screen buyer dan seller punya loading, empty, error, dan retry state yang konsisten

Target hasil:

- tidak ada halaman yang berhenti di spinner diam
- tidak ada snackbar/generic toast yang terlalu umum untuk kasus bisnis penting
- user bisa memahami apakah masalah ada di koneksi, input, role, atau data backend

Validasi yang wajib tercakup:

- error response backend harus diuji untuk `400`, `401`, `403`, `404`, `409`, `422`, dan `500`
- Android harus memetakan `status`, `code`, dan `message` backend tanpa menghilangkan konteks penting
- perbedaan antara validation error, auth error, forbidden, conflict, dan server error harus terlihat jelas di UX

### 2. Remove Remaining UI-to-API Assumptions

Walau contract inti sudah tervalidasi, risiko terbesar Android berikutnya biasanya datang dari asumsi UI yang pelan-pelan menyimpang dari payload backend.

Pengembangan yang disarankan:

- audit semua mapper DTO ke domain untuk field optional, enum, image, address, payment, dan order status
- minimalkan fallback palsu yang membuat UI terlihat valid padahal data backend tidak lengkap
- tandai field yang benar-benar wajib untuk render dan field yang opsional untuk graceful degradation
- rapikan model bersama agar buyer flow dan seller flow tidak memakai interpretasi data yang berbeda

Target hasil:

- perubahan payload backend cepat terdeteksi
- UI tidak diam-diam salah menampilkan data

### 2A. Standardize Photo Handling As Base64 String

Semua foto pada integrasi `shopme` harus dianggap menggunakan format `base64 string` sebagai kontrak utama antara Android dan backend.

Implikasi pengembangan yang wajib diikuti:

- Android tidak boleh lagi mengasumsikan foto sebagai multipart upload, file path lokal, atau model list image bila kontrak endpoint hanya menerima string
- semua request create/update yang membawa foto harus mengirim `base64 string` yang valid
- semua response yang membawa foto harus dimapping sebagai string yang bisa langsung dirender, disimpan sementara, atau diubah ke representasi UI bila perlu
- logika kompresi, resize, preview, cache, dan retry upload di Android harus menyesuaikan format `base64 string`
- validasi ukuran payload, fallback saat image kosong, dan error parsing image harus ditangani secara eksplisit

Target hasil:

- tidak ada mismatch format foto antara Android dan backend
- semua flow yang memakai foto tetap stabil pada create, update, fetch, dan render ulang

Aturan yang masih harus ditegaskan saat implementasi:

- tentukan satu format kontrak final: raw base64 atau data URL seperti `data:image/jpeg;base64,...`
- tetapkan format gambar yang diizinkan: `jpg`, `jpeg`, `png`, atau `webp`
- tetapkan ukuran maksimum payload foto per request
- tentukan apakah resize dan kompresi wajib dilakukan di Android sebelum request dikirim

### 3. Harden Seller Operations

Seller flow biasanya paling rawan karena bergantung pada ownership, profile, cafe, product, payment method, order, dan chat sekaligus.

Pengembangan yang disarankan:

- validasi penuh flow create cafe, edit store, edit address, availability, payment methods, dan order handling
- pastikan form seller punya prefill, validation, dan success/error state yang kuat
- rapikan state management untuk screen yang mengambil beberapa endpoint sekaligus
- tambah proteksi untuk state transisi, misalnya user baru jadi seller, seller belum punya cafe, atau cafe belum punya address

Target hasil:

- seller tidak masuk ke halaman yang technically terbuka tapi secara data belum siap
- halaman seller bisa dipakai end-to-end tanpa patch manual di backend

### 4. Improve Buyer Checkout Reliability

Buyer experience akan terasa rusak bila cart, address, checkout session, dan order status tidak sepenuhnya sinkron.

Pengembangan yang disarankan:

- review ulang flow cart sampai order confirmation
- perjelas state saat address default belum ada
- validasi ulang perhitungan item, note, quantity, dan session checkout dari payload backend nyata
- rapikan empty-state dan blocked-state untuk kondisi seperti cart beda cafe, food tidak aktif, atau order conflict

Target hasil:

- buyer flow lebih aman untuk demo maupun QA regression
- bug yang muncul akan lebih mudah dilacak ke tahap flow tertentu

### 5. Add Real Integration Regression Layer

Saat ini sudah ada verifier contract, tetapi Android akan lebih aman bila ada regression layer yang dekat dengan perilaku app.

Pengembangan yang disarankan:

- tambah smoke integration untuk beberapa repository/use case utama
- tambah fixture payload backend nyata untuk parsing test
- pisahkan test yang memverifikasi contract, test yang memverifikasi mapper, dan test yang memverifikasi UI state
- siapkan satu jalur verifikasi yang wajib hijau sebelum merge perubahan integrasi

Target hasil:

- mismatch data lebih cepat ketahuan sebelum sampai ke QA manual
- perubahan Android tidak mudah merusak flow yang sebelumnya sudah hidup

### 6. Define Auth And Session Lifecycle More Explicitly

Android perlu aturan yang lebih tegas untuk lifecycle sesi agar tidak ada halaman yang gagal diam-diam saat token berubah status.

Pengembangan yang disarankan:

- tetapkan perilaku saat access token expired
- pastikan refresh token flow punya handling retry yang konsisten
- tentukan apa yang terjadi saat refresh gagal atau session invalid
- samakan perilaku logout manual, logout paksa, dan unauthorized global handling
- pastikan semua halaman penting kembali ke state yang benar setelah session hilang

Target hasil:

- tidak ada screen yang stuck akibat session invalid
- auth failure selalu menghasilkan navigasi dan pesan yang konsisten

Validasi security yang wajib tercakup:

- endpoint publik dan endpoint terproteksi harus dibedakan jelas di Android
- request tanpa token, token invalid, token expired, dan role yang tidak sesuai harus diuji terhadap backend nyata
- Android harus benar menangani `401 UNAUTHORIZED` vs `403 FORBIDDEN`
- penyimpanan token, refresh token, logout, dan session reset harus konsisten dengan perilaku backend aktif

### 7. Standardize Pagination, Sorting, And Filtering Consumption

List screen akan menjadi sumber mismatch berikutnya bila shape datanya tidak dibakukan sejak awal.

Pengembangan yang disarankan:

- audit endpoint list seperti foods, orders, notifications, dan chat
- samakan ekspektasi Android terhadap parameter paging, sorting, dan filtering
- tentukan apakah response list membawa metadata paging atau tidak
- pastikan UI tidak mengasumsikan seluruh list selalu kecil dan lengkap

Target hasil:

- list besar tetap aman dirender
- perubahan query behavior backend tidak merusak screen list

### 8. Clarify Business State Transition Handling

Android perlu memahami batas transisi state bisnis agar UX tidak menawari aksi yang sebenarnya tidak valid.

Pengembangan yang disarankan:

- petakan transisi order status yang valid untuk buyer dan seller
- pastikan cart, checkout, payment confirmation, dan seller availability punya aturan state yang jelas
- cegah UI menampilkan tombol aksi bila state backend sebenarnya tidak mengizinkan
- pastikan blocked state punya copy dan recovery path yang jelas

Target hasil:

- aksi di UI selalu sesuai dengan state bisnis backend
- user tidak diarahkan ke flow yang pasti ditolak server

### 8A. Handle Partial Valid Data Explicitly

Android tidak boleh menganggap semua response sukses selalu lengkap. Banyak bug runtime justru muncul saat backend mengembalikan data valid tetapi sebagian kosong.

Pengembangan yang disarankan:

- toleransi eksplisit untuk image kosong, address `null`, payment methods kosong, notification kosong, chat kosong, atau field opsional lain
- bedakan antara data kosong yang valid dan data rusak yang harus dianggap error
- pastikan UI tetap usable saat sebagian data belum tersedia
- jangan mengganti partial data dengan fallback palsu yang menyembunyikan kondisi backend sebenarnya

Target hasil:

- screen tetap stabil saat data valid tetapi belum lengkap
- empty state dan partial state lebih jujur terhadap kondisi backend

### 9. Add Performance Guardrails For Base64 And Repeated Fetches

Karena foto memakai `base64 string`, Android perlu guardrail performa yang eksplisit.

Pengembangan yang disarankan:

- batasi ukuran foto sebelum upload
- hindari render ulang string base64 besar secara berulang tanpa cache yang tepat
- pertimbangkan pemisahan kebutuhan thumbnail vs detail image
- audit screen list agar tidak memuat payload berat secara berlebihan

Target hasil:

- list dan form media tetap responsif
- penggunaan memori dan network lebih terkontrol

### 10. Add Mobile Observability Hooks

Saat bug integrasi muncul di device nyata, Android perlu jejak yang cukup untuk dikorelasikan ke backend.

Pengembangan yang disarankan:

- teruskan request id atau trace id bila backend menyediakannya
- stabilkan internal error categorization di Android
- tandai error yang sering terjadi agar mudah dianalisis tim
- siapkan log/debug evidence minimum untuk bug report QA

Target hasil:

- bug runtime lebih cepat dipetakan ke request backend terkait
- triage lintas Android dan backend jadi lebih cepat

### 11. Add Idempotency Awareness For Retried Actions

Android perlu sadar bahwa sebagian aksi penting bisa terkirim ulang akibat retry manual, poor network, atau lifecycle app.

Pengembangan yang disarankan:

- tetapkan aksi yang harus diperlakukan idempotent seperti create order, confirm transfer, update payment methods, dan chat send bila ada retry
- simpan atau teruskan idempotency key bila kontrak backend menyediakannya
- cegah tombol aksi kritis mengirim request ganda tanpa kontrol state yang jelas
- tampilkan hasil retry dengan cara yang tidak membingungkan user

Target hasil:

- retry dari Android tidak mudah menimbulkan duplikasi aksi bisnis
- flow pembayaran dan order lebih aman

### 12. Standardize Time And Timezone Handling

Android perlu aturan waktu yang konsisten agar parsing, sorting, dan display tidak pecah antar layar.

Pengembangan yang disarankan:

- anggap field waktu backend memakai format yang stabil seperti `ISO-8601` kecuali kontrak menyatakan lain
- tetapkan bagaimana Android mem-parse UTC/server time ke local time device
- samakan perilaku display waktu untuk order, notification, chat, dan audit-like timeline
- hindari parser berbeda-beda per fitur untuk field waktu yang sejenis

Target hasil:

- urutan data dan tampilan waktu konsisten
- bug parsing waktu lebih mudah dicegah

### 13. Define Poor-Network Behavior

Android perlu perilaku yang jelas saat koneksi lambat, timeout, atau network tidak stabil.

Pengembangan yang disarankan:

- bedakan UX untuk timeout, no connection, temporary backend error, dan retryable server failure
- tentukan data apa yang boleh tetap dibaca dari cache dan apa yang wajib refetch
- pastikan screen penting tidak kehilangan seluruh konteks saat retry
- berikan retry path yang jelas untuk aksi dan untuk fetch data

Target hasil:

- pengalaman pengguna lebih tahan terhadap koneksi buruk
- bug integrasi tidak tercampur dengan bug jaringan

### 14. Stabilize IDs Used Across UI, Logging, And Tracking

Android perlu menganggap ID bisnis tertentu sebagai field stabil agar observability dan navigasi antar screen tetap kuat.

Pengembangan yang disarankan:

- perlakukan `userId`, `cafeId`, `foodId`, `orderId`, dan `conversationId` sebagai identifier yang tidak boleh ditafsirkan ulang
- gunakan field ID yang sama untuk state UI, navigation argument, dan log/tracing
- hindari fallback ke display name bila ID resmi tersedia

Target hasil:

- state UI dan debugging lebih konsisten
- cross-screen navigation dan bug tracing lebih aman

### 15. Preserve A Migration Path For Media Contract Changes

Kontrak aktif saat ini adalah foto sebagai `base64 string`, tetapi Android perlu siap bila suatu saat backend memigrasikan media ke model lain.

Pengembangan yang disarankan:

- perlakukan `base64 string` sebagai kontrak aktif, bukan asumsi sementara yang kabur
- siapkan area kode yang terisolasi untuk encoding/decoding media agar migrasi kontrak nanti tidak menyebar ke seluruh app
- jangan campur logika preview image, upload encoding, dan parsing response di terlalu banyak fitur

Target hasil:

- kontrak media aktif tetap konsisten sekarang
- migrasi ke URL/object storage di masa depan punya jalur perubahan yang lebih terkendali

## Priority Roadmap

### Priority 1

- konsistensi error state lintas fitur buyer dan seller
- hardening seller flow end-to-end
- hardening checkout dan order flow buyer

### Priority 2

- audit mapper/domain model untuk nullability dan enum
- integration regression yang memakai payload backend nyata
- standardisasi empty/loading/error/retry state semua halaman utama
- auth/session lifecycle yang konsisten
- pagination, sorting, dan filtering consumption yang stabil
- partial data tolerance yang eksplisit
- timezone/time parsing yang seragam

### Priority 3

- perbaikan UX form
- optimasi messaging dan visual feedback
- instrumentation test untuk flow paling penting
- performance guardrails untuk base64 image
- observability untuk bug mobile di runtime
- poor-network behavior yang lebih matang
- isolasi logika media untuk migration path
- idempotency awareness untuk aksi kritis

## Workstreams And Milestones

V2 Android sebaiknya dieksekusi sebagai beberapa workstream besar yang jelas.

### Workstream 1. Contract And Runtime Stabilization

- rapikan DTO, mapper, enum, nullability, dan error mapping
- validasi ulang contract check ke backend Docker
- tutup mismatch payload sukses dan payload error

Milestone keluarannya:

- semua endpoint utama yang dipakai Android lolos contract validation
- parsing error dan auth behavior konsisten

### Workstream 2. Buyer Flow Hardening

- stabilkan splash, auth, profile, village, address, cart, checkout, order, notification, dan chat
- rapikan loading, empty, partial, error, dan retry state

Milestone keluarannya:

- buyer happy path dan failure path utama bisa dipakai end-to-end

### Workstream 3. Seller Flow Hardening

- stabilkan seller profile, create cafe, edit store, cafe address, product, payment methods, order, notification, dan chat
- tutup seluruh state gap untuk seller-no-cafe atau seller-partial-setup

Milestone keluarannya:

- seller flow utama usable tanpa patch manual

### Workstream 4. Media And Performance

- standardisasi base64 image
- optimasi resize/compress, memory, dan render image
- rapikan poor-network dan retry behavior untuk payload besar

Milestone keluarannya:

- flow foto stabil dan tidak menurunkan pengalaman app secara signifikan

### Workstream 5. QA, Regression, And Release Readiness

- tambahkan regression check, UAT checklist, dan release gate per flow
- satukan bukti build, test, smoke, dan runtime verification

Milestone keluarannya:

- ada jalur release yang bisa diulang dengan confidence

## Ownership Matrix

Pembagian tanggung jawab Android sebaiknya minimal seperti ini:

- Android contract adaptation: DTO, mapper, repository, dan use case yang membaca kontrak backend
- Android UX state: loading, empty, partial, error, retry, dan navigation recovery
- Android auth/security: token storage, refresh flow, unauthorized handling, dan logout
- Android media/base64: encoding, preview, resize, compress, cache, dan render
- Android regression: unit test, integration verifier, dan runtime smoke evidence
- Backend counterpart: perubahan kontrak, error semantics, auth boundary, dan demo data yang mempengaruhi Android

## Non-Functional Requirements

V2 Android perlu target non-fungsional yang eksplisit:

- app tidak boleh crash saat menerima payload backend aktif, termasuk partial data dan error response
- parsing image base64 tidak boleh menyebabkan screen utama unusable
- screen list utama tetap responsif walau payload memuat image string
- aksi penting harus tahan terhadap retry dan poor network tanpa duplikasi UX yang membingungkan
- regression verification harus bisa dijalankan ulang dengan langkah yang konsisten

## Privacy And Sensitive Data Handling

Android harus menjaga data sensitif dengan aturan yang jelas:

- token tidak boleh ditulis ke log biasa
- base64 image tidak boleh dicetak penuh di log atau analytics
- field sensitif seperti email, nomor kontak, alamat, dan payload chat harus dimasking bila masuk log debug
- data demo dan data nyata tidak boleh tercampur tanpa penanda yang jelas
- screen yang menampilkan data sensitif harus menghindari fallback debug yang membocorkan isi payload

## Real Device And Environment Matrix

Validasi Android tidak cukup hanya di satu emulator.

Minimal matrix yang harus dipikirkan:

- emulator standar untuk regresi cepat
- minimal satu device fisik
- versi Android minimum yang didukung project
- skenario network normal, lambat, timeout, dan putus sementara
- backend Docker via localhost emulator dan via host LAN

## Release Strategy And Compatibility Matrix

V2 Android perlu strategi rilis yang eksplisit:

- tetapkan kapan perubahan Android bisa rilis sendiri dan kapan harus menunggu backend
- jaga compatibility matrix antara build Android aktif dan kontrak backend aktif
- perubahan breaking contract tidak boleh diasumsikan aman hanya karena satu sisi sudah hijau
- siapkan rollback path bila backend berubah tetapi Android belum kompatibel, atau sebaliknya

## UAT Checklist

Selain automated test, V2 Android perlu checklist uji manual minimum:

- buyer happy path
- seller happy path
- auth failure dan expired token path
- forbidden path untuk role yang salah
- upload/render image base64 path
- empty data dan partial data path
- poor network, retry, dan recovery path

## Full Flow Verification Requirement

`Ai-agent` wajib menguji seluruh flow utama Android yang tersambung ke backend, bukan hanya endpoint terpisah.

Minimal flow yang harus dibuktikan berjalan:

- splash dan bootstrap config
- register, login, forgot password, verify OTP, reset password, change password, PIN flow
- customer profile, notification preferences, village, address, dan delete account
- buyer browse foods, food detail, cafe detail, search, cart, checkout session, create order, order detail, transfer confirmation
- buyer notifications dan buyer chat
- seller profile, create cafe, edit store, cafe address, payment methods, availability, product CRUD, seller orders, seller notifications, seller chat

Kriteria verifikasinya:

- semua fitur utama harus benar-benar bekerja pada runtime yang relevan
- semua API yang memang dipakai flow sukses harus mengembalikan success response yang sesuai kontrak
- semua API yang memang dipakai flow gagal harus mengembalikan error response yang benar sesuai kontrak backend
- tidak boleh ada halaman atau fitur yang technically terbuka tetapi secara fungsi tidak berjalan
- jika ada fitur yang belum bisa dibuktikan hidup, agent harus mencatatnya sebagai gap, bukan menganggapnya lolos

## Incident And Rollback Runbook

Untuk proyek besar, Android perlu runbook saat integrasi rusak:

- cara membedakan bug Android, bug kontrak, dan bug backend runtime
- cara mengumpulkan evidence minimum dari device/log/request terkait
- cara memverifikasi apakah bug terjadi di semua device atau hanya environment tertentu
- cara memvalidasi ulang terhadap backend Docker dan demo data terbaru
- langkah rollback atau disable release bila regression kritis ditemukan

## Deprecation Policy

V2 Android perlu aturan jika field atau flow lama mulai ditinggalkan:

- jangan hapus dukungan field/flow lama tanpa bukti bahwa backend aktif dan release Android sudah aman
- perubahan kontrak lama ke baru harus punya masa transisi yang jelas
- fallback kompatibilitas sementara harus disengaja dan terdokumentasi, bukan efek samping mapper

## Recommended Feature Areas

Area pengembangan yang bernilai tinggi setelah stabilitas runtime:

- order timeline yang lebih jelas untuk buyer dan seller
- notification deep-link ke order/chat terkait
- seller dashboard ringkas untuk status toko, produk aktif, dan order baru
- cache ringan untuk data yang sering dibuka seperti profile, cafe, atau product list
- analytics event internal untuk mendeteksi halaman yang sering gagal atau kosong

## Integration Guardrails

V2 Android harus menjaga aturan berikut:

- jangan menambah mock baru untuk flow yang sudah punya backend nyata
- setiap perubahan DTO atau mapper harus bisa dijelaskan terhadap payload backend aktif
- error message jangan disederhanakan sampai kehilangan konteks bisnis
- semua fitur utama buyer dan seller harus punya state kosong, state gagal, dan retry path
- perubahan base URL, auth, atau contract harus diuji kembali terhadap backend Docker
- semua field foto harus diperlakukan sebagai `base64 string` end-to-end kecuali kontrak backend resmi berubah
- setiap perubahan kontrak field, enum, atau nullability harus punya review kompatibilitas Android-backend
- halaman list tidak boleh mengasumsikan response tanpa paging akan selalu aman untuk jangka panjang
- state tombol dan aksi di UI harus mengikuti state bisnis backend, bukan asumsi lokal
- validasi integrasi wajib mencakup success response dan error response backend
- validasi integrasi wajib mencakup auth, role, token expiry, dan access boundary antara Android dan backend

## Contract Change Rules

Perubahan kontrak Android-backend harus mengikuti aturan ini:

- perubahan field, enum, atau nullability harus dianggap breaking sampai dibuktikan aman
- Android dan backend tidak boleh merge perubahan kontrak penting secara terpisah tanpa verifikasi silang
- source of truth kontrak tetap payload backend aktif yang telah tervalidasi
- perubahan kontrak harus disertai update test, verifier, atau bukti runtime yang relevan
- perubahan format media di masa depan harus melalui migration plan yang eksplisit, tidak boleh diam-diam mengubah `base64 string`

## Definition Of Done

Sebuah fitur integrasi Android dianggap selesai bila:

- build dan test yang relevan hijau
- contract check terhadap backend tetap hijau
- smoke flow nyata ke backend Docker tetap lolos
- state loading, empty, success, dan error tervalidasi
- tidak ada mock lokal yang menyembunyikan masalah backend nyata
- perilaku auth, state bisnis, dan pesan error sudah sesuai kontrak aktif
- error response penting backend sudah diuji dan dipetakan benar di Android
- boundary security antara endpoint publik, customer, seller, dan admin sudah tervalidasi terhadap backend nyata
- retry atau poor-network path untuk aksi penting sudah tervalidasi
- parsing waktu dan render waktu sudah tervalidasi terhadap payload backend aktif

## Release Gates Per Flow

Sebuah release integrasi Android tidak boleh dianggap aman bila salah satu flow inti berikut belum lolos:

- buyer auth, profile, village, address, cart, checkout, order, notification, dan chat
- seller profile, cafe, cafe address, payment methods, product, order, notification, dan chat
- validasi error response untuk flow buyer dan seller
- validasi security boundary untuk endpoint publik, customer, seller, dan admin

Selain itu:

- `ai-agent` harus menunjukkan bukti bahwa seluruh flow Android-backend utama telah diuji
- `ai-agent` harus memahami dependensi lintas project sebelum menutup pekerjaan
- tidak boleh ada API penting pada flow sukses yang masih gagal tanpa dicatat sebagai blocker

## Acceptance Criteria

- seluruh halaman utama buyer dan seller punya perilaku loading/error/empty yang jelas
- tidak ada fitur utama yang hanya "terlihat hidup" tetapi gagal saat memakai backend nyata
- error backend penting diterjemahkan ke UX Android secara tepat
- regression check Android terhadap backend tetap tersedia dan mudah dijalankan
- perubahan Android berikutnya bergerak dari baseline integrasi yang sudah tervalidasi, bukan dari asumsi lokal
