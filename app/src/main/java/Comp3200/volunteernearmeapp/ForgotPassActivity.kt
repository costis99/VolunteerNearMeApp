package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPassActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        auth = Firebase.auth
        val buttonReset: Button = findViewById(R.id.ResetButton)
        buttonReset.setOnClickListener {
            resetPass()
        }
    }

    private fun resetPass() {
        val email: TextView = findViewById(R.id.et_email)
        // check if email is empty
        if(email.text.toString().isEmpty()){
            email.error = "Please enter an email"
            return
        }
        // check if email is valid
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            email.error = "Please enter a valid email"
            return
        }

        // send email to user with change password option
        Firebase.auth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"An email has been sent with instructions to reset your password", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else {
                    Toast.makeText(this,"Error resetting password", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
    }
}