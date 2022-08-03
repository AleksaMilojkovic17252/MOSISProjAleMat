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
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.User

class AdapterAllTeammates(val ct: Context, val users: ArrayList<User>?, val listener: Pomoc ) : RecyclerView.Adapter<AdapterAllTeammates.ViewHolder>(){

    interface Pomoc
    {
        fun pomeraj(position: User)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val slika: ImageView = itemView.findViewById(R.id.image_all_teammate)
        val ime: TextView = itemView.findViewById(R.id.teammate_all_name)
        val posao: TextView = itemView.findViewById(R.id.occupation_all_text)
        val mainLayout: ConstraintLayout = itemView.findViewById(R.id.all_teammates_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val hallo: LayoutInflater = LayoutInflater.from(ct)
        val view: View = hallo.inflate(R.layout.row_teammates_home,parent,false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ime.text = users?.get(position)?.Name
        holder.posao.text = users?.get(position)?.Occupation
        Glide.with(ct).load(users?.get(position)?.Slika).into(holder.slika)

        holder.mainLayout.setOnClickListener{
            users?.get(position)?.let { it1 -> listener.pomeraj(it1) }
        }
    }

    override fun getItemCount(): Int {
        return users?.count() ?: 0
    }


}