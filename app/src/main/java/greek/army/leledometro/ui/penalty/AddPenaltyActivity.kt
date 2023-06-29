package greek.army.leledometro.ui.penalty

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import greek.army.leledometro.R
import greek.army.leledometro.utils.DatabaseHelper
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date

class AddPenaltyActivity : AppCompatActivity() {

    private val types = arrayOf("--", "Κράτηση", "Φυλακή")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_penalty)

        supportActionBar?.hide()

        val btnBack: ImageButton = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            this.finish()
        }

        val spinnerPenaltyType: Spinner = findViewById(R.id.spinner_penalty_type)
        val penaltyTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        penaltyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPenaltyType.adapter = penaltyTypeAdapter
        


        val btnAdd: ImageButton = findViewById(R.id.btn_add)
        btnAdd.setOnClickListener {
            btnAddOnClickListener()
        }
    }


    private fun btnAddOnClickListener(){
        val spinnerPenaltyType: Spinner = findViewById(R.id.spinner_penalty_type)
        val txtEditTitle: EditText = findViewById(R.id.txt_edit_title)
        val txtEditDays: EditText = findViewById(R.id.txt_edit_days)

        val type: String = spinnerPenaltyType.selectedItem.toString()
        val title: String = txtEditTitle.text.toString()
        val days: Int = if(txtEditDays.text.toString() != "") Integer.parseInt(txtEditDays.text.toString()) else 0

        if(days > 0 && type != "--"){
            val dbHelper = DatabaseHelper(this)
            dbHelper.insertPenalty(type, title, days)
            this.finish()
        } else{
            Toast.makeText(this, "Λάθος Πληροφορίες", Toast.LENGTH_SHORT).show()
        }
    }
}