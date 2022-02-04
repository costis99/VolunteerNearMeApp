package Comp3200.volunteernearmeapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var database: DatabaseReference
    var userId = ""
    private val TAG = "RegisterActivity"
    private var role: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        // Initialize Firebase Auth
        auth = Firebase.auth
        fStore = Firebase.firestore
        val tickBox: CheckBox= findViewById(R.id.checkbox)
        tickBox.setOnClickListener {
            val orgName: EditText = findViewById(R.id.et_organization_name)
            orgName.isVisible = true
        }
        val buttonRegister: Button = findViewById(R.id.RegisterButton)
        buttonRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val emailInfo: TextView = findViewById(R.id.et_username);
        val password: TextView = findViewById(R.id.et_password);
        if (emailInfo.text.toString().isEmpty()) {
            emailInfo.error = "Please enter your valid email"
            emailInfo.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailInfo.text.toString()).matches()) {
            emailInfo.error = "Please enter valid email"
            emailInfo.requestFocus()
            return
        }
        if (password.text.toString().isEmpty()) {
            password.error = "Please enter a non empty password"
            password.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(emailInfo.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(baseContext,"Registered successfuly. " + "Verification email has been sent!", Toast.LENGTH_SHORT).show()

                                userId = user.uid
                                val tickBox: CheckBox= findViewById(R.id.checkbox)
                                val orgName: EditText = findViewById(R.id.et_organization_name)
                                database = Firebase.database.reference
                                if(tickBox.isChecked && !orgName.text.isEmpty()){
                                    role = "Organizer"
                                    database.child("users").child(userId).setValue("Organizer")
                                }

                                else{
                                    role = "Volunteer"
                                    database.child("users").child(userId).setValue("Volunteer")
                                }
                                // Create a new user with a role and email
                                val user = hashMapOf(
                                    "Email ID" to emailInfo.text.toString(),
                                    "Role" to role
                                )
                                val documentReference: DocumentReference =
                                    fStore.collection("users").document(userId)
                                // Add a new document with a generated ID
                                documentReference.set(user).addOnSuccessListener { documentReference ->
                                        Log.d(TAG, "DocumentSnapshot added with ID: $userId")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(TAG, "Error adding document", e)
                                    }
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        }
                } else {
                    Toast.makeText(baseContext, "Error registering", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
