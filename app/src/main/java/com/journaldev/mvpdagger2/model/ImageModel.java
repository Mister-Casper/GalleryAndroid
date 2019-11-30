package com.journaldev.mvpdagger2.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {

    private Uri photo;
    private Boolean isLike;

    protected ImageModel(Parcel in) {
        photo = in.readParcelable(Uri.class.getClassLoader());
        byte tmpIsLike = in.readByte();
        isLike = tmpIsLike == 0 ? null : tmpIsLike == 1;
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    public Uri getImage() {
        return photo;
    }

    public Boolean getLike() {
        return isLike;
    }
    public void setLike(Boolean value) {
        isLike = value;
    }

    public ImageModel(Uri photo , Boolean isLike) {
        this.photo = photo;
        this.isLike = isLike;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(photo, i);
        parcel.writeByte((byte) (isLike == null ? 0 : isLike ? 1 : 2));
    }
}
