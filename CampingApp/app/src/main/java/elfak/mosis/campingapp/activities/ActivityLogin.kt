package elfak.mosis.campingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import elfak.mosis.campingapp.R

class ActivityLogin : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        try
        {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)
            var storage = Firebase.storage
            var db = Firebase.firestore
            var dbb = Firebase.database
            var user = Firebase.auth.currentUser

        }
        catch(ex:Exception)
        {
            Log.d("Dead",ex.toString())
        }
    }
}