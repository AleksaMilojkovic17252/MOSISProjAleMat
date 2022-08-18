package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentActivitiesBinding
import elfak.mosis.campingapp.databinding.FragmentDetailActivityViewBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTrip
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class FragmentDetailActivityView : Fragment() {

    lateinit var binding: FragmentDetailActivityViewBinding
    private val shareViewModel: SharedViewTrip by activityViewModels()
    private lateinit var mapa: MapView
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var mapController: IMapController
    private lateinit var startMarker: Marker

    override fun onResume() {
        super.onResume()
        val navigation: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        navigation.visibility = View.GONE
        mapa.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailActivityViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.opis.text = shareViewModel.selectedActivity.value?.description!!
        binding.naslov.text = shareViewModel.selectedActivity.value?.title!!
        binding.from.text = "From " + shareViewModel.korisnici.find{x->x.ID == shareViewModel.selectedActivity.value?.koJeNapravio!!}?.Name
        var context = activity?.applicationContext;
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context!!))
        mapa = binding.osmMapView
        mapa.setMultiTouchControls(true)
        mapController = mapa.controller
        mapa.controller.setZoom(8.5)
        mapa.controller.setCenter(GeoPoint(shareViewModel.selectedActivity.value?.latitude!!, shareViewModel.selectedActivity.value?.longitude!!))
        startMarker = Marker(mapa)
        when(shareViewModel.selectedActivity.value?.type) {
            1 ->
                startMarker.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_add_location_24_nice_view)
            2->
                startMarker.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_add_location_24_shelter)
            3->
                startMarker.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_add_location_24_point_of_interest)
        }
        startMarker.position = GeoPoint(shareViewModel.selectedActivity.value?.latitude!!, shareViewModel.selectedActivity.value?.longitude!!)
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapa.overlays.add(startMarker);
        binding.backButtonImage.setOnClickListener {

            findNavController().navigate(R.id.action_fragmentDetailActivityView_to_fragmentActivities)
        }
    }



    override fun onPause()
    {

        super.onPause()
        mapa.onPause()
    }


}