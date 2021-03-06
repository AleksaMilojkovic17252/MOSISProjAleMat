package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elfak.mosis.campingapp.databinding.FragmentMapsBinding
import org.osmdroid.views.MapView
import androidx.preference.PreferenceManager
import elfak.mosis.campingapp.sharedViews.SharedViewTripForm
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay

class FragmentMaps : Fragment()
{
    private lateinit var binding: FragmentMapsBinding
    private lateinit var mapa: MapView
    private val sharedViewModel: SharedViewTripForm by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentMapsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        var context = activity?.applicationContext;
        Configuration.getInstance().load(context,PreferenceManager.getDefaultSharedPreferences(context!!))
        mapa = binding.osmMapView


        binding.buttonConfirmMapPosition.setOnClickListener {
            //podaci se setuju u onclick eventu
            findNavController().popBackStack()
        }
        mapa.setMultiTouchControls(true)
        mapa.controller.setZoom(8.5)
        mapa.controller.setCenter(GeoPoint(44.0333, 20.8))
        setOnMapClickOverlay()

    }

    override fun onResume()
    {
        super.onResume()
        mapa.onResume()
    }

    override fun onPause()
    {
        super.onPause()
        mapa.onPause()
    }

    private fun setOnMapClickOverlay()
    {
        var receiver = object : MapEventsReceiver
        {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean
            {
                Toast.makeText(requireContext(), "${p?.longitude.toString()}, ${p?.latitude.toString()}", Toast.LENGTH_SHORT).show()
                sharedViewModel.latitude.value = p?.latitude
                sharedViewModel.longitude.value = p?.longitude
                return true
            }
            override fun longPressHelper(p: GeoPoint?): Boolean { return false }
        }
        mapa.overlays.add(MapEventsOverlay(receiver))
    }
}