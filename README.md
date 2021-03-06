Velad
=====

__Description:__

Velad brings an agile tool to do daily autoevaluations, allowing you to register your activities and showing advancement and spiritual growth indicators. Please visit [Google Play](TBD) for more information about this app.

__Source code:__

Velad is open source because we believe in the open source philosophy. If you would like to contribute to this project, please get in touch [with us](mailto:mlopez@avanzadacatolica.org) first.

__Requirements:__

* JDK 1.7.0_79
* Gradle 2.8
* Android SDK

__Building:__

Velad uses Gradle as build system. In order to build this project, define the Android SDK location with sdk.dir by creating a local.properties file or with an ANDROID_HOME environment variable. Then run:

```
gradle assembleDebug
```

You'll find the debug APK in `Velad-Android/app/build/outputs/apk` as `app-debug.apk`.

__Tests:__

TBD

__Packing:__

In order to pack the project (generate the apk file to upload to Google Play) you'll need to follow the instruction on the "Building" section, afterwards create a `release.properties` file with the required information (check the sample) and run:

```
gradle assembleRelease
```

You'll find the release APK in `Velad-Android/app/build/outputs/apk` as `app-release.apk`.