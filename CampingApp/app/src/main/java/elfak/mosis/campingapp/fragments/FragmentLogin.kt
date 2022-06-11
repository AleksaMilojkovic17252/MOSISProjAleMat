package elfak.mosis.campingapp.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.activities.ActivityMain
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentLoginBinding


class FragmentLogin : Fragment()
{

    private lateinit var binding: FragmentLoginBinding
    private var emailEntered = false
    private var passEntered = false
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }
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

        binding.loginButton.setOnClickListener {
            var email = binding.editTextTextEmailAddress.text.toString()
            var pass = binding.editTextTextPassword.text.toString()
            login(email, pass)
        }

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

        binding.textViewForgottenPass.setOnClickListener {
            findNavController().navigate(R.id.frLogin_to_frForgotPassword)
        }

        enableLogin()
    }

    private fun login(email:String, pass:String)
    {
        Firebase.auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
            when(it.isSuccessful)
            {
                true ->
                {
                    var i = Intent(context, ActivityMain::class.java)
                    startActivity(i)
                }
                else -> Toast.makeText(context, "Greska, pokusajte ponovo!", Toast.LENGTH_SHORT).show()

            }
        }

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