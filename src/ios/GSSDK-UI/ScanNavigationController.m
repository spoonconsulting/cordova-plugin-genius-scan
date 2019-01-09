//
//  ScanNavigationController.m
//  RNGeniusScan
//
//  Created by Bruno Virlet on 7/3/18.
//  Copyright © 2018 Facebook. All rights reserved.
//

#import "ScanNavigationController.h"

#import "CameraViewController.h"
#import "EditFrameViewController.h"
#import "PostProcessingViewController.h"


// Private interface in the .m file
@interface ScanNavigationController () <CameraViewControllerDelegate, EditFrameViewControllerDelegate, PostProcessingViewControllerDelegate>

@property (nonatomic, copy) PromiseResolveBlock resolveBlock;
@property (nonatomic, copy) PromiseRejectBlock rejectBlock;
@property (nonatomic, strong) NSDictionary *scanOptions;

@end

@implementation ScanNavigationController

+ (instancetype)scanNavigationControllerFromCamera:(PromiseResolveBlock)resolveBlock rejecter:(PromiseRejectBlock)rejectBlock scanOptions:(NSDictionary *)scanOptions{

    ScanFactory *factory = [ScanFactory new];
    GSKCameraSession *cameraSession = [[GSKCameraSession alloc] initWithScanFactory:factory];
    CameraViewController *cameraViewController = [[CameraViewController alloc] initWithCameraSession:cameraSession];
    [cameraSession setup];

    ScanNavigationController *scanNavigationController = [[ScanNavigationController alloc] initWithRootViewController:cameraViewController resolver:resolveBlock rejecter:rejectBlock scanOptions:scanOptions];

    cameraViewController.delegate = scanNavigationController;

    return scanNavigationController;

}

+ (instancetype)scanNavigationControllerFromImageURL:(NSURL *)url resolver:(PromiseResolveBlock)resolveBlock rejecter:(PromiseRejectBlock)rejectBlock scanOptions:(NSDictionary *)scanOptions {

    Scan *scan = [Scan new];
    scan.originalImagePath = [url path]; // Usually that's enought to get /Users/bruno/….

    EditFrameViewController *editFrameViewController = [[EditFrameViewController alloc] init];
    editFrameViewController.scan = scan;

    ScanNavigationController *scanNavigationController = [[ScanNavigationController alloc] initWithRootViewController:editFrameViewController resolver:resolveBlock rejecter:rejectBlock scanOptions:scanOptions];

    editFrameViewController.delegate = scanNavigationController;

    return scanNavigationController;
}

- (instancetype)initWithRootViewController:(UIViewController *)rootViewController resolver:(PromiseResolveBlock)resolveBlock rejecter:(PromiseRejectBlock)rejectBlock scanOptions:(NSDictionary *)scanOptions {

    self = [super initWithRootViewController:rootViewController];

    if (self) {
        _resolveBlock = resolveBlock;
        _rejectBlock = rejectBlock;
        _scanOptions = scanOptions;
    }
    return self;
}


- (void)cameraViewController:(CameraViewController *)cameraViewController didFinishWithScan:(Scan *)scan {

    EditFrameViewController *editFrameViewController = [[EditFrameViewController alloc] init];
    editFrameViewController.delegate = self;
    editFrameViewController.scan = scan;
    [self pushViewController:editFrameViewController animated:YES];
}

- (void)editFrameViewController:(EditFrameViewController *)editFrameViewController didFinishWithScan:(Scan *)scan {
    PostProcessingViewController *postProcessingViewController = [[PostProcessingViewController alloc] init];
    postProcessingViewController.delegate = self;
    postProcessingViewController.scan = scan;
    postProcessingViewController.defaultEnhancement = self.scanOptions[@"defaultEnhancement"];
    [self pushViewController:postProcessingViewController animated:YES];
}

- (void)editFrameViewController:(EditFrameViewController *)editFrameViewController didCancelWithScan:(Scan *)scan {
    [self dismissViewControllerAnimated:YES completion:nil];
    self.rejectBlock(@"canceled", @"Canceled", nil);
}

- (void)postProcessingViewController:(id)postProcessingViewController didFinishWithScan:(Scan *)scan {
    NSString *enhancedFileUri = [scan saveEnhancedFileToURI];

    [self dismissViewControllerAnimated:YES completion:nil];
    self.resolveBlock(enhancedFileUri);
}

- (void)pdfViewController:(id)pdfViewController didFinishWithPdf:(NSURL *)pdfUri {
    [self dismissViewControllerAnimated:YES completion:nil];
    self.resolveBlock(pdfUri.absoluteString);
}

@end
