package com.thegrizzlylabs.geniusscan.cordova.model;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.thegrizzlylabs.geniusscan.sdk.core.ImageType;
import com.thegrizzlylabs.geniusscan.sdk.core.Quadrangle;
import com.thegrizzlylabs.geniusscan.sdk.core.Scan;
import com.thegrizzlylabs.geniusscan.sdk.core.ScanContainer;

import org.apache.cordova.CallbackContext;

import java.io.File;
import java.util.UUID;

/**
 * Created by guillaume on 29/09/16.
 */

public class Page implements ScanContainer, Parcelable {

    private Quadrangle quadrangle;
    private ImageType imageType;

    private Image originalImage;
    private Image enhancedImage;

    private CallbackContext callbackContext;

    public Page(Context context, CallbackContext _callbackContext, String originalImageFilePath) {
        String enhancedFilename = UUID.randomUUID().toString() + "enhanced.jpg";
        String enhancedFilePath = new File(context.getFilesDir(), enhancedFilename).getAbsolutePath();

        originalImage = new Image(originalImageFilePath);
        enhancedImage = new Image(enhancedFilePath);
        callbackContext = _callbackContext;
    }

    @Override
    public Scan getOriginalImage() {
        return originalImage;
    }

    @Override
    public Scan getEnhancedImage() {
        return enhancedImage;
    }

    @Override
    public void setQuadrangle(Quadrangle quadrangle) {
        this.quadrangle = quadrangle;
    }

    @Override
    public Quadrangle getQuadrangle() {
        return quadrangle;
    }

    @Override
    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    @Override
    public ImageType getImageType() {
        return imageType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Page> CREATOR
            = new Parcelable.Creator<Page>() {
        public Page createFromParcel(Parcel in) {
            return new Page(in);
        }

        public Page[] newArray(int size) {
            return new Page[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(quadrangle, 0);
        dest.writeSerializable(imageType);
        dest.writeParcelable(originalImage, 0);
        dest.writeParcelable(enhancedImage, 0);
    }

    private Page(Parcel in) {
        quadrangle = in.readParcelable(Quadrangle.class.getClassLoader());
        imageType = (ImageType) in.readSerializable();
        originalImage = in.readParcelable(Image.class.getClassLoader());
        enhancedImage = in.readParcelable(Image.class.getClassLoader());
    }
}
