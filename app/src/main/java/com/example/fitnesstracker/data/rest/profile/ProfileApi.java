package com.example.fitnesstracker.data.rest.profile;

import com.example.fitnesstracker.data.rest.dto.UserDto;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ProfileApi {
    @Multipart
    @POST("avatar")
    Single<UserDto> uploadAvatar(@Part MultipartBody.Part photo);

    @POST("profile")
    Single<UserDto> editProfile(@Body EditProfileRequest body);
}
