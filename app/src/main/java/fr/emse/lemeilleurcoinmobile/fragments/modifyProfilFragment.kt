package fr.emse.lemeilleurcoinmobile.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import fr.emse.lemeilleurcoinmobile.MenuActivity
import fr.emse.lemeilleurcoinmobile.R
import fr.emse.lemeilleurcoinmobile.dto.UserDto
import fr.emse.lemeilleurcoinmobile.services.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class modifyProfilFragment(val menuActivity: MenuActivity) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email_edit = view.findViewById<EditText>(R.id.title_text)
        val fullName_edit = view.findViewById<EditText>(R.id.fullname)
        val pass_edit = view.findViewById<EditText>(R.id.password_text)
        val phone_edit = view.findViewById<EditText>(R.id.phone_text)

        var fullName: String
        var new_password: String
        var phone: String

        val buttonSave = view.findViewById<CardView>(R.id.cardSave)

        // get the  current  user information
        val settings = context?.getSharedPreferences("UserInfo", 0)
        val email = settings?.getString("Email", "").toString()
        val old_password = settings?.getString("Password", "").toString()
        new_password = old_password

        //call api to get the UserDTo
        lifecycleScope.launch(context = Dispatchers.IO) {
            runCatching {
                ApiServices().userApiService.findById(email).execute()
            } // call login api with 2 params mail and pass
                .onSuccess {
                    withContext(context = Dispatchers.Main) {
                        fullName = it.body()?.fullName ?: ""
                        phone = it.body()?.phoneNum ?: ""

                        //load this current info into view
                        email_edit.setText(email)
                        pass_edit.setText(new_password)
                        phone_edit.setText(phone)
                        fullName_edit.setText(fullName)
                    }

                }
        }





        // add click listener to the button to save the new user info
        buttonSave.setOnClickListener() {
            fullName = fullName_edit.text.toString()
            new_password = pass_edit.text.toString()
            phone = phone_edit.text.toString()
            if (new_password.length > 7 && fullName.length > 5 && phone.length == 10) {
                // call api to save
                lifecycleScope.launch(context = Dispatchers.IO) {
                    runCatching {
                        ApiServices().userApiService.modify(old_password,UserDto(email,new_password,fullName,phone)).execute()
                    }
                        .onSuccess {
                            withContext(context = Dispatchers.Main) {
                                Toast.makeText(
                                    view.context,
                                    "Information saved !",
                                    Toast.LENGTH_LONG
                                ).show()
                                menuActivity.replaceFragment(4)

                            }
                        }
                        .onFailure {
                            withContext(context = Dispatchers.Main) {
                                Toast.makeText(
                                    view.context,
                                    "Saving failed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }
        }
    }
    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

}