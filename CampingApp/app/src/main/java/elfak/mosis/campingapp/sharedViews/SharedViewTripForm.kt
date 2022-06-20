package elfak.mosis.campingapp.sharedViews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewTripForm : ViewModel() {
    var imena: MutableList<String> = mutableListOf()
    var dataChanger: MutableLiveData<Boolean> = MutableLiveData(false)
}