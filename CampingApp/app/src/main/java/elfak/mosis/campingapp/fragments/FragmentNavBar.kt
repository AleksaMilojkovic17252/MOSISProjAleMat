package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elfak.mosis.campingapp.databinding.FragmentNavbarBinding

class FragmentNavBar : Fragment()
{
    private lateinit var binding:FragmentNavbarBinding

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
   {
       binding = FragmentNavbarBinding.inflate(layoutInflater)
       return binding.root
   }
}