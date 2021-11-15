package fr.emse.lemeilleurcoinmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.emse.lemeilleurcoinmobile.R
import fr.emse.lemeilleurcoinmobile.dto.OfferDto
import fr.emse.lemeilleurcoinmobile.dto.ProductDto
import fr.emse.lemeilleurcoinmobile.dto.ViewDto

class OfferAdapter : RecyclerView.Adapter<OfferAdapter.ViewHolder>()  {
    private val items = mutableListOf<OfferDto>()

    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        var title: TextView
        var description: TextView
        var email : TextView
        var date : TextView
        var price : TextView

        init {
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
            email = itemView.findViewById(R.id.email)
            date = itemView.findViewById(R.id.date)
            price = itemView.findViewById(R.id.price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.offer_card,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = items[position]
        holder.apply {
            title.text = product.title
            description.text = product.description
            date.text = product.date?.subSequence(0,10)
            email.text = product.userEmail
            price.text = product.price.toString() + " €"
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