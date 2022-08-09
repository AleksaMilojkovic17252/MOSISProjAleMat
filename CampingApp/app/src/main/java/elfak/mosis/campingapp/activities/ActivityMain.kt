package elfak.mosis.campingapp.activities

import android.R.id.toggle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.*
import android.media.Image
import android.os.Build
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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Tasks
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.internal.IdTokenListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.internal.InternalTokenResult
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.classes.Notifications
import elfak.mosis.campingapp.classes.NotificationsFriend
import elfak.mosis.campingapp.classes.NotificationsTrip
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.databinding.ActivityMainBinding
import elfak.mosis.campingapp.fragments.*
import elfak.mosis.campingapp.services.ServiceNotificationSpamFirestore
import elfak.mosis.campingapp.services.ServiceNotifications
import elfak.mosis.campingapp.services.ServiceSendLocation
import elfak.mosis.campingapp.sharedViews.SharedViewHome
import kotlin.random.Random


class ActivityMain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DrawerLocker
{
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    private lateinit var mDrawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding //fuck mogao sam ovo da koristim lmaaaooooo
    private val shareViewModel : SharedViewHome by viewModels()
    private val primac: BroadcastReceiver = object : BroadcastReceiver()
    {
        private fun dodajUShareViewModel(notif : Notifications)
        {
            if(shareViewModel.liveNotifikacije.value == null)
            {
                var tmp = ArrayList<Notifications>()
                tmp.add(notif)
                shareViewModel.liveNotifikacije.value = tmp
            }
            else
            {
                shareViewModel.liveNotifikacije.value?.add(notif)
                shareViewModel.liveNotifikacije.value = shareViewModel.liveNotifikacije.value
            }
        }

        override fun onReceive(p0: Context?, p1: Intent?)
        {
            if (p1?.extras?.getString("tip") == "Request")
            {
                var koSalje = p1.extras?.getString("OdKog")
                var koSaljeID = p1.extras?.getString("OdKogID")
                var notif = NotificationsFriend(koSalje!!, koSaljeID!!)
                dodajUShareViewModel(notif)
                pustiPopUp(notif)

            }
            else if (p1?.extras?.getString("tip") == "Trip")
            {
                var kojiTrip = p1.extras?.getString("trip")
                var notif = NotificationsTrip(kojiTrip!!)
                dodajUShareViewModel(notif)
                pustiPopUp(notif)
            }
        }

    }

