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

class AdapterAddTripTeammate (val ct: Context, val s1: ArrayList<String>, val img: ArrayList<Int>) : RecyclerView.Adapter<AdapterAddTripTeammate.MyViewHolder>(){

    val context = ct
    val data1 = s1
    val images = img

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
        holder.ime.setText(data1[position])
        holder.slika.setImageResource(images[position])
        holder.mainLayout.setOnClickListener{
            val dialog: Dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.add_trips_form_bottomsheetlayout)

            val remove: LinearLayout = dialog.findViewById(R.id.layoutRemove)
            val cancel: LinearLayout = dialog.findViewById(R.id.layoutCancel)

            remove.setOnClickListener{

                Toast.makeText(context,"Remove",Toast.LENGTH_SHORT).show()
                this.data1.removeAt(position)
                this.images.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,s1.count())
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
        return s1.count()
    }
}