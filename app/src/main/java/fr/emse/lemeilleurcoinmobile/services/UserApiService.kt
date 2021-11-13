package fr.emse.lemeilleurcoinmobile.services

import fr.emse.lemeilleurcoinmobile.dto.UserDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {
    @GET("users/check")
    fun check(): Call<List<UserDto>>

    @GET("users/login")
    fun login(@Query("email") mail : String , @Query("pass") pass: String) : Call<UserDto>

    @GET("users/{email}")
    fun findById(@Path("email") id: String): Call<UserDto>

    @GET("users/register")
    fun register(@Query("email") mail : String , @Query("pass") pass: String,@Query("fullName") fullName : String , @Query("phoneNum") phoneNum: String) : Call<Boolean>
}