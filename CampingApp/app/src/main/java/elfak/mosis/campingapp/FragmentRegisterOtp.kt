package elfak.mosis.campingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import elfak.mosis.campingapp.databinding.FragmentForgotPasswordNewPasswordBinding
import elfak.mosis.campingapp.databinding.FragmentForgotPasswordOtpBinding
import elfak.mosis.campingapp.databinding.FragmentRegisterOtpBinding


class FragmentRegisterOtp : Fragment() {

    private lateinit var binding: FragmentRegisterOtpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewRegisterBackButton.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterOtpBinding.inflate(layoutInflater)
        return binding.root
    }

}