package fr.emse.lemeilleurcoinmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import fr.emse.lemeilleurcoinmobile.services.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    val EMAIL_PARAM = "EMAIL"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
    fun register(view: View){
        //check the fields are valid
        val email = findViewById<EditText>(R.id.email).text.toString()
        val pass = findViewById<EditText>(R.id.pass).text.toString()
        val phoneNum = findViewById<EditText>(R.id.phone).text.toString()
        val fullName = findViewById<EditText>(R.id.fullname).text.toString()

        if ( "".equals(email))
            Toast.makeText(this, "Please enter your Email", Toast.LENGTH_LONG).show()
        else if ( "".equals(pass) || pass.length < 8)
            Toast.makeText(this, "Please enter a valid password : more than 8 digits", Toast.LENGTH_LONG).show()
        else if ( "".equals(phoneNum) || phoneNum.length != 10 )
            Toast.makeText(this, "Please enter  a valid 10 digits Phone number", Toast.LENGTH_LONG).show()
        else if ( "".equals(fullName) || fullName.length < 8)
            Toast.makeText(this, "Please enter a more than 8 digits Full Name", Toast.LENGTH_LONG).show()
        else{
            // call api for signing up
            lifecycleScope.launch(context = Dispatchers.IO) {
                runCatching { ApiServices().userApiService.register(email,pass,fullName,phoneNum).execute() } // call login api with 2 params mail and pass
                    .onSuccess {
                        if ( it.body().toString().equals("true") ){

                            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                                putExtra(EMAIL_PARAM, email)
                            }
                            startActivity(intent)
                            //Toast.makeText(applicationContext,"Signing up succesfully !" , Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(applicationContext,"Failure, something gone wrong, try later !" , Toast.LENGTH_LONG).show()

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
}