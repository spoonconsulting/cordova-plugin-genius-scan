#import "Scan.h"
#import "UIImage-Extensions.h"

@implementation Scan

+ (NSString*) convertFilePathToFileUri:(NSString*)filePath {
    return [[NSURL fileURLWithPath:filePath] absoluteString];
}
+ (NSString*) convertFileUriToFilePath:(NSString*)fileUri {
    return [[NSURL URLWithString:fileUri] path];
}

+ (Scan*)initWithFileUri:(NSString*)originalImageeUri {
    Scan *instance = [[Scan alloc] init];

    NSString *originalImagePath = [Scan convertFileUriToFilePath:originalImageeUri];
    instance.originalImage = [UIImage imageWithContentsOfFile:originalImagePath];
    instance.rotation = 0;
    return instance;
}

- (void) rotateLeft {
    self.rotation -= M_PI_2;
}

- (void) rotateRight {
    self.rotation += M_PI_2;
}

- (void)saveAndSendCallback {
    NSTimeInterval timestamp = [[NSDate date] timeIntervalSince1970];;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES);
    NSString *libPath = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"NoCloud"];
    NSString *enhancedImagePath = [libPath stringByAppendingPathComponent:[NSString stringWithFormat:@"enhanced-%lu.jpg", (unsigned long)timestamp]];

    // Rotate image before saving
    UIImage *rotatedImage = [self.enhancedImage imageRotatedByRadians:self.rotation];

    [UIImageJPEGRepresentation(rotatedImage, 0.85) writeToFile:enhancedImagePath atomically:YES];
    
    NSString *resultFileUri = [Scan convertFilePathToFileUri:enhancedImagePath];
    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:resultFileUri];
    [self.commandDelegate sendPluginResult:result callbackId:self.callbackId];
}

- (void)cancelCallback {
    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:nil];

    [self.commandDelegate sendPluginResult:result callbackId:self.callbackId];
}
@end
