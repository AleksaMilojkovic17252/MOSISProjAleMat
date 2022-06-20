package elfak.mosis.campingapp.adapters

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.sharedViews.SharedViewTripForm

class AdapterAddTripTeammate (val ct: Context, val Users: ArrayList<User>, val model: SharedViewTripForm) : RecyclerView.Adapter<AdapterAddTripTeammate.MyViewHolder>(){

    val context = ct

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ime: TextView = itemView.findViewById(R.id.Teammate_name)
        val slika: ImageView = itemView.findViewById(R.id.Teammate_image)
        val mainLayout: ConstraintLayout = itemView.findViewById(R.id.friend_cards_small)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterAddTripTeammate.MyViewHolder {
        val hallo: LayoutInflater = LayoutInflater.from(context)
        val view: View = hallo.inflate(R.layout.teammate_trip_add_form,parent,false )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterAddTripTeammate.MyViewHolder, position: Int) {
        holder.ime.setText(Users[position].Name)
        holder.slika.setImageResource(Users[position].Slika.toInt())
        holder.mainLayout.setOnClickListener{
            val dialog: Dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.add_trips_form_bottomsheetlayout)

            val remove: LinearLayout = dialog.findViewById(R.id.layoutRemove)
            val cancel: LinearLayout = dialog.findViewById(R.id.layoutCancel)

            remove.setOnClickListener{
                this.Users.removeAt(position)
                this.model.korisnici.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,Users.count())
                dialog.dismiss()
            }

            cancel.setOnClickListener{
                Toast.makeText(context,"Cancel",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            dialog.show()
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
        }
    }

    override fun getItemCount(): Int {
        return Users.count()
    }
}