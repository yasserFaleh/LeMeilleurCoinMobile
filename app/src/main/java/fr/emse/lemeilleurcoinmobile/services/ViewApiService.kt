package fr.emse.lemeilleurcoinmobile.services

import fr.emse.lemeilleurcoinmobile.dto.UserDto
import fr.emse.lemeilleurcoinmobile.dto.ViewDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ViewApiService {
    @GET("{email}")
    fun getAllUserViews(@Path("email") email: String ): Call<List<ViewDto>>
}