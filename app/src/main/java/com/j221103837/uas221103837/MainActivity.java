package com.j221103837.uas221103837;

import static java.util.Locale.filter;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private RecyclerView _recyclerView1;
    private List<MahasiswaModel> mahasiswaModelList;
    private MahasiswaAdapter ma;
    private TextView _txtMahasiswaCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        _recyclerView1 = findViewById(R.id.recyclerView1);
        _txtMahasiswaCount = findViewById(R.id.txtMahasiswaCount);

        loadRecyclerView();
    }

    private void filter(String Text) {
        List<MahasiswaModel> filteredList = new ArrayList<>();

        for (MahasiswaModel item : mahasiswaModelList) {
            if (item.getNama().toLowerCase().contains(Text.toLowerCase())) {
//                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(MainActivity.this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            ma.filter(filteredList);
        }
    }

    private void loadRecyclerView() {
        AsyncHttpClient ahc = new AsyncHttpClient();
        String url = "https://stmikpontianak.cloud/011100862/tampilMahasiswa.php";

        ahc.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson g = new Gson();
                mahasiswaModelList = g.fromJson(new String(responseBody), new TypeToken<List<MahasiswaModel>>() {
                }.getType());

                RecyclerView.LayoutManager lm = new LinearLayoutManager(MainActivity.this);
                _recyclerView1.setLayoutManager(lm);

                ma = new MahasiswaAdapter(mahasiswaModelList);
                _recyclerView1.setAdapter(ma);

                String MahasiswaCount = "Total Mahasiswa : " + ma.getItemCount();
                _txtMahasiswaCount.setText(MahasiswaCount);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}