package com.cranium.sushiteiapps.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cranium.sushiteiapps.App;
import com.cranium.sushiteiapps.model.Article;
import com.cranium.sushiteiapps.model.City;
import com.cranium.sushiteiapps.model.Firebase;
import com.cranium.sushiteiapps.model.Greeting;
import com.cranium.sushiteiapps.model.LatestMenu;
import com.cranium.sushiteiapps.model.Menu;
import com.cranium.sushiteiapps.model.MenuCategory;
import com.cranium.sushiteiapps.model.MenuWishlist;
import com.cranium.sushiteiapps.model.Message;
import com.cranium.sushiteiapps.model.OutletCity;
import com.cranium.sushiteiapps.model.PointHistory;
import com.cranium.sushiteiapps.model.PromoBanner;
import com.cranium.sushiteiapps.model.Seat;
import com.cranium.sushiteiapps.model.TermCondition;
import com.cranium.sushiteiapps.model.Wishlist;
import com.cranium.sushiteiapps.model.response.Articles;
import com.cranium.sushiteiapps.model.response.Cities;
import com.cranium.sushiteiapps.model.User;
import com.cranium.sushiteiapps.model.response.Greetings;
import com.cranium.sushiteiapps.model.response.HotMenus;
import com.cranium.sushiteiapps.model.response.HotMenusByCategory;
import com.cranium.sushiteiapps.model.response.MenuCategories;
import com.cranium.sushiteiapps.model.response.Messages;
import com.cranium.sushiteiapps.model.response.OutletCities;
import com.cranium.sushiteiapps.model.response.PointHistories;
import com.cranium.sushiteiapps.model.response.PromoBanners;
import com.cranium.sushiteiapps.model.response.Seats;
import com.cranium.sushiteiapps.model.response.TermAndConditions;
import com.cranium.sushiteiapps.model.response.Wishlists;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dayona on 07/06/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 13;

    // database name
    private static final String DATABASE_NANE = "sushiteiDb";

    // table name
    private static final String TABLE_OUTLET_CITY = "outlet_city";
    private static final String TABLE_CITY = "city";
    private static final String TABLE_USER = "user";
    private static final String TABLE_SEAT = "seat";
    private static final String TABLE_MESSAGE = "message";
    private static final String TABLE_MESSAGES = "messages";
    private static final String TABLE_ARTICLE = "article";
    private static final String TABLE_ARTICLE_NEW = "articlenew";
    private static final String TABLE_GREETING = "greeting";
    private static final String TABLE_TERM_CONDITION = "termcondition";
    private static final String TABLE_ABOUT_SUSHI_TEI = "aboutsushitei";
    private static final String TABLE_HOT_MENU = "hotmenu";
    private static final String TABLE_HOT_MENU_DETAILS = "hotmenudetails";
    private static final String TABLE_MENU_CATEGORY = "menucategory";
    private static final String TABLE_MENU_CATEGORY_DETAILS = "menucategorydetails";
    private static final String TABLE_HOT_MENU_NEW = "hotmenunew";
    private static final String TABLE_HOT_MENU_DETAILS_NEW = "hotmenudetailsnew";
    private static final String TABLE_MENU_CATEGORY_NEW = "menucategorynew";
    private static final String TABLE_MENU_CATEGORY_DETAILS_NEW = "menucategorydetailsnew";
    private static final String TABLE_PROMO_BANNERS = "promobanner";
    private static final String TABLE_WISHLIST = "wishlist";
    private static final String TABLE_WISHLISTS = "wishlists";
    private static final String TABLE_HISTORY_POINT = "historypoint";
    private static final String TABLE_FIREBASE = "firebase";
    private static final String TABLE_VOUCHER = "voucher";
    private static final String TABLE_MESSAGE_READ = "messageread";

    // column name
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_RAW = "raw";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String COLUMN_CATEGORY = "category";

    private static final String COLUMN_MEMBER_CODE = "member_code";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_FIREBASE_TOKEN = "firebase_token";
    private static final String COLUMN_DOB = "dob";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_CITY_ID = "city_id";
    private static final String COLUMN_POINT = "point";
    private static final String COLUMN_POINT_EXPIRING = "point_expiring";
    private static final String COLUMN_POINT_EXPIRED_AT = "point_expired_at";
    private static final String COLUMN_DATE_OF_EXPIRY = "date_of_expiry";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_REGISTERED_AT = "registered_at";
    private static final String COLUMN_QRCODE = "qrcode";
    private static final String COLUMN_DEVICE_NUMBER = "device_number";

    private static final String COLUMN_MESSAGE_ID = "message_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CODE_VOUCHER = "code_voucher";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_END_DATE = "end_date";
    private static final String COLUMN_PUSH_TO = "push_to";
    private static final String COLUMN_UPDATE_AT = "update_at";
    private static final String COLUMN_READ_AT = "read_at";
    private static final String COLUMN_IS_VOUCHER = "is_voucher";
    private static final String COLUMN_IS_USE = "is_use";
    private static final String COLUMN_IS_READ = "is_read";

    private static final String COLUMN_MENU_ID = "menu_id";
    private static final String COLUMN_MENU_CATEGORY = "menu_category";
    private static final String COLUMN_IS_SELECT = "is_select";

    //HOT MENU NEW and HOT MENU DETAILS NEW
    private static final String COLUMN_MENU_CATEGORY_ID = "menu_category_id";
    private static final String COLUMN_IS_HOT_MENU = "is_hot_menu";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_ORDER = "orders";
    private static final String COLUMN_IS_WISHLIST = "is_wishlist";

    //ARTICLE NEW
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_ARTICLE_DATE = "article_date";
    private static final String COLUMN_META_DESCRIPTION = "meta_description";

    private static final String CREATE_TABLE_OUTLET_CITY = "CREATE TABLE "
            + TABLE_OUTLET_CITY + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_TERM_CONDITION = "CREATE TABLE "
            + TABLE_TERM_CONDITION + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_ABOUT_SUSHI_TEI = "CREATE TABLE "
            + TABLE_ABOUT_SUSHI_TEI + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_GREETING = "CREATE TABLE "
            + TABLE_GREETING + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CATEGORY + " TEXT, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MEMBER_CODE + " TEXT, "
            + COLUMN_FIRST_NAME + " TEXT, "
            + COLUMN_LAST_NAME + " TEXT, "
            + COLUMN_EMAIL + " TEXT, "
            + COLUMN_IMAGE + " TEXT, "
            + COLUMN_FIREBASE_TOKEN + " TEXT, "
            + COLUMN_DOB + " TEXT, "
            + COLUMN_PHONE + " TEXT, "
            + COLUMN_CITY_ID + " TEXT, "
            + COLUMN_POINT + " INTEGER, "
            + COLUMN_POINT_EXPIRING + " INTEGER, "
            + COLUMN_POINT_EXPIRED_AT + " TEXT, "
            + COLUMN_DATE_OF_EXPIRY + " TEXT, "
            + COLUMN_STATUS + " TEXT, "
            + COLUMN_REGISTERED_AT + " TEXT, "
            + COLUMN_QRCODE + " TEXT )";

    private static final String CREATE_TABLE_CITY = "CREATE TABLE "
            + TABLE_CITY + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_SEAT = "CREATE TABLE "
            + TABLE_SEAT + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_MESSAGE = "CREATE TABLE "
            + TABLE_MESSAGE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_MESSAGES = "CREATE TABLE "
            + TABLE_MESSAGES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_IMAGE + " TEXT, "
            + COLUMN_CODE_VOUCHER + " TEXT, "
            + COLUMN_LATITUDE + " TEXT, "
            + COLUMN_LONGITUDE + " TEXT, "
            + COLUMN_START_DATE + " TEXT, "
            + COLUMN_END_DATE + " TEXT, "
            + COLUMN_PUSH_TO + " TEXT, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp, "
            + COLUMN_UPDATE_AT + " datetime default current_timestamp, "
            + COLUMN_READ_AT + " datetime, "
            + COLUMN_IS_VOUCHER + " INTEGER, "
            + COLUMN_IS_USE + " INTEGER default 0,"
            + COLUMN_IS_READ + " INTEGER default 0)";

    private static final String CREATE_TABLE_VOUCHER = "CREATE TABLE "
            + TABLE_VOUCHER + "(" + COLUMN_ID + " INTEGER, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_ARTICLE = "CREATE TABLE "
            + TABLE_ARTICLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_ARTICLE_NEW = "CREATE TABLE "
            + TABLE_ARTICLE_NEW + "(" + COLUMN_ID + " INTEGER , "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_IMAGE + " TEXT, "
            + COLUMN_ARTICLE_DATE + " datetime default current_timestamp, "
            + COLUMN_META_DESCRIPTION + " TEXT, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp, "
            + COLUMN_UPDATE_AT + " datetime default current_timestamp, "
            + COLUMN_URL + " TEXT)";

    private static final String CREATE_TABLE_HOT_MENU_DETAILS = "CREATE TABLE "
            + TABLE_HOT_MENU_DETAILS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CATEGORY + " TEXT, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_MENU_CATEGORY_DETAILS = "CREATE TABLE "
            + TABLE_MENU_CATEGORY_DETAILS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CATEGORY + " TEXT, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_HOT_MENU = "CREATE TABLE "
            + TABLE_HOT_MENU + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_MENU_CATEGORY = "CREATE TABLE "
            + TABLE_MENU_CATEGORY + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_HOT_MENU_NEW =  "CREATE TABLE "
            + TABLE_HOT_MENU_NEW + "(" + COLUMN_ID + " INTEGER  , "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_IMAGE + " TEXT, "
            + COLUMN_ORDER + " INTEGER , "
            + COLUMN_CREATED_AT + " datetime default current_timestamp, "
            + COLUMN_UPDATE_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_HOT_MENU_DETAIL_NEW =  "CREATE TABLE "
            + TABLE_HOT_MENU_DETAILS_NEW + "(" + COLUMN_ID + " INTEGER , "
            + COLUMN_MENU_CATEGORY_ID + " INTEGER, "
            + COLUMN_IS_HOT_MENU + " INTEGER, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_PRICE + " TEXT, "
            + COLUMN_ORDER + " INTEGER , "
            + COLUMN_CREATED_AT + " datetime default current_timestamp, "
            + COLUMN_UPDATE_AT + " datetime default current_timestamp, "
            + COLUMN_IS_WISHLIST + " INTEGER, "
            + COLUMN_IMAGE + " TEXT) ";

    private static final String CREATE_TABLE_MENU_CATEGORY_NEW =  "CREATE TABLE "
            + TABLE_MENU_CATEGORY_NEW + "(" + COLUMN_ID + " INTEGER , "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_IMAGE + " TEXT, "
            + COLUMN_ORDER + " INTEGER , "
            + COLUMN_CREATED_AT + " datetime default current_timestamp, "
            + COLUMN_UPDATE_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_MENU_CATEGORY_DETAIL_NEW =  "CREATE TABLE "
            + TABLE_MENU_CATEGORY_DETAILS_NEW + "(" + COLUMN_ID + " INTEGER , "
            + COLUMN_MENU_CATEGORY_ID + " INTEGER, "
            + COLUMN_IS_HOT_MENU + " INTEGER, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_PRICE + " TEXT, "
            + COLUMN_ORDER + " INTEGER , "
            + COLUMN_CREATED_AT + " datetime default current_timestamp, "
            + COLUMN_UPDATE_AT + " datetime default current_timestamp, "
            + COLUMN_IS_WISHLIST + " INTEGER, "
            + COLUMN_IMAGE + " TEXT) ";

    private static final String CREATE_TABLE_PROMO_BANNER = "CREATE TABLE "
            + TABLE_PROMO_BANNERS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_WISHLIST = "CREATE TABLE "
            + TABLE_WISHLIST + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MENU_CATEGORY + " TEXT, "
            + COLUMN_MENU_ID + " INTEGER, "
            + COLUMN_IS_SELECT + " INTEGER default 0)";

    private static final String CREATE_TABLE_WISHLISTS = "CREATE TABLE "
            + TABLE_WISHLISTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_HISTORY_POINT = "CREATE TABLE "
            + TABLE_HISTORY_POINT + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RAW + " BLOB, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp)";

    private static final String CREATE_TABLE_FIREBASE = "CREATE TABLE "
            + TABLE_FIREBASE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FIREBASE_TOKEN + " TEXT, "
            + COLUMN_DEVICE_NUMBER + " TEXT) ";

    private static final String CREATE_TABLE_MESSAGE_READ = "CREATE TABLE "
            + TABLE_MESSAGE_READ + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MESSAGE_ID + " INTEGER , "
            + COLUMN_IS_VOUCHER + " INTEGER default 0, "
            + COLUMN_IS_READ + " INTEGER default 0) ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NANE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_OUTLET_CITY);
        db.execSQL(CREATE_TABLE_CITY);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_SEAT);
        db.execSQL(CREATE_TABLE_ARTICLE);
        db.execSQL(CREATE_TABLE_MESSAGE);
        db.execSQL(CREATE_TABLE_MESSAGES);
        db.execSQL(CREATE_TABLE_GREETING);
        db.execSQL(CREATE_TABLE_TERM_CONDITION);
        db.execSQL(CREATE_TABLE_ABOUT_SUSHI_TEI);
        db.execSQL(CREATE_TABLE_HOT_MENU_DETAILS);
        db.execSQL(CREATE_TABLE_HOT_MENU);
        db.execSQL(CREATE_TABLE_HOT_MENU_DETAIL_NEW);
        db.execSQL(CREATE_TABLE_HOT_MENU_NEW);
        db.execSQL(CREATE_TABLE_MENU_CATEGORY);
        db.execSQL(CREATE_TABLE_MENU_CATEGORY_NEW);
        db.execSQL(CREATE_TABLE_MENU_CATEGORY_DETAILS);
        db.execSQL(CREATE_TABLE_MENU_CATEGORY_DETAIL_NEW);
        db.execSQL(CREATE_TABLE_PROMO_BANNER);
        db.execSQL(CREATE_TABLE_WISHLIST);
        db.execSQL(CREATE_TABLE_WISHLISTS);
        db.execSQL(CREATE_TABLE_HISTORY_POINT);
        db.execSQL(CREATE_TABLE_FIREBASE);
        db.execSQL(CREATE_TABLE_VOUCHER);
        db.execSQL(CREATE_TABLE_MESSAGE_READ);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUTLET_CITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GREETING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERM_CONDITION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABOUT_SUSHI_TEI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOT_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOT_MENU_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMO_BANNERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_CATEGORY_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLISTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_POINT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIREBASE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOT_MENU_NEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOT_MENU_DETAILS_NEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_CATEGORY_NEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_CATEGORY_DETAILS_NEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOUCHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE_READ);
        onCreate(db);
    }

    public void createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_USER, COLUMN_MEMBER_CODE + " = ?",
                new String[] { String.valueOf(user.getMemberCode()) });

        ContentValues values = new ContentValues();
        values.put(COLUMN_MEMBER_CODE, user.getMemberCode());
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_FIREBASE_TOKEN, user.getFirebaseToken());
        if (user.getImage() != null) {
            values.put(COLUMN_IMAGE, user.getImage());
        } else {
            values.put(COLUMN_IMAGE, "");
        }
        values.put(COLUMN_DOB, user.getDob());
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_CITY_ID, user.getCityId());
        Integer point = 0;
        point = (user.getPoint() != null) ? Integer.parseInt(user.getPoint()) : 0;
        Integer pointExpiring = 0;
        pointExpiring = (user.getPointExpiring() != null) ? Integer.parseInt(user.getPointExpiring()) : 0;
        values.put(COLUMN_POINT, point);
        values.put(COLUMN_POINT_EXPIRING, pointExpiring);
        values.put(COLUMN_DATE_OF_EXPIRY, user.getDateOfExpiry());
        values.put(COLUMN_POINT_EXPIRED_AT, user.getPointExpiredAt());
        values.put(COLUMN_STATUS, user.getStatus());
        Log.e(App.LOG, "status db user status " + user.getStatus()+" point expiring "+pointExpiring);
        if(user.getRegisteredAt() != null){
            values.put(COLUMN_REGISTERED_AT, user.getRegisteredAt());
        }else{
            values.put(COLUMN_REGISTERED_AT, "");
        }
        values.put(COLUMN_QRCODE, user.getQrcode());
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public User getUserByMemberCode(String memberCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        User user = new User();
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_MEMBER_CODE + "=?", new String[] {memberCode});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                user.setMemberCode(cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_CODE)));
                user.setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)));
                user.setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
                user.setFirebaseToken(cursor.getString(cursor.getColumnIndex(COLUMN_FIREBASE_TOKEN)));
                user.setDob(cursor.getString(cursor.getColumnIndex(COLUMN_DOB)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
                user.setCityId(cursor.getString(cursor.getColumnIndex(COLUMN_CITY_ID)));
                user.setPoint(cursor.getString(cursor.getColumnIndex(COLUMN_POINT)));
                user.setPointExpiring(cursor.getString(cursor.getColumnIndex(COLUMN_POINT_EXPIRING)));
                user.setDateOfExpiry(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_OF_EXPIRY)));
                user.setPointExpiredAt(cursor.getString(cursor.getColumnIndex(COLUMN_POINT_EXPIRED_AT)));
                user.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
                user.setRegisteredAt(cursor.getString(cursor.getColumnIndex(COLUMN_REGISTERED_AT)));
                user.setQrcode(cursor.getString(cursor.getColumnIndex(COLUMN_QRCODE)));
            }
            return user;
        }finally {
            cursor.close();
        }
    }

    /**
    * CRUD hot menus
    * */
    public void createHotMenu(HotMenus menus) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOT_MENU, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(menus).getBytes());
        db.insert(TABLE_HOT_MENU, null, values);

        db.close();
    }

    public void createHotMenuNew(MenuCategory menus) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_HOT_MENU_NEW, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,menus.getId());
        values.put(COLUMN_NAME,menus.getName());
        values.put(COLUMN_IMAGE,menus.getImage());
        values.put(COLUMN_ORDER,menus.getOrder());
        values.put(COLUMN_CREATED_AT,menus.getCreatedAt());
        values.put(COLUMN_UPDATE_AT,menus.getUpdatedAt());

        db.insert(TABLE_HOT_MENU_NEW, null, values);

        db.close();
    }

    public List<MenuCategory> getAllMenuCategory(){
        List<MenuCategory> menus = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_HOT_MENU, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                HotMenus menu = gson.fromJson(new String(cursor.getBlob(1)),  HotMenus.class);
                menus.addAll(menu.getMenuCategories());
            }
            return menus;
        }finally {
            cursor.close();
        }
    }

    public List<MenuCategory> getAllHotMenuNew(){
        List<MenuCategory> menus = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_HOT_MENU_NEW, null);
            if(cursor.getCount() > 0) {
                while(cursor.moveToFirst()){
                    Integer id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    String image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));
                    String order = String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER)));
                    String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT));
                    String updateAt = cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_AT));
                    menus.add(new MenuCategory(id,name,image,order,createdAt,updateAt));
                }
            }
            return menus;
        }finally {
            cursor.close();
        }
    }

    public void deleteAlLHotMenu() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOT_MENU, null, null);
        db.close();
    }

    public void deleteSingleHotMenu(MenuCategory menus) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_HOT_MENU_NEW+" WHERE "+COLUMN_NAME +" = '"+menus.getName()+"'");
        db.close();
    }

    /**
    * CRUD Menu Hot Detail List
    * */
    public void createHotMenuDetails(HotMenusByCategory menus, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOT_MENU_DETAILS, COLUMN_CATEGORY + " = ?", new String[] { category });

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(menus).getBytes());
        values.put(COLUMN_CATEGORY,category);
        db.insert(TABLE_HOT_MENU_DETAILS, null, values);

        db.close();
    }

    public void createHotMenuDetailNew(Menu menus) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_HOT_MENU_DETAILS_NEW, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,menus.getId());
        values.put(COLUMN_MENU_CATEGORY_ID,menus.getMenuCategoryId());
        values.put(COLUMN_IS_HOT_MENU,menus.getIsHotMenu());
        values.put(COLUMN_NAME,menus.getName());
        values.put(COLUMN_PRICE,menus.getPrice());
        values.put(COLUMN_ORDER,Integer.parseInt(menus.getOrder()));
        values.put(COLUMN_CREATED_AT,menus.getCreatedAt());
        values.put(COLUMN_UPDATE_AT,menus.getUpdatedAt());
        values.put(COLUMN_IS_WISHLIST,menus.getIsWishlist());
        values.put(COLUMN_IMAGE,menus.getImage());

        db.insert(TABLE_HOT_MENU_DETAILS_NEW, null, values);

        db.close();
    }

    public List<Menu> getAllHotMenuDetail(String category){
        List<Menu> menus = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_HOT_MENU_DETAILS +" WHERE "+COLUMN_CATEGORY+" = ?", new String[]{category});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                HotMenusByCategory menu = gson.fromJson(new String(cursor.getBlob(1)),  HotMenusByCategory.class);
                menus.addAll(menu.getMenus());
            }
            return menus;
        }finally {
            cursor.close();
        }
    }

    public List<Menu> getAllHotMenuDetailNew(int category){
        List<Menu> menus = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_HOT_MENU_DETAILS_NEW +" WHERE "+COLUMN_MENU_CATEGORY_ID+" = ?", new String[]{""+category});
            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()){
                    Integer id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String menuCategoryId = String.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_MENU_CATEGORY_ID)));
                    String isHotMenu = String.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_IS_HOT_MENU)));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                    String price = cursor.getString(cursor.getColumnIndex(COLUMN_PRICE));
                    String order = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER));
                    String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT));
                    String updateAt = cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_AT));
                    Integer isWishlist = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_WISHLIST));
                    String image = String.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));

                    menus.add(new Menu(id,menuCategoryId,isHotMenu,name,description,price,order,createdAt,updateAt,isWishlist,image));
                }
            }
            return menus;
        }finally {
            cursor.close();
        }
    }

    public void deleteSingleHotMenuDetail(Menu menus) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_HOT_MENU_DETAILS_NEW+" WHERE "+COLUMN_NAME +" = '"+menus.getName()+"'");
        db.close();
    }

    public void deleteAlLMenuDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOT_MENU_DETAILS, null, null);
        db.close();
    }

    /**
    * CRUD Menu Category
    * */
    public void createMenuCategory(MenuCategories menus) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENU_CATEGORY, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(menus).getBytes());
        db.insert(TABLE_MENU_CATEGORY, null, values);

        db.close();
    }

    public void createMenuCategoryNew(MenuCategory menus) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_MENU_CATEGORY_NEW, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,menus.getId());
        values.put(COLUMN_NAME,menus.getName());
        values.put(COLUMN_IMAGE,menus.getImage());
        values.put(COLUMN_ORDER,Integer.parseInt(menus.getOrder()));
        values.put(COLUMN_CREATED_AT,menus.getCreatedAt());
        values.put(COLUMN_UPDATE_AT,menus.getUpdatedAt());

        db.insert(TABLE_MENU_CATEGORY_NEW, null, values);

        db.close();
    }

    public List<MenuCategory> getAllMenuCategories(){
        List<MenuCategory> menus = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU_CATEGORY , null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                MenuCategories menu = gson.fromJson(new String(cursor.getBlob(1)),  MenuCategories.class);
                menus.addAll(menu.getMenuCategories());
            }
            return menus;
        }finally {
            cursor.close();
        }
    }

    public List<MenuCategory> getAllMenuCategoryNew(){
        List<MenuCategory> menus = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU_CATEGORY_NEW, null);
            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()){
                    Integer id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    String image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));
                    String order = String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER)));
                    String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT));
                    String updateAt = cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_AT));
                    menus.add(new MenuCategory(id,name,image,order,createdAt,updateAt));
                }
            }
            return menus;
        }finally {
            cursor.close();
        }
    }

    public MenuCategory getLatestMenuCategory(){
        MenuCategory menu = new MenuCategory();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU_CATEGORY_NEW+" ORDER BY "+ COLUMN_CREATED_AT +" DESC", null);
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                Integer id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));
                String order = String.valueOf(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER)));
                String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT));
                String updateAt = cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_AT));
                menu = new MenuCategory(id,name,image,order,createdAt,updateAt);
            }
            return menu;
        }finally {
            cursor.close();
        }
    }

    public void deleteSingleMenuCategory(MenuCategory menus){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_MENU_CATEGORY_NEW+" WHERE "+COLUMN_NAME +" = '"+menus.getName()+"'");
        db.close();
    }

    public void deleteAlLMenus() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENU_CATEGORY, null, null);
        db.close();
    }

    public void deleteAlLMenuCategoryNew() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENU_CATEGORY_NEW, null, null);
        db.close();
    }

    /**
    * CRUD Menu Category Details
    * */
    public void createMenuCategoryDetails(HotMenusByCategory menus, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENU_CATEGORY_DETAILS, COLUMN_CATEGORY + " = ?", new String[] { category });

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(menus).getBytes());
        values.put(COLUMN_CATEGORY,category);
        db.insert(TABLE_MENU_CATEGORY_DETAILS, null, values);

        db.close();
    }

    public void createMenuCategoryDetailNew(Menu menus) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_MENU_CATEGORY_DETAILS_NEW, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,menus.getId());
        values.put(COLUMN_MENU_CATEGORY_ID,menus.getMenuCategoryId());
        values.put(COLUMN_IS_HOT_MENU,menus.getIsHotMenu());
        values.put(COLUMN_NAME,menus.getName());
        values.put(COLUMN_PRICE,menus.getPrice());
        values.put(COLUMN_ORDER,menus.getOrder());
        values.put(COLUMN_CREATED_AT,menus.getCreatedAt());
        values.put(COLUMN_UPDATE_AT,menus.getUpdatedAt());
        values.put(COLUMN_IS_WISHLIST,menus.getIsWishlist());
        values.put(COLUMN_IMAGE,menus.getImage());

        db.insert(TABLE_MENU_CATEGORY_DETAILS_NEW, null, values);

        db.close();
    }

    public List<Menu> getAllMenuCategoriesDetails(String category){
        List<Menu> menus = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU_CATEGORY_DETAILS +" WHERE "+COLUMN_CATEGORY+" = ?", new String[]{category});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                HotMenusByCategory menu = gson.fromJson(new String(cursor.getBlob(1)),  HotMenusByCategory.class);
                menus.addAll(menu.getMenus());
            }
            return menus;
        }finally {
            cursor.close();
        }
    }

    public List<Menu> getAllMenuCategoryDetailNew(int category){
        List<Menu> menus = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU_CATEGORY_DETAILS_NEW +" WHERE "+COLUMN_MENU_CATEGORY_ID+" = ? ", new String[]{""+category});
            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()){
                    Integer id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String menuCategoryId = String.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_MENU_CATEGORY_ID)));
                    String isHotMenu = String.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_IS_HOT_MENU)));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                    String price = cursor.getString(cursor.getColumnIndex(COLUMN_PRICE));
                    String order = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER));
                    String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT));
                    String updateAt = cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_AT));
                    Integer isWishlist = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_WISHLIST));
                    String image = String.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));

                    menus.add(new Menu(id,menuCategoryId,isHotMenu,name,description,price,order,createdAt,updateAt,isWishlist,image));
                }
            }
            return menus;
        }finally {
            cursor.close();
        }
    }

    public void deleteSingleMenuCategoryDetail(Menu menus){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_MENU_CATEGORY_DETAILS_NEW+" WHERE "+COLUMN_NAME +" = '"+menus.getName()+"'");
        db.close();
    }

    public void deleteAlLMenuCategoryDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENU_CATEGORY_DETAILS, null, null);
        db.close();
    }

    /**
    * CRUD Promo Banners
    * */
    public void createPromoBanner(PromoBanners promo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROMO_BANNERS, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(promo).getBytes());
        db.insert(TABLE_PROMO_BANNERS, null, values);

        db.close();
    }

    public List<PromoBanner> getAllPromoBanners(){
        List<PromoBanner> menus = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_PROMO_BANNERS , null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                PromoBanners menu = gson.fromJson(new String(cursor.getBlob(1)),  PromoBanners.class);
                menus.addAll(menu.getPromoBanners());
            }
            return menus;
        }finally {
            cursor.close();
        }
    }

    public void deleteAlLBanner() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROMO_BANNERS, null, null);
        db.close();
    }

    /**
    * CRUD Wishlist
    */
    public void createWishlist(MenuWishlist menu) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WISHLIST, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_MENU_CATEGORY, menu.getMenuCategoryId());
        values.put(COLUMN_MENU_ID, menu.getMenuId());
        values.put(COLUMN_IS_SELECT, menu.getIsWishlist());
        db.insert(TABLE_WISHLIST, null, values);
        db.close();
    }

    public List<Wishlist> getAllWishlists(){
        List<Wishlist> points = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_WISHLISTS , null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                Wishlists point = gson.fromJson(new String(cursor.getBlob(1)),  Wishlists.class);
                points.addAll(point.getWishlists());
            }
            return points;
        }finally {
            cursor.close();
        }
    }

    public void createWishlists(Wishlists menu) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WISHLISTS, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(menu).getBytes());
        db.insert(TABLE_WISHLISTS, null, values);
        db.close();
    }

    public void updateWishlist(Integer menuId,Integer isWishlist) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_WISHLIST, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_SELECT, isWishlist);
        db.update(TABLE_WISHLIST, values, "menu_id = "+menuId,null );
        db.close();
    }

    public List<MenuWishlist> getAllWishlist(){
        List<MenuWishlist> wish = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_WISHLIST , null);
            while(cursor.moveToFirst()){
                String category = cursor.getString(cursor.getColumnIndex(COLUMN_MENU_CATEGORY));
                Integer id = cursor.getInt(cursor.getColumnIndex(COLUMN_MENU_ID));
                Integer is_wishlist = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_SELECT));
                wish.add(new MenuWishlist(category,id,is_wishlist));
            }
            return wish;
        }finally {
            cursor.close();
        }
    }

    public List<MenuWishlist> getAllWishlistByCategory(String categories){
        List<MenuWishlist> wish = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_WISHLIST +" WHERE "+COLUMN_MENU_CATEGORY +" = ?",
                    new String[]{categories});
            while(cursor.moveToFirst()){
                String category = cursor.getString(cursor.getColumnIndex(COLUMN_MENU_CATEGORY));
                Integer id = cursor.getInt(cursor.getColumnIndex(COLUMN_MENU_ID));
                Integer is_wishlist = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_SELECT));
                wish.add(new MenuWishlist(category,id,is_wishlist));
            }
            return wish;
        }finally {
            cursor.close();
        }
    }

    public MenuWishlist getWishlist(Integer menuId){
        MenuWishlist wish = new MenuWishlist();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_WISHLIST + " WHERE "+COLUMN_MENU_ID+" = ?", null);
            if(cursor.getCount() > 0) {
                String category = cursor.getString(cursor.getColumnIndex(COLUMN_MENU_CATEGORY));
                Integer id = cursor.getInt(cursor.getColumnIndex(COLUMN_MENU_ID));
                Integer is_wishlist = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_SELECT));
                new MenuWishlist(category,id,is_wishlist);
            }
            return wish;
        }finally {
            cursor.close();
        }
    }

    public void deleteAlLWishes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WISHLIST, null, null);
        db.close();
    }

    /**
    * CRUD History Point
    */
    public void createHistoryPoint(PointHistories points) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY_POINT, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(points).getBytes());
        db.insert(TABLE_HISTORY_POINT, null, values);

        db.close();
    }

    public List<PointHistory> getAllHistoryPoint(){
        List<PointHistory> points = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_HISTORY_POINT , null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                PointHistories point = gson.fromJson(new String(cursor.getBlob(1)),  PointHistories.class);
                points.addAll(point.getUser().getPointHistories());
            }
            return points;
        }finally {
            cursor.close();
        }
    }

    public void deleteAlLHistoryPoint() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY_POINT, null, null);
        db.close();
    }

    /**
     * CRUD Cities
     */
    public void createCity(Cities cities) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CITY, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(cities).getBytes());
        db.insert(TABLE_CITY, null, values);

        db.close();
    }

    public List<City> getAllCities() {
        List<City> cities = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_CITY, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                Cities city = gson.fromJson(new String(cursor.getBlob(1)), Cities.class);
                cities.addAll(city.getCities());
            }
            return cities;
        }finally {
            cursor.close();
        }
    }

    /**
     * CRUD Term Condition
     */
    public void createTermCondition(TermAndConditions terms) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TERM_CONDITION, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(terms).getBytes());
        db.insert(TABLE_TERM_CONDITION, null, values);

        db.close();
    }

    public List<TermCondition> getAllTermCondition() {
        List<TermCondition> terms = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_TERM_CONDITION, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                TermAndConditions term = gson.fromJson(new String(cursor.getBlob(1)), TermAndConditions.class);
                terms.addAll(term.getTermConditions());
            }
            return terms;
        }finally {
            cursor.close();
        }
    }

    /**
     * CRUD About Suhitei
     */
    public void createAboutSushitei(TermAndConditions terms) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ABOUT_SUSHI_TEI, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(terms).getBytes());
        db.insert(TABLE_ABOUT_SUSHI_TEI, null, values);

        db.close();
    }

    public List<TermCondition> getAllAboutSushitei() {
        List<TermCondition> terms = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_ABOUT_SUSHI_TEI, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                TermAndConditions term = gson.fromJson(new String(cursor.getBlob(1)), TermAndConditions.class);
                terms.addAll(term.getTermConditions());
            }
            return terms;
        }finally {
            cursor.close();
        }
    }

    /**
     * CRUD Greeting
     */
    public void createGreeting(Greetings greetings, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GREETING, COLUMN_CATEGORY + " = ?", new String[] { category });

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(greetings).getBytes());
        values.put(COLUMN_CATEGORY, category);
        db.insert(TABLE_GREETING, null, values);

        db.close();
    }

    public List<Greeting> getAllGreeting(String category) {
        List<Greeting> greetings = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_GREETING + " WHERE " + COLUMN_CATEGORY + " = ?", new String[] {category});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                Greetings greeting = gson.fromJson(new String(cursor.getBlob(1)), Greetings.class);
                greetings.addAll(greeting.getGreetings());
            }
            return greetings;
        }finally {
            cursor.close();
        }
    }

    /**
     * CRUD Outlet City
     */
    public void createOutletCity(OutletCities outletCities) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OUTLET_CITY, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(outletCities).getBytes());
        db.insert(TABLE_OUTLET_CITY, null, values);

        db.close();
    }

    public List<OutletCity> getAllOutletCities() {
        List<OutletCity> outletCities = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_OUTLET_CITY , null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                OutletCities outletCity = gson.fromJson(new String(cursor.getBlob(1)), OutletCities.class);
                outletCities.addAll(outletCity.getOutletCities());
            }
            return outletCities;
        }finally {
            cursor.close();
        }
    }

    /**
     * CRUD Seats
     */
    public void createSeat(Seats seats) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SEAT, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(seats).getBytes());
        db.insert(TABLE_SEAT, null, values);

        db.close();
    }

    public List<Seat> getAllSeats() {
        List<Seat> seats = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_SEAT, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                Seats seat = gson.fromJson(new String(cursor.getBlob(1)), Seats.class);
                seats.addAll(seat.getSeats());
            }
            return seats;
        }finally {
            cursor.close();
        }
    }

    /**
    * CRUD Messages
    */
    public void createMessage(Messages messages) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGE, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(messages).getBytes());
        db.insert(TABLE_MESSAGE, null, values);

        db.close();
    }

    public void createMess(Message mess){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGES, null, null);

        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, mess.getId());
        values.put(COLUMN_NAME, mess.getName());
        values.put(COLUMN_DESCRIPTION, mess.getDescription());
        if (mess.getImage() != null) values.put(COLUMN_IMAGE, mess.getImage());
        else values.put(COLUMN_IMAGE, "");

        values.put(COLUMN_CODE_VOUCHER, mess.getCodeVoucher());
