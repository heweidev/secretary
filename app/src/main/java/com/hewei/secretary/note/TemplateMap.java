package com.hewei.secretary.note;

import com.hewei.secretary.Constants;

/**
 * Created by fengyinpeng on 2018/1/5.
 */

public class TemplateMap {
    public static String type2Class(int type) {
        switch (type) {
            case Constants.DT_MAP:
                return MapTemplate.class.getName();
        }

        return null;
    }

    public static NoteTemplate getTemplate(int type, String data) throws Exception {
        if (type == Constants.DT_NUMBER) {
            return Number.CREATOR.createFromString(data);
        } else if (type == Constants.DT_STRING) {
            return new StringTemplate(data);
        } else if (type == Constants.DT_URI) {
            return FileTemplate.CREATOR.createFromString(data);
        }

        return null;
    }
}
