package Comp3200.volunteernearmeapp

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangeEmailActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email)
        auth = Firebase.auth
        val oldEmail : EditText = findViewById(R.id.et_old_email)
        val password: EditText = findViewById(R.id.et_password)
        val newEmail: EditText = findViewById(R.id.et_change)
        val changeEmailButton: Button = findViewById(R.id.change_email)
        changeEmailButton.setOnClickListener {
            changeEmail(oldEmail,password,newEmail)
        }
    }

    private fun changeEmail(oldEmail: EditText, password: EditText, newEmail: EditText) {
        //Check if email is empty
        if (newEmail.text.toString().isEmpty()) {
            newEmail.error = "Please enter your valid email"
            newEmail.requestFocus()
            return
        }
        //Check if email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(newEmail.text.toString()).matches()) {
            newEmail.error = "Please enter valid email"
            newEmail.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(oldEmail.text.toString(),password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success now update email
                    auth.currentUser!!.updateEmail(newEmail.text.toString())
                        .addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    baseContext,
                                    "Email updated successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
                                Toast.makeText(
                                    baseContext,
                                    "Email update FAILED please try again!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        baseContext,
                        "Wrong username or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
