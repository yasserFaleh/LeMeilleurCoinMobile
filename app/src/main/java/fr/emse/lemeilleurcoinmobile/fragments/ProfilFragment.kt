package fr.emse.lemeilleurcoinmobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.emse.lemeilleurcoinmobile.R
import fr.emse.lemeilleurcoinmobile.adapters.ViewAdapter
import fr.emse.lemeilleurcoinmobile.services.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.SharedPreferences
import android.widget.Toast
import fr.emse.lemeilleurcoinmobile.MenuActivity
import kotlinx.coroutines.withContext


class ProfilFragment(menuActivity: MenuActivity) : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.views_recycler_view)
        val adapter = ViewAdapter()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        val settings: SharedPreferences = view.context.getSharedPreferences("UserInfo", 0)
        val email = settings.getString("Email", "").toString()

        lifecycleScope.launch(context = Dispatchers.IO) {
            runCatching {
                ApiServices().viewApiService.getAllUserViews(email).execute()
            }
                .onSuccess {
                    withContext(context = Dispatchers.Main) {
                        adapter.update(it.body() ?: emptyList())
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            view.context,
                            "Error on windows loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }                }

        }



    }

}