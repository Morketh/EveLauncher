# EVE Launcher

A minimal Android app that plays a welcome sound on launch and exits immediately, letting Android route audio to the active output (Bluetooth, USB, or Android Auto).  

This app is designed for **Android 14+ (targetSDK 34)** and Samsung/modern devices, with full foreground service compliance.

## Features

- Plays a single `eve_welcome.mp3` sound from `res/raw/`.
- Uses proper `MediaPlayer` audio attributes so **Android automatically handles audio routing**.
- No UI — the app launches, plays the sound, and exits cleanly.
- Fully compatible with **Bluetooth headsets, USB audio, and Android Auto**.
- Minimal dependencies and code — easy to tweak or extend.

## Installation

1. Clone this repository:
   ```
   git clone https://github.com/YOUR_USERNAME/eve-launcher.git
   ```
2. Open in **Android Studio**.
3. Place your `eve_welcome.mp3` in `app/src/main/res/raw/`.
4. Build and run on a device with Android 14+ (or newer).

## Files

- `MainActivity.java` — launches the `SoundService` and exits immediately.
- `SoundService.java` — plays the sound, handles MediaPlayer lifecycle, and stops itself.
- `AndroidManifest.xml` — includes required permissions and foreground service type.
- `res/raw/eve_welcome.mp3` — welcome audio file.

## Notes

- **Spotify / Android Auto** integration is handled externally. This app does **not launch Spotify**; it only plays the sound.
- Make sure your device allows foreground services and media playback permissions.
- Tested on **Samsung Galaxy S24 Ultra**, Android 16, OneUI 8.0.

## License

MIT License — free to use, modify, and distribute.

