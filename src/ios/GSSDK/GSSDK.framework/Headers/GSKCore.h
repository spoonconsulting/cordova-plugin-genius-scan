//
//  GeniusScanImage.h
//  GeniusScanImage
//
//  Created by Bruno Virlet on 3/9/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreMedia/CoreMedia.h>

NS_ASSUME_NONNULL_BEGIN

@class GSKQuadrangle;

/**
 The different possible enhancements
 */
typedef NS_ENUM(NSInteger, GSKPostProcessingType) {
	GSKPostProcessingTypeNone = 0,
	GSKPostProcessingTypeBlackAndWhite,
	GSKPostProcessingTypeColor,
    GSKPostProcessingTypeWhiteboard,
    GSKPostProcessingTypeBlackAndWhiteBinary,
};

/**
 - GSKDetectQuadrangleOptionsFast: a fast quadrangle detection for real-time detection
 */
typedef NS_OPTIONS(NSInteger, GSKDetectQuadrangleOptions) {
    GSKDetectQuadrangleOptionsNone = 0,
    GSKDetectQuadrangleOptionsFast = 1 << 0,
};

/**
 The Genius Scan SDK main interface to image processing libraries.
 */
@interface GSK : NSObject

///-------------------------
/// @name SDK Initialization
///-------------------------

/**
 Must be called first with a valide license key to enable 
 the SDK.
 */
+ (BOOL)initWithLicenseKey:(NSString *)licenseKey;

///-------------------------
/// @name Image Processing
///-------------------------

/**
 Detects the quadrangle corresponding to the edges of a document in a photo

 @param image The photo to detect a document in
 @param options @see GSKDetectQuadrangleOptions

 @return the detected quadrangle. nil if there is no detected quadrangle.
 */
+ (GSKQuadrangle * _Nullable)detectQuadrangleFromImage:(UIImage *)image options:(GSKDetectQuadrangleOptions)options;

/**
 Detects the quadrangle corresponding to the edges of a document in video frame

 @param sampleBuffer a YCbCr sample buffer of a video frame. Make sure the video output generating these frames is configured with the pixel format kCVPixelFormatType_420YpCbCr8BiPlanarFullRange
 
 @return the detected quadrangle. nil if there is no detected quadrangle.
 */
+ (GSKQuadrangle * _Nullable)detectQuadrangleFromSampleBuffer:(CMSampleBufferRef)sampleBuffer;

/**
 Extracts the document from the given photo and corrects its perspective
 
 @param image The original photo
 @param quadrangle The quadrangle representing the edges of the document. If nil, the entire image is returned.
 
 @return the cropped and perspective-corrected document. nil if there is an error.
         Errors include: - non-convex quadrangle
                         - failure to warp
 */
+ (UIImage * _Nullable)warpImage:(UIImage *)image withQuadrangle:(GSKQuadrangle * _Nullable)quadrangle;

/**
 Determines the best image processing type for a document
 
 @param image The cropped and perspective-corrected document
 
 @return The best enhancement for this document. @see GSKPostProcessingType
 */
+ (GSKPostProcessingType)bestPostProcessingForImage:(UIImage *)image;

/**
 Enhances a document image with the selected post processing type
 
 @param image the warped, perspective-corrected document
 @param postProcessing the enhancement to apply to the image. @see GSKPostProcessingType

 @return the enhanced image
 */
+ (UIImage *)enhanceImage:(UIImage *)image withPostProcessing:(GSKPostProcessingType)postProcessing;

@end

NS_ASSUME_NONNULL_END
