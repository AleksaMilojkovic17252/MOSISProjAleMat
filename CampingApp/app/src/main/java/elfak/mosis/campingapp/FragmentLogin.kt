package elfak.mosis.campingapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import elfak.mosis.campingapp.databinding.FragmentLoginBinding


class FragmentLogin : Fragment()
{
    private lateinit var binding: FragmentLoginBinding
    private var emailEntered = false
    private var passEntered = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val regButton = binding.registerButton
        regButton.setOnClickListener {
            findNavController().navigate(R.id.frLogin_to_frReg)
        }

        enableLogin()

        binding.editTextTextEmailAddress.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?)
            {
                emailEntered = android.util.Patterns.EMAIL_ADDRESS.matcher(p0).matches()
                enableLogin()
            }

        })

        binding.editTextTextPassword.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?)
            {
                passEntered = p0?.isNotEmpty() ?: false
                enableLogin()
            }

        })

    }

    private fun enableLogin()
    {
        if (emailEntered && passEntered)
        {
            binding.loginButton.setBackgroundResource(R.drawable.et_button_shape_green)
            binding.loginButton.isEnabled = true
        }
        else
        {
            binding.loginButton.setBackgroundResource(R.drawable.button_disabled)
            binding.loginButton.isEnabled = false
        }
    }


}