package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentTripTeammatesMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView


class FragmentTripTeammatesMap : Fragment()
{
    lateinit var binding: FragmentTripTeammatesMapBinding
    private lateinit var mapa: MapView

    override fun onResume() {
        super.onResume()
        val navigation: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        for (i in 0 until navigation.getMenu().size())
            navigation.getMenu().getItem(i).setChecked(false)
        navigation.menu.findItem(R.id.nav_trip_teammates).setChecked(true)
        mapa.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentTripTeammatesMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var context = activity?.applicationContext;
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context!!))
        mapa = binding.osmMapView


        mapa.setMultiTouchControls(true)
        mapa.controller.setZoom(8.5)
        mapa.controller.setCenter(GeoPoint(44.0333, 20.8))

        binding.teammatesTeammates.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentTripTeammates_to_fragmentTripTeammates2)
        }
    }



    override fun onPause()
    {
        super.onPause()
        mapa.onPause()
    }

}