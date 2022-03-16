package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainChatActivity : AppCompatActivity() {
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main_page)
        fStore = Firebase.firestore
        val generalChat: Button = findViewById(R.id.generalChatButton)
        generalChat.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "chat")
            startActivity(i)
        }
        val climateChange: Button = findViewById(R.id.ClimateChangeButton)
        climateChange.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "ClimateChangeChat")
            startActivity(i)
        }

        val childrenAndYouth: Button = findViewById(R.id.ChildrenAndYouthButton)
        childrenAndYouth.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "ChildrenAndYouthChat")
            startActivity(i)
        }

        val communityDevelopment: Button = findViewById(R.id.CommunityDevelopmentButton)
        communityDevelopment.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "CommunityDevelopmentChat")
            startActivity(i)
        }

        val ed: Button = findViewById(R.id.EdButton)
        ed.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "EducationChat")
            startActivity(i)
        }

        val environment: Button = findViewById(R.id.EnvironmentButton)
        environment.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "EnvironmentChat")
            startActivity(i)
        }

        val health: Button = findViewById(R.id.HealthButton)
        health.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "HealthChat")
            startActivity(i)
        }

        val wildlifeProtection: Button = findViewById(R.id.WildlifeProtectionButton)
        wildlifeProtection.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "WildlifeProtectionChat")
            startActivity(i)
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
                    val id = item.itemId

                    if (id == R.id.logo) {
                    } else if (id == R.id.home_page){
                        startActivity(Intent(this, HomeOrganizersActivity::class.java))
                        finish()
                    }
                    else if (id == R.id.profile_view_org) {
                        val intent = Intent(this, ProfileViewActivity::class.java)
                        startActivity(intent)
                    } else if (id == R.id.view_events) {
                        startActivity(Intent(this, ViewEventsActivity::class.java))
                        finish()
                    } else if (id == R.id.create_event) {
                        startActivity(Intent(this, CreateEventActivity::class.java))
                        finish()
                    }else if (id == R.id.view_donations) {
                        startActivity(Intent(this, ViewDonationsActivity::class.java))
                        finish()
                    } else if (id == R.id.create_donation) {
                        startActivity(Intent(this, CreateDonationActivity::class.java))
                        finish()
                    }else if (id == R.id.Chat) {
                        startActivity(Intent(this, MainChatActivity::class.java))
                        finish()
                    } else if (id == R.id.logout) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(baseContext, "Logged out.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else {
                    val id = item.itemId

                    if (id == R.id.logo) {
                    } else if (id == R.id.home_page){
                        startActivity(Intent(this, HomeVolunteersActivity::class.java))
                        finish()
                    }
                    else if (id == R.id.profile_view) {
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
                    }else if (id == R.id.logout) {
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
}