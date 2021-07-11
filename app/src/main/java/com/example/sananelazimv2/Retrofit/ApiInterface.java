package com.example.sananelazimv2.Retrofit;

import com.example.sananelazimv2.Model.AccountModel;
import com.example.sananelazimv2.Model.AccountUpdateModel;
import com.example.sananelazimv2.Model.AddImageModel;
import com.example.sananelazimv2.Model.AdvertisementResultModel;
import com.example.sananelazimv2.Model.CallPhoneModel;
import com.example.sananelazimv2.Model.FavoriIslemModel;
import com.example.sananelazimv2.Model.FavoriListelemeModel;
import com.example.sananelazimv2.Model.FavoriModel;
import com.example.sananelazimv2.Model.IlanBilgiUpdate;
import com.example.sananelazimv2.Model.IlanDetayModel;
import com.example.sananelazimv2.Model.IlanDetaySliderModel;
import com.example.sananelazimv2.Model.IlanResimUpdateModel;
import com.example.sananelazimv2.Model.IlanSilModel;
import com.example.sananelazimv2.Model.IlanlarimModel;
import com.example.sananelazimv2.Model.LoginModel;
import com.example.sananelazimv2.Model.RegisterModel;
import com.example.sananelazimv2.Model.ResimDuzenleSliderModel;
import com.example.sananelazimv2.Model.ResimSilModel;
import com.example.sananelazimv2.Model.TumIlanlarModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("loginuser.php")
    Call<LoginModel> loginuser(@Field("email") String email,
                               @Field("password") String password);

    @FormUrlEncoded
    @POST("registeruser.php")
    Call<RegisterModel> registerUser(@Field("email") String email,
                                     @Field("username") String username,
                                     @Field("phone") String phone,
                                     @Field("password") String password);


    @FormUrlEncoded
    @POST("addadvertisements.php")
    Call<AdvertisementResultModel> advertisement(@Field("memberId") String memberId,
                                                 @Field("title") String title,
                                                 @Field("description") String description,
                                                 @Field("price") String price,
                                                 @Field("category") String category,
                                                 @Field("state") String state);


    @FormUrlEncoded
    @POST("advertisementimage.php")
    Call<AddImageModel> addImage(@Field("memberId") String memberId,
                                 @Field("advertisementId") String advertisementId,
                                 @Field("image") String base64StringImage);


    @GET("tumilanlar.php")
    Call<List<TumIlanlarModel>> tumIlanlar();


    @GET("ilandetay.php")
    Call<IlanDetayModel> ilanDetay(@Query("advertisementId") String advertisementId);


    @GET("ilandetayresim.php")
    Call<List<IlanDetaySliderModel>> ilanDetaySlider(@Query("advertisementId") String advertisementId);

    @GET("favori.php")
    Call<FavoriModel> getButtonText(@Query("member_Id") String memberId, @Query("advertisement_Id") String advertisementId);

    @GET("favoriislemler.php")
    Call<FavoriIslemModel> favoriIslemler(@Query("member_Id") String memberId, @Query("advertisement_Id") String advertisementId);

    @GET("favoriliste.php")
    Call<List<FavoriListelemeModel>> setfavoriListe(@Query("member_Id") String memberId);


    @GET("ilanlarim.php")
    Call<List<IlanlarimModel>> ilanlarim(@Query("memberId") String memberId);


    @GET("ilanupdate.php")
    Call<IlanBilgiUpdate> ilanUpdate(@Query("advertisementId") String advertisementId,
                                     @Query("title") String title,
                                     @Query("description") String description,
                                     @Query("price") String price);


    @FormUrlEncoded
    @POST("ilanresimupdate.php")
    Call<IlanResimUpdateModel> ilanResimUpdate(@Field("memberId") String memberId,
                                               @Field("advertisementId") String advertisementId,
                                               @Field("image") String base64StringImage);

    @GET("ilansil.php")
    Call<IlanSilModel> ilansil(@Query("advertisementId") String advertisementId);

    @GET("resimduzenleSlider.php")
    Call<List<ResimDuzenleSliderModel>> resimduzenleSlider(@Query("advertisementId") String advertisementId);

    @GET("resimSil.php")
    Call<ResimSilModel> resimsil(@Query("id") String id);


    @GET("user.php")
    Call<AccountModel> account(@Query("memberId") String memberId);


    @GET("userupdate.php")
    Call<AccountUpdateModel> accountUpdate(@Query("memberId") String memberId,
                                           @Query("username") String username,
                                           @Query("password") String password,
                                           @Query("email") String email,
                                           @Query("phone") String phone);



    @GET("callOther.php")
    Call<CallPhoneModel> callNumber(@Query("otherId") String otherId);

}
