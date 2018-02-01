package com.hewei.secretary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hewei.secretary.note.Note;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

public class Main2Activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Note>>,
        View.OnClickListener, AdapterView.OnItemClickListener, OKHttpLoader.OnLoadErrorListener {
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
        mListView.setOnItemClickListener(this);
        getSupportLoaderManager().initLoader(0, null, this);

        request(Network.getInstance().getApi().listNotes(""))
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Note>>() {
                    @Override
                    public void accept(List<Note> notes) throws Exception {

                    }
                });
    }

    public <T> Observable<T> request(final Call<T> rawCall) {
        return ObservableLoader.create(getApplicationContext(), getSupportLoaderManager(), rawCall);
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
    public Loader<List<Note>> onCreateLoader(int id, Bundle args) {
        String searchKey = "";
        if (args != null) {
            searchKey = args.getString("search_key");
        }

        return new OKHttpLoader<>(getApplicationContext(), Network.getInstance().getApi().listNotes(searchKey), this);
    }

    @Override
    public void onLoadFinished(Loader<List<Note>> loader, List<Note> data) {
        if (mAdapter == null) {
            mAdapter = new NotesAdapter(getApplicationContext(), data);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.mNotes = data;
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Note>> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Note note = mAdapter.getItem(position);
        Intent intent = new Intent(this, NoteDetailsActivity.class);
        intent.putExtra("id", note.id);
        startActivity(intent);
    }



    @Override
    public void onLoadError(Throwable t) {
        t.printStackTrace();
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
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

    private class NotesAdapter extends BaseAdapter {
        private List<Note> mNotes;
        private Context mContext;

        public NotesAdapter(Context context, List<Note> notes) {
            mContext = context;
            mNotes = notes;
        }

        public View newView(Context context, Note note, ViewGroup parent) {
            View rootView = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
            ViewHolder holder = new ViewHolder(rootView);
            rootView.setTag(holder);
            return rootView;
        }

        public void bindView(View view, Context context, Note note) {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.titleView.setText(note.title);
            holder.descView.setText(note.desc);
            //holder.tagView.setText(note.tags.get(0));
        }

        @Override
        public int getCount() {
            return mNotes != null ? mNotes.size() : 0;
        }

        @Override
        public Note getItem(int position) {
            return mNotes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Note note = getItem(position);
            if (convertView == null) {
                convertView = newView(mContext, note, parent);
            }

            bindView(convertView, mContext, note);
            return convertView;
        }
    }
}
