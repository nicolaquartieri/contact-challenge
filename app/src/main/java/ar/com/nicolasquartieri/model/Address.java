package ar.com.nicolasquartieri.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    private String work;
    private String home;

    public String getWork() {
        return work;
    }

    public String getHome() {
        return home;
    }

    protected Address(Parcel in) {
        work = in.readString();
        home = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(work);
        dest.writeString(home);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
