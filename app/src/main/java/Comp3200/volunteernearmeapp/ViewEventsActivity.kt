// Copyright Copyright 2022 Constantinos Papavasiliou
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package Comp3200.volunteernearmeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
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

/*
Adapted and modified from
https://github.com/googlecodelabs/maps-platform-101-android/tree/main/solution/app/src/main/java/com/google/codelabs/buildyourfirstmap
 */
class ViewEventsActivity : AppCompatActivity() {
    //firestore instance
    var mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var fStore: FirebaseFirestore
    var mLocation: Location? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_events)
        fStore = Firebase.firestore
        currentLocation = Location(LocationManager.GPS_PROVIDER)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

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

    //Request permission in order to access user's Location
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission granted
                } else {
                    // permission denied
                    Toast.makeText(
                        this, "You need to grant permission to access location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
    @SuppressLint("MissingPermission")
    private fun addMarkers(googleMap: GoogleMap) {
        // Find location of the device
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                mLocation = location
                if (location != null) {
                    // Convert location as a LatLng object
                    val latLng = LatLng(location.latitude, location.longitude)
                    // Add a red marker for the location of the device
                    googleMap.addMarker(MarkerOptions().title("Your Location").position(latLng))
                    // Once the map is opened zoom in at the location of device
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
                } else {
                    // If the permissions for the location are not allowed show appropriate message
                    Toast.makeText(
                        this, "You need to grant permission to access location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                // If the permissions for the location are not allowed show appropriate message to the user
                Toast.makeText(
                    this, "You need to grant permission to access location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        //Loop through the database of events
        mFirebaseDatabaseInstance.collection("eventsPending")
            .get()
            .addOnSuccessListener { result ->
                val user = Firebase.auth.currentUser
                val userId = user?.uid
                if (userId != null) {
                    // Add events to organizers VIEW EVENTS
                    // All events must me shown through the purple marker
                    mFirebaseDatabaseInstance.collection("users").document(userId).get()
                        .addOnSuccessListener { output ->
                            if (output.get("Role").toString().equals("Organizer")) {
                                for (document in result) {
                                    //get longitude and latitude of each event and create a LatLng object
                                    val landl = LatLng(
                                        document.data.getValue("Latitude") as Double,
                                        document.data.getValue("Longitude") as Double
                                    )
                                    // Add name, address and description of an event into a
                                    // Place object
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
                            } else {
                                // Add events for volunteers according to their interests and types of events
                                // Events shown in pink are the one volunteers are interested the rest
                                // can are shown in purple
                                for (document in result) {
                                    mFirebaseDatabaseInstance.collection("interests")
                                        .document(userId)
                                        .get().addOnSuccessListener { interest ->
                                            if (!document.get("Type").toString()
                                                    .equals("No Type") &&
                                                interest.get(document.get("Type").toString())
                                                    .toString().equals("Interested")
                                            ) {
                                                //get longitude and latitude of each event and create a LatLng object
                                                val landl = LatLng(
                                                    document.data.getValue("Latitude") as Double,
                                                    document.data.getValue("Longitude") as Double
                                                )
                                                // Add name, address and description of an event into a
                                                // Place object
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
                                                            .icon(interestLogoMark)
                                                    )

                                                // Set place as the tag on the marker object so it can be referenced within
                                                // MarkerInfoWindowAdapter
                                                marker.tag = place
                                            } else {
                                                //get longitude and latitude of each event and create a LatLng object
                                                val landl = LatLng(
                                                    document.data.getValue("Latitude") as Double,
                                                    document.data.getValue("Longitude") as Double
                                                )
                                                // Add name, address and description of an event into a
                                                // Place object
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
                            }
                        }
                }
            }
    }

    //Add event marker from drawable folder
    private val interestLogoMark: BitmapDescriptor by lazy {
        BitmapHelper.vectorToBitmap(this, R.drawable.ic_baseline_location_on_24, -65281)
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

