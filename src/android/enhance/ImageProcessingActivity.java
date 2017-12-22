package com.thegrizzlylabs.geniusscan.cordova.enhance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.thegrizzlylabs.geniusscan.cordova.GeniusScan;

import com.thegrizzlylabs.geniusscan.sdk.core.ImageType;
import com.thegrizzlylabs.geniusscan.sdk.core.RotationAngle;
import com.thegrizzlylabs.geniusscan.sdk.core.Scan;
import com.thegrizzlylabs.geniusscan.cordova.model.Page;

public class ImageProcessingActivity extends Activity {

   @SuppressWarnings("unused")
   private final static String TAG = ImageProcessingActivity.class.getSimpleName();

   public final static String EXTRA_PAGE = "page";

   private ImageView imageView;
   private ProgressDialog progressDialog;
   private Page page;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      page = getIntent().getParcelableExtra(EXTRA_PAGE);

      setContentView(GeniusScan.getResourceIdentifier("image_processing_activity", "layout"));
   }

   @Override
   protected void onResume() {
      super.onResume();

      imageView = (ImageView) findViewById(GeniusScan.getResourceIdentifier("image_view", "id"));

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
              .setTitle(GeniusScan.getResourceIdentifier("enhancement_dialog_title", "string"))
              .setItems(new CharSequence[]{
                      getString(GeniusScan.getResourceIdentifier("image_type_none", "string")),
                      getString(GeniusScan.getResourceIdentifier("image_type_color", "string")),
                      getString(GeniusScan.getResourceIdentifier("image_type_whiteboard", "string")),
                      getString(GeniusScan.getResourceIdentifier("image_type_black_white", "string"))
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
      setResult(RESULT_OK);
      finish();
   }

}
