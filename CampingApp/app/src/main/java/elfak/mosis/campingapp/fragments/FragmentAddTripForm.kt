package elfak.mosis.campingapp.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.util.Pair
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.Date
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityMain
import elfak.mosis.campingapp.activities.DrawerLocker
import elfak.mosis.campingapp.adapters.AdapterAddTripTeammate
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.databinding.FragmentAddTeammateBinding
import elfak.mosis.campingapp.databinding.FragmentAddTripFormBinding
import elfak.mosis.campingapp.sharedViews.SharedViewHome

import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentAddTripForm : Fragment()
{
    lateinit var binding: FragmentAddTripFormBinding
    lateinit var recyclerView: RecyclerView
    lateinit var s1: ArrayList<String>
    var images: ArrayList<Int> = arrayListOf(R.drawable.image3,R.drawable.image4,R.drawable.image5)// BRISI OVO TEK KADA SE POVLACE DRUGARI
    var hasTripName: Boolean = false;
    var hasLocation: Boolean = false;
    var hasDate: Boolean = false;
    var date: String = ""
    val sharedViewModel: SharedViewHome by activityViewModels()

    override fun onResume() {
        super.onResume()
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE
        (activity as DrawerLocker?)!!.setDrawerEnabled(false)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentAddTripFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


        val dateRangeText: TextView = binding.TextDate
        dateRangeText.text = date
        val calendar: ImageView = binding.openCalendar


        val constraintsBuilder=CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
            .build()
        val datepick = MaterialDatePicker
            .Builder
            .dateRangePicker()
            .setTitleText("Select dates")
            .setSelection(Pair.create(MaterialDatePicker.todayInUtcMilliseconds(),(MaterialDatePicker.todayInUtcMilliseconds() + (1000*60*60*24*7))))
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setCalendarConstraints(constraintsBuilder)
            .build()


        binding.EditTextTripName.addTextChangedListener(object : TextWatcher
            {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?)
                {
                    sharedViewModel.tripName.value = p0.toString()
                    hasTripName = p0?.isNotEmpty() ?: false
                    enableContinue()
                }

            }

        )

        calendar.setOnClickListener{
            fragmentManager?.let { it1 -> datepick.show(it1,"Picker") }
            datepick.addOnPositiveButtonClickListener {
                dateRangeText.text = datepick.headerText
                date = datepick.headerText
                hasDate = dateRangeText.text?.isNotEmpty() ?: false
                sharedViewModel.startDate.value = Date(it.first)
                sharedViewModel.endDate.value = Date(it.second)
                enableContinue()
            }
        }

        recyclerView = binding.TeammateListView

        s1 = resources.getStringArray(R.array.Names).toCollection(ArrayList())

        val teammatesAdapter: AdapterAddTripTeammate? =
            context?.let { AdapterAddTripTeammate(it,sharedViewModel.korisnici.toCollection(ArrayList()),sharedViewModel) } //requireContext

        recyclerView.adapter = teammatesAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        sharedViewModel.dataChanger.observe(viewLifecycleOwner){
            teammatesAdapter?.notifyDataSetChanged()
        }

        binding.imageBackButton.setOnClickListener{

            findNavController().navigate(R.id.action_fragmentAddTripForm2_to_fragmentHome)
        }

        binding.OpenTeammatesButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAddTripForm2_to_fragmentAddTripFormTeammates2)
        }

        binding.continueButton.setOnClickListener {

            findNavController().navigate(R.id.action_fragmentAddTripForm2_to_fragmentAddTripFormBackpack2)
        }


        binding.OpenMapButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAddTripForm2_to_fragmentMaps2)
        }

        if (sharedViewModel.latitude.value != null && sharedViewModel.longitude.value != null)
        {
            var formater = DecimalFormat("#.##")
            formater.roundingMode = RoundingMode.DOWN
            var vrednost = formater.format(sharedViewModel.longitude.value) + " " + formater.format(sharedViewModel.latitude.value)
            binding.textViewLocation.text =vrednost
            hasLocation = true
            enableContinue()
        }
        enableContinue()
    }

    fun enableContinue()
    {
        if(hasLocation && hasTripName && hasDate)
        {
            binding.continueButton.setBackgroundResource(R.drawable.et_button_shape_green)
            binding.continueButton.isEnabled = true
        }
        else
        {
            binding.continueButton.setBackgroundResource(R.drawable.button_disabled)
            binding.continueButton.isEnabled = false
        }
    }

    override fun onPause() {
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.VISIBLE
        super.onPause()
    }

}