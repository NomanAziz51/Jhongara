package com.propertyeager.realestate.Retrofit;


import com.propertyeager.realestate.Wrapper.GetADEditImages.GetADEditImagesWrapper;
import com.propertyeager.realestate.Wrapper.GetADEditText.GetADEditTextWrapper;
import com.propertyeager.realestate.Wrapper.GetAdDetails.GetAdDetailWrapper;
import com.propertyeager.realestate.Wrapper.GetAdUpload.GetAdUploadWrapper;
import com.propertyeager.realestate.Wrapper.GetAdslistWrapper.GetAdsListWrapper;
import com.propertyeager.realestate.Wrapper.GetAgentList.GetAgentListWrapper;
import com.propertyeager.realestate.Wrapper.GetAgentLogin.GetAgentLoginWrapper;
import com.propertyeager.realestate.Wrapper.GetAgentProfileData.GetAgentProfileDataWrapper;
import com.propertyeager.realestate.Wrapper.GetAgentProfileImage.GetAgentprofileImageWrapper;
import com.propertyeager.realestate.Wrapper.GetAgentSignup.GetAgentSignupWrapper;
import com.propertyeager.realestate.Wrapper.GetBookingFormWrapper;
import com.propertyeager.realestate.Wrapper.GetCustomertFormDetailWrapper;
import com.propertyeager.realestate.Wrapper.GetEditCustomertFormDetailWrapper;
import com.propertyeager.realestate.Wrapper.GetLeaderBoard.GetLeaderBoardWrapper;
import com.propertyeager.realestate.Wrapper.GetLogoutWrapper;
import com.propertyeager.realestate.Wrapper.GetPaymentFormDetailWrapper;
import com.propertyeager.realestate.Wrapper.GetProfileEdit.GetProfileEditWrapper;
import com.propertyeager.realestate.Wrapper.GetTokenStatusWrapper;
import com.propertyeager.realestate.Wrapper.GetTokenUpdateWrapper;
import com.propertyeager.realestate.Wrapper.GetUserDetailWrapper;
import com.propertyeager.realestate.Wrapper.GetUserLogin.GetUserLoginWrapper;
import com.propertyeager.realestate.Wrapper.GetUserSignup.GetUserSignupWrapper;
import com.propertyeager.realestate.Wrapper.GetVideoAdList.GetVideoAdListWrapper;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface WebService {


    @FormUrlEncoded
    @POST("search_ads.php")
    Call<GetAdsListWrapper> GET_ADS_LIST_CALL(
            @Field("city") String city,
            @Field("property_type") String property_type,
            @Field("property_on") String property_on);

    @POST("agents_list.php")
    Call<GetAgentListWrapper> GET_AGENT_LIST_WRAPPER_CALL();

    @FormUrlEncoded
    @POST("detail_ad.php")
    Call<GetAdDetailWrapper> GET_AD_DETAIL_WRAPPER_CALL(
            @Field("id") String id);

    @FormUrlEncoded
    @POST("catagories.php")
    Call<GetAdsListWrapper> GET_CATAGORY_ADS_WRAPPER_CALL(
            @Field("catagory") String city);

    @FormUrlEncoded
    @POST("agent_portfolio.php")
    Call<GetAdsListWrapper> GET_AGENT_ADS_LIST_WRAPPER_CALL(
            @Field("id") String id);

    @FormUrlEncoded
    @POST("user_signup.php")
    Call<GetUserSignupWrapper> userSignUp(
            @Field("Name") String name,
            @Field("Number") String number,
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("user_login.php")
    Call<GetUserLoginWrapper> getUserLogin(
            @Field("email") String rows,
            @Field("password") String password);


    @Multipart
    @POST("agent_signup.php")
    Call<GetAgentSignupWrapper> GET_AGENT_SIGNUP_WRAPPER_CALL(
            @Part("Name") RequestBody Name,
            @Part("Number") RequestBody Number,
            @Part("Email") RequestBody Email,
            @Part("Password") RequestBody Password,
            @Part("Agency_name") RequestBody Agency_name,
            @Part("discription") RequestBody discription,
            //
            @Part MultipartBody.Part Logo);

    @FormUrlEncoded
    @POST("agent_login.php")
    Call<GetAgentLoginWrapper> GET_AGENT_LOGIN_WRAPPER_CALL(
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("profile.php")
    Call<GetAgentProfileDataWrapper> GET_AGENT_PROFILE_DATA_WRAPPER_CALL(
            @Field("id") String id,
            @Field("type") String type);

    @POST("video_ad.php")
    Call<GetVideoAdListWrapper> GET_VIDEO_AD_LIST_WRAPPER_CALL();


    @FormUrlEncoded
    @POST("profile_edit.php")
    Call<GetProfileEditWrapper> GET_AGENT_EDIT_PROFILE_DATA_WRAPPER_CALL(
            @Field("id") String id,
            @Field("type") String type,
            @Field("Name") String Name,
            @Field("Number") String Number,
            @Field("Agency_name") String Agency_name,
            @Field("discription") String discription,
            @Field("Password") String Password);

    @FormUrlEncoded
    @POST("profile_edit.php")
    Call<GetProfileEditWrapper> GET_CLIENT_EDIT_PROFILE_DATA_WRAPPER_CALL(
            @Field("id") String id,
            @Field("type") String type,
            @Field("Name") String Name,
            @Field("Number") String Number,
            @Field("Password") String Password);

    @Multipart
    @POST("profile_edit.php")
    Call<GetProfileEditWrapper> agent_profile_edit_pic(
            @Part("id") RequestBody id,
            @Part("type") RequestBody type,
            @Part MultipartBody.Part agent_picture);



    @Multipart
    @POST("edit_ad.php")
    Call<GetADEditImagesWrapper> AD_EDIT_IMAGES_WRAPPER_CALL(
            @Part("ad_id") RequestBody ad_id,
            @Part("img_position") RequestBody img_position,
            @Part MultipartBody.Part img);

    @FormUrlEncoded
    @POST("profile_edit.php")
    Call<GetAgentprofileImageWrapper> client_profile(
            @Path("id") String agent_id,
            @Part MultipartBody.Part agent_picture,
            @Part("old_agent_picture") RequestBody old_agent_picture);




    @FormUrlEncoded
    @POST("login/send_worklog.php")
    Call<Object> SendWorklog(
            @Field("emp_id") String id,
            @Field("emp_name") String name,
            @Field("description") String email,
            @Field("date") String phonenumber,
            @Field("month") String password,
            @Field("year") String year);



    @FormUrlEncoded
    @POST("auth/login")
    Call<GetUserDetailWrapper> GetUserDetail(
            @Field("email") String email,
            @Field("password") String password);


    @FormUrlEncoded
    @POST("auth/logout")
    Call<GetLogoutWrapper> GetLogoutDetail(
           // @Header("Authorization") String auth,
           @Field("id") String agent_id,
           @Field("token") String token);

    @FormUrlEncoded
    @POST("my_uploaded_ads.php")
    Call<GetAdsListWrapper> GET_MY_ADS_LIST_WRAPPER_CALL(
            // @Header("Authorization") String auth,
            @Field("id") String id,
            @Field("type") String type);



    @FormUrlEncoded
    @POST("edit_ad.php")
    Call<GetADEditTextWrapper> GET_AD_EDIT_TEXT_WRAPPER_CALL(
            @Field("ad_id") String ad_id,

            @Field("title") String title,
            @Field("price") String price,
            @Field("address") String address,
            @Field("stories") String stories,
            @Field("rooms") String rooms,
            @Field("bath") String bath,
            @Field("area") String area,
            @Field("City") String City,
            @Field("property_type") String property_type,
            @Field("property_on") String property_on,
            @Field("Discription") String Discription,
            @Field("Link") String Link,
            @Field("amenities") String amenities,
            @Field("nearby") String nearby
    );




    @Multipart
    @POST("ad_upload.php")
    Call<GetAdUploadWrapper> GET_AD_UPLOAD_WRAPPER_CALL(
            @Part("uploder_type") RequestBody uploder_type,
            @Part("uploder_id") RequestBody uploder_id,
            @Part("title") RequestBody title,
            //

            //  @Part("image") RequestBody fa_image,
            //
            @Part("property_on") RequestBody property_on,
            @Part("price") RequestBody price,
            @Part("address") RequestBody address,
            @Part("stories") RequestBody stories,
            @Part("rooms") RequestBody rooms,
            @Part("bath") RequestBody bath,
            @Part("area") RequestBody area,
            @Part("City") RequestBody City,
            @Part("Discription") RequestBody Discription,
            @Part("Link") RequestBody Link,
            @Part("property_type") RequestBody property_type,
            @Part("amenities") RequestBody amenities,
            @Part("nearby") RequestBody nearby,


            @Part MultipartBody.Part image1_file,
            @Part MultipartBody.Part image2_file,
            @Part MultipartBody.Part image3_file,
            @Part MultipartBody.Part image4_file,
            @Part MultipartBody.Part image5_file,
            @Part MultipartBody.Part image6_file,
            @Part MultipartBody.Part floorplan_file
            );




    @Multipart
    @POST("customerform/store")
    Call<GetBookingFormWrapper> GetBookingFormDetail(
            @Part("customer_id") RequestBody customer_id,
            @Part("direct_booking") RequestBody direct_booking,
            @Part("name") RequestBody name,
            //
            @Part MultipartBody.Part fa_image_file,
            //  @Part("image") RequestBody fa_image,
            //
            @Part("email") RequestBody email,
            @Part("son_of") RequestBody son_of,
            @Part("cnic") RequestBody cnic,
            @Part("mobileno") RequestBody mobileno,
            @Part("phoneno") RequestBody phoneno,
            @Part("officeno") RequestBody officeno,
            @Part("date_of_birth") RequestBody date_of_birth,
            @Part("profession") RequestBody profession,
            @Part("passport_no") RequestBody passport_no,
            @Part("present_address") RequestBody present_address,
            @Part("present_address_city") RequestBody present_address_city,
            @Part("present_address_country") RequestBody present_address_country,
            @Part("alternate_address") RequestBody alternate_address,
            @Part("alternate_address_city") RequestBody alternate_address_city,
            @Part("alternate_address_country") RequestBody alternate_address_country,
            //
            @Part("nok_name") RequestBody nok_name,
            @Part("nok_son_of") RequestBody nok_son_of,
            @Part("nok_relationship") RequestBody nok_relationship,
            @Part("nok_cnic") RequestBody nok_cnic,
            @Part("nok_passport_no") RequestBody nok_passport_no,
            //
            @Part("ja_name") RequestBody ja_name,
            //
            @Part MultipartBody.Part ja_image_file,
            // @Part("ja_image") RequestBody ja_image,
            //
            @Part("ja_son_of") RequestBody ja_son_of,
            @Part("ja_email") RequestBody ja_email,
            @Part("ja_cnic") RequestBody ja_cnic,
            @Part("ja_mobileno") RequestBody ja_mobileno,
            @Part("ja_phoneno") RequestBody ja_phoneno,
            @Part("ja_officeno") RequestBody ja_officeno,
            @Part("ja_passport_no") RequestBody ja_passport_no,
            @Part("ja_present_address") RequestBody ja_present_address,
            @Part("ja_presentaddress_city") RequestBody ja_presentaddress_city,
            @Part("ja_presentaddress_country") RequestBody ja_presentaddress_country,
            @Part("ja_alternate_address") RequestBody ja_alternate_address,
            @Part("ja_alternateaddress_city") RequestBody ja_alternateaddress_city,
            @Part("ja_alternateaddress_country") RequestBody ja_alternateaddress_country,
            //
            @Part("form_no") RequestBody form_no,
            @Part("agent_id") RequestBody agent_id);


    @Multipart
    @POST("customerform/add_paymentinformation")
    Call<GetPaymentFormDetailWrapper> GetPaymentForm(
            @Part("customer_form_id") RequestBody customer_form_id,
            @Part("customer_id") RequestBody customer_id,
            @Part("applied_for") RequestBody applied_for,
            @Part("payment_procedure") RequestBody payment_procedure,
            @Part("floor") RequestBody floor,
            @Part("area") RequestBody area,
            @Part("total_amount") RequestBody total_amount,
            @Part("down_payment") RequestBody down_payment,
            @Part("bank_name") RequestBody bank_name,
            @Part("branch") RequestBody branch,
            @Part("witness_name1") RequestBody witness_name1,
            @Part("witness_name2") RequestBody witness_name2,

            //
            @Part MultipartBody.Part applicant_signature_img_file,
            // @Part("applicant_signature_img") RequestBody applicant_signature_img_name,
            //
            @Part MultipartBody.Part as_cnic_front_img_file,
            // @Part("as_cnic_front_img") RequestBody as_cnic_front_img_name,
            //
            @Part MultipartBody.Part as_cnic_back_img_file,
            // @Part("as_cnic_back_img") RequestBody as_cnic_back_img_name,
            //
            @Part MultipartBody.Part joint_applicant_signature_img_file,
            // @Part("joint_applicant_signature_img") RequestBody joint_applicant_signature_img_name,

            @Part MultipartBody.Part jas_cnic_front_img_file,
            //@Part("jas_cnic_front_img") RequestBody jas_cnic_front_img_name,

            @Part MultipartBody.Part jas_cnic_back_img_file,
            //@Part("jas_cnic_back_img") RequestBody jas_cnic_back_img_name,

            @Part MultipartBody.Part witness1_cnic_front_img_file,
            //@Part("witness1_cnic_front_img") RequestBody witness1_cnic_front_img_name,

            @Part MultipartBody.Part witness1_cnic_back_img_name,
            // @Part("witness1_cnic_back_img") RequestBody witness1_cnic_back_img,

            @Part MultipartBody.Part witness2_cnic_front_img_file,
            //@Part("witness2_cnic_front_img") RequestBody witness2_cnic_front_img_name,

            @Part MultipartBody.Part witness2_cnic_back_img_name);
    // @Part("witness2_cnic_back_img") RequestBody witness2_cnic_back_img_file);

   /* @Multipart
    @POST("profile_edit.php")
    Call<GetProfileEditWrapper> agent_profile_img(
            @Path("id") String agent_id,
            @Part MultipartBody.Part agent_picture,
            @Part("old_agent_picture") RequestBody old_agent_picture);*/



    @FormUrlEncoded
    @POST("customerform/addcustomer")
    Call<GetCustomertFormDetailWrapper> GetCustomerForm(
            @Field("agent_id") String agent_id,
            @Field("branch_id") String branch_id,
            @Field("name") String name,
            @Field("dealing_status") String dealing_status,
            @Field("followup_datetime") String followup_datetime,
            @Field("phoneno") String phoneno,
            @Field("mobileno") String mobileno,
            @Field("reason_of_notinterested") String reason_of_notinterested,
            @Field("interested_in") String interested_in,
            @Field("present_address") String present_address,
            @Field("present_address_city") String present_address_city,
            @Field("present_address_country") String present_address_country);



    @GET("customerform/token_validity")
    Call<GetTokenStatusWrapper> GetTokenStatus();

    @FormUrlEncoded
    @POST("delete_ad.php")
    Call<ResponseBody> DeleteFormitem(
            @Field("id") String id);
            //@Header("access_token") String access_token

    @FormUrlEncoded
    @POST("customerform/updatecustomer/{id}")
    Call<GetEditCustomertFormDetailWrapper> GetEditCustomer(
            @Path("id") String cumstomer_form_id,
          //  @Header("access_token") String access_token,
            @Field("name") String name,
            @Field("dealing_status") String dealing_status,
            @Field("followup_datetime") String followup_datetime,
            @Field("phoneno") String phoneno,
            @Field("mobileno") String mobileno,
            @Field("reason_of_notinterested") String reason_of_notinterested,
            @Field("interested_in") String interested_in,
            @Field("present_address") String present_address,
            @Field("present_address_city") String present_address_city,
            @Field("present_address_country") String present_address_country);
 /*   @FormUrlEncoded
    @POST("login/get_user_worklist.php")
    Call<WorklogWrapper> GetWorkDetail(
            @Field("emp_id") String rows,
            @Field("month") String month);

    @FormUrlEncoded
    @POST("login/get_employee_monthly_report.php")
    Call<DailyWorkWrapper> GetMonthlyWorkDetail(
            @Field("emp_id") String rows,
            @Field("month") String month);

    @FormUrlEncoded
    @POST("login/get_daily_worklist.php")
    Call<DailyWorkWrapper> GetDailyWorkDetail(
//            @Field("emp_id") String rows,
            @Field("date") String month);

    @FormUrlEncoded
    @POST("login/get_employee_list.php")
    Call<EmployeesWrapper> GetEmployeeList(
            @Field("") String rows);*/

    @FormUrlEncoded
    @POST("login/send_notification_to_all.php")
    Call<Object> SendNotifications(
            @Field("") String rows);


    @FormUrlEncoded
    @POST("login/guestresetpassword.php")
    Call<Object> ForgotPassword(
            @Field("email") String email
    );


    @FormUrlEncoded
    @POST("login/changepassword.php")
    Call<Object> ChangePassword(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login/guest_token_update.php")
    Call<Object> Admin_Token(
            @Field("id") String code,
            @Field("fcm") String Token,
            @Field("app_version") String app_version,
            @Field("last_active") String guest_last_active);

    @FormUrlEncoded
    @POST("login/employee_token_update.php")
    Call<Object> Employee_Token(
            @Field("id") String code,
            @Field("fcm") String Token,
            @Field("app_version") String app_version,
            @Field("last_active_time") String guest_last_active);

    @POST("customerform/fcmtoken_update/{id}")
    Call<GetTokenUpdateWrapper> Agent_Token(
            @Path("id") String agent_id);

  /*  @POST("auth/me")
    Call<GetAgentProfileDataWrapper> agent_profile(
            @Query("token") String token);*/

    @GET("customerform/top_sallers/{id}")
    Call<GetLeaderBoardWrapper> GetLeaderBoard(
            /* @Header("Authorization") String access_token,*/ @Path("id") String agent_id);

}