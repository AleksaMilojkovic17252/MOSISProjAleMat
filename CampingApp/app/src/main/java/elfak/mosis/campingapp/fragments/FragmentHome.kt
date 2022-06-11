package elfak.mosis.campingapp.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentHomeBinding


class FragmentHome : Fragment()
{
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mDrawer: DrawerLayout



    override fun onResume() {
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) 
    {

        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            Toast.makeText(context, "Kliknut sam", Toast.LENGTH_SHORT).show()
        }

        binding.textViewHomeIDNumber.text = "#00001"

        binding.buttonHomeCopyToClipboard.setOnClickListener {
            var clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                ?: null
            var clip = ClipData.newPlainText("Copied ID", binding.textViewHomeIDNumber.text.toString())
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(context, "ID copied to clipboard!", Toast.LENGTH_SHORT).show()
        }

    }


}