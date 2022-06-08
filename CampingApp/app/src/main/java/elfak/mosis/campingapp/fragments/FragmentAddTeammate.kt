package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentAddTeammateBinding

class FragmentAddTeammate : Fragment() {

    private lateinit var binding: FragmentAddTeammateBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentAddTeammateBinding.inflate(layoutInflater)
        return binding.root
    }

}