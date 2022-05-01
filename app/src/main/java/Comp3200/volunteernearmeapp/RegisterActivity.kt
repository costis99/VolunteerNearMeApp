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

/**
 * Activity allowing a new user to register.
 */
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
        //When user clicks on organizer check box allow user to add name of organization
        val tickBox: CheckBox = findViewById(R.id.checkbox)
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
        //Check if email is empty
        if (emailInfo.text.toString().isEmpty()) {
            emailInfo.error = "Please enter your valid email"
            emailInfo.requestFocus()
            return
        }
        //Check if email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(emailInfo.text.toString()).matches()) {
            emailInfo.error = "Please enter valid email"
            emailInfo.requestFocus()
            return
        }
        //Check if password is empty
        if (password.text.toString().isEmpty()) {
            password.error = "Please enter a non empty password"
            password.requestFocus()
            return
        }
        //Create user using firebase method (email and password)
        auth.createUserWithEmailAndPassword(emailInfo.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                //check if user creation was successful
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    //send email verification to the registered email
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            //If email verification was sent show to the user appropriate message
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    baseContext,
                                    "Registered successfuly. " + "Verification email has been sent!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                userId = user.uid
                                val tickBox: CheckBox = findViewById(R.id.checkbox)
                                val orgName: EditText = findViewById(R.id.et_organization_name)
                                database = Firebase.database.reference
                                //Check if user is registered as an Organizer or a Volunteer and add it to the database
                                if (tickBox.isChecked && !orgName.text.isEmpty()) {
                                    role = "Organizer"
                                    database.child("users").child(userId).setValue("Organizer")
                                } else {
                                    role = "Volunteer"
                                    database.child("users").child(userId).setValue("Volunteer")
                                }
                                //Create a new user with an Email, Nickname (set to null) and Role
                                val user = hashMapOf(
                                    "Email ID" to emailInfo.text.toString(),
                                    "Nickname" to "",
                                    "Role" to role
                                )
                                //Add the new user to the users collection within firebase-firestore
                                val documentReference: DocumentReference =
                                    fStore.collection("users").document(userId)
                                documentReference.set(user)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d(TAG, "Added to database successfully")
                                    }
                                    .addOnFailureListener { error ->
                                        Log.w(TAG, "Error", error)
                                    }
                                //When user has registered successfully return the user to the Login page
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        }
                } else {
                    //Show to the user appropriate message when registration failed
                    Toast.makeText(
                        baseContext,
                        "There was an error while registering. Please try again!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
