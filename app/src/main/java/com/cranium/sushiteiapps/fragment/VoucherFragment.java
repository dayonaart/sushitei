package com.cranium.sushiteiapps.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.adapter.CommentDeleteAdapter;
import com.cranium.sushiteiapps.adapter.OutletCitySpinnerAdapter;
import com.cranium.sushiteiapps.adapter.OutletSpinnerAdapter;
import com.cranium.sushiteiapps.api.CommentApi;
import com.cranium.sushiteiapps.model.Outlet;
import com.cranium.sushiteiapps.model.OutletCity;
import com.cranium.sushiteiapps.model.User;
import com.cranium.sushiteiapps.model.request.CommentRequest;
import com.cranium.sushiteiapps.model.response.CommentResponse;
import com.cranium.sushiteiapps.service.GMailSender;
import com.cranium.sushiteiapps.util.ClickListener;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.GalleryImage;
import com.cranium.sushiteiapps.util.RecyclerTouchListener;
import com.cranium.sushiteiapps.util.SessionManager;
import com.cranium.sushiteiapps.util.SwipeController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vlk.multimager.adapters.GalleryImagesAdapter;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.sangcomz.fishbun.FishBun;
//import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
//import com.sangcomz.fishbun.adapter.image.impl.PicassoAdapter;
//import com.sangcomz.fishbun.define.Define;

/**
 * Created by Dayona on 5/31/17.
 */

public class VoucherFragment extends Fragment {

