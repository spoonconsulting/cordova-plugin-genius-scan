# Genius Scan SDK Cordova plugin

## Description

This Cordova plugin allows you to access the [Genius Scan SDK](https://www.thegrizzlylabs.com/document-scanner-sdk) core features from a Cordova application:

  - Automatic document detection
  - Document perspective correction
  - Image enhancement with 4 different modes (Grayscale, Color, Black & white, Photo)


## Licence

This plugin is based on the Genius Scan SDK for which you need to [setup a licence](#set-the-licence-key).
You can aleady try the "demo" version for free by not setting a licence key, the only limitation being that the app will exit after 60 seconds.

To buy a licence or for any question regarding the SDK, please contact us at sdk@thegrizzlylabs.com!

## Demo application

As an example, you can check our [demo application](https://github.com/thegrizzlylabs/geniusscan-sdk-demo/tree/master/cordova-plugin-genius-scan-demo)

## Requirements

This plugin requires `cordova-cli` above 7.0.0: see [this page](https://cordova.apache.org/docs/en/latest/guide/cli/index.html#updating-cordova-and-your-project) for upgrading if you have an older version.

### Platform-specific requirements

- **iOS**: `cordova-ios` above 4.3.0
  Nothing to do on a new project; use `cordova platform update ios --save` within your existing cordova project to upgrade it.
- **Android**: `cordova-android` at least 6.x.x

**Note for Xcode 10 users**: [Cordova-ios is not fully compatible with Xcode 10 yet](https://github.com/apache/cordova-ios/issues/407), you may need to use `cordova run ios --buildFlag="-UseModernBuildSystem=0"` command to build the project properly

## Upgrade from an older version

If you are using an older plugin version, you will first need to remove the plugin from your cordova project:

```
cordova plugin remove @thegrizzlylabs/cordova-plugin-genius-scan
```

Then you can install the new version following the instructions below.

## Installation

You can install the plugin with Cordova CLI, plugman, or using the `config.yml` file.

**Note for Phonegap, Ionic and other cordova-based tools:**

You can usually run cordova commands from the `phonegap`/`ionic` cli.
- `ionic cordova prepare`
- `ionic cordova plugin add ....`
- `phonegap cordova prepare` (or, shorter `phonegap prepare`)
- ...

### With Cordova CLI

From your Cordova project folder, install the plugin with the following command:

```bash
cordova plugin add @thegrizzlylabs/cordova-plugin-genius-scan
```

### With config.yml

Add the following lines to your project's config.yml file:

```xml
<plugin name="@thegrizzlylabs/cordova-plugin-genius-scan" spec="~2.3.0"/>
```

And run `cordova prepare`.

### With plugman

From your Cordova project folder, you can also use **plugman** to install the plugin only for a specific platform.

```bash
plugman install --platform ios --project ./platforms/ios --plugin @thegrizzlylabs/cordova-plugin-genius-scan
```

## Usage

Once the `deviceReady` Cordova event has been fired, the following method will be available:

### Set the licence key
```javascript
cordova.plugins.GeniusScan.setLicenceKey(licenceKey, onSuccess, onFail)
```

Initialize the SDK with a valid licence key.
Note that, for testing purpose, you can also use the plugin without a licence key, but it will only work for 60 seconds.

| Param | Type | Description |
| --- | --- | --- |
| licenceKey | String | Genius scan licence key for your application id |
| onSuccess | Function | Success callback function, called when key is valid |
| onFail | Function | [Error callback](#error-callback)|


### Scan an existing image
```javascript
cordova.plugins.GeniusScan.scanImage(imageFileUri, onSuccess, onFail, scanOptions)
```

| Param | Type | Description |
| --- | --- | --- |
| imageFileUri | String | file URI to the original JPEG file to be transformed with the plugin (Note: it won't be overriden by the plugin) |
| onSuccess(fileUri) | Function | Callback function, called with the file URI of the processed JPEG output |
| onFail | Function | [Error callback](#error-callback)|
| scanOptions | Object | [Scan Options](#scan-options) |

### Scan a picture from the camera
```javascript
cordova.plugins.GeniusScan.scanCamera(onSuccess, onFail, scanOptions)
```

| Param | Type | Description |
| --- | --- | --- |
| onSuccess(fileUri) | Function | Callback function, called with the file URI of the processed JPEG output |
| onFail | Function | [Error callback](#error-callback)|
| scanOptions | Object | [Scan Options](#scan-options) |

### Generate a pdf from images
```javascript
cordova.plugins.GeniusScan.generatePDF(imageFileUris, onSuccess, onFail, pdfOptions)
```

| Param | Type | Description |
| --- | --- | --- |
| imageFileUris | Array<String> | Array of URIs to JPEG files that will be converted, in the given order, into a pdf
| onSuccess(fileUri) | Function | Callback function, called with the file URI of the resulting PDF |
| onFail | Function | [Error callback](#error-callback)|
| pdfOptions | Object | Options for pdf generation |
| pdfOptions.password | String | Password to protect the pdf |

### Scan options

`scanOptions` can be used to customize the scanning interface:

| Param | Type | Description |
| --- | --- | --- |
| scanOptions.defaultEnhancement | String | Force a specific image enhancement by default. Accepted values: `cordova.plugins.GeniusScan.ENHANCEMENT_NONE`, `cordova.plugins.GeniusScan.ENHANCEMENT_BW`, `cordova.plugins.GeniusScan.ENHANCEMENT_COLOR`, `cordova.plugins.GeniusScan.ENHANCEMENT_PHOTO` |

### Error callback

In case of failure, error callback function is called with an error message as a string

## Usage with Ionic

You can use this plugin with any Cordova-based framework, for example Ionic.
The way to detect that the device is ready is slightly different, but after that you also access the plugin with **cordova.plugins.GeniusScan.scanImage(imageFileUri, onSuccess, onFail)**

In the controller where you need the plugin, you will have to import `Platform` and pass it to the constructor, and also declare the `cordova` variable so that TypeScript recognizes it.

```javascript
import { Platform } from 'ionic-angular';

declare var cordova:any;

export class HomePage {
  constructor(private platform: Platform) {
    platform.ready().then(() => {
      // platform.ready is the equivalent of the deviceReady event described above
      // the plugin method is now available:
      cordova.plugins.GeniusScan.scanCamera(...)
    });
  }

}

```

# FAQ

## What if I get a validation error from App Store Connect?

You must remove the x86_64 and i386 slices before submitting your application to the App Store. They are only used for the iOS smiulator and iTunes rejects any binary that contains non-ARM slices.

They can stripped out with a script like [this one](https://stackoverflow.com/a/42642209/1644052).

## What should I do if my license is invalid?

Make sure you have an ongoing contract with us. Contact us at sdk@thegrizzlylabs.com for any information.

# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.4.2] - 2019-02-27
### Changed
- Compatibility with android-cordova >= v7.0.0

### Added
- `ENHANCEMENT_NONE` default enhancement option
- Cancel button on iOS camera screen
- Auto trigger in Android

## [2.4.1] - 2018-12-05
### Changed
- Fix README

## [2.4.0] - 2018-12-05
### Added
- PDF generation with `generatePDF`
- Selecting an enhancement type by default with `defaultEnhancement` option

### Changed
- Fix crash on ipad when clicking "Edit" button
- Fix error with too long activity result code
