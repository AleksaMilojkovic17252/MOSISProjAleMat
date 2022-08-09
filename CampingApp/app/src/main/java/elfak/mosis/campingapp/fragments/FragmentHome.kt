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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityAddTrip
import elfak.mosis.campingapp.activities.ActivityMain
import elfak.mosis.campingapp.activities.DrawerLocker
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.databinding.FragmentHomeBinding
import elfak.mosis.campingapp.sharedViews.SharedViewHome
import elfak.mosis.campingapp.sharedViews.SharedViewTripForm


class FragmentHome : Fragment()
{
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mDrawer: DrawerLayout
    val sharedViewModel: SharedViewHome by activityViewModels()

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
//        var id = Firebase.auth.currentUser!!.uid
//        var name: String = ""
//        var occupation: String = ""
//        var description: String = ""
//        var drugovi:ArrayList<User> = ArrayList()
//
//
//        var pribavljanjePodataka = Firebase.firestore.collection("users").document(id).get()
//
//        pribavljanjePodataka.addOnSuccessListener {
//            name = (it["name"].toString())
//            occupation = (it["occupation"].toString())
//            description = (it["description"].toString())
//            var prijatelji = it["friends"] as ArrayList<String>
//            for(drug in prijatelji)
//            {
//                drugovi.add(User(drug))
//            }
//        }
//
//        Tasks.whenAll(pribavljanjePodataka).addOnSuccessListener {
//            var korisnik = User(id,name,occupation,description,id,drugovi)
//            sharedViewModel.korisnik.value = korisnik
//        }
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
    }


}