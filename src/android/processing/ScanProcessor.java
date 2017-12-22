package com.thegrizzlylabs.geniusscan.cordova.processing;

import android.content.Context;

import com.thegrizzlylabs.geniusscan.sdk.core.GeniusScanLibrary;
import com.thegrizzlylabs.geniusscan.sdk.core.ImageType;
import com.thegrizzlylabs.geniusscan.sdk.core.ProcessingParameters;
import com.thegrizzlylabs.geniusscan.sdk.core.Quadrangle;
import com.thegrizzlylabs.geniusscan.sdk.core.ScanContainer;

import java.io.IOException;

/**
 * Created by pnollet on 04/10/2016.
 */

public class ScanProcessor {
    private static final String TAG = ScanProcessor.class.getSimpleName();

    /**
     * Generate the enhanced image for the given page
     *
     * @param context
     * @param scanContainer
     */
    public boolean processPage(Context context, ScanContainer scanContainer) {
        try {
            String enhancedImagePath = scanContainer.getEnhancedImage().getAbsolutePath(context);
            String originalImagePath = scanContainer.getOriginalImage().getAbsolutePath(context);

            ImageType imageType = scanContainer.getImageType();

            Quadrangle quadrangle = scanContainer.getQuadrangle();

            ProcessingParameters parameters = new ProcessingParameters(quadrangle, imageType);
            parameters = GeniusScanLibrary.detectWarpEnhance(originalImagePath, enhancedImagePath, parameters);

            scanContainer.setQuadrangle(parameters.getQuadrangle());
            scanContainer.setImageType(parameters.getImageType());

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
