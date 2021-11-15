package fr.emse.lemeilleurcoinmobile.dto

import fr.emse.lemeilleurcoinmobile.model.Category
import java.util.*


data class OfferDto(val id:Long?, val description:String, val price: Double, val date:String?, val title: String, val userEmail: String, val category: Category)
