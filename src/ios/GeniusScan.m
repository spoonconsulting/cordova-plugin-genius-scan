#import "GeniusScan.h"
#import "ScanNavigationController.h"
#import <GSSDK/GSSDK.h>

@implementation GeniusScan
- (void)setLicenceKey:(CDVInvokedUrlCommand*)command
{
    // Initialize SDK with licence key
    NSString *licenceKey = [command.arguments objectAtIndex:0];
    BOOL validLicence = [GSK initWithLicenseKey:licenceKey];

    if (!validLicence) {
        CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"License key is not valid or has expired."];
        [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
    } else {
        CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
    }
}

- (void)generatePDF:(CDVInvokedUrlCommand*)command
{
    NSString *title = [command.arguments objectAtIndex:0];
    NSArray *imagePaths = [command.arguments objectAtIndex:1];
    NSDictionary *pdfOptions = [command.arguments objectAtIndex:2];

    NSString *password = pdfOptions[@"password"];
    // First we generate a list of pages.
    NSMutableArray *pages = [NSMutableArray array];

    [imagePaths enumerateObjectsUsingBlock:^(NSString *filePath, NSUInteger idx, BOOL * _Nonnull stop) {
        /// For each page, we specify the document and a size in inches.
        GSKPDFPage *page = [[GSKPDFPage alloc] initWithFilePath:filePath inchesSize:[[GSKPDFSize alloc] initWithWidth:8.27 height:11.69] /* size in inches for an A4 sheet */];
        [pages addObject:page];
    }];

    /// We then create a GSKPDFDocument which holds the general information about the PDF document to generate
    GSKPDFDocument *document = [[GSKPDFDocument alloc] initWithTitle:title password:password keywords:nil pages:pages];

    NSString *documentsDirectory = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) firstObject];

    unsigned long timestamp = [[NSDate date] timeIntervalSince1970];
    NSString *outputFilePath = [documentsDirectory stringByAppendingPathComponent:[NSString stringWithFormat:@"scan-%lu.pdf",timestamp]];

    /// Last, we instantiate a GSKPDFGenerator and generate the document
    GSKPDFGenerator *generator = [GSKPDFGenerator createWithDocument:document];
    [generator generatePDF:outputFilePath];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:outputFilePath];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)scanImage:(CDVInvokedUrlCommand*)command
{
    PromiseResolveBlock resolve = ^(NSString *result) {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:result];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    };

    PromiseRejectBlock reject = ^(NSString *code, NSString *message, NSError *error) {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:message];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    };

    // Parameters
    NSString *originalImageUri = [command.arguments objectAtIndex:0];
    NSDictionary *scanOptions = [command.arguments objectAtIndex:1];
    UIViewController *viewController = [ScanNavigationController scanNavigationControllerFromImageURL:[NSURL URLWithString:originalImageUri] resolver:resolve rejecter:reject scanOptions:scanOptions];
    [self.viewController presentViewController:viewController animated:YES completion:nil];
}

- (void)scanCamera:(CDVInvokedUrlCommand*)command
{
    PromiseResolveBlock resolve = ^(NSString *result) {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:result];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    };

    PromiseRejectBlock reject = ^(NSString *code, NSString *message, NSError *error) {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:message];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    };

    NSDictionary *scanOptions = [command.arguments objectAtIndex:0];
    UIViewController *viewController = [ScanNavigationController scanNavigationControllerFromCamera:resolve rejecter:reject scanOptions:scanOptions];
    [self.viewController presentViewController:viewController animated:YES completion:nil];
}
@end
