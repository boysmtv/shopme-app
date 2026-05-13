$ErrorActionPreference = "Stop"

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
$rubyRoot = "D:\Boys\Ai\ai-software-factory\tools\ruby-devkit-3.4.7-1-x64"
$gemHome = "D:\Boys\Ai\ai-software-factory\tools\ruby-devkit-gems"
$fastlaneHome = "D:\Boys\Ai\ai-software-factory\tools\fastlane-home"

if (-not (Test-Path (Join-Path $rubyRoot "bin\ruby.exe"))) {
    throw "Ruby DevKit not found at $rubyRoot"
}

New-Item -ItemType Directory -Force -Path $fastlaneHome | Out-Null

$env:GEM_HOME = $gemHome
$env:GEM_PATH = $gemHome
$env:HOME = $fastlaneHome
$env:USERPROFILE = $fastlaneHome
$env:FASTLANE_OPT_OUT_USAGE = "1"
$env:FASTLANE_SKIP_UPDATE_CHECK = "1"
$env:Path = "$gemHome\bin;$rubyRoot\bin;$rubyRoot\msys64\mingw64\bin;$rubyRoot\msys64\usr\bin;$env:Path"

Set-Location $repoRoot

Write-Host "Ruby:" (& ruby -v)
Write-Host "Bundler:" (& bundle -v)
Write-Host ""
Write-Host "Fastlane ready. Example:"
Write-Host "  bundle exec fastlane android deploy"
