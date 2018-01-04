package com.hewei.secretary;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

/**
 * Created by fengyinpeng on 2018/1/3.
 */

public class NotesAddActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mTitleEdit;
    private EditText mTagEdit;
    private EditText mDescEdit;
    private TextView mTagsView;
    private HashSet<String> mTags = new HashSet<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        mTitleEdit = findViewById(R.id.title);
        mDescEdit = findViewById(R.id.desc);
        mTagEdit = findViewById(R.id.et_tag);
        mTagsView = findViewById(R.id.tags);

        mTagEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_DONE) {
                    String tag = mTagEdit.getText().toString();
                    if (!TextUtils.isEmpty(tag)) {
                        mTags.add(tag);
                        updateTags();
                    }
                    return true;
                }

                return false;
            }
        });

        findViewById(R.id.btn_add).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_add) {
            showDataTypeSelectDialog();
            //addNote("杜兰特", "篮球明星", "NBA", "篮球");
        }
    }

    private void updateTags() {
        StringBuilder builder = new StringBuilder();
        for (String tag : mTags) {
            builder.append(tag);
            builder.append(' ');
        }
        mTagsView.setText(builder.toString());
    }

    private void showDataTypeSelectDialog() {
        new AlertDialog.Builder(this).setItems(R.array.data_types,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            showInputDialog(which, R.string.num_input_hint);
                        } else if (which == 1) {
                            showInputDialog(which, R.string.text_input_hint);
                        } else if (which == 2) {

                        } else if (which == 3) {

                        } else if (which == 4) {

                        }
                    }
                }).show();
    }

    private String addNote() {
        String title = mTitleEdit.getText().toString();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "title can not be null", Toast.LENGTH_SHORT).show();
            return null;
        }
        String desc = mDescEdit.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            Toast.makeText(this, "desc can not be null", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (mTags.isEmpty()) {
            Toast.makeText(this, "tags can not be null", Toast.LENGTH_SHORT).show();
            return null;
        }

        String[] tags = new String[mTags.size()];
        mTags.toArray(tags);
        return addNote(title, desc, tags);
    }

    private String addNote(String title, String desc, String... tags) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        String strDate = dateFormat.format(new Date());

        ContentValues values = new ContentValues();
        values.put(Constants.TITLE, title);
        values.put(Constants.DESC, desc);
        values.put(Constants.DATE, strDate);

        if (tags != null) {
            for (int i = 0; i < Math.min(Constants.MAX_TAG_NUMBER, tags.length); i++) {
                values.put("tag" + (i+1), tags[i]);
            }
        }

        Uri uri = getContentResolver().insert(NotesProvider.URI_NOTES, values);
        return uri.getLastPathSegment();
    }

    private void addNumber(double va) {
        String id = addNote();
        if (id == null) {
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        String strDate = dateFormat.format(new Date());

        ContentValues dataValue = new ContentValues();
        dataValue.put(Constants.DATA_TYPE, Constants.DT_NUMBER);
        dataValue.put(Constants.NUM_DATA, va);
        dataValue.put(Constants.NOTE_ID, id);
        dataValue.put(Constants.DATE, strDate);

        getContentResolver().insert(NotesProvider.URI_DATA, dataValue);
    }

    private void addText(String s) {
        String id = addNote();
        if (id == null) {
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.CHINA);
        String strDate = dateFormat.format(new Date());

        ContentValues dataValue = new ContentValues();
        dataValue.put(Constants.DATA_TYPE, Constants.DT_STRING);
        dataValue.put(Constants.STRING_DATA, s);
        dataValue.put(Constants.NOTE_ID, id);
        dataValue.put(Constants.DATE, strDate);

        getContentResolver().insert(NotesProvider.URI_DATA, dataValue);
    }

    private void showInputDialog(final int tag, @StringRes int hint) {
        final EditText editText = new EditText(this);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    if (tag == 0) {
                        try {
                            double va = Double.parseDouble(editText.getText().toString());
                            addNumber(va);
                        } catch (Exception e) {}
                    } else if (tag == 1) {
                        addText(editText.getText().toString());
                    }
                }
            }
        };

        editText.setHint(hint);
        new AlertDialog.Builder(this)
                .setView(editText)
                .setPositiveButton(R.string.ok, listener)
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
