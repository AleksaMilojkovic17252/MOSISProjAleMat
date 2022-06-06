package elfak.mosis.campingapp.activities

import android.R.id.toggle
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
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.ActivityMainBinding
import elfak.mosis.campingapp.fragments.FragmentEditProfile


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
        val text: TextView = findViewById(R.id.toolbar_title)
        text.text = "Camping trips"

        mDrawer = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle: ActionBarDrawerToggle =  ActionBarDrawerToggle(this,mDrawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)

        mDrawer.addDrawerListener(toggle)


        toggle.syncState()


        val headerLayout: View = navigationView.getHeaderView(0)
        val image: ImageView = headerLayout.findViewById(R.id.edit_image)

        image.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2,FragmentEditProfile()).commit()
            mDrawer.closeDrawer(GravityCompat.START)
        }



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
                val text: TextView = findViewById(R.id.toolbar_title)
                text.text = "Camping trips"
                //supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2,MessageFragment()).commit()
            }
            R.id.nav_teammates-> {
                val text: TextView = findViewById(R.id.toolbar_title)
                text.text = "Teammates"
                //supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2,ChatFragment()).commit()
            }
            R.id.nav_notifications -> {
                val text: TextView = findViewById(R.id.toolbar_title)
                text.text = "Notifications"
                //supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2,ProfileFragment()).commit()
            }
            R.id.nav_settings -> {
                val text: TextView = findViewById(R.id.toolbar_title)
                text.text = "Settings"
                //Toast.makeText(this,"Share", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                val text: TextView = findViewById(R.id.toolbar_title)
                text.text = "Log out"
                //Toast.makeText(this,"Send", Toast.LENGTH_SHORT).show()
            }
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