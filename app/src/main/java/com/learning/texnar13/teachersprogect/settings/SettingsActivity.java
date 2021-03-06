package com.learning.texnar13.teachersprogect.settings;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.learning.texnar13.teachersprogect.R;
import com.learning.texnar13.teachersprogect.data.DataBaseOpenHelper;
import com.learning.texnar13.teachersprogect.data.SchoolContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

public class SettingsActivity extends AppCompatActivity implements SettingsRemoveInterface, EditTimeDialogFragmentInterface, EditLocaleDialogFragmentInterface {


//-----------------------------------методы диалогов----------------------------------------------

    //удаление настроек
    @Override
    public void settingsRemove() {
        DataBaseOpenHelper dbOpenHelper = new DataBaseOpenHelper(getApplicationContext());
        dbOpenHelper.restartTable();

        dbOpenHelper.createClass("1\"A\"");
        long classId = dbOpenHelper.createClass("2\"A\"");

        long lerner1Id = dbOpenHelper.createLearner("Зинченко", "Сократ", classId);
        long lerner2Id = dbOpenHelper.createLearner("Шумякин", "Феофан", classId);
        long lerner3Id = dbOpenHelper.createLearner("Рябец", "Валентин", classId);
        long lerner4Id = dbOpenHelper.createLearner("Гроша", "Любава", classId);
        long lerner5Id = dbOpenHelper.createLearner("Авдонина", "Алиса", classId);


        long cabinetId = dbOpenHelper.createCabinet("406");

        ArrayList<Long> places = new ArrayList<>();
        {
            long desk1Id = dbOpenHelper.createDesk(2, 160, 200, cabinetId);//1
            places.add(dbOpenHelper.createPlace(desk1Id, 1));
            places.add(dbOpenHelper.createPlace(desk1Id, 2));
        }
        {
            long desk2Id = dbOpenHelper.createDesk(2, 40, 200, cabinetId);//2
            places.add(dbOpenHelper.createPlace(desk2Id, 1));
            places.add(dbOpenHelper.createPlace(desk2Id, 2));
        }
        {
            long desk3Id = dbOpenHelper.createDesk(2, 160, 120, cabinetId);//3
            places.add(dbOpenHelper.createPlace(desk3Id, 1));
            places.add(dbOpenHelper.createPlace(desk3Id, 2));
        }
        {
            long desk4Id = dbOpenHelper.createDesk(2, 40, 120, cabinetId);//4
            places.add(dbOpenHelper.createPlace(desk4Id, 1));
            places.add(dbOpenHelper.createPlace(desk4Id, 2));
        }
        {
            long desk5Id = dbOpenHelper.createDesk(2, 160, 40, cabinetId);//5
            places.add(dbOpenHelper.createPlace(desk5Id, 1));
            places.add(dbOpenHelper.createPlace(desk5Id, 2));
        }
        {
            long desk6Id = dbOpenHelper.createDesk(2, 40, 40, cabinetId);//6
            places.add(dbOpenHelper.createPlace(desk6Id, 1));
            places.add(dbOpenHelper.createPlace(desk6Id, 2));
        }
        //   |6|  |5|   |    |  |  |  |
        //   |4|  |3|   |    | 4|  |  |
        //   |2|  |1|   |    |35|  |21|
        //       [-]


        long lessonId = dbOpenHelper.createSubject("физика", classId
                //, cabinetId
        );
        Date startLessonTime = new GregorianCalendar(2017, 10, 17, 8, 30).getTime();//1502343000000 --10 августа
        Date endLessonTime = new GregorianCalendar(2017, 10, 17, 9, 15).getTime();//на 7 месяц  1502345700000
        dbOpenHelper.setLessonTimeAndCabinet(lessonId, cabinetId, startLessonTime, endLessonTime, SchoolContract.TableSubjectAndTimeCabinetAttitude.CONSTANT_REPEAT_NEVER);

        //создание настроек после удаления таблицы
        //db.createNewSettingsProfileWithId1("default", 50);

        dbOpenHelper.setLearnerOnPlace(//lessonId,
                lerner1Id, places.get(1));
        dbOpenHelper.setLearnerOnPlace(//lessonId,
                lerner2Id, places.get(0));
        dbOpenHelper.setLearnerOnPlace(//lessonId,
                lerner3Id, places.get(2));
        dbOpenHelper.setLearnerOnPlace(//lessonId,
                lerner4Id, places.get(7));
        dbOpenHelper.setLearnerOnPlace(//lessonId,
                lerner5Id, places.get(3));
        dbOpenHelper.close();

        Toast toast = Toast.makeText(this, R.string.settings_activity_toast_data_delete_success, Toast.LENGTH_LONG);
        toast.show();
    }

