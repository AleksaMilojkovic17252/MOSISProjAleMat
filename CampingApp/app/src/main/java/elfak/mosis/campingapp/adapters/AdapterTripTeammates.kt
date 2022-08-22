package elfak.mosis.campingapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.sharedViews.SharedViewHome

class AdapterTripTeammates(val ct: Context, val Users: ArrayList<User>?, val Izvrsio: HashMap<String,ArrayList<String>>,val pomoc:MoveAgain) :
    RecyclerView.Adapter<AdapterTripTeammates.ViewHolder>() {

    interface MoveAgain{
        fun Move(SelectedUser: User)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ime: TextView = itemView.findViewById(R.id.teammate_trip_name)
        val slika: ImageView = itemView.findViewById(R.id.image_trip_teammate)
        val point: TextView = itemView.findViewById(R.id.points)
        val mainLayout: ConstraintLayout = itemView.findViewById(R.id.teammate_trip_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val hallo: LayoutInflater = LayoutInflater.from(ct)
        val view: View = hallo.inflate(R.layout.row_trip_teammates,parent,false )
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ime.text = Users?.get(position)?.Name
        Firebase.storage.getReference("profilePics/${Users?.get(position)?.Slika}.jpg").downloadUrl.addOnSuccessListener { uri ->
            Glide.with(ct).load(uri).into(holder.slika)
        }
        holder.point.text = "Points: " + (Izvrsio[Users?.get(position)?.ID]?.count() ?: -1)
        holder.mainLayout.setOnClickListener {
            pomoc.Move(Users!!.get(position))
        }

    }

    override fun getItemCount(): Int {
        return Users?.count() ?: 0
    }
}