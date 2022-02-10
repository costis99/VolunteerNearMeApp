package Comp3200.volunteernearmeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore

class ViewEventsActivity : AppCompatActivity() {
    //firestore instance
    var mFirebaseDatabaseInstance = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_events)
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
}

