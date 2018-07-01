package Kelas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glory on 28/09/2016.
 */
public class DBAdapter extends SQLiteAssetHelper {

    private static final String DB_NAME                 = "db_quiz_ver4";
    private static final int        DB_VER                  = 1;
    public static final String TABLE_SOAL              = "tb_soal";
    public static final String COL_SOAL_ID             = "id";
    public static final String COL_SOAL_SOAL           = "soal";
    public static final String COL_SOAL_JAWABAN_A      = "jawaban_a";
    public static final String COL_SOAL_JAWABAN_B      = "jawaban_b";
    public static final String COL_SOAL_JAWABAN_C      = "jawaban_c";
    public static final String COL_SOAL_JAWABAN_D      = "jawaban_d";
    public static final String COL_SOAL_JAWABAN_BENAR  = "jawaban_benar";


    public static DBAdapter        dbInstance;
    public static SQLiteDatabase db;



    /**
     * private Constructor, untuk menggunakan kelas ini gunakan getInstance()
     * @param context
     */


    private DBAdapter(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }


    public  static synchronized DBAdapter getInstance(Context context){

        if (dbInstance == null){
            dbInstance = new DBAdapter(context.getApplicationContext());
            db = dbInstance.getReadableDatabase();
        }

        return dbInstance;
    }

    public SQLiteDatabase ambilDB(){
        db = this.getWritableDatabase();
        return db;


    }

   /* public DBAdapter getInstance(Context context){

        if (dbInstance == null){

            dbInstance = new DBAdapter(context);
            db = dbInstance.getReadableDatabase();

        }

        return  dbInstance;
    }*/



    @Override
    public synchronized void close(){

        super.close();
        if (dbInstance!=null){

            dbInstance.close();
        }
    }


//method untuk mengambil semua data soal
    public List<Quiz> getAllSoal(){

        List<Quiz> listSoal = new ArrayList<Quiz>();

        Cursor cursor = db.query(TABLE_SOAL,new String[]{

               COL_SOAL_ID,
                COL_SOAL_SOAL,
                COL_SOAL_JAWABAN_A,
                COL_SOAL_JAWABAN_B,
                COL_SOAL_JAWABAN_C,
                COL_SOAL_JAWABAN_D,
                COL_SOAL_JAWABAN_BENAR
                },null,null,null,null,null);//kenapa ada 5 null ya ?

        if (cursor.moveToFirst()){


            do {
                Quiz quiz = new Quiz();

                quiz.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SOAL_ID)));
                quiz.setSoal(cursor.getString(cursor.getColumnIndexOrThrow(COL_SOAL_SOAL)));
                quiz.setJawaban_a(cursor.getString(cursor.getColumnIndexOrThrow(COL_SOAL_JAWABAN_A)));
                quiz.setJawaban_b(cursor.getString(cursor.getColumnIndexOrThrow(COL_SOAL_JAWABAN_B)));
                quiz.setJawaban_c(cursor.getString(cursor.getColumnIndexOrThrow(COL_SOAL_JAWABAN_C)));
                quiz.setJawaban_d(cursor.getString(cursor.getColumnIndexOrThrow(COL_SOAL_JAWABAN_D)));
                quiz.setJawaban_benar(cursor.getString(cursor.getColumnIndexOrThrow(COL_SOAL_JAWABAN_BENAR)));



            listSoal.add(quiz);
            }while (cursor.moveToNext());
        }

        return listSoal;
    }


}
