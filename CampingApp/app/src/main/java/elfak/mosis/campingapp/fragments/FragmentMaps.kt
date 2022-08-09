package elfak.mosis.campingapp.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import elfak.mosis.campingapp.databinding.FragmentMapsBinding
import org.osmdroid.views.MapView
import androidx.preference.PreferenceManager
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.sharedViews.SharedViewTripForm
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class FragmentMaps : Fragment()
{
    private lateinit var binding: FragmentMapsBinding
    private lateinit var mapa: MapView
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var mapController: IMapController
    private lateinit var startMarker: Marker
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
        mapController = mapa.controller
        mapa.controller.setZoom(8.5)
        mapa.controller.setCenter(GeoPoint(44.0333, 20.8))
        startMarker = Marker(mapa)
        startMarker.icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_marker_for_map)
        startMarker.infoWindow = null
        startMarker.position = GeoPoint(44.0333, 20.8)
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapa.overlays.add(startMarker);
        setOnMapClickOverlay()

    }
    override fun onResume()
    {
        super.onResume()
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE
        mapa.onResume()
    }

    override fun onPause()
    {
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.VISIBLE
        super.onPause()
        mapa.onPause()
    }

    private fun setOnMapClickOverlay()
    {
        var receiver = object : MapEventsReceiver
        {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean
            {
                mapa.overlays.remove(startMarker)
                startMarker.position = GeoPoint(p!!.latitude, p!!.longitude)
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                mapa.overlays.add(startMarker);
                sharedViewModel.latitude.value = p?.latitude
                sharedViewModel.longitude.value = p?.longitude
                return true
            }
            override fun longPressHelper(p: GeoPoint?): Boolean { return false }
        }
        mapa.overlays.add(MapEventsOverlay(receiver))
    }
}