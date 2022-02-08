package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class CreateEventActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var mFirebaseDatabaseInstance: FirebaseFirestore?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        auth = Firebase.auth
        mFirebaseDatabaseInstance= FirebaseFirestore.getInstance()
        val createEv: Button = findViewById(R.id.CreateButton)
        createEv.setOnClickListener {
            creatorOfEvents()
        }
    }

    private fun creatorOfEvents() {
        val nameOfEvent: TextView = findViewById(R.id.et_eventName);
        val addressOfEvent: TextView = findViewById(R.id.et_address)
        val longit: TextView = findViewById(R.id.et_log)
        val lo: Double = longit.getText().toString().toDouble()
        val latit: TextView = findViewById(R.id.et_lan)

        val la: Double = latit.getText().toString().toDouble()
        val eventDesc: TextView = findViewById(R.id.et_eventDesc)

        if (nameOfEvent.text.toString().isEmpty()) {
            nameOfEvent.error = "Please enter a non empty password"
            nameOfEvent.requestFocus()
            return
        }

        if (addressOfEvent.text.toString().isEmpty()) {
            addressOfEvent.error = "Please enter a non empty password"
            addressOfEvent.requestFocus()
            return
        }

        if (longit.text.toString().isEmpty()) {
            longit.error = "Please enter a non empty password"
            longit.requestFocus()
            return
        }

        if (latit.text.toString().isEmpty()) {
            latit.error = "Please enter a non empty password"
            latit.requestFocus()
            return
        }
        if (eventDesc.text.toString().isEmpty()) {
            eventDesc.error = "Please enter a non empty password"
            eventDesc.requestFocus()
            return
        }


        database = Firebase.database.reference
        val ev = hashMapOf(
            "Name" to nameOfEvent.text.toString(),
            "Latitude" to la,
            "Longitude" to lo,
            "Vicinity" to addressOfEvent.text.toString(),
            "Description" to eventDesc.text.toString()
        )
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        val rand = (100000000..10000000000000).random()
        if (userId != null) {
            mFirebaseDatabaseInstance?.collection("eventsPending")?.document(rand.toString())?.set(ev)}
        Toast.makeText(baseContext, "Event is added!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, HomeOrganizersActivity::class.java))
        finish()
    }
}
