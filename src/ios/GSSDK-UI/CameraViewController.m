//
//  CameraViewController.m
//  GSSDKDemo
//
//  Created by Bruno Virlet on 4/3/15.
//  Copyright (c) 2015 Bruno Virlet. All rights reserved.
//

#import "CameraViewController.h"

#import <GSSDK/GSSDK.h>
#import "EditFrameViewController.h"
#import "Scan.h"


@interface CameraViewController ()

@property (nonatomic, strong) UIView *toolbar;
@property (nonatomic, strong) UIButton *cameraButton;

@end

///
/// The interesting bits here are in the delegate methods of the camera session.
///
@implementation CameraViewController

#pragma mark - Lifecycle

- (void)viewDidLoad {
    [super viewDidLoad];

    [self _setupConstraints];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.navigationItem.leftBarButtonItems = @[
                                                [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(_cancel)],
                                                ];
}

#pragma mark - Views

- (UIView *)toolbar {
    if (!_toolbar) {
        _toolbar = [UIView new];
        _toolbar.translatesAutoresizingMaskIntoConstraints = NO;
        [self.view addSubview:_toolbar];
    }
    return _toolbar;
}

- (UIButton *)cameraButton {
    if (!_cameraButton) {
        _cameraButton = [UIButton new];
        [_cameraButton setTitle:NSLocalizedStringFromTable(@"Capture", @"GSSDK", @"") forState:UIControlStateNormal];
        _cameraButton.translatesAutoresizingMaskIntoConstraints = NO;
        [_cameraButton addTarget:self action:@selector(takePhoto) forControlEvents:UIControlEventTouchUpInside];
        [self.toolbar addSubview:_cameraButton];
    }
    return _cameraButton;
}

#pragma mark - Camera Session Delegate overrides

/// We use the delegate methods of the GSKCameraSession to
/// react to the different states of taking the photo
- (void)cameraSessionWillSnapPhoto:(GSKCameraSession *)cameraSession {
    [super cameraSessionWillSnapPhoto:cameraSession];

    dispatch_async(dispatch_get_main_queue(), ^{
        self.cameraButton.enabled = NO;
    });

}

/// We just received a photo from the camera. We could do some post-processing immediately but
/// here we choose to immediately show the interface that lets the user edit the crop area.
- (void)cameraSession:(GSKCameraSession *)cameraSession didGenerateScan:(Scan *)scan {
    [super cameraSession:cameraSession didGenerateScan:scan];

    dispatch_async(dispatch_get_main_queue(), ^{
        [self.cameraButton setEnabled:true];

        [self.delegate cameraViewController:self didFinishWithScan:scan];
    });
}

#pragma mark - Private

- (void)_setupConstraints {
    self.cameraView.translatesAutoresizingMaskIntoConstraints = NO;


    NSDictionary *views = @{ @"cameraView" : self.cameraView,
                             @"bottomToolbar" : self.toolbar,
                             @"cameraButton" : self.cameraButton
                             };

    NSDictionary *metrics = @{ @"bottomToolbarHeight" : @124,
                               @"cameraButtonSize" : @200,
                               @"topMargin" : @40
                               };

    [self.view addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|[cameraView]|" options:0 metrics:metrics views:views]];
    [self.view addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-topMargin-[cameraView][bottomToolbar(bottomToolbarHeight)]|" options:0 metrics:metrics views:views]];
    [self.view addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|[bottomToolbar]|" options:0 metrics:metrics views:views]];
    [self.view addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:[cameraButton(cameraButtonSize)]" options:0 metrics:metrics views:views]];
    [self.view addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:[cameraButton(cameraButtonSize)]" options:0 metrics:metrics views:views]];
    [self.view addConstraints:@[
                                [NSLayoutConstraint constraintWithItem:self.cameraButton attribute:NSLayoutAttributeCenterY relatedBy:NSLayoutRelationEqual toItem:self.toolbar attribute:NSLayoutAttributeCenterY multiplier:1.0f constant:0.0f],
                                [NSLayoutConstraint constraintWithItem:self.cameraButton attribute:NSLayoutAttributeCenterX relatedBy:NSLayoutRelationEqual toItem:self.toolbar attribute:NSLayoutAttributeCenterX multiplier:1.0f constant:0.0f]
                                ]];
}

- (void)_cancel {
    [self.delegate viewControllerDidCancel:self];
}

@end