    //редактирование времени
    @Override
    public void editTime(int[][] time) {
        DataBaseOpenHelper db = new DataBaseOpenHelper(this);
        int answer = db.setSettingsTime(1, time);
        db.close();
        if (answer == 1) {
            Toast toast = Toast.makeText(this, R.string.settings_activity_toast_time_successfully_saved, Toast.LENGTH_LONG);
            toast.show();
        } else {
            Toast toast = Toast.makeText(this, R.string.settings_activity_toast_time_no_saved, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    //смена локали
    @Override
    public void editLocale(String newLocale) {
        //извлекаем язык
        String lang = newLocale;//здесь просто получение строки из диалога (а в нем из бд) ..default..en..ru..
        //новая локализация в бд
        DataBaseOpenHelper db = new DataBaseOpenHelper(this);
        db.setSettingsLocale(1, lang);


        //Toast toast = Toast.makeText(getApplicationContext(),"0000", Toast.LENGTH_LONG);неработает
        // и перезапуск
        System.exit(0);

    }


//-----------------------------------------создание экрана------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//кнопка назад в actionBar

//----------изменение размера------------

        final DataBaseOpenHelper db = new DataBaseOpenHelper(this);

        SeekBar sizeSeekBar = (SeekBar) findViewById(R.id.activity_settings_seekBar);
        LinearLayout sizeShowLayOut = (LinearLayout) findViewById(R.id.activity_settings_size_show_layout);

        final RelativeLayout room = new RelativeLayout(this);
        sizeShowLayOut.addView(room, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        if (db.getInterfaceSizeBySettingsProfileId(1) == -1) {
            db.createNewSettingsProfileWithId1("default", 50);//TODO Skipped 49 frames!  The application may be doing too much work on its main thread.
        }
        sizeSeekBar.setProgress((int) db.getInterfaceSizeBySettingsProfileId(1));
        updateShowRoom(room, (int) db.getInterfaceSizeBySettingsProfileId(1));


        sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//не когда касается, а когда начинает двигаться

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i != 0) {//избегаем деления на ноль
                    updateShowRoom(room, i);
                    db.setSettingsProfileParameters(1, "default", i);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//-------------максимальный ответ-------------

        //поле ввода
        final EditText maxEdit = (EditText) findViewById(R.id.activity_settings_max_grade_editText);

        maxEdit.setText("" + db.getSettingsMaxGrade(1));

        //кнопка  сохранения
        Button saveButton = (Button) findViewById(R.id.activity_settings_max_grade_save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (maxEdit.getText().toString().equals("")) {
                    maxEdit.setText("1");
                    Toast.makeText(getApplicationContext(), getString(R.string.settings_activity_toast_grade_no_entered), Toast.LENGTH_SHORT).show();
                } else {
                    if (maxEdit.getText().toString().length() <= 6) {
                        if (Integer.valueOf(maxEdit.getText().toString()) > 100) {
                            maxEdit.setText("100");
                            Toast.makeText(getApplicationContext(), getString(R.string.settings_activity_toast_grade_too_match), Toast.LENGTH_SHORT).show();
                            db.setSettingsMaxGrade(1, Integer.valueOf(maxEdit.getText().toString()));
                        } else {
                            if (Integer.valueOf(maxEdit.getText().toString()) < 1) {
                                maxEdit.setText("1");
                                Toast.makeText(getApplicationContext(), getString(R.string.settings_activity_toast_grade_too_min), Toast.LENGTH_SHORT).show();
                                db.setSettingsMaxGrade(1, Integer.valueOf(maxEdit.getText().toString()));
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.settings_activity_toast_grade_saved) + " " + Integer.valueOf(maxEdit.getText().toString()), Toast.LENGTH_SHORT).show();
                                db.setSettingsMaxGrade(1, Integer.valueOf(maxEdit.getText().toString()));
                            }
                        }
                    } else {
                        maxEdit.setText("100");
                        Toast.makeText(getApplicationContext(), getString(R.string.settings_activity_toast_grade_too_match), Toast.LENGTH_SHORT).show();
                        db.setSettingsMaxGrade(1, Integer.valueOf(maxEdit.getText().toString()));
                    }
                }
            }
        });


//--------------изменить время-----------

        //кнопка  изменения
        Button editTimeButton = (Button) findViewById(R.id.activity_settings_edit_time_button);
        //слушатель
        editTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseOpenHelper db = new DataBaseOpenHelper(getApplicationContext());
                int[][] arrays = db.getSettingsTime(1);
                db.close();
                Log.e("TeachersApp", "*********" + Arrays.toString(arrays));

