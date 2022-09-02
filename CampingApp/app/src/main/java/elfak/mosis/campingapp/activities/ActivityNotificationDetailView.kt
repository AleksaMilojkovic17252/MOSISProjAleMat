package elfak.mosis.campingapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.Toolbar
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.ActivityTrip
import elfak.mosis.campingapp.databinding.ActivityNotificationDetailViewBinding
import org.osmdroid.api.IMapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class ActivityNotificationDetailView : AppCompatActivity() {
    private lateinit var mapa: MapView
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var mapController: IMapController
    private lateinit var locationMarker: Marker
    private lateinit var binding: ActivityNotificationDetailViewBinding
    private lateinit var activity: ActivityTrip
    private lateinit var activityComplete: MutableMap<String,ArrayList<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_detail_view)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        binding = ActivityNotificationDetailViewBinding.inflate(layoutInflater)
        return binding.root
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