package elfak.mosis.campingapp.sharedViews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.classes.Notifications
import elfak.mosis.campingapp.classes.User
import java.util.*
import kotlin.collections.ArrayList

class SharedViewHome : ViewModel()
{
    var korisnik: MutableLiveData<User> = MutableLiveData()
    var notifikacije: MutableList<String> = mutableListOf()
    val liveNotifikacije: MutableLiveData<ArrayList<Notifications>> by lazy { MutableLiveData<ArrayList<Notifications>>(ArrayList()) }
    val friendDetails: MutableLiveData<User> = MutableLiveData()
    var korisnici: MutableList<User> = mutableListOf()
    var tripName: MutableLiveData<String> = MutableLiveData()
    var longitude: MutableLiveData<Double> = MutableLiveData()
    var latitude: MutableLiveData<Double> = MutableLiveData()
    var startDate: MutableLiveData<Date>  = MutableLiveData()
    var endDate: MutableLiveData<Date> = MutableLiveData()
    var backpackItems: MutableList<BackpackItems> = mutableListOf()
    var dataChanger: MutableLiveData<Boolean> = MutableLiveData(false)
    var fullUcitavanje: MutableLiveData<Boolean> = MutableLiveData(false) //Da li su ucitani svi podaci o korisnicima

}