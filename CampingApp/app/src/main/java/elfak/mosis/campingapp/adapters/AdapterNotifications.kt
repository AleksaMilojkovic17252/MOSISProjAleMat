package elfak.mosis.campingapp.adapters

import android.content.Context
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
            accept.setOnClickListener {
                //TODO:DODAJ PRIJATELJA
                Toast.makeText(ct,"Friend Accepted",Toast.LENGTH_SHORT)
                Log.d("Accepted","Kliknuto")
            }
            decline.setOnClickListener {
                //TODO:ODBACI PRIJATELJA
                Toast.makeText(ct,"Friend Declined",Toast.LENGTH_SHORT)
                Log.d("Decline","Kliknuto")
            }
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