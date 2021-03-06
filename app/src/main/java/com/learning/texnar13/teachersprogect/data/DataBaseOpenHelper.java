package com.learning.texnar13.teachersprogect.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataBaseOpenHelper extends SQLiteOpenHelper {
    private final boolean IS_DEBUG = true;

    private static final int DB_VERSION = 13;

    /*
        final TextView textView = (TextView) findViewById(R.id.myTextView);
        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseOpenHelper db = new DataBaseOpenHelper(getApplicationContext());
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put("time_column", "2017-12-12 12:12:12");//YYYY-MM-DD HH:MM:SS// time_column TIMESTRING);
                sqLiteDatabase.insert("time", null, contentValues);

                sqLiteDatabase.close();
                db.close();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseOpenHelper db = new DataBaseOpenHelper(getApplicationContext());
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

                if(IS_DEBUG)Log.i("App","-----------");//<= >= = <>
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM time WHERE time_column <>\"2017-12-12 12:12:12\"", null);
                //select * from t where dt_system between strftime('%Y.%m.%d %H:%M:%S', 'now', '-1 day') and ---- SELECT date FROM test WHERE
                textView.setText("" + cursor.getCount());//"time_column = '2017-12-12 12:12:12'" -- time_column = strftime('%Y-%m-%dT%H:%M:%S', '2017-12-12T12:12:12')
//getString(cursor.getColumnIndex("time_column"))
                cursor.close();
                sqLiteDatabase.close();
                db.close();
            }
        });

    }
}*/
    public DataBaseOpenHelper(Context context) {
        super(context, SchoolContract.DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db, 0, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");//?????????? ?????? ???????????????? ????????????????
        super.onOpen(db);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(IS_DEBUG)Log.i("DBOpenHelper", "updateDatabase old=" + oldVersion + " new=" + newVersion);

        if (oldVersion < 1) {//???? ???????????????????? ???????? ??????????, ???????? ?????????????????? ????????????

            //???????????? ????
            db.execSQL("PRAGMA foreign_keys = OFF;");
            db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS + ";");
            db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableCabinets.NAME_TABLE_CABINETS + ";");
            db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableDesks.NAME_TABLE_DESKS + ";");
            db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TablePlaces.NAME_TABLE_PLACES + ";");
            db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableClasses.NAME_TABLE_CLASSES + ";");
            db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableLearners.NAME_TABLE_LEARNERS + ";");
            db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableLearnersOnPlaces.NAME_TABLE_LEARNERS_ON_PLACES + ";");
            db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES + ";");
            db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS + ";");
            db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableSubjectAndTimeCabinetAttitude.NAME_TABLE_SUBJECT_AND_TIME_CABINET_ATTITUDE + ";");
            db.execSQL("PRAGMA foreign_keys = ON;");

            //??????????????????
            String sql = "CREATE TABLE " + SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS + "( " + SchoolContract.TableSettingsData.KEY_SETTINGS_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SchoolContract.TableSettingsData.COLUMN_PROFILE_NAME + " VARCHAR, " +
                    SchoolContract.TableSettingsData.COLUMN_LOCALE + " TEXT DEFAULT '" + SchoolContract.TableSettingsData.COLUMN_LOCALE_DEFAULT_CODE + "', " +
                    SchoolContract.TableSettingsData.COLUMN_MAX_ANSWER + " INTEGER DEFAULT 5, " +
                    SchoolContract.TableSettingsData.COLUMN_TIME + " TEXT DEFAULT " +
                    "'{\"" + SchoolContract.TableSettingsData.COLUMN_TIME_BEGIN_HOUR_NAME + "\":" +
                    //???????? ????????????
                    "[\"8\",\"9\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\",\"16\"]," +
                    //???????????? ????????????
                    "\"" + SchoolContract.TableSettingsData.COLUMN_TIME_BEGIN_MINUTE_NAME + "\":" +
                    "[\"30\",\"30\",\"30\",\"30\",\"25\",\"30\",\"25\",\"20\",\"6\"]," +
                    //???????? ??????????
                    "\"" + SchoolContract.TableSettingsData.COLUMN_TIME_END_HOUR_NAME + "\":" +
                    "[\"9\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\",\"16\",\"23\"],"+
                    //???????????? ??????????
                    "\"" + SchoolContract.TableSettingsData.COLUMN_TIME_END_MINUTE_NAME + "\":" +
                    "[\"15\",\"15\",\"15\",\"15\",\"10\",\"15\",\"10\",\"5\",\"59\"]" +
                    "}'," +
                    //???????? ????????????
//                    "[\"" + STANDARD_LESSONS_TIMES[0][0][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[1][0][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[2][0][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[3][0][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[4][0][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[5][0][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[6][0][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[7][0][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[8][0][0] + "\"]," +
//                    //???????????? ????????????
//                    "\"" + SchoolContract.TableSettingsData.COLUMN_TIME_BEGIN_MINUTE_NAME + "\":" +
//                    "[\"" + STANDARD_LESSONS_TIMES[0][0][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[1][0][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[2][0][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[3][0][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[4][0][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[5][0][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[6][0][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[7][0][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[8][0][1] + "\"]," +
//                    //???????? ??????????
//                    "\"" + SchoolContract.TableSettingsData.COLUMN_TIME_END_HOUR_NAME + "\":" +
//                    "[\"" + STANDARD_LESSONS_TIMES[0][1][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[1][1][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[2][1][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[3][1][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[4][1][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[5][1][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[6][1][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[7][1][0] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[8][1][0] + "\"]," +
//                    //???????????? ??????????
//                    "\"" + SchoolContract.TableSettingsData.COLUMN_TIME_END_MINUTE_NAME + "\":" +
//                    "[\"" + STANDARD_LESSONS_TIMES[0][1][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[1][1][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[2][1][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[3][1][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[4][1][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[5][1][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[6][1][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[7][1][1] + "\"," +
//                    "\"" + STANDARD_LESSONS_TIMES[8][1][1] + "\"]" +
//                    "}'," +
                    SchoolContract.TableSettingsData.COLUMN_INTERFACE_SIZE + " INTEGER ); ";
            db.execSQL(sql);

            //??????????????
            sql = "CREATE TABLE " + SchoolContract.TableCabinets.NAME_TABLE_CABINETS + "( " + SchoolContract.TableCabinets.KEY_CABINET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SchoolContract.TableCabinets.COLUMN_NAME + " VARCHAR ); ";
            db.execSQL(sql);
            //??????????
            sql = "CREATE TABLE " + SchoolContract.TableDesks.NAME_TABLE_DESKS + "( " + SchoolContract.TableDesks.KEY_DESK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SchoolContract.TableDesks.COLUMN_X + " INTEGER, " +
                    SchoolContract.TableDesks.COLUMN_Y + " INTEGER, " +
                    SchoolContract.TableDesks.COLUMN_NUMBER_OF_PLACES + " INTEGER, " +
                    SchoolContract.TableDesks.KEY_CABINET_ID + " INTEGER, " +
                    "FOREIGN KEY(" + SchoolContract.TableDesks.KEY_CABINET_ID + ") REFERENCES " + SchoolContract.TableCabinets.NAME_TABLE_CABINETS + "(" + SchoolContract.TableCabinets.KEY_CABINET_ID + ") ON DELETE CASCADE ); ";
            db.execSQL(sql);
            //??????????
            sql = "CREATE TABLE " + SchoolContract.TablePlaces.NAME_TABLE_PLACES + " ( " + SchoolContract.TablePlaces.KEY_PLACE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SchoolContract.TablePlaces.KEY_DESK_ID + " INTEGER, " +
                    SchoolContract.TablePlaces.COLUMN_ORDINAL + " INTEGER, " +
                    "FOREIGN KEY(" + SchoolContract.TablePlaces.KEY_DESK_ID + ") REFERENCES " + SchoolContract.TableDesks.NAME_TABLE_DESKS + " (" + SchoolContract.TableDesks.KEY_DESK_ID + ") ON DELETE CASCADE ); ";
            db.execSQL(sql);
            //??????????
            sql = "CREATE TABLE " + SchoolContract.TableClasses.NAME_TABLE_CLASSES + " ( " + SchoolContract.TableClasses.KEY_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SchoolContract.TableClasses.COLUMN_CLASS_NAME + " VARCHAR ); ";
            db.execSQL(sql);
            //????????????
            sql = "CREATE TABLE " + SchoolContract.TableLearners.NAME_TABLE_LEARNERS + " ( " + SchoolContract.TableLearners.KEY_LEARNER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SchoolContract.TableLearners.COLUMN_FIRST_NAME + " VARCHAR, " +
                    SchoolContract.TableLearners.COLUMN_SECOND_NAME + " VARCHAR, " +
                    SchoolContract.TableLearners.KEY_CLASS_ID + " INTEGER, " +
                    "FOREIGN KEY(" + SchoolContract.TableLearners.KEY_CLASS_ID + ") REFERENCES " + SchoolContract.TableClasses.NAME_TABLE_CLASSES + " (" + SchoolContract.TableClasses.KEY_CLASS_ID + ") ON DELETE CASCADE ); ";
            db.execSQL(sql);
            //????????????-??????????
            sql = "CREATE TABLE " + SchoolContract.TableLearnersOnPlaces.NAME_TABLE_LEARNERS_ON_PLACES + " ( " + SchoolContract.TableLearnersOnPlaces.KEY_ATTITUDES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SchoolContract.TableLearnersOnPlaces.KEY_LEARNER_ID + " INTEGER, " +
                    SchoolContract.TableLearnersOnPlaces.KEY_PLACE_ID + " INTEGER, " +
                    "FOREIGN KEY(" + SchoolContract.TableLearnersOnPlaces.KEY_LEARNER_ID + ") REFERENCES " + SchoolContract.TableLearners.NAME_TABLE_LEARNERS + " (" + SchoolContract.TableLearners.KEY_LEARNER_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY(" + SchoolContract.TableLearnersOnPlaces.KEY_PLACE_ID + ") REFERENCES " + SchoolContract.TablePlaces.NAME_TABLE_PLACES + " (" + SchoolContract.TablePlaces.KEY_PLACE_ID + ") ON DELETE CASCADE ); ";
            db.execSQL(sql);
            //???????????? ????????????????
            sql = "CREATE TABLE " + SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES + " ( " + SchoolContract.TableLearnersGrades.KEY_GRADE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SchoolContract.TableLearnersGrades.KEY_LEARNER_ID + " INTEGER, " +
                    SchoolContract.TableLearnersGrades.COLUMN_GRADE + " INTEGER, " +
                    SchoolContract.TableLearnersGrades.KEY_SUBJECT_ID + " INTEGER, " +
                    SchoolContract.TableLearnersGrades.COLUMN_TIME_STAMP + " TIMESTRING DEFAULT \"0000-00-00 00:00:00\", " +
                    "FOREIGN KEY(" + SchoolContract.TableLearnersGrades.KEY_SUBJECT_ID + ") REFERENCES " + SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS + " (" + SchoolContract.TableSubjects.KEY_SUBJECT_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY(" + SchoolContract.TableLearnersGrades.KEY_LEARNER_ID + ") REFERENCES " + SchoolContract.TableLearners.NAME_TABLE_LEARNERS + " (" + SchoolContract.TableLearners.KEY_LEARNER_ID + ") ON DELETE CASCADE ); ";
            db.execSQL(sql);
            //??????????????
            sql = "CREATE TABLE " + SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS + " ( " + SchoolContract.TableSubjects.KEY_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SchoolContract.TableSubjects.COLUMN_NAME + " VARCHAR, " +
                    SchoolContract.TableSubjects.KEY_CLASS_ID + " INTEGER, " +
                    "FOREIGN KEY(" + SchoolContract.TableSubjects.KEY_CLASS_ID + ") REFERENCES " + SchoolContract.TableClasses.NAME_TABLE_CLASSES + " (" + SchoolContract.TableClasses.KEY_CLASS_ID + ") ON DELETE CASCADE ); ";
            db.execSQL(sql);
            //???????? ?? ?????? ?????????? ????????????????????
            sql = "CREATE TABLE " + SchoolContract.TableSubjectAndTimeCabinetAttitude.NAME_TABLE_SUBJECT_AND_TIME_CABINET_ATTITUDE + " ( " + SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_AND_TIME_CABINET_ATTITUDE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_ID + " INTEGER, " +
                    SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_CABINET_ID + " INTEGER, " +
                    SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + " TIMESTRING, " +
                    SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_END + " TIMESTRING, " +
                    SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_REPEAT + " INTEGER DEFAULT " + SchoolContract.TableSubjectAndTimeCabinetAttitude.CONSTANT_REPEAT_NEVER + ", " +
                    "FOREIGN KEY(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_CABINET_ID + ") REFERENCES " + SchoolContract.TableCabinets.NAME_TABLE_CABINETS + " (" + SchoolContract.TableCabinets.KEY_CABINET_ID + ") ON DELETE CASCADE ," +
                    "FOREIGN KEY(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_ID + ") REFERENCES " + SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS + " (" + SchoolContract.TableSubjects.KEY_SUBJECT_ID + ") ON DELETE CASCADE ); ";
            db.execSQL(sql);
        } else {//?????????? ??@????????????
            //???????????????? ???? ???????????????????? ???????????? ???? ???????????? ??????????????
            if (oldVersion < 5) {//???????? ???????? ???????????? 5 ?? ????????, ???? ?????? ???? ???????????????? ???????? ?????? //?????????????? ???????????? ?????????????? ?????? ???? ????????????????
                db.execSQL("PRAGMA foreign_keys = OFF;");
                db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS + ";");
                db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableCabinets.NAME_TABLE_CABINETS + ";");
                db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableDesks.NAME_TABLE_DESKS + ";");
                db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TablePlaces.NAME_TABLE_PLACES + ";");
                db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableClasses.NAME_TABLE_CLASSES + ";");
                db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableLearners.NAME_TABLE_LEARNERS + ";");
                db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableLearnersOnPlaces.NAME_TABLE_LEARNERS_ON_PLACES + ";");
                db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES + ";");
                db.execSQL("DROP TABLE IF EXISTS lessons;");
                db.execSQL("DROP TABLE IF EXISTS lessonsAnd;");
                db.execSQL("PRAGMA foreign_keys = ON;");


