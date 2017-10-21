package com.example.mgigena.databaseapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import java.util.ArrayList;

/**
 * This class is used as a service to access the database.
 */
class DBHelper extends SQLiteOpenHelper {
    //Here are the version and the name of the database.
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "MyDBName.db";
    //This the create table schema.
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.CONTACTS_COLUMN_NAME + " TEXT," +
                    FeedReaderContract.FeedEntry.CONTACTS_COLUMN_PHONE + " TEXT," +
                    FeedReaderContract.FeedEntry.CONTACTS_COLUMN_EMAIL + " TEXT," +
                    FeedReaderContract.FeedEntry.CONTACTS_COLUMN_STREET + " TEXT," +
                    FeedReaderContract.FeedEntry.CONTACTS_COLUMN_CITY + " TEXT)";
    //Here is the drop table sentence.
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    /**
     * Package level constructor.
     */
    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This method is called when the DBHelper class is created.
     *
     * @param db SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * This method, is used when the database is upgraded.
     *
     * @param db         SQLiteDatabase
     * @param oldVersion int
     * @param newVersion int
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * This method is used when the database is downgraded.
     *
     * @param db         SQLiteDatabase
     * @param oldVersion int
     * @param newVersion int
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * This method is called to insert a contact to the db.
     *
     * @param name   String
     * @param phone  String
     * @param email  String
     * @param street String
     * @param place  String
     * @return success
     */
    boolean insertContact(String name, String phone, String email, String street,
            String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_NAME, name);
        contentValues.put(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_PHONE, phone);
        contentValues.put(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_EMAIL, email);
        contentValues.put(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_STREET, street);
        contentValues.put(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_CITY, place);
        long id = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, contentValues);
        return (id > 0);
    }

    /**
     * This method creates a query that retrieves a single contact using a given _id.
     *
     * @param id int
     * @return cursor
     */
    Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        //The second parameter is related to the select clause
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.CONTACTS_COLUMN_NAME,
                FeedReaderContract.FeedEntry.CONTACTS_COLUMN_PHONE,
                FeedReaderContract.FeedEntry.CONTACTS_COLUMN_EMAIL,
                FeedReaderContract.FeedEntry.CONTACTS_COLUMN_STREET,
                FeedReaderContract.FeedEntry.CONTACTS_COLUMN_CITY
        };
        //The third and fourth parameters are related to the where clause
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = new String[]{Integer.toString(id)};
        //The seventh parameter is related to the order by clause
        String sortOrder = FeedReaderContract.FeedEntry.CONTACTS_COLUMN_NAME + " DESC";

        return db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,//First parameter is the table name
                projection,
                selection,
                selectionArgs,
                null,//The fifth parameter is related to group by clause (null in this case)
                null,//The sixth parameter is related to filter by clause (null in this case)
                sortOrder
        );
    }

    /**
     * This method deletes a contact from the database.
     *
     * @param id Integer
     * @return deletedId
     */
    Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        return db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * This method updates a contact from the database.
     *
     * @param id     Integer
     * @param name   String
     * @param phone  String
     * @param email  String
     * @param street String
     * @param place  String
     * @return success
     */
    boolean updateContact(Integer id, String name, String phone, String email, String street,
            String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //The ContentValues object is used to bundle the data
        contentValues.put(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_NAME, name);
        contentValues.put(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_PHONE, phone);
        contentValues.put(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_EMAIL, email);
        contentValues.put(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_STREET, street);
        contentValues.put(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_CITY, place);
        //The selection parameters single out the row to be modified.
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        //The update is executed.
        db.update(FeedReaderContract.FeedEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        return true;
    }

    /**
     * This method goes over the Contacts table, and retrieves the list of first names of each
     * contact.
     *
     * @return listOfContactNames
     */
    ArrayList<String> getAllContacts() {
        ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedEntry.TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(
                    res.getColumnIndex(FeedReaderContract.FeedEntry.CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
}
