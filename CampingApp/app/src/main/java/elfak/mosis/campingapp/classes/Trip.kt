package elfak.mosis.campingapp.classes

import java.util.*
import kotlin.collections.ArrayList

data class Trip(var tripName: String, var longitude: Double, var latitude: Double, var users: ArrayList<User>,var startDate: Date,
                var endDate: Date, var UserItems: Map<String,ArrayList<BackpackItems>>)
{

}
