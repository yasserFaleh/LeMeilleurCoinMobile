package fr.emse.lemeilleurcoinmobile.services

import fr.emse.lemeilleurcoinmobile.dto.UserDto
import retrofit2.Call
import retrofit2.http.*

interface UserApiService {


    @GET("login")
    fun login(@Query("email") mail : String , @Query("pass") pass: String) : Call<UserDto>

    @GET("{email}")
    fun findById(@Path("email") id: String): Call<UserDto>

    @POST("modify")
    fun modify(@Query("pass") old_password : String , @Body userDto: UserDto):Call<UserDto>


    @GET("register")
    fun register(@Query("email") mail : String , @Query("pass") pass: String,@Query("fullName") fullName : String , @Query("phoneNum") phoneNum: String) : Call<Boolean>
}