package com.hewei.secretary.note;

import com.hewei.secretary.Constants;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

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
        if (type == Constants.DT_LIST) {
            int itemType = Constants.itemType(type);
            List<NoteTemplate> list = new ArrayList<>();
            JSONArray array = new JSONArray(data);
            final int SIZE = array.length();
            for (int i = 0; i < SIZE; i++) {
                list.add(getTemplate(itemType, array.getString(i)));
            }
            return new ListTemplate<>(list);
        } else {
            if (type == Constants.DT_NUMBER) {
                return Number.CREATOR.createFromString(data);
            } else if (type == Constants.DT_STRING) {
                return new StringTemplate(data);
            } else if (type == Constants.DT_URI) {
                return FileTemplate.CREATOR.createFromString(data);
            }
        }

        return null;
    }
}
