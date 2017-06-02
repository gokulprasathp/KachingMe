package com.wifin.kachingme.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbhelper {

    public static final String DB_NAME = "KaChing.me";

    public static final int DB_VERSION = 4;

    public static final String TABLE_CART = "deels";
    public static final String CREATE_TABLE_CART = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CART
            + " (id integer primary key autoincrement,"
            + "desc text , "
            + "phonenumber text , "
            + "deelid text , "
            + "merchantid text , "
            + "prodname text , "
            + "item text , "
            + "offer text , "
            + "discount text , "
            + "type text , "
            + "bux text , "
            + "photopath text , "
            + "qrpath text ,"
            + "merchantname text ,"
            + "merchantimagepath text , "
            + "companyname text  , "
            + "current_date text  , "
            + "validity text " + ");";
    public static final String TABLE_BUX = "bux";
    public static final String CREATE_TABLE_BUX = "CREATE TABLE IF NOT EXISTS "
            + TABLE_BUX + " (id integer primary key autoincrement,"
            + "phonenumber text , " + "bux text" + ");";
    public static final String TABLE_DONATE = "donate";
    public static final String CREATE_TABLE_DONATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DONATE
            + " (id integer primary key autoincrement,"
            + "date text , "
            + "points text , "
            + "name text , "
            + "buxdonate text , " + "status text" + ");";
    public static final String TABLE_NYM = "nym";
    public static final String CREATE_TABLE_NYM = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NYM + " (id integer primary key autoincrement,"
            + "name text , " + "meaning text , " + "deleted text" + ");";
    public static final String TABLE_SOCIAL = "sociallogin";
    public static final String CREATE_TABLE_SOCIAL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SOCIAL
            + " (id integer primary key autoincrement,"
            + "name text , " + "mail text , " + "photo text" + ");";
    public static final String TABLE_TOTALBUX = "totalbux";
    public static final String CREATE_TABLE_TOTALBUX = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TOTALBUX
            + " (id integer primary key autoincrement,"
            + "reg text , "
            + "contintro text , "
            + "chat text , "
            + "zzle text , "
            + "zzlebuy text , "
            + "nym text , "
            + "whr text , "
            + "shwt text , "
            + "deel text , "
            + "deelred text , " + "wish text , " + "kons text" + ");";
    public static final String TABLE_ZZLE = "zzle";
    public static final String CREATE_TABLE_ZZLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ZZLE
            + " (id integer primary key autoincrement,"
            + "msg text , "
            + "backgrount text , "
            + "speed text , "
            + "fontcolor text , " + "seen text , " + "font text" + ");";
    public static final String TABLE_LED = "led";
    public static final String CREATE_TABLE_LED = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LED + " (id integer primary key autoincrement,"
            + "msg text , " + "shape text , " + "background text , "
            + "speed text , "

            + "date text" + ");";
    public static final String TABLE_WISH = "wish";
    public static final String CREATE_TABLE_WISH = "CREATE TABLE IF NOT EXISTS "
            + TABLE_WISH
            + " (id integer primary key autoincrement,"
            + "name text , "
            + "recipientnane text , "
            + "datetext text , "
            + "occatext text" + ");";
    public static final String TABLE_FREEBIE = "freebie";
    public static final String CREATE_TABLE_FREEBIE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_FREEBIE
            + " (id integer primary key autoincrement,"
            + "phonenumber text , "
            + "countryc text , "
            + "advertiserId text , "
            + "companyLogoPath text , "
            + "firstName text , "
            + "lastName text , "
            + "website text , "
            + "companyName text , "
            + "freebielist text , " + "date text " + ");";
    public static final String TABLE_KONS = "kons";
    public static final String CREATE_TABLE_KONS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_KONS
            + " (id integer primary key autoincrement,"
            + "msg text , "
            + "image blob , "
            + "background text , "
            + "color text" + ");";
    public static final String TABLE_RET = "ret";
    public static final String CREATE_TABLE_RET = "CREATE TABLE IF NOT EXISTS "
            + TABLE_RET + " (id integer primary key autoincrement,"
            + "name text , " + "bux text" + ");";
    public static final String TABLE_UPDATE_STATUS = "updatestatus";
    public static final String CREATE_UPDATE_TABLE_STATUS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_UPDATE_STATUS
            + " (id integer primary key autoincrement,"
            + "status text" + ");";
    public static final String TABLE_PRIMENUMBER = "primetable";
    public static final String CREATE_TABLE_PRIME = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PRIMENUMBER
            + " (id integer primary key autoincrement,"
            + "primno text , " + "secondaryno text" + ");";
    public static final String TABLE_DEFAULT_STATUS = "status";
    public static final String CREATE_TABLE_DEFAULT_STATUS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DEFAULT_STATUS
            + " (id integer primary key autoincrement,"
            + "status text" + ");";
    public static final String TABLE_BUX_COUNTER = "counter";
    public static final String CREATE_TABLE_BUX_COUNTER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_BUX_COUNTER
            + " (id integer primary key autoincrement,"
            + "activity_bux text , "
            + "activity text , "
            + "bux_master_id text , "
            + "earned_bux text"
            + ");";
    public static final String TABLE_NUMBERS = "sp_numbers";
    public static final String CREATE_TABLE_NUMBERS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NUMBERS
            + " (id integer primary key autoincrement,"
            + "primarynum text , " + "secondarynum text" + ");";
    public static final String TABLE_LOCK = "lock";
    public static final String CREATE_TABLE_LOCK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LOCK
            + " (id integer primary key autoincrement,"
            + "jid text , "
            + "jid_name , "
            + "password text  , "
            + "status text" + ");";

    public static Mydatabase mydata;

    public static SQLiteDatabase sqldb;
    private static Mydatabase sInstance;
    Context context;

    public Dbhelper(Context c) {
        context = c;
    }

    public static void deleteDatabse(Context context) {
        context.deleteDatabase(DB_NAME);
    }

    public Dbhelper open() {
        try {
            mydata = new Mydatabase(context, DB_NAME, null, DB_VERSION);
            sqldb = mydata.getWritableDatabase();
        } catch (Exception e) {

        }
        return this;

    }

    public SQLiteDatabase getDatabaseObj() {
        return sqldb;
    }

    public void close() {
        mydata.close();
    }

    public void dumpDatabase() {

        open();

        sqldb.execSQL("delete from " + TABLE_CART);
        sqldb.execSQL("delete from " + TABLE_BUX);
        sqldb.execSQL("delete from " + TABLE_DONATE);
        sqldb.execSQL("delete from " + TABLE_NYM);
        sqldb.execSQL("delete from " + TABLE_FREEBIE);
        sqldb.execSQL("delete from " + TABLE_KONS);
        sqldb.execSQL("delete from " + TABLE_WISH);
        sqldb.execSQL("delete from " + TABLE_SOCIAL);
        sqldb.execSQL("delete from " + TABLE_TOTALBUX);
        sqldb.execSQL("delete from " + TABLE_ZZLE);
        sqldb.execSQL("delete from " + TABLE_LED);
        sqldb.execSQL("delete from " + TABLE_LED);
        sqldb.execSQL("delete from " + TABLE_RET);
        sqldb.execSQL("delete from " + TABLE_UPDATE_STATUS);
        sqldb.execSQL("delete from " + TABLE_PRIMENUMBER);
        sqldb.execSQL("delete from " + TABLE_DEFAULT_STATUS);
        sqldb.execSQL("delete from " + TABLE_BUX_COUNTER);
        sqldb.execSQL("delete from " + TABLE_NUMBERS);
        sqldb.execSQL("delete from " + TABLE_LOCK);

    }

    public static class Mydatabase extends SQLiteOpenHelper {

        public Mydatabase(Context context, String name, CursorFactory factory,
                          int version) {
            super(context, name, factory, version);
        }

        private Mydatabase(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public static synchronized Mydatabase getInstance(Context context) {
            // Use the application context, which will ensure that you
            // don't accidentally leak an Activity's context.
            // See this article for more information: http://bit.ly/6LRzfx
            if (sInstance == null) {
                sInstance = new Mydatabase(context.getApplicationContext());
            }

            return sInstance;
        }

        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(CREATE_TABLE_CART);
            db.execSQL(CREATE_TABLE_BUX);
            db.execSQL(CREATE_TABLE_NYM);
            db.execSQL(CREATE_TABLE_SOCIAL);
            db.execSQL(CREATE_TABLE_DONATE);
            db.execSQL(CREATE_TABLE_TOTALBUX);
            db.execSQL(CREATE_TABLE_ZZLE);
            db.execSQL(CREATE_TABLE_LED);
            db.execSQL(CREATE_TABLE_DEFAULT_STATUS);
            db.execSQL(CREATE_TABLE_WISH);
            db.execSQL(CREATE_TABLE_FREEBIE);
            db.execSQL(CREATE_TABLE_KONS);
            db.execSQL(CREATE_TABLE_RET);
            db.execSQL(CREATE_TABLE_PRIME);
            db.execSQL(CREATE_UPDATE_TABLE_STATUS);
            db.execSQL(CREATE_TABLE_BUX_COUNTER);
            db.execSQL(CREATE_TABLE_NUMBERS);
            db.execSQL(CREATE_TABLE_LOCK);

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("Drop table if exists " + TABLE_CART);
            db.execSQL("Drop table if exists " + TABLE_BUX);
            db.execSQL("Drop table if exists " + TABLE_NYM);
            db.execSQL("Drop table if exists " + TABLE_SOCIAL);
            db.execSQL("Drop table if exists " + TABLE_DONATE);
            db.execSQL("Drop table if exists " + TABLE_TOTALBUX);
            db.execSQL("Drop table if exists " + TABLE_ZZLE);
            db.execSQL("Drop table if exists " + TABLE_LED);
            db.execSQL("Drop table if exists " + TABLE_WISH);
            db.execSQL("Drop table if exists " + TABLE_FREEBIE);
            db.execSQL("Drop table if exists " + TABLE_KONS);
            db.execSQL("Drop table if exists " + TABLE_RET);
            db.execSQL("Drop table if exists " + TABLE_PRIMENUMBER);
            db.execSQL("Drop table if exists " + TABLE_DEFAULT_STATUS);
            db.execSQL("Drop table if exists " + TABLE_UPDATE_STATUS);
            db.execSQL("Drop table if exists " + TABLE_BUX_COUNTER);
            db.execSQL("Drop table if exists " + TABLE_NUMBERS);
            db.execSQL("Drop table if exists " + TABLE_LOCK);

            onCreate(db);
        }
    }


}
