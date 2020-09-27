package org.techtown.puppydiary.network;

import org.techtown.puppydiary.network.Data.CheckemailData;
import org.techtown.puppydiary.network.Data.FindpwData;
import org.techtown.puppydiary.network.Data.KgupdateData;
import org.techtown.puppydiary.network.Data.RegisterData;
import org.techtown.puppydiary.network.Data.SigninData;
import org.techtown.puppydiary.network.Data.SignupData;
import org.techtown.puppydiary.network.Data.UpdatepwData;
import org.techtown.puppydiary.network.Data.account.AccountUpdateData;
import org.techtown.puppydiary.network.Data.account.InsertAccountData;
import org.techtown.puppydiary.network.Data.calendar.CalendarUpdateData;
import org.techtown.puppydiary.network.Response.CheckemailResponse;
import org.techtown.puppydiary.network.Response.EmailResponse;
import org.techtown.puppydiary.network.Response.FindpwResponse;
import org.techtown.puppydiary.network.Response.KgupdateResponse;
import org.techtown.puppydiary.network.Response.MyinfoResponse;
import org.techtown.puppydiary.network.Response.ProfileResponse;
import org.techtown.puppydiary.network.Response.RegisterResponse;
import org.techtown.puppydiary.network.Response.ShowKgResponse;
import org.techtown.puppydiary.network.Response.SigninResponse;
import org.techtown.puppydiary.network.Response.SignupResponse;
import org.techtown.puppydiary.network.Response.UpdatepwResponse;
import org.techtown.puppydiary.network.Response.account.AccountUpdateResponse;
import org.techtown.puppydiary.network.Response.account.CheckAccountResponse;
import org.techtown.puppydiary.network.Response.account.DeleteAccountResponse;
import org.techtown.puppydiary.network.Response.account.InsertAccountResponse;
import org.techtown.puppydiary.network.Response.account.ShowAccountResponse;
import org.techtown.puppydiary.network.Response.calendar.CalendarPhotoResponse;
import org.techtown.puppydiary.network.Response.calendar.CalendarUpdateResponse;
import org.techtown.puppydiary.network.Response.calendar.ShowDayResponse;
import org.techtown.puppydiary.network.Response.calendar.ShowMonthResponse;
import org.techtown.puppydiary.network.Response.kgDeleteResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ServiceApi {

    @POST("/user/signup") //회원가입
    Call<SignupResponse> usersignup(@Body SignupData data);


    @POST("/user/signin") //로그인
    Call<SigninResponse> usersignin(@Body SigninData data);


    @POST("/user/checkemail") //이메일 중복확인
    Call<CheckemailResponse> checkemail(@Body CheckemailData data);


    @POST("/user/findpw") //비밀번호 찾기
    Call<FindpwResponse> findpw (@Body FindpwData data);


    @POST("/user/updatepw") //비밀번호 업데이트
    Call<UpdatepwResponse> updatepw (@Header("token") String token, @Body UpdatepwData data);


    @GET("/user/getemail") //비밀번호 업데이트
    Call<EmailResponse> getEmail (@Header("token") String token);


    @POST("/mypage/registermyinfo") //강아지 정보 등록/업데이트
    Call<RegisterResponse> registerinfo (@Header("token") String token, @Body RegisterData data);


    @Multipart
    @POST("/user/profile") //프로필 사진 업데이트
    Call<ProfileResponse> profile (@Part MultipartBody.Part profile, @Header("token") String token);


    @GET("/mypage/myinfo") //강아지 정보 조회
    Call<MyinfoResponse> Getmyinfo (@Header("token") String token);


    @GET("/calendar/show/{year}/{month}") //달력 월별 조회
    Call<ShowMonthResponse> showmonth (@Header("token") String token, @Path("year") int year, @Path("month") int month);


    @GET("/calendar/show/{year}/{month}/{date}") //달력 일일 조회
    Call<ShowDayResponse> showday (@Header("token") String token, @Path("year") int year, @Path("month") int month, @Path("date") int date);


    @Multipart
    @POST("/calendar/{year}/{month}/{date}/photo") //달력 사진 업로드
    Call<CalendarPhotoResponse> calendarphoto (@Part MultipartBody.Part profile, @Header("token") String token, @Path("year") int year, @Path("month") int month, @Path("date") int date); //@Body CalendarPhotoData data,


    @POST("/calendar/update") //달력 update
    Call<CalendarUpdateResponse> calendarupdate (@Header("token") String token, @Body CalendarUpdateData data);


    @GET("/kg/show/{year}") //kg 조회
    Call<ShowKgResponse> showkg (@Header("token") String token, @Path("year") int year);


    @POST("/kg/update") //kg update
    Call<KgupdateResponse> kgupdate (@Header("token") String token, @Body KgupdateData data);


    @POST("/kg/delete/{year}/{month}") // KG Delete
    Call<kgDeleteResponse> kgdelete (@Header("token") String token, @Path("year") int year, @Path("month") int month);


    @GET("/account/show/{year}/{month}/{date}") //가계부 조회
    Call<ShowAccountResponse> showaccount (@Header("token") String token, @Path("year") int year, @Path("month") int month, @Path("date") int date);


    @POST("/account/insert") //가계부 아이템 추가
    Call<InsertAccountResponse> insertaccount (@Header("token") String token, @Body InsertAccountData data);


    @GET("/account/check/{year}/{month}/{date}/{item}/{price}") //가계부 아이템 조회 확인(idx)
    Call<CheckAccountResponse> checkaccount (@Header("token") String token, @Path("year") int year, @Path("month") int month, @Path("date") int date, @Path("item") String item, @Path("price") int price);


    @POST("/account/update/{idaccount}") //가계부 아이템 수정
    Call<AccountUpdateResponse> accountupdate (@Header("token") String token, @Path("idaccount") int idaccount, @Body AccountUpdateData data);


    @DELETE("/account/delete/{idaccount}") //가계부 아이템 삭제
    Call<DeleteAccountResponse> deleteaccount (@Header("token") String token, @Path("idaccount") int idaccount);

}