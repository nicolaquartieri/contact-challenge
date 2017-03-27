package ar.com.nicolasquartieri.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Phones implements Parcelable {
    private String number;
    private String type;

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    protected Phones(Parcel in) {
        number = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Phones> CREATOR = new Parcelable.Creator<Phones>() {
        @Override
        public Phones createFromParcel(Parcel in) {
            return new Phones(in);
        }

        @Override
        public Phones[] newArray(int size) {
            return new Phones[size];
        }
    };
}
