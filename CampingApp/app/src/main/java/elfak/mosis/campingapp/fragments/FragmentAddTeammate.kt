package elfak.mosis.campingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import elfak.mosis.campingapp.R
import elfak.mosis.campingapp.databinding.FragmentAddTeammateBinding

class FragmentAddTeammate : Fragment()
{
    private lateinit var binding: FragmentAddTeammateBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentAddTeammateBinding.inflate(layoutInflater)
        return binding.root
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
                        var zaDodati = hashMapOf(
                            "from" to Firebase.auth.currentUser!!.uid,
                            "to" to doc.id,
                            "processed" to false
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
            var zaDodati = hashMapOf(
                "from" to Firebase.auth.currentUser!!.uid,
                "to" to refFriend,
                "processed" to false
            )

            Firebase.firestore
                .collection(getString(R.string.db_coll_req))
                .add(zaDodati)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Request sent!", Toast.LENGTH_SHORT).show()
                }
        }
    }


}