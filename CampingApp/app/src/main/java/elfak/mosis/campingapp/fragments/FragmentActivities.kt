package elfak.mosis.campingapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityMain
import elfak.mosis.campingapp.adapters.AdapterAllActivities
import elfak.mosis.campingapp.classes.ActivityTrip
import elfak.mosis.campingapp.databinding.FragmentActivitiesBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTrip


class FragmentActivities : Fragment(), AdapterAllActivities.IdiNaDetaljeIJosNesto
{
    lateinit var binding: FragmentActivitiesBinding
    private val shareViewModel: SharedViewTrip by activityViewModels()
    lateinit var recycler: RecyclerView

    override fun onResume() {
        super.onResume()
        val navigation: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        for (i in 0 until navigation.getMenu().size())
            navigation.getMenu().getItem(i).setChecked(false)
        navigation.menu.findItem(R.id.nav_activities).setChecked(true)
        navigation.visibility = View.VISIBLE
        shareViewModel.activityDescription.value = null
        shareViewModel.activityTitle.value = null
        shareViewModel.activityType.value = null
        shareViewModel.activityLatitude.value = null
        shareViewModel.activityLongitude.value = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentActivitiesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter: AdapterAllActivities = AdapterAllActivities(requireContext(),shareViewModel.allActivities.toCollection(ArrayList()),shareViewModel.korisnici.toCollection(ArrayList()),
            Firebase.auth.currentUser!!.uid,this)
        recycler = binding.recyclerActivities
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true)

        shareViewModel.dataSetChanged.observe(viewLifecycleOwner){
            adapter.notifyDataSetChanged()
        }

        binding.buttonAddActivity.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentActivities_to_fragmentTripWriteActivity)
        }
        binding.addActivityToolbar.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentActivities_to_fragmentTripWriteActivity)
        }
        binding.returnHomeActivity.setOnClickListener {
            var intent = Intent(context, ActivityMain::class.java)
            startActivity(intent)
        }
    }


    override fun Detalji(Activity: ActivityTrip) {
        shareViewModel.selectedActivity.value = Activity
        findNavController().navigate(R.id.action_fragmentActivities_to_fragmentDetailActivityView)
    }
}