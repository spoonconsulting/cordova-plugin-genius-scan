#import <GSSDK/GSSDK.h>
#import "Scan.h"

@class EditFrameViewController;

@protocol EditFrameViewControllerDelegate
- (void)editFrameViewController:(EditFrameViewController *)editFrameViewController didFinishWithScan:(Scan *)scan;
- (void)viewControllerDidCancel:(UIViewController *)viewController;
@end

@interface EditFrameViewController : GSKEditFrameViewController
@property (nonatomic, strong) Scan *scan;
@property BOOL showCancel;

@property (nonatomic, weak) id<EditFrameViewControllerDelegate> delegate;
@end
