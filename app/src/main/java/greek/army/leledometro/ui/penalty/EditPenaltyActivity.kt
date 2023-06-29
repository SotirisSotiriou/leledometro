package greek.army.leledometro.ui.penalty

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import greek.army.leledometro.R
import greek.army.leledometro.utils.DatabaseHelper
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditPenaltyActivity : AppCompatActivity() {

    private val types = arrayOf("--", "Κράτηση", "Φυλακή")

    private var btnBack: ImageButton? = null
    private var btnSave: ImageButton? = null
    private var btnDelete: ImageButton? = null

    private var spinnerPenaltyType: Spinner? = null
    private var txtEditTitle: EditText? = null
    private var txtEditDays: EditText? = null

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_penalty)

        supportActionBar?.hide()

        val dbHelper = DatabaseHelper(this)
        val id = intent.getIntExtra("id", -1)
        if(id == -1) this.finish()

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

        val penalty = dbHelper.getPenaltyById(id)

        penalty.let {
            val selectedTypeIndex = types.indexOf(it?.type)

            spinnerPenaltyType = findViewById(R.id.spinner_penalty_type)
            val penaltyTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
            penaltyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPenaltyType?.adapter = penaltyTypeAdapter
            spinnerPenaltyType?.setSelection(selectedTypeIndex)

            txtEditTitle = findViewById(R.id.txt_edit_title)
            txtEditTitle?.text = Editable.Factory.getInstance().newEditable(it?.title)


        }
    }


    private fun btnSaveOnClickListener(id: Int){
        val dbHelper = DatabaseHelper(this)
        val type = spinnerPenaltyType?.selectedItem.toString()
        val title = txtEditTitle?.text.toString()
        val days = Integer.parseInt(txtEditDays?.text.toString())

        if(days > 0 && type != "--"){
            val daysCount: Int
            val dbHelper = DatabaseHelper(this)
            dbHelper.updatePenalty(id, type, title, days)
            this.finish()
        } else{
            Toast.makeText(this, "Λάθος Πληροφορίες", Toast.LENGTH_SHORT).show()
        }
    }


    private fun btnDeleteOnClickListener(id: Int){
        val dbHelper = DatabaseHelper(this)
        dbHelper.deletePenalty(id)
    }
}