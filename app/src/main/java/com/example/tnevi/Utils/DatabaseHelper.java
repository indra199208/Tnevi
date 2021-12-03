package com.example.tnevi.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tnevi.model.BlockModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //database name
    public static final String DATABASE_NAME = "11zon";
    //database version
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "tbl_notes";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        //creating table
        query = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY, blockname TEXT, numberofrows TEXT,numberofseats TEXT, seatprice TEXT)";
        db.execSQL(query);
    }

    //upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add the new note
    public void addNotes(String blockname, String numberofrows, String numberofseats, String seatprice) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("blockname", blockname);
        values.put("numberofrows", numberofrows);
        values.put("numberofseats", numberofseats);
        values.put("seatprice", seatprice);

        //inserting new row
        sqLiteDatabase.insert(TABLE_NAME, null, values);
        //close database connection
        sqLiteDatabase.close();
    }

    //get the all notes
    public ArrayList<BlockModel> getNotes() {
        ArrayList<BlockModel> arrayList = new ArrayList<>();

        // select all query
        String select_query = "SELECT *FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BlockModel blockModel = new BlockModel();
                blockModel.setId(cursor.getString(0));
                blockModel.setBlockname(cursor.getString(1));
                blockModel.setNumberofrows(cursor.getString(2));
                blockModel.setNumberofseats(cursor.getString(3));
                blockModel.setSeatprice(cursor.getString(4));
                arrayList.add(blockModel);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    //delete the note
    public void delete(String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete(TABLE_NAME, "ID=" + ID, null);
        sqLiteDatabase.close();
    }

    //cleat table
    public void clearTable() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME);
        sqLiteDatabase.close();
    }

    //update the note
    public void updateNote(String blockname, String numberofrows, String numberofseats, String seatprice, String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("blockname", blockname);
        values.put("numberofrows", numberofrows);
        values.put("numberofseats", numberofseats);
        values.put("seatprice", seatprice);
        //updating row
        sqLiteDatabase.update(TABLE_NAME, values, "ID=" + ID, null);
        sqLiteDatabase.close();
    }
}