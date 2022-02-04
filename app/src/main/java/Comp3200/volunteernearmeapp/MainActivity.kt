package Comp3200.volunteernearmeapp

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
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    //Initial commit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val reg: TextView = findViewById(R.id.tv_registerHere)
        reg.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        val forgotPass: TextView = findViewById(R.id.tv_forgotPassword)
        forgotPass.setOnClickListener{
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }

        val login: Button = findViewById(R.id.LoginButton)
        login.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
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

        auth.signInWithEmailAndPassword(emailInfo.text.toString(), password.text.toString()).addOnCompleteListener(this) {
                task ->
            if(task.isSuccessful){
                val currentUser = auth.currentUser
                if(currentUser != null && currentUser.isEmailVerified){
                    //read from database if volunteer or organizer and show appropriate home screen
                    database = Firebase.database.reference
                    val user = Firebase.auth.currentUser
                    var userId = user?.uid
                    if (userId != null) {
                        database.child("users").child(userId).get().addOnSuccessListener {
                            Log.i("firebase", "Got value ${it.value}")
                            if(it.value.toString() == "Volunteer") {
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            if(it.value.toString() == "Organizer"){
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        }.addOnFailureListener{
                            Log.e("firebase", "Error getting data", it)
                        }
                    }
                }
                else{
                    Toast.makeText(baseContext, "Email is not verified yet!", Toast.LENGTH_SHORT).show()
                }
           }
        }
    }
}