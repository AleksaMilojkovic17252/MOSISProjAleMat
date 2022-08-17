package elfak.mosis.campingapp.adapters

import android.content.Context
import android.icu.text.Transliterator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.sharedViews.SharedViewHome


class AdapterAddTripBackpack(val ct: Context, val shared: SharedViewHome) : RecyclerView.Adapter<AdapterAddTripBackpack.BackpackViewHolder>() {

    class BackpackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val naziv: TextView = itemView.findViewById(R.id.text_backpack_name_add_trip)
        val remove: ImageView = itemView.findViewById(R.id.imageButtonMinus)
        val ammount: TextView = itemView.findViewById(R.id.number_of_items)
        val add: ImageView = itemView.findViewById(R.id.imageButtonAdd)
        val delete: ImageView = itemView.findViewById(R.id.imageButtonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackpackViewHolder {
        val hallo: LayoutInflater = LayoutInflater.from(ct)
        val view: View = hallo.inflate(R.layout.row_trip_add_form_backpack_items,parent,false )
        return AdapterAddTripBackpack.BackpackViewHolder(view)
    }

    override fun onBindViewHolder(holder: BackpackViewHolder, position: Int) {
        holder.naziv.text = shared.backpackItems[position].name
        holder.ammount.text = shared.backpackItems[position].items.toString()

        holder.remove.setOnClickListener{
            shared.backpackItems[position].items = shared.backpackItems[position].items - 1
            notifyItemChanged(position)
        }
        holder.add.setOnClickListener{
            shared.backpackItems[position].items = shared.backpackItems[position].items + 1
            notifyItemChanged(position)
        }

        holder.delete.setOnClickListener{
            shared.backpackItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,shared.backpackItems.count())
        }
    }

    override fun getItemCount(): Int {
        return shared.backpackItems.count()
    }
}