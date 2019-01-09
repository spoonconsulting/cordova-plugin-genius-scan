/********* GeniusScan.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>

@interface GeniusScan : CDVPlugin
{
  // Member variables go here.
}

- (void)scanImage:(CDVInvokedUrlCommand *)command;
- (void)scanCamera:(CDVInvokedUrlCommand *)command;
- (void)setLicenceKey:(CDVInvokedUrlCommand *)command;
- (void)generatePDF:(CDVInvokedUrlCommand *)command;
@end
