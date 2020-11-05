package com.squareit.tripaja.ui.createpost

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareit.tripaja.R
import kotlinx.android.synthetic.main.activity_search_destination.*

class SearchDestinationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_destination)

        btnBack.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("Location", "Nusa Penida")
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}