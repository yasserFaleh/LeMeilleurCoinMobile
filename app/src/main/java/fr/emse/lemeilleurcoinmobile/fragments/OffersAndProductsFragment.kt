package fr.emse.lemeilleurcoinmobile.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.emse.lemeilleurcoinmobile.MenuActivity
import fr.emse.lemeilleurcoinmobile.R
import fr.emse.lemeilleurcoinmobile.adapters.OwnOfferAdapter
import fr.emse.lemeilleurcoinmobile.adapters.OwnProductAdapter
import fr.emse.lemeilleurcoinmobile.services.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OffersAndProductsFragment(val menuActivity: MenuActivity) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offers_and_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerViewProducts = view.findViewById<RecyclerView>(R.id.views_recycler_products)
        val productAdapter = OwnProductAdapter(menuActivity)
        val offerAdapter = OwnOfferAdapter(menuActivity)

        // Adjustment of the recyclerview
        recyclerViewProducts.layoutManager = LinearLayoutManager(context)
        recyclerViewProducts.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerViewProducts.setHasFixedSize(true)
        recyclerViewProducts.adapter = productAdapter

        val recyclerViewOffers = view.findViewById<RecyclerView>(R.id.views_recycler_offers)
        // Adjustment of the recyclerview
        recyclerViewOffers.layoutManager = LinearLayoutManager(context)
        recyclerViewOffers.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerViewOffers.setHasFixedSize(true)
        recyclerViewOffers.adapter = offerAdapter

        val settings: SharedPreferences = view.context.getSharedPreferences("UserInfo", 0)
        val email = settings.getString("Email", "").toString()

        lifecycleScope.launch(context = Dispatchers.IO) {
            runCatching {
                ApiServices().offerApiService.getAllOffersByUser(email).execute()
            }
                .onSuccess {
                    withContext(context = Dispatchers.Main) {
                        offerAdapter.update(it.body() ?: emptyList())
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) {
                        Toast.makeText(
                            view.context,
                            "Error on results loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
        lifecycleScope.launch(context = Dispatchers.IO) {
            runCatching {
                ApiServices().productApiService.getAllProductsByUser(email).execute()
            }
                .onSuccess {
                    withContext(context = Dispatchers.Main) {
                        productAdapter.update(it.body() ?: emptyList())
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) {
                        Toast.makeText(
                            view.context,
                            "Error on results loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

    }

}