#import "EditFrameViewController.h"
#import "PostProcessingViewController.h"

@implementation EditFrameViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    self.view.backgroundColor = [UIColor blackColor];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.image = self.scan.originalImage;
    self.quadrangle = self.scan.quadrangle;
    self.navigationController.navigationBar.translucent = NO;
    [self.navigationController setNavigationBarHidden:NO animated:NO];

    self.navigationItem.rightBarButtonItems = @[
        [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(_done)],
        [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(_cancel)],
    ];

    if (self.scan.quadrangle != nil) {
        self.quadrangle = self.scan.quadrangle;
    } else {
      NSLog(@"Detecting quadrangle…");
      /// We use the SDK method to autodetect the quadrangle and show the result to the user
      dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
          GSKQuadrangle *quadrangle = [GSK detectQuadrangleFromImage:self.image options:GSKDetectQuadrangleOptionsNone];
          dispatch_async(dispatch_get_main_queue(), ^{
              NSLog(@"Quadrangle analysis finished. Found: %@", quadrangle);
              self.quadrangle = quadrangle;
          });
      });
    }
}

#pragma mark - Private

- (void)_done {
    // Save quadrangle
    self.scan.quadrangle = self.quadrangle;

    PostProcessingViewController *postProcessingViewController = [[PostProcessingViewController alloc] init];
    postProcessingViewController.scan = self.scan;
    [self.navigationController pushViewController:postProcessingViewController animated:YES];
}

- (void)_cancel {
    [self dismissViewControllerAnimated:YES completion:nil];
    [self.scan cancelCallback];
}

@end
