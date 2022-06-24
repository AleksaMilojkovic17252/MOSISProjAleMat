package elfak.mosis.campingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.fragments.*

class ActivityTrip : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener
{

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(this)

        //supportFragmentManager.beginTransaction().replace(R.id.fragment_trip_container,FragmentActivities()).addToBackStack(null).commit()

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_trip_container) as NavHostFragment
        navController = navHostFragment.navController

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            R.id.nav_activities -> {
                when(navController.currentDestination?.id)
                {
                    R.id.fragmentTripTeammates->{
                        navController.navigate(R.id.action_fragmentTripTeammates_to_fragmentActivities)
                    }
                    R.id.fragmentBackpack->{
                        navController.navigate(R.id.action_fragmentBackpack_to_fragmentActivities)
                    }
                    R.id.fragmentMemories->{
                        navController.navigate(R.id.action_fragmentMemories_to_fragmentActivities)
                    }
                    R.id.fragmentTripTeammates2->
                    {
                        navController.navigate(R.id.action_fragmentTripTeammates2_to_fragmentActivities)
                    }
                    else -> {}
                }
            }

            R.id.nav_memories-> {
                when(navController.currentDestination?.id)
                {
                    R.id.fragmentTripTeammates->{
                        navController.navigate(R.id.action_fragmentTripTeammates_to_fragmentMemories)
                    }
                    R.id.fragmentBackpack->{
                        navController.navigate(R.id.action_fragmentBackpack_to_fragmentMemories)
                    }
                    R.id.fragmentActivities->{
                        navController.navigate(R.id.action_fragmentActivities_to_fragmentMemories)
                    }
                    R.id.fragmentTripTeammates2->
                    {
                        navController.navigate(R.id.action_fragmentTripTeammates2_to_fragmentMemories)
                    }
                    else -> {}
                }
            }

            R.id.nav_backpack -> {
                when(navController.currentDestination?.id)
                {
                    R.id.fragmentTripTeammates->{
                        navController.navigate(R.id.action_fragmentTripTeammates_to_fragmentBackpack)
                    }
                    R.id.fragmentMemories->{
                        navController.navigate(R.id.action_fragmentMemories_to_fragmentBackpack)
                    }
                    R.id.fragmentActivities->{
                        navController.navigate(R.id.action_fragmentActivities_to_fragmentBackpack)
                    }
                    R.id.fragmentTripTeammates2->
                    {
                        navController.navigate(R.id.action_fragmentTripTeammates2_to_fragmentBackpack)
                    }
                    else -> {}
                }
            }

            R.id.nav_trip_teammates -> {
                when(navController.currentDestination?.id)
                {
                    R.id.fragmentBackpack->{
                        navController.navigate(R.id.action_fragmentBackpack_to_fragmentTripTeammates)
                    }
                    R.id.fragmentMemories->{
                        navController.navigate(R.id.action_fragmentMemories_to_fragmentTripTeammates)
                    }
                    R.id.fragmentActivities->{
                        navController.navigate(R.id.action_fragmentActivities_to_fragmentTripTeammates)
                    }
                    R.id.fragmentTripTeammates2->
                    {
                        navController.navigate(R.id.action_fragmentTripTeammates2_to_fragmentTripTeammates)
                    }
                    else -> {}
                }
            }
        }

        return true
    }


}