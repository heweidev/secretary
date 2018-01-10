package com.hewei.secretary;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hewei.secretary.note.FileTemplate;
import com.hewei.secretary.note.Note;
import com.hewei.secretary.note.tojson.NoteTemplateAdapter;
import com.hewei.secretary.note.tojson.NoteTypeAdapter;

import org.junit.Test;

/**
 * Created by fengyinpeng on 2018/1/10.
 */

public class GsonTest {
    @Test
    public void testGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(FileTemplate.class, new NoteTemplateAdapter());
        builder.registerTypeAdapter(Note.class, new NoteTypeAdapter());

        String strData = "{\n" +
                "\ttitle: \"this is title\",\n" +
                "\tdesc: \"this is desc\",\n" +
                "\ttags: [\"A\",\n" +
                "\t\"B\"],\n" +
                "\tdata: [{\"type\": 1, \"data\": 1}, {\"type\": 1, \"data\": 2}, {\"type\": 1, \"data\": 3}]\n" +
                "}";
        Gson gson = builder.create();
        Note note = gson.fromJson(strData, Note.class);
        System.out.print(note.desc);
    }

    @Test
    public void testJson2() {

    }
}
