package elfak.mosis.campingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.fragments.*

class ActivityTrip : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_trip_container,FragmentActivities()).addToBackStack(null).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var selectedFragment: Fragment = FragmentActivities()
        when(item.itemId){
            R.id.nav_activities -> {

                selectedFragment = FragmentActivities()
            }
            R.id.nav_memories-> {

                selectedFragment = FragmentMemories()
            }
            R.id.nav_backpack -> {

                selectedFragment = FragmentBackpack()
            }
            R.id.nav_trip_teammates -> {

                selectedFragment = FragmentTripTeammates()
            }

        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_trip_container,selectedFragment).addToBackStack(null).commit()
        return true
    }


}