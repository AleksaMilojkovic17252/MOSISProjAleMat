package elfak.mosis.campingapp.sharedViews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.classes.User

class SharedViewTripForm : ViewModel() {
    var korisnici: MutableList<User> = mutableListOf()
    var tripName: MutableLiveData<String> = MutableLiveData()
    var longitude: MutableLiveData<Double> = MutableLiveData()
    var latitude: MutableLiveData<Double> = MutableLiveData()
    var startDate: MutableLiveData<Long>  = MutableLiveData()
    var endDate: MutableLiveData<Long> = MutableLiveData()
    var backpackItems: MutableList<BackpackItems> = mutableListOf()
    var dataChanger: MutableLiveData<Boolean> = MutableLiveData(false)
}