# Panduan Setup Fastlane - Shopme Android

Dokumentasi ini menjelaskan cara melakukan setup environment Fastlane agar dapat dijalankan secara langsung dengan perintah `fastlane deploy` di Windows.

## 1. Persiapan Ruby (Sekali Saja)
Pastikan Ruby sudah terinstall di folder `C:\Ruby40-x64`. Jika belum ada, silakan pindahkan folder Ruby Anda ke lokasi tersebut.

## 2. Setup System Environment (Agar bisa dipanggil di mana saja)
Anda perlu mendaftarkan Ruby ke dalam **System PATH** Windows:
1. Klik **Start**, cari **"Edit the system environment variables"**.
2. Klik **Environment Variables**.
3. Di bagian **System variables**, cari variable **Path**, lalu klik **Edit**.
4. Tambahkan dua baris baru berikut:
   - `C:\Ruby40-x64\bin`
   - `C:\Ruby40-x64\msys64\mingw64\bin`
5. Klik **OK** pada semua jendela.

## 3. Install Dependency Global
Buka PowerShell sebagai **Administrator**, lalu jalankan perintah berikut untuk menginstall engine utama Fastlane:

```powershell
# Install Fastlane tanpa dokumentasi (untuk menghindari error long path di Windows)
gem install fastlane --no-document
gem install bundler --no-document
```

## 4. Setup Dependency Proyek
Masuk ke root folder proyek `shopme-app`, lalu jalankan perintah berikut untuk menginstall semua plugin yang dibutuhkan (Firebase App Distribution & Versioning):

```powershell
bundle install
```

## 5. Cara Menjalankan
Setelah setup selesai, Anda tidak perlu lagi menjalankan script `.ps1`. Cukup buka terminal di root proyek dan ketik:

```powershell
# Untuk deploy dengan menu interaktif (Seperti standar Maybank)
fastlane deploy
```

---

### Tips Troubleshooting:
- **Project not linked to Google Play**: Jika memilih format AAB, pastikan Firebase sudah terhubung ke Google Play Console. Jika belum, pilih format **APK** saat muncul pilihan di menu.
- **GemNotFound**: Jika muncul error gem tidak ditemukan, pastikan Anda sudah menjalankan `bundle install` di dalam folder proyek.
- **UTF-8 Warning**: Jika muncul warning locale, pastikan terminal Anda mendukung UTF-8 (sudah otomatis diatur jika menggunakan script `fastlane-env.ps1` sebelumnya).
