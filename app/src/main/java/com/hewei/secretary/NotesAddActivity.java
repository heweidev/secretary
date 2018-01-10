package com.hewei.secretary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hewei.secretary.note.ListTemplate;
import com.hewei.secretary.note.Note;
import com.hewei.secretary.note.NoteTemplate;

import java.util.ArrayList;
import java.util.HashSet;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

/**
 * Created by fengyinpeng on 2018/1/3.
 */

public class NotesAddActivity extends AppCompatActivity implements View.OnClickListener, NoteAddView {
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
            String msg = checkData();
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                return;
            }

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
        DialogFragment newFragment = AddNoteDataFragment.newInstance("",
                new AddNoteDataFragment.OnGetDataListener() {
            @Override
            public void onGetData(NoteTemplate data) {
                addNote(data);
            }
        });

        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    private String checkData() {
        String title = mTitleEdit.getText().toString();
        if (TextUtils.isEmpty(title)) {
            return "title can not be null";
        }
        String desc = mDescEdit.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            return "desc can not be null";
        }
        if (mTags.isEmpty()) {
            return "tags can not be null";
        }

        return null;
    }

    private void addNote(NoteTemplate data) {
        String title = mTitleEdit.getText().toString();
        String desc = mDescEdit.getText().toString();
        Note note = new Note(title, desc, new ArrayList<>(mTags));
        if (Constants.isList(data.getType())) {
            note.data = ((ListTemplate) data).getItems();
        } else {
            ArrayList<NoteTemplate> list = new ArrayList<>();
            list.add(data);
            note.data = list;
        }

        mPresenter.addNote(note);
    }

    @Override
    public void onAddFinish(String id) {
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
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
