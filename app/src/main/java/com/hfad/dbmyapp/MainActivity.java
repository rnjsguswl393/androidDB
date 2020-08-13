package com.hfad.dbmyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    //사용안하고 있을때 지우거나delete 사용 안함으로 업데이트update
    //사용하고 있을떄 기존 데이터가 있으면 업데이트
    //사용하고 있을때 기존 데이터 없으면 추가insert
    DBHelper dbHelper;
    SQLiteDatabase db = null;
    Cursor cursor;
    Switch switch1, switch2, switch3;
    EditText place1, lati1, longi1;
    EditText place2, lati2, longi2;
    EditText place3, lati3, longi3;
    ArrayAdapter adapter1, adapter2, adapter3;
    ListView listView1, listView2, listView3;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        place1 = findViewById(R.id.place1);
        lati1 = findViewById(R.id.lati1);
        longi1 = findViewById(R.id.longi1);
        place2 = findViewById(R.id.place2);
        lati2 = findViewById(R.id.lati2);
        longi2 = findViewById(R.id.longi2);
        place3 = findViewById(R.id.place3);
        lati3 = findViewById(R.id.lati3);
        longi3 = findViewById(R.id.longi3);
        listView1 = findViewById(R.id.Listview1);
        listView2 = findViewById(R.id.Listview2);
        listView3 = findViewById(R.id.Listview3);

        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);
        switch3 = findViewById(R.id.switch3);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); //키보드 내리기.올리기
        BtnOnClickListener onClickListener = new BtnOnClickListener(); //버튼 클릭 리스너
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(onClickListener);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(onClickListener);
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(onClickListener);
        dbHelper = new DBHelper(this, 3);
        db = dbHelper.getWritableDatabase();
