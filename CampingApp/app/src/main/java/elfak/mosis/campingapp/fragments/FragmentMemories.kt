package elfak.mosis.campingapp.fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityMain
import elfak.mosis.campingapp.adapters.AdapterMemories
import elfak.mosis.campingapp.databinding.FragmentActivitiesBinding
import elfak.mosis.campingapp.databinding.FragmentMemoriesBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTrip
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentMemories : Fragment(), AdapterMemories.helper
{
    private val REQUEST_IMAGE_CAPTURE = 1
    lateinit var binding: FragmentMemoriesBinding
    lateinit var recycler: RecyclerView
    private val shareViewModel: SharedViewTrip by activityViewModels()

    override fun onResume()
    {
        super.onResume()
        val navigation: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        for (i in 0 until navigation.getMenu().size())
            navigation.getMenu().getItem(i).setChecked(false)
        navigation.menu.findItem(R.id.nav_memories).setChecked(true)
        navigation.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentMemoriesBinding.inflate(layoutInflater)
        binding.buttonAddActivity.setOnClickListener {
            if(shareViewModel.endDate.value!!.before(Date()))
            {
                Toast.makeText(requireContext(), "Cant add items on finished trip", Toast.LENGTH_SHORT).show()
            }
            else {
                dispatchTakePictureIntent()
            }
        }
        binding.newPicture.setOnClickListener {
            if(shareViewModel.endDate.value!!.before(Date()))
            {
                Toast.makeText(requireContext(), "Cant add items on finished trip", Toast.LENGTH_SHORT).show()
            }
            else {
                dispatchTakePictureIntent()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.goHome.setOnClickListener {
            var intent = Intent(context, ActivityMain::class.java)
            startActivity(intent)
        }

        recycler = binding.allMemoriesPictures
        val adapter: AdapterMemories = AdapterMemories(requireContext(),shareViewModel.memories,shareViewModel.tripID.value!!,this)
        recycler.adapter = adapter
        recycler.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        Firebase.firestore
            .collection(getString(R.string.db_coll_trips))
            .document(shareViewModel.tripID.value!!)
            .get()
            .addOnSuccessListener {
                var tmp = it["memories"] as ArrayList<String>
                shareViewModel.memories.clear()
                shareViewModel.memories.addAll(tmp)
                recycler!!.adapter!!.notifyDataSetChanged()
            }



    }

    private fun dispatchTakePictureIntent()
    {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try
                {
                    createImageFile()
                }
                catch (ex: IOException)
                {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File
    {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
        {
            var file = File(currentPhotoPath)
            var nazivSlike = currentPhotoPath.substring(currentPhotoPath.indexOf("JPEG_"))
            Toast.makeText(requireContext(), "Uploading image... image will show when uploaded", Toast.LENGTH_SHORT).show()
            Firebase.storage
                .getReference("trips/${shareViewModel.tripID.value}/${nazivSlike}")
                .putFile(Uri.fromFile(file))
                .addOnSuccessListener {

                    shareViewModel.memories.add(nazivSlike)
                    Firebase.firestore
                        .collection(getString(R.string.db_coll_trips))
                        .document(shareViewModel.tripID.value!!)
                        .update("memories", FieldValue.arrayUnion(nazivSlike))
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Successful uploaded image", Toast.LENGTH_SHORT).show()
                            recycler!!.adapter!!.notifyDataSetChanged()
                        }
                }

        }
    }

    override fun sendImage(image: String) {
        shareViewModel.selectedImage.value = image
        findNavController().navigate(R.id.action_fragmentMemories_to_fragmentMemoryView)
    }


}