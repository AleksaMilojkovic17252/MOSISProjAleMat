package elfak.mosis.campingapp.adapters

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.Notifications
import elfak.mosis.campingapp.classes.NotificationsFriend
import elfak.mosis.campingapp.classes.NotificationsTrip

class AdapterNotifications (val ct: Context, val notifications: ArrayList<Notifications>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class FriendViewHolder(itemView: View, val ct: Context) : RecyclerView.ViewHolder(itemView) {
        val ime: TextView = itemView.findViewById(R.id.friend_name_text)
        val accept: Button = itemView.findViewById(R.id.notification_accept_button)
        val decline: Button = itemView.findViewById(R.id.notification_decline_button)

        fun bind(friend: NotificationsFriend)
        {
            ime.text = friend.data

        }

    }

    class TripViewHolder(itemView: View, val ct: Context) : RecyclerView.ViewHolder(itemView) {
        val ime: TextView = itemView.findViewById(R.id.trip_name_text)

        fun bind(notif: NotificationsTrip)
        {
            ime.text = notif.data
        }

    }

    private val ITEM_TYPE_FRIEND = 0
    private val ITEM_TYPE_TRIP = 1

    override fun getItemViewType(position: Int): Int {
        if(notifications.get(position) is NotificationsFriend)
        {
            return ITEM_TYPE_FRIEND
        }
        else
        {
            return ITEM_TYPE_TRIP
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        if(viewType == ITEM_TYPE_FRIEND)
        {
            val view: View = LayoutInflater.from(ct).inflate(R.layout.row_notifications_friend,parent,false)
            return FriendViewHolder(view,ct)
        }
        else
        {
            val view: View = LayoutInflater.from(ct).inflate(R.layout.row_notification_trip,parent,false)
            return TripViewHolder(view,ct)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notif: Notifications = notifications[position]

        if(holder is FriendViewHolder)
        {
            (holder as FriendViewHolder).bind(notif as NotificationsFriend)
            holder.accept.setOnClickListener {
                //TODO:DODAJ PRIJATELJA
                Toast.makeText(ct,"Friend Accepted",Toast.LENGTH_SHORT)
                Log.d("Accepted","Kliknuto")

                Firebase.firestore
                    .collection("users")
                    .document(Firebase.auth.currentUser!!.uid)
                    .update("friends", FieldValue.arrayUnion(notif.ID))

                Firebase.firestore
                    .collection("users")
                    .document(notif.ID)
                    .update("friends", FieldValue.arrayUnion(Firebase.auth.currentUser!!.uid))

                Firebase.firestore
                    .collection("requests")
                    .whereEqualTo("from", notif.ID)
                    .whereEqualTo("to", Firebase.auth.currentUser!!.uid)
                    .whereEqualTo("processed",true)
                    .get()
                    .addOnSuccessListener {
                        for (doc in it)
                        {
                            Firebase.firestore
                                .collection("requests")
                                .document(doc.id)
                                .delete()
                        }
                        notifications.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position,notifications.count())
                    }

                var intent = Intent("AjdProradi")
                intent.putExtra("prijatelj", notif.ID)
                intent.putExtra("tip", "NoviPrijatelj")
                ct.sendBroadcast(intent)
            }
            holder.decline.setOnClickListener {
                //TODO:ODBACI PRIJATELJA
                Toast.makeText(ct,"Friend Declined",Toast.LENGTH_SHORT)
                Log.d("Decline","Kliknuto")
                Firebase.firestore
                    .collection("requests")
                    .whereEqualTo("from", notif.ID)
                    .whereEqualTo("to", Firebase.auth.currentUser!!.uid)
                    .whereEqualTo("processed",true)
                    .get()
                    .addOnSuccessListener {
                        for (doc in it)
                        {
                            Firebase.firestore
                                .collection("requests")
                                .document(doc.id)
                                .delete()
                        }
                        notifications.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position,notifications.count())
                    }
            }
        }
        else
        {
            (holder as TripViewHolder).bind(notif as NotificationsTrip)
        }
    }


    override fun getItemCount(): Int {
        return notifications.count()
    }
}