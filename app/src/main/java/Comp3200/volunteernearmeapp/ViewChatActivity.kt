package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class ViewChatActivity : AppCompatActivity() {
    private lateinit var fStore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private val listOfChat = ArrayList<MessageType>()
    private val messageAdapter = MessageAdapter(listOfChat)
    var chatView = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        fStore = Firebase.firestore
        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val b = intent.extras
        chatView = b!!.getString("id").toString()
        //create a list of all the messages
        listOfMessages()
        val sender: ImageView = findViewById(R.id.send_chat_button)
        val msg: EditText = findViewById(R.id.typer)
        //When the user tries to send a new message in the chat
        //check if message is empty
        //else send it
        sender.setOnClickListener {
            val textMsg = msg.text.toString()
            if (textMsg == "") {
                Toast.makeText(this, "Message empty!", Toast.LENGTH_SHORT).show()
            } else {
                msgSender(textMsg)
                msg.text.clear()
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
                    val id = item.itemId

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
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else {
                    val id = item.itemId

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

    private fun msgSender(message: String) {
        //find time of current message
        val timeOfMessage = Date(Timestamp.now().seconds * 1000).toLocaleString()
        //associate message with current userID
        val userId = auth.currentUser!!.uid

        val newMessage = MessageType(message, userId, timeOfMessage)
        //Create hashmap of messageType
        val hm = hashMapOf(
            "messageContent" to message,
            "user" to userId,
            "time" to timeOfMessage
        )
        val viewer: RecyclerView = findViewById(R.id.chat_view)
        listOfChat.add(newMessage)
        viewer.scrollToPosition(listOfChat.size - 1)
        messageAdapter.notifyDataSetChanged()
        //add message along with the time and userID (hashmap) to the firestore db
        fStore.collection(chatView).document().set(hm).addOnSuccessListener {
            System.out.println("Success write on firestore")

        }
    }

    private fun listOfMessages() {
        fStore.collection(chatView).get().addOnSuccessListener { result ->
            for (document in result) {
                //get text, userID and time of each message within the db
                //then add all messages to the listOfChat
                val msg = MessageType(
                    document.data.get("messageContent") as String,
                    document.data.get("user") as String,
                    document.data.get("time").toString()
                )
                listOfChat += msg
            }
            //sort the list according to time of messages
            listOfChat.sortBy { it.time }
            val viewer: RecyclerView = findViewById(R.id.chat_view)
            viewer.adapter = messageAdapter
            //allow the users to scroll through messages
            viewer.layoutManager = LinearLayoutManager(this)
            viewer.scrollToPosition(listOfChat.size - 1)
            //optimize the viewer
            viewer.setHasFixedSize(true)

        }
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