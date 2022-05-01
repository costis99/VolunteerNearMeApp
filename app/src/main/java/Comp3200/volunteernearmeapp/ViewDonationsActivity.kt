package Comp3200.volunteernearmeapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
Add the donations from the database to the UI (DONATIONS option)
programmatically as the number of Donation Options may vary every time
 */
class ViewDonationsActivity : AppCompatActivity() {
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_donations)
        fStore = Firebase.firestore
        // Find from the XML file the linear layout
        val linearLayout = findViewById<View>(R.id.linear) as LinearLayout
        // Remove everything from the XML linear layout
        linearLayout.removeAllViews()
        // Loop the donations collection and add info about each donation along with its own button
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        if (userId != null) {
            // "donations" collection loop
            fStore.collection("donations").get().addOnSuccessListener { result ->
                for (document in result) {
                    /*
                        Programmatically add the name of each donation to the linear layout
                     */
                    val nameOfDonation = TextView(this)

                    nameOfDonation.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, 100
                        )
                    // Set the gravity of the name of each donation to center-vertical
                    nameOfDonation.gravity = Gravity.CENTER_VERTICAL
                    // Set the text
                    nameOfDonation.text = "Name: " + document.get("Name").toString()
                    nameOfDonation.setTypeface(nameOfDonation.getTypeface(), Typeface.BOLD);
                    // Add the name of the donation to the linear layout
                    linearLayout.addView(nameOfDonation)
                    /*
                        Programmatically add the description of each donation to the linear layout
                     */
                    val descriptionOfDonation = TextView(this)
                    descriptionOfDonation.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, 100
                        )
                    // Set the gravity of the description of each donation to center-vertical
                    descriptionOfDonation.gravity = Gravity.CENTER_VERTICAL
                    // Set the text
                    descriptionOfDonation.text =
                        "Description: " + document.get("Description").toString()
                    descriptionOfDonation.setTypeface(nameOfDonation.getTypeface(), Typeface.BOLD);
                    // Add to the linear layout
                    linearLayout.addView(descriptionOfDonation)
                    /*
                        Programmatically add the button to redirect the user to the website of each
                         donation so that the users can donate
                     */
                    val buttonOfDonation = Button(this)
                    buttonOfDonation.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, 100
                        )
                    // Set the gravity of the button of each donation to center-vertical
                    buttonOfDonation.gravity = Gravity.CENTER_VERTICAL
                    // Set the text of the button
                    buttonOfDonation.text = "Donate"
                    // Set colour to blue
                    buttonOfDonation.setTextColor(Color.BLUE)
                    // Add to the linear layout
                    linearLayout.addView(buttonOfDonation)
                    // Make the button clickable an redirect users to the appropriate link
                    buttonOfDonation.setOnClickListener {
                        val i =
                            Intent(Intent.ACTION_VIEW, Uri.parse(document.get("Link").toString()))
                        startActivity(i)
                    }
                    /*
                        Programmatically add the breaking line between the  donations
                     */
                    val breakingLine = TextView(this)

                    breakingLine.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, 45
                        )
                    // Set the gravity of the breaking line to center-vertical
                    breakingLine.gravity = Gravity.CENTER_VERTICAL
                    breakingLine.text =
                        "--------------------------------------------------------------------------------------------------"
                    // Add to the linear layout
                    linearLayout.addView(breakingLine)
                }
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
                    } else if (id == R.id.instructions) {
                        startActivity(Intent(this, InstructionsOrganizerActivity::class.java))
                        finish()
                    } else if (id == R.id.Chat) {
                        startActivity(Intent(this, MainChatActivity::class.java))
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
                        startActivity(Intent(this, MainChatActivity::class.java))
                        finish()
                    } else if (id == R.id.instructions) {
                        startActivity(Intent(this, InstructionsVolunteerActivity::class.java))
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