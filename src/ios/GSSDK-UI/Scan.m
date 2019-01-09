#import "Scan.h"
#import "UIImage-Extensions.h"
#import "EditFrameViewController.h"
#import "CameraViewController.h"

@implementation Scan

- (instancetype)init {
    self = [super init];

    unsigned long timestamp = [[NSDate date] timeIntervalSince1970];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths firstObject];
    self.originalImagePath = [documentsDirectory stringByAppendingPathComponent:[NSString stringWithFormat:@"%lu.jpg",timestamp]];
    self.rotation = 0;
    return self;
}

- (NSString *)filePath {
    return self.originalImagePath;
}

- (UIImage *)originalImage {
    return [UIImage imageWithContentsOfFile:self.originalImagePath];
}

- (void) rotateLeft {
    self.rotation -= M_PI_2;
}

- (void) rotateRight {
    self.rotation += M_PI_2;
}

- (NSString *) saveEnhancedFileToURI {
    NSTimeInterval timestamp = [[NSDate date] timeIntervalSince1970];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths firstObject];
    NSString *enhancedImagePath = [documentsDirectory stringByAppendingPathComponent:[NSString stringWithFormat:@"enhanced-%lu.jpg", (unsigned long)timestamp]];

    // Rotate image before saving
    UIImage *rotatedImage = [self.enhancedImage imageRotatedByRadians:self.rotation];

    [UIImageJPEGRepresentation(rotatedImage, 0.85) writeToFile:enhancedImagePath atomically:YES];
    NSString *resultFileUri = [[NSURL URLWithString:enhancedImagePath] path];

    return resultFileUri;
}
@synthesize orientation;

@end

@implementation ScanFactory

- (id<GSKScanProtocol>)createScan {
    return [[Scan alloc] init];
}

@end
