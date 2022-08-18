package elfak.mosis.campingapp.sharedViews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import elfak.mosis.campingapp.classes.ActivityTrip
import elfak.mosis.campingapp.classes.BackpackItems
import elfak.mosis.campingapp.classes.User
import java.util.*
import kotlin.collections.ArrayList

class SharedViewTrip : ViewModel()
{
    var korisnici: MutableList<User> = mutableListOf()
    var tripName: MutableLiveData<String> = MutableLiveData()
    var longitude: MutableLiveData<Double> = MutableLiveData()
    var latitude: MutableLiveData<Double> = MutableLiveData()
    var startDate: MutableLiveData<Date>  = MutableLiveData()
    var endDate: MutableLiveData<Date> = MutableLiveData()
    var backpackItems: MutableMap<String, ArrayList<BackpackItems>> = mutableMapOf()
    var tripID:MutableLiveData<String> = MutableLiveData()
    var activityTitle:MutableLiveData<String> = MutableLiveData()
    var activityDescription: MutableLiveData<String> = MutableLiveData()
    var activityType:MutableLiveData<Int> = MutableLiveData()
    var activityLongitude:MutableLiveData<Double> = MutableLiveData()
    var activityLatitude:MutableLiveData<Double> = MutableLiveData()
    var dataSetChanged:MutableLiveData<Boolean> = MutableLiveData(false)
    var allActivities: MutableList<ActivityTrip> = mutableListOf()
    var selectedActivity: MutableLiveData<ActivityTrip> = MutableLiveData()
}