package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * Activity demonstrating the Interests of the current volunteer. Allow volunteers to change their
 * interests.
 */
class InterestsActivity : AppCompatActivity() {
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interests)
        fStore = Firebase.firestore
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        // Add the Switches for every Type of event
        val climate: Switch = findViewById(R.id.ClimateSwitch)
        val children: Switch = findViewById(R.id.ChildrenSwitch)
        val community: Switch = findViewById(R.id.CommunitySwitch)
        val education: Switch = findViewById(R.id.EducationSwitch)
        val environment: Switch = findViewById(R.id.EnvironmentSwitch)
        val health: Switch = findViewById(R.id.HealthSwitch)
        val wildLife: Switch = findViewById(R.id.WildlifeSwitch)
        val save: Button = findViewById(R.id.saveBtn)
        val interestHashMap = hashMapOf<String, String>()
        // Add into the hashmap as the key, every type of event with the value as Interested or
        // Not Interested according to if the switch is on or off.
        save.setOnClickListener {
            if (climate.isChecked) {
                interestHashMap["Climate change"] = "Interested"
            } else {
                interestHashMap["Climate change"] = "NotInterested"
            }

            if (children.isChecked) {
                interestHashMap["Children and Youth"] = "Interested"
            } else {
                interestHashMap["Children and Youth"] = "NotInterested"
            }

            if (community.isChecked) {
                interestHashMap["Community Development"] = "Interested"
            } else {
                interestHashMap["Community Development"] = "NotInterested"
            }

            if (education.isChecked) {
                interestHashMap["Education"] = "Interested"
            } else {
                interestHashMap["Education"] = "NotInterested"
            }

            if (environment.isChecked) {
                interestHashMap["Environment"] = "Interested"
            } else {
                interestHashMap["Environment"] = "NotInterested"
            }

            if (health.isChecked) {
                interestHashMap["Health"] = "Interested"
            } else {
                interestHashMap["Health"] = "NotInterested"
            }

            if (wildLife.isChecked) {
                interestHashMap["Wildlife Protection"] = "Interested"
            } else {
                interestHashMap["Wildlife Protection"] = "NotInterested"
            }
            if (userId != null) {
                fStore.collection("interests").document(userId).set(interestHashMap)
            }
            startActivity(Intent(this, HomeVolunteersActivity::class.java))
        }

        // Change the interests of the current User and save them to the Firestore
        if (userId != null) {
            fStore.collection("interests").document(userId).get().addOnSuccessListener { result ->
                if (result == null) {
                    val interests = hashMapOf(
                        "Climate change" to "NotInterested",
                        "Children and Youth" to "NotInterested",
                        "Community Development" to "NotInterested",
                        "Education" to "NotInterested",
                        "Environment" to "NotInterested",
                        "Health" to "NotInterested",
                        "Wildlife Protection" to "NotInterested"
                    )
                    fStore.collection("interests").document(userId).set(interests)
                } else {
                    if (result.get("Climate change") == "Interested") {
                        climate.isChecked = true
                    }
                    if (result.get("Children and Youth") == "Interested") {
                        children.isChecked = true
                    }
                    if (result.get("Community Development") == "Interested") {
                        community.isChecked = true
                    }
                    if (result.get("Education") == "Interested") {
                        education.isChecked = true
                    }
                    if (result.get("Environment") == "Interested") {
                        environment.isChecked = true
                    }
                    if (result.get("Health") == "Interested") {
                        health.isChecked = true
                    }
                    if (result.get("Wildlife Protection") == "Interested") {
                        wildLife.isChecked = true
                    }
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_volunteers, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HomeVolunteersActivity::class.java))
        finish()
    }

}