package elfak.mosis.campingapp.fragments

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentEditProfileBinding


class FragmentEditProfile : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val REQUEST_IMAGE_CAPTURE = 1;
    private var formCheck:BooleanArray = BooleanArray(4)

    override fun onResume() {
        super.onResume()
        val title: TextView = requireActivity().findViewById(R.id.toolbar_title)
        val navigation: NavigationView = requireActivity().findViewById(R.id.nav_view)
        for (i in 0 until navigation.getMenu().size()) {
            navigation.getMenu().getItem(i).setChecked(false)
        }
        title.text = "Edit Profile"
        val buttonNotification: ImageView = requireActivity().findViewById(R.id.notification_toolbar)
        val buttonFriend: ImageView = requireActivity().findViewById(R.id.addFriend_toolbar)

        buttonFriend.visibility = View.GONE
        buttonNotification.visibility = View.GONE

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val btnImage = binding.profileImage
        btnImage.setOnClickListener{
            dispatchTakePictureIntent()
            enableLogin()
        }

        val btnText = binding.textViewTakePicture
        btnText.setOnClickListener{
            dispatchTakePictureIntent()
            enableLogin()
        }

        binding.button.setOnClickListener {
            Toast.makeText(context, "Changed user data", Toast.LENGTH_SHORT).show()
            fragmentManager?.popBackStack()

        }

        binding.editTextEditProfileName.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?)
            {
                formCheck[1] = p0?.isNotEmpty() ?: false
                enableLogin()
            }
        })

        binding.editTextEditProfileOccupation.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?)
            {
                formCheck[2] = p0?.isNotEmpty() ?: false
                enableLogin()
            }
        })

        binding.editTextEditProfileDescription.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?)
            {
                formCheck[3] = p0?.isNotEmpty() ?: false
                enableLogin()
            }
        })
        enableLogin()
    }

    private fun enableLogin()
    {
            if(formCheck.all { it })
            {
                binding.button.setBackgroundResource(R.drawable.et_button_shape_green)
                binding.button.isEnabled = true
            }
            else
            {
                binding.button.setBackgroundResource(R.drawable.button_disabled)
                binding.button.isEnabled = false
            }
    }
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            formCheck[0] = true
            binding.profileImagePlaceholder.setImageDrawable(null)
            binding.profileImage.setImageBitmap(imageBitmap)
        }
    }

}