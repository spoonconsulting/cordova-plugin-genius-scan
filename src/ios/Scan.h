#import <GSSDK/GSSDK.h>
#import <Cordova/CDV.h>

@interface Scan : NSObject
@property (nonatomic, weak) id <CDVCommandDelegate> commandDelegate;
@property (nonatomic, strong) NSString *callbackId;

@property CGFloat rotation;

@property (nonatomic, strong) UIImage *originalImage;
@property (nonatomic, strong) UIImage *enhancedImage;
@property (nonatomic, strong) GSKQuadrangle *quadrangle;

+ (Scan *)initWithFileUri:(NSString *)originalFileUri;
- (void)rotateLeft;
- (void)rotateRight;
- (void)saveAndSendCallback;
- (void)cancelCallback;

+ (NSString*) convertFilePathToFileUri:(NSString*)filePath;
+ (NSString*) convertFileUriToFilePath:(NSString*)fileUri;
@end
