package com.ktknahmet.final_project.sevices



import com.ktknahmet.final_project.model.loginModel.LoginResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("(default)/documents/userData")
    suspend  fun getUserData(): Response<List<LoginResponse>>
}