package fr.emse.lemeilleurcoinmobile.dto



data class ViewDto(val id:Long, val description:String , val viewStatus: ViewStatus, val emailViewer: String , val emailViewedUser: String)
