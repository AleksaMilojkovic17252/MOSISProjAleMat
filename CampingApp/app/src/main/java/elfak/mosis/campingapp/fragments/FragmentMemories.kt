package elfak.mosis.campingapp.fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityMain
import elfak.mosis.campingapp.databinding.FragmentActivitiesBinding
import elfak.mosis.campingapp.databinding.FragmentMemoriesBinding


class FragmentMemories : Fragment()
{
    private val REQUEST_IMAGE_CAPTURE = 1
    lateinit var binding: FragmentMemoriesBinding
    override fun onResume() {
        super.onResume()
        val navigation: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        for (i in 0 until navigation.getMenu().size())
            navigation.getMenu().getItem(i).setChecked(false)
        navigation.menu.findItem(R.id.nav_memories).setChecked(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentMemoriesBinding.inflate(layoutInflater)
        binding.buttonAddActivity.setOnClickListener {
            dispatchTakePictureIntent()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goHome.setOnClickListener {
            var intent = Intent(context, ActivityMain::class.java)
            startActivity(intent)
        }
    }

    private fun dispatchTakePictureIntent()
    {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
        catch (e: ActivityNotFoundException)
        {
            Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
        {
            
        }
    }
}