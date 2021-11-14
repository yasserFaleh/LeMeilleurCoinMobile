package fr.emse.lemeilleurcoinmobile.dto

import fr.emse.lemeilleurcoinmobile.model.Category
import java.util.*


data class ProductDto(val id:Long, val description:String, val price: Double,val date:Date,val title: String, val emailOwner: String,val category: Category)
