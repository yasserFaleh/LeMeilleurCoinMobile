package fr.emse.lemeilleurcoinmobile.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import fr.emse.lemeilleurcoinmobile.MenuActivity
import fr.emse.lemeilleurcoinmobile.R
import fr.emse.lemeilleurcoinmobile.dto.OfferDto
import fr.emse.lemeilleurcoinmobile.dto.ProductDto
import fr.emse.lemeilleurcoinmobile.model.Category
import fr.emse.lemeilleurcoinmobile.services.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class editProductOrProductFragment(val menuActivity: MenuActivity,val isProduct: Boolean,val id:Long) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_product_or_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("edit fragment ", "$id")
        var title_text = view.findViewById<EditText>(R.id.title_text)
        var desc_text = view.findViewById<EditText>(R.id.description_text)
        var price_text = view.findViewById<EditText>(R.id.price_text)

        //Spinner config
        val spinner: Spinner = view.findViewById(R.id.spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        val arrayAdapter = ArrayAdapter.createFromResource(
            view.context,
            R.array.real_categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        // Getting the Product or the offer details with calling the api
        if ( isProduct)
            lifecycleScope.launch(context = Dispatchers.IO) {
                runCatching {
                    ApiServices().productApiService.get(id).execute()
                }
                    .onSuccess {
                        withContext(context = Dispatchers.Main) {
                            Log.v("yasser3","$it")
                            title_text.setText(it.body()?.title)
                            desc_text.setText(it.body()?.description)
                            price_text.setText(it.body()?.price.toString())
                            spinner.setSelection(arrayAdapter.getPosition(converterCategory(it.body()?.category?:Category.OTHER)))
                        }
                    }
                    .onFailure {
                        withContext(context = Dispatchers.Main) {
                            Toast.makeText(context, "Error finding product", Toast.LENGTH_LONG).show()
                            menuActivity.replaceFragment(9)
                        }
                    }
            }
        // if is an offer
        else
            lifecycleScope.launch(context = Dispatchers.IO) {
                runCatching {
                    ApiServices().offerApiService.get(id).execute()
                }
                    .onSuccess {
                        withContext(context = Dispatchers.Main) {
                            //Toast.makeText(context, "Creating succeed", Toast.LENGTH_LONG).show()
                            title_text.setText(it.body()?.title)
                            desc_text.setText(it.body()?.description)
                            price_text.setText(it.body()?.price.toString())
                            spinner.setSelection(arrayAdapter.getPosition(converterCategory(it.body()?.category?:Category.OTHER)))
                        }
                    }
                    .onFailure {
                        Toast.makeText(context, "Error finding offer", Toast.LENGTH_LONG).show()
                        menuActivity.replaceFragment(9)
                    }
            }

        //add listener to button create
        val cardView = view.findViewById<CardView>(R.id.cardSave)

        cardView.setOnClickListener {
            var category: Category
            var title: String
            var description: String
            var price: Double

            // get the email of the user connected
            val settings = context?.getSharedPreferences("UserInfo", 0)
            val email = settings?.getString("Email", "").toString()
            val password = settings?.getString("Password", "").toString()


            // get the category
            category = converterCategory(spinner.selectedItem.toString())

            //get the title
            title = title_text.text.toString()

            //get the description
            description = desc_text.text.toString()

            // get the price
            if ( ! price_text.text.toString().equals(""))
                price = price_text.text.toString().toDouble()
            else
                price = 0.0

            if ("".equals(description) || "".equals(title) || "".equals(price)  ) {
                Toast.makeText(context, "Please enter all information", Toast.LENGTH_LONG).show()
            }
            else {
                if ( isProduct)
                    //save the product
                    lifecycleScope.launch(context = Dispatchers.IO) {
                        runCatching {
                            Log.v("yas","$password $id $description $price $title $email $category")
                            ApiServices().productApiService.modify(password, ProductDto(id,description,price,null,title,email,category)).execute()
                        }
                            .onSuccess {
                                withContext(context = Dispatchers.Main) {
                                    Toast.makeText(context, "Modified successfully", Toast.LENGTH_LONG).show()
                                    menuActivity.replaceFragment(9)
                                }
                            }
                            .onFailure {
                                Toast.makeText(context, "Error finding offer", Toast.LENGTH_LONG).show()
                                menuActivity.replaceFragment(9)
                            }
                    }
                //save the offer
                else
                    lifecycleScope.launch(context = Dispatchers.IO) {
                        runCatching {
                            ApiServices().offerApiService.modify(password, OfferDto(id,description,price,null,title,email,category)).execute()
                        }
                            .onSuccess {
                                withContext(context = Dispatchers.Main) {
                                    Toast.makeText(context, "Modified successfully", Toast.LENGTH_LONG).show()
                                    menuActivity.replaceFragment(9)

                                }
                            }
                            .onFailure {
                                Toast.makeText(context, "Error finding offer", Toast.LENGTH_LONG).show()
                                menuActivity.replaceFragment(9)
                            }
                    }
            }
        }
        //add listener to button create
        val cardViewCancel = view.findViewById<CardView>(R.id.cardCancel)

        cardViewCancel.setOnClickListener {
            menuActivity.replaceFragment(9)
        }


    }

    fun converterCategory(category: String): Category{
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
        else
            return Category.OTHER
    }
}   fun converterCategory(category: Category): String{
    if ( category.equals(Category.MODE))
        return "Mode"
    else if ( category.equals(Category.IMMOVABLE))
        return "Immovable"
    else if ( category.equals(Category.JOB))
        return "Job"
    else if ( category.equals(Category.MULTIMEDIA))
        return "Multimedia"
    else if ( category.equals(Category.SERVICE))
        return "Service"
    else if ( category.equals(Category.VEHICLES))
        return "Vehicles"
    else
        return "Other"
}