                int[] arr0 = arrays[0];
                int[] arr1 = arrays[1];
                int[] arr2 = arrays[2];
                int[] arr3 = arrays[3];
                int[] arr4 = arrays[4];
                int[] arr5 = arrays[5];
                int[] arr6 = arrays[6];
                int[] arr7 = arrays[7];
                int[] arr8 = arrays[8];

                //диалог
                EditTimeDialogFragment editTimeDialogFragment = new EditTimeDialogFragment();
                //данные
                Bundle args = new Bundle();
                args.putIntArray("arr0", arr0);
                args.putIntArray("arr1", arr1);
                args.putIntArray("arr2", arr2);
                args.putIntArray("arr3", arr3);
                args.putIntArray("arr4", arr4);
                args.putIntArray("arr5", arr5);
                args.putIntArray("arr6", arr6);
                args.putIntArray("arr7", arr7);
                args.putIntArray("arr8", arr8);
                editTimeDialogFragment.setArguments(args);
                editTimeDialogFragment.show(getFragmentManager(), "editTime");

            }
        });


//--------------удаление данных-----------

        Button removeDataButton = (Button) findViewById(R.id.activity_settings_remove_data_button);
        removeDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //создаем диалог
                SettingsRemoveDataDialogFragment removeDialog =
                        new SettingsRemoveDataDialogFragment();
                // запускаем
                removeDialog.show(getFragmentManager(), "removeSettingsDialog");
            }
        });

//--------------оцените нас-----------------

        Button rateUsButton = (Button) findViewById(R.id.settings_rate_button);
        rateUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.learning.texnar13.teachersprogect"));
                if (!isActivityStarted(intent)) {
                    intent.setData(Uri
                            .parse("https://play.google.com/store/apps/details?id=com.learning.texnar13.teachersprogect"));
                    if (!isActivityStarted(intent)) {
                        Toast.makeText(
                                getApplicationContext(),
                                "Could not open Android market, please check if the market app installed or not. Try again later",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//--------------настройка локализации-----------------

        Button EditLocaleButton = (Button) findViewById(R.id.activity_settings_edit_locale_button);
        EditLocaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //текущая локаль из бд
                DataBaseOpenHelper db = new DataBaseOpenHelper(getApplicationContext());

                Bundle args = new Bundle();
                args.putString("locale", db.getSettingsLocale(1));
                //создаем диалог
                EditLocaleDialogFragment localeDialog =
                        new EditLocaleDialogFragment();
                localeDialog.setArguments(args);
                // запускаем
                localeDialog.show(getFragmentManager(), "editLocaleDialog");
            }
        });
    }


    //для кнопки оцените нас
    private boolean isActivityStarted(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

//----------------------------------------обновление парт-------------------------------------------

    private void updateShowRoom(RelativeLayout room, float multiplier) {
        room.removeAllViews();

        multiplier = multiplier / 1000 * getResources().getInteger(R.integer.desks_screen_multiplier);

        RelativeLayout[] tables = new RelativeLayout[4];
        RelativeLayout.LayoutParams[] tablesParams = new RelativeLayout.LayoutParams[4];

        for (int i = 0; i < 4; i++) {
            tables[i] = new RelativeLayout(this);
            tables[i].setBackground(getResources().getDrawable(R.drawable.settings_desk));
            tablesParams[i] = new RelativeLayout.LayoutParams(
                    (int) pxFromDp(2000 * multiplier), (int) pxFromDp(1000 * multiplier));

            tablesParams[i].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            tablesParams[i].addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            tablesParams[i].addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);


            room.addView(tables[i], tablesParams[i]);
        }
        // 00 11
        // 22 33
        tablesParams[0].setMargins((int) pxFromDp(1000 * multiplier), (int) pxFromDp(1000 * multiplier), 0, 0);
        tablesParams[1].setMargins((int) pxFromDp(4000 * multiplier), (int) pxFromDp(1000 * multiplier), 0, 0);
        tablesParams[2].setMargins((int) pxFromDp(1000 * multiplier), (int) pxFromDp(3000 * multiplier), 0, 0);
        tablesParams[3].setMargins((int) pxFromDp(4000 * multiplier), (int) pxFromDp(3000 * multiplier), 0, 0);

        //room.setLayoutParams(new LinearLayout.LayoutParams());
    }


//---------------------------------------технические методы-----------------------------------------

    private float pxFromDp(float dp) {
        return dp * getApplicationContext().getResources().getDisplayMetrics().density;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://кнопка назад в actionBar
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
