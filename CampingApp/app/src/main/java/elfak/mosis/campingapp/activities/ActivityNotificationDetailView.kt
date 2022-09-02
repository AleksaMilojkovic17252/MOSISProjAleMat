package elfak.mosis.campingapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.ActivityTrip
import elfak.mosis.campingapp.databinding.ActivityNotificationDetailViewBinding
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.HashMap

class ActivityNotificationDetailView : AppCompatActivity()
{
    private lateinit var mapa: MapView
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var mapController: IMapController
    private lateinit var locationMarker: Marker
    private lateinit var binding: ActivityNotificationDetailViewBinding
    private lateinit var activity: ActivityTrip
    private lateinit var activityComplete: MutableMap<String,ArrayList<String>>
    private lateinit var task:Task<DocumentSnapshot>
    private lateinit var koJeNaptavio: String

    override fun onCreate(savedInstanceState: Bundle?)
    {
        var user = Firebase.auth.currentUser

        super.onCreate(savedInstanceState)
        binding = ActivityNotificationDetailViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var context = applicationContext;
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context!!))
        mapa = binding.osmMapView
        mapa.setMultiTouchControls(true)
        mapController = mapa.controller

        var aID = intent.extras?.getString("aID")
        var tripID = intent.extras?.getString("staDaUcitas")

        binding.buttonComplete.setOnClickListener {
            Firebase.firestore
                .collection(getString(R.string.db_coll_trips))
                .document(tripID!!)
                .update("completedActivities.${Firebase.auth.currentUser!!.uid}", FieldValue.arrayUnion(
                    activity.ID))
                .addOnSuccessListener {
                    Toast.makeText(this, "You did it!", Toast.LENGTH_SHORT).show()
                }
        }

        if(aID != null && tripID != null)
        {
            task = Firebase.firestore
                .collection(getString(R.string.db_coll_trips))
                .document(tripID)
                .get()
            task.addOnSuccessListener {

                var tmp2 = it["completedActivities"] as HashMap<*, *>
                var zavrseneAktivnosti = HashMap<String, ArrayList<String>>()
                for (i in tmp2)
                {
                    var key = i.key.toString()
                    var value = i.value
                    if (value == null)
                        value = ArrayList<String>()
                    zavrseneAktivnosti[key] = value as ArrayList<String>
                }
                activityComplete = zavrseneAktivnosti

                var aktivnosti = it["activities"] as ArrayList<Map<String, Object>>
                var aktivnost = aktivnosti.find { x -> x.getValue("id").toString() == aID }!!
                activity = ActivityTrip(aktivnost["id"].toString(),
                        aktivnost["koJeNapravio"].toString(),
                        aktivnost["title"].toString(),
                        aktivnost["description"].toString(),
                        aktivnost["latitude"].toString().toDouble(),
                        aktivnost["longitude"].toString().toDouble(),
                        aktivnost["type"].toString().toInt())

                var taskic = Firebase.firestore
                    .collection(getString(R.string.db_coll_users))
                    .document(activity.koJeNapravio)
                    .get()
                taskic.addOnSuccessListener {itic ->
                    koJeNaptavio = itic["name"] as String
                }

                Tasks.whenAll(taskic).addOnSuccessListener {
                    setujViews()
                    crtajMapu()
                }
            }

        }

    }

    private fun setujViews()
    {
        binding.naslov.text = activity.title
        binding.opis.text = activity.description
        binding.from.text = "From $koJeNaptavio"

    }

    private fun crtajMapu()
    {
        mapa.overlays.clear()
        mapa.controller.setZoom(15.0)
        mapa.controller.setCenter(GeoPoint(activity.latitude, activity.longitude))
        var startMarker = Marker(mapa)
        when(activity.type)
        {
            1 ->
                startMarker.icon =
                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_location_nice_view_48)
            2->
                startMarker.icon =
                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_location_on_shelter_48)
            3->
                startMarker.icon =
                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_location_on_poi_48)
        }
        if(activity.ID in activityComplete[Firebase.auth.currentUser!!.uid]!!)
        {
            binding.buttonComplete.visibility = View.GONE
            binding.completed.visibility = View.VISIBLE
            startMarker.icon = ContextCompat.getDrawable(this,R.drawable.ic_baseline_location_on_completed_48)
        }
        else
        {
            binding.buttonComplete.visibility = View.VISIBLE
            binding.completed.visibility = View.GONE
        }
        startMarker.infoWindow = null
        startMarker.position = GeoPoint(activity.latitude, activity.longitude)
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapa.overlays.add(startMarker);
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


}