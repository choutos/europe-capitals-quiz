# Capital Quest 🧭

A small, offline Android game for learning European countries and capitals. Pick the capital for a country, pick the country for a capital, or play a mixed round. Four choices per question; ten questions per round, with immediate feedback and no repeated place within a deck. The interface and quiz answers are available in English and German: the initial language follows the device locale, and the EN/DE control switches languages in-app. Switching languages starts a fresh round.

## Scope

The built-in list has **51 places**: sovereign states wholly or partly in geographical Europe, plus Cyprus (often grouped with Europe) and Kosovo (partially recognised). This intentionally includes the UK, non-EU Balkan states, microstates, Russia, Türkiye, and the South Caucasus; Kazakhstan is included because a portion lies west of the Ural River. Kosovo's inclusion is a geography-game choice, not a statement on recognition. Capital names use familiar English forms; Netherlands uses its constitutional capital Amsterdam (not the government seat The Hague), Switzerland uses Bern (federal city), and Vatican City uses Vatican City. See [Europe's country overview](https://www.britannica.com/place/Europe) and the [CIA World Factbook country listings](https://www.cia.gov/the-world-factbook/countries/) for background.

## Build

Requires JDK 17 and Android SDK platform 35. Run `./gradlew testDebugUnitTest assembleDebug`. Install `app/build/outputs/apk/debug/app-debug.apk` on Android 7.0 or later. GitHub Actions runs the tests and publishes a debug APK as a workflow artifact on every push.

## Design

Native Kotlin + Jetpack Compose and Material 3. No network permission, accounts, ads, analytics, or runtime data collection. Quiz state lives in memory; starting a new round or changing modes resets the score. Source code licensed under MIT.
