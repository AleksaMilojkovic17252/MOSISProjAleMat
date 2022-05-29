package elfak.mosis.campingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import elfak.mosis.campingapp.databinding.FragmentNotificationBinding

class FragmentNotification : Fragment()
{
    lateinit var binding: FragmentNotificationBinding
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        return binding.root
    }


}