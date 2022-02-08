package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class HomeVolunteersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_volunteers)
        val viewEvents: Button = findViewById(R.id.ViewEventsButton)
        viewEvents.setOnClickListener {
            startActivity(Intent(this, ViewEventsActivity::class.java ))
        }
        val logout: Button = findViewById(R.id.LogOutButton)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(baseContext, "Logged out.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}