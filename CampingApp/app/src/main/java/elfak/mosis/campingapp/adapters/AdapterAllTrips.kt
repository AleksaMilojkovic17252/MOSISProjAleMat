package elfak.mosis.campingapp.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityTrip
import elfak.mosis.campingapp.classes.Trip
import elfak.mosis.campingapp.classes.User
import java.lang.Exception
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AdapterAllTrips(val ct: Context, val trips: MutableList<Trip>?, val listener: Koriscenje) : RecyclerView.Adapter<AdapterAllTrips.ViewHolder>() {

    interface Koriscenje
    {
        fun pocniTrip(trip:Trip)
    }

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val slika: ImageView = itemView.findViewById(R.id.imageHolder)
        val trip_name: TextView = itemView.findViewById(R.id.trip_name)
        val numberOfTeammates: TextView = itemView.findViewById(R.id.teammates_number)
        val startDate: TextView = itemView.findViewById(R.id.date_start)
        val endDate: TextView = itemView.findViewById(R.id.date_end)
        val trip: ConstraintLayout = itemView.findViewById(R.id.row_trip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val hallo: LayoutInflater = LayoutInflater.from(ct)
        val view: View = hallo.inflate(R.layout.row_trip,parent,false)
        return ViewHolder(view)
    }

    //@RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.trip_name.text = trips?.get(position)?.tripName ?: ""
        //var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        holder.numberOfTeammates.text = trips?.get(position)?.users?.count().toString()
        holder.startDate.text = DateFormat.getDateInstance().format(trips?.get(position)?.startDate)
        holder.endDate.text = DateFormat.getDateInstance().format(trips?.get(position)?.endDate)
        //srediSliku
            if (trips?.get(position)?.slika != "")
            {
                Firebase.storage
                    .getReference("trips/${trips?.get(position)?.id}/${trips?.get(position)?.slika}")
                    .downloadUrl
                    .addOnSuccessListener {
                        Glide.with(ct).load(it).into(holder.slika)
                    }
            }


        holder.trip.setOnClickListener{
            trips?.let { it1 -> listener.pocniTrip(it1.get(position)) }
        }


    }

    override fun getItemCount(): Int {
        return trips?.count() ?: 0
    }
}
