package elfak.mosis.campingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import elfak.mosis.campingapp.R

class ActivityLogin : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)
        }
        catch(ex:Exception)
        {
            Log.d("Dead",ex.toString())
        }
    }
}