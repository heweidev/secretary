package com.hewei.secretary;

/**
 * Created by fengyinpeng on 2018/1/2.
 */

public class Constants {
    public static final String NOTE_ID = "note_id";

    public static final int MAX_TAG_NUMBER = 6;

    public static final String CONTENT_TYPE_JPEG = "image/jpeg";


    public static final String TAG1 = "tag1";
    public static final String TAG2 = "tag2";
    public static final String TAG3 = "tag3";
    public static final String TAG4 = "tag4";
    public static final String TAG5 = "tag5";
    public static final String TAG6 = "tag6";

    public static final String TITLE = "key";
    public static final String DESC = "desc";
    public static final String DATA_TYPE = "data_type";
    public static final String STRING_DATA = "ds";
    public static final String NUM_DATA = "ns";
    public static final String DATE = "date";

    public static final String EXTRA1 = "extra1";
    public static final String EXTRA2 = "extra2";
    public static final String EXTRA3 = "extra3";
    public static final String EXTRA4 = "extra4";
    public static final String EXTRA5 = "extra5";
    public static final String EXTRA6 = "extra6";

    // basic type
    public static final int DT_UNKNOWN = -1;
    public static final int DT_NUMBER = 1;
    public static final int DT_STRING = 2;
    public static final int DT_URI = 3;
    public static final int DT_TIME = 4;
    public static final int DT_BOOLEAN = 5;
    public static final int DT_MAP = 6;

    // container type 不允许嵌套， 容器的数据项只允许是基础类型
    public static final int DT_LIST = 0x100;

    public static int listType(int dt) {
        return DT_LIST + (dt & 0xff);
    }

    public static int itemType(int dt) {
        return dt & 0xff;
    }

    public static boolean isList(int dt) {
        return dt == Constants.DT_LIST;
    }
}
