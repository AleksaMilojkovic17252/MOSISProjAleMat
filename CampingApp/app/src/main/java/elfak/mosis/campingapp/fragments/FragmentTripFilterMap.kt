package elfak.mosis.campingapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.ActivityTrip
import elfak.mosis.campingapp.databinding.FragmentTeammatesBinding
import elfak.mosis.campingapp.databinding.FragmentTripFilterMapBinding
import elfak.mosis.campingapp.sharedViews.SharedViewHome
import elfak.mosis.campingapp.sharedViews.SharedViewTrip


class FragmentTripFilterMap : Fragment() {

    private lateinit var binding: FragmentTripFilterMapBinding
    private var filterTypes: MutableList<Int> = mutableListOf(1,2,3)
    private var filterCompleted: Boolean = true
    private var distanceEntered:Boolean = false
    private val shareViewModel: SharedViewTrip by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripFilterMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val navigation: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        navigation.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(shareViewModel.distance.value != 0.0)
        {
            binding.editTextDistance.setText(shareViewModel.distance.value.toString())
        }
        binding.checkBoxCompleted.isChecked = shareViewModel.filterCompletedActivities.value!!
        binding.checkBoxPointOfInterest.isChecked = shareViewModel.filterTypeActivities.contains(ActivityTrip.POINT_OF_INTEREST)
        binding.checkBoxNiceView.isChecked = shareViewModel.filterTypeActivities.contains(ActivityTrip.NICE_VIEW)
        binding.checkBoxShelter.isChecked = shareViewModel.filterTypeActivities.contains(ActivityTrip.SHELTER)
        filterCompleted = shareViewModel.filterCompletedActivities.value!!
        filterTypes = shareViewModel.filterTypeActivities
        binding.checkBoxCompleted.setOnClickListener {
            filterCompleted = binding.checkBoxCompleted.isChecked
        }
        binding.checkBoxShelter.setOnClickListener {
            if(binding.checkBoxShelter.isChecked)
            {
                filterTypes.add(ActivityTrip.SHELTER)
            }
            else
            {
                filterTypes.remove(ActivityTrip.SHELTER)
            }
        }
        binding.checkBoxNiceView.setOnClickListener {
            if(binding.checkBoxNiceView.isChecked)
            {
                filterTypes.add(ActivityTrip.NICE_VIEW)
            }
            else
            {
                filterTypes.remove(ActivityTrip.NICE_VIEW)
            }
        }
        binding.checkBoxPointOfInterest.setOnClickListener {
            if(binding.checkBoxPointOfInterest.isChecked)
            {
                filterTypes.add(ActivityTrip.POINT_OF_INTEREST)
            }
            else
            {
                filterTypes.remove(ActivityTrip.POINT_OF_INTEREST)
            }
        }
        binding.editTextDistance.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?)
            {
               distanceEntered = p0?.isNotEmpty() ?: false
            }
        })

        binding.ContinueFilter.setOnClickListener {
            shareViewModel.filterCompletedActivities.value = filterCompleted
            shareViewModel.filterTypeActivities = filterTypes
            if(distanceEntered)
            {
                shareViewModel.distance.value = binding.editTextDistance.text.toString().toDouble()
            }
            findNavController().popBackStack()
        }
    }


}