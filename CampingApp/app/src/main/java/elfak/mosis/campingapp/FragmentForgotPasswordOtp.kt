package elfak.mosis.campingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordOtpBinding.inflate(layoutInflater)
        return binding.root
    }

}