package elfak.mosis.campingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.ActivityAddFriendsBinding
import elfak.mosis.campingapp.databinding.FragmentAddTeammateBinding

class ActivityAddFriends : AppCompatActivity()
{
    private lateinit var binding: ActivityAddFriendsBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageViewBackButton.setOnClickListener { finish() }
    }
}