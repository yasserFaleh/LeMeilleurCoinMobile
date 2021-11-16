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
import fr.emse.lemeilleurcoinmobile.MenuActivity
import fr.emse.lemeilleurcoinmobile.R
import fr.emse.lemeilleurcoinmobile.dto.OfferDto
import fr.emse.lemeilleurcoinmobile.dto.ProductDto
import fr.emse.lemeilleurcoinmobile.model.Category
import fr.emse.lemeilleurcoinmobile.services.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddProductFragment(val menuActivity: MenuActivity) : Fragment() {
    enum class Type {OFFER,PRODUCT}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var title_text = view.findViewById<EditText>(R.id.title_text)
        var desc_text = view.findViewById<EditText>(R.id.description_text)
        var price_text = view.findViewById<EditText>(R.id.price_text)


        var type: Type



        // radio buttons config
        var radioButton: RadioGroup
        radioButton = view.findViewById(R.id.radio_group)
        radioButton.check(R.id.radio_product)
        radioButton.setOnCheckedChangeListener{group,checkedId ->
            if ( checkedId == R.id.radio_product){
                type = Type.PRODUCT
            }
            if(checkedId == R.id.radio_offer){
                type = Type.OFFER
            }
        }

        //Spinner config
        val spinner: Spinner = view.findViewById(R.id.spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            view.context,
            R.array.real_categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        //add listener to button create
        val cardView: CardView
        cardView = view.findViewById(R.id.cardSave)
        cardView.setOnClickListener {
            var category: Category
            var type: Type
            var title: String
            var description: String
            var price: Double

            // get the email of the user connected
            val settings = context?.getSharedPreferences("UserInfo", 0)
            val email = settings?.getString("Email", "").toString()


            // checking the fields
            if (radioButton.checkedRadioButtonId == R.id.radio_offer)
                type = Type.OFFER
            else
                type = Type.PRODUCT

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
                if ( type == Type.PRODUCT)
                    createProduct(title,description,category,price,email,menuActivity)

                else
                    createOffer(title,description,category,price,email,menuActivity)

            }
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

    fun createProduct(title: String,description: String,category: Category,price: Double,email: String,menuActivity: MenuActivity){
        lifecycleScope.launch(context = Dispatchers.IO) {
            runCatching {
                Log.v("yasser","$description $price $title $email $category")
                ApiServices().productApiService.create(ProductDto(null,description,price,null,title,email,category))
                    .execute()
            }
                .onSuccess {
                    Log.v("yasser2",it.message())
                    withContext(context = Dispatchers.Main) {
                        Toast.makeText(context, "Creating succeed", Toast.LENGTH_LONG).show()
                        menuActivity.replaceFragment(9)

                    }
                }
                .onFailure {
                    Log.v("yasser3",it.localizedMessage)
                }
        }
    }
    fun createOffer(title: String,description: String,category: Category,price: Double,email: String,menuActivity: MenuActivity){
        lifecycleScope.launch(context = Dispatchers.IO) {
            runCatching {
                Log.v("yasser","$description $price $title $email $category")
                ApiServices().offerApiService.create(OfferDto(null,description,price,null,title,email,category))
                    .execute()
            }
                .onSuccess {
                    withContext(context = Dispatchers.Main) {
                        Toast.makeText(context, "Creating succeed", Toast.LENGTH_LONG).show()
                        menuActivity.replaceFragment(9)

                    }
                }
                .onFailure {
                }
        }
    }



}