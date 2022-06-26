package elfak.mosis.campingapp.activities

import android.R.id.toggle
import android.content.*
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.internal.IdTokenListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.internal.InternalTokenResult
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.ActivityMainBinding
import elfak.mosis.campingapp.fragments.*
import elfak.mosis.campingapp.services.ServiceNotificationSpamFirestore
import elfak.mosis.campingapp.services.ServiceNotifications
import elfak.mosis.campingapp.sharedViews.SharedViewHome


class ActivityMain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DrawerLocker
{
    private lateinit var mDrawer: DrawerLayout
    private lateinit var binding: ActivityMainBinding //fuck mogao sam ovo da koristim lmaaaooooo
    private val primac: BroadcastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(p0: Context?, p1: Intent?)
        {
            p1?.extras?.getString("OdKog")?.let {
                if(shareViewModel.liveNotifikacije.value == null)
                {
                    var tmp = ArrayList<String>()
                    tmp.add(it)
                    shareViewModel.liveNotifikacije.value = tmp
                }
                else
                {
                    shareViewModel.liveNotifikacije.value?.add(it)
                    shareViewModel.liveNotifikacije.value = shareViewModel.liveNotifikacije.value
                }
            }
            //Log.d("CampingApp", "USO SaM U ONrECEIVE")
        }

    }
    private val shareViewModel : SharedViewHome by viewModels()


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

        val notificationButton = findViewById<ImageView>(R.id.notification_toolbar)
        notificationButton.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,FragmentNotification()).addToBackStack(null).commit()
        }

        val addTeammateButton = findViewById<ImageView>(R.id.addFriend_toolbar)
        addTeammateButton.setOnClickListener {
            var intent = Intent(this, ActivityAddFriends::class.java)
            startActivity(intent);
        }
        
        FirebaseMessaging.getInstance().token.addOnCompleteListener { 
            val token = it.result
            Toast.makeText(this, "$token", Toast.LENGTH_SHORT).show()
            //Log.d("CampingApp", token)
        }

    }


    override fun onBackPressed()
    {
        if(mDrawer.isDrawerOpen(GravityCompat.START))
            mDrawer.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            R.id.nav_home ->
            {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,FragmentHome()).addToBackStack(null).commit()
            }
            R.id.nav_teammates->
            {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,FragmentTeammates()).addToBackStack(null).commit()
            }
            R.id.nav_notifications ->
            {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,FragmentNotification()).addToBackStack(null).commit()
            }
            R.id.nav_settings ->
            {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,FragmentSettings()).addToBackStack(null).commit()
            }
            R.id.nav_logout ->
            {
                val navigation: NavigationView = findViewById(R.id.nav_view)
                for (i in 0 until navigation.getMenu().size())
                    navigation.getMenu().getItem(i).setChecked(false)

                var youSureDialog = AlertDialog.Builder(this)
                youSureDialog
                    .setMessage(R.string.logout_question)
                    .setPositiveButton("Yes") { p0, p1 ->
                        if (p1 == DialogInterface.BUTTON_POSITIVE)
                        {
                            Firebase.auth.signOut()
                        }
                    }
                    .setNegativeButton("No") {p0, p1 -> }
                    .show()
            }
        }
        mDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun setDrawerEnabled(enabled: Boolean)
    {
        val lockMode = if (enabled) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        mDrawer.setDrawerLockMode(lockMode)
    }

    override fun onPause()
    {
        stopService(Intent(this, ServiceNotificationSpamFirestore::class.java))
        unregisterReceiver(primac)
        super.onPause()
    }

    override fun onResume()
    {
        super.onResume()

        var korisnickePreference = getSharedPreferences("CampingApp", 0)

        if(korisnickePreference?.getBoolean("notifikacije", false) == true)
        {
            //Servis za posmatranje notifikacija
            val intent = Intent(this, elfak.mosis.campingapp.services.ServiceNotificationSpamFirestore::class.java)
            startService(intent)
        }

        //Obrada notifikacija
        registerReceiver(primac, IntentFilter(getString(R.string.intent_filter_notif)));
    }




}