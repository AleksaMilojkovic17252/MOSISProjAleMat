package elfak.mosis.campingapp.fragments

import android.content.Intent
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityMain
import elfak.mosis.campingapp.classes.ActivityTrip
import elfak.mosis.campingapp.databinding.FragmentTeammatesBinding
import elfak.mosis.campingapp.databinding.FragmentTripMapActivityBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTrip
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import java.util.*


class FragmentTripMapActivity : Fragment() {

    private lateinit var binding: FragmentTripMapActivityBinding
    private val shareViewModel: SharedViewTrip by activityViewModels()
    private lateinit var mapa: MapView
    private lateinit var mapController: IMapController
    private lateinit var startMarker: Marker

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentTripMapActivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        var context = activity?.applicationContext;
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context!!))
        mapa = binding.osmMapView
        binding.buttonConfirmMapPosition.visibility = View.GONE

        binding.buttonConfirmMapPosition.setOnClickListener {
            var novi = ActivityTrip(UUID.randomUUID().toString(), Firebase.auth.currentUser!!.uid,
                shareViewModel.activityTitle.value!!,shareViewModel.activityDescription.value!!,shareViewModel.activityLatitude.value!!,shareViewModel.activityLongitude.value!!,shareViewModel.activityType.value!!
            )
            shareViewModel.allActivities.add(novi)
            saveOnFirebase(novi)
            shareViewModel.dataSetChanged.value = !shareViewModel.dataSetChanged.value!!
            findNavController().navigate(R.id.action_fragmentTripMapActivity_to_fragmentActivities)
        }
        mapa.setMultiTouchControls(true)
        mapController = mapa.controller
        mapa.controller.setZoom(13)
        startMarker = Marker(mapa)
        when(shareViewModel.activityType.value) {
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
        startMarker.infoWindow = null

        if(shareViewModel.activityLongitude.value != null && shareViewModel.activityLatitude.value != null) {
            mapa.controller.setCenter(GeoPoint(shareViewModel.activityLatitude.value!!,
                shareViewModel.activityLongitude.value!!))
            startMarker.position = GeoPoint(shareViewModel.activityLatitude.value!!,
                shareViewModel.activityLongitude.value!!)
        }
        else if(shareViewModel.longitude.value != null && shareViewModel.latitude.value != null)
        {
            mapa.controller.setCenter(GeoPoint(shareViewModel.latitude.value!!,
                shareViewModel.longitude.value!!))
            startMarker.position = GeoPoint(shareViewModel.latitude.value!!,
                shareViewModel.longitude.value!!)
        }
        else
        {
            mapa.controller.setCenter(GeoPoint(44.0333, 20.8))
            startMarker.position = GeoPoint(44.0333, 20.8)
        }
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapa.overlays.add(startMarker);
        setOnMapClickOverlay()

    }

    private fun saveOnFirebase(novi: ActivityTrip)
    {
        Firebase.firestore
            .collection(getString(R.string.db_coll_trips))
            .document(shareViewModel.tripID.value!!)
            .update("activities", FieldValue.arrayUnion(novi))
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
                binding.buttonConfirmMapPosition.visibility = View.VISIBLE
                mapa.overlays.remove(startMarker)
                startMarker.position = GeoPoint(p!!.latitude, p!!.longitude)
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                mapa.overlays.add(startMarker);
                shareViewModel.activityLatitude.value = p?.latitude
                shareViewModel.activityLongitude.value = p?.longitude

                return true
            }
            override fun longPressHelper(p: GeoPoint?): Boolean { return false }
        }
        mapa.overlays.add(MapEventsOverlay(receiver))
    }
}