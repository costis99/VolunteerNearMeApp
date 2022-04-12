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

class ViewDonationsActivity: AppCompatActivity() {
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_donations)
        fStore = Firebase.firestore
        val linearLayout = findViewById<View>(R.id.linear) as LinearLayout //find the linear layout

        linearLayout.removeAllViews() //add this too
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        if (userId != null) {
            fStore.collection("donations").get().addOnSuccessListener { result ->
                for (document in result) {
                    val textView = TextView(this)

                    textView.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,100
                        )
                    textView.gravity = Gravity.CENTER_VERTICAL //set the gravity too
                    textView.text = "Name: " +document.get("Name").toString() //adding text
                    textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                    linearLayout.addView(textView) //inflating :)



                    val textView2 = TextView(this)

                    textView2.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, 100
                        )
                    textView2.gravity = Gravity.CENTER_VERTICAL //set the gravity too
                    textView2.text = "Description: " +document.get("Description").toString() //adding text
                    textView2.setTypeface(textView.getTypeface(), Typeface.BOLD);
                    linearLayout.addView(textView2)

                    val textView1 = Button(this)

                    textView1.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, 100
                        )
                    textView1.gravity = Gravity.CENTER_VERTICAL //set the gravity too
                    textView1.text = "Donate" //adding text
                    textView1.setTextColor(Color.BLUE)
                    linearLayout.addView(textView1)
//                  MAKE LINK CLICKABLE
                    textView1.setOnClickListener {
                        val i = Intent(Intent.ACTION_VIEW, Uri.parse(document.get("Link").toString()))
                        startActivity(i)
                    }
                    val textView3 = TextView(this)

                    textView3.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, 45
                        )
                    textView3.gravity = Gravity.CENTER_VERTICAL //set the gravity too
                    textView3.text = "--------------------------------------------------------------------------------------------------" //adding text
                    linearLayout.addView(textView3)
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
                    } else if (id == R.id.view_donations) {
                        startActivity(Intent(this, ViewDonationsActivity::class.java))
                        finish()
                    } else if (id == R.id.create_donation) {
                        startActivity(Intent(this, CreateDonationActivity::class.java))
                        finish()
                    }else if (id == R.id.instructions) {
                        startActivity(Intent(this, InstructionsOrganizerActivity::class.java))
                        finish()
                    }else if (id == R.id.Chat) {
                        startActivity(Intent(this, MainChatActivity::class.java))
                        finish()
                    }
                    else if (id == R.id.logout) {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(baseContext, "Logged out.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else {
                    var id = item.itemId

                    if (id == R.id.logo) {
                    }else if (id == R.id.home_page){
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
                    }else if (id == R.id.Chat) {
                        startActivity(Intent(this, MainChatActivity::class.java))
                        finish()
                    }else if (id == R.id.instructions) {
                        startActivity(Intent(this, InstructionsVolunteerActivity::class.java))
                        finish()
                    }
                    else if (id == R.id.logout) {
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