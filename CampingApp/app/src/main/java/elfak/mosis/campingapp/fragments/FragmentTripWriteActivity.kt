package elfak.mosis.campingapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentTripMapActivityBinding
import elfak.mosis.campingapp.databinding.FragmentTripWriteActivityBinding
import elfak.mosis.campingapp.sharedViews.SharedViewHome
import elfak.mosis.campingapp.sharedViews.SharedViewTrip


class FragmentTripWriteActivity : Fragment(), AdapterView.OnItemSelectedListener {


    private lateinit var binding: FragmentTripWriteActivityBinding
    private val shareViewModel: SharedViewTrip by activityViewModels()
    private var activityType = false
    private var activityTitle = false
    private var activityDescription = false
    private lateinit var spinner:Spinner


    override fun onResume() {
        super.onResume()
        var Bottom: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        Bottom.visibility = View.GONE
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentTripWriteActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        spinner = binding.izaberiTip
        var adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(requireContext(),R.array.types,android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        binding.editTextTitle.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?)
            {
                activityTitle = p0?.isNotEmpty() ?: false
                enableContinue()
            }
        })
        binding.editTextDescription.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?)
            {
                activityDescription = p0?.isNotEmpty() ?: false
                enableContinue()
            }
        })

        binding.finishAcitivty.setOnClickListener {
            shareViewModel.activityTitle.value = binding.editTextTitle.text.toString()
            shareViewModel.activityDescription.value = binding.editTextDescription.text.toString()
            Log.d("Title",shareViewModel.activityTitle.value.toString())
            Log.d("Description",shareViewModel.activityDescription.value.toString())
            Log.d("Type",shareViewModel.activityType.value.toString())
            findNavController().navigate(R.id.action_fragmentTripWriteActivity_to_fragmentTripMapActivity)
        }
        binding.goBackButton.setOnClickListener {
            shareViewModel.activityDescription.value = null
            shareViewModel.activityTitle.value = null
            shareViewModel.activityType.value = null
            findNavController().popBackStack()
        }




    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        shareViewModel.activityType.value = p2
        if(p2>0)
        {
            activityType = true
        }
        else
            activityType = false
        enableContinue()
    }

    private fun enableContinue() {
        if(activityDescription && activityTitle && activityType)
        {
            binding.finishAcitivty.isEnabled = true
            binding.finishAcitivty.setImageResource(R.drawable.ic_baseline_check_24_enabled)
        }
        else
        {
            binding.finishAcitivty.isEnabled = false
            binding.finishAcitivty.setImageResource(R.drawable.ic_baseline_check_24_disabled)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}