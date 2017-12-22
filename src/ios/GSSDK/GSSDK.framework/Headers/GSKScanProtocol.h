//
//  GSKScanProtocol.h
//  GSSDK
//
//  Created by Bruno Virlet on 6/2/16.
//
//

#import <Foundation/Foundation.h>

/**
 A protocol representing a scan, the result of the camera output. It encapsulates a simple file path. Since it's a protocol,
 you can implement this object to store more details.
 */
@protocol GSKScanProtocol <NSObject>

/**
 Returns a filepath including the filename to store
 the unrotated original JPEG out of the camera
*/
- (NSString *)filePath;

@end
