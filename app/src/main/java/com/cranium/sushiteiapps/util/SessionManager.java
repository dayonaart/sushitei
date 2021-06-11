package com.cranium.sushiteiapps.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.cranium.sushiteiapps.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by aldieemaulana on 5/24/17.
 */

public class SessionManager {

    private static final String KEY_ID_ADMIN = "admin";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "CraniumIndonesia";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_TOKEN = "getToken";
    private static final String KEY_MEMBER_CODE = "getMemberCode";
    private static final String KEY_IS_ALLOW_LOCATION = "getKeyIsAllowLocation";
    private static final String KEY_IS_FIRST_TIME_LAUNCH = "getKeyIsFirstTimeLaunch";
    private static final String KEY_IS_SECOND_TIME_LAUNCH = "getKeyIsSecondTimeLaunch";
    private static final String KEY_IS_FIRST_TIME = "getKeyIsFirstTimeLaunch1";
    private static final String KEY_WHATS_NEW_UPDATED = "getKeyWhatsNewUpdated";
    private static final String KEY_WAITING_LIST_UPDATED = "getKeyWaitingListUpdated";
    private static final String KEY_STATUS_WAITING_LIST = "getKeyStatusWaitingList";
    private static final String KEY_LAST_DATE_WAITING_LIST = "getKeyLastDateWaitingList";
    private static final String KEY_POINT_UPDATED = "getKeyPointUpdated";
    private static final String KEY_DATE_UPDATED = "getKeyDateUpdated";
    private static final String FIREBASE_TOKEN_KEY = "firebaseToken";
    private static final String KEY_IS_FIRST = "getKeyIsFirst";
    private static final String DEVICE_NUMBER ="getDeviceNumber";
    private static final String NEW_MESSAGE = "newMessage";
    private static final String NEW_VOUCHER = "newVoucher";
    private static final String COUNT_VOUCHER = "countVoucher";
    private static final String COUNT_MESSAGE = "countMessage";


    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn, User user) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.putString(KEY_TOKEN, user.getToken());
        editor.putString(KEY_MEMBER_CODE, user.getMemberCode());
        this.setKeyPointUpdated();
        editor.commit();
    }

    public void  isLogOut(){
        editor.clear();
        editor.putBoolean(KEY_IS_FIRST_TIME_LAUNCH, true);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getToken(){
        return pref.getString(KEY_TOKEN, null);
    }

    public String getMemberCode(){
        return pref.getString(KEY_MEMBER_CODE, null);
    }

    public void setAllowLocation(boolean allowLocation) {
        editor.putBoolean(KEY_IS_ALLOW_LOCATION, allowLocation);
        editor.commit();
    }

    public boolean isAllowLocation() {
        return pref.getBoolean(KEY_IS_ALLOW_LOCATION, false);
    }

    public boolean getIsGreetingFirstOpenDone() {
        return pref.getBoolean(KEY_IS_FIRST_TIME_LAUNCH, false);
    }

    public void setIsGreetingFirstOpenDone(boolean isFirstTime) {
        editor.putBoolean(KEY_IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean getIsGreetingSecondOpenDone() {
        return pref.getBoolean(KEY_IS_SECOND_TIME_LAUNCH, false);
    }

    public void setIsGreetingSecondOpenDone(boolean isFirstTime) {
        editor.putBoolean(KEY_IS_SECOND_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean getIsFirst() {
        return pref.getBoolean(KEY_IS_FIRST, false);
    }

    public void setIsFirst(boolean isFirstTime) {
        editor.putBoolean(KEY_IS_FIRST, isFirstTime);
        editor.commit();
    }

    public String getFirebaseToken() {
        return pref.getString(FIREBASE_TOKEN_KEY,"");
    }

    public void setFirebaseTokenKey(String firebaseTokenKey) {
        editor.putString(FIREBASE_TOKEN_KEY, firebaseTokenKey);
        editor.commit();
    }

    public boolean isNewMessageExist() {
        return pref.getBoolean(NEW_MESSAGE,false);
    }

    public void setNewMessage(boolean newMessage) {
        editor.putBoolean(NEW_MESSAGE, newMessage);
        editor.commit();
    }

    public boolean isNewVoucherExist() {
        return pref.getBoolean(NEW_VOUCHER,false);
    }

    public void setNewVoucher(boolean newVoucher) {
        editor.putBoolean(NEW_VOUCHER, newVoucher);
        editor.commit();
    }

    public String getDeviceNumber(){
        return pref.getString(DEVICE_NUMBER,"");
    }

    public void setDeviceNumber(String deviceNumber) {
        editor.putString(DEVICE_NUMBER, deviceNumber);
        editor.commit();
    }

    public void setIsGreeting(String isFirstTime) {
        editor.putString(KEY_IS_FIRST_TIME, isFirstTime);
        editor.commit();
    }

    public String getIsGreeting() {
        return pref.getString(KEY_IS_FIRST_TIME, "firstopen");
    }

    public String getKeyWhatsNewUpdated() {
        String string = pref.getString(KEY_WHATS_NEW_UPDATED, "");
        if (!string.equals("")) {
            String formattedDate;
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd H:m:s", new Locale("en", "EN")).parse(string);

                SimpleDateFormat outGoing = new SimpleDateFormat("EEEE, dd MMMM yyyy, H:mm a", new Locale("en", "EN"));
                formattedDate = outGoing.format(date);
            } catch (ParseException e) {
                formattedDate = string;
                e.printStackTrace();
            }

            return formattedDate;
        }

        return string;
    }

    public void setKeyPointUpdated() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s", new Locale("id", "ID"));
        Date date = new Date();
        editor.putString(KEY_POINT_UPDATED, dateFormat.format(date));
        editor.commit();
    }

    public void setLoginDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s", new Locale("id", "ID"));
        Date date = new Date();
        editor.putString(KEY_DATE_UPDATED, dateFormat.format(date));
        editor.commit();
    }

    public String getLoginUpdate() {
        String string = pref.getString(KEY_POINT_UPDATED, "");
        if (!string.equals("")) {
            String formattedDate;
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd H:m:s").parse(string);
                SimpleDateFormat outGoing = new SimpleDateFormat("yyyy-MM-dd H:m:s");
                formattedDate = outGoing.format(date);
                Log.e("waktuuu logiinn",formattedDate);
            } catch (ParseException e) {
                formattedDate = string;
                e.printStackTrace();
            }

            return formattedDate;
        }

        return string;
    }

    public String getKeyPointUpdated() {
        String string = pref.getString(KEY_POINT_UPDATED, "");
        if (!string.equals("")) {
            String formattedDate;
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd H:m:s").parse(string);

                SimpleDateFormat outGoing = new SimpleDateFormat("dd MMMM yyyy, H:mm");
                formattedDate = outGoing.format(date)+" WIB";
            } catch (ParseException e) {
                formattedDate = string;
                e.printStackTrace();
            }

            return formattedDate;
        }

        return string;
    }

    public void setKeyWhatsNewUpdated() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s", new Locale("en", "EN"));
        Date date = new Date();
        editor.putString(KEY_WHATS_NEW_UPDATED, dateFormat.format(date));
        editor.commit();
    }

    public Integer getKeyStatusWaitingList() {
        return pref.getInt(KEY_STATUS_WAITING_LIST, 0);
    }

    public void setKeyWaitingListUpdated() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s", new Locale("id", "ID"));
        Date date = new Date();
        editor.putString(KEY_WAITING_LIST_UPDATED, dateFormat.format(date));
        editor.commit();
    }

    public String getKeyWaitingListUpdated() {
        String string = pref.getString(KEY_WAITING_LIST_UPDATED, "");
        if (!string.equals("")) {
            String formattedDate;
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd H:m:s", new Locale("en", "EN")).parse(string);

                SimpleDateFormat outGoing = new SimpleDateFormat("EEEE, dd MMMM yyyy, h:m a", new Locale("en", "EN"));
                formattedDate = outGoing.format(date);
            } catch (ParseException e) {
                formattedDate = string;
                e.printStackTrace();
            }

            return formattedDate;
        }

        return string;
    }

    public boolean hasWaitingList() {
        if (getKeyStatusWaitingList() == 0) {
            return false;
        }

        if (!getLastDateWaitingList().equals(null)) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID")).parse(getLastDateWaitingList());
                if (new Date().after(date)) {
                    return false;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void setWaitingList(String status, Date date) {
        String currentDate = DateFormat.getDateTimeInstance().format(date);

        editor.putString(KEY_LAST_DATE_WAITING_LIST, currentDate);
        editor.putInt(KEY_STATUS_WAITING_LIST, Integer.parseInt(status));
        editor.commit();
    }

    public String getLastDateWaitingList() {
        return pref.getString(KEY_LAST_DATE_WAITING_LIST, null);
    }

    public Integer getCountVoucher() {
        return pref.getInt(COUNT_VOUCHER, 0);
    }

    public void setCountVoucher(int count) {
        editor.putInt(COUNT_VOUCHER, count);
        editor.commit();
    }

    public Integer getCountMessage() {
        return pref.getInt(COUNT_MESSAGE, 0);
    }

    public void setCountMessage(int count) {
        editor.putInt(COUNT_MESSAGE, count);
        editor.commit();
    }

}
