package com.hewei.secretary;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hewei.secretary.note.FileTemplate;
import com.hewei.secretary.note.ListTemplate;
import com.hewei.secretary.note.Note;
import com.hewei.secretary.note.NoteTemplate;
import com.hewei.secretary.note.StringTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class NoteDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Note>,
        OKHttpLoader.OnLoadErrorListener {
    private static final String TAG = "NoteDetailsActivity";
    private ListView mListView;
    private NoteDetailsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        mListView = (ListView) findViewById(R.id.list);
        Bundle args = new Bundle();
        args.putString("noteId", "5a4f1e554e5f2f2c54d3a66e");
        getSupportLoaderManager().initLoader(0, args, this);
    }

    @Override
    public Loader<Note> onCreateLoader(int id, Bundle args) {
        if (args == null) {
            Util.logicError("invalid params");
            return null;
        }

        String noteId = args.getString("noteId");
        Call<Note> call = Network.getInstance().getApi().getNote(noteId);
        return new OKHttpLoader<>(getApplicationContext(), call, this);
    }

    @Override
    public void onLoadFinished(Loader<Note> loader, Note note) {
        List<NoteTemplate> dataList = note.data;
        if (mAdapter == null) {
            mAdapter = new NoteDetailsAdapter(dataList);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.mData = dataList;
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Note> loader) {

    }

    @Override
    public void onLoadError(Throwable t) {
        t.printStackTrace();
    }

    private interface ItemView {
        boolean accept(NoteTemplate data);
        View createView(int position, ViewGroup parent);
        void bindView(int position, NoteTemplate data);
    }

    private class StringItem implements ItemView {
        private TextView mTextView;

        @Override
        public boolean accept(NoteTemplate data) {
            int dt = data.getType();
            return dt == Constants.DT_STRING ||
                    (data instanceof ListTemplate && Constants.itemType(dt) == Constants.DT_STRING);
        }

        @Override
        public View createView(int position, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View rootView = inflater.inflate(R.layout.string_item, parent, false);
            mTextView = (TextView) rootView.findViewById(R.id.text);
            return rootView;
        }

        @Override
        public void bindView(int position, NoteTemplate data) {
            int dt = data.getType();
            if (dt == Constants.DT_STRING) {
                mTextView.setText(data.getData());
            } else if (data instanceof ListTemplate && Constants.itemType(dt) == Constants.DT_STRING) {
                ListTemplate<StringTemplate> list = (ListTemplate<StringTemplate>) data;
                int size = list.size();
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    StringTemplate itemData = list.getItem(i);
                    builder.append(itemData.getData());
                    builder.append("\n\n");
                }
                mTextView.setText(builder.toString());
            }
        }
    }

    private class ImagesItem implements ItemView {
        private ImageGridView mImageGridView;

        @Override
        public boolean accept(NoteTemplate data) {
            int dt = data.getType();
            if (dt == Constants.DT_URI && data instanceof FileTemplate) {
                FileTemplate fileData = (FileTemplate) data;
                return Constants.CONTENT_TYPE_JPEG.equals(fileData.contentType);
            } else if (dt == Constants.DT_LIST && data instanceof ListTemplate) {
                return accept(((ListTemplate) data).getItem(0));
            }

            return false;
        }

        @Override
        public View createView(int position, ViewGroup parent) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_item, parent, false);
            mImageGridView = (ImageGridView) rootView.findViewById(R.id.image_grid);
            return rootView;
        }

        @Override
        public void bindView(int position, NoteTemplate data) {
            int dt = data.getType();
            if (dt == Constants.DT_URI && data instanceof FileTemplate) {
                FileTemplate fileData = (FileTemplate) data;
                List<Uri> uris = new ArrayList<>();
                uris.add(fileData.uri);
                mImageGridView.setImages(uris);
            } else if (dt == Constants.DT_LIST && data instanceof ListTemplate) {
                List<FileTemplate> fileList = (List<FileTemplate>) data;
                List<Uri> uris = new ArrayList<>();
                for (FileTemplate itemData : fileList) {
                    uris.add(itemData.uri);
                }
                mImageGridView.setImages(uris);
            }
        }
    }

    private class UnSupportView implements ItemView {
        private View mRootView;

        @Override
        public boolean accept(NoteTemplate data) {
            return true;
        }

        @Override
        public View createView(int position, ViewGroup parent) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.unsupport_item,
                    parent, false);
            mRootView = rootView;
            return rootView;
        }

        @Override
        public void bindView(int position, final NoteTemplate data) {
            mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(NoteDetailsActivity.this, data.getData(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private class NoteDetailsAdapter extends BaseAdapter {
        private List<NoteTemplate> mData;

        private Class[] itemViewClass = new Class[] {
                StringItem.class,
                ImagesItem.class,

                // keep this the last
                UnSupportView.class,
        };

        public NoteDetailsAdapter(List<NoteTemplate> data) {
            mData = data;
        }

        @Override
        public int getCount() {
            return mData != null ? mData.size() : 0;
        }

        @Override
        public NoteTemplate getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NoteTemplate data = getItem(position);
            ItemView itemView = null;
            try {
                for (Class cls : itemViewClass) {
                    ItemView temp = ((ItemView) cls.newInstance());
                    if (temp.accept(data)) {
                        itemView = temp;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (convertView == null) {
                convertView = itemView.createView(position, parent);
            }
            itemView.bindView(position, data);
            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            NoteTemplate data = getItem(position);
            try {
                for (int i = 0; i < itemViewClass.length; i++) {
                    ItemView temp = ((ItemView) itemViewClass[i].newInstance());
                    if (temp.accept(data)) {
                        return i;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return itemViewClass.length - 1;
        }

        @Override
        public int getViewTypeCount() {
            return itemViewClass.length;
        }
    }
}
