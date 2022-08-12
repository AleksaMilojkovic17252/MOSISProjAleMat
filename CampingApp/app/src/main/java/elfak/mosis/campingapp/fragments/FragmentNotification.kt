package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.DrawerLocker
import elfak.mosis.campingapp.adapters.AdapterNotifications
import elfak.mosis.campingapp.classes.Notifications
import elfak.mosis.campingapp.classes.NotificationsFriend
import elfak.mosis.campingapp.classes.NotificationsTrip
import elfak.mosis.campingapp.databinding.FragmentNotificationBinding
import elfak.mosis.campingapp.sharedViews.SharedViewHome

class FragmentNotification : Fragment()
{
    lateinit var binding: FragmentNotificationBinding
    lateinit var recycler: RecyclerView
    private val shareViewModel : SharedViewHome by activityViewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        recycler = binding.recyclerViewNotification

        var taskovi = ArrayList<Task<QuerySnapshot>>()
        var nizNotifikacija = arrayListOf<Notifications>()

        var taskPribavljanjeZahteva = Firebase.firestore
            .collection(getString(R.string.db_coll_req))
            .whereEqualTo("to", Firebase.auth.currentUser!!.uid)
            .whereEqualTo("processed", true)
            .get()
        taskPribavljanjeZahteva.addOnSuccessListener {
            for (doc in it)
            {
                var koSalje = doc["fromName"] as String
                var koSaljeID = doc["from"] as String
                nizNotifikacija.add(NotificationsFriend(koSalje, koSaljeID))
            }
        }
        taskovi.add(taskPribavljanjeZahteva)

        var taskPribavljanjeTripova = Firebase.firestore
            .collection(getString(R.string.db_coll_newTrips))
            .whereEqualTo("processed", true)
            .whereArrayContains("userIDs", Firebase.auth.currentUser!!.uid)
            .get()
        taskPribavljanjeTripova.addOnSuccessListener {
            for(doc in it)
            {
                nizNotifikacija.add(NotificationsTrip(doc["tripName"] as String))
            }
        }
        taskovi.add(taskPribavljanjeTripova)

        Tasks.whenAll(taskovi).addOnSuccessListener {
            //val notifs: ArrayList<Notifications> = arrayListOf(NotificationsFriend("Pavle","12312321321"), NotificationsTrip("PLANINA SUVA LUDA"), NotificationsFriend("Marko", "12321321213"))
            shareViewModel.liveNotifikacije.value = nizNotifikacija
//            val notifs = shareViewModel.liveNotifikacije.value
            val notifs = nizNotifikacija

            val NotificationsAdapter: AdapterNotifications = AdapterNotifications(requireContext(),notifs!!)

            recycler.adapter = NotificationsAdapter
            recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            var posmatrac = Observer<ArrayList<Notifications>>
            { t ->
                //Log.d("CampingApp", "USO U OBSERVER")
                //Log.d("CampingApp",t?.reduce{ i, j -> "$i $j" } ?: "Mrk")
                //binding.textView10.text = t?.reduce{ i, j -> "$i $j" }
                // TODO: RECYCLEVIEW JEDAN DA STAVLJA NOTIFIKACIJE
                NotificationsAdapter.notifyDataSetChanged()

            }
            shareViewModel.liveNotifikacije.observe(viewLifecycleOwner,posmatrac)
        }


    }

    override fun onResume()
    {
        super.onResume()

        val title: TextView = requireActivity().findViewById(R.id.toolbar_title)
        title.text = getString(R.string.notification_fragment_title)
        (activity as DrawerLocker?)!!.setDrawerEnabled(true)

        val buttonNotification: ImageView = requireActivity().findViewById(R.id.notification_toolbar)
        val buttonFriend: ImageView = requireActivity().findViewById(R.id.addFriend_toolbar)

        val navigation: NavigationView = requireActivity().findViewById(R.id.nav_view)
        for (i in 0 until navigation.getMenu().size())
            navigation.getMenu().getItem(i).setChecked(false)
        navigation.menu.findItem(R.id.nav_notifications).setChecked(true)

        buttonFriend.visibility = View.GONE
        buttonNotification.visibility = View.VISIBLE

    }
}