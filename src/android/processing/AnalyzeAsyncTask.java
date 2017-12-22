package com.thegrizzlylabs.geniusscan.cordova.processing;

import android.content.Context;
import android.os.AsyncTask;

import com.thegrizzlylabs.geniusscan.sdk.core.GeniusScanLibrary;
import com.thegrizzlylabs.geniusscan.sdk.core.ScanContainer;

import java.io.IOException;

class AnalyzeAsyncTask extends AsyncTask<ScanContainer, Void, Void> {

   private final Context context;

   AnalyzeAsyncTask(Context context) {
      this.context = context;
   }

   @Override
   protected Void doInBackground(ScanContainer... params) {
      ScanContainer scanContainer = params[0];
      try {
         scanContainer.setQuadrangle(GeniusScanLibrary.detectFrame(scanContainer.getOriginalImage().getAbsolutePath(context)));
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }
}