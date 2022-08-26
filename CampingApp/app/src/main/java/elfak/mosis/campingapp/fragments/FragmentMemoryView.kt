package elfak.mosis.campingapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentDetailActivityViewBinding
import elfak.mosis.campingapp.databinding.FragmentMemoryViewBinding
import elfak.mosis.campingapp.sharedViews.SharedViewTrip


class FragmentMemoryView : Fragment() {

    lateinit var binding: FragmentMemoryViewBinding
    private val shareViewModel: SharedViewTrip by activityViewModels()

    override fun onResume() {
        super.onResume()
        val navigation: BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        navigation.visibility = View.GONE
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemoryViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("tag",shareViewModel.selectedImage.value!!)
        Firebase.storage.getReference("trips/${shareViewModel.tripID.value}/${shareViewModel.selectedImage.value}").downloadUrl.addOnSuccessListener { uri ->
            Glide.with(requireContext()).load(uri).into(binding.showWholeImage)
        }
    }

}