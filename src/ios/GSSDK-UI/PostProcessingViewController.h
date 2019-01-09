#import <UIKit/UIKit.h>
#import <GSSDK/GSSDK.h>
#import "Scan.h"

@class PostProcessingViewController;

@protocol PostProcessingViewControllerDelegate
- (void)postProcessingViewController:(PostProcessingViewController *)postProcessingViewController didFinishWithScan:(Scan *)scan;
@end

@class GSKQuadrangle;

@interface PostProcessingViewController : UIViewController
@property (nonatomic, strong) Scan *scan;
@property (nonatomic, strong) NSString *defaultEnhancement;
@property (nonatomic, weak) id<PostProcessingViewControllerDelegate> delegate;
@end
