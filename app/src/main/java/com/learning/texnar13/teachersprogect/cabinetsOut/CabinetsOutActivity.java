package com.learning.texnar13.teachersprogect.cabinetsOut;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.learning.texnar13.teachersprogect.CabinetRedactorActivity;
import com.learning.texnar13.teachersprogect.R;
import com.learning.texnar13.teachersprogect.data.DataBaseOpenHelper;
import com.learning.texnar13.teachersprogect.data.SchoolContract;

import java.util.ArrayList;

public class CabinetsOutActivity extends AppCompatActivity implements EditCabinetDialogInterface, CreateCabinetInterface {

    //static потом (для переворота)
    Long[] cabinetsId;
    String[] cabinetsNames;
    LinearLayout room;

//---------------------------------методы диалогов--------------------------------------------------


//-----создание-----

    @Override
    public void createCabinet(String name) {
        //создаем кабинет
        DataBaseOpenHelper db = new DataBaseOpenHelper(this);
        db.createCabinet(name);
        db.close();
        //опять выводим списки
        getCabinets();
        outCabinets();
    }

//-----редактирование-----

    //переименование
    @Override
    public void editCabinet(String name, long cabinetId) {
        //изменяем кабинет
        DataBaseOpenHelper db = new DataBaseOpenHelper(this);
        ArrayList<Long> arrayList = new ArrayList<>();
        arrayList.add(cabinetId);
        db.setCabinetName(arrayList, name);
        db.close();
        //опять выводим списки
        getCabinets();
        outCabinets();
    }

    //удаление
    @Override
    public void removeCabinet(long cabinetId) {
        //удаляем кабинет
        DataBaseOpenHelper db = new DataBaseOpenHelper(this);
        ArrayList<Long> arrayList = new ArrayList<>();
        arrayList.add(cabinetId);
        db.deleteCabinets(arrayList);
        db.close();
        //опять выводим списки
        getCabinets();
        outCabinets();
    }

//-------------------------------меню сверху--------------------------------------------------------

//убрал за ненадобностью в подсказке
//    //раздуваем неаше меню
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.cabinets_out_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    //назначаем функции меню
//    @Override
//    public boolean onPrepareOptionsMenu(final Menu menu) {
//        //кнопка помощь
//        menu.findItem(R.id.cabinets_out_menu_item_help).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                Toast toast = Toast.makeText(getApplicationContext(),"В разработке ¯\\_(ツ)_/¯",Toast.LENGTH_LONG);
//                toast.show();
//
//                return true;
//            }
//        });
//        return super.onPrepareOptionsMenu(menu);
//    }

//------------------------------создаем активность--------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinets_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.cabinets_out_toolbar);
        setSupportActionBar(toolbar);

        //кнопка назад
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //------плавающая кнопка с низу--------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-----диалог создания-----
                //инициализируем диалог
                CreateCabinetDialogFragment createCabinetDialog = new CreateCabinetDialogFragment();
                //показать диалог
                createCabinetDialog.show(getFragmentManager(), "createCabinetDialog");

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryOrange)));

        //--------экран со списком---------
        //создание
        room = (LinearLayout) findViewById(R.id.cabinets_out_room);

        //обновляем данные
        getCabinets();
        //выводим с новыми данными
        outCabinets();
    }

//-----------------------------------обновляем список кабинетов-------------------------------------

    void getCabinets() {
        //выводим кабинеты из бд
        DataBaseOpenHelper db = new DataBaseOpenHelper(this);
        Cursor cabinets = db.getCabinets();
        //инициализируем и очищаем массивы
        cabinetsId = new Long[cabinets.getCount()];
        cabinetsNames = new String[cabinets.getCount()];
        //пробегаемся по курсору
        for (int i = 0; i < cabinetsId.length; i++) {
            cabinets.moveToPosition(i);
            //получаем id кабинета
            cabinetsId[i] = cabinets.getLong(
                    cabinets.getColumnIndex(SchoolContract.TableCabinets.KEY_CABINET_ID)
            );
            cabinetsNames[i] = cabinets.getString(
                    cabinets.getColumnIndex(SchoolContract.TableCabinets.COLUMN_NAME)
            );
        }
        //заканчиваем работу
        cabinets.close();
        db.close();
    }

//-----------------------------------выводим список кабинетов---------------------------------------

    void outCabinets() {
        //удаляем все что было
        room.removeAllViews();
        //пробегаемся по кабинетам и создаем список из LinearLayout
        for (int i = 0; i < cabinetsId.length; i++) {
//создаем пункт списка-----------
            //список
            //-контейнер
            //--текст

//------контейнер----
            //создаем LinearLayout
            LinearLayout container = new LinearLayout(this);
            container.setBackgroundResource(R.drawable.start_screen_3_2_yellow_spot);

            //параметры контейнера(т.к. элемент находится в LinearLayout то и параметры используем его)
            LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,//ш
                    ViewGroup.LayoutParams.WRAP_CONTENT//в
            );
            containerParams.setMargins((int)pxFromDp(4), (int)pxFromDp(4), (int)pxFromDp(4), (int)pxFromDp(0));

//------текст------
            //создаём текст
            TextView item = new TextView(this);
            item.setGravity(Gravity.CENTER);
            item.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_title_size));
            item.setTextColor(Color.WHITE);

            //параметры пункта(т.к. элемент находится в LinearLayout то и параметры используем его)
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,//ш
                    ViewGroup.LayoutParams.WRAP_CONTENT//в
            );
            itemParams.gravity = Gravity.CENTER;
            // отступы текста в рамке
            itemParams.setMargins((int) pxFromDp(3), (int) pxFromDp(9), (int) pxFromDp(3), (int) pxFromDp(9));

            //--выводим текст--
            item.setText(cabinetsNames[i]);

