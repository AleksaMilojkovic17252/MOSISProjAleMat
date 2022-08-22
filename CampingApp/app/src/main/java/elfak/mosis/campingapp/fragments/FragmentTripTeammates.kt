package elfak.mosis.campingapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityMain
import elfak.mosis.campingapp.adapters.AdapterTripTeammates
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.databinding.FragmentTripTeammatesBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTrip


class FragmentTripTeammates : Fragment(), AdapterTripTeammates.MoveAgain {


    lateinit var binding: FragmentTripTeammatesBinding
    private val sharedViewModel: SharedViewTrip by activityViewModels()
    lateinit var recycler: RecyclerView

    override fun onResume() {
        super.onResume()
        val navigation: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        for (i in 0 until navigation.getMenu().size())
            navigation.getMenu().getItem(i).setChecked(false)
        navigation.menu.findItem(R.id.nav_trip_teammates).setChecked(true)
        navigation.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentTripTeammatesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {




        super.onViewCreated(view, savedInstanceState)

        binding.teammatesMap.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentTripTeammates2_to_fragmentTripTeammates)
        }

        binding.HomeButtonTrip.setOnClickListener {
            var intent = Intent(context, ActivityMain::class.java)
            startActivity(intent)
        }

        recycler = binding.allFriends
        val adapter:AdapterTripTeammates = AdapterTripTeammates(requireContext(),
            sharedViewModel.korisnici.filter { x -> x.ID != Firebase.auth.uid } as ArrayList<User>,HashMap(sharedViewModel.zavrseneAktivnosti),this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true)


    }

    override fun Move(SelectedUser: User) {
        sharedViewModel.selectedUser.value = SelectedUser
        findNavController().navigate(R.id.action_fragmentTripTeammates2_to_fragmentTripTeammateBackpack)
    }


}