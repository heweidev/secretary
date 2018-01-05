package com.hewei.secretary.note;

import com.hewei.secretary.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by fengyinpeng on 2018/1/5.
 */

public class MapTemplate extends NoteTemplate {
    private Map<String, ? extends NoteTemplate> mData;

    @Override
    public String getData() {
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, ? extends NoteTemplate> entry : mData.entrySet()) {
            try {
                jsonObject.put(entry.getKey(), new JSONObject(entry.getValue().getData()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonObject.toString();
    }

    @Override
    public int getType() {
        return Constants.DT_MAP;
    }

    public static final Creator<MapTemplate> CREATOR = new Creator<MapTemplate>() {
        @Override
        public MapTemplate createFromString(String source) throws Exception {
            MapTemplate template = new MapTemplate();
            template.mData = new HashMap<>();

            JSONObject object = new JSONObject(source);
            Iterator<String> iter = object.keys();

            while (iter.hasNext()) {
                String key = iter.next();
                //template.mData.put(key, object.getJSONObject(iter.next()));
            }

            return template;
        }

        @Override
        public MapTemplate[] newArray(int size) {
            return new MapTemplate[size];
        }
    };
}
