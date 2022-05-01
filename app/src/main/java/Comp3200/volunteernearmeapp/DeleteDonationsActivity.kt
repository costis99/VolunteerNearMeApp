package Comp3200.volunteernearmeapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
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
 * Activity that allows only Organizers to delete Donations that they have created (delete all
 *  Donations from Firestore)
 */
class DeleteDonationsActivity : AppCompatActivity() {
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_donations)
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
                    // Check that the current Organizer is the creator of the Donation
                    if (document.get("Creator").toString() == userId.toString()) {
                        val textView = TextView(this)
                        /*
                            Programmatically add the name of each donation to the linear layout
                        */
                        textView.layoutParams =
                            LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, 100
                            )
                        // Set the gravity of the name of each donation to center-vertical
                        textView.gravity = Gravity.CENTER_VERTICAL
                        // Set the text
                        textView.text = "Name: " + document.get("Name").toString()
                        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                        // Add the name of the donation to the linear layout
                        linearLayout.addView(textView)

                        /*
                            Programmatically add the Link of each donation to the linear layout
                         */
                        val textView2 = TextView(this)

                        textView2.layoutParams =
                            LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, 100
                            )
                        // Set the gravity of the link of each donation to center-vertical
                        textView2.gravity = Gravity.CENTER_VERTICAL
                        // Set the text = Link
                        textView2.text =
                            "Link: " + document.get("Link").toString()
                        textView2.setTypeface(textView.getTypeface(), Typeface.BOLD);
                        // Add the link of the donation to the linear layout
                        linearLayout.addView(textView2)
                        /*
                           Programmatically add the button to DELETE the current donation when pressed
                         */
                        val textView1 = Button(this)

                        textView1.layoutParams =
                            LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, 100
                            )
                        // Set the gravity of the button of each donation to center-vertical
                        textView1.gravity = Gravity.CENTER_VERTICAL
                        // Set the text of each button as CLOSE
                        textView1.text = "Close"
                        // Set the colour of the button to blue
                        textView1.setTextColor(Color.BLUE)
                        linearLayout.addView(textView1)
                        // MAKE Button CLICKABLE
                        // Once the button is clicked restart the activity to apply changes
                        textView1.setOnClickListener {
                            document.reference.delete()
                            startActivity(Intent(this, DeleteDonationsActivity::class.java))
                        }
                        /*
                        Programmatically add the breaking line between the close donations
                        */
                        val textView3 = TextView(this)

                        textView3.layoutParams =
                            LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, 45
                            )
                        textView3.gravity = Gravity.CENTER_VERTICAL
                        textView3.text =
                            "--------------------------------------------------------------------------------------------------"
                        linearLayout.addView(textView3)
                    }
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
        startActivity(Intent(this, HomeOrganizersActivity::class.java))
        finish()
    }
}