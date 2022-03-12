package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChangeEmailActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email)
        auth = Firebase.auth
        fStore = Firebase.firestore
        val oldEmail: EditText = findViewById(R.id.et_old_email)
        val password: EditText = findViewById(R.id.et_password)
        val newEmail: EditText = findViewById(R.id.et_change)
        val changeEmailButton: Button = findViewById(R.id.change_email)
        changeEmailButton.setOnClickListener {
            changeEmail(oldEmail, password, newEmail)
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
        auth.signInWithEmailAndPassword(oldEmail.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    val userId = user?.uid
                    auth.currentUser!!.updateEmail(newEmail.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (userId != null) {
                                    fStore.collection("users").document(userId).get()
                                        .addOnSuccessListener { result ->
                                            val name = hashMapOf(
                                                "Email ID" to user?.email.toString(),
                                                "Nickname" to result.get("Nickname").toString(),
                                                "Role" to result.get("Role").toString()
                                            )

                                            fStore.collection("users").document(userId).set(name)
                                        }
                                    Toast.makeText(
                                        baseContext,
                                        "Email updated successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                }
                            } else {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        if (userId != null) {
            fStore.collection("users").document(userId).get().addOnSuccessListener { result ->
                if (result.get("Role").toString().equals("Organizer")) {
                    menuInflater.inflate(R.menu.menu_main_organizers, menu)
                } else {
                    menuInflater.inflate(R.menu.menu_main_volunteers, menu)
                }
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        if (userId != null) {
            fStore.collection("users").document(userId).get().addOnSuccessListener { result ->
                if (result.get("Role").toString().equals("Organizer")) {
                    var id = item.itemId

                    if (id == R.id.logo) {
                    } else if (id == R.id.home_page) {
                        startActivity(Intent(this, HomeOrganizersActivity::class.java))
                        finish()
                    } else if (id == R.id.profile_view_org) {
                        val intent = Intent(this, ProfileViewActivity::class.java)
                        startActivity(intent)
                    } else if (id == R.id.view_events) {
                        startActivity(Intent(this, ViewEventsActivity::class.java))
                        finish()
                    } else if (id == R.id.create_event) {
                        startActivity(Intent(this, CreateEventActivity::class.java))
                        finish()
                    } else if (id == R.id.view_donations) {
                        startActivity(Intent(this, ViewDonationsActivity::class.java))
                        finish()
                    } else if (id == R.id.create_donation) {
                        startActivity(Intent(this, CreateDonationActivity::class.java))
                        finish()
                    } else if (id == R.id.Chat) {
                        startActivity(Intent(this, ViewChatActivity::class.java))
                        finish()
                    } else if (id == R.id.logout) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(baseContext, "Logged out.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else {
                    var id = item.itemId

                    if (id == R.id.logo) {
                    } else if (id == R.id.home_page) {
                        startActivity(Intent(this, HomeVolunteersActivity::class.java))
                        finish()
                    } else if (id == R.id.profile_view) {
                        val intent = Intent(this, ProfileViewActivity::class.java)
                        startActivity(intent)
                    } else if (id == R.id.view_events) {
                        startActivity(Intent(this, ViewEventsActivity::class.java))
                        finish()
                    } else if (id == R.id.view_donations) {
                        startActivity(Intent(this, ViewDonationsActivity::class.java))
                        finish()
                    } else if (id == R.id.Chat) {
                        startActivity(Intent(this, ViewChatActivity::class.java))
                        finish()
                    } else if (id == R.id.logout) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(baseContext, "Logged out.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        if (userId != null) {
            fStore.collection("users").document(userId).get().addOnSuccessListener { result ->
                if (result.get("Role").toString().equals("Organizer")) {
                    startActivity(Intent(this, HomeOrganizersActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, HomeVolunteersActivity::class.java))
                    finish()

                }
            }
        }
    }
}
