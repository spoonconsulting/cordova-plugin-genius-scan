
package com.geniusscan;

import com.thegrizzlylabs.geniusscan.sdk.core.GeniusScanLibrary;
import com.thegrizzlylabs.geniusscan.sdk.core.InitializationException;

import com.geniusscan.model.Page;
import com.geniusscan.camera.ScanActivity;
import com.geniusscan.processing.BorderDetectionActivity;

import android.app.Application;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import java.util.HashMap;

public class GeniusScanSdkUI {
  public final static String EXTRA_PAGE = "EXTRA_PAGE";
  public final static String RESULT_URI = "FINAL_RESULT_URI";

  private static final int SCAN_REQUEST = 42;
  private static final String E_SCAN_CANCELLED = "E_SCAN_CANCELLED";
  private static final String E_LICENCE_KEY_ERROR = "E_LICENCE_KEY_ERROR";

  public static void scanCamera(Activity currentActivity, HashMap scanOptions) {
    Context context = currentActivity.getApplicationContext();

    Page scanContainer = new Page(context, scanOptions);

    Intent intent = new Intent(context, ScanActivity.class);
    intent.putExtra(EXTRA_PAGE, scanContainer);

    currentActivity.startActivityForResult(intent, SCAN_REQUEST);
  }

  public static void scanImage(Activity currentActivity, String imageUri, HashMap scanOptions) {
    Context context = currentActivity.getApplicationContext();
    Uri imageFileUri = Uri.parse(imageUri);

    Page scanContainer = new Page(context, imageFileUri.getPath(), scanOptions);

    Intent intent = new Intent(context, BorderDetectionActivity.class);
    intent.putExtra(EXTRA_PAGE, scanContainer);

    currentActivity.startActivityForResult(intent, SCAN_REQUEST);
  }

  public static PromiseResult resolvePromiseWithActivityResult(Activity activity, int requestCode, int resultCode,
      Intent intent) {
    if (requestCode == SCAN_REQUEST) {

      if (resultCode == Activity.RESULT_CANCELED) {
        return PromiseResult.reject(E_SCAN_CANCELLED, "Scan was cancelled");
      } else if (resultCode == Activity.RESULT_OK) {
        String resultUri = intent.getStringExtra(RESULT_URI);
        return PromiseResult.resolve(resultUri);
      }
    }

    return null;
  }

  public static PromiseResult setLicenceKey(Context context, String licenceKey) {
    try {
      GeniusScanLibrary.init(context, licenceKey);
      return PromiseResult.resolve();
    } catch (InitializationException e) {
      return PromiseResult.reject(E_LICENCE_KEY_ERROR, e.getMessage());
    }
  }

  public static int getResourceIdentifier(Activity activity, String name, String type) {
    Application app = activity.getApplication();
    String packageName = app.getPackageName();
    Resources resources = app.getResources();
    return resources.getIdentifier(name, type, packageName);
  }
}