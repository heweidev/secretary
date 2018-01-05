package com.hewei.secretary;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testRefact() throws Exception {
        Class cls = Class.forName("com.hewei.secretary.note.ListTemplate");
        Field field = cls.getDeclaredField("CREATOR");
        field.toString();
    }
}