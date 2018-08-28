package com.zrz.android.olearisweather;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.zrz.android.olearisweather.adapters.RvPlacesAdapter;
import com.zrz.android.olearisweather.data.Contract;
import com.zrz.android.olearisweather.data.DBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerViewMain;
    ArrayList<Double> latitudeList;
    ArrayList<Double> longitudeList;
    private final static String PLACE="place";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonAddPlace=(Button)findViewById(R.id.button_add_place);
        recyclerViewMain=(RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerViewMain.setLayoutManager(linearLayoutManager);

        buttonAddPlace.setOnClickListener(this);

        createRecyclerView();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        int id=v.getId();
        switch (id){
            case R.id.button_add_place:
                intent=new Intent(this, MapActivity.class);
                startActivityForResult(intent,1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode==RESULT_OK){
                    createRecyclerView();
                }
                break;
            default:
                break;
        }
    }

    private void setData(){
        latitudeList=new ArrayList<>();
        longitudeList=new ArrayList<>();

        DBHelper dbHelper=new DBHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] columns={Contract.PLACES_TABLE_COLUMN_LATITUDE, Contract.PLACES_TABLE_COLUMN_LONGITUDE};
        Cursor cursor = database.query(Contract.PLACES_TABLE_NAME, columns, null, null,
                null, null, null);
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int columnIndexLat = cursor.getColumnIndex(Contract.PLACES_TABLE_COLUMN_LATITUDE);
            int columnIndexLng = cursor.getColumnIndex(Contract.PLACES_TABLE_COLUMN_LONGITUDE);

            double lat, lng;
            do {
                lat = cursor.getDouble(columnIndexLat);
                lng = cursor.getDouble(columnIndexLng);
                latitudeList.add(lat);
                longitudeList.add(lng);
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close();
    }

    private void createRecyclerView(){
        setData();
        RvPlacesAdapter rvPlacesAdapter=new RvPlacesAdapter(this, latitudeList, longitudeList);
        recyclerViewMain.setAdapter(rvPlacesAdapter);
    }
}
