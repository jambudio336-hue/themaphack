# Native UI Demo

This is an isolated, benign Android UI demo added separately from the native game-mod source.

## What it does
- Builds a normal Android APK with local UI switches.
- Demonstrates local status and UI state changes.
- Does not connect to Mobile Legends, inspect another app's memory, inject libraries, or implement maphack/ESP.

## Build
The GitHub Actions workflow `.github/workflows/android-demo.yml` builds a debug APK and uploads it as an artifact. To publish a demo release, create and push a tag matching `demo-v*` (for example, `demo-v1.0.0`).

The first workflow run must pass before an APK can be considered available. The debug APK is signed with Android's debug key and is intended for testing, not production distribution.
