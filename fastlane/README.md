fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android firebase

```sh
[bundle exec] fastlane android firebase
```

Build release APK and upload it to Firebase App Distribution

### android firebase_debug

```sh
[bundle exec] fastlane android firebase_debug
```

Build debug APK with Chucker and upload it to Firebase App Distribution

### android deploy

```sh
[bundle exec] fastlane android deploy
```

Alias for firebase deployment

### android deploy_debug

```sh
[bundle exec] fastlane android deploy_debug
```

Alias for debug Firebase deployment with Chucker

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
