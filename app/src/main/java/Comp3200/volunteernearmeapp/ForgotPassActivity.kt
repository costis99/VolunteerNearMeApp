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

/**
 * Activity representing the change of a user's password
 */
class ForgotPassActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        auth = Firebase.auth
        val buttonReset: Button = findViewById(R.id.ResetButton)
        //When the reset button is clicked
        buttonReset.setOnClickListener {
            resetPass()
        }
    }

    private fun resetPass() {
        val email: TextView = findViewById(R.id.et_email)
        //Check if email input of the user is empty
        if (email.text.toString().isEmpty()) {
            email.error = "Please enter an email"
            return
        }
        //Check if email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Please enter a valid email"
            return
        }

        //Send the appropriate email to the user in order to change password
        Firebase.auth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //If email was sent successfully show user appropriate message
                    Toast.makeText(
                        this,
                        "An email has been sent with instructions to reset your password",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    //If email was not sent successfully show user appropriate message
                    Toast.makeText(this, "Error resetting password", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}