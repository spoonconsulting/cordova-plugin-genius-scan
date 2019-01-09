//
//  ScanNavigationController.h
//  RNGeniusScan
//
//  Created by Bruno Virlet on 7/3/18.
//  Copyright © 2018 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>

/**
 * Block that bridge modules use to resolve the JS promise waiting for a result.
 * Nil results are supported and are converted to JS's undefined value.
 */
typedef void (^PromiseResolveBlock)(NSString *resultingImageUri);

/**
 * Block that bridge modules use to reject the JS promise waiting for a result.
 * The error may be nil but it is preferable to pass an NSError object for more
 * precise error messages.
 */
typedef void (^PromiseRejectBlock)(NSString *code, NSString *message, NSError *error);

@class CameraViewController;

// Public interface
@interface ScanNavigationController : UINavigationController

// Class factory methods to make it easy to build the two slighly different scan flows.
+ (instancetype)scanNavigationControllerFromCamera:(PromiseResolveBlock)resolveBlock rejecter:(PromiseRejectBlock)rejecter scanOptions:(NSDictionary *)scanOptions;
+ (instancetype)scanNavigationControllerFromImageURL:(NSURL *)url resolver:(PromiseResolveBlock)resolveBlock rejecter:(PromiseRejectBlock)rejecter scanOptions:(NSDictionary *)scanOptions;

- (instancetype)initWithRootViewController:(UIViewController *)rootViewController resolver:(PromiseResolveBlock)resolveBlock rejecter:(PromiseRejectBlock)rejectBlock scanOptions:(NSDictionary *)scanOptions;
@end
