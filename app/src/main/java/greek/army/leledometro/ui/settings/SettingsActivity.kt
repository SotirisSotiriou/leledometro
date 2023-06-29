package greek.army.leledometro.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import greek.army.leledometro.R
import greek.army.leledometro.utils.DatabaseHelper

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.hide()

        val btnClearAll: Button = findViewById(R.id.btn_clear_all)
        btnClearAll.setOnClickListener {
            btnClearAllOnClickListener()
        }

        val btnBack: ImageButton = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            this.finish()
        }
    }


    private fun btnClearAllOnClickListener(){
        val dbHelper = DatabaseHelper(this)
        dbHelper.clearAllData()
        Toast.makeText(this, "Η διαγραφή δεδομένων ολοκληρώθηκε", Toast.LENGTH_LONG).show()
    }
}