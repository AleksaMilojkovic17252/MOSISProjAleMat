package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentAddTeammateBinding
import elfak.mosis.campingapp.databinding.FragmentAddTripFormBinding


class FragmentAddTripForm : Fragment() {



    lateinit var binding: FragmentAddTripFormBinding
    lateinit var recyclerView: RecyclerView
    lateinit var s1: Array<String>
    var images: Array<Int> = arrayOf(R.drawable.image3,R.drawable.image4,R.drawable.image5)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTripFormBinding.inflate(layoutInflater)

        recyclerView = binding.TeammateListView

        s1 = resources.getStringArray(R.array.Names)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateRangeText: TextView = binding.TextDate
        val calendar: ImageView = binding.openCalendar
        val datepick = MaterialDatePicker.Builder.dateRangePicker().setTitleText("Select dates").
                                            setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds())).setTheme(R.style.ThemeOverlay_App_DatePicker).build()

        calendar.setOnClickListener{
            fragmentManager?.let { it1 -> datepick.show(it1,"Picker") }
            datepick.addOnPositiveButtonClickListener {
                dateRangeText.text = datepick.headerText
            }
        }

    }

}