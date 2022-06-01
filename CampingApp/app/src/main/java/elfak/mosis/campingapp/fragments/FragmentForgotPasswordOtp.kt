package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import elfak.mosis.campingapp.databinding.FragmentForgotPasswordBinding
import elfak.mosis.campingapp.databinding.FragmentForgotPasswordOtpBinding


class FragmentForgotPasswordOtp : Fragment() {

    private lateinit var binding: FragmentForgotPasswordOtpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        
        binding.buttonSendOTP.setOnClickListener { 
            var otp = binding.editTextOTPNumber.text.toString()
            sendOTP(otp)
        }

    }

    private fun sendOTP(otp: String) 
    {
        Toast.makeText(context, otp, Toast.LENGTH_SHORT).show()
        // TODO: Slanje OTP 
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentForgotPasswordOtpBinding.inflate(layoutInflater)
        return binding.root
    }

}