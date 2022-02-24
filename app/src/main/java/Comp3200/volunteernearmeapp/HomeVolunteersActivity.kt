package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
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

class HomeVolunteersActivity : AppCompatActivity() {
    private lateinit var fStore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_volunteers)
        fStore = Firebase.firestore
        auth = Firebase.auth
        val tv: TextView = findViewById(R.id.hello_vol)
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        if (userId != null) {
            fStore.collection("users").document(userId).get().addOnSuccessListener { result ->
                if (result.get("Nickname").toString() != null && !result.get("Nickname").toString().isEmpty()) {
                    tv.text = "Hello " + result.get("Nickname") + "!"
                }
                else{
                    tv.text = "Hello Volunteer!"
                }
            }
        }
        val picture: ImageView = findViewById(R.id.memberPicture)
        val userID = auth.currentUser?.uid
        if (userID != null) {
            fStore.collection("users").document(userID).get().addOnSuccessListener { result ->

                if(result.get("profilePic") != null) {
                    Picasso.get()
                        .load(result.get("profilePic").toString())
                        .fit()
                        .into(picture)
                }
            }
        }
        // If the volunteer clicks on the View Events button go to the view events activity
        val viewEvents: Button = findViewById(R.id.ViewEventsButton)
        viewEvents.setOnClickListener {
            startActivity(Intent(this, ViewEventsActivity::class.java))
        }
        val viewDonation: Button = findViewById(R.id.ViewDonationsButton)
        viewDonation.setOnClickListener {
            startActivity(Intent(this, ViewDonationsActivity::class.java))
        }
        val chat: Button = findViewById(R.id.ChatButton)
        chat.setOnClickListener {
            startActivity(Intent(this, ViewChatActivity::class.java))
        }
        // If the volunteer clicks on the logout button log the volunteer out of the system and return to the
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
        menuInflater.inflate(R.menu.menu_main_volunteers, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId

        if (id == R.id.logo) {
        } else if (id == R.id.home_page){
            startActivity(Intent(this, HomeVolunteersActivity::class.java))
            finish()
        } else if (id == R.id.profile_view){
            val intent = Intent(this@HomeVolunteersActivity, ProfileViewActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.view_events) {
            startActivity(Intent(this, ViewEventsActivity::class.java))
            finish()
        }else if (id == R.id.view_donations) {
            startActivity(Intent(this, ViewDonationsActivity::class.java))
            finish()
        }else if (id == R.id.Chat) {
            startActivity(Intent(this, ViewChatActivity::class.java))
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