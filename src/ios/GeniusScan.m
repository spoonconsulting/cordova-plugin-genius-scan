#import "GeniusScan.h"
#import "Scan.h"
#import "EditFrameViewController.h"

#import <GSSDK/GSSDK.h>

@implementation GeniusScan

- (void)scan:(CDVInvokedUrlCommand*)command
{
    // Initialize SDK with licence key
    NSString *licenceKey = [[NSBundle mainBundle] infoDictionary][@"GSSDK_LICENCE_KEY"];
    [GSK initWithLicenseKey:licenceKey];

    // Parameters
    NSString *originalImageUri = [command.arguments objectAtIndex:0];

    Scan *scan = [Scan initWithFileUri:originalImageUri];

    scan.commandDelegate = self.commandDelegate;
    scan.callbackId = command.callbackId;

    EditFrameViewController *editFrameViewController = [[EditFrameViewController alloc] init];
    editFrameViewController.scan = scan;

    UINavigationController *navigationController = [[UINavigationController alloc] initWithRootViewController:editFrameViewController];

    [self.viewController presentViewController:navigationController animated:YES completion:nil];
}

@end
