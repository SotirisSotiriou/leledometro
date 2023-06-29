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
import java.util.Locale

class EditServiceActivity : AppCompatActivity() {

    private val types = arrayOf("--", "Α.Μ.", "Α.Ο.Τ.", "Α.Υ.Δ.Μ.", "Αρχιφύλακας", "Β.Α.Υ.Δ.Μ.", "Γραφέας", "Δ.Υ.Λ.",
        "Δεκανέας Αλλαγής", "Επιφυλακή", "Εστιατόρια", "Εφημερία", "Έφοδος", "Θαλαμοφύλακας", "Κ.Ε.Π.Ι.Κ.", "Κ.Ψ.Μ.",
        "Καθαριότητα", "Κάμερες", "ΚΕΕΗΠ", "Κεντρική Πύλη", "Λ.Υ.Λ. (Όργανο)", "Λοχίας Υπηρεσίας", "Μαγειρία",
        "Νοσοκόμος - Τραυματιοφορέας", "Οδηγός", "Οπλίτης ΓΕΠ", "Ορχήστρα", "Περίπολο", "Πλυντήρια", "Πυρασφάλεια", "Ραντάρ",
        "Σημαία", "Σκοπιά", "Συνεργείο Κατάταξης", "Υγειονομική Κάκυψη", "Φυλάκιο")

    private val numbers = arrayOf("--", "1ο Νούμερο", "2ο Νούμερο", "3ο Νούμερο", "4ο Νούμερο", "24ωρη")


    private var spinnerServiceType: Spinner? = null
    private var spinnerServiceNumber: Spinner? = null
    private var txtEditDate: TextView? = null
    private var btnBack: ImageButton? = null
    private var btnSave: ImageButton? = null
    private var btnDelete: ImageButton? = null

    private val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_service)

        supportActionBar?.hide()

        val id = intent.getIntExtra("id", -1)

        if(id == -1) this.finish()

        val dbHelper = DatabaseHelper(this)
        val service = dbHelper.getServiceById(id)

        val selectedTypeIndex = types.indexOf(service?.type)
        val selectedNumberIndex = numbers.indexOf(service?.number)

        btnBack = findViewById(R.id.btn_back)
        btnBack?.setOnClickListener {
            this.finish()
        }

        btnSave = findViewById(R.id.btn_save)
        btnSave?.setOnClickListener {
            btnSaveOnClickListener(id)
        }

        btnDelete = findViewById(R.id.btn_delete)
        btnDelete?.setOnClickListener {
            btnDeleteOnClickListener(id)
            this.finish()
        }

        spinnerServiceType = findViewById(R.id.spinner_service_type)
        val serviceTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerServiceType?.adapter = serviceTypeAdapter
        spinnerServiceType?.setSelection(selectedTypeIndex)

        spinnerServiceNumber = findViewById(R.id.spinner_service_number)
        val serviceNumberAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers)
        serviceNumberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerServiceNumber?.adapter = serviceNumberAdapter
        spinnerServiceNumber?.setSelection(selectedNumberIndex)

        txtEditDate = findViewById(R.id.txt_date)
        txtEditDate?.text = service?.date?.let { formatter.format(it) }
        txtEditDate?.setOnClickListener {
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

                    txtEditDate?.text = "$selectedDayStr/$selectedMonthStr/$selectedYearStr"
                },
                currentYear, currentMonth, currentDay
            ).show()
        }
    }


    private fun btnSaveOnClickListener(id: Int){
        val dbHelper = DatabaseHelper(this)
        val type = spinnerServiceType?.selectedItem.toString()
        val number = spinnerServiceNumber?.selectedItem.toString()
        val dateStr = txtEditDate?.text.toString()
        val date = formatter.parse(dateStr)
        if(dateStr != "" && number != "--" && type != "--"){
            dbHelper.updateService(id, type, number, date)
            this.finish()
        } else{
            Toast.makeText(this, "Λάθος Πληροφορίες", Toast.LENGTH_SHORT).show()
        }

    }


    private fun btnDeleteOnClickListener(id: Int){
        val dbHelper = DatabaseHelper(this)
        dbHelper.deleteService(id)
        Toast.makeText(this, "Η υπηρεσία διαγράφηκε", Toast.LENGTH_SHORT).show()
    }
}