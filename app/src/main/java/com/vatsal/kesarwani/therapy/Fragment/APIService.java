package com.vatsal.kesarwani.therapy.Fragment;

import com.vatsal.kesarwani.therapy.Notifications.MyResponse;
import com.vatsal.kesarwani.therapy.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=	AAAAxlNEhZk:APA91bH5KLsYXP_gYy4cQzzCQkB0t_CrlbOk3JFzoHlUDr064HwhNVcDoaZyJbPdRvmX0eRXVfieIybVP8QPz0w4fZW7NqkuZ79RTTKl_ekyVT7mrIRUjMH04NgA9sK0frDtupaABj4T"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifications(@Body Sender body);
}
