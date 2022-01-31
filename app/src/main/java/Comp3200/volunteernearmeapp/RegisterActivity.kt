package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        // Initialize Firebase Auth
        auth = Firebase.auth
        fStore = Firebase.firestore
        val tickBox: CheckBox= findViewById(R.id.checkbox)
        tickBox.setOnClickListener{
            val orgName: EditText = findViewById(R.id.et_organization_name)
            orgName.isVisible = true
        }
        val buttonRegister: Button = findViewById(R.id.RegisterButton)
        buttonRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
//        val emailInfo: TextView = findViewById(R.id.et_username);
//        val password: TextView = findViewById(R.id.et_password);
//
//
//        //check if empty email
//        if (emailInfo.text.toString().isEmpty()) {
//            emailInfo.error = "Please enter email"
//            emailInfo.requestFocus()
//            return
//        }
//
//        //check if email is valid
//        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
//            emailInfo.error = "Please enter a valid email"
//            emailInfo.requestFocus()
//            return
//        }
//
//        //check if a password empty
//        if (password.text.toString().isEmpty()) {
//            password.error = "Please enter password"
//            password.requestFocus()
//            return
//        }
//
//
//        //register user
//        auth.createUserWithEmailAndPassword(emailInfo.text.toString(), password.text.toString())
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    val user = Firebase.auth.currentUser
//
//                    //send a verification email to the registered email ID
//                    user!!.sendEmailVerification()
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                Toast.makeText(baseContext,"Signed up successfuly. " +
//                                        "A verification email has been sent to your email address",
//                                    Toast.LENGTH_SHORT).show()
//                                userID = user.uid
//                                if(username.text.toString() == "ts1g18@soton.ac.uk"){
//                                    role = "Team Captain"
//                                }else{
//                                    role = "Team Member"
//                                }
//                                // Create a new user with a fullname and username(emailID) and student id
//                                val user = hashMapOf(
//                                    "Email ID" to emailInfo.text.toString(),
//                                    "Role" to role
//                                )
//                                val documentReference: DocumentReference =
//                                    fStore.collection("users").document(userID)
//                                // Add a new document with a generated ID
//                                documentReference.set(user)
//                                    .addOnSuccessListener { documentReference ->
//                                        Log.d(TAG, "DocumentSnapshot added with ID: $userID")
//                                    }
//                                    .addOnFailureListener { e ->
//                                        Log.w(TAG, "Error adding document", e)
//                                    }
//                                startActivity(Intent(this, LoginActivity::class.java))
//                                finish()
//                            }
//                        }
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Toast.makeText(
//                        baseContext, "Sign up failed. Try again later.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
    }

}