    private fun pustiPopUp(notif: Notifications)
    {
        var fja =
        {
            if (notif is NotificationsTrip)
                "New Trip"
            else if(notif is NotificationsFriend)
                "New teammate request"
            else
                "New notification"
        }

        var fja2 =
        {
            if (notif is NotificationsTrip)
                "You have been invited to a trip " + (notif as NotificationsTrip).data
            else if (notif is NotificationsFriend)
                (notif as NotificationsFriend).data + " added you as friend"
            else
                "New message"
        }

        createNotificationChannel()

        var builder = NotificationCompat.Builder(this,"MojeKanalce")
            .setSmallIcon(R.mipmap.ikonica)
            .setContentTitle(fja())
            .setContentText(fja2())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(this))
        {
            // notificationId is a unique int for each notification that you must define
            notify(Random.nextInt(0, Int.MAX_VALUE), builder.build())
        }
    }

    private fun createNotificationChannel()
    {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "CampingAppChannel"
            val descriptionText = "NekiOpis"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("MojeKanalce", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    fun loadData(id:String)
    {
        var name: String = ""
        var occupation: String = ""
        var description: String = ""
        var drugovi:ArrayList<User> = ArrayList()

        var pribavljanjePodataka = Firebase.firestore.collection("users").document(id).get()

        pribavljanjePodataka.addOnSuccessListener {
            name = (it["name"].toString())
            occupation = (it["occupation"].toString())
            description = (it["description"].toString())
            var prijatelji = it["friends"] as ArrayList<String>
            for(drug in prijatelji)
            {
                drugovi.add(User(drug))
            }
        }

        Tasks.whenAll(pribavljanjePodataka).addOnSuccessListener {
            var korisnik = User(id,name,occupation,description,id,drugovi)
            shareViewModel.korisnik.value = korisnik
            var ramZaIme = findViewById<NavigationView>(R.id.nav_view).getHeaderView(0).findViewById<TextView>(R.id.NamePlaceHolder)
            ramZaIme.text = shareViewModel.korisnik.value?.Name
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        mDrawer = findViewById(R.id.drawer_layout)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        toggle =  ActionBarDrawerToggle(this,mDrawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)

        mDrawer.addDrawerListener(toggle)

        var user = Firebase.auth.currentUser
        var userID = Firebase.auth.currentUser!!.uid
        loadData(userID)



        toggle.syncState()
        if(savedInstanceState == null)
        {
            navigationView.setCheckedItem(R.id.nav_home)
        }

        val headerLayout: View = navigationView.getHeaderView(0)
        val image: ImageView = headerLayout.findViewById(R.id.edit_image)

        Firebase.storage.getReference("profilePics/$userID.jpg").downloadUrl.addOnSuccessListener { uri ->
            var ramZaSliku = headerLayout.findViewById<ImageView>(R.id.slika)
            Glide.with(this).load(uri).into(ramZaSliku)
        }

        image.setOnClickListener {
            when(navController.currentDestination?.id) {
                R.id.fragmentSettings->{
                    navController.navigate(R.id.action_fragmentSettings_to_fragmentEditProfile)
                }
                R.id.fragmentHome -> {
                    navController.navigate(R.id.action_fragmentHome_to_fragmentEditProfile)
                }
                R.id.fragmentTeammates -> {
                    navController.navigate(R.id.action_fragmentTeammates_to_fragmentEditProfile)
                }
                R.id.fragmentNotification -> {
                    navController.navigate(R.id.action_fragmentNotification_to_fragmentEditProfile)
                }

            }
            mDrawer.closeDrawer(GravityCompat.START)
        }

        Firebase.auth.addIdTokenListener(IdTokenListener
        {
            finish()
        })

        val notificationButton = findViewById<ImageView>(R.id.notification_toolbar)
        notificationButton.setOnClickListener {
            when(navController.currentDestination?.id) {
                R.id.fragmentSettings->{
                    navController.navigate(R.id.action_fragmentSettings_to_fragmentNotification)
                }
                R.id.fragmentHome -> {
                    navController.navigate(R.id.frHome_to_frNotification)
                }
                R.id.fragmentTeammates -> {
                    navController.navigate(R.id.action_fragmentTeammates_to_fragmentNotification)
                }
                R.id.fragmentEditProfile -> {
                    navController.navigate(R.id.action_fragmentEditProfile_to_fragmentNotification)
                }

            }
        }

        val addTeammateButton = findViewById<ImageView>(R.id.addFriend_toolbar)
        addTeammateButton.setOnClickListener {
            navController.navigate(R.id.action_fragmentTeammates_to_fragmentAddTeammate2)
        }
        
//        FirebaseMessaging.getInstance().token.addOnCompleteListener {
//            val token = it.result
//            Toast.makeText(this, "$token", Toast.LENGTH_SHORT).show()
//            //Log.d("CampingApp", token)
//        }

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
                when(navController.currentDestination?.id)
                {
                    R.id.fragmentSettings->{
                        navController.navigate(R.id.action_fragmentSettings_to_fragmentHome)
                    }
                    R.id.fragmentTeammates->
                    {
                        navController.navigate(R.id.action_fragmentTeammates_to_fragmentHome)
                    }
                    R.id.fragmentNotification->
                    {
                        navController.navigate(R.id.action_fragmentNotification_to_fragmentHome)
                    }
                    R.id.fragmentEditProfile->
                    {
                        navController.navigate(R.id.action_fragmentEditProfile_to_fragmentHome)
                    }
                }
            }
            R.id.nav_teammates->
            {
                when(navController.currentDestination?.id)
                {
                    R.id.fragmentSettings->{
                        navController.navigate(R.id.action_fragmentSettings_to_fragmentTeammates)
                    }
                    R.id.fragmentHome->
                    {
                        navController.navigate(R.id.frHome_to_frTeammates)
                    }
                    R.id.fragmentNotification->
                    {
                        navController.navigate(R.id.action_fragmentNotification_to_fragmentTeammates)
                    }
                    R.id.fragmentEditProfile->
                    {
                        navController.navigate(R.id.action_fragmentEditProfile_to_fragmentTeammates)
                    }
                }
            }
            R.id.nav_notifications ->
            {
                when(navController.currentDestination?.id)
                {
                    R.id.fragmentSettings->{
                        navController.navigate(R.id.action_fragmentSettings_to_fragmentNotification)
                    }
                    R.id.fragmentHome->
                    {
                        navController.navigate(R.id.frHome_to_frNotification)
                    }
                    R.id.fragmentTeammates->
                    {
                        navController.navigate(R.id.action_fragmentTeammates_to_fragmentNotification)
                    }
                    R.id.fragmentEditProfile->
                    {
                        navController.navigate(R.id.action_fragmentEditProfile_to_fragmentNotification)
                    }
                }
            }
            R.id.nav_settings ->
            {
                when(navController.currentDestination?.id)
                {
                    R.id.fragmentNotification->{
                        navController.navigate(R.id.action_fragmentNotification_to_fragmentSettings)
                    }
                    R.id.fragmentHome->
                    {
                        navController.navigate(R.id.action_fragmentHome_to_fragmentSettings)
                    }
                    R.id.fragmentTeammates->
                    {
                        navController.navigate(R.id.action_fragmentTeammates_to_fragmentSettings)
                    }
                    R.id.fragmentEditProfile->
                    {
                        navController.navigate(R.id.action_fragmentEditProfile_to_fragmentSettings)
                    }
                }
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
        toggle.setDrawerIndicatorEnabled(enabled);


    }

    override fun onPause()
    {
        stopService(Intent(this, ServiceNotificationSpamFirestore::class.java))
        stopService(Intent(this, ServiceSendLocation::class.java))
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

        var i = Intent(this, ServiceSendLocation::class.java)
        startService(i)

        //Obrada notifikacija
        registerReceiver(primac, IntentFilter(getString(R.string.intent_filter_notif)));




    }




}