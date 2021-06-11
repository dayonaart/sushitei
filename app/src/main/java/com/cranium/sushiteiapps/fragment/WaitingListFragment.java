package com.cranium.sushiteiapps.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.Config;
import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.activity.LoginActivity;
import com.cranium.sushiteiapps.activity.MainActivity;
import com.cranium.sushiteiapps.activity.MessageDetailActivity;
import com.cranium.sushiteiapps.activity.TermAndConditionActivity;
import com.cranium.sushiteiapps.adapter.OutletCitySpinnerAdapter;
import com.cranium.sushiteiapps.adapter.OutletSpinnerAdapter;
import com.cranium.sushiteiapps.adapter.SeatSpinnerAdapter;
import com.cranium.sushiteiapps.api.WaitingListApi;
import com.cranium.sushiteiapps.model.Outlet;
import com.cranium.sushiteiapps.model.OutletCity;
import com.cranium.sushiteiapps.model.Reservation;
import com.cranium.sushiteiapps.model.Seat;
import com.cranium.sushiteiapps.model.User;
import com.cranium.sushiteiapps.model.request.ReservationRequest;
import com.cranium.sushiteiapps.model.response.EditProfileResponseError;
import com.cranium.sushiteiapps.model.response.ReservationData;
import com.cranium.sushiteiapps.util.DatabaseHelper;
import com.cranium.sushiteiapps.util.InputFilterMinMax;
import com.cranium.sushiteiapps.util.Notification;
import com.cranium.sushiteiapps.util.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cranium.sushiteiapps.App.TIMEOUT;

/**
 * Created by Dayona on 5/31/17.
 */

public class WaitingListFragment extends Fragment {

    @BindView(R.id.parent) LinearLayout parentView;
    @BindView(R.id.parent_top) LinearLayout parentTop;
    // waiting list form
    @BindView(R.id.waiting_form) LinearLayout waitingForm;
    @BindView(R.id.empty_state) LinearLayout emptyState;
    @BindView(R.id.first_name) AutoCompleteTextView mFirstNameView;
    @BindView(R.id.last_name) AutoCompleteTextView mLastNameView;
    @BindView(R.id.phone) AutoCompleteTextView mPhoneView;
    @BindView(R.id.visitor_count) AutoCompleteTextView mVisitorCountView;
    @BindView(R.id.city) Spinner citySpinner;
    @BindView(R.id.outlet) Spinner outletSpinner;
    @BindView(R.id.seat) Spinner seatSpinner;
    @BindView(R.id.submit_button) AppCompatButton submitButton;
    @BindView(R.id.login_button) AppCompatButton loginButton;
    @BindView(R.id.register_button) AppCompatButton registerButton;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @BindView(R.id.comingsoon) TextView comingSoon;
    // waiting list result
    @BindView(R.id.waiting_list_result) LinearLayout waitingListResult;
    @BindView(R.id.date_updated) TextView dateUpdatedView;
    @BindView(R.id.refresh) ImageButton refreshButton;
    @BindView(R.id.refreshd) ImageButton refreshDButton;
    @BindView(R.id.outlet_name) TextView outletView;
    @BindView(R.id.last_queuing) TextView lastQueuingView;
    @BindView(R.id.waiting_list_at) TextView waitingListAtView;
    @BindView(R.id.your_number) TextView yourNumberView;
    @BindView(R.id.description2) TextView desc2;
    @BindView(R.id.description) TextView desc;
    @BindView(R.id.your_number_description) TextView yourNumberDescriptionView;


    private SessionManager sessionManager;
    private DatabaseHelper databaseHelper;
    private List<OutletCity> outletCities;
    private List<Outlet> outlets;
    private Integer selectedCity;
    private Integer selectedOutlet;
    private Integer selectedSeat;
    private User user;
    private Timer timer = new Timer();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        databaseHelper = new DatabaseHelper(getActivity());
        sessionManager = new SessionManager(getActivity());
        final View view = inflater.inflate(R.layout.fragment_waiting_list, container, false);
        ButterKnife.bind(this, view);

