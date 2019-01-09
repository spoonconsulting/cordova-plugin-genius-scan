package com.geniusscan.processing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.thegrizzlylabs.geniusscan.sdk.ui.BorderDetectionImageView;
import com.thegrizzlylabs.geniusscan.sdk.ui.MagnifierBorderDetectionListener;
import com.thegrizzlylabs.geniusscan.sdk.ui.MagnifierView;

import com.geniusscan.GeniusScanSdkUI;
import com.geniusscan.enhance.ImageProcessingActivity;
import com.geniusscan.model.Page;

public class BorderDetectionActivity extends Activity {

   @SuppressWarnings("unused")
   private static final String TAG = BorderDetectionActivity.class.getSimpleName();

   private final int ACTIVITY_REQUEST_CODE = 421;

   private Page page;

   private ProgressDialog progressDialog;
   private BorderDetectionImageView imageView;
   private MagnifierView magnifierView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(GeniusScanSdkUI.getResourceIdentifier(this, "border_detection_activity", "layout"));

      imageView = (BorderDetectionImageView) findViewById(GeniusScanSdkUI.getResourceIdentifier(this, "image_view", "id"));
      imageView.setColor(GeniusScanSdkUI.getResourceIdentifier(this, "blue", "color"));

      magnifierView = (MagnifierView) findViewById(GeniusScanSdkUI.getResourceIdentifier(this, "magnifier_view", "id"));
      imageView.setListener(new MagnifierBorderDetectionListener(magnifierView));

      page = getIntent().getParcelableExtra(GeniusScanSdkUI.EXTRA_PAGE);
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
      intent.putExtra(GeniusScanSdkUI.EXTRA_PAGE, page);

      startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == ACTIVITY_REQUEST_CODE) {
         if (resultCode == RESULT_OK) {
            setResult(resultCode, data);
            finish();
         }
      }
   }
}
