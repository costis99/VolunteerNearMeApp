package Comp3200.volunteernearmeapp

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName ("name") @Expose val name: String,
    @SerializedName("latLng") @Expose val latLng: LatLng,
    @SerializedName("address") @Expose val address: String,
    @SerializedName("description") @Expose val description: String
)
