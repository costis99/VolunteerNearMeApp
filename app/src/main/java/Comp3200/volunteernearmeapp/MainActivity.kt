package Comp3200.volunteernearmeapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    //Create Firebase Firestore instance
    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null

    //Initial commit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        // If user clicks on the sign up button go to the register activity
        val reg: TextView = findViewById(R.id.tv_registerHere)
        reg.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        // If user clicks on the forgot password button go to the forgot password activity
        val forgotPass: TextView = findViewById(R.id.tv_forgotPassword)
        forgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }
        // If the user clicks on the login button log the user in if the credentials are correct
        val login: Button = findViewById(R.id.LoginButton)
        login.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val emailInfo: TextView = findViewById(R.id.et_username);
        val password: TextView = findViewById(R.id.et_password);
        // Check if email is empty
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
        // Using firebase sign in method try to sign in the user
        auth.signInWithEmailAndPassword(emailInfo.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                //Check if signin is successful
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    //Check if user verified email used in sign up
                    if (currentUser != null && currentUser.isEmailVerified) {
                        //Read from database if user if volunteer or organizer and show appropriate home screen
                        database = Firebase.database.reference
                        val user = Firebase.auth.currentUser
                        val userId = user?.uid
                        if (userId != null) {
                            val docRef =
                                mFirebaseDatabaseInstance?.collection("users")?.document(userId)
                            docRef?.get()?.addOnSuccessListener { document ->
                                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                                val role = document.data?.getValue("Role")
                                if (role.toString().equals("Volunteer")) {
                                    startActivity(Intent(this, HomeVolunteersActivity::class.java))
                                    finish()
                                }
                                if (role.toString().equals("Organizer")) {
                                    startActivity(Intent(this, HomeOrganizersActivity::class.java))
                                    finish()
                                }
                            }
                                //Show appropriate message when data is not read correctly
                                ?.addOnFailureListener {
                                    Log.e("firebase", "Error getting data", it)
                                }
                        }
                    } else {
                        //Show appropriate message if email was not verified
                        Toast.makeText(
                            baseContext,
                            "Email is not verified yet!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    //If credentials are incorrect show appropriate message to the user
                    Toast.makeText(baseContext, "Wrong email or password.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}