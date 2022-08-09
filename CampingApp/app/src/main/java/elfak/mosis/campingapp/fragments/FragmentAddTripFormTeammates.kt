package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.adapters.AdapterAddTripAllTeammates
import elfak.mosis.campingapp.adapters.AdapterAddTripTeammate
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.databinding.FragmentAddTripFormTeammatesBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTripForm


class FragmentAddTripFormTeammates : Fragment(), AdapterAddTripAllTeammates.Sotka {


    lateinit var binding : FragmentAddTripFormTeammatesBinding
    lateinit var recycler: RecyclerView
    lateinit var s1: ArrayList<String>
    lateinit var s2: ArrayList<String>
    var images: ArrayList<Int> = arrayListOf(R.drawable.image3,R.drawable.image4,R.drawable.image5)
    val sharedViewModel: SharedViewTripForm by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentAddTripFormTeammatesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE
    }

    override fun onPause() {
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.VISIBLE
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        recycler = binding.recyclerTeammates


        s1 = resources.getStringArray(R.array.Names).toCollection(ArrayList())
        s2 = resources.getStringArray(R.array.Occupations).toCollection(ArrayList())

        var korisnici: ArrayList<User> = ArrayList()

        for(item in s1.indices)
        {
            var korisnik: User = User("0",s1[item],s2[item],"posao",images[item].toString(),ArrayList())
            korisnici.add(korisnik)
        }

        val teammatesAdapter: AdapterAddTripAllTeammates? =
            context?.let { AdapterAddTripAllTeammates(it,korisnici,sharedViewModel, this) }

        recycler.adapter = teammatesAdapter
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)

        binding.backButtonImage.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    override fun goBack() {
        findNavController().popBackStack()
    }

}