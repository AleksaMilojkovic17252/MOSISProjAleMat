package elfak.mosis.campingapp.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.widget.Toast


import android.os.*
import android.os.Process
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import java.util.concurrent.TimeUnit

class ServiceSendLocation : Service()
{

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private lateinit var nit : Thread
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null


    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper)
    {

        override fun handleMessage(msg: Message)
        {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            nit = Thread.currentThread()

            if (ActivityCompat.checkSelfPermission(this@ServiceSendLocation, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this@ServiceSendLocation, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            )
            { }
            while (true)
            {
////                try
////                {
////                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener { loc ->
////                        var lat = loc.latitude
////                        var long = loc.longitude
////                        var id = Firebase.auth.currentUser!!.uid
////                        Firebase.database("https://mosisprojekat-2b28d-default-rtdb.europe-west1.firebasedatabase.app/")
////                            .getReference("locations/$id").setValue(mapOf("lat" to lat, "long" to long)).addOnSuccessListener {
////
////                            }
////                    }
////                    fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
////
////                    }
//                }
//                catch (e: InterruptedException)
//                {
//                    // Restore interrupt status.
//                    Thread.currentThread().interrupt()
//                }
//                Thread.sleep(5000)
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

                var lat = p0.lastLocation?.latitude
                var long = p0.lastLocation?.longitude
                var id = Firebase.auth.currentUser!!.uid
                Firebase.database("https://mosisprojekat-2b28d-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("locations/$id").setValue(mapOf("lat" to lat, "long" to long)).addOnSuccessListener {

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
        Toast.makeText(this, "ServiceSendLoc start", Toast.LENGTH_SHORT).show()

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        if (ActivityCompat.checkSelfPermission(this@ServiceSendLocation, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this@ServiceSendLocation, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
        { }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

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
        Toast.makeText(this, "ServiceSendLoc done", Toast.LENGTH_SHORT).show()
        nit.interrupt()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }

}