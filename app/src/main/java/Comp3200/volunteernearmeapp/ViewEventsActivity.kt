package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewEventsActivity : AppCompatActivity() {
    //firestore instance
    var mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_events)
        fStore = Firebase.firestore
        //create map fragment
        val mapFragment = supportFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            addMarkers(googleMap)
            // Set custom info window adapter
            googleMap.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))
        }

    }

    //Add event marker from drawable folder
    private val logoMark: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.design_default_color_primary)
        BitmapHelper.vectorToBitmap(this, R.drawable.ic_baseline_location_on_24, color)
    }

    /**
     * Adds marker representations of the places list on the provided GoogleMap object
     */
    private fun addMarkers(googleMap: GoogleMap) {
        //Loop through the database of events
        mFirebaseDatabaseInstance.collection("eventsPending")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    //get longitude and latitude of each event and create a LatLng object
                    val landl = LatLng(
                        document.data.getValue("Latitude") as Double,
                        document.data.getValue("Longitude") as Double
                    )
                    val place = Place(
                        document.data.getValue("Name") as String,
                        landl,
                        document.data.getValue("Vicinity").toString(),
                        document.data.getValue("Description").toString()
                    )
                    //Add marker in the map
                    val marker =
                        googleMap.addMarker(
                            MarkerOptions()
                                .title("Place name")
                                .position(landl)
                                .icon(logoMark)
                        )

                    // Set place as the tag on the marker object so it can be referenced within
                    // MarkerInfoWindowAdapter
                    marker.tag = place
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
                    }else if (id == R.id.view_donations) {
                        startActivity(Intent(this, ViewDonationsActivity::class.java))
                        finish()
                    } else if (id == R.id.create_donation) {
                        startActivity(Intent(this, CreateDonationActivity::class.java))
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
                    } else if (id == R.id.home_page){
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
}

