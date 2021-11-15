package fr.emse.lemeilleurcoinmobile.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import fr.emse.lemeilleurcoinmobile.MenuActivity
import fr.emse.lemeilleurcoinmobile.R
import fr.emse.lemeilleurcoinmobile.dto.ProductDto
import fr.emse.lemeilleurcoinmobile.dto.ViewDto
import fr.emse.lemeilleurcoinmobile.fragments.OffersAndProductsFragment
import fr.emse.lemeilleurcoinmobile.services.ApiServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OwnProductAdapter(val menuActivity: MenuActivity): RecyclerView.Adapter<OwnProductAdapter.ViewHolder>() {
    private val items = mutableListOf<ProductDto>()

    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
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
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.own_product_card,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = items[position]
        holder.apply {
            title.text = product.title
            description.text = product.description
            date.text = product.date?.subSequence(0,10)
            price.text = product.price.toString() + " €"

            // get the  password because the delete fun needs a password
            val settings = menuActivity.applicationContext.getSharedPreferences("UserInfo", 0)
            val password = settings?.getString("Password", "").toString()

            edit.setOnClickListener() {
                // open the
                //menuActivity.replaceFragment()
            }
            delete.setOnClickListener() {
                CoroutineScope(Dispatchers.IO).launch {
                    runCatching {
                        ApiServices().productApiService.delete(product.id,password).execute()
                    }
                        .onSuccess {
                            withContext(context = Dispatchers.Main) {
                                Toast.makeText(
                                    holder.itemView.context,
                                    "Delete succesfuly!",
                                    Toast.LENGTH_LONG
                                ).show()
                                menuActivity.replaceFragment(OffersAndProductsFragment(menuActivity))
                            }
                        }
                        .onFailure {
                            withContext(context = Dispatchers.Main) {
                                withContext(context = Dispatchers.Main) {
                                    Log.v("yas","$it")
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "$it Delete gone wrong!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                }

            }
        }
    }
    fun update(products: List<ProductDto>) {  // (4)
        items.clear()
        items.addAll(products)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
       return items.size
    }
}