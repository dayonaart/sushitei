package com.cranium.sushiteiapps.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.widget.ArrayAdapter;

import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.util.SessionManager;

import java.util.HashMap;
import java.util.List;

public class MessagetTypeAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public MessagetTypeAdapter(Context context, int textViewResourceId,
                               List<String> objects) {
        super(context, textViewResourceId, objects);

        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}