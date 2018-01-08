package com.hewei.secretary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hewei.secretary.note.ListTemplate;
import com.hewei.secretary.note.NoteTemplate;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class NoteDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        ListView listView = (ListView) findViewById(R.id.list);

    }

    private class NoteDetailsAdapter extends BaseAdapter {
        final int VT_UNKNOWN = -1;
        final int VT_STRING = 0;
        final int VT_IMAGES = 1;
        final int VT_URI = 2;

        private List<NoteTemplate> mData;

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
            int vt = getItemViewType(position);
            NoteTemplate data = getItem(position);

            if (vt == VT_STRING) {
                if (convertView == null) {
                    convertView = new TextView(parent.getContext());
                }
                TextView textView = (TextView) convertView;
                textView.setText("");
            } else if (vt == VT_IMAGES) {
                try {
                    JSONArray array = new JSONArray(data.getData());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (vt == VT_URI) {

            }

            return null;
        }

        @Override
        public int getItemViewType(int position) {
            NoteTemplate data = getItem(position);
            int dtItem = data.getType();

            if (dtItem == Constants.DT_LIST) {
                ListTemplate listData = (ListTemplate) data;
                dtItem = listData.getItem(0).getType();
                return dt2vt(dtItem, true);
            }

            return dt2vt(dtItem, false);
        }

        private int dt2vt(int dt, boolean list) {
            if (list) {
                if (dt == Constants.DT_STRING) {
                    return VT_STRING;
                } else if (dt == Constants.DT_URI) {
                    return VT_IMAGES;
                }
            } else {
                if (dt == Constants.DT_STRING) {
                    return VT_STRING;
                } else if (dt == Constants.DT_URI) {
                    return VT_URI;
                }
            }

            return VT_UNKNOWN;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }
    }
}
