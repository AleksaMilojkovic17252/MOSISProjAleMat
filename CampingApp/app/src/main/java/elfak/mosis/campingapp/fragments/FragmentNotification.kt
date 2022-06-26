package elfak.mosis.campingapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentNotificationBinding
import elfak.mosis.campingapp.sharedViews.SharedViewHome

class FragmentNotification : Fragment()
{
    lateinit var binding: FragmentNotificationBinding
    private val shareViewModel : SharedViewHome by activityViewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        var posmatrac = Observer<ArrayList<String>>
        { t ->
            //Log.d("CampingApp", "USO U OBSERVER")
            //Log.d("CampingApp",t?.reduce{ i, j -> "$i $j" } ?: "Mrk")
            //binding.textView10.text = t?.reduce{ i, j -> "$i $j" }
            // TODO: RECYCLEVIEW JEDAN DA STAVLJA NOTIFIKACIJE

        }
        shareViewModel.liveNotifikacije.observe(viewLifecycleOwner,posmatrac)
    }

    override fun onResume()
    {
        super.onResume()

        val title: TextView = requireActivity().findViewById(R.id.toolbar_title)
        title.text = getString(R.string.notification_fragment_title)

        val buttonNotification: ImageView = requireActivity().findViewById(R.id.notification_toolbar)
        val buttonFriend: ImageView = requireActivity().findViewById(R.id.addFriend_toolbar)

        val navigation: NavigationView = requireActivity().findViewById(R.id.nav_view)
        for (i in 0 until navigation.getMenu().size())
            navigation.getMenu().getItem(i).setChecked(false)
        navigation.menu.findItem(R.id.nav_notifications).setChecked(true)

        buttonFriend.visibility = View.GONE
        buttonNotification.visibility = View.GONE
    }
}