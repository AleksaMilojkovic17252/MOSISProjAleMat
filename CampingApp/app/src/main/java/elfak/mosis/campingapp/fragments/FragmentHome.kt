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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityAddTrip
import elfak.mosis.campingapp.activities.ActivityMain
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
        val buttonNotification: ImageView = requireActivity().findViewById(R.id.notification_toolbar)
        val buttonFriend: ImageView = requireActivity().findViewById(R.id.addFriend_toolbar)


        buttonFriend.visibility = View.GONE
        buttonNotification.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        var id = Firebase.auth.currentUser!!.uid
        var name: String = ""
        var occupation: String = ""
        var description: String = ""
        Firebase.firestore.collection("users").document(id).get().addOnSuccessListener {
            name = (it["name"].toString())
            occupation = (it["occupation"].toString())
            description = (it["description"].toString())
        }

        var korisnik:User = User(id,name,occupation,description,"")
        sharedViewModel.korisnik.value = korisnik
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) 
    {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            var i = Intent(context, ActivityAddTrip::class.java)
            startActivity(i)
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