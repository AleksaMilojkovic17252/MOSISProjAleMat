package elfak.mosis.campingapp.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityAddFriends
import elfak.mosis.campingapp.databinding.FragmentTeammatesBinding

class FragmentTeammates : Fragment()
{
    private lateinit var binding: FragmentTeammatesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentTeammatesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume()
    {
        super.onResume()
        val title: TextView = requireActivity().findViewById(R.id.toolbar_title)
        title.text = "Teammates"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAddTeammate.setOnClickListener{
            var intent = Intent(context, ActivityAddFriends::class.java)
            startActivity(intent);
        }
        binding.textViewIDNumber.text = "#00001"

        binding.buttonCopyToClipboard.setOnClickListener {
            var clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager ?: null
            var clip = ClipData.newPlainText("Copied ID", binding.textViewIDNumber.text.toString())
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(context, "ID copied to clipboard!", Toast.LENGTH_SHORT).show()
        }
    }

}