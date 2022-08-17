package elfak.mosis.campingapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.ActivityTrip
import elfak.mosis.campingapp.classes.Trip
import elfak.mosis.campingapp.classes.User

class AdapterAllActivities(val ct: Context, val activities: ArrayList<ActivityTrip>?, val friends: ArrayList<User>) : RecyclerView.Adapter<AdapterAllActivities.ViewHolder>() {
    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val ceoView:ConstraintLayout = itemView.findViewById(R.id.row_activity)
        val naslov:TextView = itemView.findViewById(R.id.naslov)
        val fromWho:TextView = itemView.findViewById(R.id.from)
        val desc:TextView = itemView.findViewById(R.id.opis)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val hallo: LayoutInflater = LayoutInflater.from(ct)
        val view: View = hallo.inflate(R.layout.row_trip_activities,parent,false)
        return AdapterAllActivities.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.naslov.text = activities?.get(position)?.title
        holder.fromWho.text = friends.find { x-> x.ID == activities?.get(position)?.koJeNapravio }?.Name
        holder.desc.text = activities?.get(position)?.description

        holder.ceoView.setOnClickListener{
            //posalji na drugi fragment
        }


    }

    override fun getItemCount(): Int {
        return activities?.count() ?: 0
    }


}