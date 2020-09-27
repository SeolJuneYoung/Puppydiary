package org.techtown.puppydiary.kgmenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.puppydiary.R;
import org.techtown.puppydiary.network.Data.KgupdateData;
import org.techtown.puppydiary.network.Response.KgupdateResponse;
import org.techtown.puppydiary.network.Response.kgDeleteResponse;
import org.techtown.puppydiary.network.RetrofitClient;
import org.techtown.puppydiary.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.techtown.puppydiary.kgmenu.KgTab.kg_month;
import static org.techtown.puppydiary.kgmenu.KgTab.yearr;

public class KgPopup extends AppCompatActivity {
    String monthname;
    private  static Context context;
    ActionBar actionBar;
    public static double puppykg;
    String kgStr;
    EditText weight;
    Button okay;
    Button close;
    int year;
    int month;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_kg_upload);

        actionBar = getSupportActionBar();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffD6336B));
        getSupportActionBar().setTitle("댕댕이어리");
        actionBar.setIcon(R.drawable.logo);
        actionBar.setDisplayUseLogoEnabled(true) ;
        actionBar.setDisplayShowHomeEnabled(true) ;

        service = RetrofitClient.getClient().create(ServiceApi.class);

        monthname = kg_month;
        TextView Month = (TextView) findViewById(R.id.kgmonth);
        Month.setText(monthname);  //클릭한 달의 이름으로 setText

        year = yearr;

        okay = findViewById(R.id.kg_confirm);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okay.setBackgroundResource(R.drawable.button_pressed); //확인 버튼 클릭시 color changed -> 다른 버튼들에도 추가해주면 좋음
                Intent intent_kgconfirm = new Intent(getApplicationContext(), KgTab.class);
                startActivity(intent_kgconfirm);

                weight = (EditText)findViewById(R.id.kg_weight); //weight edittext 가져오기, kgTab에서 사용해야 하는 것이므로 이 클래스에서는 public 설정
                kgStr = weight.getText().toString();
                //만약 jan의 kg 이 3.5 였다면 tostring으로 "3.5"=kgStr

                if(monthname.equals("January")) {
                    puppykg = Double.parseDouble(kgStr);
                    month = 1;
                    //double.parsedouble을 이용해서 string이었던 "3.5"를 double로 형변환시켜주어 저장 -> 저장 후 kgTab으로 가져감 (이 코드는 kgTab에 있음)
                }
                else if(monthname.equals("February")) {
                    puppykg = Double.parseDouble(kgStr);
                    month = 2;
                }
                else if(monthname.equals("March")) {
                    puppykg = Double.parseDouble(kgStr);
                    month = 3;
                }
                else if(monthname.equals("April")) {
                    puppykg = Double.parseDouble(kgStr);
                    month = 4;
                }
                else if(monthname.equals("May")) {
                    puppykg = Double.parseDouble(kgStr);
                    month = 5;
                }
                else if(monthname.equals("June")) {
                    puppykg = Double.parseDouble(kgStr);
                    month = 6;
                }
                else if(monthname.equals("July")) {
                    puppykg = Double.parseDouble(kgStr);
                    month = 7;
                }
                else if(monthname.equals("August")) {
                    puppykg = Double.parseDouble(kgStr);
                    month = 8;
                }
                else if(monthname.equals("September")) {
                    puppykg = Double.parseDouble(kgStr);
                    month = 9;
                }
                else if(monthname.equals("October")) {
                    puppykg = Double.parseDouble(kgStr);
                    month = 10;
                }
                else if(monthname.equals("November")) {
                    puppykg = Double.parseDouble(kgStr);
                    month = 11;
                }
                else if(monthname.equals("December")) {
                    puppykg = Double.parseDouble(kgStr);
                    month = 12;
                }

                UpdateKg(new KgupdateData(year, month, puppykg));
            }
        });

        close = findViewById(R.id.close); // 삭제 버튼
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close.setBackgroundColor( Color.parseColor("#ed426e"));

                if(monthname.equals("January")) {
                    month = 1;
                }
                else if(monthname.equals("February")) {
                    month = 2;
                }
                else if(monthname.equals("March")) {
                    month = 3;
                }
                else if(monthname.equals("April")) {
                    month = 4;
                }
                else if(monthname.equals("May")) {
                    month = 5;
                }
                else if(monthname.equals("June")) {
                    month = 6;
                }
                else if(monthname.equals("July")) {
                    month = 7;
                }
                else if(monthname.equals("August")) {
                    month = 8;
                }
                else if(monthname.equals("September")) {
                    month = 9;
                }
                else if(monthname.equals("October")) {
                    month = 10;
                }
                else if(monthname.equals("November")) {
                    month = 11;
                }
                else if(monthname.equals("December")) {
                    month = 12;
                }

                DeleteKG(year, month);
                Intent intent_kgclose = new Intent(getApplicationContext(), KgTab.class); //일단 바로 검색결과 띄음
                startActivity(intent_kgclose);
            }
        });

    }

    private void UpdateKg(KgupdateData data){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sp.getString("TOKEN", "");
        service.kgupdate(token, data).enqueue(new Callback<KgupdateResponse>() {

            @Override
            public void onResponse(Call<KgupdateResponse> call, Response<KgupdateResponse> response) {
                KgupdateResponse result = response.body();
                Toast.makeText(KgPopup.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if(result.getSuccess() == true){
                    Intent intent_start = new Intent(getApplicationContext(), KgTab.class);
                    intent_start.putExtra("year",year);
                    startActivityForResult(intent_start, 2000);
                }
            }

            @Override
            public void onFailure(Call<KgupdateResponse> call, Throwable t) {
                Toast.makeText(KgPopup.this, "체중 업데이트 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e("체중 업데이트 에러 발생", t.getMessage());
            }
        });
    }

    private void  DeleteKG(int year_D, int month_D){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sp.getString("TOKEN", "");
        service.kgdelete(token, year_D, month_D).enqueue(new Callback<kgDeleteResponse>() {
            @Override
            public void onResponse(Call<kgDeleteResponse> call, Response<kgDeleteResponse> response) {
                kgDeleteResponse result = response.body();
                Toast.makeText(KgPopup.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if(result.getSuccess() == true){
                    Intent intent_start = new Intent(getApplicationContext(), KgTab.class);
                    intent_start.putExtra("year",year);
                    startActivityForResult(intent_start, 2000);
                }
            }

            @Override
            public void onFailure(Call<kgDeleteResponse> call, Throwable t) {
                Toast.makeText(KgPopup.this, "체중 삭제 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }

        });
    }
}