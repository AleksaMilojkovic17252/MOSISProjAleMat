package elfak.mosis.campingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.ActivityTrip
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.services.ServiceNearActivities
import elfak.mosis.campingapp.sharedViews.SharedViewTrip
import java.util.*
import kotlin.collections.ArrayList

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
        shareViewModel.ucitaniSviKorisnici.value = false
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
            var listaMalihTaskova = ArrayList<Task<DocumentSnapshot>>()
            for (id in it["userIDs"] as ArrayList<String>)
            {
                var maliTask = Firebase.firestore
                    .collection(getString(R.string.db_coll_users))
                    .document(id)
                    .get()
                maliTask.addOnSuccessListener { usr ->
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
                listaMalihTaskova.add(maliTask)
            }
            shareViewModel.korisnici = drustvo
            Tasks.whenAll(listaMalihTaskova).addOnSuccessListener {
                shareViewModel.ucitaniSviKorisnici.value = true
            }

            shareViewModel.memories.clear()
            shareViewModel.memories.addAll(it["memories"] as ArrayList<String>)

            var tmpx = it["activities"] as ArrayList<Map<String, Object>>
            for (i in tmpx)
            {
                var novi = ActivityTrip(i["id"].toString(),
                                        i["koJeNapravio"].toString(),
                                        i["title"].toString(),
                                        i["description"].toString(),
                                        i["latitude"].toString().toDouble(),
                                        i["longitude"].toString().toDouble(),
                                        i["type"].toString().toInt())
                shareViewModel.allActivities.add(novi)
            }
            shareViewModel.dataSetChanged.value = !shareViewModel.dataSetChanged.value!!


            var tmp2 = it["completedActivities"] as HashMap<*, *>
            var zavrseneAktivnosti = HashMap<String, ArrayList<String>>()
            for (i in tmp2) {
                var key = i.key.toString()
                var value = i.value
                if (value == null)
                    value = ArrayList<String>()
                zavrseneAktivnosti[key] = value as ArrayList<String>
            }
            shareViewModel.zavrseneAktivnosti = zavrseneAktivnosti


            Toast.makeText(this, "${shareViewModel.memories.count()}", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onResume()
    {
        super.onResume()
        Tasks.whenAll(task).addOnSuccessListener {

            var korisnickePreference = getSharedPreferences("CampingApp", 0)
            if(korisnickePreference?.getBoolean("lokacija", false) == false)
                return@addOnSuccessListener

            var itn = Intent(this, ServiceNearActivities::class.java)
            stopService(itn)


            var intent = Intent(this, ServiceNearActivities::class.java)
            //intent.putExtra("tripID", shareViewModel.tripID.value)
            intent.putExtra("tripN", shareViewModel.tripName.value)
            intent.putExtra("brojA", shareViewModel.allActivities.count())
            intent.putExtra("aID", shareViewModel.allActivities.map { a -> a.ID }.toTypedArray())
            intent.putExtra("aName", shareViewModel.allActivities.map { a -> a.title }.toTypedArray())
            intent.putExtra("aTypes", shareViewModel.allActivities.map { a -> a.type }.toTypedArray() as Array<Int>)
            var tmp = DoubleArray(shareViewModel.allActivities.count()*2)
            var i = 0
            for(a in shareViewModel.allActivities)
            {
                tmp[i++] = a.latitude
                tmp[i++] = a.longitude
            }
            intent.putExtra("aCoor", tmp)
            startService(intent)
        }
    }
}