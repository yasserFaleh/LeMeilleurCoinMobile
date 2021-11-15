package fr.emse.lemeilleurcoinmobile.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.emse.lemeilleurcoinmobile.MenuActivity
import fr.emse.lemeilleurcoinmobile.R
import fr.emse.lemeilleurcoinmobile.adapters.OfferAdapter
import fr.emse.lemeilleurcoinmobile.adapters.ProductAdapter
import fr.emse.lemeilleurcoinmobile.model.Category
import fr.emse.lemeilleurcoinmobile.services.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class Type {OFFER,PRODUCT}

class SearchFragment(menuActivity: MenuActivity) : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var editText = view.findViewById<EditText>(R.id.title_text)
        val recyclerView = view.findViewById<RecyclerView>(R.id.views_recycler_search)
        val productAdapter = ProductAdapter()
        val offerAdapter = OfferAdapter()

        // Adjustment of the recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)


        // radio buttons config
        var radioButton: RadioGroup
        radioButton = view.findViewById(R.id.radio_group)
        radioButton.check(R.id.radio_product)
        radioButton.setOnCheckedChangeListener{group,checkedId ->
            if ( checkedId == R.id.radio_product){
                Toast.makeText(context,"radio product",Toast.LENGTH_LONG)
            }
            if(checkedId == R.id.radio_offer){
                Toast.makeText(context,"radio product",Toast.LENGTH_LONG)
            }
        }
        val spinner: Spinner = view.findViewById(R.id.spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            view.context,
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        val cardView: CardView
        cardView = view.findViewById(R.id.cardSearch)
        cardView.setOnClickListener{
            var category: Category?
            var type :Type
            var description : String

            // checking the fields
            if ( radioButton.checkedRadioButtonId == R.id.radio_offer )
                type = Type.OFFER
            else
                type = Type.PRODUCT

            // get the category
            category = converterCategory(spinner.selectedItem.toString())

            //get the title of the research
            description = editText.text.toString()


            if  ("".equals(description)){
                Toast.makeText(context,"Please enter a description",Toast.LENGTH_LONG).show()
            }else {
                // if is a product research
                if (type == Type.PRODUCT) {
                    recyclerView.adapter = productAdapter
                    showProductResult(view, productAdapter, category, description)
                }
                // if is an offer research
                else {
                    recyclerView.adapter = offerAdapter
                    showOfferResult(view, offerAdapter, category, description)

                }
            }

        }
    }

    private fun showOfferResult(view: View, adapter: OfferAdapter, category: Category?,title: String) {
        lifecycleScope.launch(context = Dispatchers.IO) {
            runCatching {
                ApiServices().offerApiService.getAllOffersByCategoryByTitle(category,title).execute()            }
                .onSuccess {
                    withContext(context = Dispatchers.Main) {
                        adapter.update(it.body() ?: emptyList())
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) {
                        Toast.makeText(
                            view.context,
                            "Error on results loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }                }

        }
    }

    private fun showProductResult(view: View, adapter: ProductAdapter,category: Category?,title: String) {
        lifecycleScope.launch(context = Dispatchers.IO) {
            runCatching {
                ApiServices().productApiService.getAllProductsByCategoryByTitle(category,title).execute()
            }
                .onSuccess {
                    withContext(context = Dispatchers.Main) {
                        Log.v("dd" , it.toString())
                       adapter.update(it.body() ?: emptyList())
                    }
                }
                .onFailure {
                    throw  it
                    withContext(context = Dispatchers.Main) {
                        Toast.makeText(
                            view.context,
                            "Error on results loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }                }

        }
    }

    fun converterCategory(category: String): Category?{
        if ( category.equals("Mode"))
            return Category.MODE
        else if ( category.equals("Immovable"))
            return Category.IMMOVABLE
        else if ( category.equals("Job"))
            return Category.JOB
        else if ( category.equals("Multimedia"))
            return Category.MULTIMEDIA
        else if ( category.equals("Service"))
            return Category.SERVICE
        else if ( category.equals("Vehicles"))
            return Category.VEHICLES
        else if ( category.equals("Other"))
            return Category.OTHER
        else return null

    }

}