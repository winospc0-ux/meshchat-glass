# meshchat

A glassmorphism mesh chat app for Android — private, serverless messaging over Bluetooth mesh with Nostr fallback, no accounts, no phone numbers.

## Credit

Based on [bitchat-android](https://github.com/permissionlesstech/bitchat-android) by permissionlesstech, licensed under GPL-3.0 (see LICENSE.md). This is an independent repo, not a fork: the mesh, encryption, and protocol code comes from upstream; the UI is a full-glass redesign built under the anti-slop filter (see DESIGN.md).

## Build

Binary assets (launcher icons, fonts, native libraries, Gradle wrapper jar) are not stored in this repo. The GitHub Actions workflow fetches them from the pinned upstream commit at build time, so a push builds a complete APK automatically. For a local build, copy those files from upstream first (see `.github/workflows/android.yml`).
