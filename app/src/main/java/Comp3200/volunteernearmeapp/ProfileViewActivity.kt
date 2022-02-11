package Comp3200.volunteernearmeapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileViewActivity: AppCompatActivity()  {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        auth = Firebase.auth
        fStore = Firebase.firestore

        val email : TextView = findViewById(R.id.tv_email)
        val nickname : TextView = findViewById(R.id.tv_nickname)
        val role : TextView = findViewById(R.id.tv_role)
        addToUserProfile(email, nickname, role)

        val editProfile: Button = findViewById(R.id.editProfileButton)
        editProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        val resetPass: Button = findViewById(R.id.changePassword)
        resetPass.setOnClickListener {
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }
        val resetEmail:Button = findViewById(R.id.changeEmail)
        resetEmail.setOnClickListener {
            startActivity(Intent(this, ChangeEmailActivity::class.java))
        }
    }

    private fun addToUserProfile(email: TextView, nickname: TextView, role: TextView) {
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        fStore.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.data.get("Email ID") == user?.email) {
                    email.text = "Email: ${document.data.get("Email ID") as CharSequence?}"
                    if(document.data.get("Nickname") == null){
                        nickname.text = "Nickname: No nickname added yet!"
                    }
                    else {
                        nickname.text =
                            "Nickname: ${document.data.get("Nickname") as CharSequence?}"
                    }
                    role.text = "Role: ${document.data.get("Role") as CharSequence?}"
                }
            }
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}
