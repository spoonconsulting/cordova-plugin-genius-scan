package com.geniusscan.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.thegrizzlylabs.geniusscan.sdk.core.ImageType;
import com.thegrizzlylabs.geniusscan.sdk.core.Quadrangle;
import com.thegrizzlylabs.geniusscan.sdk.core.Scan;
import com.thegrizzlylabs.geniusscan.sdk.core.ScanContainer;
import java.util.UUID;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.thegrizzlylabs.geniusscan.sdk.core.ImageType;
import com.thegrizzlylabs.geniusscan.sdk.core.Quadrangle;
import com.thegrizzlylabs.geniusscan.sdk.core.Scan;
import com.thegrizzlylabs.geniusscan.sdk.core.ScanContainer;

import java.io.File;
import java.util.UUID;
import java.util.HashMap;

/**
 * Created by guillaume on 29/09/16.
 */

public class Page implements ScanContainer, Parcelable {

    private Quadrangle quadrangle;
    private ImageType imageType;

    private Image originalImage;
    private Image enhancedImage;

    private Page(Context context, Image originalImage, HashMap scanOptions) {
        this.originalImage = originalImage;
        this.enhancedImage = new Image(context, UUID.randomUUID().toString() + "-enhanced.jpg");
        this.imageType = getImageTypeFromDefaultEnhancement((String)scanOptions.get("defaultEnhancement"));
    }

    public Page(Context context, String originalImagePath, HashMap scanOptions) {
        this(context, new Image(originalImagePath), scanOptions);
    }

    public Page(Context context, HashMap scanOptions) {
        this(context, new Image(context, UUID.randomUUID().toString() + "-original.jpg"), scanOptions);
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

    public ImageType getImageTypeFromDefaultEnhancement(String defaultEnhancement) {
        if (defaultEnhancement == null) {
            return null;
        }
        if (defaultEnhancement.equals("color")) {
            return ImageType.COLOR;
        } else if (defaultEnhancement.equals("bw")) {
            return ImageType.BLACK_WHITE;
        } else if (defaultEnhancement.equals("whiteboard")) {
            return ImageType.WHITEBOARD;
        } else if (defaultEnhancement.equals("none")) {
            return ImageType.NONE;
        }

        return null;
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
