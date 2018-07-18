package com.login.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper
{
    public static final String DB_NAME="MyDatabse";
    public static final int DB_VERSION=1;
    private static final String TABLE_NAME="StudentDetails";
    private static final String COL1="ID";
    private static final String COL2="Name";
    private static final String COL3="Course";
    private static final String COL4="Number";
    private static final String COL5="Image";
    Context context;
    public MyDatabase(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query="CREATE TABLE "+TABLE_NAME+ "("+COL1+" INTEGER PRIMARY KEY,"+COL2+" TEXT,"+COL3+" TEXT,"+COL4+" INTEGER,"+COL5+" BLOB)";
         db.execSQL(query);
        Toast.makeText(context, "Table Created", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public List<Data> getListData()
    {
        String qry="select * from "+TABLE_NAME;
        List<Data> alldata=new ArrayList<>();
        SQLiteDatabase SQLITE=this.getReadableDatabase();
        Cursor c=SQLITE.rawQuery(qry,null);
        if(c.moveToFirst())
        {
            do {
                String id=c.getString(0);
                String name=c.getString(1);
                String course=c.getString(2);
                String num=c.getString(3);
                byte[] image=c.getBlob(4);
                alldata.add(new Data(id,name,course,num,image));


            }while (c.moveToNext());

        }
        return alldata;
    }

    public void addData(Data data)
    {
        SQLiteDatabase sqlite=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL1, data.getId());
        cv.put(COL2, data.getName());
        cv.put(COL3, data.getCourse());
        cv.put(COL4, data.getNumber());
        cv.put(COL5,data.getImage());
        sqlite.insert(TABLE_NAME, null, cv);
        Toast.makeText(context, "Data Inserted", Toast.LENGTH_SHORT).show();
    }
    public void updateData(Data data)
    {
        SQLiteDatabase sqlite=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL2, data.getName());
        cv.put(COL3, data.getCourse());
        cv.put(COL4, data.getNumber());
        cv.put(COL5, data.getImage());
        String[] args={String.valueOf(data.getId())};
        sqlite.update(TABLE_NAME, cv, COL1+" =?", args);
        Toast.makeText(context, "Data Updated", Toast.LENGTH_SHORT).show();
    }
    public void deleteData(String id)
    {
        SQLiteDatabase sqlite=this.getWritableDatabase();
        String[] args={id};
        sqlite.delete(TABLE_NAME, COL1 +" =? ", args);
        Toast.makeText(context, "Data deleted", Toast.LENGTH_SHORT).show();
    }

}
