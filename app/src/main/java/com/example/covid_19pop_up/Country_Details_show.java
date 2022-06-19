package com.example.covid_19pop_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class Country_Details_show extends AppCompatActivity {
private int positionCountry;
TextView tvCountry,tvCase,tvRecovered,tvCritical,tvActive,tvTodayCase,tvTotaldeath,tvTodayDeath;
PieChart pieChart2;
SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country__details_show);
        refreshLayout=findViewById(R.id.refresh);//refresh activity
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(getApplicationContext(),Country_Details_show.class));
                refreshLayout.setRefreshing(false);
                finish();
            }
        });
        Intent intent=getIntent();
        positionCountry=intent.getIntExtra("position",0);
        Log.i("positionCountry", String.valueOf(positionCountry));

        getSupportActionBar().setTitle("Details of "+AffectedCountries.countrtModelList.get(positionCountry).getCountry());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        tvCountry=findViewById(R.id.tvCountry);
        tvCase=findViewById(R.id.tvCases);
        tvRecovered=findViewById(R.id.tvRecovered);
        tvCritical=findViewById(R.id.tvCritical);
        tvActive=findViewById(R.id.tvActive);
        tvTodayCase=findViewById(R.id.tvTodayCases);
        tvTotaldeath=findViewById(R.id.tvDeaths);
        tvTodayDeath=findViewById(R.id.tvTodayDeaths);

        pieChart2=findViewById(R.id.piechart2);


        tvCountry.setText(AffectedCountries.countrtModelList.get(positionCountry).getCountry());
        tvCase.setText(AffectedCountries.countrtModelList.get(positionCountry).getCases());
        tvRecovered.setText(AffectedCountries.countrtModelList.get(positionCountry).getRecovered());
        tvCritical.setText(AffectedCountries.countrtModelList.get(positionCountry).getCritical());
        tvActive.setText(AffectedCountries.countrtModelList.get(positionCountry).getActive());
        tvTodayCase.setText(AffectedCountries.countrtModelList.get(positionCountry).getTodayCase());
        tvTotaldeath.setText(AffectedCountries.countrtModelList.get(positionCountry).getDeaths());
        tvTodayDeath.setText(AffectedCountries.countrtModelList.get(positionCountry).getTodayDeaths());
        System.out.println("update value:::"+AffectedCountries.countrtModelList.get(positionCountry).getUpdated());

        pieChart2.addPieSlice(new PieModel("cases",Integer.parseInt(tvCase.getText().toString()), Color.parseColor("#FFA726")));
        pieChart2.addPieSlice(new PieModel("recovered",Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
        pieChart2.addPieSlice(new PieModel("deaths",Integer.parseInt(tvTotaldeath.getText().toString()), Color.parseColor("#EF5350")));
        pieChart2.addPieSlice(new PieModel("active",Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29B6F6")));
        pieChart2.startAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
