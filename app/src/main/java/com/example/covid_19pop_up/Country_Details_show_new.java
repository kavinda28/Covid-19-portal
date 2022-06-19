package com.example.covid_19pop_up;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Country_Details_show_new extends AppCompatActivity {

TextView tvCountry,tvCase,tvRecovered,tvCritical,tvActive,tvTodayCase,tvTotaldeath,tvTodayDeath;
PieChart pieChart2;
SwipeRefreshLayout refreshLayout;
    SimpleArcLoader simpleArcLoader;
    public static List<CountrtModel> countrtModelList=new ArrayList<>();
 Country_Details_show_new(List<CountrtModel> countrtModelsList){

    this.countrtModelList=countrtModelsList;

}
public Country_Details_show_new(){}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country__details_show_new);
        refreshLayout=findViewById(R.id.refresh);//refresh activity
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(getApplicationContext(), Country_Details_show_new.class));
                refreshLayout.setRefreshing(false);
                finish();
            }
        });
        SharedPreferences sharedPreferences=this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        String Country_name=sharedPreferences.getString("Country_name","sri");
        System.out.println("sharedPreferences::::::"+Country_name);

        getSupportActionBar().setTitle("Details of "+Country_name);
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
        simpleArcLoader=findViewById(R.id.loader);
        pieChart2=findViewById(R.id.piechart2);


        Country_name=Country_name.replaceAll(" ", "%20");
        System.out.println("ooooooooooooooooo"+Country_name);
        fetchData(Country_name);
//        tvCountry.setText(countrtModelList.get(positionCountry).getCountry());
//        tvCase.setText(countrtModelList.get(positionCountry).getCases());
//        tvRecovered.setText(countrtModelList.get(positionCountry).getRecovered());
//        tvCritical.setText(countrtModelList.get(positionCountry).getCritical());
//        tvActive.setText(countrtModelList.get(positionCountry).getActive());
//        tvTodayCase.setText(countrtModelList.get(positionCountry).getTodayCase());
//        tvTotaldeath.setText(countrtModelList.get(positionCountry).getDeaths());
//        tvTodayDeath.setText(countrtModelList.get(positionCountry).getTodayDeaths());
//        System.out.println("update value:::"+countrtModelList.get(positionCountry).getUpdated());
//
//        pieChart2.addPieSlice(new PieModel("cases",Integer.parseInt(tvCase.getText().toString()), Color.parseColor("#FFA726")));
//        pieChart2.addPieSlice(new PieModel("recovered",Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
//        pieChart2.addPieSlice(new PieModel("deaths",Integer.parseInt(tvTotaldeath.getText().toString()), Color.parseColor("#EF5350")));
//        pieChart2.addPieSlice(new PieModel("active",Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29B6F6")));
//        pieChart2.startAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void fetchData(String countryname) {

        String url="https://disease.sh/v3/covid-19/countries/"+countryname;

        simpleArcLoader.start();

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject=new JSONObject(response.toString());
                    tvCountry.setText(jsonObject.getString("country"));
                    tvCase.setText(jsonObject.getString("cases"));
                    tvRecovered.setText(jsonObject.getString("recovered"));
                    tvCritical.setText(jsonObject.getString("critical"));
                    tvActive.setText(jsonObject.getString("active"));
                    tvTodayCase.setText(jsonObject.getString("todayCases"));
                    tvTotaldeath.setText(jsonObject.getString("deaths"));
                    tvTodayDeath.setText(jsonObject.getString("todayDeaths"));


                    pieChart2.addPieSlice(new PieModel("cases",Integer.parseInt(tvCase.getText().toString()), Color.parseColor("#FFA726")));
                    pieChart2.addPieSlice(new PieModel("recovered",Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
                    pieChart2.addPieSlice(new PieModel("deaths",Integer.parseInt(tvTotaldeath.getText().toString()), Color.parseColor("#EF5350")));
                    pieChart2.addPieSlice(new PieModel("active",Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29B6F6")));
                    pieChart2.startAnimation();

                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                Toast.makeText(Country_Details_show_new.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
