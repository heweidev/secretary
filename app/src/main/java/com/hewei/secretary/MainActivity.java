package com.hewei.secretary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private NotesAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_add).setOnClickListener(this);

        EditText editText = (EditText) findViewById(R.id.search_bar);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doSearch(s.toString());
            }
        });

        mListView = (ListView) findViewById(R.id.list);
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    public void doSearch(String str) {
        Bundle args = new Bundle();
        args.putString("search_key", str);
        getSupportLoaderManager().restartLoader(0, args, this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_add) {
            Intent intent = new Intent(this, NotesAddActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (args == null) {
            return new CursorLoader(getApplicationContext(), NotesProvider.URI_NOTES, null,
                    null, null, null);
        } else {
            String selection = Constants.TITLE + " like ?";
            String[] selectionArgs = new String[] {
                    '%' + args.getString("search_key") + '%',
            };

            return new CursorLoader(getApplicationContext(), NotesProvider.URI_NOTES, null,
                    selection, selectionArgs, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter == null) {
            mAdapter = new NotesAdapter(getApplicationContext(), data);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private static final class ViewHolder {
        public ViewHolder(View root) {
            titleView = (TextView) root.findViewById(R.id.title);
            descView = (TextView) root.findViewById(R.id.desc);
            tagView = (TextView) root.findViewById(R.id.tags);
        }

        TextView titleView;
        TextView descView;
        TextView tagView;
    }

    private class NotesAdapter extends CursorAdapter {
        public NotesAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View rootView = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
            ViewHolder holder = new ViewHolder(rootView);
            rootView.setTag(holder);
            return rootView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            int idxTitle = cursor.getColumnIndex(Constants.TITLE);
            int idxDesc = cursor.getColumnIndex(Constants.DESC);
            int idxTags = cursor.getColumnIndex(Constants.TAG1);

            ViewHolder holder = (ViewHolder) view.getTag();
            String title = cursor.getString(idxTitle);
            String desc = cursor.getString(idxDesc);
            String tag1 = cursor.getString(idxTags);

            holder.titleView.setText(title);
            holder.descView.setText(desc);
            holder.tagView.setText(tag1);
        }
    }
}
