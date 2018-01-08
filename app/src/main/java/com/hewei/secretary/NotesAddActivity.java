package com.hewei.secretary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.hewei.secretary.note.FileTemplate;
import com.hewei.secretary.note.ListTemplate;
import com.hewei.secretary.note.Note;
import com.hewei.secretary.note.NoteTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

/**
 * Created by fengyinpeng on 2018/1/3.
 */

public class NotesAddActivity extends AppCompatActivity implements View.OnClickListener, NoteAddView {
    private static final int REQUEST_IMAGE = 10001;
    private EditText mTitleEdit;
    private EditText mTagEdit;
    private EditText mDescEdit;
    private TextView mTagsView;
    private HashSet<String> mTags = new HashSet<>();

    private NoteAddPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        mTitleEdit = findViewById(R.id.title);
        mDescEdit = findViewById(R.id.desc);
        mTagEdit = findViewById(R.id.et_tag);
        mTagsView = findViewById(R.id.tags);

        mPresenter = new AddtoDatabasePresenter();
        mPresenter.bindView(this);

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
        /*
        <item>@string/text</item>
        <item>@string/media</item>
        <item>@string/doc</item>
        <item>@string/link</item>
        <item>@string/table</item>
         */
        new AlertDialog.Builder(this).setItems(R.array.data_types,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            showInputDialog(which, R.string.text_input_hint);
                        } else if (which == 1) {
                            MultiImageSelector.create(NotesAddActivity.this)
                                    .showCamera(true)
                                    .count(100)
                                    .multi()
                                    .start(NotesAddActivity.this, REQUEST_IMAGE);
                        } else if (which == 2) {

                        } else if (which == 3) {

                        }
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (REQUEST_IMAGE == requestCode) {
            List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            if (paths == null || paths.isEmpty()) {
                return;
            }

            NoteTemplate noteData;
            if (paths.size() == 1) {
                noteData = new FileTemplate(Uri.parse("file://" + paths.get(0)), "image/jpeg", "");
            } else {
                List<FileTemplate> listData = new ArrayList<>();
                for (String p :paths) {
                    FileTemplate item = new FileTemplate(Uri.parse("file://" + p), "image/jpeg", "");
                    listData.add(item);
                }
                noteData = new ListTemplate<>(listData);
            }

            mPresenter.addData(noteData);
        }
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

        Note note = new Note(title, desc, new ArrayList<>(mTags));
        mPresenter.addNote(note);
        return "";
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
                            //addNumber(va);
                        } catch (Exception e) {}
                    } else if (tag == 1) {
                        //addText(editText.getText().toString());
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

    @Override
    public void onAddFinish() {

    }

    @Override
    public void onAddError(Throwable t) {
        String msg = t.getMessage();
        if (TextUtils.isEmpty(msg)) {
            msg = "add error!";
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
