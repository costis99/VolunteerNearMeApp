package Comp3200.volunteernearmeapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

/**
 * Activity displaying to the current user their profile information (Email, Profile Picture, Nickname
 * and Role)
 */
class ProfileViewActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        auth = Firebase.auth
        fStore = Firebase.firestore

        val email: TextView = findViewById(R.id.tv_email)
        val nickname: TextView = findViewById(R.id.tv_nickname)
        val role: TextView = findViewById(R.id.tv_role)
        val picture: ImageView = findViewById(R.id.memberPicture)
        val userID = auth.currentUser?.uid
        if (userID != null) {
            // Loop through the users collection and find the profile picture under Profile Pic
            //  Using the Picasso library add the profile picture (if one exists) to it's place in the UI
            fStore.collection("users").document(userID).get().addOnSuccessListener { result ->
                if (result.get("profilePic") != null) {
                    Picasso.get()
                        .load(result.get("profilePic").toString())
                        .fit()
                        .into(picture)
                }
            }
        }

        addToUserProfile(email, nickname, role)

        val editProfile: Button = findViewById(R.id.editProfileButton)
        editProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        val resetPass: Button = findViewById(R.id.changePassword)
        resetPass.setOnClickListener {
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }
        val resetEmail: Button = findViewById(R.id.changeEmail)
        resetEmail.setOnClickListener {
            startActivity(Intent(this, ChangeEmailActivity::class.java))
        }
    }

    private fun addToUserProfile(email: TextView, nickname: TextView, role: TextView) {
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        fStore.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                if (document.data.get("Email ID") == user?.email) {
                    email.text = "Email: ${document.data.get("Email ID") as CharSequence?}"
                    if (document.data.get("Nickname") == null) {
                        nickname.text = "Nickname: No nickname added yet!"
                    } else {
                        nickname.text =
                            "Nickname: ${document.data.get("Nickname") as CharSequence?}"
                    }
                    role.text = "Role: ${document.data.get("Role") as CharSequence?}"
                }
            }
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
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
                        startActivity(Intent(this, MainChatActivity::class.java))
                        finish()
                    } else if (id == R.id.instructions) {
                        startActivity(Intent(this, InstructionsOrganizerActivity::class.java))
                        finish()
                    } else if (id == R.id.logout) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(baseContext, "Logged out.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
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
                        startActivity(Intent(this, MainChatActivity::class.java))
                        finish()
                    } else if (id == R.id.instructions) {
                        startActivity(Intent(this, InstructionsVolunteerActivity::class.java))
                        finish()
                    } else if (id == R.id.logout) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(baseContext, "Logged out.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
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
