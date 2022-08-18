package elfak.mosis.campingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.sharedViews.SharedViewTrip
import java.util.HashMap

class ActivityTrip : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener
{

    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    private val shareViewModel: SharedViewTrip by viewModels()
    private lateinit var task: Task<DocumentSnapshot>
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

        var tripID = intent.extras?.getString("tripId")
        if(tripID != null)
        {
            shareViewModel.tripID.value = tripID
        }
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

    override fun onStart()
    {
        super.onStart()
        ucitajTrip()
    }

    private fun ucitajTrip()
    {
        task = Firebase.firestore
            .collection(getString(R.string.db_coll_trips))
            .document(shareViewModel.tripID.value!!)
            .get()

        task.addOnSuccessListener { it ->
            shareViewModel.tripName.value = it["tripName"] as String
            shareViewModel.startDate.value = (it["startDate"] as Timestamp).toDate()
            shareViewModel.endDate.value = (it["endDate"] as Timestamp).toDate()
            shareViewModel.latitude.value = it["latitude"] as Double
            shareViewModel.longitude.value = it["longitude"] as Double
            //shareViewModel.backpackItems = it["userItems"] as HashMap<String, ArrayList<BackpackItems>>


            var tmp = it["userItems"]
            var pomoc: HashMap<String, ArrayList<BackpackItems>> = HashMap()
            for(id in it["userItems"] as HashMap<String,ArrayList<HashMap<String,Object>>>)
            {
              var anotherHelp: ArrayList<BackpackItems> = ArrayList()
              for(help in id.value)
               {
                  anotherHelp.add(BackpackItems(help["name"].toString(),help["items"].toString().toInt()))
               }
                pomoc[id.key]=anotherHelp
            }
            shareViewModel.backpackItems = pomoc

            var drustvo = ArrayList<User>()
            for (id in it["userIDs"] as ArrayList<String>)
            {
                Firebase.firestore
                    .collection(getString(R.string.db_coll_users))
                    .document(id)
                    .get()
                    .addOnSuccessListener { usr ->
                        var tmpUser = User(
                            id,
                            usr["name"] as String,
                            usr["occupation"] as String,
                            usr["description"] as String,
                            id,
                            ArrayList<User>()
                        )
                        drustvo.add(tmpUser)
                    }
            }
            shareViewModel.korisnici = drustvo
        }

    }
}