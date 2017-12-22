package com.thegrizzlylabs.geniusscan.cordova.enhance;

import android.content.Context;
import android.os.AsyncTask;

import com.thegrizzlylabs.geniusscan.sdk.core.GeniusScanLibrary;
import com.thegrizzlylabs.geniusscan.sdk.core.ScanContainer;
import com.thegrizzlylabs.geniusscan.cordova.processing.ScanProcessor;

import java.io.IOException;

class EnhanceAsyncTask extends AsyncTask<ScanContainer, Void, Void> {

   private final Context context;

   EnhanceAsyncTask(Context context) {
      this.context = context;
   }

   @Override
   protected Void doInBackground(ScanContainer... params) {
      ScanContainer scanContainer = params[0];

      if (scanContainer.getImageType() == null) {
         try {
            scanContainer.setImageType(GeniusScanLibrary.detectImageType(scanContainer.getOriginalImage().getAbsolutePath(context)));
         } catch (IOException e) {
            e.printStackTrace();
         }
      }

      new ScanProcessor().processPage(context, scanContainer);

      return null;
   }
}