package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentActivitiesBinding
import elfak.mosis.campingapp.databinding.FragmentMemoriesBinding


class FragmentMemories : Fragment()
{
    lateinit var binding: FragmentMemoriesBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentMemoriesBinding.inflate(layoutInflater)
        return binding.root
    }
}