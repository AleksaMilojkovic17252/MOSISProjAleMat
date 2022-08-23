package elfak.mosis.campingapp.fragments

import android.content.DialogInterface
import android.content.Intent
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
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.DrawerLocker
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.databinding.FragmentSettingsBinding
import elfak.mosis.campingapp.services.ServiceNotificationSpamFirestore
import elfak.mosis.campingapp.services.ServiceSendLocation

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

        var korisnickePreference = activity?.getSharedPreferences("CampingApp", 0)
        binding.switchNotifications.setOnClickListener {

            var editor = korisnickePreference?.edit()
            if(binding.switchNotifications.isChecked)
            {
                val intent = Intent(requireContext(), ServiceNotificationSpamFirestore::class.java)
                requireActivity().startService(intent)

                editor?.putBoolean("notifikacije", true)

            }
            else
            {
                requireActivity().stopService(Intent(requireContext(), ServiceNotificationSpamFirestore::class.java))
                editor?.putBoolean("notifikacije", false)
            }
            editor?.commit()
        }
        binding.switchNotifications.isChecked = korisnickePreference?.getBoolean("notifikacije", false) ?: false

        binding.switchLocation.setOnClickListener {
            var editor = korisnickePreference?.edit()
            if(binding.switchLocation.isChecked)
            {
                val intent = Intent(requireContext(), ServiceSendLocation::class.java)
                requireActivity().startService(intent)

                editor?.putBoolean("lokacija", true)

            }
            else
            {
                requireActivity().stopService(Intent(requireContext(), ServiceSendLocation::class.java))
                editor?.putBoolean("lokacija", false)
            }
            editor?.commit()

        }
        binding.switchLocation.isChecked = korisnickePreference?.getBoolean("lokacija", false) ?: false
    }


    override fun onResume()
    {
        super.onResume()
        val title: TextView = requireActivity().findViewById(R.id.toolbar_title)
        title.text = getString(R.string.settings_label)
        (activity as DrawerLocker?)!!.setDrawerEnabled(true)
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