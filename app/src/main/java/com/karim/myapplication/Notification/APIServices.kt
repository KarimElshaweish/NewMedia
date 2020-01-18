package com.karim.myapplication.Notification

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIServices {
    @Headers(
        "Content-Type:application/json",
            "Authorization: key=AAAAqfjGqgc:APA91bGG0fnauyZ0bOjS408mbtl8O1NHSjnUKMhKKSGTAVwoZ-SUguTNkdHbY_VhNYKx9kMy5nHy3H1C1UWgoayxh_wo512R6Un4o8Mtoaw8ptmFfSivYcyW7qraan3dgJ3PsUWqddf5"    )
    @POST("fcm/send")
    fun sendNotificaiton(@Body body: Sender?): Call<MyResponse>

}