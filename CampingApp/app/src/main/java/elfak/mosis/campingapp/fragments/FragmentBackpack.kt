package elfak.mosis.campingapp.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityMain
import elfak.mosis.campingapp.adapters.AdapterTripBackpack
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.databinding.FragmentBackpackBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTrip
import java.util.*

class FragmentBackpack : Fragment()
{
    lateinit var binding: FragmentBackpackBinding
    lateinit var recycler: RecyclerView
    val sharedViewModel: SharedViewTrip by activityViewModels()

    override fun onResume() {
        super.onResume()
        val navigation: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        for (i in 0 until navigation.getMenu().size())
            navigation.getMenu().getItem(i).setChecked(false)
        navigation.menu.findItem(R.id.nav_backpack).setChecked(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentBackpackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = binding.allItems
        val itemsAdapter:AdapterTripBackpack = AdapterTripBackpack(requireContext(),sharedViewModel)
        recycler.adapter = itemsAdapter
        recycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, true)
        binding.goHome.setOnClickListener {
            var intent = Intent(context, ActivityMain::class.java)
            startActivity(intent)
        }

        binding.newItem.setOnClickListener {
            if(sharedViewModel.endDate.value!!.before(Date()))
            {
                Toast.makeText(requireContext(), "Cant add items on finished trip", Toast.LENGTH_SHORT).show()
            }
            else {
                val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                dialog.setTitle("Item name")
                var itemInput: EditText = EditText(context)
                itemInput.inputType = InputType.TYPE_CLASS_TEXT
                dialog.setView(itemInput)
                dialog.setPositiveButton(
                    "Ok",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        val myText = itemInput.text.toString()
                        sharedViewModel.backpackItems[Firebase.auth.uid]?.add(BackpackItems(myText))
                        itemsAdapter?.notifyItemInserted(sharedViewModel.backpackItems[Firebase.auth.uid]?.count()!! - 1)
                    })

                dialog.setNegativeButton(
                    "Cancel",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.cancel()
                    })

                dialog.show()
            }
        }

        binding.buttonAddItem.setOnClickListener {
            if(sharedViewModel.endDate.value!!.before(Date()))
            {
                Toast.makeText(requireContext(), "Cant add items on finished trip", Toast.LENGTH_SHORT).show()
            }
            else {
                val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                dialog.setTitle("Item name")
                var itemInput: EditText = EditText(context)
                itemInput.inputType = InputType.TYPE_CLASS_TEXT
                dialog.setView(itemInput)
                dialog.setPositiveButton(
                    "Ok",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        val myText = itemInput.text.toString()
                        sharedViewModel.backpackItems[Firebase.auth.uid]?.add(BackpackItems(myText))
                        itemsAdapter?.notifyItemInserted(sharedViewModel.backpackItems[Firebase.auth.uid]?.count()!! - 1)
                    })

                dialog.setNegativeButton(
                    "Cancel",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.cancel()
                    })

                dialog.show()
            }
        }

        binding.floatingActionButton.setOnClickListener {
            if (sharedViewModel.endDate.value!!.after(Date())) {
                Firebase.firestore
                    .collection(getString(R.string.db_coll_trips))
                    .document(sharedViewModel.tripID.value!!)
                    .update("userItems", sharedViewModel.backpackItems)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Successful update", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }


    }


}