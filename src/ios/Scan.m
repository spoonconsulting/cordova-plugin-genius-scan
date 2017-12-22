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

- (UIImage*)imageByScalingNotCroppingForSize:(UIImage*)anImage toSize:(CGSize)frameSize
{
    UIImage* sourceImage = anImage;
    UIImage* newImage = nil;
    CGSize imageSize = sourceImage.size;
    CGFloat width = imageSize.width;
    CGFloat height = imageSize.height;
    CGFloat targetWidth = frameSize.width;
    CGFloat targetHeight = frameSize.height;
    CGFloat scaleFactor = 0.0;
    CGSize scaledSize = frameSize;
    
    if (CGSizeEqualToSize(imageSize, frameSize) == NO) {
        CGFloat widthFactor = targetWidth / width;
        CGFloat heightFactor = targetHeight / height;
        
        // opposite comparison to imageByScalingAndCroppingForSize in order to contain the image within the given bounds
        if (widthFactor == 0.0) {
            scaleFactor = heightFactor;
        } else if (heightFactor == 0.0) {
            scaleFactor = widthFactor;
        } else if (widthFactor > heightFactor) {
            scaleFactor = heightFactor; // scale to fit height
        } else {
            scaleFactor = widthFactor; // scale to fit width
        }
        scaledSize = CGSizeMake(floor(width * scaleFactor), floor(height * scaleFactor));
    }
    
    UIGraphicsBeginImageContextWithOptions(scaledSize, YES, 1.0); // this will resize
    
    [sourceImage drawInRect:CGRectMake(0, 0, scaledSize.width, scaledSize.height)];
    
    newImage = UIGraphicsGetImageFromCurrentImageContext();
    if (newImage == nil) {
        NSLog(@"could not scale image");
    }
    
    // pop the context to get back to the default
    UIGraphicsEndImageContext();
    return newImage;
}

- (void)saveAndSendCallback {
    NSTimeInterval timestamp = [[NSDate date] timeIntervalSince1970];;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES);
    NSString *libPath = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"NoCloud"];
    NSString *enhancedImagePath = [libPath stringByAppendingPathComponent:[NSString stringWithFormat:@"enhanced-%lu.jpg", (unsigned long)timestamp]];

    // Rotate image before saving
    UIImage *rotatedImage = [self.enhancedImage imageRotatedByRadians:self.rotation];

    [UIImageJPEGRepresentation(rotatedImage, 0.85) writeToFile:enhancedImagePath atomically:YES];
    
    //save also the thumbnail
    UIImage* scaledImage = [self imageByScalingNotCroppingForSize:rotatedImage toSize:CGSizeMake(370, 370)];
    NSString*thumbName= [NSString stringWithFormat:@"thumb_%@", [enhancedImagePath lastPathComponent]];
    NSString*thumb=[enhancedImagePath stringByReplacingOccurrencesOfString:[enhancedImagePath lastPathComponent] withString:thumbName];
    [UIImageJPEGRepresentation(scaledImage, 0.85) writeToFile:thumb atomically:YES];
    
    NSString *resultFileUri = [Scan convertFilePathToFileUri:enhancedImagePath];
    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:resultFileUri];
    [self.commandDelegate sendPluginResult:result callbackId:self.callbackId];
}

- (void)cancelCallback {
    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_NO_RESULT messageAsString:@"Canceled"];

    [self.commandDelegate sendPluginResult:result callbackId:self.callbackId];
}
@end
