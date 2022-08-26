package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.adapters.AdapterAddTripAllTeammates
import elfak.mosis.campingapp.adapters.AdapterAddTripTeammate
import elfak.mosis.campingapp.adapters.AdapterAllTeammates
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.databinding.FragmentAddTripFormTeammatesBinding
import elfak.mosis.campingapp.sharedViews.SharedViewHome



class FragmentAddTripFormTeammates : Fragment(), AdapterAddTripAllTeammates.Sotka {


    lateinit var binding : FragmentAddTripFormTeammatesBinding
    lateinit var recycler: RecyclerView
    lateinit var s1: ArrayList<String>
    lateinit var s2: ArrayList<String>
    var images: ArrayList<Int> = arrayListOf(R.drawable.image3,R.drawable.image4,R.drawable.image5)
    val sharedViewModel: SharedViewHome by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentAddTripFormTeammatesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume()
    {
        super.onResume()
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE
    }

    override fun onPause()
    {
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.VISIBLE
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        recycler = binding.recyclerTeammates


        s1 = resources.getStringArray(R.array.Names).toCollection(ArrayList())
        s2 = resources.getStringArray(R.array.Occupations).toCollection(ArrayList())

        var korisnici: ArrayList<User> = ArrayList()

        if (sharedViewModel.fullUcitavanje.value == false)
            ucitajIPostavi()
        else
        {
            for (drug in sharedViewModel.korisnik.value!!.Drugari)
                korisnici.add(drug)
            val teammatesAdapter: AdapterAddTripAllTeammates? =
                context?.let { AdapterAddTripAllTeammates(it, korisnici.minus(sharedViewModel.korisnici).toCollection(ArrayList()), sharedViewModel, this) }

            recycler.adapter = teammatesAdapter
            recycler.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        }
        binding.backButtonImage.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    private fun ucitajIPostavi()
    {
        var drugovi = ArrayList<User>()
        var listaTaskovaSakupljanjaPodatakaKorsinika = ArrayList<Task<DocumentSnapshot>>()

        var tmp2 = Firebase.firestore
            .collection(getString(R.string.db_coll_users))
            .document(sharedViewModel.korisnik.value!!.ID)
            .get()
        tmp2.addOnSuccessListener {it2 ->
            var prike = it2["friends"] as ArrayList<String>
            //Toast.makeText(requireContext(), "${prike.count()}", Toast.LENGTH_SHORT).show()
            for (p in prike)
            {
                var tmp = Firebase.firestore
                    .collection(getString(R.string.db_coll_users))
                    .document(p)
                    .get()
                tmp.addOnSuccessListener {
                    var tmpUser = User(it.id,
                        it["name"].toString(),
                        it["occupation"].toString(),
                        it["description"].toString(),
                        it.id,
                        ArrayList<User>())
                    drugovi.add(tmpUser)
                }
                listaTaskovaSakupljanjaPodatakaKorsinika.add(tmp)
            }
            Tasks.whenAll(listaTaskovaSakupljanjaPodatakaKorsinika).addOnSuccessListener {
                sharedViewModel.korisnik.value!!.Drugari = drugovi
                sharedViewModel.fullUcitavanje.value = true

                val teammatesAdapter: AdapterAddTripAllTeammates? =
                    context?.let { AdapterAddTripAllTeammates(it, drugovi.minus(sharedViewModel.korisnici).toCollection(ArrayList()), sharedViewModel, this) }

                recycler.adapter = teammatesAdapter
                recycler.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            }
        }


    }

    override fun goBack()
    {
        findNavController().popBackStack()
    }

}