$ErrorActionPreference = "Stop"

# 1. Tentukan Root Proyek
$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
Set-Location $repoRoot

Write-Host "Repo Root: $repoRoot"

# 2. Cari Ruby (Cek di PATH sistem dulu, jika tidak ada baru gunakan fallback)
$rubyExe = Get-Command ruby -ErrorAction SilentlyContinue
if ($null -eq $rubyExe) {
    # Fallback lokasi umum Ruby portable di Windows
    $possiblePaths = @("C:\Ruby40-x64", "C:\Ruby33-x64", "C:\Ruby32-x64", "C:\Ruby")
    $found = $false
    foreach ($p in $possiblePaths) {
        if (Test-Path "$p\bin\ruby.exe") {
            $rubyRoot = $p
            $env:Path = "$rubyRoot\bin;$rubyRoot\msys64\mingw64\bin;$rubyRoot\msys64\usr\bin;$env:Path"
            Write-Host "Using Ruby fallback from: $rubyRoot"
            $found = $true
            break
        }
    }
    if (-not $found) {
        Write-Error "Ruby tidak ditemukan di PATH atau lokasi standar. Silakan install Ruby atau tambahkan ke PATH."
        exit 1
    }
}

# 3. Isolasi Environment (Gems disimpan di folder .vendor proyek agar portable)
$vendorDir = Join-Path $repoRoot ".vendor\bundle"
if (-not (Test-Path $vendorDir)) {
    New-Item -ItemType Directory -Force -Path $vendorDir | Out-Null
}

# Set GEM_HOME ke folder lokal proyek
$env:GEM_HOME = $vendorDir
$env:GEM_PATH = $vendorDir
$env:Path = "$vendorDir\bin;$env:Path"

# Tambahkan standar UTF-8 agar Fastlane tidak komplain
$env:LC_ALL = "en_US.UTF-8"
$env:LANG = "en_US.UTF-8"

# 4. Verifikasi
Write-Host "Ruby Version:" (& ruby -v)

# Cek Bundler
if (-not (Get-Command bundle -ErrorAction SilentlyContinue)) {
    Write-Host "Installing Bundler..."
    gem install bundler --no-document
}

# 5. Tambahkan Fungsi Global agar bisa panggil 'fastlane' langsung
# Hapus alias lama jika ada agar tidak bentrok (tanpa parameter -Scope yang bermasalah)
if (Test-Path Alias:fastlane) {
    Remove-Item Alias:fastlane -Force -ErrorAction SilentlyContinue
}

function global:fastlane {
    & bundle exec fastlane $args
}

Write-Host ""
Write-Host "Fastlane ready. Silakan jalankan perintah:"
Write-Host "  fastlane deploy"
