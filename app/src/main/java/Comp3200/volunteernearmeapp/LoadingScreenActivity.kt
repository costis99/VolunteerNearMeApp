package Comp3200.volunteernearmeapp;

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity demonstrating the Loading Screen Animations once the Application is started
 */
class LoadingScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)
        // Animation when starting the application
        val logo: ImageView = findViewById(R.id.Logo)
        logo.alpha = 0f
        // Make animation fade in/out and then go to the login page
        logo.animate().setDuration(1500).alpha(1f).withEndAction {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}
