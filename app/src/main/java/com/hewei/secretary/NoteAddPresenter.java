package com.hewei.secretary;

import android.text.TextUtils;

import com.hewei.secretary.domain.NoteIdRequest;
import com.hewei.secretary.note.Note;
import com.hewei.secretary.note.NoteTemplate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fengyinpeng on 2018/1/5.
 */

interface NoteAddPresenter {
    void bindView(NoteAddView view);
    void addNote(Note note);
    void addData(NoteTemplate data);
    void addNote(Note note, NoteTemplate data);
}

interface NoteAddView {
    void onAddFinish();
    void onAddError(Throwable throwable);
}

/*
class AddtoProviderPresenter implements NoteAddPresenter {
    public ContentResolver mContentResolver;

    @Override
    public void bindView(NoteAddView view) {

    }

    @Override
    public void addNote(Note note) {

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

        Uri uri = mContentResolver.insert(NotesProvider.URI_NOTES, values);
        return uri.getLastPathSegment();
    }

    /*
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

        mContentResolver.insert(NotesProvider.URI_DATA, dataValue);
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

        mContentResolver.insert(NotesProvider.URI_DATA, dataValue);
    }
}   */

class AddtoDatabasePresenter implements NoteAddPresenter {
    private NoteAddView mView;

    @Override
    public void bindView(NoteAddView view) {
        mView = view;
    }

    @Override
    public void addNote(Note note) {
        Network.getInstance().getApi().addNote(note).enqueue(new Callback<NoteIdRequest>() {
            @Override
            public void onResponse(Call<NoteIdRequest> call, Response<NoteIdRequest> response) {
                if (mView != null) {
                    mView.onAddFinish();
                }
            }

            @Override
            public void onFailure(Call<NoteIdRequest> call, Throwable t) {
                if (mView != null) {
                    mView.onAddError(t);
                }
            }
        });
    }

    private void addDataImpl(NoteTemplate data) {
        Network.getInstance().getApi().addData(data).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (mView != null) {
                    mView.onAddFinish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (mView != null) {
                    mView.onAddError(t);
                }
            }
        });
    }

    @Override
    public void addData(NoteTemplate data) {
        if (TextUtils.isEmpty(data.noteId)) {
            if (mView != null) {
                mView.onAddError(new Exception("noteId is null"));
            }
            return;
        }

        addDataImpl(data);
    }

    @Override
    public void addNote(Note note, final NoteTemplate data) {
        Network.getInstance().getApi().addNote(note).enqueue(new Callback<NoteIdRequest>() {
            @Override
            public void onResponse(Call<NoteIdRequest> call, Response<NoteIdRequest> response) {
                data.noteId = response.body().id;
                addDataImpl(data);
            }

            @Override
            public void onFailure(Call<NoteIdRequest> call, Throwable t) {
                if (mView != null) {
                    mView.onAddError(t);
                }
            }
        });
    }
}
