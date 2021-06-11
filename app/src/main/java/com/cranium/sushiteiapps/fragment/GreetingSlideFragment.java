package com.cranium.sushiteiapps.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GreetingSlideFragment extends Fragment {

    @BindView(R.id.description) TextView description;
    @BindView(R.id.image) ImageView image;

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;
    private String descriptionText, imageText;

    public static GreetingSlideFragment newInstance(int layoutResId, String desc, @Nullable String img) {
        GreetingSlideFragment sampleSlide = new GreetingSlideFragment(desc, img);

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        sampleSlide.setArguments(args);

        return sampleSlide;
    }

    public GreetingSlideFragment() {
    }

    public GreetingSlideFragment(String desc, @Nullable String img) {
        descriptionText = desc;
        imageText = img;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(layoutResId, container, false);
        ButterKnife.bind(this, view);

        description.setText(descriptionText);
        Picasso.with(getContext()).load(App.URL + "files/greetings/" + imageText).error(R.drawable.greeting).into(image);

        return view;
    }
}