package Comp3200.volunteernearmeapp

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

/**
 * Activity that allows only Organizers to create Events (add all Events
 *  created successfully into Firestore)
 */
class CreateEventActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    //Create Firebase Firestore instance
    private var mFirebaseDatabaseInstance: FirebaseFirestore? = null
    var latit: Double = 0.0
    var longitu: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        auth = Firebase.auth
        mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
        val spinner = findViewById<Spinner>(R.id.interestsSpinner)
        val items = arrayOf(
            "No Type", "Climate change", "Children and Youth",
            "Community Development", "Education", "Environment", "Health", "Wildlife Protection"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = adapter
        val createEv: Button = findViewById(R.id.CreateButton)
        createEv.setOnClickListener {
            creatorOfEvents()
        }
    }

    // Convert an address given in to latitude and longitude (according to the google maps addresses)
    fun convertAddress(address: String) {
        val geoCoder = Geocoder(this)
        if (!address.isEmpty()) {
            try {
                val addressList: List<Address> = geoCoder.getFromLocationName(address, 1)
                if (addressList.size > 0) {
                    latit = addressList[0].getLatitude()
                    longitu = addressList[0].getLongitude()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun creatorOfEvents() {
        val nameOfEvent: TextView = findViewById(R.id.et_eventName);
        val addressOfEvent: TextView = findViewById(R.id.et_address)
        val spinner = findViewById<Spinner>(R.id.interestsSpinner)
        val eventDesc: TextView = findViewById(R.id.et_eventDesc)

        //Check if name of event is empty and show appropriate message to the user
        if (nameOfEvent.text.toString().isEmpty()) {
            nameOfEvent.error = "Please enter a non empty name of event"
            nameOfEvent.requestFocus()
            return
        }
        convertAddress(addressOfEvent.text.toString())
        //Check if address of event is empty and show appropriate message to the user
        if (addressOfEvent.text.toString().isEmpty()) {
            addressOfEvent.error = "Please enter a non empty address"
            addressOfEvent.requestFocus()
            return
        }
        //Check if event description is empty and show appropriate message to the user
        if (eventDesc.text.toString().isEmpty()) {
            eventDesc.error = "Please enter a non empty event Description"
            eventDesc.requestFocus()
            return
        }
        if (longitu.equals(0.0) || latit.equals(0.0)) {
            addressOfEvent.error = "Please check the address you entered"
            addressOfEvent.requestFocus()
            return
        }

        val lo: Double = longitu

        val la: Double = latit

        val user = Firebase.auth.currentUser
        val userId = user?.uid
        //Make a hashmap of the data that are going to be added to the database
        val ev = hashMapOf(
            "Name" to nameOfEvent.text.toString(),
            "Latitude" to la,
            "Longitude" to lo,
            "Vicinity" to addressOfEvent.text.toString(),
            "Description" to eventDesc.text.toString(),
            "Creator" to userId,
            "Type" to spinner.selectedItem.toString()
        )
        //Add the event creation hashmap to the database
        val rand = (100000000..10000000000000).random()
        if (userId != null) {
            mFirebaseDatabaseInstance?.collection("eventsPending")?.document(rand.toString())
                ?.set(ev)
        }
        //Once the event is added go back to Home Activity
        Toast.makeText(baseContext, "Event is added!", Toast.LENGTH_SHORT).show()
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
