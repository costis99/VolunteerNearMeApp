package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeOrganizersActivity : AppCompatActivity() {
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_organizers)
        fStore = Firebase.firestore
        val tv: TextView = findViewById(R.id.hello_org)
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        if (userId != null) {
            fStore.collection("users").document(userId).get().addOnSuccessListener { result ->
                if (result.get("Nickname").toString() != null && !result.get("Nickname").toString().isEmpty()) {
                    tv.text = "Hello " + result.get("Nickname") + "!"
                }
                else{
                    tv.text = "Hello Organizer!"
                }
            }.addOnFailureListener{
                tv.text = "Hello Organizer!"
            }
        }
        //If the organizer clicks on the View Events Button go to the View Events Activity
        val viewEvents: Button = findViewById(R.id.ViewEventsButton)
        viewEvents.setOnClickListener {
            startActivity(Intent(this, ViewEventsActivity::class.java))
        }
        //If the organizer clicks on Create Events Button go to the Create Event Activity
        val createEvent: Button = findViewById(R.id.CreateEventButton)
        createEvent.setOnClickListener {
            startActivity(Intent(this, CreateEventActivity::class.java))
        }
        // If the organizer clicks on the logout button log the organizer out of the system and return to the
        // login page
        val logout: Button = findViewById(R.id.LogOutButton)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(baseContext, "Logged out.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_organizers, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId

        if (id == R.id.logo) {
        } else if (id == R.id.home_page){
            startActivity(Intent(this, HomeOrganizersActivity::class.java))
            finish()
        }
        else if (id == R.id.profile_view_org){
            val intent = Intent(this@HomeOrganizersActivity, ProfileViewActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.view_events) {
            startActivity(Intent(this, ViewEventsActivity::class.java))
            finish()
        } else if (id == R.id.create_event){
            startActivity(Intent(this, CreateEventActivity::class.java))
            finish()
        } else if(id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(baseContext, "Logged out.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}