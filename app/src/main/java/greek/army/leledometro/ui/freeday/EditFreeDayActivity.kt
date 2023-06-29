package greek.army.leledometro.ui.freeday

import android.app.DatePickerDialog
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import greek.army.leledometro.R
import greek.army.leledometro.utils.DatabaseHelper
import greek.army.leledometro.utils.FreeDay
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditFreeDayActivity : AppCompatActivity() {

    private var txtEditTitle: EditText? = null
    private var txtStartDate: TextView? = null
    private var txtEndDate: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_free_day)

        supportActionBar?.hide()

        val dbHelper = DatabaseHelper(this)
        val id: Int = intent.getIntExtra("id", -1)
        if(id == -1) this.finish()
        val freeDay: FreeDay? = dbHelper.getFreeDayById(id)

        freeDay.let {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            txtEditTitle = findViewById(R.id.txt_edit_title)
            val titleEditable = Editable.Factory.getInstance().newEditable(it?.title)
            txtEditTitle?.text = titleEditable

            txtStartDate = findViewById(R.id.txt_start_date)
            txtStartDate?.text = it?.startDate?.let { it1 -> formatter.format(it1) }
            txtStartDate?.setOnClickListener {
                txtDateOnClickListener(txtStartDate!!)
            }

            val calendar = Calendar.getInstance()
            calendar.time = it?.startDate!!
            calendar.add(Calendar.DAY_OF_YEAR, it.daysCount)
            val endDate = calendar.time

            txtEndDate = findViewById(R.id.txt_end_date)
            txtEndDate?.text = endDate.let { it1 -> formatter.format(it1) }
            txtEndDate?.setOnClickListener {
                txtDateOnClickListener(txtEndDate!!)
            }
        }


        val btnBack: ImageButton = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            this.finish()
        }


        val btnSave: ImageButton = findViewById(R.id.btn_save)
        btnSave.setOnClickListener {
            btnSaveOnClickListener(id)
        }


        val btnDelete: ImageButton =  findViewById(R.id.btn_delete)
        btnDelete.setOnClickListener {
            btnDeleteOnClickListener(id)
            this.finish()
        }
    }


    private fun txtDateOnClickListener(view: TextView){
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val oldDate = formatter.parse(view.text.toString())
        calendar.time = oldDate!!
        val oldDay = calendar.get(Calendar.DAY_OF_MONTH)
        val oldMonth = calendar.get(Calendar.MONTH)
        val oldYear = calendar.get(Calendar.YEAR)

        DatePickerDialog(this, R.style.CustomCalendarDialog,
            { _, year, month, day ->
                val selectedYear = year
                val selectedMonth = month + 1
                val selectedDay = day

                val selectedDayStr = if(selectedDay < 10) "0$selectedDay" else "$selectedDay"
                val selectedMonthStr = if(selectedMonth < 10) "0$selectedMonth" else "$selectedMonth"
                val selectedYearStr = "$selectedYear"

                view.text = "$selectedDayStr/$selectedMonthStr/$selectedYearStr"
            },
            oldYear, oldMonth, oldDay
        ).show()
    }


    private fun btnSaveOnClickListener(id: Int){
        val title = txtEditTitle?.text.toString()
        val startDateStr = txtStartDate?.text.toString()
        val endDateStr = txtEndDate?.text.toString()

        if(startDateStr != "" && endDateStr != ""){
            val daysCount: Int
            val dbHelper = DatabaseHelper(this)
            if(Build.VERSION.SDK_INT >= 26){
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val startDateLocal = LocalDate.parse(startDateStr, formatter)
                val endDate = LocalDate.parse(endDateStr, formatter)
                daysCount = ChronoUnit.DAYS.between(startDateLocal, endDate).toInt() + 1
                val startDate = Date.from(startDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant())
                dbHelper.updateFreeDay(id, title, startDate, daysCount)
            } else{
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val startDate = formatter.parse(startDateStr)
                val endDate = formatter.parse(endDateStr)
                daysCount = ((endDate.time - startDate.time)/(1000 * 60 * 60 * 24)).toInt() + 1
                dbHelper.updateFreeDay(id, title, startDate, daysCount)
            }

            this.finish()
        } else{
            Toast.makeText(this, "Λάθος Πληροφορίες", Toast.LENGTH_SHORT).show()
        }
    }


    private fun btnDeleteOnClickListener(id: Int){
        val dbHelper = DatabaseHelper(this)
        dbHelper.deleteFreeDay(id)
        Toast.makeText(this, "Η άδεια διαγράφηκε", Toast.LENGTH_SHORT).show()
    }
}