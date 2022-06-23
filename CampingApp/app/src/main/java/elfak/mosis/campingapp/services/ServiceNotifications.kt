package elfak.mosis.campingapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import elfak.mosis.campingapp.R

class ServiceNotifications : FirebaseMessagingService()
{
    override fun onNewToken(token: String)
    {
        super.onNewToken(token)

        Firebase.firestore
            .collection("users")
            .document(Firebase.auth.currentUser!!.uid)
            .update("messToken", token)

    }

    override fun onCreate()
    {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onMessageReceived(message: RemoteMessage)
    {
        super.onMessageReceived(message)
        Log.d("CampingApp", message.notification?.body ?: "mrk")
        var builder = NotificationCompat.Builder(this,"MojeKanalce")
            .setSmallIcon(R.mipmap.ikonica)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(this))
        {
            // notificationId is a unique int for each notification that you must define
            notify(12316465, builder.build())
        }

    }

    private fun createNotificationChannel() {
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

}