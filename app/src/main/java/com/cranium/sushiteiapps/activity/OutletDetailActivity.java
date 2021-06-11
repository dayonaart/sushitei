package com.cranium.sushiteiapps.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.model.Outlet;
import com.cranium.sushiteiapps.model.OutletImage;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OutletDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title_bar) TextView titleBar;
    @BindView(R.id.outlet_slider) SliderLayout imagesSlider;
    @BindView(R.id.outlet_slider_indicator) PagerIndicator imageSliderIndicator;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.address) TextView address;
    @BindView(R.id.map_direction) ImageView mapDirection;
    @BindView(R.id.phone_number) TextView phoneNumber;
    @BindView(R.id.call_button) AppCompatButton callButton;

    @OnClick(R.id.image_back)
    public void doActionLeft(View view) {
        onBackPressed();
    }

    private Outlet outlet;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlet_detail);
        ButterKnife.bind(this);

        retrofit = new Retrofit.Builder()
                .baseUrl(App.API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        outlet = (Outlet) getIntent().getSerializableExtra("outlet");

        titleBar.setText(outlet.getName());
        name.setText(outlet.getName());
        String outledt = Html.fromHtml(outlet.getAddress()).toString();
        address.setText(outledt);
        phoneNumber.setText(outlet.getPhone());
        Picasso.with(this).load(getMapImage(outlet.getLatitude(), outlet.getLongitude())).error(R.drawable.image_315x315).into(mapDirection);
        for (OutletImage outletImage : outlet.getOutletImages()) {

            DefaultSliderView textSliderView = new DefaultSliderView(this);

            String image = App.URL + "/files/outlet-images/" + outletImage.getImage();
            textSliderView
                    .image(image)
                    .error(R.drawable.image_720x540)
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .setOnSliderClickListener(this);

            imagesSlider.addSlider(textSliderView);
        }

        imagesSlider.stopAutoCycle();

        imagesSlider.setCustomIndicator(imageSliderIndicator);
        if(outlet.getOutletImages().size() >= 1) {
            imagesSlider.setCurrentPosition(0);
            imageSliderIndicator.setVisibility(View.GONE);
            imagesSlider.stopAutoCycle();
            imagesSlider.setPagerTransformer(false, new BaseTransformer() {
                @Override
                protected void onTransform(View view, float v) {
                }
            });
        }

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + outlet.getPhone()));
                startActivity(callIntent);
            }
        });

        mapDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OutletMapActivity.class);
                intent.putExtra("outlet", outlet);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    /**
     * this method is for get Map Image by latitude and logitude data
     * @param latitude is the string latitude input data
     * @param longitude is the string longitude input data
     * @return string maps image
    * */
    private String getMapImage(String latitude, String longitude) {
        return "https://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=18&size=200x200&key=" + App.getGoogleApiKey();
    }
}
