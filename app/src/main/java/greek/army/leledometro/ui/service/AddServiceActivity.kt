package greek.army.leledometro.ui.service

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import greek.army.leledometro.R
import greek.army.leledometro.utils.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar

class AddServiceActivity : AppCompatActivity() {

    private val types = arrayOf("--", "Α.Μ.", "Α.Ο.Τ.", "Α.Υ.Δ.Μ.", "Αρχιφύλακας", "Β.Α.Υ.Δ.Μ.", "Γραφέας", "Δ.Υ.Λ.",
        "Δεκανέας Αλλαγής", "Επιφυλακή", "Εστιατόρια", "Εφημερία", "Έφοδος", "Θαλαμοφύλακας", "Κ.Ε.Π.Ι.Κ.", "Κ.Ψ.Μ.",
        "Καθαριότητα", "Κάμερες", "ΚΕΕΗΠ", "Κεντρική Πύλη", "Λ.Υ.Λ. (Όργανο)", "Λοχίας Υπηρεσίας", "Μαγειρία",
        "Νοσοκόμος - Τραυματιοφορέας", "Οδηγός", "Οπλίτης ΓΕΠ", "Ορχήστρα", "Περίπολο", "Πλυντήρια", "Πυρασφάλεια", "Ραντάρ",
        "Σημαία", "Σκοπιά", "Συνεργείο Κατάταξης", "Υγειονομική Κάκυψη", "Φυλάκιο")

    private val numbers = arrayOf("--", "1ο Νούμερο", "2ο Νούμερο", "3ο Νούμερο", "4ο Νούμερο", "24ωρη")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)

        supportActionBar?.hide()

        val btnBack: ImageButton = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            this.finish()
        }

        val spinnerServiceType: Spinner = findViewById(R.id.spinner_service_type)
        val serviceTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerServiceType.adapter = serviceTypeAdapter

        val spinnerServiceNumber: Spinner = findViewById(R.id.spinner_service_number)
        val serviceNumberAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers)
        serviceNumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerServiceNumber.adapter = serviceNumberAdapter

        val editTextDate: TextView = findViewById(R.id.txt_date)
        editTextDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, R.style.CustomCalendarDialog,
                { _, year, month, day ->
                    val selectedYear = year
                    val selectedMonth = month + 1
                    val selectedDay = day

                    val selectedDayStr = if(selectedDay < 10) "0$selectedDay" else "$selectedDay"
                    val selectedMonthStr = if(selectedMonth < 10) "0$selectedMonth" else "$selectedMonth"
                    val selectedYearStr = "$selectedYear"

                    editTextDate.text = "$selectedDayStr/$selectedMonthStr/$selectedYearStr"
                },
                currentYear, currentMonth, currentDay
            ).show()
        }

        val btnAdd: ImageButton = findViewById(R.id.btn_add)
        btnAdd.setOnClickListener {
            //check if values are valid
            val spinnerType: Spinner = findViewById(R.id.spinner_service_type)
            val type: String = spinnerType.selectedItem.toString()

            val spinnerNumber: Spinner = findViewById(R.id.spinner_service_number)
            val number: String = spinnerNumber.selectedItem.toString()

            val txtDate: TextView = findViewById(R.id.txt_date)
            val dateStr: String = txtDate.text.toString()

            if(type != "--" && number != "--" && dateStr != ""){
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val date = formatter.parse(dateStr)
                val dbHelper = DatabaseHelper(this)
                dbHelper.insertService(type, number, date)
                this.finish()
            }
            else{
                Toast.makeText(this, "Λάθος Πληροφορίες", Toast.LENGTH_SHORT).show()
            }
        }
    }
} 