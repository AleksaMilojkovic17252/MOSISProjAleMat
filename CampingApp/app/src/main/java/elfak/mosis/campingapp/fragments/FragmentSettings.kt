package elfak.mosis.campingapp.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.databinding.FragmentSettingsBinding

class FragmentSettings: Fragment()
{
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) 
    {
        super.onViewCreated(view, savedInstanceState)
        binding.linearLayoutRecetnTrips.setOnClickListener {
            Toast.makeText(context, "All trips", Toast.LENGTH_SHORT).show()
        }
        binding.linearLayoutRecetnTrips.setOnClickListener {
            // TODO: Napisano ali nije provereno da li radi, deluje da radi, ce probamo sa pravim podacima kasnijeee
            var youSureDialog = AlertDialog.Builder(requireContext())
            youSureDialog
                .setMessage(R.string.delete_all_trips_question)
                .setPositiveButton("Yes") { p0, p1 ->
                    if (p1 == DialogInterface.BUTTON_POSITIVE)
                        deleteAllTrips()
                }
                .setNegativeButton("No") {p0, p1 -> }
                .show()
        }

        binding.switchNotifications.setOnClickListener {
            Toast.makeText(requireContext(), "${binding.switchNotifications.isChecked}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteAllTrips()
    {
        Firebase.firestore
            .collection(getString(R.string.db_coll_trips))
            .whereArrayContains("userIDs", Firebase.auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                for(doc in it)
                {
                    var nizID = doc["userIDs"] as java.util.ArrayList<String>
                    nizID.remove(Firebase.auth.currentUser!!.uid)

                    var nizUser = doc["users"] as java.util.ArrayList<HashMap<String, String>>
                    Log.d("CampingApp", nizUser[0]::class.java.name)
                    var noviNizUser = nizUser.filter { u -> u["id"] != Firebase.auth.currentUser!!.uid}

                    var nizStvari = doc["userItems"] as HashMap<String, ArrayList<BackpackItems>>

                    var noviNizStvari = nizStvari.filter { par -> par.key != Firebase.auth.currentUser!!.uid }
                    Log.d("CampingApp", nizStvari.size.toString())
                    var zaAzuriranje = mapOf(
                        "userIDs" to nizID,
                        "users" to noviNizUser,
                        "userItems" to noviNizStvari
                    )


                    Firebase.firestore
                        .collection(getString(R.string.db_coll_trips))
                        .document(doc.id)
                        .update(zaAzuriranje)


                }
            }

    }

    override fun onResume()
    {
        super.onResume()
        val title: TextView = requireActivity().findViewById(R.id.toolbar_title)
        title.text = getString(R.string.settings_label)

        val buttonNotification: ImageView = requireActivity().findViewById(R.id.notification_toolbar)
        val buttonFriend: ImageView = requireActivity().findViewById(R.id.addFriend_toolbar)
        val navigation: NavigationView = requireActivity().findViewById(R.id.nav_view)
        for (i in 0 until navigation.getMenu().size())
            navigation.getMenu().getItem(i).setChecked(false)
        navigation.menu.findItem(R.id.nav_settings).setChecked(true)

        buttonFriend.visibility = View.GONE
        buttonNotification.visibility = View.GONE
    }
}