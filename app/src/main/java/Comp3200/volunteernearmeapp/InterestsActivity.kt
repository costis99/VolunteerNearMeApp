package Comp3200.volunteernearmeapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class InterestsActivity: AppCompatActivity() {
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interests)
        fStore = Firebase.firestore
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        val climate: Switch = findViewById(R.id.ClimateSwitch)
        val children: Switch = findViewById(R.id.ChildrenSwitch)
        val community: Switch = findViewById(R.id.CommunitySwitch)
        val education: Switch = findViewById(R.id.EducationSwitch)
        val environment: Switch = findViewById(R.id.EnvironmentSwitch)
        val health: Switch = findViewById(R.id.HealthSwitch)
        val wildLife: Switch = findViewById(R.id.WildlifeSwitch)
        val save: Button = findViewById(R.id.saveBtn)
        val interestHashMap = hashMapOf<String,String>()
        save.setOnClickListener {
            if(climate.isChecked){
                interestHashMap["Climate change"] = "Interested"
            }
            else{
                interestHashMap["Climate change"] = "NotInterested"
            }

            if(children.isChecked){
                interestHashMap["Children and Youth"] = "Interested"
            }
            else{
                interestHashMap["Children and Youth"] = "NotInterested"
            }

            if(community.isChecked){
                interestHashMap["Community Development"] = "Interested"
            }
            else{
                interestHashMap["Community Development"] = "NotInterested"
            }

            if(education.isChecked){
                interestHashMap["Education"] = "Interested"
            }
            else{
                interestHashMap["Education"] = "NotInterested"
            }

            if(environment.isChecked){
                interestHashMap["Environment"] = "Interested"
            }
            else{
                interestHashMap["Environment"] = "NotInterested"
            }

            if(health.isChecked){
                interestHashMap["Health"] = "Interested"
            }
            else{
                interestHashMap["Health"] = "NotInterested"
            }

            if(wildLife.isChecked){
                interestHashMap["Wildlife Protection"] = "Interested"
            }
            else{
                interestHashMap["Wildlife Protection"] = "NotInterested"
            }
            if (userId != null) {
                fStore.collection("interests").document(userId).set(interestHashMap)
            }
            startActivity(Intent(this, HomeVolunteersActivity::class.java))
        }


        if (userId != null) {
            fStore.collection("interests").document(userId).get().addOnSuccessListener { result ->
                    if (result == null) {
                        val interests = hashMapOf(
                            "Climate change" to "NotInterested",
                            "Children and Youth" to "NotInterested",
                            "Community Development" to "NotInterested",
                            "Education" to "NotInterested",
                            "Environment" to "NotInterested",
                            "Health" to "NotInterested",
                            "Wildlife Protection" to "NotInterested"
                        )
                        fStore.collection("interests").document(userId).set(interests)
                    }
                else{
                    if(result.get("Climate change") == "Interested"){
                        climate.isChecked = true
                    }
                        if(result.get("Children and Youth") == "Interested"){
                            children.isChecked = true
                        }
                        if(result.get("Community Development") == "Interested"){
                            community.isChecked = true
                        }
                        if(result.get("Education") == "Interested"){
                            education.isChecked = true
                        }
                        if(result.get("Environment") == "Interested"){
                            environment.isChecked = true
                        }
                        if(result.get("Health") == "Interested"){
                            health.isChecked = true
                        }
                        if(result.get("Wildlife Protection") == "Interested"){
                            wildLife.isChecked = true
                        }
                }

                }
            }
        }

    }