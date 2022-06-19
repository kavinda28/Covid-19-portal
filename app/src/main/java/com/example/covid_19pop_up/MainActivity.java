package com.example.covid_19pop_up;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
TextView tvCases,tvRecovered,tvCritical,tvActive,tvTodayCase,tvTotalDeaths,tvTodayDeaths,tvAffectedCountries;
SimpleArcLoader simpleArcLoader;
ScrollView scrollView;
PieChart pieChart;
SwipeRefreshLayout refreshLayout;
Button showcountry;

    public static List<CountrtModel> countrtModelListarray=new ArrayList<>();
    CountrtModel countrtModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!isConnected(this)){
            showCustomerDialog();
        }else {
            System.out.println("...............................done network");
        }

        refreshLayout=findViewById(R.id.refresh);//refresh activity
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               startActivity(new Intent(getApplicationContext(),MainActivity.class));
                refreshLayout.setRefreshing(false);
                finish();
            }
        });

        tvCases=findViewById(R.id.tvCases);
        tvRecovered=findViewById(R.id.tvRecovered);
        tvCritical=findViewById(R.id.tvcritical);
        tvActive=findViewById(R.id.tvActive);
        tvTodayCase=findViewById(R.id.tvTodayCase);
        tvTotalDeaths=findViewById(R.id.tvTotalDeaths);
        tvTodayDeaths=findViewById(R.id.tvTodayDeaths);
        tvAffectedCountries=findViewById(R.id.tvAffectedCountries);

        simpleArcLoader=findViewById(R.id.loader);
        scrollView=findViewById(R.id.scrollStats);
        pieChart=findViewById(R.id.piechart);

        showcountry=findViewById(R.id.newbtn);
        showcountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=8;
                Country_Details_show_new c=new Country_Details_show_new(countrtModelListarray);
                startActivity(new Intent(getApplicationContext(),Country_Details_show_new.class).putExtra("position",position));
            }
        });
        fetchData();// fetch global covid updates
        fetchData_country();
    }

    private void fetchData() {
        String url="https://disease.sh/v3/covid-19/all";

        simpleArcLoader.start();

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject=new JSONObject(response.toString());
                    tvCases.setText(jsonObject.getString("cases"));
                    tvRecovered.setText(jsonObject.getString("recovered"));
                    tvCritical.setText(jsonObject.getString("critical"));
                    tvActive.setText(jsonObject.getString("active"));
                    tvTodayCase.setText(jsonObject.getString("todayCases"));
                    tvTotalDeaths.setText(jsonObject.getString("deaths"));
                    tvTodayDeaths.setText(jsonObject.getString("todayDeaths"));
                    tvAffectedCountries.setText(jsonObject.getString("affectedCountries"));

                    pieChart.addPieSlice(new PieModel("cases",Integer.parseInt(tvCases.getText().toString()), Color.parseColor("#FFA726")));
                    pieChart.addPieSlice(new PieModel("recovered",Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
                    pieChart.addPieSlice(new PieModel("deaths",Integer.parseInt(tvTotalDeaths.getText().toString()), Color.parseColor("#EF5350")));
                    pieChart.addPieSlice(new PieModel("active",Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29B6F6")));
                    pieChart.startAnimation();

                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void GoTrack_Countries(View view) {

        startActivity(new Intent(getApplicationContext(),AffectedCountries.class));

    } //AffectedCountries call

    private void showCustomerDialog() {
        System.out.println("...............................showCustomerDialog");


        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Please Connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // startActivity(new Intent(getApplicationContext(),AffectedCountries.class));
                        finish();
                    }
                });
builder.show();

    } //show network ont working popup

    private boolean isConnected(MainActivity mainActivity) {
        ConnectivityManager connectivityManager=(ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())){
            return  true;
        }else {
            return false;
        }


    }//check is that have network connections


    private void fetchData_country() {
        String url="https://disease.sh/v3/covid-19/countries";
//old https://corona.lmao.ninja/v2/countries
        simpleArcLoader.start();

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0; i < jsonArray.length(); i++){

                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        String CountryName= jsonObject.getString("country");
                        String cases= jsonObject.getString("cases");
                        String todayCases= jsonObject.getString("todayCases");
                        String deaths= jsonObject.getString("deaths");
                        String todayDeaths= jsonObject.getString("todayDeaths");
                        String recovered= jsonObject.getString("recovered");
                        String active= jsonObject.getString("active");
                        String critical= jsonObject.getString("critical");
                        String updated= jsonObject.getString("updated");

                        JSONObject object=jsonObject.getJSONObject("countryInfo");
                        String flagUrl=object.getString("flag");

                        countrtModel = new CountrtModel(flagUrl,CountryName,cases,todayCases,deaths,todayDeaths,recovered,active,critical,updated);
                        countrtModelListarray.add(countrtModel);
                    }

                    Log.i("logs", String.valueOf(countrtModelListarray));

//                    customerAdapter =new customerAdapter(AffectedCountries.this,countrtModelList);//parse affected country list details to adpter class
//                    listView.setAdapter(customerAdapter);//loard list view get data from adpter class
//                    simpleArcLoader.stop();
//                    simpleArcLoader.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
//                    simpleArcLoader.stop();
//                    simpleArcLoader.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity  .this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);


    }
}
