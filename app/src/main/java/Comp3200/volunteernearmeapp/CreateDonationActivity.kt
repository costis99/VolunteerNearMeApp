package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.webkit.URLUtil
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

/**
 * Activity that allows only Organizers to create Donations (add all Donations
 *  created successfully into Firestore)
 */
class CreateDonationActivity : AppCompatActivity() {
    //Create Firebase Firestore instance
    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_donation)
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val createDon: Button = findViewById(R.id.CreateDonationButton)
        createDon.setOnClickListener {
            creatorOfDonations()
        }
    }

    private fun creatorOfDonations() {
        val nameOfDon: TextView = findViewById(R.id.et_donationName);
        val linkOfDon: TextView = findViewById(R.id.et_donation_link)
        val descOfDon: TextView = findViewById(R.id.et_donationDesc)

        //Check if name of donation is empty and show appropriate message to the user
        if (nameOfDon.text.toString().isEmpty()) {
            nameOfDon.error = "Please enter a non empty name of donation"
            nameOfDon.requestFocus()
            return
        }
        //Check if link of donation is valid
        if (linkOfDon.text.toString().isEmpty() || !URLUtil.isValidUrl(linkOfDon.text.toString())
            || !Patterns.WEB_URL.matcher(linkOfDon.text.toString())
                .matches() || !linkOfDon.text.toString().contains("https://")
            || !linkOfDon.text.toString().contains("paypal")
        ) {
            linkOfDon.error = "Please enter a valid paypal link"
            linkOfDon.requestFocus()
            return
        }
        //Check if description of donation is empty and show appropriate message to the user
        if (descOfDon.text.toString().isEmpty()) {
            descOfDon.error = "Please enter a non empty name of donation"
            descOfDon.requestFocus()
            return
        }

        //Make a hashmap of the data that are going to be added to the database
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        val newDonation = hashMapOf(
            "Name" to nameOfDon.text.toString(),
            "Link" to linkOfDon.text.toString(),
            "Description" to descOfDon.text.toString(),
            "Creator" to userId
        )
        //Add the event creation hashmap to the database
        val rand = (100000000..10000000000000).random()
        if (userId != null) {
            mFirebaseDatabaseInstance?.collection("donations")?.document(rand.toString())
                ?.set(newDonation)
        }
        //Once the donation is added go back to Home Activity
        Toast.makeText(baseContext, "Donation has been added!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, HomeOrganizersActivity::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_organizers, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HomeOrganizersActivity::class.java))
        finish()
    }
}
