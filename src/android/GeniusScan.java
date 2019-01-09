package com.thegrizzlylabs.geniusscan.cordova;

import com.thegrizzlylabs.geniusscan.sdk.pdf.PDFGeneratorError;
import com.geniusscan.GeniusScanSdkUI;
import com.geniusscan.PromiseResult;
import com.geniusscan.enhance.PdfGenerationTask;


import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.res.Resources;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;


/**
 * This class echoes a string called from JavaScript.
 */
public class GeniusScan extends CordovaPlugin {
    private static CordovaInterface cordovaInstance;
    private CallbackContext callback = null;
    private static final String E_PDF_ERROR = "E_PDF_GENERATION_ERROR";

    public static PluginResult promiseResultToPluginResult(PromiseResult result) {
        if (result.isError) {
            return new PluginResult(PluginResult.Status.ERROR, result.message);
        } else {
            return result.result == null ? new PluginResult(PluginResult.Status.OK)
                    : new PluginResult(PluginResult.Status.OK, result.result);
        }
    }

    public static int getResourceIdentifier(String name, String type) {
        Application app = cordovaInstance.getActivity().getApplication();
        String package_name = app.getPackageName();
        Resources resources = app.getResources();
        return resources.getIdentifier(name, type, package_name);
    }

    public static String getResourceString(String name, String type) {
        Application app = cordovaInstance.getActivity().getApplication();
        Resources resources = app.getResources();
        return resources.getString(GeniusScan.getResourceIdentifier(name, type));
    }

    private static HashMap jsonOptionsToHashmap(JSONObject options) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keysItr = options.keys();

        while(keysItr.hasNext()) {
            String key = keysItr.next();
            try {
                Object value = options.get(key);
                map.put(key, value);
            } catch(JSONException e) {
                // This should not happen
            }
        }
        return map;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        GeniusScan.cordovaInstance = cordova;
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        callback = callbackContext;

        if (action.equals("scanImage")) {
            Uri imageFileUri = Uri.parse(args.getString(0));
            HashMap scanOptions = jsonOptionsToHashmap(args.getJSONObject(1));
            String originalImageFilePath = imageFileUri.getPath();
            this.cordova.setActivityResultCallback((CordovaPlugin) this);
            GeniusScanSdkUI.scanImage(this.cordova.getActivity(), originalImageFilePath, scanOptions);
            return true;
        }

        if (action.equals("scanCamera")) {
            HashMap scanOptions = jsonOptionsToHashmap(args.getJSONObject(0));
            this.cordova.setActivityResultCallback((CordovaPlugin) this);
            GeniusScanSdkUI.scanCamera(this.cordova.getActivity(), scanOptions);
            return true;
        }

        if (action.equals("generatePDF")) {
            String title = args.getString(0);
            JSONArray imageUrisJson = args.getJSONArray(1);
            HashMap pdfOptions = jsonOptionsToHashmap(args.getJSONObject(2));

            ArrayList<String> imageUris = new ArrayList<String>();
            for (int i=0; i<imageUrisJson.length(); i++) {
                Uri imageFileUri = Uri.parse(imageUrisJson.getString(i));
                imageUris.add( imageFileUri.getPath());
            }
            File outputFile = new File(this.cordova.getActivity().getApplicationContext().getExternalCacheDir(), UUID.randomUUID().toString() + "-scan.pdf");
            final String absoluteFilePath = outputFile.getAbsolutePath();

            new PdfGenerationTask(title, imageUris, absoluteFilePath, pdfOptions, new PdfGenerationTask.OnPdfGeneratedListener() {
                @Override
                public void onPdfGenerated(PDFGeneratorError result) {
                    if (result != PDFGeneratorError.PDFGENERATORERRORCODESUCCESS) {
                        callback.sendPluginResult(promiseResultToPluginResult(PromiseResult.reject(E_PDF_ERROR, "Error generating PDF: " + result)));
                    } else {
                        callback.sendPluginResult(promiseResultToPluginResult(PromiseResult.resolve(absoluteFilePath)));
                    }
                }
            }).execute();

            return true;
        }

        if (action.equals("setLicenceKey")) {
            String licenceKey = args.getString(0);
            PromiseResult result = GeniusScanSdkUI.setLicenceKey(this.cordova.getActivity().getApplicationContext(),
                    licenceKey);
            callback.sendPluginResult(promiseResultToPluginResult(result));
            return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        PromiseResult result = GeniusScanSdkUI.resolvePromiseWithActivityResult(this.cordova.getActivity(), requestCode,
                resultCode, data);
        if (result != null) {
            callback.sendPluginResult(promiseResultToPluginResult(result));
        }
    }
}
