//
//  GSCEditFrameView.h
//  GSCamera
//
//  Created by Bruno Virlet on 4/4/15.
//  Copyright (c) 2015 Bruno Virlet. All rights reserved.
//

#import <UIKit/UIKit.h>

@class GSKEditableFrame;
@class GSKQuadrangle;

@interface GSKEditFrameView : UIImageView

- (void)clearSelection;

@property (nonatomic, readonly) GSKEditableFrame *imageSelection;

// Normalized quadrangle
@property (nonatomic, strong) GSKQuadrangle *quadrangle;
@property (nonatomic, assign) BOOL noCrop;

@end
