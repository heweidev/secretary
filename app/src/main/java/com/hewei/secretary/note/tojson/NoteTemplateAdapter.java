package com.hewei.secretary.note.tojson;

import android.text.TextUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.hewei.secretary.note.NoteTemplate;
import com.hewei.secretary.note.TemplateMap;
import com.hewei.secretary.note.UnknownTemplate;

import java.io.IOException;

/**
 * Created by fengyinpeng on 2018/1/10.
 */

public class NoteTemplateAdapter extends TypeAdapter<NoteTemplate> {
    @Override
    public void write(JsonWriter out, NoteTemplate value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("type");
        out.value(value.getType());
        out.name("data");
        out.value(value.getData());

        if (!TextUtils.isEmpty(value.noteId)) {
            out.name("noteId");
            out.value(value.noteId);
        }

        out.endObject();
    }

    @Override
    public NoteTemplate read(JsonReader in) throws IOException {
        int type = -1;
        String data = null;

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if ("type".equals(name)) {
                type = in.nextInt();
            } else if ("data".equals(name)) {
                data = in.nextString();
            }
        }
        in.endObject();

        try {
            return TemplateMap.getTemplate(type, data);
        } catch (Exception e) {
            return UnknownTemplate.INSTANCE;
        }
    }
}
