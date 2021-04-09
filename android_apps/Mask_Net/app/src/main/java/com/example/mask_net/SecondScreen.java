package com.example.mask_net;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondScreen extends Activity {
    ListView listView;
    List<Penalty> penaltyList=new ArrayList<Penalty>();
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        listView = findViewById(R.id.listView);

        Intent ii = getIntent();
        String user_name = ii.getStringExtra("user_name");

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://52.188.166.61:7000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final ApiInterface request = retrofit.create(ApiInterface.class);
            Call<List<Task>> call = request.getstatus(user_name);
            call.enqueue(new Callback<List<Task>>() {
                @Override
                public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                    try {

                        List<Task> rs = response.body();
                        if (rs.size() > 0) {

                            for (int i = 0; i <= rs.size(); i++) {
                                Task user = rs.get(i);

                               id= user.getId();
                                String location = user.getLocation();
                                String date = user.getDate();
                                String image = user.getImg();


                                Bitmap bm = StringToBitMap(image);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] byteArray = stream.toByteArray();
                                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                penaltyList.add(new Penalty(bmp,date,location,id));

                                CustomerAdapter ca=new CustomerAdapter(SecondScreen.this,R.layout.activity_main3,penaltyList);
                                listView.setAdapter(ca);

                            }

                        }

                    } catch (Exception e) {
                        Log.d("Response error", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<List<Task>> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });


    }


    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "image to bitmap error", Toast.LENGTH_LONG).show();
            e.getMessage();
            return null;
        }
    }
}
