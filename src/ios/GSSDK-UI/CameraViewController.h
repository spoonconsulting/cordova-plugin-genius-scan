//
//  CameraViewController.h
//  GSSDKDemo
//
//  Created by Bruno Virlet on 4/3/15.
//  Copyright (c) 2015 Bruno Virlet. All rights reserved.
//

#import <GSSDK/GSSDK.h>

@class CameraViewController;
@class Scan;

@protocol CameraViewControllerDelegate
- (void)cameraViewController:(CameraViewController *)cameraViewController didFinishWithScan:(Scan *)scan;
- (void)viewControllerDidCancel:(UIViewController *)viewController;
@end

/**
 A very simple camera view.

 The complexity is hidden in GSKCameraViewController.
 You have access to all the delegate methods of the GSKCameraSession.
 */
@interface CameraViewController : GSKCameraViewController

@property (nonatomic, weak) id<CameraViewControllerDelegate> delegate;

@end