//        switch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {});
        select();
    }

    private void select() {
        cursor = db.rawQuery("SELECT * FROM testtable", null);
        adapter1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);
        adapter2 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);
        adapter3 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);

        while (cursor.moveToNext()) {
            adapter1.add(cursor.getString(0));
            adapter2.add(cursor.getString(1));
            adapter3.add(cursor.getString(2));
        }
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);
        listView3.setAdapter(adapter3);
    }

    public void selectclick(View view) {
        select();
    }

    class BtnOnClickListener implements Button.OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button1:
                    if (switch1.isChecked()) { //스위치 on
                        String pos = place1.getText().toString();
                        String lati = lati1.getText().toString();
                        String longi = longi1.getText().toString();
//            Integer lati = Integer.parseInt(lati1.getText().toString());
//            Integer longi = Integer.parseInt(longi1.getText().toString());
                        //아무것도 입력되지않았을때
                        if (pos.getBytes().length <= 0 || lati.getBytes().length <= 0 || longi.getBytes().length <= 0) {
                            Toast.makeText(getApplicationContext(), "데이터 입력하세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //db에 값 존재 유무 확인
                        else {
                            cursor = db.rawQuery("SELECT pos FROM testtable", null);
                            boolean checkDB = false;
                            while (cursor.moveToNext()) {
                                if ((cursor.getString(0)).equals(pos)) {
                                    checkDB = true;
                                    break;
                                }
                            }
                            if (checkDB == false) { //존재하지않는다면 추가
                                imm.hideSoftInputFromWindow(place1.getWindowToken(), 0); //키보드 내리기
                                Toast.makeText(getApplicationContext(), "추가 성공", Toast.LENGTH_SHORT).show();
                                db.execSQL("INSERT INTO testtable VALUES('" + pos + "'," + lati + "," + longi + ");");
                                select();

                                place1.setText("");
                                longi1.setText("");
                                lati1.setText("");
                            } else { //존재한다면 수정
                                imm.hideSoftInputFromWindow(place1.getWindowToken(), 0); //키보드 내리기
                                Toast.makeText(getApplicationContext(), "수정 성공", Toast.LENGTH_SHORT).show();
                                db.execSQL("UPDATE testtable SET lati = " + lati + ",longi=" + longi + " WHERE pos = '" + pos + "';");
                                select();
                            }
                        }
                    } else {
                        //스위치Off
                        String pos1 = place1.getText().toString();
                        if (pos1.getBytes().length <= 0) { //아무것도 입력되지않음
                            Toast.makeText(getApplicationContext(), "값을 입력하세요", Toast.LENGTH_SHORT).show();
                        } else { //해당 값 입력되면 삭제
                            imm.hideSoftInputFromWindow(place1.getWindowToken(), 0); //키보드 내리기
                            Toast.makeText(getApplicationContext(), "정상적으로 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            db.execSQL("DELETE FROM testtable WHERE pos='" + pos1 + "';");
                            place1.setText("");
                            longi1.setText("");
                            lati1.setText("");
                            select();
                        }
                    }
                    break;
                case R.id.button2:
                    if (switch2.isChecked()) { //스위치 on
                        String pos = place2.getText().toString();
                        String lati = lati2.getText().toString();
                        String longi = longi2.getText().toString();
//            Integer lati = Integer.parseInt(lati1.getText().toString());
//            Integer longi = Integer.parseInt(longi1.getText().toString());
                        //아무것도 입력되지않았을때
                        if (pos.getBytes().length <= 0 || lati.getBytes().length <= 0 || longi.getBytes().length <= 0) {
                            Toast.makeText(getApplicationContext(), "데이터 입력하세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //db에 값 존재 유무 확인
                        else {
                            cursor = db.rawQuery("SELECT pos FROM testtable", null);
                            boolean checkDB = false;
                            while (cursor.moveToNext()) {
                                if ((cursor.getString(0)).equals(pos)) {
                                    checkDB = true;
                                    break;
                                }
                            }
                            if (checkDB == false) { //존재하지않는다면 추가
                                imm.hideSoftInputFromWindow(place2.getWindowToken(), 0); //키보드 내리기
                                Toast.makeText(getApplicationContext(), "추가 성공", Toast.LENGTH_SHORT).show();
                                db.execSQL("INSERT INTO testtable VALUES('" + pos + "'," + lati + "," + longi + ");");
                                select();

                                place2.setText("");
                                longi2.setText("");
                                lati2.setText("");
                            } else { //존재한다면 수정
                                imm.hideSoftInputFromWindow(place2.getWindowToken(), 0); //키보드 내리기
                                Toast.makeText(getApplicationContext(), "수정 성공", Toast.LENGTH_SHORT).show();
                                db.execSQL("UPDATE testtable SET lati = " + lati + ",longi=" + longi + " WHERE pos = '" + pos + "';");
                                select();
                            }
                        }
                    } else {
                        //스위치Off
                        String pos = place2.getText().toString();
                        if (pos.getBytes().length <= 0) { //아무것도 입력되지않음
                            Toast.makeText(getApplicationContext(), "값을 입력하세요", Toast.LENGTH_SHORT).show();
                        } else { //해당 값 입력되면 삭제
                            imm.hideSoftInputFromWindow(place2.getWindowToken(), 0); //키보드 내리기
                            Toast.makeText(getApplicationContext(), "정상적으로 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            db.execSQL("DELETE FROM testtable WHERE pos='" + pos + "';");
                            place2.setText("");
                            longi2.setText("");
                            lati2.setText("");
                            select();
                        }
                    }
                    break;
                case R.id.button3:
                    if (switch3.isChecked()) { //스위치 on
                        String pos = place3.getText().toString();
                        String lati = lati3.getText().toString();
                        String longi = longi3.getText().toString();
//            Integer lati = Integer.parseInt(lati1.getText().toString());
//            Integer longi = Integer.parseInt(longi1.getText().toString());
                        //아무것도 입력되지않았을때
                        if (pos.getBytes().length <= 0 || lati.getBytes().length <= 0 || longi.getBytes().length <= 0) {
                            Toast.makeText(getApplicationContext(), "데이터 입력하세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //db에 값 존재 유무 확인
                        else {
                            cursor = db.rawQuery("SELECT pos FROM testtable", null);
                            boolean checkDB = false;
                            while (cursor.moveToNext()) {
                                if ((cursor.getString(0)).equals(pos)) {
                                    checkDB = true;
                                    break;
                                }
                            }
                            if (checkDB == false) { //존재하지않는다면 추가
                                imm.hideSoftInputFromWindow(place3.getWindowToken(), 0); //키보드 내리기
                                Toast.makeText(getApplicationContext(), "추가 성공", Toast.LENGTH_SHORT).show();
                                db.execSQL("INSERT INTO testtable VALUES('" + pos + "'," + lati + "," + longi + ");");
                                select();

                                place3.setText("");
                                longi3.setText("");
                                lati3.setText("");
                            } else { //존재한다면 수정
                                imm.hideSoftInputFromWindow(place3.getWindowToken(), 0); //키보드 내리기
                                Toast.makeText(getApplicationContext(), "수정 성공", Toast.LENGTH_SHORT).show();
                                db.execSQL("UPDATE testtable SET lati = " + lati + ",longi=" + longi + " WHERE pos = '" + pos + "';");
                                select();
                            }
                        }
                    } else {
                        //스위치Off
                        String pos = place3.getText().toString();
                        if (pos.getBytes().length <= 0) { //아무것도 입력되지않음
                            Toast.makeText(getApplicationContext(), "값을 입력하세요", Toast.LENGTH_SHORT).show();
                        } else { //해당 값 입력되면 삭제
                            imm.hideSoftInputFromWindow(place3.getWindowToken(), 0); //키보드 내리기
                            Toast.makeText(getApplicationContext(), "정상적으로 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            db.execSQL("DELETE FROM testtable WHERE pos='" + pos + "';");
                            place3.setText("");
                            longi3.setText("");
                            lati3.setText("");
                            select();
                        }
                    }
                    break;
            }
        }
    }
}
