package com.thegrizzlylabs.geniusscan.cordova.enhance;

import android.content.Context;
import android.os.AsyncTask;

import com.thegrizzlylabs.geniusscan.sdk.core.GeniusScanLibrary;
import com.thegrizzlylabs.geniusscan.sdk.core.RotationAngle;
import com.thegrizzlylabs.geniusscan.sdk.core.ScanContainer;
import com.thegrizzlylabs.geniusscan.cordova.processing.ScanProcessor;

import java.io.IOException;

class RotateAsyncTask extends AsyncTask<ScanContainer, Void, Void> {

   private final Context context;
   private final RotationAngle angle;

   @Override
   protected Void doInBackground(ScanContainer... params) {
      ScanContainer scanContainer = params[0];
      try {
         if (scanContainer.getQuadrangle() != null) {
            scanContainer.setQuadrangle(scanContainer.getQuadrangle().rotate(angle));
         }
         GeniusScanLibrary.rotateImage(scanContainer.getOriginalImage().getAbsolutePath(context), scanContainer.getOriginalImage().getAbsolutePath(context), angle);
      } catch (IOException e) {
         e.printStackTrace();
      }

      // original image was rotated, let's reprocess it
      new ScanProcessor().processPage(context, scanContainer);

      return null;
   }

   RotateAsyncTask(Context context, RotationAngle angle) {
      this.context = context;
      this.angle = angle;
   }
}