//??????????????????
                String sql = "CREATE TABLE " + SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS + "( " + SchoolContract.TableSettingsData.KEY_SETTINGS_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SchoolContract.TableSettingsData.COLUMN_PROFILE_NAME + " VARCHAR, " +
                        SchoolContract.TableSettingsData.COLUMN_INTERFACE_SIZE + " INTEGER ); ";
                db.execSQL(sql);

//??????????????
                sql = "CREATE TABLE " + SchoolContract.TableCabinets.NAME_TABLE_CABINETS + "( " + SchoolContract.TableCabinets.KEY_CABINET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SchoolContract.TableCabinets.COLUMN_NAME + " VARCHAR ); ";
                db.execSQL(sql);
//??????????
                sql = "CREATE TABLE " + SchoolContract.TableDesks.NAME_TABLE_DESKS + "( " + SchoolContract.TableDesks.KEY_DESK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SchoolContract.TableDesks.COLUMN_X + " INTEGER, " +
                        SchoolContract.TableDesks.COLUMN_Y + " INTEGER, " +
                        SchoolContract.TableDesks.COLUMN_NUMBER_OF_PLACES + " INTEGER, " +
                        SchoolContract.TableDesks.KEY_CABINET_ID + " INTEGER, " +
                        "FOREIGN KEY(" + SchoolContract.TableDesks.KEY_CABINET_ID + ") REFERENCES " + SchoolContract.TableCabinets.NAME_TABLE_CABINETS + "(" + SchoolContract.TableCabinets.KEY_CABINET_ID + ") ON DELETE CASCADE ); ";
                db.execSQL(sql);
//??????????
                sql = "CREATE TABLE " + SchoolContract.TablePlaces.NAME_TABLE_PLACES + " ( " + SchoolContract.TablePlaces.KEY_PLACE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SchoolContract.TablePlaces.KEY_DESK_ID + " INTEGER, " +
                        SchoolContract.TablePlaces.COLUMN_ORDINAL + " INTEGER, " +
                        "FOREIGN KEY(" + SchoolContract.TablePlaces.KEY_DESK_ID + ") REFERENCES " + SchoolContract.TableDesks.NAME_TABLE_DESKS + " (" + SchoolContract.TableDesks.KEY_DESK_ID + ") ON DELETE CASCADE ); ";
                db.execSQL(sql);
//??????????
                sql = "CREATE TABLE " + SchoolContract.TableClasses.NAME_TABLE_CLASSES + " ( " + SchoolContract.TableClasses.KEY_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SchoolContract.TableClasses.COLUMN_CLASS_NAME + " VARCHAR ); ";
                db.execSQL(sql);
//????????????
                sql = "CREATE TABLE " + SchoolContract.TableLearners.NAME_TABLE_LEARNERS + " ( " + SchoolContract.TableLearners.KEY_LEARNER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SchoolContract.TableLearners.COLUMN_FIRST_NAME + " VARCHAR, " +
                        SchoolContract.TableLearners.COLUMN_SECOND_NAME + " VARCHAR, " +
                        SchoolContract.TableLearners.KEY_CLASS_ID + " INTEGER, " +
                        "FOREIGN KEY(" + SchoolContract.TableLearners.KEY_CLASS_ID + ") REFERENCES " + SchoolContract.TableClasses.NAME_TABLE_CLASSES + " (" + SchoolContract.TableClasses.KEY_CLASS_ID + ") ON DELETE CASCADE ); ";
                db.execSQL(sql);
//????????????-??????????
                sql = "CREATE TABLE " + SchoolContract.TableLearnersOnPlaces.NAME_TABLE_LEARNERS_ON_PLACES + " ( " + SchoolContract.TableLearnersOnPlaces.KEY_ATTITUDES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SchoolContract.TableLearnersOnPlaces.KEY_LEARNER_ID + " INTEGER, " +
                        SchoolContract.TableLearnersOnPlaces.KEY_PLACE_ID + " INTEGER, " +
                        "FOREIGN KEY(" + SchoolContract.TableLearnersOnPlaces.KEY_LEARNER_ID + ") REFERENCES " + SchoolContract.TableLearners.NAME_TABLE_LEARNERS + " (" + SchoolContract.TableLearners.KEY_LEARNER_ID + ") ON DELETE CASCADE, " +
                        "FOREIGN KEY(" + SchoolContract.TableLearnersOnPlaces.KEY_PLACE_ID + ") REFERENCES " + SchoolContract.TablePlaces.NAME_TABLE_PLACES + " (" + SchoolContract.TablePlaces.KEY_PLACE_ID + ") ON DELETE CASCADE ); ";
                db.execSQL(sql);
//???????????? ????????????????
                sql = "CREATE TABLE " + SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES + " ( " + SchoolContract.TableLearnersGrades.KEY_GRADE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SchoolContract.TableLearnersGrades.KEY_LEARNER_ID + " INTEGER, " +
                        SchoolContract.TableLearnersGrades.COLUMN_GRADE + " INTEGER, " +
                        "lessonId INTEGER, " +
                        "FOREIGN KEY(" + SchoolContract.TableLearnersGrades.KEY_LEARNER_ID + ") REFERENCES " + SchoolContract.TableLearners.NAME_TABLE_LEARNERS + " (" + SchoolContract.TableLearners.KEY_LEARNER_ID + ") ON DELETE CASCADE ); ";
                db.execSQL(sql);
