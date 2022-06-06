package elfak.mosis.campingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentHomeBinding


class FragmentHome : Fragment()
{
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mDrawer: DrawerLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) 
    {

        mDrawer = requireActivity().findViewById(R.id.drawer_layout)
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            Toast.makeText(context, "Kliknut sam", Toast.LENGTH_SHORT).show()
        }

        val navigationView: NavigationView = requireActivity().findViewById(R.id.nav_view)
        val headerLayout = navigationView.getHeaderView(0)
        val mLogo: ImageView = headerLayout.findViewById(R.id.edit_image)
        mLogo.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_fragmentEditProfile)
            mDrawer.closeDrawer(GravityCompat.START)
        }


    }


}