//
//        values.put(COLUMN_LATITUDE, mess.getLatitude().toString());
//        values.put(COLUMN_LONGITUDE, mess.getLongitude().toString());
        values.put(COLUMN_START_DATE, mess.getStartDate());
        values.put(COLUMN_END_DATE, mess.getEndDate());
//        values.put(COLUMN_UPDATE_AT, mess.getUpdatedAt().toString());

        if (mess.getReadAt() != null) values.put(COLUMN_READ_AT, mess.getReadAt());
        else values.put(COLUMN_READ_AT, "");

        Log.i("surya", "surya db: " + mess.getReadAt());
        values.put(COLUMN_IS_VOUCHER, mess.getIsVoucher());
        if (mess.getIsUse() !=null)values.put(COLUMN_IS_USE, mess.getIsUse());
        else values.put(COLUMN_IS_USE, "");

        db.insert(TABLE_MESSAGES, null, values);
        db.close();
    }

    public Messages getAllMessages() {
        Messages messages = new Messages();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_MESSAGE, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                messages = gson.fromJson(new String(cursor.getBlob(1)), Messages.class);
            }
            return messages;
        }finally {
            cursor.close();
        }
    }

    public List<Message> getAllMessage(){
        List<Message> messages = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_MESSAGES , null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                Messages mess = gson.fromJson(new String(cursor.getBlob(1)), Messages.class);
                messages.addAll(mess.getMessages());
            }
            return messages;
        }finally {
            cursor.close();
        }
    }

    public void deleteAlLMessage() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGE, null, null);
        db.close();
    }

    public void createVoucher(Messages messages) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VOUCHER, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(messages).getBytes());
        db.insert(TABLE_VOUCHER, null, values);

        db.close();
    }

    public Messages getAllVouchers() {
        Messages messages = new Messages();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_VOUCHER, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                messages = gson.fromJson(new String(cursor.getBlob(1)), Messages.class);
            }
            return messages;
        }finally {
            cursor.close();
        }
    }

    public void deleteAlLVoucher() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VOUCHER, null, null);
        db.close();
    }

    public void deleteAlLMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGES, null, null);
        db.close();
    }

    public int getMessageCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MESSAGE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * CRUD Article Whats new
     */
    public void createArticle(Articles articles) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTICLE, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_RAW, new Gson().toJson(articles).getBytes());
        db.insert(TABLE_ARTICLE, null, values);

        db.close();
    }

    public List<Article> getAlArticles() {
        List<Article> articles = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_ARTICLE, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                Gson gson = new Gson();
                Articles article = gson.fromJson(new String(cursor.getBlob(1)), Articles.class);
                articles.addAll(article.getArticles());
            }
            return articles;
        }finally {
            cursor.close();
        }
    }

    /**
     * CRUD FIREBASE
     * @param firebase
     * @return
     */
    public void createFirebase(Firebase firebase){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_FIREBASE, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, firebase.getId());
        values.put(COLUMN_FIREBASE_TOKEN, firebase.getFirebaseToken());
        values.put(COLUMN_DEVICE_NUMBER, firebase.getDeviceNumber());

        db.insert(TABLE_FIREBASE, null, values);
        db.close();
    }

    public Firebase getFirebase() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Firebase firebase = new Firebase();
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_FIREBASE ,null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                firebase.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                firebase.setFirebaseToken(cursor.getString(cursor.getColumnIndex(COLUMN_FIREBASE_TOKEN)));
                firebase.setDeviceNumber(cursor.getString(cursor.getColumnIndex(COLUMN_DEVICE_NUMBER)));
            }
            return firebase;
        }finally {
            cursor.close();
        }
    }

    /**
     * CRUD VOUCHER
     * @param message
     * @return
     */
    public void createVoucher(Message message){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, message.getId());
        values.put(COLUMN_NAME, message.getName());
        values.put(COLUMN_DESCRIPTION, message.getDescription());
        values.put(COLUMN_IMAGE, message.getImage());
        values.put(COLUMN_CODE_VOUCHER, message.getCodeVoucher());
        Log.e(App.LOG, "CODE VOUCHER FROM DB :"+message.getCodeVoucher());
        if (message.getLatitude() != null) values.put(COLUMN_LATITUDE, message.getLatitude().toString());
        else values.put(COLUMN_LATITUDE, "");
        if (message.getLongitude() != null) values.put(COLUMN_LONGITUDE, message.getLongitude().toString());
        else values.put(COLUMN_LONGITUDE, "");
        values.put(COLUMN_START_DATE, message.getStartDate());
        values.put(COLUMN_END_DATE, message.getEndDate());
        values.put(COLUMN_PUSH_TO, message.getPushTo());
        values.put(COLUMN_CREATED_AT, message.getCreatedAt());
        if (message.getUpdatedAt() != null) values.put(COLUMN_UPDATE_AT, message.getUpdatedAt().toString());
        else values.put(COLUMN_UPDATE_AT, "");
        values.put(COLUMN_READ_AT, message.getReadAt());
        values.put(COLUMN_IS_VOUCHER, message.getIsVoucher());
        values.put(COLUMN_IS_USE, message.getIsUse());
        db.insert(TABLE_VOUCHER, null, values);

        db.close();
    }

    public List<Message> getAllVoucher(){
        List<Message> messages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_VOUCHER, null);
            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()){
                    Integer id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                    String image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));
                    String codeVoucher = String.valueOf(cursor.getColumnIndex(COLUMN_CODE_VOUCHER));
                    Object latitude = new Object[]{String.valueOf(cursor.getColumnIndex(COLUMN_LATITUDE))};
                    Object longitude = new Object[]{String.valueOf(cursor.getColumnIndex(COLUMN_LONGITUDE))};
                    String startDate = cursor.getString(cursor.getColumnIndex(COLUMN_START_DATE));
                    String endDate = cursor.getString(cursor.getColumnIndex(COLUMN_END_DATE));
                    String pushTo = cursor.getString(cursor.getColumnIndex(COLUMN_PUSH_TO));
                    String createdAt = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT));
                    Object updateAt = new Object[]{String.valueOf(cursor.getColumnIndex(COLUMN_UPDATE_AT))};
                    String readAt = cursor.getString(cursor.getColumnIndex(COLUMN_READ_AT));
                    Integer isVoucher = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_VOUCHER));
                    Integer isUse = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_USE));

                    messages.add(new Message(id,name,description,image,codeVoucher,latitude,longitude,startDate,endDate,pushTo,createdAt,updateAt,readAt,isVoucher,isUse));
                }
            }
            return messages;
        }finally {
            cursor.close();
        }
    }

    public void updateVoucher(String codeVoucher,Integer messageId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE_VOUCHER, codeVoucher);
        db.update(TABLE_VOUCHER, values, "id = "+messageId,null );
    }

    public void createMesssageRead(Message message){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_MESSAGE_READ, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, message.getId());
        values.put(COLUMN_MESSAGE_ID, message.getId());
        values.put(COLUMN_IS_VOUCHER, message.getIsVoucher());
        values.put(COLUMN_IS_READ, 1);

        db.insert(TABLE_MESSAGE_READ, null, values);
        db.close();
    }

    public void updateMesssageRead(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_MESSAGE_READ, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);

        db.update(TABLE_MESSAGE_READ, values, COLUMN_MESSAGE_ID+" = "+id,null );
        db.close();
    }

    public boolean getMessagIsRead(Message message){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        cursor = db.rawQuery("SELECT * FROM " + TABLE_MESSAGE_READ+" WHERE "+ COLUMN_ID+ " = "+message.getId() , null);
        if(cursor.getCount()>0){

            Integer isread = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_READ));
            if (isread == 1){
                db.close();
                return true;
            }else{
                db.close();
                return false;
            }
        }else{
            db.close();
            return false;
        }
    }

}