//??????????
                sql = "CREATE TABLE lessons ( " + SchoolContract.TableSubjects.KEY_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SchoolContract.TableSubjects.COLUMN_NAME + " VARCHAR, " +
                        SchoolContract.TableSubjects.KEY_CLASS_ID + " INTEGER, " +
                        //SchoolContract.TableSubjects.KEY_CABINET_ID + " INTEGER, " +
                        "FOREIGN KEY(" + SchoolContract.TableSubjects.KEY_CLASS_ID + ") REFERENCES " + SchoolContract.TableClasses.NAME_TABLE_CLASSES + " (" + SchoolContract.TableClasses.KEY_CLASS_ID + ") ON DELETE CASCADE ); ";
                //"FOREIGN KEY(" + SchoolContract.TableSubjects.KEY_CABINET_ID + ") REFERENCES " + SchoolContract.TableCabinets.NAME_TABLE_CABINETS + " (" + SchoolContract.TableCabinets.KEY_CABINET_ID + ") ON DELETE CASCADE, " +
                db.execSQL(sql);
//--
//???????? ?? ?????? ?????????? ????????????????????
                sql = "CREATE TABLE lessonsAnd ( " + SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_AND_TIME_CABINET_ATTITUDE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "lessonId INTEGER, " +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_CABINET_ID + " INTEGER, " +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + " INTEGER, " +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_END + " INTEGER, " +
                        "FOREIGN KEY(lessonId) REFERENCES lessons (" + SchoolContract.TableSubjects.KEY_SUBJECT_ID + ") ON DELETE CASCADE ); ";
                db.execSQL(sql);
            }
            if (oldVersion < 6) {
                db.execSQL("ALTER TABLE lessonsAnd ADD COLUMN " + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_REPEAT + " INTEGER DEFAULT " + SchoolContract.TableSubjectAndTimeCabinetAttitude.CONSTANT_REPEAT_NEVER + ";");//?????????????? ?????? ???????????????????? ????????????
            }
            if (oldVersion < 7) {
                db.execSQL("ALTER TABLE " + SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES + " ADD COLUMN " + SchoolContract.TableLearnersGrades.COLUMN_TIME_STAMP + " TEXT DEFAULT '0000-00-00 00:00:00';");//?????????? ???????????????????????? ????????????
            }
            if (oldVersion < 8) {//?????????????????? foreign key
                db.execSQL("PRAGMA foreign_keys = OFF;");
                // --?????? ??????????????????--

                //?????????????????????????????? ???????????? ??????????????
                db.execSQL("ALTER TABLE lessonsAnd RENAME TO lessonsAnd_old;");
                //?????????????? ?????????? ??????????????
                db.execSQL("CREATE TABLE lessonsAnd ( " + SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_AND_TIME_CABINET_ATTITUDE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "lessonId INTEGER, " +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_CABINET_ID + " INTEGER, " +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + " INTEGER, " +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_END + " INTEGER, " +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_REPEAT + " INTEGER DEFAULT " + SchoolContract.TableSubjectAndTimeCabinetAttitude.CONSTANT_REPEAT_NEVER + ", " +
                        "FOREIGN KEY(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_CABINET_ID + ") REFERENCES " + SchoolContract.TableCabinets.NAME_TABLE_CABINETS + " (" + SchoolContract.TableCabinets.KEY_CABINET_ID + ") ON DELETE CASCADE ," +
                        "FOREIGN KEY(lessonId) REFERENCES lessons (" + SchoolContract.TableSubjects.KEY_SUBJECT_ID + ") ON DELETE CASCADE ); "
                );
                //?????????????????? ????????????????
                db.execSQL("INSERT INTO lessonsAnd SELECT * FROM lessonsAnd_old;");
                //?????????????? ???????????? ??????????????
                db.execSQL("DROP TABLE IF EXISTS lessonsAnd_old;");

                // --?????? ????????????--
                db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES + ";");
                String sql = "CREATE TABLE " + SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES + " ( " + SchoolContract.TableLearnersGrades.KEY_GRADE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SchoolContract.TableLearnersGrades.KEY_LEARNER_ID + " INTEGER, " +
                        SchoolContract.TableLearnersGrades.COLUMN_GRADE + " INTEGER, " +
                        SchoolContract.TableLearnersGrades.COLUMN_TIME_STAMP + " TEXT DEFAULT '0000-00-00 00:00:00', " +
                        "lessonId INTEGER, " +
                        "FOREIGN KEY(lessonId) REFERENCES lessons ( _id ) ON DELETE CASCADE, " +
                        "FOREIGN KEY(" + SchoolContract.TableLearnersGrades.KEY_LEARNER_ID + ") REFERENCES " + SchoolContract.TableLearners.NAME_TABLE_LEARNERS + " (" + SchoolContract.TableLearners.KEY_LEARNER_ID + ") ON DELETE CASCADE ); ";
                db.execSQL(sql);
                db.execSQL("PRAGMA foreign_keys = ON");
            }
            if (oldVersion < 10) {//???????????????????????????????? ?????????????? ?? ???????? ?? ?????????? ??????????
                db.execSQL("PRAGMA foreign_keys = OFF;");

                db.execSQL("DROP TABLE IF EXISTS lessonsAnd_old;");//???? ???????????????????? ??????????

                // --?????? ????????????????????????--

                //?????????????? ?????????? ??????????????
                db.execSQL("CREATE TABLE " + SchoolContract.TableSubjectAndTimeCabinetAttitude.NAME_TABLE_SUBJECT_AND_TIME_CABINET_ATTITUDE + " ( " + SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_AND_TIME_CABINET_ATTITUDE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_ID + " INTEGER, " +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_CABINET_ID + " INTEGER, " +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + " INTEGER, " +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_END + " INTEGER, " +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_REPEAT + " INTEGER DEFAULT " + SchoolContract.TableSubjectAndTimeCabinetAttitude.CONSTANT_REPEAT_NEVER + ", " +
                        "FOREIGN KEY(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_CABINET_ID + ") REFERENCES " + SchoolContract.TableCabinets.NAME_TABLE_CABINETS + " (" + SchoolContract.TableCabinets.KEY_CABINET_ID + ") ON DELETE CASCADE ," +
                        "FOREIGN KEY(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_ID + ") REFERENCES " + SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS + " (" + SchoolContract.TableSubjects.KEY_SUBJECT_ID + ") ON DELETE CASCADE ); "
                );
                //?????????????????? ????????????????
                db.execSQL("INSERT INTO " + SchoolContract.TableSubjectAndTimeCabinetAttitude.NAME_TABLE_SUBJECT_AND_TIME_CABINET_ATTITUDE + " SELECT * FROM lessonsAnd;");
                //?????????????? ???????????? ??????????????
                db.execSQL("DROP TABLE IF EXISTS lessonsAnd;");

                // --?????? ??????????????????--

                //?????????????????????????????? ???????????? ??????????????
                db.execSQL("ALTER TABLE lessons RENAME TO " + SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS + ";");

                // --?????? ????????????--

                //?????????????? ???????????? ??????????????
                db.execSQL("DROP TABLE IF EXISTS " + SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES + ";");
                //?????????????? ??????????
                String sql = "CREATE TABLE " + SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES + " ( " + SchoolContract.TableLearnersGrades.KEY_GRADE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SchoolContract.TableLearnersGrades.KEY_LEARNER_ID + " INTEGER, " +
                        SchoolContract.TableLearnersGrades.COLUMN_GRADE + " INTEGER, " +
                        SchoolContract.TableLearnersGrades.KEY_SUBJECT_ID + " INTEGER, " +
                        SchoolContract.TableLearnersGrades.COLUMN_TIME_STAMP + " TEXT DEFAULT '0000-00-00 00:00:00', " +
                        "FOREIGN KEY(" + SchoolContract.TableLearnersGrades.KEY_SUBJECT_ID + ") REFERENCES " + SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS + " (" + SchoolContract.TableSubjects.KEY_SUBJECT_ID + ") ON DELETE CASCADE, " +
                        "FOREIGN KEY(" + SchoolContract.TableLearnersGrades.KEY_LEARNER_ID + ") REFERENCES " + SchoolContract.TableLearners.NAME_TABLE_LEARNERS + " (" + SchoolContract.TableLearners.KEY_LEARNER_ID + ") ON DELETE CASCADE ); ";
                db.execSQL(sql);

                db.execSQL("PRAGMA foreign_keys = ON");
            }
            //?????????????? ?????? ???????????????????????? ????????????
            if (oldVersion < 11) {
                db.execSQL("ALTER TABLE " + SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS + " ADD COLUMN " + SchoolContract.TableSettingsData.COLUMN_MAX_ANSWER + " INTEGER DEFAULT 5;");
            }
            //?????????????? ?????? ???????????????????????? ????????????
            if (oldVersion < 12) {
                db.execSQL("ALTER TABLE " + SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS + " ADD COLUMN " + SchoolContract.TableSettingsData.COLUMN_TIME + " TEXT DEFAULT " +
                        "'{\"" + SchoolContract.TableSettingsData.COLUMN_TIME_BEGIN_HOUR_NAME + "\":" +
                        //???????? ????????????
                        "[\"8\",\"9\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\",\"16\"]," +
                        //???????????? ????????????
                        "\"" + SchoolContract.TableSettingsData.COLUMN_TIME_BEGIN_MINUTE_NAME + "\":" +
                        "[\"30\",\"30\",\"30\",\"30\",\"25\",\"30\",\"25\",\"20\",\"6\"]," +
                        //???????? ??????????
                        "\"" + SchoolContract.TableSettingsData.COLUMN_TIME_END_HOUR_NAME + "\":" +
                        "[\"9\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\",\"16\",\"23\"],"+
                        //???????????? ??????????
                        "\"" + SchoolContract.TableSettingsData.COLUMN_TIME_END_MINUTE_NAME + "\":" +
                        "[\"15\",\"15\",\"15\",\"15\",\"10\",\"15\",\"10\",\"5\",\"59\"]" +
                        "}';");
            }
            //?????????????? ?????? ??????????????????????
            if (oldVersion < 13) {
                db.execSQL("ALTER TABLE " + SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS + " ADD COLUMN " + SchoolContract.TableSettingsData.COLUMN_LOCALE + " TEXT DEFAULT '" + SchoolContract.TableSettingsData.COLUMN_LOCALE_DEFAULT_CODE + "';");
            }
        }
        //db.close();
    }

