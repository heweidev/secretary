package com.hewei.secretary;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class NotesProvider extends ContentProvider {
    public static final int DB_VERSION = 105;
    public static final String DB_NAME = "db_notes";
    public static final String AUTHORITIES = "secretary.notes";

    public static final String TYPE_DATA = "";
    public static final String TYPE_NOTE = "";

    private MyDbHelper mDbHelper;

    private static final int CODE_NOTES_LIST = 1;
    private static final int CODE_NOTES_ITEM = 2;
    private static final int CODE_DATA_LIST = 3;
    private static final int CODE_DATA_ITEM = 4;

    public static final String TBL_NOTES = "tbl_notes";
    public static final String TBL_DATA = "tbl_data";

    public static final Uri URI_NOTES = Uri.parse("content://" + AUTHORITIES + "/notes");
    public static final Uri URI_DATA = Uri.parse("content://" + AUTHORITIES + "/data");

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITIES, "notes", CODE_NOTES_LIST);
        sUriMatcher.addURI(AUTHORITIES, "notes/#", CODE_NOTES_ITEM);

        sUriMatcher.addURI(AUTHORITIES, "data", CODE_DATA_LIST);
        sUriMatcher.addURI(AUTHORITIES, "data/#", CODE_DATA_ITEM);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MyDbHelper(getContext(), DB_NAME, null, DB_VERSION);
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        int code = sUriMatcher.match(uri);
        if (code == CODE_NOTES_LIST) {

        } else if (code == CODE_NOTES_ITEM) {

        } else if (code == CODE_DATA_LIST) {

        } else if (code == CODE_DATA_ITEM) {

        } else {
            // not support
        }

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int code = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        if (code == CODE_NOTES_LIST) {
            long id = db.insert(TBL_NOTES, null, values);
            if (id != -1) {
                return Uri.withAppendedPath(URI_NOTES, String.valueOf(id));
            }
        } else if (code == CODE_DATA_LIST) {
            long id = db.insert(TBL_NOTES, null, values);
            if (id != -1) {
                return Uri.withAppendedPath(URI_DATA, String.valueOf(id));
            }
        } else {
            // not support
        }

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int code = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        if (code == CODE_NOTES_LIST) {
            return db.query(TBL_NOTES, projection, selection, selectionArgs, null, null, sortOrder);
        } else if (code == CODE_NOTES_ITEM) {
            String id = uri.getLastPathSegment();
            String sel = "_id = ?";
            String[] selArgs = new String[] {
                 id,
            };
            return db.query(TBL_NOTES, projection, sel, selArgs, null, null, null);
        } else if (code == CODE_DATA_LIST) {
            return db.query(TBL_DATA, projection, selection, selectionArgs, null, null, sortOrder);
        } else if (code == CODE_DATA_ITEM) {
            String id = uri.getLastPathSegment();
            String sel = "_id = ?";
            String[] selArgs = new String[] {
                    id,
            };
            return db.query(TBL_DATA, projection, sel, selArgs, null, null, null);
        } else {
            // not support
        }

        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    static final class MyDbHelper extends SQLiteOpenHelper {
        private static final String SQL_CREATE_NOTES = "CREATE TABLE if not exists tbl_notes(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.TITLE + " TEXT NOT NULL, " +
                Constants.DESC + " TEXT, " +
                Constants.TAG1 + " TEXT, " +
                Constants.TAG2 + " TEXT, " +
                Constants.TAG3 + " TEXT, " +
                Constants.TAG4 + " TEXT, " +
                Constants.TAG5 + " TEXT, " +
                Constants.TAG6 + " TEXT, " +
                Constants.DATE + " TEXT " +
                ")";

        private static final String SQL_CREATE_DATA = "CREATE TABLE if not exists tbl_data(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.DATA_TYPE + " INTEGER, " +
                Constants.STRING_DATA + " TEXT, " +
                Constants.NUM_DATA + " NUMERIC, " +
                Constants.NOTE_ID + " INTEGER, " +
                Constants.DATE + " TEXT, " +
                Constants.EXTRA1 + " INTEGER, " +
                Constants.EXTRA2 + " INTEGER, " +
                Constants.EXTRA3 + " TEXT, " +
                Constants.EXTRA4 + " TEXT, " +
                Constants.EXTRA5 + " TEXT, " +
                Constants.EXTRA6 + " TEXT " +
                ")";

        public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_NOTES);
            db.execSQL(SQL_CREATE_DATA);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists tbl_notes");
            db.execSQL("drop table if exists tbl_data");
            onCreate(db);
        }
    }
}
