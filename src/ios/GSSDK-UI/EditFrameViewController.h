#import <GSSDK/GSSDK.h>
#import "Scan.h"

@class EditFrameViewController;

@protocol EditFrameViewControllerDelegate
- (void)editFrameViewController:(EditFrameViewController *)editFrameViewController didFinishWithScan:(Scan *)scan;
- (void)editFrameViewController:(EditFrameViewController *)editFrameViewController didCancelWithScan:(Scan *)scan;
@end

@interface EditFrameViewController : GSKEditFrameViewController
@property (nonatomic, strong) Scan *scan;

@property (nonatomic, weak) id<EditFrameViewControllerDelegate> delegate;
@end
