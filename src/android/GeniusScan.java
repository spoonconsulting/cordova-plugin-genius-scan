package com.thegrizzlylabs.geniusscan.cordova;

import com.thegrizzlylabs.geniusscan.sdk.core.GeniusScanLibrary;
import com.thegrizzlylabs.geniusscan.cordova.model.Page;
import com.thegrizzlylabs.geniusscan.cordova.processing.BorderDetectionActivity;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * This class echoes a string called from JavaScript.
 */
public class GeniusScan extends CordovaPlugin {
    private static CordovaInterface cordovaInstance;

    private final int REQUEST_CODE = 42;
    private CallbackContext callback = null;
    private Page scanContainer = null;

    public static int getResourceIdentifier(String name, String type) {
      Application app = cordovaInstance.getActivity().getApplication();
      String package_name = app.getPackageName();
      Resources resources = app.getResources();
      return resources.getIdentifier(name, type, package_name);
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        GeniusScan.cordovaInstance = cordova;
        super.initialize(cordova, webView);

        Context context = cordova.getActivity().getApplicationContext();
        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String value = ai.metaData.getString("GSSDK_LICENCE_KEY");
        GeniusScanLibrary.init(cordova.getActivity().getApplicationContext(), value);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("scan")) {
            callback = callbackContext;

            Uri imageFileUri = Uri.parse(args.getString(0));
            String originalImageFilePath = imageFileUri.getPath();

            Context context = this.cordova.getActivity().getApplicationContext();
            scanContainer = new Page(context, callbackContext, originalImageFilePath);

            Intent intent = new Intent(context, BorderDetectionActivity.class);
            intent.putExtra(BorderDetectionActivity.EXTRA_PAGE, (Page) scanContainer);

            this.cordova.setActivityResultCallback((CordovaPlugin) this);
            this.cordova.startActivityForResult((CordovaPlugin) this, intent, REQUEST_CODE);
            return true;
        }
        return false;
    }

     private void generateThumbnail(Context context, String pathOfInputImage){
        try
        {
            int inWidth = 0;
            int inHeight = 0;
            int dstWidth = 370;
            int dstHeight = 370;

            InputStream in = new FileInputStream(pathOfInputImage);

            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            // decode full image pre-resized
            in = new FileInputStream(pathOfInputImage);
            options = new BitmapFactory.Options();
            // calc rough re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth/dstWidth, inHeight/dstHeight);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);

            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);

            // save image
            File file = new File(pathOfInputImage);
            String filename = file.getName();
            FileOutputStream out = new FileOutputStream(new File(context.getFilesDir(), "thumb_" + filename).getAbsolutePath());
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        }
        catch (Exception e)
        {
            Log.e("Image", e.getMessage(), e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE) {
            if( resultCode == Activity.RESULT_OK)
            {
                String enhancedImageFilePath = scanContainer.getEnhancedImage().getAbsolutePath(null);
                generateThumbnail(this.cordova.getActivity().getApplicationContext(), enhancedImageFilePath);
                Uri enhancedImageFileUri = Uri.fromFile(new File(enhancedImageFilePath));
                PluginResult result = new PluginResult(PluginResult.Status.OK, enhancedImageFileUri.toString());
                result.setKeepCallback(true);
                callback.sendPluginResult(result);
            }
            else
            {
                PluginResult result = new PluginResult(PluginResult.Status.ERROR, "error" );
                result.setKeepCallback(true);
                callback.sendPluginResult(result);
            }
        }
    }
}
