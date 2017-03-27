package ar.com.nicolasquartieri.remote;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;

import ar.com.nicolasquartieri.R;

/**
 * Represents an API error response.
 * @author Nicolas Quartieri (nicolas.quartieri@gmail.com)
 */
public class ApiErrorResponse implements Parcelable {
    /** Error type. It is not deserialize. */
    private int mErrorType = ERROR_SERVICE;
    /** Http status code. It is not deserialize. */
    private int mStatusCode = INVALID_ERROR_CODE;
    /** Error code. */
    @SerializedName("code")
    private int mCode = INVALID_ERROR_CODE;

    /** Response error. */
    public static final String EXTRA_RESPONSE_ERROR = "EXTRA_RESPONSE_ERROR";

    /** Error type. */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ERROR_NO_ERROR, ERROR_CONNECTION, ERROR_APPLICATION, ERROR_SERVICE})
    public @interface ErrorType {}
    /** No error id. */
    public static final int ERROR_NO_ERROR = 0;
    /** Connection error. */
    public static final int ERROR_CONNECTION = 1;
    /** Application error. */
    public static final int ERROR_APPLICATION = 2;
    /** Service error. */
    public static final int ERROR_SERVICE = 3;

    /** Invalid error code. */
    private static final int INVALID_ERROR_CODE = -1;

    /** Creator for parcelable implementation. */
    public static final Creator<ApiErrorResponse> CREATOR
            = new Creator<ApiErrorResponse>() {
        /**
         * Constructor.
         * @param in the parcel.
         * @return a instance.
         */
        public ApiErrorResponse createFromParcel(Parcel in) {
            return new ApiErrorResponse(in);
        }

        /**
         * Creates an array of instances.
         * @param size the array size.
         * @return an array of instances.
         */
        public ApiErrorResponse[] newArray(int size) {
            return new ApiErrorResponse[size];
        }
    };

    /**
     * Constructor for parcelable implementation.
     * @param in the parcel.
     */
    private ApiErrorResponse(Parcel in) {
        mErrorType = in.readInt();
        mStatusCode = in.readInt();
        mCode = in.readInt();
    }

    /**
     * Constructor.
     * @param errorType the error type, can be ERROR_NO_ERROR, ERROR_CONNECTION, ERROR_APPLICATION,
     * ERROR_SERVICE.
     */
    public ApiErrorResponse(@ErrorType int errorType) {
        this(errorType, INVALID_ERROR_CODE);
    }

    /**
     * Constructor.
     * @param errorType the error type, can be ERROR_NO_ERROR, ERROR_CONNECTION, ERROR_APPLICATION,
     * ERROR_SERVICE.
     * @param statusCode the status code.
     */
    public ApiErrorResponse(@ErrorType int errorType, int statusCode) {
        mErrorType = errorType;
        mStatusCode = statusCode;
    }

    /**
     * Constructor.
     * @param errorType the error type, can be ERROR_NO_ERROR, ERROR_CONNECTION, ERROR_APPLICATION,
     * ERROR_SERVICE.
     * @param statusCode the response status code.
     * @param errorCode The error code.
     */
    public ApiErrorResponse(@ErrorType final int errorType,
            final int statusCode, final int errorCode) {
        mErrorType = errorType;
        mStatusCode = statusCode;
        mCode = errorCode;
    }

    /**
     * Sets the http status code.
     * @param statusCode the status code.
     */
    public void setStatusCode(int statusCode) {
        mStatusCode = statusCode;
    }

    /**
     * Returns the http status code.
     * @return the http status code.
     */
    public int getStatusCode() {
        return mStatusCode;
    }

    /**
     * Returns the error type.
     * @return the error type.
     */
    public int getErrorType() {
        return mErrorType;
    }

    public boolean isConnectionError() {
        return mErrorType == ERROR_CONNECTION;
    }

    public boolean isApplicationError() {
        return mErrorType == ERROR_APPLICATION;
    }

    public boolean isServiceError() {
        return mErrorType == ERROR_SERVICE;
    }

    /**
     * Returns the error message resource.
     * @return an message resource.
     */
    public @StringRes
    int getErrorMessage() {
        return R.string.generic_error;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mErrorType);
        out.writeInt(mStatusCode);
        out.writeInt(mCode);
    }
}
