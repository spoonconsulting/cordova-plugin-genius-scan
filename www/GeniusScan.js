// Note: we avoid ES6 in cordova JS, because underlying webview might not be compatible.
var exec = require("cordova/exec");

exports.scanImage = function(imageFileUri, onSuccess, onError, scanOptions) {
  scanOptions = scanOptions || {}
  exec(onSuccess, onError, "GeniusScan", "scanImage", [imageFileUri, scanOptions]);
};

exports.scanCamera = function(onSuccess, onError, scanOptions) {
  scanOptions = scanOptions || {}
  exec(onSuccess, onError, "GeniusScan", "scanCamera", [scanOptions]);
};

exports.generatePDF = function(title, imageFileUris, onSuccess, onError, pdfOptions) {
  pdfOptions = pdfOptions || {}
  exec(onSuccess, onError, "GeniusScan", "generatePDF", [title, imageFileUris, pdfOptions]);
};

exports.setLicenceKey = function(licenceKey, onSuccess, onError) {
  exec(onSuccess, onError, "GeniusScan", "setLicenceKey", [licenceKey]);
};

exports.ENHANCEMENT_NONE = 'none'
exports.ENHANCEMENT_BW = 'bw'
exports.ENHANCEMENT_PHOTO = 'color'
exports.ENHANCEMENT_COLOR = 'whiteboard'