        mVisitorCountView.setFilters(new InputFilter[] {
                new InputFilterMinMax("1", "99")
        });

        if (sessionManager.isLoggedIn()) {

            user = databaseHelper.getUserByMemberCode(sessionManager.getMemberCode());
            Log.e("SURYA"," User type "+user.getStatusDescription());
            if(user.getStatusDescription().equals("Permanent")) {
                parentView.setVisibility(View.VISIBLE);
                emptyState.setVisibility(View.GONE);
                mFirstNameView.setText(user.getFirstName());
                mLastNameView.setText(user.getLastName());
                mPhoneView.setText(user.getPhone());
                mPhoneView.setVisibility(View.GONE);
                loginButton.setVisibility(View.GONE);
                waitingForm.setVisibility(View.VISIBLE);
                waitingListResult.setVisibility(View.GONE);

            }else{
                parentView.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.GONE);
                desc2.setVisibility(View.GONE);
            }
            enableComingSoon();

        } else {
            parentView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        }
        enableComingSoon();

        outletCities = databaseHelper.getAllOutletCities();
        OutletCitySpinnerAdapter outletCitySpinnerAdapter = new OutletCitySpinnerAdapter(getActivity(), outletCities);
        citySpinner.setAdapter(outletCitySpinnerAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("cranium", "item selected" + position + " " + outletCities.get(position).getName());
                selectedCity = outletCities.get(position).getId();
                Log.i("cranium", "pilih kota"+selectedCity);
                loadOutletSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("cranium", "item nothing selected");
            }
        });

        loadSeatSpinner();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSubmit();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialog();
                loadData();
            }
        });

        refreshDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialog();
                loadData();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TermAndConditionActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * this method will handle requeest for waitinglist
     * and whow notification to user if already bumped or canceled
     */
    private void loadData() {
        if (sessionManager.isLoggedIn()) {

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(App.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WaitingListApi service = retrofit.create(WaitingListApi.class);
            Call<ReservationData> call = service.getData();

            call.enqueue(new Callback<ReservationData>() {
                @Override
                public void onResponse(Call<ReservationData> call, Response<ReservationData> response) {

                    if(response.code() ==411){
                        new Notification(getContext()).showNotificationMessage(getResources().getString(R.string.info_wl),getResources().getString(R.string.canceled),new Date().toString(), new Intent(getContext(),MainActivity.class),null);
                        showDialogBox(getResources().getString(R.string.canceled));
                        timer.cancel();
                        timer.purge();
                        managePageIsWaiting(false, null);
                    }else if (response.code() == 410){
                        new Notification(getContext()).showNotificationMessage(getResources().getString(R.string.info_wl),getResources().getString(R.string.bumped),new Date().toString(), new Intent(getContext(),MainActivity.class),null);
                        showDialogBox(getResources().getString(R.string.bumped));
                        timer.cancel();
                        timer.purge();
                        managePageIsWaiting(false, null);
                    }

                    if (response.raw().isSuccessful()) {
                        managePageIsWaiting(true, response.body());
                        Log.e("SURYA", "Alalal: de +  "+response.body().getReservation().getStatus());
                    }

                    Log.i("SURYA", "Alalal: diiiisstoo "+response.code());
                    if (response.code() == 400 || response.code() == 404 || response.body() !=null) {
                        if (response.body() == null){
                            timer.cancel();
                            timer.purge();
                            managePageIsWaiting(false, null);
                        }
                        Log.i("SURYA", "Alalal: diiiiss "+response.code());
                    }else{

                    }
                }

                @Override
                public void onFailure(Call<ReservationData> call, Throwable t) {

                    Log.i("SURYA", "Alalal: do" + t.getLocalizedMessage());
                }
            });
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (sessionManager != null) {
//            loadData();

            Log.i("SURYA", "SURYA cranium: sesssion");
        }


    }

    /**
     * this method will handle all condition of reservation data from API
     * @param bool true = show current reservation layout; false = show form reservation
     * @param reservationData Data of the current resevation
     */
    private void managePageIsWaiting(boolean bool, @Nullable ReservationData reservationData) {
        if (bool) {
            waitingListResult.setVisibility(View.VISIBLE);
            waitingForm.setVisibility(View.GONE);

            Reservation reservation = reservationData.getReservation();

            Log.i("SURYA", "SURYA cranium: " + reservation.getLastQueue().getId());

            if(reservation.getLastQueue() != null) {
                if(reservation.getLastQueue().getQueueNumber().equals(reservation.getQueueNumber()))
                    lastQueuingView.setText("..");
                else
                    lastQueuingView.setText(reservation.getLastQueue().getQueueNumber());
                    lastQueuingView.setText(reservation.getLastQueue().getQueueNumber());
            }else{
                lastQueuingView.setText("..");
            }
            outletView.setText("Sushi Tei\n" + reservation.getOutlet().getName().replaceAll("Sushi Tei ", ""));
            waitingListAtView.setText("You apply waiting list at Sushi Tei " + reservation.getOutlet().getName().replaceAll("Sushi Tei ", ""));
            yourNumberView.setText(reservation.getQueueNumber());
            yourNumberDescriptionView.setText("There are " + reservation.getLeftoverQueue() + " number queuing before you.");

            sessionManager.setKeyWaitingListUpdated();
            dateUpdatedView.setText(sessionManager.getKeyWaitingListUpdated());

        } else {
            waitingForm.setVisibility(View.VISIBLE);
            waitingListResult.setVisibility(View.GONE);

        }
    }

    /**
     * this method will load all outlet from API then set it to soutlet spinner
     * @param position index of spinner
     */
    private void loadOutletSpinner(int position) {

        outlets = outletCities.get(position).getOutlets();
        OutletSpinnerAdapter outletSpinnerAdapter = new OutletSpinnerAdapter(getActivity(), outlets);
        outletSpinner.setAdapter(outletSpinnerAdapter);
        outletSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOutlet = outlets.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("cranium", "item nothing selected");
            }
        });
    }

    /**
     * this method will load all seat from API then set it to seat spinner
     */
    private void loadSeatSpinner() {

        final List<Seat> seats = databaseHelper.getAllSeats();

        SeatSpinnerAdapter seatSpinnerAdapter = new SeatSpinnerAdapter(getActivity(), seats);
        seatSpinner.setAdapter(seatSpinnerAdapter);
        seatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSeat = seats.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("cranium", "item nothing selected");
            }
        });
    }

    /**
     * this method will attempt the reservation then send the reservation request and handle the result
     */
    private void attemptSubmit() {


        App.hideSoftKeyboard(getActivity());

        // reset errors
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mPhoneView.setError(null);
        mVisitorCountView.setError(null);

        final String firstName = mFirstNameView.getText().toString();
        final String lastName = mLastNameView.getText().toString();
        final String phone = mPhoneView.getText().toString();
        final String visitorCount = mVisitorCountView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(visitorCount)) {
            mVisitorCountView.setError(getString(R.string.error_field_required));
            focusView = mVisitorCountView;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        }

        if (TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }

