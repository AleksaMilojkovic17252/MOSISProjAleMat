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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentRegisterBinding

class FragmentRegister : Fragment()
{
    private lateinit var binding: FragmentRegisterBinding

    private var nameEntered = false
    private var emailEntered = false
    private var passEntered = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.registerRegisterLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.registerRegisterButton.setOnClickListener {
            var name = binding.editTextRegisterName.text.toString()
            var email = binding.editTextRegisterEmail.text.toString()
            var pass = binding.editTextRegisterPassword.text.toString()
            register(name, email, pass)
        }

        binding.editTextRegisterEmail.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?)
            {
                emailEntered = android.util.Patterns.EMAIL_ADDRESS.matcher(p0).matches()
                enableRegister()
            }
        })

        binding.editTextRegisterName.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?)
            {
                nameEntered = p0?.isNotEmpty() ?: false
                enableRegister()
            }
        })

        binding.editTextRegisterPassword.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?)
            {
                passEntered = p0?.isNotEmpty() == true && binding.editTextRegisterConfirmPassword.text.toString() == p0.toString() && p0.length >= 8
                enableRegister()
            }
        })

        binding.editTextRegisterConfirmPassword.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?)
            {
                passEntered = p0?.isNotEmpty() == true && binding.editTextRegisterPassword.text.toString() == p0.toString() && p0.length >= 8
                enableRegister()
            }
        })

        enableRegister()
    }

    private fun register(name: String, email: String, pass: String)
    {
        Firebase.auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if(it.isSuccessful)
            {
                var korisnik = hashMapOf(
                    "email" to email,
                    "name" to name,
                    "friends" to arrayListOf<String>())

                if(Firebase.auth.currentUser?.uid?.isNotEmpty() == true)
                    Firebase.firestore
                        .collection("users")
                        .document(Firebase.auth.currentUser!!.uid)
                        .set(korisnik)

                else
                    Firebase.firestore
                        .collection("users")
                        .add(korisnik)


                Firebase.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                    findNavController().navigate(R.id.frRegister_to_frRegHurray)
                }
            }
        }
    }

    private fun enableRegister()
    {
        if(nameEntered && emailEntered && passEntered)
        {
            binding.registerRegisterButton.setBackgroundResource(R.drawable.et_button_shape_green)
            binding.registerRegisterButton.isEnabled = true
        }
        else
        {
            binding.registerRegisterButton.setBackgroundResource(R.drawable.button_disabled)
            binding.registerRegisterButton.isEnabled = false
        }
    }

}