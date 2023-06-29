package greek.army.leledometro.ui.freeday

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
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

class AddFreeDayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_free_day)

        supportActionBar?.hide()

        val btnBack: ImageButton = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            this.finish()
        }


        val editTextStartDate: TextView = findViewById(R.id.txt_start_date)
        editTextStartDate.setOnClickListener {
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

                    editTextStartDate.text = "$selectedDayStr/$selectedMonthStr/$selectedYearStr"
                },
                currentYear, currentMonth, currentDay
            ).show()
        }


        val editTextEndDate: TextView = findViewById(R.id.txt_end_date)
        editTextEndDate.setOnClickListener {
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

                    editTextEndDate.text = "$selectedDayStr/$selectedMonthStr/$selectedYearStr"
                },
                currentYear, currentMonth, currentDay
            ).show()
        }


        val btnAdd: ImageButton = findViewById(R.id.btn_add)
        btnAdd.setOnClickListener {
            btnAddOnClickListener()
        }
    }


    private fun btnAddOnClickListener(){
        val txtEditTitle: EditText = findViewById(R.id.txt_edit_title)
        val txtStartDate: TextView = findViewById(R.id.txt_start_date)
        val txtEndDate: TextView = findViewById(R.id.txt_end_date)

        val title: String = txtEditTitle.text.toString()
        val startDateStr: String = txtStartDate.text.toString()
        val endDateStr: String = txtEndDate.text.toString()

        if(startDateStr != "" && endDateStr != ""){
            val daysCount: Int
            if(Build.VERSION.SDK_INT >= 26){
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val startDateLocal = LocalDate.parse(startDateStr, formatter)
                val endDate = LocalDate.parse(endDateStr, formatter)
                daysCount = ChronoUnit.DAYS.between(startDateLocal, endDate).toInt() + 1
                val startDate = Date.from(startDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant())
                val dbHelper = DatabaseHelper(this)
                dbHelper.insertFreeDay(title, startDate, daysCount)
            } else{
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val startDate = formatter.parse(startDateStr)
                val endDate = formatter.parse(endDateStr)
                daysCount = ((endDate.time - startDate.time)/(1000 * 60 * 60 * 24)).toInt() + 1
                val dbHelper = DatabaseHelper(this)
                dbHelper.insertFreeDay(title, startDate, daysCount)
            }
            this.finish()
        } else{
            Toast.makeText(this, "Λάθος Πληροφορίες", Toast.LENGTH_SHORT).show()
        }
    }
}