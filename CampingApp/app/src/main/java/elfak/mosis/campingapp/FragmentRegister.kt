package elfak.mosis.campingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import elfak.mosis.campingapp.databinding.FragmentRegisterBinding

class FragmentRegister : Fragment()
{
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.registerRegisterLogin.setOnClickListener {
            findNavController().navigate(R.id.frReg_to_frLogin)
        }
    }

}