//----------------------------------------???????????? ??????????????--------------------------------------------

    //??????????????????
    public long createNewSettingsProfileWithId1(String profileName, int interfaceSize) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableSettingsData.KEY_SETTINGS_PROFILE_ID, 1);
        values.put(SchoolContract.TableSettingsData.COLUMN_PROFILE_NAME, profileName);
        values.put(SchoolContract.TableSettingsData.COLUMN_INTERFACE_SIZE, interfaceSize);
        long temp = db.insert(SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS, null, values);//-1 = ???????????? ??????????
        if(IS_DEBUG)Log.i("DBOpenHelper", "createSettingsProfile returnId = " + temp + " profileName= " + profileName + " interfaceSize= " + interfaceSize);
        //db.close();
        return temp;
    }

    public long createNewSettingsProfile(String profileName, int interfaceSize) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableSettingsData.COLUMN_PROFILE_NAME, profileName);
        values.put(SchoolContract.TableSettingsData.COLUMN_INTERFACE_SIZE, interfaceSize);
        long temp = db.insert(SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS, null, values);//-1 = ???????????? ??????????
        if(IS_DEBUG)Log.i("DBOpenHelper", "createSettingsProfile returnId = " + temp + " profileName= " + profileName + " interfaceSize= " + interfaceSize);
        //db.close();
        return temp;
    }

    public int setSettingsProfileParameters(long profileId, String profileName, int interfaceSize) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableSettingsData.COLUMN_PROFILE_NAME, profileName);
        values.put(SchoolContract.TableSettingsData.COLUMN_INTERFACE_SIZE, interfaceSize);
        int temp = db.update(SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS, values, SchoolContract.TableSettingsData.KEY_SETTINGS_PROFILE_ID + " = ?", new String[]{"" + profileId});
        if(IS_DEBUG)Log.i("DBOpenHelper", "setSettingsProfileParameters return = " + temp + " profileId= " + profileId + " profileName= " + profileName + " interfaceSize= " + interfaceSize);
        //db.close();
        return temp;
    }

    public int setSettingsLocale(long profileId, String locale) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableSettingsData.COLUMN_LOCALE, locale);
        int temp = db.update(SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS, values, SchoolContract.TableSettingsData.KEY_SETTINGS_PROFILE_ID + " = ?", new String[]{"" + profileId});
        if(IS_DEBUG)Log.i("DBOpenHelper", "setSettingsProfileParameters return = " + temp + " profileId= " + profileId + " locale= " + locale);
        return temp;
    }

    public String getSettingsLocale(long profileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS, null, SchoolContract.TableSettingsData.KEY_SETTINGS_PROFILE_ID + " = ?", new String[]{"" + profileId}, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            return SchoolContract.TableSettingsData.COLUMN_LOCALE_DEFAULT_CODE;
        }
        cursor.moveToFirst();
        String answer = cursor.getString(cursor.getColumnIndex(SchoolContract.TableSettingsData.COLUMN_LOCALE));
        cursor.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "setSettingsProfileParameters return = " + answer + " profileId= " + profileId);
        return answer;
    }

    public int setSettingsMaxGrade(long profileId, int maxGrade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableSettingsData.COLUMN_MAX_ANSWER, maxGrade);
        int temp = db.update(SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS, values, SchoolContract.TableSettingsData.KEY_SETTINGS_PROFILE_ID + " = ?", new String[]{"" + profileId});
        if(IS_DEBUG)Log.i("DBOpenHelper", "setSettingsProfileParameters return = " + temp + " profileId= " + profileId + " maxGrade= " + maxGrade);
        //db.close();
        return temp;
    }

    public int getSettingsMaxGrade(long profileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS, null, SchoolContract.TableSettingsData.KEY_SETTINGS_PROFILE_ID + " = ?", new String[]{"" + profileId}, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            return -1;
        }
        cursor.moveToFirst();
        int answer = cursor.getInt(cursor.getColumnIndex(SchoolContract.TableSettingsData.COLUMN_MAX_ANSWER));
        cursor.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "setSettingsProfileParameters return = " + answer + " profileId= " + profileId);
        //db.close();
        return answer;
    }

    //???????????????????? ???????????????????? ?????????? (?????????????????? ???? ?????? ?????????? ??????????)
    public void setNewTimeForLessons(int[][] lastTime, int[][] newTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat timeDateFormat = new SimpleDateFormat("HH:mm:ss");
        if(IS_DEBUG)Log.i("DBOpenHelper", "setNewTimeForLessons lastTime= " + Arrays.toString(lastTime)+"; newTime= "+Arrays.toString(newTime));
        for (int i = 0; i < lastTime.length; i++) {
            //???????????????????? ?????????? ?? ????????????????????
            GregorianCalendar lastStart = new GregorianCalendar(
                    0, 0, 0, lastTime[i][0], lastTime[i][1]
            );
            GregorianCalendar lastEnd = new GregorianCalendar(
                    0, 0, 0, lastTime[i][2], lastTime[i][3]
            );
            //?????????? ?????????? ?? ????????????????????
            GregorianCalendar newStart = new GregorianCalendar(
                    0, 0, 0, newTime[i][0], newTime[i][1]
            );
            GregorianCalendar newEnd = new GregorianCalendar(
                    0, 0, 0, newTime[i][2], newTime[i][3]
            );

            //???????????? ?????? ?? ?????????? ???????? '00:00:00'
            String lastStartText = timeDateFormat.format(lastStart.getTime());
            String lastEndText = timeDateFormat.format(lastEnd.getTime());
            String newStartText = timeDateFormat.format(newStart.getTime());
            String newEndText = timeDateFormat.format(newEnd.getTime());

            db.execSQL("UPDATE " + SchoolContract.TableSubjectAndTimeCabinetAttitude.NAME_TABLE_SUBJECT_AND_TIME_CABINET_ATTITUDE + " " +
                    "SET " + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + " = ((date(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + "))||(\" " + newStartText + "\"))," +
                    " " + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_END + " = ((date(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_END + "))||(\" " + newEndText + "\"))" +
                    " WHERE (time(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + ") == \"" + lastStartText + "\" " +
                    "AND time(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_END + ") == \"" + lastEndText + "\");"
            );

        }
    }

    //???????????????????? ???????????? (?????????????????? ???? ?????? ?????????? ??????????)
    public void setNewTimeForGrades(int[][] lastTime, int[][] newTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat timeDateFormat = new SimpleDateFormat("HH:mm:ss");

        for (int i = 0; i < lastTime.length; i++) {
            //???????????????????? ?????????? ?? ????????????????????
            GregorianCalendar lastStart = new GregorianCalendar(
                    0, 0, 0, lastTime[i][0], lastTime[i][1]
            );
            //?????????? ?????????? ?? ????????????????????
            GregorianCalendar newStart = new GregorianCalendar(
                    0, 0, 0, newTime[i][0], newTime[i][1]
            );

            //???????????? ?????? ?? ?????????? ???????? '00:00:00'
            String lastStartText = timeDateFormat.format(lastStart.getTime());
            String newStartText = timeDateFormat.format(newStart.getTime());

            db.execSQL("UPDATE " + SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES + " " +
                    "SET " + SchoolContract.TableLearnersGrades.COLUMN_TIME_STAMP + " = ((date(" + SchoolContract.TableLearnersGrades.COLUMN_TIME_STAMP + "))||(\" " + newStartText + "\"))"+
                    " WHERE (time(" + SchoolContract.TableLearnersGrades.COLUMN_TIME_STAMP + ") == \"" + lastStartText + "\");"
            );

        }
    }


    public int setSettingsTime(long profileId, int[][] time) {//{{hh,mm,hh,mm},{hh,mm,hh,mm},...}
        SQLiteDatabase db = this.getWritableDatabase();

//?????????? ???????????????????????????? ???????????????????? ???????????????????? ???????????????????? ?????????? (?????????????????? ???? ?????? ?????????? ??????????)
        setNewTimeForLessons(getSettingsTime(profileId), time);
//?? ????????????
        setNewTimeForGrades(getSettingsTime(profileId), time);

//???????????????????????? ????????????????????
        //???????????? ?? json ?????? ????????????????
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(
                    "{\"" + SchoolContract.TableSettingsData.COLUMN_TIME_BEGIN_HOUR_NAME + "\":" +
                            //???????? ????????????
                            "[\"" + time[0][0] + "\"," +
                            "\"" + time[1][0] + "\"," +
                            "\"" + time[2][0] + "\"," +
                            "\"" + time[3][0] + "\"," +
                            "\"" + time[4][0] + "\"," +
                            "\"" + time[5][0] + "\"," +
                            "\"" + time[6][0] + "\"," +
                            "\"" + time[7][0] + "\"," +
                            "\"" + time[8][0] + "\"]," +
                            //???????????? ????????????
                            "\"" + SchoolContract.TableSettingsData.COLUMN_TIME_BEGIN_MINUTE_NAME + "\":" +
                            "[\"" + time[0][1] + "\"," +
                            "\"" + time[1][1] + "\"," +
                            "\"" + time[2][1] + "\"," +
                            "\"" + time[3][1] + "\"," +
                            "\"" + time[4][1] + "\"," +
                            "\"" + time[5][1] + "\"," +
                            "\"" + time[6][1] + "\"," +
                            "\"" + time[7][1] + "\"," +
                            "\"" + time[8][1] + "\"]," +
                            //???????? ??????????
                            "\"" + SchoolContract.TableSettingsData.COLUMN_TIME_END_HOUR_NAME + "\":" +
                            "[\"" + time[0][2] + "\"," +
                            "\"" + time[1][2] + "\"," +
                            "\"" + time[2][2] + "\"," +
                            "\"" + time[3][2] + "\"," +
                            "\"" + time[4][2] + "\"," +
                            "\"" + time[5][2] + "\"," +
                            "\"" + time[6][2] + "\"," +
                            "\"" + time[7][2] + "\"," +
                            "\"" + time[8][2] + "\"]," +
                            //???????????? ??????????
                            "\"" + SchoolContract.TableSettingsData.COLUMN_TIME_END_MINUTE_NAME + "\":" +
                            "[\"" + time[0][3] + "\"," +
                            "\"" + time[1][3] + "\"," +
                            "\"" + time[2][3] + "\"," +
                            "\"" + time[3][3] + "\"," +
                            "\"" + time[4][3] + "\"," +
                            "\"" + time[5][3] + "\"," +
                            "\"" + time[6][3] + "\"," +
                            "\"" + time[7][3] + "\"," +
                            "\"" + time[8][3] + "\"]" +
                            "}");
        } catch (JSONException e) {
            if(IS_DEBUG)Log.e("TeachersApp", "setSettingsTime-error-JSONParse", e);
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableSettingsData.COLUMN_TIME, jsonObject.toString());
        int temp = db.update(SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS, values, SchoolContract.TableSettingsData.KEY_SETTINGS_PROFILE_ID + " = ?", new String[]{"" + profileId});

        if(IS_DEBUG)Log.i("DBOpenHelper", "setSettingsTime return = " + temp + " profileId= " + profileId + " time= " + Arrays.toString(time));
        //db.close();
        return temp;
    }

    public int[][] getSettingsTime(long profileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS, null, SchoolContract.TableSettingsData.KEY_SETTINGS_PROFILE_ID + " = ?", new String[]{"" + profileId}, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            return new int[][]{};
        }
        int[][] answer = new int[9][4];
        cursor.moveToFirst();

        JSONObject json;
        JSONArray beginHour;
        JSONArray beginMinute;
        JSONArray endHour;
        JSONArray endMinute;
        try {
            if(IS_DEBUG)Log.e("TeachersApp", "" + Arrays.toString(cursor.getColumnNames()));
            json = new JSONObject(
                    cursor.getString(cursor.getColumnIndex(SchoolContract.TableSettingsData.COLUMN_TIME))
            );

            beginHour = json.getJSONArray(SchoolContract.TableSettingsData.COLUMN_TIME_BEGIN_HOUR_NAME);
            beginMinute = json.getJSONArray(SchoolContract.TableSettingsData.COLUMN_TIME_BEGIN_MINUTE_NAME);
            endHour = json.getJSONArray(SchoolContract.TableSettingsData.COLUMN_TIME_END_HOUR_NAME);
            endMinute = json.getJSONArray(SchoolContract.TableSettingsData.COLUMN_TIME_END_MINUTE_NAME);

            for (int i = 0; i < 9; i++) {
                answer[i][0] = beginHour.getInt(i);
                answer[i][1] = beginMinute.getInt(i);
                answer[i][2] = endHour.getInt(i);
                answer[i][3] = endMinute.getInt(i);
            }
        } catch (JSONException e) {
            if(IS_DEBUG)Log.e("TeachersApp", "getSettingsTime-error-JSONParse", e);
            answer = new int[][]{};
        }

        cursor.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "getSettingsTime return = " + Arrays.toString(answer) + " profileId= " + profileId);
        //db.close();
        return answer;
    }

    public long getInterfaceSizeBySettingsProfileId(long profileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {profileId + ""};
        Cursor cursor = db.query(SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS, null, SchoolContract.TableSettingsData.KEY_SETTINGS_PROFILE_ID + " = ?", selectionArgs, null, null, null);
        long answer;
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            answer = cursor.getLong(cursor.getColumnIndex(SchoolContract.TableSettingsData.COLUMN_INTERFACE_SIZE));
        } else {
            answer = -1;
        }
        if(IS_DEBUG)Log.i("DBOpenHelper", "getInterfaceSizeBySettingsProfileId profileId=" + profileId + " return=" + answer);
        cursor.close();
        //db.close();
        return answer;
    }

    public Cursor getSettingProfileById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {id + ""};
        Cursor cursor = db.query(SchoolContract.TableSettingsData.NAME_TABLE_SETTINGS, null, SchoolContract.TableSettingsData.KEY_SETTINGS_PROFILE_ID + " = ?", selectionArgs, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getInterfaceSizeBySettingsProfileId profileId=" + id + " return=" + cursor);
        return cursor;
    }


    //??????????
    public long createClass(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
//        String sql = "INSERT INTO NAME_TABLE_CLASSES( " + SchoolContract.TableClasses.COLUMN_CLASS_NAME + " ) " +
//                "VALUES ( '" + name + "' );";
//        db.execSQL(sql);
//        db.close();
//        return true;
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableClasses.COLUMN_CLASS_NAME, name);
        long temp = db.insert(SchoolContract.TableClasses.NAME_TABLE_CLASSES, null, values);//-1 = ???????????? ??????????
        if(IS_DEBUG)Log.i("DBOpenHelper", "createClass returnId = " + temp + " name= " + name);
        //db.close();
        return temp;
    }

    public Cursor getClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SchoolContract.TableClasses.NAME_TABLE_CLASSES, null, null, null, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getClasses " + cursor + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }

    public Cursor getClasses(long classId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {classId + ""};
        Cursor cursor = db.query(SchoolContract.TableClasses.NAME_TABLE_CLASSES, null, SchoolContract.TableClasses.KEY_CLASS_ID + " = ?", selectionArgs, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getClasses " + cursor + "id=" + classId + "number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }

    public int setClassesNames(ArrayList<Long> classId, String name) {
        // todo ???????????????????? ??????????????
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentName = new ContentValues();
        contentName.put(SchoolContract.TableClasses.COLUMN_CLASS_NAME, name);
        int answer = 0;
        String stringClassId = "";
        for (int i = 0; i < classId.size(); i++) {
            stringClassId = stringClassId + classId.get(i) + " | ";
            if (db.update(SchoolContract.TableClasses.NAME_TABLE_CLASSES, contentName, SchoolContract.TableClasses.KEY_CLASS_ID + " = ?", new String[]{"" + classId.get(i)}) == 1)
                answer++;
        }
        if(IS_DEBUG)Log.i("DBOpenHelper", "setClassesNames name = " + name + " id= " + stringClassId + " return = " + answer);
        //db.close();
        return answer;
    }

    public int deleteClasses(ArrayList<Long> classesId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int answer = 0;
        String stringClassId = "";
        for (int i = 0; i < classesId.size(); i++) {
            stringClassId = stringClassId + classesId.get(i) + " | ";
            if (db.delete(SchoolContract.TableClasses.NAME_TABLE_CLASSES, SchoolContract.TableClasses.KEY_CLASS_ID + " = ?", new String[]{"" + classesId.get(i)}) == 1)
                answer++;
        }
        if(IS_DEBUG)Log.i("DBOpenHelper", "deleteClasses id= " + stringClassId + " return = " + answer);
        //db.close();
        return answer;
    }


    //????????????
    public long createLearner(String secondName, String name, long class_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableLearners.COLUMN_SECOND_NAME, secondName);
        values.put(SchoolContract.TableLearners.COLUMN_FIRST_NAME, name);
        values.put(SchoolContract.TableLearners.KEY_CLASS_ID, class_id);
        long temp = db.insert(SchoolContract.TableLearners.NAME_TABLE_LEARNERS, null, values);//-1 = ???????????? ??????????
        //db.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "createLearner returnId= " + temp + " secondName= " + secondName + " name= " + name + " class_id= " + class_id);
        return temp;
    }

    public Cursor getLearnersByClassId(long classId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {classId + ""};
        Cursor cursor = db.rawQuery("SELECT * FROM " + SchoolContract.TableLearners.NAME_TABLE_LEARNERS + " WHERE (" + SchoolContract.TableLearners.KEY_CLASS_ID + "= " + classId + ") ORDER BY " + SchoolContract.TableLearners.COLUMN_SECOND_NAME + " ASC, " + SchoolContract.TableLearners.COLUMN_FIRST_NAME + " ASC;", null);
        //?????????? ?? ?????????????????????? ???? ?????????????? ?? ??????????
        //= db.query(SchoolContract.TableLearners.NAME_TABLE_LEARNERS, null, SchoolContract.TableLearners.KEY_CLASS_ID + " = ?", selectionArgs, null, null, SchoolContract.TableLearners.COLUMN_SECOND_NAME);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getLearnersByClassId classId=" + classId + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }

    //SELECT all FROM table1 ORDER BY sex ASC, sal DESC;


    public long getLearnerIdByClassIdAndPlaceId(long classId, long placeId) {//todo ????????????????????????!!!!111 ???? ???????? ????????????????????
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor learnersCursor = this.getLearnersByClassId(classId);
        ArrayList<Long> learnersId = new ArrayList<>(learnersCursor.getCount());//???????????????? ???? ???????????? id ????????????????
        while (learnersCursor.moveToNext()) {
            learnersId.add(learnersCursor.getLong(learnersCursor.getColumnIndex(SchoolContract.TableLearners.KEY_LEARNER_ID)));
        }
        learnersCursor.close();

        long answer = -1;//???????? ???????????????????? ???????????? - ??????????
        Cursor cursor;
        for (int i = 0; i < learnersId.size(); i++) {
            String[] selectionArgs = {learnersId.get(i) + "", placeId + ""};
            cursor = db.query(SchoolContract.TableLearnersOnPlaces.NAME_TABLE_LEARNERS_ON_PLACES, null, SchoolContract.TableLearnersOnPlaces.KEY_LEARNER_ID + " = ? AND " + SchoolContract.TableLearnersOnPlaces.KEY_PLACE_ID + " = ?", selectionArgs, null, null, null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                answer = cursor.getLong(cursor.getColumnIndex(SchoolContract.TableLearnersOnPlaces.KEY_LEARNER_ID));
            }
            cursor.close();
        }
        if(IS_DEBUG)Log.i("DBOpenHelper", "getLearnerIdByClassIdAndPlaceId classId=" + classId + " placeId=" + placeId + " answer=" + answer);
        //db.close();
        return answer;
    }

    public Cursor getLearner(long learnerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {learnerId + ""};
        Cursor cursor = db.query(SchoolContract.TableLearners.NAME_TABLE_LEARNERS, null, SchoolContract.TableLearners.KEY_LEARNER_ID + " = ?", selectionArgs, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getLearner learnerId=" + learnerId + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }

    public ArrayList<Long> getNotPutLearnersIdByCabinetIdAndClassId(long cabinetId, long classId) {
        ArrayList<Long> placesId = new ArrayList<>();
        {//???????????????? id ????????
            ArrayList<Long> desksId = new ArrayList<>();
            {
                Cursor desksCursor = this.getDesksByCabinetId(cabinetId);
                while (desksCursor.moveToNext()) {
                    desksId.add(desksCursor.getLong(desksCursor.getColumnIndex(SchoolContract.TableDesks.KEY_DESK_ID)));
                }
                desksCursor.close();
            }

            for (int i = 0; i < desksId.size(); i++) {
                Cursor placeCursor = this.getPlacesByDeskId(desksId.get(i));
                while (placeCursor.moveToNext()) {
                    placesId.add(placeCursor.getLong(placeCursor.getColumnIndex(SchoolContract.TablePlaces.KEY_PLACE_ID)));
                }
                placeCursor.close();
            }
        }
        ArrayList<Long> learnersId = new ArrayList<>();
        {//???????????????? id
            Cursor learnersCursor = this.getLearnersByClassId(classId);
            while (learnersCursor.moveToNext()) {
                learnersId.add(learnersCursor.getLong(learnersCursor.getColumnIndex(SchoolContract.TableLearners.KEY_LEARNER_ID)));
            }
            learnersCursor.close();
        }
        ArrayList<Long> answer = new ArrayList<>();
        for (int i = 0; i < learnersId.size(); i++) {//?????????????????????? ???? ????????????????
            boolean flag = false;
            for (int j = 0; j < placesId.size(); j++) {//?????????????????????? ?? ???????????????? ???? ????????????
                Cursor cursor = this.getAttitudeByLearnerIdAndPlaceId(learnersId.get(i), placesId.get(j));
                if (cursor.getCount() != 0) {//???????? ???????? ???????????????????? ???????????? - ??????????
                    flag = true;
                }
                cursor.close();
            }
            if (!flag) {//???????? ???????????????????? ??????, ????,
                answer.add(learnersId.get(i));//???????????????????? ???? ???????????????????????? ??????????????
            }
        }
        if(IS_DEBUG)Log.i("DBOpenHelper", "getNotPutLearnersIdByCabinetIdAndClassId cabinetId=" + cabinetId + " classId=" + classId + " return=" + answer);
        return answer;
    }

    public int setLearnerNameAndLastName(ArrayList<Long> learnersId, String name, String lastName) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentName = new ContentValues();
        contentName.put(SchoolContract.TableLearners.COLUMN_FIRST_NAME, name);
        contentName.put(SchoolContract.TableLearners.COLUMN_SECOND_NAME, lastName);
        int answer = 0;
        String stringLearnersId = "";
        for (int i = 0; i < learnersId.size(); i++) {
            stringLearnersId = stringLearnersId + learnersId.get(i) + " | ";
            if (db.update(SchoolContract.TableLearners.NAME_TABLE_LEARNERS, contentName, SchoolContract.TableLearners.KEY_LEARNER_ID + " = ?", new String[]{"" + learnersId.get(i)}) == 1)
                answer++;
        }
        if(IS_DEBUG)Log.i("DBOpenHelper", "setLearnerNameAndLastName name= " + name + " lastName= " + lastName + " id= " + stringLearnersId + " return = " + answer);
        //db.close();
        return answer;
    }

    public int deleteLearners(ArrayList<Long> learnersId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int answer = 0;
        String stringClassId = "";
        for (int i = 0; i < learnersId.size(); i++) {
            stringClassId = stringClassId + learnersId.get(i) + " | ";
            if (db.delete(SchoolContract.TableLearners.NAME_TABLE_LEARNERS, SchoolContract.TableLearners.KEY_LEARNER_ID + " = ?", new String[]{"" + learnersId.get(i)}) == 1)
                answer++;
        }
        if(IS_DEBUG)Log.i("DBOpenHelper", "deleteLearners id= " + stringClassId + " return = " + answer);
        //db.close();
        return answer;
    }


    //??????????????
    public long createCabinet(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableCabinets.COLUMN_NAME, name);
        long temp = db.insert(SchoolContract.TableCabinets.NAME_TABLE_CABINETS, null, values);//-1 = ???????????? ??????????
        //db.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "createCabinet returnId = " + temp + " name= " + name);
        return temp;
    }

    public Cursor getCabinets() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SchoolContract.TableCabinets.NAME_TABLE_CABINETS, null, null, null, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getCabinets " + cursor + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }

    public Cursor getCabinets(long cabinetId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {cabinetId + ""};
        Cursor cursor = db.query(SchoolContract.TableCabinets.NAME_TABLE_CABINETS, null, SchoolContract.TableCabinets.KEY_CABINET_ID + " = ?", selectionArgs, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getCabinets " + cursor + "id=" + cabinetId + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }

    public int setCabinetName(ArrayList<Long> cabinetId, String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentName = new ContentValues();
        contentName.put(SchoolContract.TableCabinets.COLUMN_NAME, name);
        int answer = 0;
        String stringCabinetsId = "";
        for (int i = 0; i < cabinetId.size(); i++) {
            stringCabinetsId = stringCabinetsId + cabinetId.get(i) + " | ";
            if (db.update(SchoolContract.TableCabinets.NAME_TABLE_CABINETS, contentName, SchoolContract.TableCabinets.KEY_CABINET_ID + " = ?", new String[]{"" + cabinetId.get(i)}) == 1)
                answer++;
        }
        if(IS_DEBUG)Log.i("DBOpenHelper", "setCabinetName name= " + name + " id= " + stringCabinetsId + " return = " + answer);
        //db.close();
        return answer;
    }

    public int deleteCabinets(ArrayList<Long> cabinetsId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int answer = 0;
        String stringClassId = "";
        for (int i = 0; i < cabinetsId.size(); i++) {
            stringClassId = stringClassId + cabinetsId.get(i) + " | ";
            if (db.delete(SchoolContract.TableCabinets.NAME_TABLE_CABINETS, SchoolContract.TableCabinets.KEY_CABINET_ID + " = ?", new String[]{"" + cabinetsId.get(i)}) == 1)
                answer++;
        }
        if(IS_DEBUG)Log.i("DBOpenHelper", "deleteCabinets id= " + stringClassId + " return = " + answer);
        //db.close();
        return answer;
    }


    //??????????
    public long createDesk(int numberOfPlaces, int x, int y, long cabinet_id) {//????????????????--- ?????? ???????????????? ?????????? ???????????? ?????????????????????? ??????????, ?? ?????????? ???????????????????? ?????? ?????????? ????????????????? ?????? ?? ?????????????? ???????????? ???????????????? ????????????????, ???????????????? ???????? ?? ?????????? ?? ???????????????????? ???? ?? ????????????.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableDesks.COLUMN_NUMBER_OF_PLACES, numberOfPlaces);
        values.put(SchoolContract.TableDesks.COLUMN_X, x);
        values.put(SchoolContract.TableDesks.COLUMN_Y, y);
        values.put(SchoolContract.TableDesks.KEY_CABINET_ID, cabinet_id);
        long temp = db.insert(SchoolContract.TableDesks.NAME_TABLE_DESKS, null, values);//-1 = ???????????? ??????????
        //db.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "createDesk returnId = " + temp + " numberOfPlaces= " + numberOfPlaces + " x= " + x + " y= " + y + " cabinet_id= " + cabinet_id);
        return temp;
    }

    public long setDeskCoordinates(long deskId, long x, long y) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues content = new ContentValues();
        content.put(SchoolContract.TableDesks.COLUMN_X, x);
        content.put(SchoolContract.TableDesks.COLUMN_Y, y);
        int answer = 0;
        if (db.update(SchoolContract.TableDesks.NAME_TABLE_DESKS, content, SchoolContract.TableDesks.KEY_DESK_ID + " = ?", new String[]{"" + deskId}) == 1)
            answer++;

        if(IS_DEBUG)Log.i("DBOpenHelper", "setDeskCoordinates id= " + deskId + " x= " + x + " y= " + y + " return = " + answer);
        //db.close();
        return answer;
    }

    public Cursor getDesksByCabinetId(long cabinetId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SchoolContract.TableDesks.NAME_TABLE_DESKS, null, SchoolContract.TableDesks.KEY_CABINET_ID + " = ?", new String[]{cabinetId + ""}, null, null, null);//??????????????, ???? ???????????????? ???????????????? ?? ???????????? query
        if(IS_DEBUG)Log.i("DBOpenHelper", "getDesksByCabinetId cabinetId=" + cabinetId + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }

    public int deleteDesk(long deskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int answer = db.delete(SchoolContract.TableDesks.NAME_TABLE_DESKS, SchoolContract.TableDesks.KEY_DESK_ID + " = ?", new String[]{"" + deskId});
        if(IS_DEBUG)Log.i("DBOpenHelper", "deleteDesk id= " + deskId + " return = " + answer);
        //db.close();
        return answer;
    }


    //??????????
    public long createPlace(long deskId, long ordinal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TablePlaces.KEY_DESK_ID, deskId);
        values.put(SchoolContract.TablePlaces.COLUMN_ORDINAL, ordinal);
        long temp = db.insert(SchoolContract.TablePlaces.NAME_TABLE_PLACES, null, values);//-1 = ???????????? ??????????
        //db.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "createPlace returnId = " + temp + " desk_id= " + deskId);
        return temp;
    }

    public Cursor getPlacesByDeskId(long deskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SchoolContract.TablePlaces.NAME_TABLE_PLACES, null, SchoolContract.TablePlaces.KEY_DESK_ID + " =?", new String[]{deskId + ""}, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getPlacesByDeskId deskId=" + deskId + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }


    //????????????-??????????
    public long setLearnerOnPlace(long learnerId, long placeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableLearnersOnPlaces.KEY_LEARNER_ID, learnerId);
        values.put(SchoolContract.TableLearnersOnPlaces.KEY_PLACE_ID, placeId);
        long temp = db.insert(SchoolContract.TableLearnersOnPlaces.NAME_TABLE_LEARNERS_ON_PLACES, null, values);//-1 = ???????????? ??????????
        //db.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "setLearnerOnPlace returnId = " + temp + " learnerId= " + learnerId + " placeId= " + placeId);
        return temp;
    }

    public Cursor getAttitudeByLearnerIdAndPlaceId(Long learnerId, Long placeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SchoolContract.TableLearnersOnPlaces.NAME_TABLE_LEARNERS_ON_PLACES, null, SchoolContract.TableLearnersOnPlaces.KEY_LEARNER_ID + " = ? AND " + SchoolContract.TableLearnersOnPlaces.KEY_PLACE_ID + " = ?", new String[]{Long.toString(learnerId), Long.toString(placeId)}, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getAttitudeByLearnerIdAndPlaceId learnerId=" + learnerId + " placeId=" + placeId + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames())
        );
        return cursor;
    }

    public long deleteAttitudeByLearnerIdAndPlaceId(long learnerId, long placeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int answer = db.delete(SchoolContract.TableLearnersOnPlaces.NAME_TABLE_LEARNERS_ON_PLACES, SchoolContract.TableLearnersOnPlaces.KEY_LEARNER_ID + " = ? and " + SchoolContract.TableLearnersOnPlaces.KEY_PLACE_ID + " = ?", new String[]{Long.toString(learnerId), Long.toString(placeId)});
        if(IS_DEBUG)Log.i("DBOpenHelper", "deleteAttitudeByLearnerIdAndPlaceId learnerId=" + learnerId + " placeId=" + placeId + " return = " + answer);
        //db.close();
        return answer;
    }


    //???????????? ????????????????
    public long createGrade(long learnerId, long grade, long subjectId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableLearnersGrades.KEY_LEARNER_ID, learnerId);
        values.put(SchoolContract.TableLearnersGrades.COLUMN_GRADE, grade);
        values.put(SchoolContract.TableLearnersGrades.KEY_SUBJECT_ID, subjectId);
        values.put(SchoolContract.TableLearnersGrades.COLUMN_TIME_STAMP, date);
        long temp = db.insert(SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES, null, values);//-1 = ???????????? ??????????
        //db.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "createGrade returnId = " + temp + " learnerId= " + learnerId + " grade= " + grade + " subjectId= " + subjectId + " date= " + date);
        return temp;
    }

    public Cursor getGradeById(long gradeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {gradeId + ""};
        Cursor cursor = db.query(SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES, null, SchoolContract.TableLearnersGrades.KEY_GRADE_ID + " = ?", selectionArgs, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getGrade gradeId=" + gradeId + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }

    public Cursor getGradesByLearnerIdSubjectAndTimePeriod(long learnerId, long subjectId, GregorianCalendar startTame, GregorianCalendar endTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        //???????????? ???? ???????????????? ?????????? ???????????????? ?? ????
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//%Y-%m-%d %H:%M:%S
        //?????????????????????? ???????????? ?????????????? ???? ?????????? ?????????????????????? ?????????? startTime ?? endTime
        Cursor cursor = db.query(SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES, null, SchoolContract.TableLearnersGrades.KEY_LEARNER_ID + " = ? AND " + SchoolContract.TableLearnersGrades.KEY_SUBJECT_ID + " = ? AND " + SchoolContract.TableLearnersGrades.COLUMN_TIME_STAMP + " BETWEEN \"" + dateFormat.format(startTame.getTime()) + "\" AND \"" + dateFormat.format(endTime.getTime()) + "\"", new String[]{Long.toString(learnerId), Long.toString(subjectId)}, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "" + SchoolContract.TableLearnersGrades.KEY_LEARNER_ID + " = ? AND " + SchoolContract.TableLearnersGrades.COLUMN_TIME_STAMP + " BETWEEN \"" + dateFormat.format(startTame.getTime()) + "\" AND \"" + dateFormat.format(endTime.getTime()) + "\"" + "getGradesByLearnerIdAndTimePeriod learnerId=" + learnerId + " subjectId=" + subjectId + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }

    public long editGrade(long gradeId, long grade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableLearnersGrades.COLUMN_GRADE, grade);
        long temp = db.update(SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES, values, SchoolContract.TableLearnersGrades.KEY_GRADE_ID + " = ?", new String[]{Long.toString(gradeId)});//-1 = ???????????? ??????????
        //db.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "editGrade returnId = " + temp + " grade= " + grade + " gradeId= " + gradeId);
        return temp;
    }

    public long removeGrade(long gradeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long temp = db.delete(SchoolContract.TableLearnersGrades.NAME_TABLE_LEARNERS_GRADES, SchoolContract.TableLearnersGrades.KEY_GRADE_ID + " = ?", new String[]{Long.toString(gradeId)});//-1 = ???????????? ??????????
        //db.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "removeGrade returnId = " + temp + " gradeId= " + gradeId);
        return temp;
    }


    //??????????(????????????????)
    public long createSubject(String name, long classId//,long cabinetId
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SchoolContract.TableSubjects.COLUMN_NAME, name);
        values.put(SchoolContract.TableSubjects.KEY_CLASS_ID, classId);
        //values.put(SchoolContract.TableSubjects.KEY_CABINET_ID, cabinetId);
        long temp = db.insert(SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS, null, values);//-1 = ???????????? ??????????
        //db.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "createSubject returnId = " + temp + " name= " + name +
                        " classId=" + classId
                //+ " cabinetId=" + cabinetId
        );
        return temp;
    }


    public int setSubjectParameters(long subjectId, String subjectName, long classId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentName = new ContentValues();
        contentName.put(SchoolContract.TableSubjects.COLUMN_NAME, subjectName);
        contentName.put(SchoolContract.TableSubjects.KEY_CLASS_ID, classId);
        //contentName.put(SchoolContract.TableSubjects.KEY_CABINET_ID, cabinetId);
        int answer = db.update(SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS, contentName, SchoolContract.TableSubjects.KEY_SUBJECT_ID + " = ?", new String[]{Long.toString(subjectId)});
        //db.close();
        return answer;
    }

    public Cursor getSubjectById(long subjectId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {subjectId + ""};
        Cursor cursor = db.query(SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS, null, SchoolContract.TableSubjects.KEY_SUBJECT_ID + " = ?", selectionArgs, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getSubjectById " + cursor + " id=" + subjectId + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }

    public Cursor getSubjectsById(ArrayList<Long> subjectsId) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (subjectsId.size() == 0) {
            return db.query(SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS, null, SchoolContract.TableSubjects.KEY_SUBJECT_ID + " = ? ", new String[]{"-1"}, null, null, null);
        }
        StringBuilder selection = new StringBuilder();
        String[] selectionArgs = new String[subjectsId.size()];
        selectionArgs[0] = subjectsId.get(0).toString();
        selection.append(SchoolContract.TableSubjects.KEY_SUBJECT_ID + " = ? ");
        for (int i = 1; i < subjectsId.size(); i++) {
            selection.append("OR " + SchoolContract.TableSubjects.KEY_SUBJECT_ID + " = ? ");

        }
        Cursor cursor = db.query(SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS, null, selection.toString(), selectionArgs, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getSubjectById  id=" + Arrays.toString(selectionArgs) + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }

    public Cursor getSubjectsByClassId(long classId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {classId + ""};
        Cursor cursor = db.query(SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS, null, SchoolContract.TableSubjects.KEY_CLASS_ID + " = ?", selectionArgs, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getSubjectsByClassId " + cursor + " classId=" + classId + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }

    public int deleteSubjects(ArrayList<Long> subjectsId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int answer = 0;
        String stringSubjectsId = "";
        for (int i = 0; i < subjectsId.size(); i++) {
            stringSubjectsId = stringSubjectsId + subjectsId.get(i) + " | ";
            if (db.delete(SchoolContract.TableSubjects.NAME_TABLE_SUBJECTS, SchoolContract.TableSubjects.KEY_SUBJECT_ID + " = ?", new String[]{"" + subjectsId.get(i)}) == 1)
                answer++;
        }
        if(IS_DEBUG)Log.i("DBOpenHelper", "deleteSubjects id= " + stringSubjectsId + " return = " + answer);
        //db.close();
        return answer;
    }


    //???????? ?? ?????? ?????????? ????????????????????
    public long setLessonTimeAndCabinet(long subjectId, long cabinetId, Date startTime, Date endTime, long repeatPeriod) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SchoolContract.DECODING_TIMES_STRING);
        //date ?? database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_ID, subjectId);
        contentValues.put(SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_CABINET_ID, cabinetId);
        contentValues.put(SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN, simpleDateFormat.format(startTime));
        contentValues.put(SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_END, simpleDateFormat.format(endTime));
        contentValues.put(SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_REPEAT, repeatPeriod);
        long answer = db.insert(SchoolContract.TableSubjectAndTimeCabinetAttitude.NAME_TABLE_SUBJECT_AND_TIME_CABINET_ATTITUDE, null, contentValues);
        if(IS_DEBUG)Log.i("DBOpenHelper", "setLessonTime subjectId= " + subjectId + " cabinetId= " + cabinetId + " startTime= " + simpleDateFormat.format(startTime) + " endTime= " + simpleDateFormat.format(endTime) + " repeatPeriod= " + repeatPeriod + " return = " + answer);
        //db.close();
        return answer;
    }

    public int editLessonTimeAndCabinet(long attitudeId, long subjectId, long cabinetId, Date startTime, Date endTime, long repeatPeriod) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SchoolContract.DECODING_TIMES_STRING);
        //date ?? database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_ID, subjectId);
        contentValues.put(SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_CABINET_ID, cabinetId);
        contentValues.put(SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN, simpleDateFormat.format(startTime));
        contentValues.put(SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_END, simpleDateFormat.format(endTime));
        contentValues.put(SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_REPEAT, repeatPeriod);
        int answer = db.update(SchoolContract.TableSubjectAndTimeCabinetAttitude.NAME_TABLE_SUBJECT_AND_TIME_CABINET_ATTITUDE, contentValues, SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_AND_TIME_CABINET_ATTITUDE_ID + " = ?", new String[]{Long.toString(attitudeId)});
        //db.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "editLessonTimeAndCabinet attitudeId= " + attitudeId + " subjectId= " + subjectId + " cabinetId= " + cabinetId + " startTime= " + simpleDateFormat.format(startTime) + " endTime= " + simpleDateFormat.format(endTime) + " return = " + answer);
        return answer;
    }

    public Cursor getSubjectAndTimeCabinetAttitudeById(long attitudeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {attitudeId + ""};
        Cursor cursor = db.query(SchoolContract.TableSubjectAndTimeCabinetAttitude.NAME_TABLE_SUBJECT_AND_TIME_CABINET_ATTITUDE, null, SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_AND_TIME_CABINET_ATTITUDE_ID + " = ?", selectionArgs, null, null, null);
        if(IS_DEBUG)Log.i("DBOpenHelper", "getSubjectAndTimeCabinetAttitudeById " + " id=" + attitudeId + " number=" + cursor.getCount() + " content=" + Arrays.toString(cursor.getColumnNames()));
        return cursor;
    }

    public ArrayList<Long> getSubjectAndTimeCabinetAttitudesIdByTimePeriod(GregorianCalendar periodStart, GregorianCalendar periodEnd) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SchoolContract.DECODING_TIMES_STRING);
        SimpleDateFormat timeDateFormat = new SimpleDateFormat("HH:mm:ss");
        //?????????????????????? ???????????? ???????? ???????????? ????????????
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + SchoolContract.TableSubjectAndTimeCabinetAttitude.NAME_TABLE_SUBJECT_AND_TIME_CABINET_ATTITUDE +
                        " WHERE ((" + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + " >= \"" + simpleDateFormat.format(periodStart.getTime()) + "\" AND " + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + " <= \"" + simpleDateFormat.format(periodEnd.getTime()) + "\") OR ((" +
                        // strftime(\"%w-%H:%M:%S\","++")
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_REPEAT + " = " + SchoolContract.TableSubjectAndTimeCabinetAttitude.CONSTANT_REPEAT_DAILY + ") AND (time(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + ") >= \"" + timeDateFormat.format(periodStart.getTime()) + "\") AND (time(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + ") <= \"" + timeDateFormat.format(periodEnd.getTime()) + "\")) OR ((" +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_REPEAT + " = " + SchoolContract.TableSubjectAndTimeCabinetAttitude.CONSTANT_REPEAT_WEEKLY + ") AND (strftime(\"%w\"," + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + ") == \"" + (periodStart.get(Calendar.DAY_OF_WEEK) - 1) + "\") AND (time(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + ") >= \"" + timeDateFormat.format(periodStart.getTime()) + "\") AND (strftime(\"%w\", " + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + ") == \"" + (periodEnd.get(Calendar.DAY_OF_WEEK) - 1) + "\") AND (time( " + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + ") <= \"" + timeDateFormat.format(periodEnd.getTime()) + "\")))"
                , null);

        ArrayList<Long> answer = new ArrayList<>();
        StringBuilder stringSubjectAndTimeCabinetAttitudesId = new StringBuilder();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_AND_TIME_CABINET_ATTITUDE_ID));
            stringSubjectAndTimeCabinetAttitudesId.append(id);
            stringSubjectAndTimeCabinetAttitudesId.append(" | ");
            answer.add(id);
        }
        cursor.close();
        if(IS_DEBUG)Log.i("DBOpenHelper", "getSubjectAndTimeCabinetAttitudesIdByTimePeriod periodStart=" + periodStart.getTime().getTime() + " periodEnd=" + periodEnd.getTime().getTime() + " answer=" + stringSubjectAndTimeCabinetAttitudesId);
        //db.close();
        return answer;
    }

    public long getSubjectAndTimeCabinetAttitudeIdByTime(Calendar time) {//???????????????? ???? ???????????????????? ?????????? ?? ??????????-???????? ???? ????????????
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SchoolContract.DECODING_TIMES_STRING);
        SimpleDateFormat timeDateFormat = new SimpleDateFormat("HH:mm:ss");

        SQLiteDatabase db = this.getReadableDatabase();
