package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentRegisterHurrayBinding


class FragmentRegisterHurray : Fragment()
{

    private lateinit var binding: FragmentRegisterHurrayBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = FragmentRegisterHurrayBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonGoBack.setOnClickListener {
            findNavController().navigate(R.id.frRegHurray_to_frLogin)
        }
    }

}