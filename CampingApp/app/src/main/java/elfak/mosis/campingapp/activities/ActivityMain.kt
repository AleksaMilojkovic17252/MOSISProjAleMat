package elfak.mosis.campingapp.activities

import android.R.id.toggle
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.internal.IdTokenListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.internal.InternalTokenResult
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.ActivityMainBinding
import elfak.mosis.campingapp.fragments.FragmentAddTeammate
import elfak.mosis.campingapp.fragments.FragmentEditProfile
import elfak.mosis.campingapp.fragments.FragmentHome
import elfak.mosis.campingapp.fragments.FragmentTeammates


class ActivityMain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DrawerLocker
{
    private lateinit var mDrawer: DrawerLayout
    private lateinit var binding: ActivityMainBinding //fuck mogao sam ovo da koristim lmaaaooooo

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        mDrawer = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle: ActionBarDrawerToggle =  ActionBarDrawerToggle(this,mDrawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)

        mDrawer.addDrawerListener(toggle)


        toggle.syncState()
        if(savedInstanceState == null)
        {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentHome()).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }

        val headerLayout: View = navigationView.getHeaderView(0)
        val image: ImageView = headerLayout.findViewById(R.id.edit_image)

        image.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,FragmentEditProfile()).addToBackStack(null).commit()
            mDrawer.closeDrawer(GravityCompat.START)
        }

        Firebase.auth.addIdTokenListener(IdTokenListener
        {
            finish()
        })

    }


    override fun onBackPressed() {
        if(mDrawer.isDrawerOpen(GravityCompat.START))
        {
            mDrawer.closeDrawer(GravityCompat.START)
        }
        else
        {
            super.onBackPressed()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_home -> {

                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,FragmentHome()).addToBackStack(null).commit()
            }
            R.id.nav_teammates-> {

                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,FragmentTeammates()).addToBackStack(null).commit()
            }
            R.id.nav_notifications -> {

                //supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ProfileFragment()).commit()
            }
            R.id.nav_settings -> {

                //supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ProfileFragment()).commit()
            }
            R.id.nav_logout -> Firebase.auth.signOut()
        }
        mDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun setDrawerEnabled(enabled: Boolean) {
        val lockMode =
            if (enabled) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        mDrawer.setDrawerLockMode(lockMode)
    }


}