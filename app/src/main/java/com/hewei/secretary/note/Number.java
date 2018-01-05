package com.hewei.secretary.note;

import com.hewei.secretary.Constants;

/**
 * Created by fengyinpeng on 2018/1/5.
 */

public class Number extends NoteTemplate {
    private double mData;

    @Override
    public String getData() {
        return String.valueOf(mData);
    }

    @Override
    public int getType() {
        return Constants.DT_NUMBER;
    }

    public static final Creator<Number> CREATOR = new Creator<Number>() {
        @Override
        public Number createFromString(String source) throws Exception {
            Number number = new Number();
            number.mData = Double.parseDouble(source);
            return number;
        }

        @Override
        public Number[] newArray(int size) {
            return new Number[size];
        }
    };
}
