package elfak.mosis.campingapp.sharedViews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.campingapp.classes.User

class SharedViewHome : ViewModel()
{
    var korisnik: MutableLiveData<User> = MutableLiveData()
    var notifikacije: MutableList<String> = mutableListOf()
    val liveNotifikacije: MutableLiveData<ArrayList<String>> by lazy { MutableLiveData<ArrayList<String>>() }

}