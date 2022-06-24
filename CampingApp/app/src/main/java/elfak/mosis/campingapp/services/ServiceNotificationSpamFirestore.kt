package elfak.mosis.campingapp.services

import android.app.Service
import android.content.Intent
import android.os.*
import android.os.Process
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ServiceNotificationSpamFirestore : Service()
{
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    private inner class ServiceHandler(looper: Looper) : Handler(looper)
    {
        override fun handleMessage(msg: Message)
        {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try
            {
                while (true)
                {
                    Firebase.firestore
                        .collection("requestus")
                        .whereEqualTo("to", Firebase.auth.currentUser!!.uid)
                        .whereEqualTo("processed", true)
                        .get().addOnSuccessListener {
                            Log.d("CampingApp", it.documents.size.toString())
                        }
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
        super.onCreate()
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply{
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()
        Log.d("CampingApp", "AJDEEEE")
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also{ msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
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
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }


}


