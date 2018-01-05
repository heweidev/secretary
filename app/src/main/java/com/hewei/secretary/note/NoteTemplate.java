package com.hewei.secretary.note;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fengyinpeng on 2018/1/5.
 */

public abstract class NoteTemplate {
    @SerializedName("data")
    public abstract String getData();

    @SerializedName("type")
    public abstract int getType();


    public String noteId;

    /**
     * Interface that must be implemented and provided as a public CREATOR
     * field that generates instances of your Parcelable class from a Parcel.
     */
    interface Creator<T> {
        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        public T createFromString(String source) throws Exception;

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         * initialized to null.
         */
        public T[] newArray(int size);
    }
}
