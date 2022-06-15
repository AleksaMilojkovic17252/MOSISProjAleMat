package elfak.mosis.campingapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentForgotPasswordBinding


class FragmentForgotPassword : Fragment()
{
    private lateinit var binding:FragmentForgotPasswordBinding

    private var emailEntered = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonForgotPasswordSend.setOnClickListener {
            var email = binding.editTextForgotPasswordEmail.text.toString()
            requestPasswordChange(email)
        }

        binding.editTextForgotPasswordEmail.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?)
            {
                emailEntered = android.util.Patterns.EMAIL_ADDRESS.matcher(p0).matches()
                enableChangePassword()
            }
        })

        enableChangePassword()
    }

    private fun enableChangePassword()
    {
        if(emailEntered)
        {
            binding.buttonForgotPasswordSend.setBackgroundResource(R.drawable.et_button_shape_green)
            binding.buttonForgotPasswordSend.isEnabled = true
        }
        else
        {
            binding.buttonForgotPasswordSend.setBackgroundResource(R.drawable.button_disabled)
            binding.buttonForgotPasswordSend.isEnabled = false
        }
    }

    private fun requestPasswordChange(email: String)
    {
        Firebase.auth.sendPasswordResetEmail(email).addOnCompleteListener{
            if(it.isSuccessful)
            {
                Toast.makeText(context, R.string.email_sent, Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }


}