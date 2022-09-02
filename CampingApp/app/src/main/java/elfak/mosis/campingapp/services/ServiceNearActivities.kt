package elfak.mosis.campingapp.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location

import android.os.IBinder
import android.os.Looper
import android.widget.Toast


import android.os.*
import android.os.Process
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.ActivityTrip
import elfak.mosis.campingapp.classes.Notifications
import elfak.mosis.campingapp.classes.NotificationsFriend
import elfak.mosis.campingapp.classes.NotificationsTrip
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class ServiceNearActivities : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private var aktivnosti = ArrayList<ActivityTrip>()
    private lateinit var tripName: String
    private lateinit var tridID:String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null
    private lateinit var nit: Thread

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper)
    {

        override fun handleMessage(msg: Message)
        {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try
            {
                nit = Thread.currentThread()
                while (true)
                {
                    Thread.sleep(5000)
                }
            }
            catch (e: InterruptedException)
            {
                // Restore interrupt status.
                Thread.currentThread().interrupt()
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate()
    {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            smallestDisplacement = 100f
        }
        locationCallback = object : LocationCallback()
        {
            override fun onLocationResult(p0: LocationResult)
            {
                super.onLocationResult(p0)
                currentLocation = p0.lastLocation

                for(a in aktivnosti)
                {
                    var tmp = FloatArray(3)
                    Location.distanceBetween(a.latitude, a.longitude, currentLocation!!.latitude, currentLocation!!.longitude, tmp)
                    if (tmp[0] < 100)
                    {
                        val i = Intent("notifikacije")
                        i.putExtra("tip", "Blizina")
                        i.putExtra("razdaljina", tmp[0])
                        i.putExtra("aIme", a.title)
                        i.putExtra("aTip", a.type)
                        i.putExtra("aID", a.ID)
                        i.putExtra("tripID", tridID)
                        sendBroadcast(i)
                    }
                }
            }
        }

        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        try
        {
            //Toast.makeText(this, "ServiceNearActivities starting", Toast.LENGTH_SHORT).show()

            tripName = intent.extras!!.getString("tripN")!!
            tridID = intent.extras!!.getString("tripID")!!
            var aIDs = intent.extras!!.getStringArray("aID")
            var aNames = intent.extras!!.getStringArray("aName")
            var aCoors = intent.extras!!.getDoubleArray("aCoor")
            var aTypes = intent.extras!!.get("aTypes") as Array<Int>
            var j = 0

            for ((i, v) in aIDs!!.withIndex())
            {
                aktivnosti.add(ActivityTrip(v, "", aNames!![i], "", aCoors!![j++], aCoors!![j++], aTypes!![i]))
            }

            if (ActivityCompat.checkSelfPermission(this@ServiceNearActivities, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this@ServiceNearActivities, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {

            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

            // For each start request, send a message to start a job and deliver the
            // start ID so we know which request we're stopping when we finish the job
            serviceHandler?.obtainMessage()?.also { msg ->
                msg.arg1 = startId
                serviceHandler?.sendMessage(msg)
            }
        }
        catch (e:Exception)
        {

        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder?
    {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy()
    {
        //Toast.makeText(this, "ServiceNearActivities done", Toast.LENGTH_SHORT).show()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        nit?.interrupt()
        super.onDestroy()
    }
}
