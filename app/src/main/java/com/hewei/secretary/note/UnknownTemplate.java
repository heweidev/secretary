package com.hewei.secretary.note;

import com.hewei.secretary.Constants;

/**
 * Created by fengyinpeng on 2018/1/10.
 */

public class UnknownTemplate extends NoteTemplate {
    public static UnknownTemplate INSTANCE = new UnknownTemplate();

    @Override
    public String getData() {
        return "";
    }

    @Override
    public int getType() {
        return Constants.DT_UNKNOWN;
    }
}
