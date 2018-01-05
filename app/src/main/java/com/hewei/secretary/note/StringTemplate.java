package com.hewei.secretary.note;

import com.hewei.secretary.Constants;

/**
 * Created by fengyinpeng on 2018/1/5.
 */

public class StringTemplate extends NoteTemplate {
    private String mData;

    public StringTemplate(String str) {
        mData = str;
    }

    @Override
    public String getData() {
        return mData;
    }

    @Override
    public int getType() {
        return Constants.DT_NUMBER;
    }
}
