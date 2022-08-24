package elfak.mosis.campingapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

import android.os.IBinder
import android.widget.Toast


import android.os.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.*
import kotlin.random.Random


class ServicePushNotification : Service()
{
//    private var serviceLooper: Looper? = null
//    private var serviceHandler: ServiceHandler? = null
//    private lateinit var nit : Thread
    private val primac: BroadcastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(p0: Context?, p1: Intent?)
        {
            if (p1?.extras?.getString("tip") == "Request")
            {
                var koSalje = p1.extras?.getString("OdKog")
                var koSaljeID = p1.extras?.getString("OdKogID")
                var notif = NotificationsFriend(koSalje!!, koSaljeID!!)

                pustiPopUp(notif)

            }
            else if (p1?.extras?.getString("tip") == "Trip")
            {
                var kojiTrip = p1.extras?.getString("trip")
                var notif = NotificationsTrip(kojiTrip!!)

                pustiPopUp(notif)
            }
            else if(p1?.extras?.getString("tip") == "Blizina")
            {
                var ime = p1.extras?.getString("aIme")
                var razdaljina = p1.extras?.getFloat("razdaljina")
                var tip = p1.extras?.getInt("aTip")

                var notif = NotificationsNearActivity(ime!!, razdaljina!!.toDouble(), tip!!)
                pustiPopUp(notif)
            }
        }

    }

    private fun pustiPopUp(notif: Notifications) {
        var fja =
            {
                if (notif is NotificationsTrip)
                    "New Trip"
                else if (notif is NotificationsFriend)
                    "New teammate request"
                else if(notif is NotificationsNearActivity)
                {
                    var tip = when(notif.tip)
                    {
                        ActivityTrip.NICE_VIEW -> "Nice view"
                        ActivityTrip.POINT_OF_INTEREST -> "Point of interest"
                        ActivityTrip.SHELTER -> "Shelter"
                        else -> "Activity"
                    }
                    "$tip is near"
                }
                else
                    "New notification"
            }

        var fja2 =
            {
                if (notif is NotificationsTrip)
                    "You have been invited to a trip " + notif.data
                else if (notif is NotificationsFriend)
                    notif.data + " added you as friend"
                else if (notif is NotificationsNearActivity)
                {
                    var ime = notif.data
                    var raz = String.format("%.2f",notif.razdaljina)
                    
                    "$ime is $raz meters from you"
                }
                else
                    "New message"
            }

        createNotificationChannel()

        var builder = NotificationCompat.Builder(this, "MojeKanalce")
            .setSmallIcon(R.mipmap.ikonica)
            .setContentTitle(fja())
            .setContentText(fja2())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(this))
        {
            // notificationId is a unique int for each notification that you must define
            notify(Random.nextInt(0, Int.MAX_VALUE), builder.build())
        }
    }

    private fun createNotificationChannel()
    {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "CampingAppChannel"
            val descriptionText = "NekiOpis"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("MojeKanalce", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Handler that receives messages from the thread
//    private inner class ServiceHandler(looper: Looper) : Handler(looper)
//    {
//
//        override fun handleMessage(msg: Message)
//        {
//            // Normally we would do some work here, like download a file.
//            // For our sample, we just sleep for 5 seconds.
//            try
//            {
//                nit = Thread.currentThread()
//                while (true)
//                {
////                    Thread.sleep(5000)
//                }
//            }
//            catch (e: InterruptedException)
//            {
//                // Restore interrupt status.
//                Thread.currentThread().interrupt()
//            }
//
//            // Stop the service using the startId, so that we don't stop
//            // the service in the middle of handling another job
//            stopSelf(msg.arg1)
//        }
//    }

    override fun onCreate()
    {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
//        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
//            start()
//
//            // Get the HandlerThread's Looper and use it for our Handler
//            serviceLooper = looper
//            serviceHandler = ServiceHandler(looper)
//        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        Toast.makeText(this, "ServicePushNotif starting", Toast.LENGTH_SHORT).show()

        registerReceiver(primac, IntentFilter("notifikacije"))
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
//        serviceHandler?.obtainMessage()?.also { msg ->
//            msg.arg1 = startId
//            serviceHandler?.sendMessage(msg)
//        }

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
        Toast.makeText(this, "ServicePushNotif dying", Toast.LENGTH_SHORT).show()
        unregisterReceiver(primac)
//        nit.interrupt()
    }





}