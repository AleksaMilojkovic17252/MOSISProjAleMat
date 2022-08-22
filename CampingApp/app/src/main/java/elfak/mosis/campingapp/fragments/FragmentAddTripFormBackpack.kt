package elfak.mosis.campingapp.fragments


import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.adapters.AdapterAddTripBackpack
import elfak.mosis.campingapp.classes.ActivityTrip
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.classes.Trip
import elfak.mosis.campingapp.databinding.FragmentAddTripFormBackpackBinding
import elfak.mosis.campingapp.sharedViews.SharedViewHome
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class FragmentAddTripFormBackpack : Fragment()
{
    lateinit var binding: FragmentAddTripFormBackpackBinding
    lateinit var recycler: RecyclerView
    val sharedViewModel: SharedViewHome by activityViewModels()
    var backpackItems: ArrayList<BackpackItems> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentAddTripFormBackpackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE
    }
    override fun onPause() {
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.VISIBLE
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        recycler = binding.backpackItems

        val backpackAdapter: AdapterAddTripBackpack? =
            context?.let { AdapterAddTripBackpack(it,sharedViewModel) }

        recycler.adapter = backpackAdapter
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)

        binding.buttonGoBackBackpack.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.imageButtonPlus.setOnClickListener{
            val dialog : AlertDialog.Builder = AlertDialog.Builder(requireContext())
            dialog.setTitle("Item name")
            var itemInput: EditText = EditText(context)
            itemInput.inputType = InputType.TYPE_CLASS_TEXT
            dialog.setView(itemInput)
            dialog.setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                val myText = itemInput.text.toString()
                sharedViewModel.backpackItems.add(BackpackItems(myText))
                backpackAdapter?.notifyItemInserted(sharedViewModel.backpackItems.count() - 1)
            })

            dialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })

            dialog.show()
        }

        binding.continueButton.setOnClickListener{
            sharedViewModel.korisnik.value?.let { it1 -> sharedViewModel.korisnici.add(it1) }
            var tripName = sharedViewModel.tripName.value
            var longitude = sharedViewModel.longitude.value
            var latitude = sharedViewModel.latitude.value
            var users = sharedViewModel.korisnici.toCollection(ArrayList())
            var startDate = sharedViewModel.startDate.value
            var endDate = sharedViewModel.endDate.value
            var backpackItems = HashMap<String,ArrayList<BackpackItems>>()
            for(user in users)
            {
                backpackItems.put(user.ID, ArrayList())
            }
            backpackItems.put(sharedViewModel.korisnik.value!!.ID,sharedViewModel.backpackItems.toCollection(ArrayList()))
            val trip = Trip("0",tripName!!,longitude!!,latitude!!,users,startDate!!,endDate!!, backpackItems, "")
            createTrip(trip)
            findNavController().navigate(R.id.action_fragmentAddTripFormBackpack2_to_fragmentHome)
            
        }
    }

    private fun createTrip(trip: Trip)
    {
        //Moram da dodam da li lakse nasao stvari
        var userIDs = ArrayList<String>()
        for(user in trip.users)
            userIDs.add(user.ID)

        var tmp = HashMap<String, ArrayList<String>>()
        for(id in userIDs)
        {
            tmp[id] = ArrayList()
        }

        var zaDodati = hashMapOf(
            "tripName" to trip.tripName,
            "startDate" to trip.startDate,
            "endDate" to trip.endDate,
            "latitude" to trip.latitude,
            "longitude" to trip.longitude,
            "userIDs" to userIDs,
            "users" to trip.users,
            "userItems" to trip.UserItems,
            "processed" to false,
            "memories" to arrayOf<String>(),
            "activities" to arrayOf<ActivityTrip>(),
            "completedActivities" to tmp
        )

        Firebase.firestore.collection("trips").add(zaDodati)
    }


}