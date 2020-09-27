package org.techtown.puppydiary.accountmenu;

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
import org.techtown.puppydiary.network.Data.account.AccountUpdateData;
import org.techtown.puppydiary.network.Response.account.AccountUpdateResponse;
import org.techtown.puppydiary.network.Response.account.DeleteAccountResponse;
import org.techtown.puppydiary.network.RetrofitClient;
import org.techtown.puppydiary.network.ServiceApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//moneyTab item click 시 나오는 수정 삭제 화면
public class MoneyEdit extends AppCompatActivity {

    private  static Context context;
    ActionBar actionBar;

    TextView tv_date;
    EditText et_price;
    EditText et_memo;

    int idx = 0;
    int getyear = 0;
    int getmonth = 0;
    int getday = 0;
    int price = 0;
    String memo = null;

    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_edit);

        MoneyEdit.context = getApplicationContext();
        actionBar = getSupportActionBar();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffD6336B));
        getSupportActionBar().setTitle("댕댕이어리");
        actionBar.setIcon(R.drawable.logo);
        actionBar.setDisplayUseLogoEnabled(true) ;
        actionBar.setDisplayShowHomeEnabled(true) ;

        service = RetrofitClient.getClient().create(ServiceApi.class);

        //moneytab.java 에서 표시할 데이터 가져옴
        final Intent intent = new Intent(getIntent());
        idx = intent.getIntExtra("idx", 0);
        getyear = intent.getIntExtra("year", 0);
        getmonth = intent.getIntExtra("month", 0);
        getday = intent.getIntExtra("day", 0);
        memo = intent.getStringExtra("memo");
        price = intent.getIntExtra("price", 0);


        //날짜 표시
        final String getdate = getyear + "/" + getmonth + "/" + getday;
        tv_date = findViewById(R.id.date_data);
        tv_date.setText(getdate);
        //가격 표시
        et_price = (EditText) findViewById(R.id.price_data);
        et_price.setText(Integer.toString(price));
        //메모 표시
        et_memo = (EditText) findViewById(R.id.memo_data);
        et_memo.setText(memo);

        //취소 버튼
        Button close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //수정 버튼
        final Button edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setBackgroundColor( Color.parseColor("#D6336B"));
                price = Integer.parseInt(et_price.getText().toString());
                memo = et_memo.getText().toString();
                AccountUpdate(new AccountUpdateData(getyear, getmonth, getday, memo, price));
            }
        });

        //삭제 버튼
        final Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.setBackgroundColor( Color.parseColor("#D6336B"));
                DeleteAccount();
                Intent intent_after = new Intent(MoneyEdit.this, MoneyTab.class);
                intent_after.putExtra("after_year", getyear);
                intent_after.putExtra("after_month", getmonth);
                intent_after.putExtra("after_day", getday);
                startActivity(intent_after);
            }
        });
    }

    //항목 수정
    private void AccountUpdate(AccountUpdateData data){
        //토큰 넘겨줌
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sp.getString("TOKEN", "");

        service.accountupdate(token, idx, data).enqueue(new Callback<AccountUpdateResponse>() {
            @Override
            public void onResponse(Call<AccountUpdateResponse> call, Response<AccountUpdateResponse> response) {
                AccountUpdateResponse result = response.body();

                if(result.getSuccess() == true){
                    //수정 성공하면 moneytab 해당 날짜로 돌아감
                    Intent intent_after = new Intent(MoneyEdit.this, MoneyTab.class);
                    intent_after.putExtra("after_year", getyear);
                    intent_after.putExtra("after_month", getmonth);
                    intent_after.putExtra("after_day", getday);
                    startActivity(intent_after);
                }

                Toast.makeText(MoneyEdit.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AccountUpdateResponse> call, Throwable t) {
                Toast.makeText(MoneyEdit.this, "가계부 수정 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("가계부 수정 에러 발생", t.getMessage());
            }
        });
    }

    //항목 삭제
    private void DeleteAccount(){
        //토큰 넘겨줌
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sp.getString("TOKEN", "");

        service.deleteaccount(token, idx).enqueue(new Callback<DeleteAccountResponse>() {
            @Override
            public void onResponse(Call<DeleteAccountResponse> call, Response<DeleteAccountResponse> response) {
                DeleteAccountResponse result = response.body();

                if(result.getSuccess() == true){
                    //삭제 성공하면 moneytab 해당 날짜로 돌아감
                    Intent intent_after = new Intent(MoneyEdit.this, MoneyTab.class);
                    intent_after.putExtra("after_year", getyear);
                    intent_after.putExtra("after_month", getmonth);
                    intent_after.putExtra("after_day", getday);
                    startActivity(intent_after);
                }

                Toast.makeText(MoneyEdit.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DeleteAccountResponse> call, Throwable t) {
                Toast.makeText(MoneyEdit.this, "가계부 삭제 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("가계부 삭제 에러 발생", t.getMessage());
            }
        });
    }

}
