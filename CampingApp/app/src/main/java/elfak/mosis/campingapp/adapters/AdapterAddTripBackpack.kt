package elfak.mosis.campingapp.adapters

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.sharedViews.SharedViewTripForm

class AdapterAddTripBackpack(val ct: Context, val BackpackItems: ArrayList<BackpackItems>, val shared: SharedViewTripForm) : RecyclerView.Adapter<AdapterAddTripBackpack.BackpackViewHolder>() {

    class BackpackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val naziv: TextView = itemView.findViewById(R.id.teammate_name)
        val occupation: TextView = itemView.findViewById(R.id.occupation_text)
        val slika: ImageView = itemView.findViewById(R.id.image_teammate)
        val add: ImageView = itemView.findViewById(R.id.add_button)

    }
}