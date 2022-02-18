package Comp3200.volunteernearmeapp

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.collection.LLRBNode
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewDonationsActivity: AppCompatActivity() {
    private lateinit var fStore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_donations)
        fStore = Firebase.firestore
        val linearLayout = findViewById<View>(R.id.linear) as LinearLayout //find the linear layout

        linearLayout.removeAllViews() //add this too
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        if (userId != null) {
            fStore.collection("donations").get().addOnSuccessListener { result ->
                for (document in result) {
                    val textView = TextView(this)

                    textView.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,100
                        )
                    textView.gravity = Gravity.CENTER_VERTICAL //set the gravity too
                    textView.text = "Name: " +document.get("Name").toString() //adding text
                    linearLayout.addView(textView) //inflating :)



                    val textView2 = TextView(this)

                    textView2.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, 100
                        )
                    textView2.gravity = Gravity.CENTER_VERTICAL //set the gravity too
                    textView2.text = "Description: " +document.get("Description").toString() //adding text
                    linearLayout.addView(textView2)

                    val textView1 = TextView(this)

                    textView1.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, 100
                        )
                    textView1.gravity = Gravity.CENTER_VERTICAL //set the gravity too
                    textView1.text = document.get("Link").toString() //adding text
                    textView1.setTextColor(Color.BLUE)
                    linearLayout.addView(textView1)
//                  TODO: MAKE LINK CLICKABLE
                    val textView3 = TextView(this)

                    textView3.layoutParams =
                        LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, 45
                        )
                    textView3.gravity = Gravity.CENTER_VERTICAL //set the gravity too
                    textView3.text = "--------------------------------------------------------------------------------------------------" //adding text
                    linearLayout.addView(textView3)
                }
            }
        }
    }
}