//        if (TextUtils.isEmpty(lastName)) {
//            mLastNameView.setError(getString(R.string.error_field_required));
//            focusView = mLastNameView;
//            cancel = true;
//        }`

        if (cancel) {
            focusView.requestFocus();
        } else {

            progressBar.setVisibility(View.VISIBLE);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + sessionManager.getToken())
                            .addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(newRequest);
                }
            }).readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(App.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Log.e("crannnnp", "craniiummmmkk: luar");

            Log.e("suryassk", " hahahah "+getFirebaseToken());
            WaitingListApi service = retrofit.create(WaitingListApi.class);
            ReservationRequest reservationRequest = new ReservationRequest(firstName, lastName, phone, selectedCity, selectedOutlet, Integer.parseInt(visitorCount), selectedSeat, getFirebaseToken());
            Call<ReservationData> call = service.createReservation(reservationRequest);
            call.enqueue(new Callback<ReservationData>() {
                @Override
                public void onResponse(Call<ReservationData> call, Response<ReservationData> response) {
//                    Log.e("suryassk", " hahahah "+getFirebaseToken());

                    if (response.raw().isSuccessful()) {
                        Reservation reservation = response.body().getReservation();
                        sessionManager.setWaitingList(reservation.getStatus(), new Date());
                        Log.e("crannnn", "craniiummmm: dalam");
                        setSubmitSuccessDialog(response.body());
                        resetFields();
                        launchDialog();
                        loadData();
                    }else if(response.code()==400){
                        try {
                            Log.e("crannnn", response.errorBody().toString());
                            Gson gson = new GsonBuilder().create();
                            EditProfileResponseError mError = gson.fromJson(response.errorBody().string(), EditProfileResponseError.class);
                            Toast.makeText(getContext(), mError.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("crannnn", mError.getMessage().toString()+"\n"+firstName+"\n"+lastName+"\n"+phone +"\n"+ selectedCity+"\n"+ selectedOutlet+"\n"+Integer.parseInt(visitorCount)+"\n"+ selectedSeat);
                        } catch (IOException e) {
                            // handle failure to read error
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ReservationData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"cant reach",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * this method will show the success dialog if reservation is success registered from outlet
     * @param reservationData reservation information
     */
    private void setSubmitSuccessDialog(final ReservationData reservationData) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.waiting_list_success_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView waitingListAt = (TextView) dialog.findViewById(R.id.waiting_list_at);
        TextView yourNumber = (TextView) dialog.findViewById(R.id.your_number);
        TextView yourNumberDescription = (TextView) dialog.findViewById(R.id.your_number_description);
        ImageButton close = (ImageButton) dialog.findViewById(R.id.close);

        waitingListAt.setText("You apply waiting list at Sushi Tei " + reservationData.getReservation().getOutlet().getName().replaceAll("Sushi Tei ", ""));
        yourNumber.setText(reservationData.getReservation().getQueueNumber());
        yourNumberDescription.setText("There are " + reservationData.getReservation().getLeftoverQueue() + " number queuing before you.");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managePageIsWaiting(true, reservationData);
                dialog.dismiss();

                launchDialog();
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        loadData();
                    }
                }, 0, 4000);
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * load refrest status dialog
     */
    public void launchDialog() {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Refresh Status", "Please wait ...", true);
        progressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Here you should write your time consuming task...
                    // Let the progress ring for 10 seconds...
                    Thread.sleep(2000);
                } catch (Exception e) {

                }
                progressDialog.dismiss();
            }
        }).start();
    }

    /**
     * reset reservation forms after reservation is finished
     */
    public void resetFields(){
        citySpinner.setId(0);
        outletSpinner.setId(0);
        seatSpinner.setId(0);
        mVisitorCountView.setText("");
    }

    /**
     * get firebase token from sharedpreferences
     * @return firebasetoken = generated firebase token
     */
    private String getFirebaseToken(){
        SharedPreferences pref = getContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String firebaseToken = pref.getString("regId", null);

        Log.e("cranium", "Firebase reg id: " + firebaseToken);

        return firebaseToken;
    }

    /**
     * this method will show the message result from greeter API
     * @param message is the message information if bumped or canceled
     */
    private void showDialogBox(String message) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView description = (TextView) dialog.findViewById(R.id.description);

        title.setText(message);
        description.setText("");
        description.setVisibility(View.GONE);
        ImageButton close = (ImageButton) dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void enableComingSoon(){
        parentView.setVisibility(View.GONE);
        emptyState.setVisibility(View.VISIBLE);
        comingSoon.setVisibility(View.VISIBLE);
        desc.setVisibility(View.GONE);
        desc2.setVisibility(View.GONE);
        loginButton.setVisibility(View.GONE);
    }

}
