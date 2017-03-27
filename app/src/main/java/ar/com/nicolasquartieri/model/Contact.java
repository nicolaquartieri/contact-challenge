package ar.com.nicolasquartieri.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Contact implements Parcelable {
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("birth_date")
    private String birthDate;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("user_id")
    private String userId;
    private String photo;
    private String thumb;
    private List<Phones> phones;
    private List<Address> addresses;

    public String getFirstName() {
        return firstName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhoto() {
        return photo;
    }

    public String getThumb() {
        return thumb;
    }

    public List<Phones> getPhones() {
        return phones;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    protected Contact(Parcel in) {
        firstName = in.readString();
        birthDate = in.readString();
        lastName = in.readString();
        createdAt = in.readString();
        userId = in.readString();
        photo = in.readString();
        thumb = in.readString();
        if (in.readByte() == 0x01) {
            phones = new ArrayList<>();
            in.readList(phones, Phones.class.getClassLoader());
        } else {
            phones = null;
        }
        if (in.readByte() == 0x01) {
            addresses = new ArrayList<>();
            in.readList(addresses, Address.class.getClassLoader());
        } else {
            addresses = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(birthDate);
        dest.writeString(lastName);
        dest.writeString(createdAt);
        dest.writeString(userId);
        dest.writeString(photo);
        dest.writeString(thumb);
        if (phones == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(phones);
        }
        if (addresses == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(addresses);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
