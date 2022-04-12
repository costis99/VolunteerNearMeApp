package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class InstructionsOrganizerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructions_organizer)
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
            val intent = Intent(this, ProfileViewActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.view_events) {
            startActivity(Intent(this, ViewEventsActivity::class.java))
            finish()
        } else if (id == R.id.create_event){
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
        else if(id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(baseContext, "Logged out.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
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