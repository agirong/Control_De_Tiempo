package com.example.timingrecord.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static DbHelper instance;
    private static final String DATABASE_NAME="TimingRecord";
    private static final int DATABASE_VERSION=1;

    public static synchronized DbHelper getInstance(Context context){
        if(instance==null)
            instance=new DbHelper(context.getApplicationContext());
        return instance;
    }

    private DbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DataBaseManager.timeTable);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //habilitar las llaves foraneas
        if(!db.isReadOnly())
            db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS time");
    }
}
