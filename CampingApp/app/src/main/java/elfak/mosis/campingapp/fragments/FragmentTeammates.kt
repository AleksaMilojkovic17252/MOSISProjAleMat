package elfak.mosis.campingapp.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.ActivityAddFriends
import elfak.mosis.campingapp.activities.DrawerLocker
import elfak.mosis.campingapp.adapters.AdapterAllTeammates
import elfak.mosis.campingapp.classes.User
import elfak.mosis.campingapp.databinding.FragmentTeammatesBinding
import elfak.mosis.campingapp.sharedViews.SharedViewHome

class FragmentTeammates : Fragment(), AdapterAllTeammates.Pomoc
{
    private lateinit var binding: FragmentTeammatesBinding
    lateinit var recycler: RecyclerView
    private val shareViewModel: SharedViewHome by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentTeammatesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume()
    {
        super.onResume()
        val title: TextView = requireActivity().findViewById(R.id.toolbar_title)
        title.text = "Teammates"
        (activity as DrawerLocker?)!!.setDrawerEnabled(true)
        val buttonNotification: ImageView = requireActivity().findViewById(R.id.notification_toolbar)
        val buttonFriend: ImageView = requireActivity().findViewById(R.id.addFriend_toolbar)
        val navigation: NavigationView = requireActivity().findViewById(R.id.nav_view)
        for (i in 0 until navigation.getMenu().size())
            navigation.getMenu().getItem(i).setChecked(false)
        navigation.menu.findItem(R.id.nav_teammates).setChecked(true)

        buttonFriend.visibility = View.VISIBLE
        buttonNotification.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        var drugovi = ArrayList<User>()
        var listaTaskovaSakupljanjaPodatakaKorsinika = ArrayList<Task<DocumentSnapshot>>()

        var tmp2 = Firebase.firestore
            .collection(getString(R.string.db_coll_users))
            .document(shareViewModel.korisnik.value!!.ID)
            .get()
        tmp2.addOnSuccessListener {it2 ->
                var prike = it2["friends"] as ArrayList<String>
                //Toast.makeText(requireContext(), "${prike.count()}", Toast.LENGTH_SHORT).show()
                for (p in prike)
                {
                    var tmp = Firebase.firestore
                        .collection(getString(R.string.db_coll_users))
                        .document(p)
                        .get()
                    tmp.addOnSuccessListener {
                        var tmpUser = User(it.id,
                            it["name"].toString(),
                            it["occupation"].toString(),
                            it["description"].toString(),
                            it.id,
                            ArrayList<User>())
                        drugovi.add(tmpUser)
                    }
                    listaTaskovaSakupljanjaPodatakaKorsinika.add(tmp)
                }
            Tasks.whenAll(listaTaskovaSakupljanjaPodatakaKorsinika).addOnSuccessListener {
                shareViewModel.korisnik.value!!.Drugari = drugovi
                shareViewModel.fullUcitavanje.value = true
                val FriendAdapter: AdapterAllTeammates = AdapterAllTeammates(requireContext(),shareViewModel.korisnik.value?.Drugari,this)
                recycler = binding.allTeammatesView
                recycler.adapter = FriendAdapter
                recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            }
            }

//        for (drug in shareViewModel.korisnik.value!!.Drugari)
//        {
//            var tmp = Firebase.firestore
//                .collection("users")
//                .document(drug.ID)
//                .get()
//            tmp.addOnSuccessListener {
//                var tmpUser = User(it.id,
//                    it["name"].toString(),
//                    it["occupation"].toString(),
//                    it["description"].toString(),
//                    it.id,
//                    ArrayList<User>())
//                drugovi.add(tmpUser)
//            }
//            listaTaskovaSakupljanjaPodatakaKorsinika.add(tmp)
//        }

//        Tasks.whenAll(listaTaskovaSakupljanjaPodatakaKorsinika).addOnSuccessListener {
//            shareViewModel.korisnik.value!!.Drugari = drugovi
//            shareViewModel.fullUcitavanje.value = true
//            val FriendAdapter: AdapterAllTeammates = AdapterAllTeammates(requireContext(),shareViewModel.korisnik.value?.Drugari,this)
//            recycler = binding.allTeammatesView
//            recycler.adapter = FriendAdapter
//            recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
//        }




        super.onViewCreated(view, savedInstanceState)
        if(Firebase.auth.currentUser?.uid?.isNotEmpty() == true)
        {
            var uidLength = Firebase.auth.currentUser!!.uid.length;
            var stringBuilder = StringBuilder()
            stringBuilder.append(Firebase.auth.currentUser!!.uid.subSequence(0,3))
            stringBuilder.append("...")
            stringBuilder.append(Firebase.auth.currentUser!!.uid.subSequence(uidLength-3, uidLength))
            binding.textViewIDNumber.text = stringBuilder.toString()
        }


        binding.buttonCopyToClipboard.setOnClickListener {
            var clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager ?: null
            var clip = ClipData.newPlainText("Copied ID", Firebase.auth.currentUser!!.uid)
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(context, "ID copied to clipboard!", Toast.LENGTH_SHORT).show()

        }


    }

    override fun pomeraj(position: User) {
        shareViewModel.friendDetails.value = position
        //findNavController().navigate()
    }

}