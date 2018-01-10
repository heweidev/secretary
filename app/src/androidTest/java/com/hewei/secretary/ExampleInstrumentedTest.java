package com.hewei.secretary;

import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hewei.secretary.note.FileTemplate;
import com.hewei.secretary.note.tojson.NoteTemplateAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "TestLog";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.hewei.secretary", appContext.getPackageName());
    }

    @Test
    public void testGson() {
        FileTemplate fileTemplate = new FileTemplate(Uri.parse("http://localhost/123"), "image/jpeg", "this is desc");

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(FileTemplate.class, new NoteTemplateAdapter());
        // if PointAdapter didn't check for nulls in its read/write methods, you should instead use
        // builder.registerTypeAdapter(Point.class, new PointAdapter().nullSafe());

        Gson gson = builder.create();
        String str = gson.toJson(fileTemplate);
        Log.d(TAG, str);

        FileTemplate newData = gson.fromJson(str, FileTemplate.class);
        assertEquals(newData.contentType, fileTemplate.contentType);
    }
}
