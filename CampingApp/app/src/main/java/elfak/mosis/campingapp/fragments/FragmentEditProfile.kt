package elfak.mosis.campingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.DrawerLocker
import elfak.mosis.campingapp.databinding.FragmentEditProfileBinding


class FragmentEditProfile : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding

//Mislim da ima bug ovde, pitanje je dal je zbog OnResume i OnStop ili navGraph
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageviewEditProfileBack.setOnClickListener {
            findNavController().popBackStack();
        }


    }

    override fun onResume() {
        super.onResume()
        (activity as DrawerLocker?)?.setDrawerEnabled(false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as DrawerLocker?)?.setDrawerEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

    }
}