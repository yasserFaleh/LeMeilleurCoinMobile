package fr.emse.lemeilleurcoinmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import fr.emse.lemeilleurcoinmobile.MenuActivity
import fr.emse.lemeilleurcoinmobile.R
import fr.emse.lemeilleurcoinmobile.dto.OfferDto
import fr.emse.lemeilleurcoinmobile.dto.ProductDto
import fr.emse.lemeilleurcoinmobile.dto.ViewDto
import fr.emse.lemeilleurcoinmobile.fragments.OffersAndProductsFragment
import fr.emse.lemeilleurcoinmobile.fragments.editProductOrProductFragment
import fr.emse.lemeilleurcoinmobile.services.ApiServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OwnOfferAdapter(val menuActivity: MenuActivity) : RecyclerView.Adapter<OwnOfferAdapter.ViewHolder>()  {
    private val items = mutableListOf<OfferDto>()

    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        var id:Long = 0
        var title: TextView
        var description: TextView
        var date : TextView
        var price : TextView
        var edit : Button
        var delete : Button

        init {
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
            date = itemView.findViewById(R.id.date)
            price = itemView.findViewById(R.id.price)
            edit = itemView.findViewById(R.id.edit)
            delete = itemView.findViewById(R.id.delete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.own_offer_card,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val offer = items[position]
        holder.apply {
            id = offer.id!!
            title.text = offer.title
            description.text = offer.description
            date.text = offer.date?.subSequence(0, 10)
            price.text = offer.price.toString() + " €"

            // get the  password because the delete fun needs a password
            val settings = menuActivity.applicationContext.getSharedPreferences("UserInfo", 0)
            val password = settings?.getString("Password", "").toString()

            edit.setOnClickListener() {
                // open the
                menuActivity.replaceFragment(editProductOrProductFragment(menuActivity,false,id))
            }
            delete.setOnClickListener() {
                CoroutineScope(Dispatchers.IO).launch {
                    runCatching {
                        ApiServices().offerApiService.delete(offer.id,password).execute()
                    }
                        .onSuccess {
                            withContext(context = Dispatchers.Main) {
                                menuActivity.replaceFragment(OffersAndProductsFragment(menuActivity))
                            }
                        }
                        .onFailure {

                            }
                        }

            }
        }

    }
    fun update(offers: List<OfferDto>) {  // (4)
        items.clear()
        items.addAll(offers)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return items.size
    }
}