package elfak.mosis.campingapp.adapters

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
import elfak.mosis.campingapp.classes.BackpackItems

class AdapterMemories(val ct: Context, val allMemories: MutableList<String>?, val tripID: String) : RecyclerView.Adapter<AdapterMemories.ViewHolder>() {

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val slika: ImageView = itemView.findViewById(R.id.memory_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val hallo: LayoutInflater = LayoutInflater.from(ct)
        val view: View = hallo.inflate(R.layout.row_trip_memories,parent,false )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Firebase.storage.getReference("trips/${tripID}/${allMemories?.get(position)}").downloadUrl.addOnSuccessListener { uri ->
            Glide.with(ct).load(uri).into(holder.slika)
        }
    }

    override fun getItemCount(): Int {
        return allMemories?.count() ?: 0
    }

}