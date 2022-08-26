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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.adapters.AdapterTripTeammateBackpack
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.databinding.FragmentTripTeammateBackpackBinding
import elfak.mosis.campingapp.databinding.FragmentTripTeammatesBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTrip
import java.util.HashMap

class FragmentTripTeammateBackpack : Fragment() {

    lateinit var binding: FragmentTripTeammateBackpackBinding
    private val sharedViewModel: SharedViewTrip by activityViewModels()
    lateinit var recycler: RecyclerView

    override fun onResume()
    {
        super.onResume()
        var Bottom: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        Bottom.visibility = View.GONE

        Firebase.firestore
            .collection(getString(R.string.db_coll_trips))
            .document(sharedViewModel.tripID.value!!)
            .get()
            .addOnSuccessListener {
                var pomoc: HashMap<String, ArrayList<BackpackItems>> = HashMap()
                for(id in it["userItems"] as HashMap<String, ArrayList<HashMap<String, Object>>>)
                {
                    var anotherHelp: ArrayList<BackpackItems> = ArrayList()
                    for(help in id.value)
                    {
                        anotherHelp.add(BackpackItems(help["name"].toString(),help["items"].toString().toInt()))
                    }
                    pomoc[id.key]=anotherHelp
                }
                sharedViewModel.backpackItems = pomoc
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripTeammateBackpackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
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