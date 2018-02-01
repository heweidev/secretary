package com.hewei.secretary;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.hewei.secretary.note.Note;
import com.hewei.secretary.note.NoteTemplate;
import com.hewei.secretary.note.tojson.NoteTemplateAdapter;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by fengyinpeng on 2018/1/10.
 */

public class GsonTest {
    public static class User {
        public String name;
        public String phoneNum;
    }

    public static class CityUser extends User {
        public String city;
        List<String> addr;
    }

    public static final class Message<T> {
        int code;
        String msg;
        T data;
        WeekDay day;
    }

    public static final class MyList<T> implements List<T> {

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<T> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T1> T1[] toArray(@NonNull T1[] a) {
            return null;
        }

        @Override
        public boolean add(T t) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends T> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NonNull Collection<? extends T> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public T get(int index) {
            return null;
        }

        @Override
        public T set(int index, T element) {
            return null;
        }

        @Override
        public void add(int index, T element) {

        }

        @Override
        public T remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<T> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<T> listIterator(int index) {
            return null;
        }

        @NonNull
        @Override
        public List<T> subList(int fromIndex, int toIndex) {
            return null;
        }
    }

    public enum WeekDay {
        $Monday, $Tuesday, $Wednesday, $Thursday, $Friday, $Satursday, $Sunday;
    }

    private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
        private Class<T> mCls;

        public EnumTypeAdapter(Class<T> classOfT) {
            mCls = classOfT;
        }

        @Override public T read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            try {
                return Enum.valueOf(mCls, "$" + in.nextString());
            } catch (Exception e) {
                return null;
            }
        }

        @Override public void write(JsonWriter out, T value) throws IOException {
            out.value(value == null ? null : value.toString().substring(1));
        }
    }

    public static final TypeAdapterFactory ENUM_FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            Class<? super T> rawType = typeToken.getRawType();
            if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
                return null;
            }
            if (!rawType.isEnum()) {
                rawType = rawType.getSuperclass(); // handle anonymous subclasses
            }
            return (TypeAdapter<T>) new EnumTypeAdapter(rawType);
        }
    };

    @Test
    public void testGson() {
        GsonBuilder builder = new GsonBuilder();
        //builder.registerTypeAdapter(FileTemplate.class, new NoteTemplateAdapter());
        //builder.registerTypeAdapter(Note.class, new NoteTypeAdapter());
        builder.registerTypeHierarchyAdapter(NoteTemplate.class, new NoteTemplateAdapter());
        builder.registerTypeAdapterFactory(ENUM_FACTORY);

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


        CityUser user = new CityUser();
        user.addr = Collections.singletonList("1");

        Class<?> cls = user.addr.getClass();
        int  a = 1;
        a++;

        try {
            Field field = CityUser.class.getDeclaredField("addr");
            Type t = field.getType();

            int  aa =0;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


        /*
        CityUser user = new CityUser();
        user.name = "Hello";
        user.phoneNum = "12443443";
        user.city = "ZZ";
        user.addr = Collections.singletonList("Street");

        String strUser = gson.toJson(user);
        System.out.println(strUser);
        */

        /*
        Message<String> msg = new Message<>();
        msg.code = 12;
        msg.msg = "ok";
        msg.data = "Hello";
        msg.day = WeekDay.$Friday;

        String str = gson.toJson(msg);
        System.out.println(str);

        Message msg2 = gson.fromJson(str, Message.class);
        */
    }

    @Test
    public void testJson2() {

    }
}
