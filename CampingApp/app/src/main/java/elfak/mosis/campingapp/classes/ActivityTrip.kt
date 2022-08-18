package elfak.mosis.campingapp.classes

data class ActivityTrip(var ID:String,var koJeNapravio:String,var title:String, var description: String, var latitude: Double, var longitude: Double, var type: Int = NICE_VIEW)
{
    companion object{
        const val NICE_VIEW = 1
        const val SHELTER = 2
        const val POINT_OF_INTEREST = 3
    }
}