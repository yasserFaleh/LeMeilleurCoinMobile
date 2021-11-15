package fr.emse.lemeilleurcoinmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.emse.lemeilleurcoinmobile.fragments.AddProductFragment
import fr.emse.lemeilleurcoinmobile.fragments.ProfilFragment
import fr.emse.lemeilleurcoinmobile.fragments.SearchFragment

class MenuActivity : AppCompatActivity() {
    private val searchFragment = SearchFragment(this);
    private val addProductFragment = AddProductFragment(this);
    private val profilFragment = ProfilFragment(this);


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        replaceFragment(searchFragment)

        var bottomNavigationView: BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_profil -> replaceFragment(profilFragment)
                R.id.ic_search -> replaceFragment(searchFragment)
                R.id.ic_add    -> replaceFragment(addProductFragment)
            }
            true
        }


    }
    public fun replaceFragment(int : Int){
        if ( int == 1)
            replaceFragment(searchFragment)
        else if ( int == 2)
            replaceFragment(addProductFragment)
        else
            replaceFragment(profilFragment)
    }
    private fun replaceFragment(fragment: Fragment){
        if ( fragment != null ){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()

        }
    }
}