package com.hewei.secretary.note;

import com.hewei.secretary.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengyinpeng on 2018/1/5.
 */

public class ListTemplate<T extends NoteTemplate> extends NoteTemplate {
    private List<T> mData;

    @Override
    public String getData() {
        StringBuilder builder = new StringBuilder();
        if (mData == null || mData.isEmpty()) {
            return "{}";
        }

        JSONArray array = new JSONArray();
        int i = 0;
        try {
            for (T d: mData) {
                array.put(i++, new JSONObject(d.getData()));
            }
        } catch (Exception e) {}

        return array.toString();
    }

    @Override
    public int getType() {
        return Constants.DT_LIST;
    }

    public static final Creator<ListTemplate<? extends NoteTemplate>> CREATOR = new Creator<ListTemplate<? extends NoteTemplate>>() {
        @Override
        public ListTemplate<? extends NoteTemplate> createFromString(String source) throws Exception {
            ListTemplate template = new ListTemplate();
            template.mData = new ArrayList();

            JSONArray array = new JSONArray(source);
            int count = array.length();
            for (int i = 0; i < count; i++) {
                JSONObject object = array.getJSONObject(i);
                int type = object.getInt("type");
                template.mData.add(TemplateMap.getTemplate(type, object.toString()));
            }
            return template;
        }

        @Override
        public ListTemplate[] newArray(int size) {
            return new ListTemplate[0];
        }
    };
}
