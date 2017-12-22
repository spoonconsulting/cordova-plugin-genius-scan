package com.thegrizzlylabs.geniusscan.cordova.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.thegrizzlylabs.geniusscan.sdk.core.Scan;

import java.io.File;

/**
 * Created by guillaume on 29/09/16.
 */

public class Image implements Scan, Parcelable {

    public Image(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    private String absolutePath;

    @Override
    public String getAbsolutePath(Context context) {
        return this.absolutePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Image> CREATOR
            = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(absolutePath);
    }

    private Image(Parcel in) {
        absolutePath = in.readString();
    }
}
