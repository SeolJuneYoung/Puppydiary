package org.techtown.puppydiary.calendarmenu;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import org.techtown.puppydiary.R;
import org.techtown.puppydiary.network.Data.calendar.CalendarUpdateData;
import org.techtown.puppydiary.network.Response.calendar.CalendarPhotoResponse;
import org.techtown.puppydiary.network.Response.calendar.CalendarUpdateResponse;
import org.techtown.puppydiary.network.Response.calendar.ShowDayResponse;
import org.techtown.puppydiary.network.RetrofitClient;
import org.techtown.puppydiary.network.ServiceApi;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarDetail extends AppCompatActivity {
    int IMAGE_FROM_GALLERY = 0;
    private static final int REQUEST_CODE = 0;
    Uri selectedImage;
    int year = 0;
    int month = 0;
    int date = 0;
    int state_waterdrop = 0;
    int state_injection = 0;
    String memo;
    String photo;

    ImageView image_upload;

    ActionBar actionBar;
    Button waterdrop_btn;
    Button waterdrop_btn2;
    Button injection_btn;
    Button injection_btn2;
    Button cancel_btn;
    Button save_btn;
    EditText memo_et;

    TextView tv_date;
    private ServiceApi service;

    String mediaPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_calendar_detail);

        actionBar = getSupportActionBar();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffD6336B));
        getSupportActionBar().setTitle("댕댕이어리");
        actionBar.setIcon(R.drawable.logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        final Intent intent = new Intent(getIntent());
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        date = intent.getIntExtra("date", 0);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        tv_date = (TextView) findViewById(R.id.tv_date);
        waterdrop_btn = findViewById(R.id.waterdrop_detail);
        waterdrop_btn2 = findViewById(R.id.waterdrop_color);
        injection_btn = findViewById(R.id.injection_detail);
        injection_btn2 = findViewById(R.id.injection_color);

        cancel_btn = findViewById(R.id.btn_canceldetail);
        save_btn = findViewById(R.id.btn_savedetail);

        memo_et = (EditText) findViewById(R.id.edittext_memo);

        image_upload = (ImageView) findViewById(R.id.image_upload);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

        //기본세팅
        waterdrop_btn2.setVisibility(View.INVISIBLE);
        waterdrop_btn.setVisibility(View.VISIBLE);
        injection_btn2.setVisibility(View.INVISIBLE);
        injection_btn.setVisibility(View.VISIBLE);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sp.getString("TOKEN", "");
        Call<ShowDayResponse> showday = service.showday(token, year, month, date);
        showday.enqueue(new Callback<ShowDayResponse>() {
            @Override
            public void onResponse(Call<ShowDayResponse> call, Response<ShowDayResponse> response) {
                if (response.isSuccessful()) {
                    ShowDayResponse showday = response.body();
                    List<ShowDayResponse.ShowDay> my = showday.getData();
                    if (my != null) {
                        if (my.get(0).getMemo() != null) {
                            memo = my.get(0).getMemo();
                            memo_et.setText(memo);
                        }
                        if (my.get(0).getPhoto() != null) {
                            String requestURL = my.get(0).getPhoto();
                            Glide.with(CalendarDetail.this).load(requestURL).into(image_upload);
                        }
                        state_waterdrop = my.get(0).getWater();
                        state_injection = my.get(0).getInject();
                        if (state_waterdrop == 1 && state_injection == 0) {
                            // 물방울만 색깔 있을 때
                            waterdrop_btn2.setVisibility(View.VISIBLE);
                            waterdrop_btn.setVisibility(View.INVISIBLE);
                            injection_btn2.setVisibility(View.INVISIBLE);
                            injection_btn.setVisibility(View.VISIBLE);
                        } else if (state_waterdrop == 0 && state_injection == 1) {
                            // 주사기만 색깔 있을 때
                            waterdrop_btn2.setVisibility(View.INVISIBLE);
                            waterdrop_btn.setVisibility(View.VISIBLE);
                            injection_btn2.setVisibility(View.VISIBLE);
                            injection_btn.setVisibility(View.INVISIBLE);
                        } else if (state_waterdrop == 1 && state_injection == 1) {
                            waterdrop_btn2.setVisibility(View.VISIBLE);
                            waterdrop_btn.setVisibility(View.INVISIBLE);
                            injection_btn2.setVisibility(View.VISIBLE);
                            injection_btn.setVisibility(View.INVISIBLE);
                        } else if (state_waterdrop == 0 && state_injection == 0) {
                            waterdrop_btn2.setVisibility(View.INVISIBLE);
                            waterdrop_btn.setVisibility(View.VISIBLE);
                            injection_btn2.setVisibility(View.INVISIBLE);
                            injection_btn.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ShowDayResponse> call, Throwable t) {
                Toast.makeText(CalendarDetail.this, "getcall 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("getcall 에러 발생", t.getMessage());
            }
        });

        tv_date.setText(year + ". " + (month + 1) + ". " + date);


        // on
        waterdrop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterdrop_btn2.setVisibility(View.VISIBLE);
                waterdrop_btn.setVisibility(View.INVISIBLE);
                state_waterdrop = 1;
            }
        });

        // off
        waterdrop_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterdrop_btn.setVisibility(View.VISIBLE);
                waterdrop_btn2.setVisibility(View.INVISIBLE);
                state_waterdrop = 0;
            }
        });

        // on
        injection_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                injection_btn2.setVisibility(View.VISIBLE);
                injection_btn.setVisibility(View.INVISIBLE);
                state_injection = 1;
            }
        });

        // off
        injection_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                injection_btn.setVisibility(View.VISIBLE);
                injection_btn2.setVisibility(View.INVISIBLE);
                state_injection = 0;
            }
        });

        memo_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_btn.setBackgroundColor(Color.parseColor("#D6336B"));
                memo = memo_et.getText().toString();
                CalendarUpdate(new CalendarUpdateData(year, month, date, memo, state_injection, state_waterdrop));
                UpdatePhoto();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_btn.setBackgroundColor(Color.parseColor("#D6336B"));
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQUEST_CODE && resultCode==RESULT_OK && data!=null) {
            selectedImage = data.getData();
            Uri photoUri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),photoUri);
                bitmap = rotateImage(bitmap, 90);
            } catch (IOException e) {
                e.printStackTrace();
            }

            image_upload.setImageBitmap(bitmap);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Cursor cursor = getContentResolver().query(Uri.parse(selectedImage.toString()), null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            Log.d("경로 확인 >> ", "$selectedImg  /  $absolutePath");

        }else{
            Toast.makeText(this, "사진 업로드 실패", Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void UpdatePhoto() {
        if(mediaPath != null) {
            File file = new File(mediaPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profile", file.getName(), requestBody);

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String token = sp.getString("TOKEN", "");

            service.calendarphoto(fileToUpload, token, year, month, date).enqueue(new Callback<CalendarPhotoResponse>() {
                @Override
                public void onResponse(Call<CalendarPhotoResponse> call, Response<CalendarPhotoResponse> response) {
                    CalendarPhotoResponse result = response.body();
                }

                @Override
                public void onFailure(Call<CalendarPhotoResponse> call, Throwable t) {
                    Toast.makeText(CalendarDetail.this, "통신 실패요우아아아아아악!!!", Toast.LENGTH_SHORT).show();
                    Log.d("에러", t.getMessage());
                }
            });
        }
    }

    private void CalendarUpdate(CalendarUpdateData data) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sp.getString("TOKEN", "");

        service.calendarupdate(token, data).enqueue(new Callback<CalendarUpdateResponse>() {
            @Override
            public void onResponse(Call<CalendarUpdateResponse> call, Response<CalendarUpdateResponse> response) {
                CalendarUpdateResponse result = response.body();
                if (result.getSuccess() == true) {
                    Intent intent_month = new Intent(getApplicationContext(), CalendarTab.class);
                    intent_month.putExtra("after_year", year);
                    intent_month.putExtra("after_month", month);
                    startActivityForResult(intent_month, 2000);
                    Toast.makeText(CalendarDetail.this, "달력 업데이트 성공", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<CalendarUpdateResponse> call, Throwable t) {
                Toast.makeText(CalendarDetail.this, "달력 업데이트 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("달력 업데이트 에러 발생", t.getMessage());
            }
        });
    }

}