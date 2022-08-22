package elfak.mosis.campingapp.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityMain
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.databinding.FragmentTripTeammatesMapBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTrip
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class FragmentTripTeammatesMap : Fragment()
{
    lateinit var binding: FragmentTripTeammatesMapBinding
    private lateinit var mapa: MapView
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var mapController: IMapController
    val PERMISSION_ACCESS_FINE_LOCATION = 1
    private val sharedViewModel: SharedViewTrip by activityViewModels()
    private val listaReferenci = ArrayList<DatabaseReference>()
    private val mapaKoordinata = HashMap<String,GeoPoint>()

    override fun onResume()
    {
        super.onResume()
        val navigation: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        for (i in 0 until navigation.getMenu().size())
            navigation.getMenu().getItem(i).setChecked(false)

        navigation.menu.findItem(R.id.nav_trip_teammates).setChecked(true)
        mapa.onResume()

        for(u in sharedViewModel.korisnici)
        {
            if(u.ID == Firebase.auth.currentUser!!.uid)
                continue

            var tmp = Firebase.database(getString(R.string.db_realtime_db)).getReference("locations/${u.ID}")
            tmp.addValueEventListener(object : ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    mapaKoordinata["${u.ID}"] = GeoPoint(snapshot.child("lat").value as Double,
                                                            snapshot.child("long").value as Double)
                    crtajMapu()
                }

                override fun onCancelled(error: DatabaseError) { return }

            })
            listaReferenci.add(tmp)
        }

        crtajMapu()

    }

    private fun crtajMapu()
    {
        mapa.overlays.clear()
        mapa.controller.setCenter(GeoPoint(sharedViewModel.latitude.value!!, sharedViewModel.longitude.value!!))

        if(ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        else
        {
            setMyLocationOverlay()
        }
        for(par in mapaKoordinata)
        {
            var idK = par.key
            var korisnik: User = sharedViewModel.korisnici.find { user -> user.ID == idK }!!
            var lokacija = par.value

            var marker = Marker(mapa)
            marker.icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_person_pin_circle_24)
            //marker.infoWindow = InfoWindow()
            marker.title = korisnik.Name
            marker.subDescription = "Points: 0"
            marker.image = ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_person_24)
            marker.position = lokacija
            marker.setOnMarkerClickListener { marker, _ ->
                marker.showInfoWindow()
                Log.d("hallo","worky")
                true//ovako cemo prikazivati detalje o activity-u
            }
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapa.overlays.add(marker);
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentTripTeammatesMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.filterButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentTripTeammates_to_fragmentTripFilterMap)
        }
        var context = activity?.applicationContext;
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context!!))
        mapa = binding.osmMapView
        mapa.setMultiTouchControls(true)
        binding.teammatesTeammates.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentTripTeammates_to_fragmentTripTeammates2)
        }
        binding.HomeButtonTrip.setOnClickListener {
            var intent = Intent(context, ActivityMain::class.java)
            startActivity(intent)
        }
    }

    private fun setMyLocationOverlay()
    {
        mapa.controller.setZoom(15)
        //mapa.controller.setCenter(GeoPoint(44.0333, 20.8))
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity),mapa)
        myLocationOverlay.enableMyLocation()
        mapa.overlays.add(myLocationOverlay)
        mapController = mapa.controller;
        if(mapController != null)
        {
            myLocationOverlay.enableFollowLocation()

        }

    }


    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    {
        isGranted:Boolean ->
            if(isGranted)
            {
                setMyLocationOverlay()
            }
    }

    override fun onPause()
    {
        super.onPause()
        mapa.onPause()
        listaReferenci.clear()
    }

}