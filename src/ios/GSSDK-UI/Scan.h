#import <GSSDK/GSSDK.h>

@interface Scan : NSObject <GSKScanProtocol>
@property CGFloat rotation;

@property (nonatomic, strong) NSString *originalImagePath;
- (UIImage *)originalImage;

@property (nonatomic, strong) UIImage *enhancedImage;
@property (nonatomic, strong) GSKQuadrangle *quadrangle;

- (void)rotateLeft;
- (void)rotateRight;
- (NSString*)saveEnhancedFileToURI;
@end

/**
 An implementation of the GSKScanFactoryProtocol to return our concrete Scan object.
 */
@interface ScanFactory : NSObject <GSKScanFactoryProtocol>
@end
