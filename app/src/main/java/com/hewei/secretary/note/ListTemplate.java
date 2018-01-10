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

    public ListTemplate(List<T> data) {
        mData = data;
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    public List<T> getItems() {
        return mData;
    }

    public int size() {
        return mData.size();
    }

    @Override
    public String getData() {
        if (mData == null || mData.isEmpty()) {
            return "[]";
        }

        JSONArray array = new JSONArray();
        int i = 0;
        try {
            for (T d: mData) {
                array.put(i++, d.getData());
            }
        } catch (Exception e) {}

        return array.toString();
    }

    @Override
    public int getType() {
        return mData == null || mData.isEmpty() ?
                Constants.DT_LIST : Constants.listType(mData.get(0).getType());
    }

    public static final Creator<ListTemplate<? extends NoteTemplate>> CREATOR = new Creator<ListTemplate<? extends NoteTemplate>>() {
        @Override
        public ListTemplate<? extends NoteTemplate> createFromString(String source) throws Exception {
            ArrayList<NoteTemplate> list = new ArrayList();
            JSONObject object = new JSONObject(source);
            int dt = object.getInt("type");
            int itemType = Constants.itemType(dt);
            JSONArray array = object.getJSONArray("data");
            int count = array.length();
            for (int i = 0; i < count; i++) {
                list.add(TemplateMap.getTemplate(itemType, array.getString(i)));
            }

            ListTemplate template = new ListTemplate(list);
            return template;
        }

        @Override
        public ListTemplate[] newArray(int size) {
            return new ListTemplate[0];
        }
    };
}
