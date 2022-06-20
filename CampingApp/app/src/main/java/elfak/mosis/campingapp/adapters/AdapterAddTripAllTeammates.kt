package elfak.mosis.campingapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.sharedViews.SharedViewTripForm
import androidx.navigation.fragment.findNavController

class AdapterAddTripAllTeammates(val ct: Context,val s1: ArrayList<String>, val s2: ArrayList<String> , val img: ArrayList<Int> , val model: SharedViewTripForm, val nesto: Sotka) :
    RecyclerView.Adapter<AdapterAddTripAllTeammates.TeammateViewHolder>() {

    val context = ct
    val data1 = s1
    val data2 = s2
    val images = img
    val shared = model


    interface Sotka{
        fun goBack()
    }

    class TeammateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ime: TextView = itemView.findViewById(R.id.teammate_name)
        val occupation: TextView = itemView.findViewById(R.id.occupation_text)
        val slika: ImageView = itemView.findViewById(R.id.image_teammate)
        val add: ImageView = itemView.findViewById(R.id.add_button)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeammateViewHolder {
        val hallo: LayoutInflater = LayoutInflater.from(context)
        val view: View = hallo.inflate(R.layout.row_trip_add_form_add_teammate,parent,false )
        return TeammateViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeammateViewHolder, position: Int) {
        holder.ime.text = data1.get(position)
        holder.occupation.text = data2.get(position)
        holder.slika.setImageResource(images[position])
        holder.add.setOnClickListener{
            shared.imena.add(data1[position])
            shared.dataChanger.value = !shared.dataChanger.value!!
            nesto.goBack()

        }

    }

    override fun getItemCount(): Int {
        return data1.count()
    }


}