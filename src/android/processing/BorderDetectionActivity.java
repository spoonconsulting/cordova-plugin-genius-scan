package com.thegrizzlylabs.geniusscan.cordova.processing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.thegrizzlylabs.geniusscan.cordova.GeniusScan;

import com.thegrizzlylabs.geniusscan.sdk.ui.BorderDetectionImageView;
import com.thegrizzlylabs.geniusscan.cordova.enhance.ImageProcessingActivity;
import com.thegrizzlylabs.geniusscan.cordova.model.Page;
import com.thegrizzlylabs.geniusscan.sdk.ui.MagnifierBorderDetectionListener;
import com.thegrizzlylabs.geniusscan.sdk.ui.MagnifierView;

import org.apache.cordova.CordovaPlugin;

public class BorderDetectionActivity extends Activity {

   @SuppressWarnings("unused")
   private static final String TAG = BorderDetectionActivity.class.getSimpleName();

   private final int RESULT_CODE = 42;

   private Page page;

   private ProgressDialog progressDialog;
   private BorderDetectionImageView imageView;
   private MagnifierView magnifierView;

   public final static String EXTRA_PAGE = "page";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(GeniusScan.getResourceIdentifier("border_detection_activity", "layout"));

      imageView = (BorderDetectionImageView) findViewById(GeniusScan.getResourceIdentifier("image_view", "id"));
      imageView.setColor(GeniusScan.getResourceIdentifier("blue", "color"));

      magnifierView = (MagnifierView) findViewById(GeniusScan.getResourceIdentifier("magnifier_view", "id"));
      imageView.setListener(new MagnifierBorderDetectionListener(magnifierView));

      page = getIntent().getParcelableExtra(EXTRA_PAGE);
   }

   @Override
   protected void onResume() {
      super.onResume();
      String filename = page.getOriginalImage().getAbsolutePath(this);
      BitmapFactory.Options opts = new BitmapFactory.Options();
      opts.inSampleSize = 2;

      Bitmap bitmap = BitmapFactory.decodeFile(filename, opts);
      imageView.setImageBitmap(bitmap);
      magnifierView.setBitmap(bitmap);

      progressDialog = new ProgressDialog(this);

      progressDialog.show();
      new AnalyzeAsyncTask(this) {
         @Override
         protected void onPostExecute(Void aVoid) {
            endAnalyze();
         }
      }.execute(page);
   }

   protected void endAnalyze() {
      progressDialog.hide();
      addQuadrangleToView();
   }

   void addQuadrangleToView() {
      imageView.setQuad(page.getQuadrangle());
      imageView.invalidate();
   }

   public void setQuadrangleToFullImage(View view) {
      page.getQuadrangle().setToFullImage();
      imageView.invalidate();
   }

   public void select(View view) {
      Intent intent = new Intent(this, ImageProcessingActivity.class);
      intent.putExtra(ImageProcessingActivity.EXTRA_PAGE, page);

      startActivityForResult(intent, RESULT_CODE);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == RESULT_CODE) {
         setResult(resultCode);
         finish();
      }
   }

}
