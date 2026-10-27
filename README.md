# Capital Quest 🧭

A small, offline Android game for learning European countries and capitals. Pick the capital for a country, pick the country for a capital, or play a mixed round. Four choices per question; ten questions per round, with immediate feedback and no repeated place within a deck. The interface and quiz answers are available in English and German: the initial language follows the device locale, and the EN/DE control switches languages in-app. Switching languages starts a fresh round.

## Scope

The built-in list has **51 places**: the 44 sovereign states in the [UN M49 Europe grouping](https://unstats.un.org/unsd/methodology/m49/overview/), plus Cyprus, Türkiye, Armenia, Azerbaijan, Georgia, Kazakhstan, and Kosovo as explicit edge cases. This intentionally includes the UK, non-EU Balkan states, and microstates. Kazakhstan is included because a portion lies west of the Ural River; the South Caucasus and Cyprus have varying Europe/Asia classifications. Kosovo's inclusion is a geography-game choice, not a statement on recognition. Dependent territories are not in this sovereign-state quiz. Capital names use familiar English forms; Netherlands uses its constitutional capital Amsterdam (not the government seat The Hague), Switzerland uses Bern (federal city), and Vatican City uses Vatican City. Capital cross-check: [WorldAtlas European capitals](https://www.worldatlas.com/articles/capital-cities-of-europe.html); [Kosovo status note](https://www.eeas.europa.eu/kosovo/eu-and-kosovo_en?s=321).

## Build

Requires JDK 17 and Android SDK platform 35. Run `./gradlew testDebugUnitTest assembleDebug`. Install `app/build/outputs/apk/debug/app-debug.apk` on Android 7.0 or later. GitHub Actions runs the tests and publishes a debug APK as a workflow artifact on every push.

## Design

Native Kotlin + Jetpack Compose and Material 3. No network permission, accounts, ads, analytics, or runtime data collection. Quiz state lives in memory; starting a new round or changing modes resets the score. Source code licensed under MIT.
