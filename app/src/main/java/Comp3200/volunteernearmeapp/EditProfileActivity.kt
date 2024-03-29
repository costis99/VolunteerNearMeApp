package Comp3200.volunteernearmeapp

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.createSource
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Activity that allows user to CHANGE profile information (Profile picture and Nickname)
 */
class EditProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var profilePic: Uri? = null
    private val mExecutor: Executor = Executors.newSingleThreadExecutor()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        auth = Firebase.auth
        fStore = Firebase.firestore
        val nickname: EditText = findViewById(R.id.tv_nickname)
        val picture: ImageView = findViewById(R.id.memberPicture)
        val userID = auth.currentUser?.uid
        if (userID != null) {
            // Display the users current profile picture if it is not null
            fStore.collection("users").document(userID).get().addOnSuccessListener { result ->

                if (result.get("profilePic") != null) {
                    Picasso.get()
                        .load(result.get("profilePic").toString())
                        .fit()
                        .into(picture)
                }
            }
        }
        val getResult =
            // Allows user to pick a new profile picture
            registerForActivityResult(
                // Find profile picture from device location
                ActivityResultContracts.StartActivityForResult()
            ) {
                // If the picture exists
                if (it.resultCode == Activity.RESULT_OK) {
                    // Find selected picture's location
                    val data: Intent? = it.data
                    // Store new picture's location
                    profilePic =
                        data?.data
                    // Display picture to the current user
                    val source = profilePic?.let { it1 ->
                        createSource(
                            this.contentResolver,
                            it1
                        )
                    }
                    // Using a bitmap decoder display the selected picture where
                    //  the old profile picture was shown
                    val bitmap = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                    picture.setImageBitmap(bitmap)
                }
            }
        picture.setOnClickListener {
            // Launch getResult activity where the user can change profile picture
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getResult.launch(intent)
        }
        // When the save button is clicked allow user to change displayed nickname
        val saver: Button = findViewById(R.id.save_button)
        saver.setOnClickListener {
            saveChanges(nickname)
        }
        // Go back to the view profile activity
        val viewProfile: Button = findViewById(R.id.view_profile)
        viewProfile.setOnClickListener {
            startActivity(Intent(this, ProfileViewActivity::class.java))
        }

    }

    // Apply changes according to what the user has changed
    private fun saveChanges(nickname: EditText) {
        if (profilePic == null) {
            val user = Firebase.auth.currentUser
            val userId = user?.uid
            if (nickname.text != null) {
                if (userId != null) {
                    fStore.collection("users").document(userId).get()
                        .addOnSuccessListener { result ->
                            val name: HashMap<String, String>
                            if (result.get("profilePic") == null) {
                                name = hashMapOf(
                                    "Email ID" to user.email.toString(),
                                    "Nickname" to nickname.text.toString(),
                                    "Role" to result.get("Role").toString()
                                )
                            } else {
                                name = hashMapOf(
                                    "Email ID" to user.email.toString(),
                                    "Nickname" to nickname.text.toString(),
                                    "Role" to result.get("Role").toString(),
                                    "profilePic" to result.get("profilePic").toString()
                                )

                            }
                            fStore.collection("users").document(userId).set(name)
                            Toast.makeText(
                                baseContext,
                                "Nickname updated successfully!",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                }

            }
        } else {
            //Give the profile picture a random string name
            val str = UUID.randomUUID().toString()
            //Get in the /images folder within firebase storage
            val reference = FirebaseStorage.getInstance().getReference("/images/$str")
            //uploads from a content URI to this StorageReference
            reference.putFile(profilePic!!).addOnSuccessListener {
                //With in the file location download the image URL and
                //check if download was successful
                reference.downloadUrl.addOnSuccessListener {
                    //Get the image URL so that it is added to the users collection
                    val imageUrl = it.toString()
                    val user = Firebase.auth.currentUser
                    val userId = user?.uid
                    if (nickname.text != null) {
                        if (userId != null) {
                            fStore.collection("users").document(userId).get()
                                .addOnSuccessListener { result ->
                                    val name: HashMap<String, String>
                                    if (nickname.text.toString()
                                            .isEmpty() && result.get("Nickname") == null
                                    ) {
                                        name = hashMapOf(
                                            "Email ID" to user.email.toString(),
                                            "Nickname" to nickname.text.toString(),
                                            "Role" to result.get("Role").toString(),
                                            "profilePic" to imageUrl
                                        )
                                    } else if (nickname.text.toString()
                                            .isEmpty() && result.get("Nickname") != null
                                    ) {
                                        name = hashMapOf(
                                            "Email ID" to user.email.toString(),
                                            "Nickname" to result.get("Nickname").toString(),
                                            "Role" to result.get("Role").toString(),
                                            "profilePic" to imageUrl
                                        )
                                    } else {
                                        name = hashMapOf(
                                            "Email ID" to user.email.toString(),
                                            "Nickname" to nickname.text.toString(),
                                            "Role" to result.get("Role").toString(),
                                            "profilePic" to imageUrl
                                        )
                                    }
                                    fStore.collection("users").document(userId).set(name)
                                    Toast.makeText(
                                        baseContext,
                                        "Profile updated successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                        }

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
                        startActivity(Intent(this, LoginActivity::class.java))
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
