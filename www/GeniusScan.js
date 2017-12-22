var exec = require('cordova/exec');

exports.scan = function(imageFileUri, success, error) {
    exec(success, error, "GeniusScan", "scan", [imageFileUri]);
};
