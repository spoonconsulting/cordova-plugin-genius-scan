package com.geniusscan.enhance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.thegrizzlylabs.geniusscan.sdk.core.ImageType;
import com.thegrizzlylabs.geniusscan.sdk.core.RotationAngle;
import com.thegrizzlylabs.geniusscan.sdk.core.Scan;

import com.geniusscan.GeniusScanSdkUI;
import com.geniusscan.model.Page;

import java.io.File;

public class ImageProcessingActivity extends Activity {

   @SuppressWarnings("unused")
   private final static String TAG = ImageProcessingActivity.class.getSimpleName();

   private ImageView imageView;
   private ProgressDialog progressDialog;
   private Page page;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      page = getIntent().getParcelableExtra(GeniusScanSdkUI.EXTRA_PAGE);

      setContentView(GeniusScanSdkUI.getResourceIdentifier(this, "image_processing_activity", "layout" ));
   }

   @Override
   protected void onResume() {
      super.onResume();

      imageView = (ImageView) findViewById(GeniusScanSdkUI.getResourceIdentifier(this, "image_view", "id" ));

      displayScan(page.getOriginalImage());

      progressDialog = new ProgressDialog(this);
      progressDialog.show();
      enhance();
   }

   private void endEnhance() {
      displayEnhancedScan();
      progressDialog.hide();
   }

   private void endRotate() {
      displayEnhancedScan();
      progressDialog.hide();
   }

   public void changeEnhancement(View view) {
      new AlertDialog.Builder(this)
              .setTitle(GeniusScanSdkUI.getResourceIdentifier(this, "enhancement_dialog_title", "string"))
              .setItems(new CharSequence[]{
                      getString(GeniusScanSdkUI.getResourceIdentifier(this, "image_type_none", "string" )),
                      getString(GeniusScanSdkUI.getResourceIdentifier(this, "image_type_photo", "string" )),
                      getString(GeniusScanSdkUI.getResourceIdentifier(this, "image_type_color", "string" )),
                      getString(GeniusScanSdkUI.getResourceIdentifier(this, "image_type_black_white", "string"))
              },new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
             ImageType imageType = new ImageType[] {
                     ImageType.NONE,
                     ImageType.COLOR,
                     ImageType.WHITEBOARD,
                     ImageType.BLACK_WHITE
             }[which];
             page.setImageType(imageType);
             progressDialog.show();
             enhance();
         }
      }).show();
   }

   private void rotate(RotationAngle angle) {
      progressDialog.show();
      new RotateAsyncTask(this, angle) {
         @Override
         protected void onPostExecute(Void aVoid) {
            endRotate();
         }
      }.execute(page);
   }

   private void enhance() {
      new EnhanceAsyncTask(this) {
         @Override
         protected void onPostExecute(Void aVoid) {
            endEnhance();
         }
      }.execute(page);
   }

   private void displayEnhancedScan() {
      displayScan(page.getEnhancedImage());
   }

   private void displayScan(Scan scan) {
      Options opts = new Options();
      opts.inSampleSize = 2;
      Bitmap bitmap = BitmapFactory.decodeFile(scan.getAbsolutePath(this), opts);
      imageView.setImageBitmap(bitmap);
      imageView.invalidate();
   }

   public void rotateLeft(View view) {
      rotate(RotationAngle.ROTATION_90_CCW);
   }

   public void rotateRight(View view) {
      rotate(RotationAngle.ROTATION_90_CW);
   }

   public void savePage(View view) {
      String enhancedImageFilePath = page.getEnhancedImage().getAbsolutePath(this);
      Uri enhancedImageFileUri = Uri.fromFile(new File(enhancedImageFilePath));

      Intent intent = getIntent();
      intent.putExtra(GeniusScanSdkUI.RESULT_URI, enhancedImageFileUri.toString());

      setResult(RESULT_OK, intent);
      finish();
   }

}
