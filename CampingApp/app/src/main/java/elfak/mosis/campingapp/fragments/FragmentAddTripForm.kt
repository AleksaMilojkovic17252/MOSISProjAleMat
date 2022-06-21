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
import androidx.core.util.Pair
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityMain
import elfak.mosis.campingapp.adapters.AdapterAddTripTeammate
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.databinding.FragmentAddTeammateBinding
import elfak.mosis.campingapp.databinding.FragmentAddTripFormBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTripForm


class FragmentAddTripForm : Fragment()
{
    lateinit var binding: FragmentAddTripFormBinding
    lateinit var recyclerView: RecyclerView
    lateinit var s1: ArrayList<String>
    var images: ArrayList<Int> = arrayListOf(R.drawable.image3,R.drawable.image4,R.drawable.image5)// BRISI OVO TEK KADA SE POVLACE DRUGARI
    val sharedViewModel: SharedViewTripForm by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentAddTripFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val dateRangeText: TextView = binding.TextDate
        val calendar: ImageView = binding.openCalendar
        val datepick = MaterialDatePicker
            .Builder
            .dateRangePicker()
            .setTitleText("Select dates")
            .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds()))
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .build()

        binding.EditTextTripName.addTextChangedListener{ object : TextWatcher
            {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?)
                {
                    sharedViewModel.tripName.value = p0.toString()
                }

            }

        }

        calendar.setOnClickListener{
            fragmentManager?.let { it1 -> datepick.show(it1,"Picker") }
            datepick.addOnPositiveButtonClickListener {
                dateRangeText.text = datepick.headerText
                sharedViewModel.startDate.value = it.first
                sharedViewModel.endDate.value = it.second
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
            var id = Firebase.auth.currentUser!!.uid
            var name: String = ""
            var occupation: String = ""
            var description: String = ""
            Firebase.firestore.collection("users").document(id).get().addOnSuccessListener {
                name = (it["name"].toString())
                occupation = (it["occupation"].toString())
                description = (it["description"].toString())
            }

            sharedViewModel.korisnici.add(User(id,name,occupation,description))
            var i = Intent(context, ActivityMain::class.java)
            startActivity(i)
        }

        binding.OpenTeammatesButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAddTripForm_to_fragmentAddTripFormTeammates)
        }

        binding.continueButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAddTripForm_to_fragmentAddTripFormBackpack)

        }


        binding.OpenMapButton.setOnClickListener {
            findNavController().navigate(R.id.frAddTripForm_to_frMaps)
        }

    }

}