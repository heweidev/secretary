package com.hewei.secretary.note.tojson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.hewei.secretary.note.Note;
import com.hewei.secretary.note.NoteTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengyinpeng on 2018/1/10.
 */

public class NoteTypeAdapter extends TypeAdapter<Note> {
    @Override
    public void write(JsonWriter out, Note value) throws IOException {
        out.beginObject();
        out.name("title");
        out.value(value.title);
        out.name("desc");
        out.value(value.desc);
        out.name("tags");
        out.beginArray();
        for (String tag : value.tags) {
            out.value(tag);
        }
        out.endArray();

        out.name("data");
        out.beginArray();
        NoteTemplateAdapter adapter = new NoteTemplateAdapter();
        for (NoteTemplate item : value.data) {
            adapter.write(out, item);
        }
        out.endArray();

        out.endObject();
    }

    @Override
    public Note read(JsonReader in) throws IOException {
        String title = null;
        String desc = null;
        List<String> tags = null;
        List<NoteTemplate> data = null;

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if ("title".equals(name)) {
                title = in.nextString();
            } else if ("desc".equals(name)) {
                desc = in.nextString();
            } else if ("tags".equals(name)) {
                tags = new ArrayList<>();
                in.beginArray();
                while (in.hasNext()) {
                    tags.add(in.nextString());
                }
                in.endArray();

                if (tags.isEmpty()) {
                    tags = null;
                }
            } else if ("data".equals(name)) {
                in.beginArray();
                data = new ArrayList<>();
                NoteTemplateAdapter adapter = new NoteTemplateAdapter();
                while (in.hasNext()) {
                    data.add(adapter.read(in));
                }
                in.endArray();

                if (data.isEmpty()) {
                    data = null;
                }
            }
        }
        in.endObject();

        Note note = new Note(title, desc, tags);
        note.data = data;
        return note;
    }
}
