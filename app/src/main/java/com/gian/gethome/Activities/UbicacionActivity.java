package com.gian.gethome.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.gian.gethome.Adapters.RecyclerLocationAdapter;
import com.gian.gethome.Clases.MainPojo;
import com.gian.gethome.Interface.ApiInterface;
import com.gian.gethome.R;

import java.lang.reflect.Array;

import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UbicacionActivity extends AppCompatActivity {
    EditText editText;
    RecyclerView recyclerView;
    ApiInterface apiInterface;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        editText = findViewById(R.id.editText_ubicacion);
        recyclerView = findViewById(R.id.recyclerview);

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(ApiInterface.class);



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getData(s.toString());
            }
        });




    }

    private void getData(String text)
    {
        apiInterface.getPlace(text,getString(R.string.api_key_location)).enqueue(new Callback<MainPojo>() {
            @Override
            public void onResponse(Call<MainPojo> call, Response<MainPojo> response) {
                if(response.isSuccessful()){
                    recyclerView.setVisibility(View.VISIBLE);
                    RecyclerLocationAdapter recyclerLocationAdapter = new RecyclerLocationAdapter(response.body().getPredictions());
                    recyclerView.setAdapter(recyclerLocationAdapter);
                }else{
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MainPojo> call, Throwable t) {

            }
        });
    }
}