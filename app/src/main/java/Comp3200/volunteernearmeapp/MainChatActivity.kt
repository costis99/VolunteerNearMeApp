package Comp3200.volunteernearmeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main_page)
        val generalChat: Button = findViewById(R.id.generalChatButton)
        generalChat.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "chat")
            startActivity(i)
        }
        val climateChange: Button = findViewById(R.id.ClimateChangeButton)
        climateChange.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "ClimateChangeChat")
            startActivity(i)
        }

        val childrenAndYouth: Button = findViewById(R.id.ChildrenAndYouthButton)
        childrenAndYouth.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "ChildrenAndYouthChat")
            startActivity(i)
        }

        val communityDevelopment: Button = findViewById(R.id.CommunityDevelopmentButton)
        communityDevelopment.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "CommunityDevelopmentChat")
            startActivity(i)
        }

        val ed: Button = findViewById(R.id.EdButton)
        ed.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "EducationChat")
            startActivity(i)
        }

        val environment: Button = findViewById(R.id.EnvironmentButton)
        environment.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "EnvironmentChat")
            startActivity(i)
        }

        val health: Button = findViewById(R.id.HealthButton)
        health.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "HealthChat")
            startActivity(i)
        }

        val wildlifeProtection: Button = findViewById(R.id.WildlifeProtectionButton)
        wildlifeProtection.setOnClickListener {
            val i = Intent(this, ViewChatActivity::class.java)
            i.putExtra("id", "WildlifeProtectionChat")
            startActivity(i)
        }
    }
}