    @BindView(R.id.first_name) AutoCompleteTextView mFirstNameView;
    @BindView(R.id.last_name) AutoCompleteTextView mLastNameView;
    @BindView(R.id.phone) AutoCompleteTextView mPhoneView;
    @BindView(R.id.email_comment) AutoCompleteTextView mEmailView;
    @BindView(R.id.date_visit) AutoCompleteTextView mDateVisitView;
    @BindView(R.id.comment) AutoCompleteTextView mCommentView;
    @BindView(R.id.upload_photo_text) TextView tvUpload;
    @BindView(R.id.city) Spinner citySpinner;
    @BindView(R.id.outlet) Spinner outletSpinner;
    @BindView(R.id.comment_form) LinearLayout parentView;
    @BindView(R.id.input_layout_photo) LinearLayout inputPhotoLayout;
    @BindView(R.id.result_upload_photo_recycler_view) RecyclerView uploadPhotoRecyclerView;
    @BindView(R.id.submit_button) AppCompatButton submitButton;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.recviewdelete) RecyclerView delImageRecView;

    private Retrofit retrofit;
    private List<OutletCity> outletCities;
    private List<Outlet> outlets;
    private Integer selectedCity;
    private Integer selectedOutlet;
    private DatabaseHelper db;
    private DatePickerDialog datePickerDialog;
    private SessionManager sessionManager;
    private User user;
    String Tag = getClass().getName();

    private ProgressDialog progressDialog;

    private String namaLengkap,nomorMember,nomorTelp,tanggal,kota,outlet, komentar;

    ArrayList<Image> imagesList;
    List<String> imagees = new ArrayList<String>();
    List<String> imageUrl = new ArrayList<String>();
    int plusImg = 0;
    static final int OPEN_MEDIA_PICKER = 1;

    SwipeController swipeController = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_comment, container, false);
        ButterKnife.bind(this, view);

        selectedCity = selectedOutlet = null;
        db = new DatabaseHelper(getActivity());
        sessionManager = new SessionManager(getActivity());

        if (sessionManager.isLoggedIn()) {
            user = db.getUserByMemberCode(sessionManager.getMemberCode());
            mFirstNameView.setText(user.getFirstName());
            if(user.getLastName()!=null)
                mLastNameView.setText(user.getLastName());
            else
                mLastNameView.setText("-");
            mLastNameView.setText(user.getLastName());
            mPhoneView.setText(user.getPhone());
            mEmailView.setText(user.getEmail());
        }

        parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.hideSoftKeyboard(getActivity());
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSubmit();
            }
        });

        outletCities = db.getAllOutletCities();
        OutletCitySpinnerAdapter outletCitySpinnerAdapter = new OutletCitySpinnerAdapter(getActivity(), outletCities);
        citySpinner.setAdapter(outletCitySpinnerAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("cranium", "item selected" + position + " " + outletCities.get(position).getName());
                selectedCity = outletCities.get(position).getId();
                loadOutletSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("cranium", "item nothing selected");
            }
        });

        mDateVisitView.setFocusable(false);
        mDateVisitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mDateVisitView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("cranium", "test");
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            final DatePicker datePicker = datePickerDialog
                                    .getDatePicker();
                            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String datetime = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                                    Date date = null;
                                    try {
                                        date = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN")).parse(datetime);
                                        SimpleDateFormat formatted = new SimpleDateFormat("dd MMM yyyy", new Locale("en", "EN"));
                                        String mm = (minute < 10) ? "0"+minute : String.valueOf(minute);
                                        mDateVisitView.setText(formatted.format(date) + " " + hourOfDay + ":" + mm);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, hour, minute, DateFormat.is24HourFormat(getActivity()));

                            timePickerDialog.show();
                        }

                    }
                });
            }
        });

        return view;
    }

    private void attemptSubmit() {

        App.hideSoftKeyboard(getActivity());

        // reset errors
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mPhoneView.setError(null);
        mEmailView.setError(null);
        mDateVisitView.setError(null);
        mCommentView.setError(null);

        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String phoneNumber = mPhoneView.getText().toString();
        String dateVisit = mDateVisitView.getText().toString();
        String comment = mCommentView.getText().toString();
        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(comment)) {
            mCommentView.setError(getString(R.string.error_field_required));
            focusView = mCommentView;
            cancel = true;
        }

        if (TextUtils.isEmpty(dateVisit)) {
            mDateVisitView.setError(getString(R.string.error_field_required));
            focusView = mDateVisitView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email) || !isValidEmail(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        }

        if (TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(lastName)) {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        }

        if (cancel) {

            focusView.requestFocus();

        } else {

            progressDialog = ProgressDialog.show(getContext(), "Sending Email", "Please wait ...", true);
            String tok = "";
            if(sessionManager.isLoggedIn()) {
                tok = "Bearer " + sessionManager.getToken();
            }

            final String finalTok = tok;
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", finalTok)
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(App.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            CommentApi service = retrofit.create(CommentApi.class);

            List<String> images = imagees;

            Log.e("List Images : ",""+images.size());

            final CommentRequest request = new CommentRequest(firstName, lastName, phoneNumber,email, dateVisit, selectedOutlet, comment, images);

            Log.e(Tag,"first name : "+firstName+"\nlastname: "+lastName+"\nphoneNumber: "+phoneNumber+"\ndatevisit: "+dateVisit+"\nselected outlet: "+selectedOutlet+"\ncomment: "+comment+"\nemail : "+email);

            for(String e : images) Log.e(Tag,"url img :"+e);


            Call<CommentResponse> call = service.sendComment(request);

            call.enqueue(new Callback<CommentResponse>() {
                @Override
                public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {

                    if (response.raw().isSuccessful()) {

                        CommentResponse comment = response.body();
                        Toast.makeText(getActivity(), comment.getMessage(), Toast.LENGTH_LONG).show();
                        ArrayList<Image> imagesList = new ArrayList<Image>();

                        GalleryImagesAdapter imageAdapter = new GalleryImagesAdapter(getActivity(), imagesList, 3, new Params());
                        uploadPhotoRecyclerView.setAdapter(imageAdapter);

                        CommentDeleteAdapter deleteAdapter = new CommentDeleteAdapter(getActivity(),imagesList);
                        delImageRecView.setAdapter(deleteAdapter);

                        setDialogBox();

                    } else if (response.code() == 400) {
                        Gson gson = new GsonBuilder().create();
                        CommentResponse mError =new CommentResponse();
                        try {
                            mError = gson.fromJson(response.errorBody().string(),CommentResponse.class);
                            Toast.makeText(getActivity(), mError.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Log.e(Tag,"Error IO "+e);
                        }
                    }
                    else {
                        Toast.makeText(getActivity(), "send comment us is failed", Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
//                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<CommentResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "unable to send comment, please try again", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void setDialogBox() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView description = (TextView) dialog.findViewById(R.id.description);

        title.setText("Thank you for your feedback");
        description.setText("");
        description.setVisibility(View.GONE);

        ImageButton close = (ImageButton) dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstNameView.setText("");
                mLastNameView.setText("");
                mPhoneView.setText("");
                mEmailView.setText("");
                mDateVisitView.setText(null);
                mCommentView.setText(null);
                citySpinner.setSelection(0);
                outletSpinner.setSelection(0);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void loadOutletSpinner(int position) {
        outlets = outletCities.get(position).getOutlets();
        OutletSpinnerAdapter outletSpinnerAdapter = new OutletSpinnerAdapter(getActivity(), outlets);
        outletSpinner.setAdapter(outletSpinnerAdapter);
        outletSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOutlet = outlets.get(position).getId();
                Log.e(Tag,"selected outlet :"+outlets.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("cranium", "item nothing selected");
            }
        });
    }

    @OnClick(R.id.input_layout_photo)
    public void takePhoto(View view) {
        App.hideSoftKeyboard(getActivity());
        Intent intent = new Intent(getActivity(), GalleryImage.class);
        Params params = new Params();
        params.setCaptureLimit(3);
        params.setPickerLimit(3);
        params.setToolbarColor(R.color.colorAccent);
        params.setActionButtonColor(R.color.colorAccent);
        params.setButtonTextColor(R.color.colorAccent);
//        GalleryImage g = new GalleryImage();
//        g.showLimitAlert();
        intent.putExtra(Constants.KEY_PARAMS, params);

        startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);

//        Intent intent = new Intent(getContext(), AlbumSelectActivity.class);
////set limit on number of images that can be selected, default is 10
//        intent.putExtra("masuk", 3);
//        startActivityForResult(intent, 4);
//
//        FishBun.with(CommentFragment.this)
//                .setImageAdapter(new PicassoAdapter())
//                .startAlbum();
//        FishBun.with(CommentFragment.this)
//                .setImageAdapter(new PicassoAdapter())
//                .setIsUseDetailView(false)
//                .setPickerCount(5) //Deprecated
//                .setMaxCount(5)
//                .setMinCount(1)
//                .setPickerSpanCount(6)
//                .setActionBarColor(Color.parseColor("#795548"), Color.parseColor("#5D4037"), false)
//                .setActionBarTitleColor(Color.parseColor("#ffffff"))
//                .setAlbumSpanCount(2, 4)
//                .setButtonInAlbumActivity(false)
//                .setCamera(true)
//                .setReachLimitAutomaticClose(true)
////                .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_back_white))
////                .setOkButtonDrawable(ContextCompat.getDrawable(this, R.drawable.ic_custom_ok))
//                .setAllViewTitle("All")
//                .setActionBarTitle("Image Library")
//                .textOnImagesSelectionLimitReached("Limit Reached!")
//                .textOnNothingSelected("Nothing Selected")
//                .startAlbum();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.TYPE_MULTI_CAPTURE:
                handleResponsePhoto(intent,Constants.TYPE_MULTI_CAPTURE);
                break;

            case Constants.TYPE_MULTI_PICKER:
                handleResponsePhoto(intent,Constants.TYPE_MULTI_PICKER);
                break;

        }

//        super.onActivityResult(requestCode, resultCode, intent);
//        switch (requestCode) {
//            case Define.ALBUM_REQUEST_CODE:
//                if (resultCode == RESULT_OK) {
//                    // path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
//                    // you can get an image path(ArrayList<String>) on <0.6.2
//
//                    handleResponsePhoto(intent,Constants.TYPE_MULTI_PICKER);
////                    imagesList = intent.getParcelableArrayListExtra(Define.INTENT_PATH);
//                    // you can get an image path(ArrayList<Uri>) on 0.6.2 and later
//                    break;
//                }
//        }
    }

    private void handleResponsePhoto(Intent intent, int type) {
        if (type==Constants.TYPE_MULTI_PICKER){
            imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
//            imagesList = intent.getParcelableArrayListExtra(Define.INTENT_PATH);
        }else if (type==Constants.TYPE_MULTI_CAPTURE){
            imagesList = intent.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
        }

        uploadPhotoRecyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, GridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        uploadPhotoRecyclerView.setLayoutManager(mLayoutManager);
        final GalleryImagesAdapter imageAdapter = new GalleryImagesAdapter(getActivity(), imagesList, 3, new Params());
        Log.e("Comment imageadap"," size "+imagesList.size()+" adapt size "+imageAdapter.getItemCount());

        /*set adapter for button delete image*/
        CommentDeleteAdapter deleteAdapter = new CommentDeleteAdapter(getActivity(),imagesList);
        delImageRecView.setAdapter(deleteAdapter);
        delImageRecView.setLayoutManager(new GridLayoutManager(getContext(),3));
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        delImageRecView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), delImageRecView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                try{

                    final int pos = position;
//                    if  (pos>0){
//                        builder.setTitle("Confirm");
//                        builder.setMessage("Are you sure delete the image from list ?");
//
//                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//
//                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("Comment after","Pos "+(pos+plusImg)+" size "+imagesList.size());
                                uploadPhotoRecyclerView.getAdapter().notifyDataSetChanged();
                                uploadPhotoRecyclerView.getAdapter().notifyItemRemoved(pos+plusImg);
                                uploadPhotoRecyclerView.getAdapter().notifyItemRangeChanged(pos+plusImg,imagesList.size());

                                delImageRecView.getAdapter().notifyDataSetChanged();
                                delImageRecView.getAdapter().notifyItemRemoved(pos+plusImg);
                                delImageRecView.getAdapter().notifyItemRangeChanged(pos+plusImg,imagesList.size());
                                imagesList.remove(pos+plusImg);
//                                dialog.dismiss();


//                            }
//                        });
//
//                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // Do nothing
//                                dialog.dismiss();
//                            }
//                        });
//
//                        AlertDialog alert = builder.create();
//                        alert.show();

//                    }

                }catch(ArrayIndexOutOfBoundsException e){

                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        uploadPhotoRecyclerView.setAdapter(imageAdapter);
        progressDialog = ProgressDialog.show(getContext(), "Processing Image", "Please wait ...", true);
        progressDialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                atttmm(imagesList);
                progressDialog.dismiss();
            }
        }, 1000);

    }

    public void atttmm(ArrayList<Image> imagesList) {
        if(imagesList.size() > 0){
            Bitmap bm = getBitmap(imagesList.get(0).imagePath);

            imageUrl.add(imagesList.get(0).imagePath);
            File f = new File(imagesList.get(0).imagePath);

            long fileSize = getFileSize(f);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, getQuality(fileSize), baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            String pathImage = encodedImage;

            int sz = BitmapCompat.getAllocationByteCount(bm);

            String mensagem = "data:image/jpeg;base64," + pathImage.replaceAll("\r*\n*", "");

            Log.e("size img","pos 1 size "+fileSize+" quality "+getQuality(fileSize)+" comprs "+sz);

            imagees.add(mensagem);

            if(imagesList.size() > 1){
                bm = getBitmap(imagesList.get(1).imagePath);
                imageUrl.add(imagesList.get(1).imagePath);

                f = new File(imagesList.get(1).imagePath);

                fileSize = getFileSize(f);

                baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, getQuality(fileSize), baos); //bm is the bitmap object
                b = baos.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                sz = BitmapCompat.getAllocationByteCount(bm);

                pathImage = encodedImage;

                mensagem = "data:image/jpeg;base64," + pathImage.replaceAll("\r*\n*", "");

                Log.e("size img","pos 2 size "+fileSize+" quality "+getQuality(fileSize)+" comprs "+sz);

                imagees.add(mensagem);

                if(imagesList.size() > 2){
                    bm = getBitmap(imagesList.get(2).imagePath);
                    baos = new ByteArrayOutputStream();

                    f = new File(imagesList.get(2).imagePath);

                    fileSize = getFileSize(f);

                    bm.compress(Bitmap.CompressFormat.JPEG, getQuality(fileSize), baos); //bm is the bitmap object
                    b = baos.toByteArray();
                    encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    sz = BitmapCompat.getAllocationByteCount(bm);

                    pathImage = encodedImage;

                    mensagem = "data:image/jpeg;base64," + pathImage.replaceAll("\r*\n*", "");
                    Log.e("size img","pos 3 size "+fileSize+" quality "+getQuality(fileSize)+" comprs "+sz);

                    imagees.add(mensagem);
                    imageUrl.add(imagesList.get(2).imagePath);
                }

            }

        }

    }

    public Bitmap getBitmap(String path) {
        try {
            Bitmap bitmap=null;
            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendEmail(){
        user = db.getUserByMemberCode(sessionManager.getMemberCode());

        loadCommentField(sessionManager.getMemberCode());

        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("hallosushitei@gmail.com", "hallosushitei2017");
                    for(int i=0 ; i<imageUrl.size();i++){
                        File f = new File(imageUrl.get(i));
                        String url = f.getPath();
                        Log.e("LOLOS", f.getPath() );
                        sender.addAttachment(url);
                    }

                    String body = "Hi Customer Service,\n\n" +
                            namaLengkap+" telah mengirimkan komentar tentang Sushi Tei. Berikut adalah komentar yang dikirimkan:\n\n"+
                            "\t\t* Nama Lengkap \t :"+namaLengkap+"\n"+
                            "\t\t* Nomor Member \t :"+nomorMember+"\n"+
                            "\t\t* Nomor Telepon \t :"+nomorTelp+"\n"+
                            "\t\t* Tanggal Berkunjung \t :"+tanggal+"\n"+
                            "\t\t* Kota \t :"+kota+"\n"+
                            "\t\t* Outlet \t :"+outlet+"\n"+
                            "\t\t* Komentar \t :"+komentar+"\n\n\n"+
                            "Terima Kasih, \n"+
                            "Sushi Tei.";

                    Log.e("LOLOS Body", body);

                    sender.sendMail("Sushi Tei | New Comment from "+namaLengkap+
                                    " ("+user.getEmail()+")", body,
//                            "suryamudti0128@gmail.com",
                            "hallosushitei@gmail.com",
                            "cs_jkt@sushitei.co.id");
//                            "suryamudti0128@gmail.com");
                } catch (Exception e) {
                    Log.e("mylog message email", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();

    }

    public void loadCommentField(String memberCode){

        user = db.getUserByMemberCode(memberCode);

        namaLengkap = mFirstNameView.getText().toString()+" "+mLastNameView.getText().toString();

        nomorMember = user.getMemberCode();

        nomorTelp = mPhoneView.getText().toString();

        tanggal = mDateVisitView.getText().toString();

        kota = citySpinner.getSelectedItem().toString();

        outlet = outletSpinner.getSelectedItem().toString();

        komentar = mCommentView.getText().toString();

    }

    private long getFileSize(File file) {
        long length = file.length();
        return length;
    }

    private int getQuality(long filesize){
        int quality = 75;
        if (filesize>10240000) quality = 10; // Quality > 10MB
        else if (filesize>5120000 && filesize<10240000) quality =20; // Quality > 5MB && < 10MB
        else if (filesize>1024000 && filesize<5120000) quality =40; // Quality > 1MB && < 5MB
        else if (filesize>102400 && filesize<1024000) quality =50; // Quality > 100KB && < 1MB

        return quality;
    }

    private boolean isValidEmail(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".");
    }

}