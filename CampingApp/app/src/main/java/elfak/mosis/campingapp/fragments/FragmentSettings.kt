package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentSettingsBinding

class FragmentSettings: Fragment()
{
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) 
    {
        super.onViewCreated(view, savedInstanceState)
        binding.linearLayout2.setOnClickListener {
            Toast.makeText(context, "All trips", Toast.LENGTH_SHORT).show()
        }
        binding.linearLayout3.setOnClickListener {
            Toast.makeText(context, "DELETE ALL", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume()
    {
        super.onResume()
        val title: TextView = requireActivity().findViewById(R.id.toolbar_title)
        title.text = getString(R.string.settings_label)
    }
}