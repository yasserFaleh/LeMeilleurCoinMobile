package fr.emse.lemeilleurcoinmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import fr.emse.lemeilleurcoinmobile.dto.UserDto
import fr.emse.lemeilleurcoinmobile.services.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.SharedPreferences




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val settings = getSharedPreferences("UserInfo", 0)
        val s = settings.getString("FullName", "").toString()
        val email = settings.getString("Email", "").toString()
        val pass = settings.getString("Password", "").toString()

        // if the user is already logged we go the menu activity directly
        if ( !"".equals(email) && !"".equals(pass) && !"".equals(s)){
            Toast.makeText(this, "hello $s", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MenuActivity::class.java).apply {}
            startActivity(intent)
        }


    }
    // if the button Login pressed
    fun login(view: View){
        //check the mail and password are valid
        val email = findViewById<EditText>(R.id.email).text.toString()
        val pass = findViewById<EditText>(R.id.pass).text.toString()

        if ( "".equals(email) ){
            Toast.makeText(this, "Please enter your email or Register", Toast.LENGTH_LONG).show()
        }
        else if ( "".equals(pass)){
            Toast.makeText(this, "Please enter your password or Register", Toast.LENGTH_LONG).show()

        }else{
            // calling the api to check the email and password
            lifecycleScope.launch(context = Dispatchers.IO) {
                runCatching {
                    ApiServices().userApiService.login(email,pass).execute() } // call login api with 2 params mail and pass
                    .onSuccess {
                        // the result of the call it the user Dto if everything went well
                        withContext(context = Dispatchers.Main) {
                            val userDto: UserDto? = it.body()

                            if (userDto != null && userDto.email != null) {
                                //saving the user in the system preferences and move to the menu
                                val settings = getSharedPreferences("UserInfo", 0)
                                val editor = settings.edit()
                                editor.putString("Email", email)
                                editor.putString("FullName", userDto.fullName)
                                editor.putString("Password", pass)
                                editor.commit()
                                Toast.makeText(
                                    applicationContext,
                                    "Hello ${userDto.fullName}",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(applicationContext, MenuActivity::class.java).apply {}
                                startActivity(intent)

                            }else {
                                Toast.makeText(
                                    applicationContext,
                                    "Mail or password invalid !",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                    .onFailure {
                        withContext(context = Dispatchers.Main) { // (3)
                            Log.d("api call",it.toString())
                            Toast.makeText(
                                applicationContext,
                                "Unable to reach the server, check your internet !",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }


    }

    // function to go the register activity
    fun register(view: View){
        val intent = Intent(this, RegisterActivity::class.java).apply {}
        startActivity(intent)
    }
}