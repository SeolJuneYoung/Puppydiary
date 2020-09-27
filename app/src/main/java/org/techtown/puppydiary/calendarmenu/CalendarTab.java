package org.techtown.puppydiary.calendarmenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.puppydiary.MypuppyTab;
import org.techtown.puppydiary.R;
import org.techtown.puppydiary.accountmenu.MoneyTab;
import org.techtown.puppydiary.kgmenu.KgTab;
import org.techtown.puppydiary.network.Data.calendar.ShowDayData;
import org.techtown.puppydiary.network.Response.calendar.ShowDayResponse;
import org.techtown.puppydiary.network.Response.calendar.ShowMonthResponse;
import org.techtown.puppydiary.network.RetrofitClient;
import org.techtown.puppydiary.network.ServiceApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarTab extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ActionBar actionBar;

    int year = 0;
    int month = 0;
    int date = 0;
    int state_waterdrop;
    int state_injection;
    int showmonth_pos;

    //연 월 텍스트뷰
    private TextView tvDate;
    //그리드뷰
    private GridView gridView;
    //그리드뷰 어댑터
    private GridAdapter gridAdapter;
    //day 저장 리스트
    private ArrayList<DayInfo> dayList;

    Calendar mCal;

    Button pvs_button;
    Button nxt_button;

    int lastMonthStartDay;
    int dayOfMonth;
    int thisMonthLastDay;

    private ServiceApi service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        actionBar = getSupportActionBar();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffD6336B));
        getSupportActionBar().setTitle("댕댕이어리");
        actionBar.setIcon(R.drawable.logo);
        actionBar.setDisplayUseLogoEnabled(true) ;
        actionBar.setDisplayShowHomeEnabled(true) ;


        Button cal = findViewById(R.id.calendar);
        cal.setOnClickListener(new View.OnClickListener() {
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

        Button money = findViewById(R.id.account1);
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


        tvDate = findViewById(R.id.tv_date);
        gridView = findViewById(R.id.gridview);

        pvs_button = findViewById(R.id.previous);
        nxt_button = findViewById(R.id.next);

        pvs_button.setOnClickListener(this);
        nxt_button.setOnClickListener(this);

        gridView.setOnItemClickListener(this);

        dayList = new ArrayList<>();

        service = RetrofitClient.getClient().create(ServiceApi.class);

        mCal = Calendar.getInstance();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mCal);

    }

    //캘린더 구현
    private void getCalendar(Calendar mCal) {

        dayList.clear();

        // 이번달 시작일의 요일 구하기
        dayOfMonth = mCal.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        mCal.add(Calendar.MONTH, -1);

        // 지난달의 마지막 일자 구하기
        lastMonthStartDay = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        mCal.add(Calendar.MONTH, 1);

        lastMonthStartDay -= (dayOfMonth - 1) - 1;

        // 캘린더 타이틀(년월 표시)을 세팅
        tvDate.setText((mCal.get(Calendar.YEAR))+ ". " + (mCal.get(Calendar.MONTH)+1) + "월월!");

        year = mCal.get(Calendar.YEAR);

        switch (mCal.get(Calendar.MONTH)){
            case 0: {month = 0; break;}
            case 1: {month = 1; break;}
            case 2: {month = 2; break;}
            case 3: {month = 3; break;}
            case 4: {month = 4; break;}
            case 5: {month = 5; break;}
            case 6: {month = 6; break;}
            case 7: {month = 7; break;}
            case 8: {month = 8; break;}
            case 9: {month = 9; break;}
            case 10: {month = 10; break;}
            case 11: {month = 11; break;}
        }


        DayInfo day;

        for (int i = 0; i < dayOfMonth - 1; i++) {
            int date = lastMonthStartDay + i;
            day = new DayInfo();
            day.setDay(Integer.toString(date));
            day.setInMonth(false);
            dayList.add(day);
        }
        for (int i = 1; i <= thisMonthLastDay; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(true);
            dayList.add(day);
        }
        for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(false);
            dayList.add(day);
        }

        gridAdapter = new GridAdapter(getApplicationContext(), R.layout.item_calendar, dayList);
        gridView.setAdapter(gridAdapter);

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //날짜 클릭 - calendardetail
        DayInfo day = dayList.get(position);
        date = position - dayOfMonth + 2;

        if(day.isInMonth()) {
            ShowDay();
        }

    }

    //지난달, 다음달 구현
    public void onClick(View v) {
        if (v.getId()==R.id.previous){
            mCal.set(mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), 1);
            mCal.add(Calendar.MONTH, -1);
            tvDate.setText((mCal.get(Calendar.MONTH)+1) + "월");
            getCalendar(mCal);
        } else if (v.getId()==R.id.next) {
            mCal.set(mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), 1);
            mCal.add(Calendar.MONTH, +1);
            tvDate.setText((mCal.get(Calendar.MONTH)+1) + "월");
            getCalendar(mCal);
        }
    }


    // 그리드뷰 어댑터
    public class GridAdapter extends BaseAdapter {

        private ArrayList<DayInfo> mdayList;
        private Context mcontext;
        private int mresource;
        private LayoutInflater minflater;

        public GridAdapter(Context context, int textResource, ArrayList<DayInfo> dayList) {
            this.mcontext = context;
            this.mdayList = dayList;
            this.mresource = textResource;
            this.minflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mdayList.size();
        }

        @Override
        public Object getItem(int position) { return mdayList.get(showmonth_pos); }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            DayInfo day = dayList.get(position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = minflater.inflate(mresource, null);

                //달력 레이아웃 크기 설정
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int screenW = metrics.widthPixels;
                int screenH = metrics.heightPixels;
                convertView.setLayoutParams(new GridView.LayoutParams(screenW/7 + screenW%7, screenH/8));

                holder = new ViewHolder();
                holder.tvItem = (TextView) convertView.findViewById(R.id.tv_item_gridview);
                holder.waterdrop = (ImageView) convertView.findViewById(R.id.waterdrop);
                holder.injection = (ImageView) convertView.findViewById(R.id.injection);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //기본세팅 - 색깔 없음
            holder.waterdrop.setImageResource(R.drawable.waterdrop);
            holder.injection.setImageResource(R.drawable.injection);


            /*
            서버에서 month 정보 받아옴
            */
            final ViewHolder finalHolder = holder;
            //토큰 넘겨줌
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String token = sp.getString("TOKEN", "");
            service.showmonth(token, year, month).enqueue(new Callback<ShowMonthResponse>() {
                @Override
                public void onResponse(Call<ShowMonthResponse> call, Response<ShowMonthResponse> response) {

                    if (response.isSuccessful()) {
                        ShowMonthResponse showmonth = response.body();
                        List<ShowMonthResponse.ShowMonth> my = showmonth.getData();

                        if (my != null) {

                            for (int i = 0; i < my.size(); i++) {
                                //물방울, 주사기 아이콘 색깔 변경
                                showmonth_pos = my.get(i).getDate();
                                state_waterdrop = my.get(i).getWater();
                                state_injection = my.get(i).getInject();

                                if(showmonth_pos == (position-dayOfMonth+2)){
                                    if (state_waterdrop == 0 && state_injection == 0) {
                                        finalHolder.waterdrop.setImageResource(R.drawable.waterdrop);
                                        finalHolder.injection.setImageResource(R.drawable.injection);
                                    } else if (state_waterdrop == 1 && state_injection == 0) {
                                        //물방울만
                                        finalHolder.waterdrop.setImageResource(R.drawable.waterdrop_color);
                                        finalHolder.injection.setImageResource(R.drawable.injection);
                                    } else if (state_waterdrop == 0 && state_injection == 1) {
                                        //주사기만
                                        finalHolder.waterdrop.setImageResource(R.drawable.waterdrop);
                                        finalHolder.injection.setImageResource(R.drawable.injection_color);
                                    } else if (state_waterdrop == 1 && state_injection == 1) {
                                        //물방울, 주사기 둘 다
                                        finalHolder.waterdrop.setImageResource(R.drawable.waterdrop_color);
                                        finalHolder.injection.setImageResource(R.drawable.injection_color);
                                    }
                                }

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ShowMonthResponse> call, Throwable t) {
                    Toast.makeText(CalendarTab.this, "getcall 에러 발생", Toast.LENGTH_SHORT).show();
                    Log.e("getcall 에러 발생", t.getMessage());
                }
            });


            if (day != null) {
                holder.tvItem.setText(day.getDay());
                if (day.isInMonth()) {
                    if (position % 7 == 0) {
                        //일요일
                        holder.tvItem.setTextColor(getColor(R.color.setRed));
                    } else {
                        holder.tvItem.setTextColor(Color.GRAY);
                    }

                } else {
                    //해당 월에 포함되지 않는 날짜 invisible
                    holder.tvItem.setTextColor(Color.TRANSPARENT);
                    holder.waterdrop.setVisibility(View.INVISIBLE);
                    holder.injection.setVisibility(View.INVISIBLE);
                }
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView waterdrop;
            public ImageView injection;
            public TextView tvItem;
        }

    }


    //하루 정보 확인하고 저장
    public class DayInfo {
        private String day;
        private boolean inMonth;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        } //날짜저장

        public boolean isInMonth() {
            return inMonth;
        } //이번 달 날짜인지 확인

        public void setInMonth(boolean inMonth) {
            this.inMonth = inMonth;
        } //정보저장
    }


    //아이템 클릭 - 서버에서 하루 정보 가져옴
    private void ShowDay(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sp.getString("TOKEN", "");
        service.showday(token, year, month, date).enqueue(new Callback<ShowDayResponse>() {
            @Override
            public void onResponse(Call<ShowDayResponse> call, Response<ShowDayResponse> response) {
                //클릭한 년, 월, 일 보내줌
                Intent intent_day = new Intent(getApplicationContext(), CalendarDetail.class);
                intent_day.putExtra("year", year);
                intent_day.putExtra("month", month);
                intent_day.putExtra("date", date);
                startActivityForResult(intent_day, 2000);
            }

            @Override
            public void onFailure(Call<ShowDayResponse> call, Throwable t) {
                Toast.makeText(CalendarTab.this, "달력 일일 조회 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("달력 일일 조회 에러 발생", t.getMessage());
            }
        });
    }
}
