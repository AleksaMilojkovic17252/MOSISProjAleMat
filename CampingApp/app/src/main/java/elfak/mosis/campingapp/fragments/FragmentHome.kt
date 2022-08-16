package elfak.mosis.campingapp.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Tasks
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityAddTrip
import elfak.mosis.campingapp.activities.ActivityMain
import elfak.mosis.campingapp.activities.DrawerLocker
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.classes.Trip
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.databinding.FragmentHomeBinding
import elfak.mosis.campingapp.sharedViews.SharedViewHome
import elfak.mosis.campingapp.sharedViews.SharedViewTripForm
import java.util.*
import kotlin.collections.ArrayList


class FragmentHome : Fragment()
{
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mDrawer: DrawerLayout
    val sharedViewModel: SharedViewHome by activityViewModels()

    private fun ucitajTripove()
    {
        var tmp = Date()
        Firebase.firestore
            .collection(getString(R.string.db_coll_trips))
            .whereArrayContains("userIDs", Firebase.auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                for (doc in it)
                {
                    var drustvo = ArrayList<User>()
                    for (id in doc["userIDs"] as ArrayList<String>)
                        drustvo.add(User(id))


                    var startDate = doc["startDate"] as Timestamp
                    var endDate = doc["endDate"] as Timestamp
                    var trip = Trip(
                        doc["tripName"] as String,
                        doc["longitude"] as Double,
                        doc["latitude"] as Double,
                        drustvo,
                        startDate.toDate(),
                        endDate.toDate(),
                        doc["userItems"] as HashMap<String, ArrayList<BackpackItems>>,
                        ""
                    )
                    sharedViewModel.tripovi.add(trip)
                }
                sharedViewModel.tripovi.sortByDescending { x -> x.endDate }
            }
    }

    override fun onResume()
    {
        super.onResume()
        val title: TextView = requireActivity().findViewById(R.id.toolbar_title)
        title.text = "Camping Trips"
        (activity as DrawerLocker?)!!.setDrawerEnabled(true)
        val navigation: NavigationView = requireActivity().findViewById(R.id.nav_view)
        for (i in 0 until navigation.getMenu().size())
            navigation.getMenu().getItem(i).setChecked(false)
        navigation.menu.findItem(R.id.nav_home).setChecked(true)
        val buttonNotification: ImageView = requireActivity().findViewById(R.id.notification_toolbar)
        val buttonFriend: ImageView = requireActivity().findViewById(R.id.addFriend_toolbar)


        buttonFriend.visibility = View.GONE
        buttonNotification.visibility = View.VISIBLE
        (activity as DrawerLocker?)!!.setDrawerEnabled(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) 
    {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_fragmentAddTripForm2)
        }

        if(Firebase.auth.currentUser?.uid?.isNotEmpty() == true)
        {
            var uidLength = Firebase.auth.currentUser!!.uid.length;
            var stringBuilder = StringBuilder()
            stringBuilder.append(Firebase.auth.currentUser!!.uid.subSequence(0,3))
            stringBuilder.append("...")
            stringBuilder.append(Firebase.auth.currentUser!!.uid.subSequence(uidLength-3, uidLength))
            binding.textViewHomeIDNumber.text = stringBuilder.toString()
        }

        binding.buttonHomeCopyToClipboard.setOnClickListener {
            var clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var clip = ClipData.newPlainText("Copied ID", Firebase.auth.currentUser?.uid)
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(context, "ID copied to clipboard!", Toast.LENGTH_SHORT).show()
        }

        ucitajTripove()
    }


}