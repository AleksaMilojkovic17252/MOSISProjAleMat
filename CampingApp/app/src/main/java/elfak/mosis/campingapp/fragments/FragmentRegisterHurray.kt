package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elfak.mosis.campingapp.databinding.FragmentForgotPasswordHurrayBinding
import elfak.mosis.campingapp.databinding.FragmentRegisterOtpBinding


class FragmentRegisterHurray : Fragment() {

    private lateinit var binding: FragmentForgotPasswordHurrayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordHurrayBinding.inflate(layoutInflater)
        return binding.root
    }


}