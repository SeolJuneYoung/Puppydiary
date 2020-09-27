package org.techtown.puppydiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.techtown.puppydiary.accountmenu.MoneyTab;
import org.techtown.puppydiary.calendarmenu.CalendarTab;
import org.techtown.puppydiary.kgmenu.KgTab;
import org.techtown.puppydiary.network.Response.MyinfoResponse;
import org.techtown.puppydiary.network.RetrofitClient;
import org.techtown.puppydiary.network.ServiceApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.techtown.puppydiary.Signup.set_flag;

public class MypuppyTab extends AppCompatActivity {
    ActionBar actionBar;
    private ServiceApi service;
    de.hdodenhof.circleimageview.CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypuppy);

        actionBar = getSupportActionBar();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffD6336B));
        getSupportActionBar().setTitle("댕댕이어리");
        actionBar.setIcon(R.drawable.logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        service = RetrofitClient.getClient().create(ServiceApi.class);


        TextView textView = findViewById(R.id.textView);
        SpannableString content = new SpannableString("우리 집 댕댕이는요");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);

        Button calen = findViewById(R.id.calendar);
        calen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_calendar = new Intent(getApplicationContext(), CalendarTab.class);
                startActivity(intent_calendar);
            }
        });

        Button kg = findViewById(R.id.kg);
        kg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_kg = new Intent(getApplicationContext(), KgTab.class);
                startActivity(intent_kg);
            }
        });

        Button money = findViewById(R.id.account);
        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_money = new Intent(getApplicationContext(), MoneyTab.class);
                startActivity(intent_money);
            }
        });

        Button puppy = findViewById(R.id.puppy);
        puppy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_puppy = new Intent(getApplicationContext(), MypuppyTab.class);
                startActivity(intent_puppy);
            }
        });


        // 설정아이콘 누르면 설정으로 넘어가게
        final ImageView set_button = findViewById(R.id.set_button);
        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_flag = 1;
                Intent intent = new Intent(getApplicationContext(), SetPuppy.class);
                startActivity(intent);
            }
        });

        // 사람 아이콘 누르면 비밀번호 재설정으로 넘어가게
        ImageView person = findViewById(R.id.pwd_set);
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Pwd.class);
                startActivity(intent);
            }
        });

        final TextView puppy_name = findViewById(R.id.name_input);
        final TextView age_ = findViewById(R.id.age_input);
        final TextView birth_ = findViewById(R.id.bd_input);
        final RadioButton option_male = (RadioButton) findViewById(R.id.male);
        final RadioButton option_female = (RadioButton) findViewById(R.id.female);
        imageView = findViewById(R.id.profile);
        imageView.setImageResource(R.drawable.profile_default);

        //토큰 넘겨줌
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sp.getString("TOKEN", "");

        final Call<MyinfoResponse> getCall = service.Getmyinfo(token);
        getCall.enqueue(new Callback<MyinfoResponse>() {
            @Override
            public void onResponse(Call<MyinfoResponse> call, Response<MyinfoResponse> response) {
                if(response.isSuccessful()){
                    MyinfoResponse myinfo = response.body();
                    List<MyinfoResponse.Myinfo> my = myinfo.getData();

                    for(MyinfoResponse.Myinfo myinfo1 : my) {

                        String requestURL = my.get(0).getImage();
                        Glide.with(MypuppyTab.this).load(requestURL).into(imageView);

                        //이름 설정
                        if( myinfo1.getPuppyname().equals("") ){
                            puppy_name.setText("댕댕이");
                        }
                        else {
                            puppy_name.setText(myinfo1.getPuppyname());
                        }
                        age_.setText("" + myinfo1.getAge());

                        //나이 설정
                        if(myinfo1.getBirth().equals("")){
                            birth_.setText("몇살인가요?");
                        }
                        else {
                            birth_.setText(myinfo1.getBirth());
                        }

                        //성별 설정
                        int gender = myinfo1.getGender();
                        if ( gender ==1 ){
                            option_male.setChecked(true);
                        }
                        else if ( gender == 2) {
                            option_female.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyinfoResponse> call, Throwable t) {

            }
        });
    }
}