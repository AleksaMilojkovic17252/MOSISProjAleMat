package elfak.mosis.campingapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.sharedViews.SharedViewTrip

class AdapterTripTeammateBackpack(val ct: Context, val allBackpackItems: ArrayList<BackpackItems>?) : RecyclerView.Adapter<AdapterTripTeammateBackpack.BackpackViewHolder>() {

    class BackpackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val naziv: TextView = itemView.findViewById(R.id.backpack_item_name)
        val ammount: TextView = itemView.findViewById(R.id.number_of_items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackpackViewHolder {
        val hallo: LayoutInflater = LayoutInflater.from(ct)
        val view: View = hallo.inflate(R.layout.row_backpack_item_friend,parent,false )
        return BackpackViewHolder(view)
    }

    override fun onBindViewHolder(holder: BackpackViewHolder, position: Int) {
        holder.naziv.text = allBackpackItems?.get(position)?.name ?: ""
        holder.ammount.text = allBackpackItems?.get(position)?.items?.toString() ?: "0"
    }

    override fun getItemCount(): Int {
        return allBackpackItems?.count() ?: 0
    }
}