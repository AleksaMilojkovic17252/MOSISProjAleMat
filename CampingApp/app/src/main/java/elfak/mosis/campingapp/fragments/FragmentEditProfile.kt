package elfak.mosis.campingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentEditProfileBinding


class FragmentEditProfile : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding


    override fun onResume() {
        super.onResume()
        val title: TextView = requireActivity().findViewById(R.id.toolbar_title)
        val navigation: NavigationView = requireActivity().findViewById(R.id.nav_view)
        for (i in 0 until navigation.getMenu().size()) {
            navigation.getMenu().getItem(i).setChecked(false)
        }
        title.text = "Edit Profile"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }



}