package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.adapters.AdapterTripTeammateBackpack
import elfak.mosis.campingapp.databinding.FragmentTripTeammateBackpackBinding
import elfak.mosis.campingapp.databinding.FragmentTripTeammatesBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTrip

class FragmentTripTeammateBackpack : Fragment() {

    lateinit var binding: FragmentTripTeammateBackpackBinding
    private val sharedViewModel: SharedViewTrip by activityViewModels()
    lateinit var recycler: RecyclerView

    override fun onResume() {
        super.onResume()
        var Bottom: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        Bottom.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripTeammateBackpackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = binding.backpackItemsFriend
        val FriendBackpackItemsAdapter: AdapterTripTeammateBackpack = AdapterTripTeammateBackpack(requireContext(),
            sharedViewModel.backpackItems[sharedViewModel.selectedUser.value?.ID]?.toCollection(ArrayList()))
        recycler.adapter = FriendBackpackItemsAdapter
        recycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true)
        binding.buttonGoBackBackpack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.friendBackpackTitle.text = sharedViewModel.selectedUser.value!!.Name + "'s Backpack"
    }




}