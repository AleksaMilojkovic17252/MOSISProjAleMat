package elfak.mosis.campingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import elfak.mosis.campingapp.databinding.FragmentHomeBinding
import elfak.mosis.campingapp.databinding.FragmentLoginBinding

class FragmentHome : Fragment()
{
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) 
    {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            Toast.makeText(context, "Kliknut sam", Toast.LENGTH_SHORT).show()
        }

        binding.imageViewMenuButton.setOnClickListener {

        }

        binding.imageViewNotificationButton.setOnClickListener {
            findNavController().navigate(R.id.frHome_to_frNotification)
        }

    }


}