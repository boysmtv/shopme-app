#!/bin/bash
set -e

# 1. Tentukan Root Proyek
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$REPO_ROOT"

echo "Repo Root: $REPO_ROOT"

# 2. Cek Ruby
if ! command -v ruby &> /dev/null; then
    echo "Error: Ruby tidak ditemukan di PATH."
    echo "Silakan install Ruby melalui rbenv, asdf, atau installer resmi."
    exit 1
fi

# 3. Isolasi Gems ke dalam folder .vendor/bundle di proyek
# Ini agar library Fastlane tidak bentrok antar proyek
export GEM_HOME="$REPO_ROOT/.vendor/bundle"
export PATH="$GEM_HOME/bin:$PATH"

# 4. Verifikasi & Persiapan
echo "Ruby version: $(ruby -v)"

if ! command -v bundle &> /dev/null; then
    echo "Installing Bundler..."
    gem install bundler --no-document
fi

echo ""
echo "Fastlane ready."
echo "Langkah selanjutnya:"
echo "  1. Jalankan 'bundle install' untuk pasang dependensi."
echo "  2. Jalankan 'bundle exec fastlane android deploy_debug'"
