package org.techtown.puppydiary;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;

import org.techtown.puppydiary.calendarmenu.CalendarTab;
import org.techtown.puppydiary.network.Data.RegisterData;
import org.techtown.puppydiary.network.Response.MyinfoResponse;
import org.techtown.puppydiary.network.Response.ProfileResponse;
import org.techtown.puppydiary.network.Response.RegisterResponse;
import org.techtown.puppydiary.network.RetrofitClient;
import org.techtown.puppydiary.network.ServiceApi;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.techtown.puppydiary.Signup.set_flag;

public class SetPuppy extends AppCompatActivity {
    private  static final int REQUEST_CODE = 0;
    de.hdodenhof.circleimageview.CircleImageView imageView;
    ActionBar actionBar;
    private ServiceApi service;
    Button button;
    String mediaPath;
    Uri selectedImage;
    DatePickerDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpuppy);

        actionBar = getSupportActionBar();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffD6336B));
        getSupportActionBar().setTitle("댕댕이어리");
        actionBar.setIcon(R.drawable.logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        //갤러리 접근 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        TextView textView = findViewById(R.id.textView);
        SpannableString content = new SpannableString("우리 집 댕댕이는요");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);

        Calendar cal = Calendar.getInstance();
        final TextView b_day = findViewById(R.id.bd_input);

        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        dialog = new DatePickerDialog(
                SetPuppy.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = year + "/ " + month + "/ " + day;
                        b_day.setText(date);
                    }
                }, year, month, day);


        b_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });


        imageView = findViewById(R.id.profile);
        imageView.setImageResource(R.drawable.profile_default);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        final EditText puppy_name = findViewById(R.id.name_input);
        final EditText age_ = findViewById(R.id.age_input);
        final RadioButton option_male = (RadioButton) findViewById(R.id.male);
        final RadioButton option_female = (RadioButton) findViewById(R.id.female);


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
                        String requestURL = myinfo1.getImage();
                        Glide.with(SetPuppy.this).load(requestURL).into(imageView);

                        puppy_name.setText(myinfo1.getPuppyname());
                        age_.setText("" + myinfo1.getAge());
                        if (age_.getText().equals("0")){
                            age_.setText(null);
                        }

                        b_day.setText(myinfo1.getBirth());

                        int gender = myinfo1.getGender();
                        if ( gender ==1 ){
                            option_male.setChecked(true);
                        }
                        else if ( gender == 2 ) {
                            option_female.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyinfoResponse> call, Throwable t) {
                Log.e("실패", "했지롱");
            }
        });

        button = (Button) findViewById(R.id.finish_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setBackgroundColor( Color.parseColor("#D6336B"));


                if( !(puppy_name.getText().equals(""))) {
                    String puppyname = puppy_name.getText().toString();
                    Integer age = Integer.parseInt("" + age_.getText());
                    String birth = b_day.getText().toString();

                    int gender = 0; // 1이 남자, 2가 여자
                    if (option_male.isChecked() && (!option_female.isChecked())) {
                        gender = 1;
                    } else if ((!option_male.isChecked()) && option_female.isChecked()) {
                        gender = 2;
                    }

                    UpdatePhoto();
                    infoInputCheck(new RegisterData(puppyname, age, birth, gender));
                }
                else {
                    Toast.makeText(getApplicationContext(), "인자가 입력되지 않았습니다", Toast.LENGTH_LONG).show();
                }
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

            imageView.setImageBitmap(bitmap);

            Cursor cursor = getContentResolver().query(Uri.parse(selectedImage.toString()), null, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));

        }else{
            Toast.makeText(this, "사진 업로드에 실패하였습니다.", Toast.LENGTH_LONG).show();
        }
    }

    private void infoInputCheck(final RegisterData data){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sp.getString("TOKEN", "");
        service.registerinfo(token, data).enqueue(new Callback<RegisterResponse>() {

            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse result = response.body();

                if(result.getMessage().equals("강아지 정보 등록에 성공하였습니다.")){
                    if(set_flag == 0){
                        Intent intent_Calendar = new Intent(getApplicationContext(), CalendarTab.class);
                        startActivity(intent_Calendar);
                    }else{
                        Intent intent_mypuppy = new Intent(getApplicationContext(), MypuppyTab.class);
                        startActivity(intent_mypuppy);
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(SetPuppy.this, "강아지 정보 등록 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void UpdatePhoto() {
        if(mediaPath != null){
        File file = new File(mediaPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profile", file.getName(), requestBody);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sp.getString("TOKEN", "");

        service.profile(fileToUpload, token).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(SetPuppy.this, "통신 실패 에러입니다.", Toast.LENGTH_SHORT).show();
                Log.d("에러",  t.getMessage());
            }
        });
    }}
}