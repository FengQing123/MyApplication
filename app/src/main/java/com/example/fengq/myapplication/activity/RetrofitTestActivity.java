package com.example.fengq.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fengq.myapplication.R;
import com.example.fengq.myapplication.api.APIInterface;
import com.example.fengq.myapplication.bean.PersonModel;

import javax.security.auth.login.LoginException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fengq on 2017/5/10.
 */

public class RetrofitTestActivity extends Activity {

    private static final String TAG = RetrofitTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_begin_retrofit:
                requestData();
                break;
        }
    }

    public void requestData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface service = retrofit.create(APIInterface.class);
        Call<PersonModel> model = service.repo("Guolei1130");
        model.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                Log.e(TAG, "id=" + response.body().getId());
            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                Log.e(TAG, "failure message=" + t.getMessage());
            }
        });
    }
}
