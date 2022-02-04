package Comp3200.volunteernearmeapp;

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class LoadingScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)
        //create animation
        val logo : ImageView = findViewById(R.id.Logo)
        logo.alpha=0f
        //make animation fade in and then fade out
        logo.animate().setDuration(1500).alpha(1f).withEndAction{
            // after that go to the login page
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}
