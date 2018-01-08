package com.hewei.secretary.note;

import android.net.Uri;

import com.hewei.secretary.Constants;

import org.json.JSONObject;

/**
 * Created by fengyinpeng on 2018/1/5.
 */

public class FileTemplate extends NoteTemplate {
    public String contentType;
    public Uri uri;
    public String desc;

    public FileTemplate(Uri uri, String contentType, String desc) {
        this.uri = uri;
        this.contentType = contentType;
        this.desc = desc;
    }

    @Override
    public String getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("contentType", contentType);
            object.put("uri", uri.toString());
            object.put("desc", desc);
        } catch (Exception e) {

        }

        return object.toString();
    }

    @Override
    public int getType() {
        return Constants.DT_URI;
    }

    public static final Creator<FileTemplate> CREATOR = new Creator<FileTemplate>() {
        @Override
        public FileTemplate createFromString(String source) throws Exception {
            JSONObject object = new JSONObject(source);
            String contentType = object.optString("contentType");
            Uri uri = Uri.parse(object.optString("uri"));
            String desc = object.optString("desc");
            FileTemplate template = new FileTemplate(uri, contentType, desc);
            return template;
        }

        @Override
        public FileTemplate[] newArray(int size) {
            return new FileTemplate[size];
        }
    };
}
