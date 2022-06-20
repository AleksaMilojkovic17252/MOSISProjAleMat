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
import elfak.mosis.campingapp.classes.User

class AdapterAddTripAllTeammates(val ct: Context, val Users: ArrayList<User>, val shared: SharedViewTripForm, val nesto: Sotka) :
    RecyclerView.Adapter<AdapterAddTripAllTeammates.TeammateViewHolder>() {


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
        val hallo: LayoutInflater = LayoutInflater.from(ct)
        val view: View = hallo.inflate(R.layout.row_trip_add_form_add_teammate,parent,false )
        return TeammateViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeammateViewHolder, position: Int) {
        holder.ime.text = Users.get(position).Name
        holder.occupation.text = Users.get(position).Occupation
        holder.slika.setImageResource(Users[position].Slika.toInt())
        holder.add.setOnClickListener{
            shared.korisnici.add(Users[position])
            shared.dataChanger.value = !shared.dataChanger.value!!
            nesto.goBack()

        }

    }

    override fun getItemCount(): Int {
        return Users.count()
    }


}