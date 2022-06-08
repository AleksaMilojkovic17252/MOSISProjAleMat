package elfak.mosis.campingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import elfak.mosis.campingapp.databinding.ActivityAddFriendsBinding
import elfak.mosis.campingapp.databinding.FragmentAddTeammateBinding

class ActivityAddFriends : AppCompatActivity()
{
    private lateinit var binding: ActivityAddFriendsBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendsBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbarAddTeammate)
    }
}