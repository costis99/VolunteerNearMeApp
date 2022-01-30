package Comp3200.volunteernearmeapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        // Initialize Firebase Auth
        auth = Firebase.auth
        fStore = Firebase.firestore
       // val btnSignUp: Button = findViewById(R.id.btnSignUp)
//        btnSignUp.setOnClickListener {
//            uploadImageAndRegister()
//        }
    }
}
