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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityMain
import elfak.mosis.campingapp.adapters.AdapterAddTripAllTeammates
import elfak.mosis.campingapp.adapters.AdapterAddTripBackpack
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.classes.Trip
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.databinding.FragmentAddTripFormBackpackBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTripForm
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class FragmentAddTripFormBackpack : Fragment()
{
    lateinit var binding: FragmentAddTripFormBackpackBinding
    lateinit var recycler: RecyclerView
    val sharedViewModel: SharedViewTripForm by activityViewModels()
    var backpackItems: ArrayList<BackpackItems> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentAddTripFormBackpackBinding.inflate(layoutInflater)
        return binding.root
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
            var id = Firebase.auth.currentUser!!.uid
            var name: String = ""
            var occupation: String = ""
            var description: String = ""
            Firebase.firestore.collection("users").document(id).get().addOnSuccessListener {
                name = (it["name"].toString())
                occupation = (it["occupation"].toString())
                description = (it["description"].toString())
            }

            sharedViewModel.korisnici.remove(User(id,name,occupation,description,""))
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
            // TODO: OVO NE RADI JOS UVEK 
            var tripName = sharedViewModel.tripName.value
            var longitude = sharedViewModel.longitude.value
            var latitude = sharedViewModel.latitude.value
            var users = sharedViewModel.korisnici.toCollection(ArrayList())
            var startDate = sharedViewModel.startDate.value
            var endDate = sharedViewModel.endDate.value
            var backpackItems = HashMap<String,ArrayList<BackpackItems>>()
            backpackItems.put(sharedViewModel.glavniKorisnik.value!!.ID,sharedViewModel.backpackItems.toCollection(ArrayList()))
            val trip = Trip(tripName!!,longitude!!,latitude!!,users,startDate!!,endDate!!, backpackItems)
            createTrip(trip)
            var i = Intent(context, ActivityMain::class.java)
            startActivity(i)
            
        }
    }

    private fun createTrip(trip: Trip)
    {
        // TODO: OVO NE RADI JOS UVEK 
        Firebase.firestore.collection("trips").add(trip)
    }


}