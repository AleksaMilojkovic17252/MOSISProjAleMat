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
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.adapters.AdapterAddTripAllTeammates
import elfak.mosis.campingapp.adapters.AdapterAddTripBackpack
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.databinding.FragmentAddTripFormBackpackBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTripForm


class FragmentAddTripFormBackpack : Fragment() {

    lateinit var binding: FragmentAddTripFormBackpackBinding
    lateinit var recycler: RecyclerView
    val sharedViewModel: SharedViewTripForm by activityViewModels()
    var backpackItems: ArrayList<BackpackItems> = arrayListOf()
    var ListOfItems: ArrayList<BackpackItems> = arrayListOf(BackpackItems("Lampa"), BackpackItems("Solja"), BackpackItems("PVC"))
    var counter: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTripFormBackpackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = binding.backpackItems

        val backpackAdapter: AdapterAddTripBackpack? =
            context?.let { AdapterAddTripBackpack(it,backpackItems,sharedViewModel) }

        recycler.adapter = backpackAdapter
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)

        binding.buttonGoBackBackpack.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.imageButtonPlus.setOnClickListener{
            backpackItems.add(ListOfItems[counter%3])
            counter++
            backpackAdapter?.notifyItemInserted(backpackItems.count() - 1)

        }
    }


}