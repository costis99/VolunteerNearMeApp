package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditProfileActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        auth = Firebase.auth
        fStore = Firebase.firestore
        val nickname : EditText = findViewById(R.id.tv_nickname)
        val saver: Button = findViewById(R.id.save_button)
        saver.setOnClickListener {
            saveChanges(nickname)
        }
        val viewProfile: Button = findViewById(R.id.view_profile)
        viewProfile.setOnClickListener {
            startActivity(Intent(this, ProfileViewActivity::class.java))
        }

    }

    private fun saveChanges(nickname: EditText) {
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        if(nickname.text != null){
            if (userId != null) {
                fStore.collection("users").document(userId).get().addOnSuccessListener { result ->
                        val name = hashMapOf(
                            "Email ID" to user?.email.toString(),
                            "Nickname" to nickname.text.toString(),
                            "Role" to result.get("Role").toString()
                        )
                    fStore.collection("users").document(userId).set(name)
                    Toast.makeText(
                        baseContext,
                        "Nickname updated successfully!",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

        }

    }
}
