package elfak.mosis.campingapp.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityAddFriends
import elfak.mosis.campingapp.databinding.FragmentTeammatesBinding

class FragmentTeammates : Fragment()
{
    private lateinit var binding: FragmentTeammatesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentTeammatesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume()
    {
        super.onResume()
        val title: TextView = requireActivity().findViewById(R.id.toolbar_title)
        title.text = "Teammates"
        val buttonNotification: ImageView = requireActivity().findViewById(R.id.notification_toolbar)
        val buttonFriend: ImageView = requireActivity().findViewById(R.id.addFriend_toolbar)
        val navigation: NavigationView = requireActivity().findViewById(R.id.nav_view)
        for (i in 0 until navigation.getMenu().size())
            navigation.getMenu().getItem(i).setChecked(false)
        navigation.menu.findItem(R.id.nav_teammates).setChecked(true)

        buttonFriend.visibility = View.VISIBLE
        buttonNotification.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAddTeammate.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentTeammates_to_fragmentAddTeammate2)
        }

        if(Firebase.auth.currentUser?.uid?.isNotEmpty() == true)
        {
            var uidLength = Firebase.auth.currentUser!!.uid.length;
            var stringBuilder = StringBuilder()
            stringBuilder.append(Firebase.auth.currentUser!!.uid.subSequence(0,3))
            stringBuilder.append("...")
            stringBuilder.append(Firebase.auth.currentUser!!.uid.subSequence(uidLength-3, uidLength))
            binding.textViewIDNumber.text = stringBuilder.toString()
        }

        binding.buttonCopyToClipboard.setOnClickListener {
            var clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager ?: null
            var clip = ClipData.newPlainText("Copied ID", Firebase.auth.currentUser!!.uid)
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(context, "ID copied to clipboard!", Toast.LENGTH_SHORT).show()

        }
    }

}