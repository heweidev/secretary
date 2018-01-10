package com.hewei.secretary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import com.hewei.secretary.note.FileTemplate;
import com.hewei.secretary.note.ListTemplate;
import com.hewei.secretary.note.NoteTemplate;
import com.hewei.secretary.note.StringTemplate;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by fengyinpeng on 2018/1/9.
 */

public class AddNoteDataFragment extends DialogFragment {
    private static final int REQUEST_IMAGE = 10001;

    private OnGetDataListener mDataListener;

    public interface OnGetDataListener {
        void onGetData(NoteTemplate data);
    }

    public static AddNoteDataFragment newInstance(String noteId, OnGetDataListener listener) {
        Bundle args = new Bundle();
        args.putString("noteId", noteId);
        AddNoteDataFragment fragment = new AddNoteDataFragment();
        fragment.setDataListener(listener);
        fragment.setArguments(args);
        return fragment;
    }

    public void setDataListener(OnGetDataListener listener) {
        mDataListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setItems(R.array.data_types,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            showInputDialog(which, R.string.text_input_hint);
                        } else if (which == 1) {
                            MultiImageSelector.create(getContext())
                                    .showCamera(true)
                                    .count(100)
                                    .multi()
                                    .start(AddNoteDataFragment.this, REQUEST_IMAGE);
                        } else if (which == 2) {

                        } else if (which == 3) {

                        }
                    }
                }).create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //super.onDismiss(dialog);
    }

    private void showInputDialog(final int tag, @StringRes int hint) {
        final EditText editText = new EditText(getContext());

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
                        StringTemplate data = new StringTemplate(editText.getText().toString());
                        postData(data);
                    }
                }
            }
        };

        editText.setHint(hint);
        new AlertDialog.Builder(getContext())
                .setView(editText)
                .setPositiveButton(R.string.ok, listener)
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
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
            postData(noteData);
        }
    }

    private void postData(NoteTemplate data) {
        if (mDataListener != null) {
            mDataListener.onGetData(data);
            super.onDismiss(getDialog());
        }
    }
}
