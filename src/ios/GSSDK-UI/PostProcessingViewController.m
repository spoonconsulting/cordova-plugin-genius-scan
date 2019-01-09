#import "PostProcessingViewController.h"

@interface PostProcessingViewController ()
@property (nonatomic, strong) UIImageView *imageView;
@property (nonatomic, strong) UIBarButtonItem *editButton;
@end

@implementation PostProcessingViewController
- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor blackColor];
    self.imageView = [[UIImageView alloc] initWithFrame:self.view.bounds];
    self.imageView.autoresizingMask = UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight;
    self.imageView.contentMode = UIViewContentModeScaleAspectFit;
    [self.view addSubview:self.imageView];

    self.editButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemEdit target:self action:@selector(_edit)];

    self.navigationItem.rightBarButtonItems = @[
        [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(_done)],
        self.editButton,
        [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemRefresh target:self action:@selector(_rotateRight)]
    ];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.navigationController.navigationBar.translucent = NO;
    [self.navigationController setNavigationBarHidden:NO animated:NO];
    [self _refreshImageView];

    if ([self.defaultEnhancement isEqual: @"bw"])
         [self _processImageWithPostProcessingType:GSKPostProcessingTypeBlackAndWhite autoDetect:NO];
    else if ([self.defaultEnhancement isEqual: @"color"])
        [self _processImageWithPostProcessingType:GSKPostProcessingTypeColor autoDetect:NO];
    else if ([self.defaultEnhancement isEqual: @"whiteboard"])
        [self _processImageWithPostProcessingType:GSKPostProcessingTypeWhiteboard autoDetect:NO];
    else
        [self _processImageWithPostProcessingType:GSKPostProcessingTypeNone autoDetect:YES];
}

#pragma mark - Private

- (void)_edit {
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:NSLocalizedStringFromTable(@"Enhancement", @"GSSDK", @"") message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    alertController.modalPresentationStyle = UIModalPresentationPopover;
    UIPopoverPresentationController* popPresentationController = alertController.popoverPresentationController;
    popPresentationController.permittedArrowDirections = UIPopoverArrowDirectionAny;
    popPresentationController.barButtonItem = self.editButton;

    [alertController addAction:[UIAlertAction actionWithTitle:NSLocalizedStringFromTable(@"None", @"GSSDK", @"") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [self _processImageWithPostProcessingType:GSKPostProcessingTypeNone autoDetect:NO];
    }]];
    [alertController addAction:[UIAlertAction actionWithTitle:NSLocalizedStringFromTable(@"Black and White", @"GSSDK", @"") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [self _processImageWithPostProcessingType:GSKPostProcessingTypeBlackAndWhite autoDetect:NO];
    }]];
    [alertController addAction:[UIAlertAction actionWithTitle:NSLocalizedStringFromTable(@"Photo", @"GSSDK", @"") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [self _processImageWithPostProcessingType:GSKPostProcessingTypeColor autoDetect:NO];
    }]];
    [alertController addAction:[UIAlertAction actionWithTitle:NSLocalizedStringFromTable(@"Color", @"GSSDK", @"") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [self _processImageWithPostProcessingType:GSKPostProcessingTypeWhiteboard autoDetect:NO];
    }]];
    [self presentViewController:alertController animated:YES completion:nil];
}

- (void)_done {
    [self dismissViewControllerAnimated:YES completion:nil];
    [self.delegate postProcessingViewController:self didFinishWithScan:self.scan];
}

- (void)_rotateLeft {
    [self.scan rotateLeft];
    [self _refreshImageView];
}

- (void)_rotateRight {
    [self.scan rotateRight];
    [self _refreshImageView];
}

/// This applies the powerful SDK image processing methods:
/// - correct the perspective of the scan with the quadrangle set at the previous set
/// - attempt to detect the best post-processing, or use the user defined post-processing
/// - enhance the image according to this post-processing
- (void)_processImageWithPostProcessingType:(GSKPostProcessingType)postProcessingType autoDetect:(BOOL)autodetect {
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
        UIImage *warpedImage = [GSK warpImage:self.scan.originalImage withQuadrangle:self.scan.quadrangle];

        GSKPostProcessingType bestPostProcessing = autodetect ? [GSK bestPostProcessingForImage:warpedImage] : postProcessingType;
        self.scan.enhancedImage = [GSK enhanceImage:warpedImage withPostProcessing:bestPostProcessing];

        dispatch_async(dispatch_get_main_queue(), ^{
            [self _refreshImageView];
        });
    });
}

- (void)_refreshImageView {
    self.imageView.transform = CGAffineTransformMakeRotation(self.scan.rotation);
    self.imageView.image = self.scan.enhancedImage;
    // Resize image view to fit the parent view
    [self.imageView setFrame:self.view.bounds];
}

@end