//
        Cursor cursor = db.rawQuery("SELECT * FROM " + SchoolContract.TableSubjectAndTimeCabinetAttitude.NAME_TABLE_SUBJECT_AND_TIME_CABINET_ATTITUDE +
                        " WHERE (((" + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + " <= \"" + simpleDateFormat.format(time.getTime()) + "\") AND (" +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_END + " >= \"" + simpleDateFormat.format(time.getTime()) + "\")) OR ((" +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_REPEAT + " = " + SchoolContract.TableSubjectAndTimeCabinetAttitude.CONSTANT_REPEAT_DAILY + ") AND (time(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + ") <= \"" + timeDateFormat.format(time.getTime()) + "\") AND (time(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_END + ")>= \"" + timeDateFormat.format(time.getTime()) + "\")) OR ((" +
                        SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_REPEAT + " = " + SchoolContract.TableSubjectAndTimeCabinetAttitude.CONSTANT_REPEAT_WEEKLY + ") AND (strftime(\"%w\"," + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + ") == \"" + (time.get(Calendar.DAY_OF_WEEK) - 1) + "\") AND (time(" + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_BEGIN + ") <= \"" + timeDateFormat.format(time.getTime()) + "\") AND (strftime(\"%w\", " + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_END + ") == \"" + (time.get(Calendar.DAY_OF_WEEK) - 1) + "\") AND (time( " + SchoolContract.TableSubjectAndTimeCabinetAttitude.COLUMN_DATE_END + ") >= \"" + timeDateFormat.format(time.getTime()) + "\")))"
                , null);
        long answer;
        if(IS_DEBUG)Log.i("TeachersApp", "" + cursor.getCount());
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            answer = cursor.getLong(cursor.getColumnIndex(SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_AND_TIME_CABINET_ATTITUDE_ID));
            cursor.close();
            if(IS_DEBUG)Log.i("DBOpenHelper", "getSubjectAndTimeCabinetAttitudesIdByTimePeriod time=" + time.getTime().getTime() + " answer=" + answer);
            //db.close();
            return answer;
        } else {
            cursor.close();
            if(IS_DEBUG)Log.i("DBOpenHelper", "getSubjectAndTimeCabinetAttitudesIdByTimePeriod time=" + time.getTime().getTime() + " answer=" + (-1));
            //db.close();
            return -1;
        }
    }

    public int deleteSubjectAndTimeCabinetAttitude(long attitudeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int answer = db.delete(SchoolContract.TableSubjectAndTimeCabinetAttitude.NAME_TABLE_SUBJECT_AND_TIME_CABINET_ATTITUDE, SchoolContract.TableSubjectAndTimeCabinetAttitude.KEY_SUBJECT_AND_TIME_CABINET_ATTITUDE_ID + " = ?", new String[]{"" + attitudeId});
        if(IS_DEBUG)Log.i("DBOpenHelper", "deleteSubjectAndTimeCabinetAttitude attitudeId= " + attitudeId + " return = " + answer);
        //db.close();
        return answer;
    }

    //???????????? ?? ????
    public void restartTable() {//???????????????? ???? ????????????
        onUpgrade(this.getReadableDatabase(), 0, 100);
    }

//    String format(int i) {
//        if (i < 10) {
//            return "0" + i;
//        } else return "" + i;
//    }

}