//------помещаем текст в контейнер--------
            container.addView(item, itemParams);
//------помещаем контейнер в список-------
            room.addView(container, containerParams);

//------нажатие на пункт списка-------
            //номер пункта в списке
            final long finalId = cabinetsId[i];
//--короткое--
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //переходим на редактирование этого кабинета
                    Intent intent = new Intent(getApplicationContext(), CabinetRedactorActivity.class);
                    //передаём id выбранного кабинета
                    intent.putExtra(CabinetRedactorActivity.EDITED_CABINET_ID, finalId);
                    startActivity(intent);
                }
            });

//--долгое--
            container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //инициализируем диалог
                    EditCabinetDialogFragment editDialog = new EditCabinetDialogFragment();
                    //-данные для диалога-
                    //получаем из бд
                    DataBaseOpenHelper db = new DataBaseOpenHelper(getApplicationContext());
                    //кабинеты по Id
                    Cursor cabinetCursor = db.getCabinets(finalId);
                    cabinetCursor.moveToFirst();
                    //создаем обьект с данными
                    Bundle args = new Bundle();
                    args.putLong("cabinetId", finalId);
                    args.putString("name", cabinetCursor.getString(
                            cabinetCursor.getColumnIndex(
                                    SchoolContract.TableCabinets.COLUMN_NAME)
                    ));
                    //данные диалогу
                    editDialog.setArguments(args);
                    //показать диалог
                    editDialog.show(getFragmentManager(), "editCabinetDialog");
                    //заканчиваем работу с бд
                    cabinetCursor.close();
                    db.close();
                    return true;
                }
            });
        }

//------в конце выводим текст с подсказкой------

        //экран
        //-...
        //-контейнер
        //--текст
        //-контейнер
        //экран

        //---контейнер---
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        //параметры контейнера
        LinearLayout.LayoutParams containerParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
        containerParams.setMargins((int) pxFromDp(10), (int) pxFromDp(10), (int) pxFromDp(10), (int) pxFromDp(10));

        //---1 текст---
        //создаем
        TextView helpText1 = new TextView(this);
        helpText1.setGravity(Gravity.CENTER);
        helpText1.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_subtitle_size));
        helpText1.setTextColor(getResources().getColor(R.color.colorBackGroundDark));
        //helpText1.setText("Чтобы создать кабинет, нажмите \"+\" и введите его название. ");
        helpText1.setText(R.string.cabinets_out_activity_text_help);
        //добавляем
        container.addView(
                helpText1,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        //---выводим контейнер в экран---
        room.addView(container, containerParams);
    }

//------системные кнопки--------

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

//---------форматы----------

    private float pxFromDp(float px) {
        return px * getApplicationContext().getResources().getDisplayMetrics().density;
    }
}

/*
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int ZOOM = 2;
    int mode = NONE;
    //середина касания пальцев
    PointF startMid = new PointF();
    //изначальное растояние между пальцам
    float oldDist = 1f;
    //начальные параметры обьекта
    int widthOld = 1;
    int heightOld = 1;
    int xOld = 1;
    int yOld = 1;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                //если поставлен второй палец,назначаем новые координаты
                if (event.getPointerCount() == 2) {
                    //начальные размеры обьекта
                    widthOld = myRectangle.getWidth();
                    heightOld = myRectangle.getHeight();
                    //начальные координаты обьекта
                    xOld = (int) myRectangle.getX();
                    yOld = (int) myRectangle.getY();
                    //находим изначальное растояние между пальцами
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        findMidPoint(startMid, event);
                        mode = ZOOM;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ZOOM) {
                    //новое расстояние между пальцами
                    float newDist = spacing(event);
                    //находим коэффициент разницы между изначальным и новым расстоянием
                    float scale = newDist / oldDist;

                    if (newDist > 10f &&//слишком маленькое расстояние между пальцами
                            scale > 0.01f &&//слишком маленький коэффициент
                            (widthOld * scale > 10f && heightOld * scale > 10f) &&//слишком маленький размер
                            (widthOld * scale < 1500f && heightOld * scale < 1500f)//слишком большой размер
                            ) {
                        //-----трансформация размера-----
                        rectParams.width = (int) (widthOld * scale);
                        rectParams.height = (int) (heightOld * scale);
                        myRectangle.setLayoutParams(rectParams);

                        //-----трансформация координаты-----
                        //текущая середина пальцев
                        PointF nowMid = new PointF();
                        findMidPoint(nowMid, event);
                        //-перемещение обьекта-
                        // относительно центра зуммирования и перемещение пальцев в процессе зума
                        //ставим обьекту координаты
                        myRectangle.setX(((xOld - startMid.x) * scale) + nowMid.x);
                        myRectangle.setY(((yOld - startMid.y) * scale) + nowMid.y);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:

                mode = NONE;

                break;
        }
        return true;
    }

    //******************* Расстояние между первым и вторым пальцами из event
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    //************* координата середины между первым и вторым пальцами из event
    private void findMidPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    */