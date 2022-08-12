package elfak.mosis.campingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.activities.DrawerLocker
import elfak.mosis.campingapp.databinding.FragmentAddTeammateBinding
import elfak.mosis.campingapp.sharedViews.SharedViewHome


class FragmentAddTeammate : Fragment()
{
    private lateinit var binding: FragmentAddTeammateBinding
    private val shareViewModel: SharedViewHome by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentAddTeammateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val title: TextView = requireActivity().findViewById(R.id.toolbar_title)
        title.text = getString(R.string.add_teammate_title)
        (activity as DrawerLocker?)!!.setDrawerEnabled(false)

        val buttonNotification: ImageView = requireActivity().findViewById(R.id.notification_toolbar)
        val buttonFriend: ImageView = requireActivity().findViewById(R.id.addFriend_toolbar)


        buttonFriend.visibility = View.GONE
        buttonNotification.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSendInvite.setOnClickListener {
            var refFriend = binding.editTextFriendID.text.toString()
            sendTeammateRequest(refFriend)
        }
    }

    private fun sendTeammateRequest(refFriend: String)
    {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(refFriend).matches())
        {
            Firebase.firestore
                .collection(getString(R.string.db_coll_users))
                .whereEqualTo("email", refFriend)
                .get()
                .addOnSuccessListener {
                    for (doc in it)
                    {
                        //Proveriti da li ga vec ima za prijatelja
                        var mozda = shareViewModel.korisnik.value?.Drugari?.find { x -> x.ID == doc.id }
                        if (mozda != null)
                        {
                            doc
                            Toast.makeText(requireContext(), "You have ${doc["name"] as String} as a teammate", Toast.LENGTH_SHORT).show()
                            break
                        }

                        var zaDodati = hashMapOf(
                            "from" to Firebase.auth.currentUser!!.uid,
                            "to" to doc.id,
                            "processed" to false,
                            "fromName" to shareViewModel.korisnik.value?.Name
                        )

                        Firebase.firestore
                            .collection(getString(R.string.db_coll_req))
                            .add(zaDodati)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Request sent!", Toast.LENGTH_SHORT).show()
                            }
                        break
                    }
                }
        }
        else
        {
            Toast.makeText(requireContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show()
        }
    }


}