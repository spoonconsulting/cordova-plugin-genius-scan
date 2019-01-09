//
//  GSCEditableFrame.h
//  GSCamera
//
//  Created by Bruno Virlet on 4/4/15.
//  Copyright (c) 2015 Bruno Virlet. All rights reserved.
//

#import <UIKit/UIKit.h>

@class GSKEditableFrame;
@class GSKQuadrangle;

@protocol GSCEditableFrameDelegate

- (void)editableFrameDidChange:(GSKEditableFrame *)frame;
- (CGRect)editableFrameBoundsForFrameCorners:(GSKEditableFrame *)editableFrame ;
- (void)editableFrame:(GSKEditableFrame *)editableFrame userDidTouchPoint:(CGPoint)currentTouchPosition;
- (void)editableFrame:(GSKEditableFrame *)editableFrame userDidSelectCorner:(CGPoint)cornerPosition withFingerLocation:(CGPoint)pt;
- (void)editableFrameUserDidEndSelectCorner:(GSKEditableFrame *)editableFrame;
- (void)editableFrameUserDidDoubleTap:(GSKEditableFrame *)editableFrame;
@end

@interface GSKEditableFrame : UIView

@property (nonatomic, strong) GSKQuadrangle *quadrangle;

@property (nonatomic, weak) id <GSCEditableFrameDelegate> delegate;

@property (nonatomic, copy) UIColor *shadeColor;
@property (nonatomic, copy) UIColor *lineColor;

@property (nonatomic, assign) BOOL noCrop;

@end
