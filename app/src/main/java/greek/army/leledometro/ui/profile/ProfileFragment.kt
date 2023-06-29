package greek.army.leledometro.ui.profile

import android.R
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import greek.army.leledometro.databinding.FragmentProfileBinding
import greek.army.leledometro.utils.DatabaseHelper
import java.util.Calendar


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val paths = arrayOf("--", "Στρατός Ξηράς", "Πολεμικό Ναυτικό", "Πολεμική Αεροπορία")

    private val grades = arrayOf("--", "Ψάρακας", "Στρατιώτης", "Υποδεκανέας", "Δεκανέας", "Δεκανέας ΟΒΑ", "Δεκανέας ΕΠΟΠ",
                                "Λοχίας Έφεδρος", "Λοχίας ΕΠΟΠ", "Λοχίας ΣΜΥ", "Επιλοχίας ΕΠΟΠ", "Επιλοχίας ΣΜΥ", "Αρχιλοχίας ΕΠΟΠ",
                                "Αρχιλοχίας ΣΜΥ", "Ανθυπασπιστής", "Δ.Ε.Α", "Ανθυπολοχαγός", "Υπολοχαγός", "Λοχαγός", "Ταγματάρχης",
                                "Αντισυνταγματάρχης", "Συνταγματάρχης", "Ταξίαρχος", "Υποστράτηγος", "Αντιστράτηγος", "Στρατηγός")

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var spinnerEditForce: Spinner? = null
    private var spinnerEditGrade: Spinner? = null
    private var btnEditStartDate: Button? = null
    private var btnEditEndDate: Button? = null
    private var btnClear: Button? = null
    private var btnClearStartDate: ImageButton? = null
    private var btnClearEndDate: ImageButton? = null
    private var btnSave: Button? = null
    private var imgProfilePhoto: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        imgProfilePhoto = binding.imgProfilePhoto
        imgProfilePhoto?.setOnClickListener{

        }

        spinnerEditForce = binding.spinnerEditForce
        val adapterForce = ArrayAdapter(context as Context,R.layout.simple_spinner_dropdown_item,paths)
        adapterForce.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinnerEditForce?.adapter = adapterForce

        btnEditStartDate = binding.btnEditStartDate
        btnEditStartDate?.setOnClickListener{
            btnEditStartDateOnClickListener()
        }

        btnEditEndDate = binding.btnEditEndDate
        btnEditEndDate?.setOnClickListener {
            btnEditEndDateOnClickListener()
        }

        spinnerEditGrade = binding.spinnerEditGrade
        val adapterGrade = ArrayAdapter(context as Context, R.layout.simple_spinner_dropdown_item, grades)
        adapterGrade.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinnerEditGrade?.adapter = adapterGrade

        btnClear = binding.btnClear
        btnClear?.setOnClickListener {
            btnClearOnClickListener()
        }

        btnClearStartDate = binding.btnClearStartDate
        btnClearStartDate?.setOnClickListener {
            btnClearStartDateOnClickListener()
        }

        btnClearEndDate = binding.btnClearEndDate
        btnClearEndDate?.setOnClickListener {
            btnClearEndDateOnClickListener()
        }

        btnSave = binding.btnSave
        btnSave?.setOnClickListener {
            btnSaveOnClickListener()
        }

        initializeFields()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onResume() {
        super.onResume()
        initializeFields()
    }


    private fun initializeFields(){
        val txtEditName: TextInputEditText = binding.txtEditName
        val txtStartDate: TextView = binding.txtStartDate
        val txtEndDate: TextView = binding.txtEndDate
        val txtEditEsso: TextInputEditText = binding.txtEditEsso
        val spinnerEditGrade: Spinner = binding.spinnerEditGrade
        val spinnerEditForce: Spinner = binding.spinnerEditForce
        val txtEditCamp: TextInputEditText = binding.txtEditCamp
        val txtEditNumber: TextInputEditText = binding.txtEditNumber

        val dbHelper = DatabaseHelper(requireContext())
        val info = dbHelper.getInfo()

        info.let {
            txtEditName.setText(it?.name)
            txtStartDate.text = it?.startDate
            txtEndDate.text = it?.endDate
            txtEditEsso.setText(it?.esso)

            var editGradeIndex: Int = -1
            if(it?.grade != ""){
                editGradeIndex = grades.indexOf(it?.grade)
            }
            if(editGradeIndex != -1){
                spinnerEditGrade.setSelection(editGradeIndex)
            } else{
                spinnerEditGrade.setSelection(0)
            }

            var editForceIndex: Int = -1
            if(it?.force != ""){
                editForceIndex = paths.indexOf(it?.force)
            }
            if(editForceIndex != -1){
                spinnerEditForce.setSelection(editForceIndex)
            } else{
                spinnerEditForce.setSelection(0)
            }

            txtEditCamp.setText(if(it?.camp != "") it?.camp else "")
            txtEditNumber.setText(if(it?.number != "") it?.number else "")
        }
    }

    private fun btnEditStartDateOnClickListener(){
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context as Context, greek.army.leledometro.R.style.CustomCalendarDialog,
            { _, year, month, day ->
                val selectedYear = year
                val selectedMonth = month + 1
                val selectedDay = day

                val selectedDayStr: String = if (selectedDay < 10) "0$selectedDay" else "$selectedDay"
                val selectedMonthStr: String = if (selectedMonth < 10) "0$selectedMonth" else "$selectedMonth"
                val selectedYearStr: String = "$selectedYear"

                val txtStartDate: TextView = binding.txtStartDate
                txtStartDate.text = "$selectedDayStr/$selectedMonthStr/$selectedYearStr"
            },
            currentYear, currentMonth, currentDay
        ).show()


    }


    private fun btnEditEndDateOnClickListener(){
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context as Context, activity?.applicationContext?.resources!!.getIdentifier("CustomCalendarDialog", "style", activity?.packageName),
            { _, year, month, day ->
                val selectedYear = year
                val selectedMonth = month + 1
                val selectedDay = day

                val selectedDayStr: String = if (selectedDay < 10) "0$selectedDay" else "$selectedDay"
                val selectedMonthStr: String = if (selectedMonth < 10) "0$selectedMonth" else "$selectedMonth"
                val selectedYearStr: String = "$selectedYear"

                val txtEndDate: TextView = binding.txtEndDate
                txtEndDate.text = "$selectedDayStr/$selectedMonthStr/$selectedYearStr"
            },
            currentYear, currentMonth, currentDay
        ).show()


    }

    private fun btnClearOnClickListener() {
        val txtEditName: TextView = binding.txtEditName
        val txtStartDate: TextView = binding.txtStartDate
        val txtEndDate: TextView = binding.txtEndDate
        val txtEditEsso: TextView = binding.txtEditEsso
        val txtEditCamp: TextView = binding.txtEditCamp
        val txtEditNumber: TextView = binding.txtEditNumber
        val spinnerEditForce: Spinner = binding.spinnerEditForce
        val spinnerEditGrade: Spinner = binding.spinnerEditGrade

        txtEditName.text = ""
        txtStartDate.text = resources.getString(activity?.applicationContext?.resources!!.getIdentifier(
            "default_start_date_value",
            "string",
            activity?.packageName
        ))
        txtEndDate.text = resources.getString(activity?.applicationContext?.resources!!.getIdentifier(
            "default_end_date_value",
            "string",
            activity?.packageName
        ))
        txtEditEsso.text = ""
        txtEditCamp.text = ""
        txtEditNumber.text = ""
        spinnerEditForce.setSelection(0)
        spinnerEditGrade.setSelection(0)
    }

    private fun btnClearStartDateOnClickListener(){
        val txtStartDate: TextView = binding.txtStartDate
        txtStartDate.text = getString(greek.army.leledometro.R.string.default_start_date_value)
    }

    private fun btnClearEndDateOnClickListener(){
        val txtEndDate: TextView = binding.txtEndDate
        txtEndDate.text = getString(greek.army.leledometro.R.string.default_end_date_value)
    }

    private fun btnSaveOnClickListener(){
        val name: String = binding.txtEditName.text.toString()
        val startDate: String = if(binding.txtStartDate.text.toString() == getString(greek.army.leledometro.R.string.default_start_date_value)) "" else binding.txtStartDate.text.toString()
        val endDate: String = if(binding.txtEndDate.text.toString() == getString(greek.army.leledometro.R.string.default_end_date_value)) "" else binding.txtEndDate.text.toString()
        val esso: String = binding.txtEditEsso.text.toString()
        val camp: String = binding.txtEditCamp.text.toString()
        val force: String = if(binding.spinnerEditForce.selectedItem.toString() == "--") "" else binding.spinnerEditForce.selectedItem.toString()
        val grade: String = if(binding.spinnerEditGrade.selectedItem.toString() == "--") "" else binding.spinnerEditGrade.selectedItem.toString()
        val number: String = binding.txtEditNumber.text.toString()
        val photo = byteArrayOf()

        val dbHelper = DatabaseHelper(requireContext())
        dbHelper.updateInfo(name, startDate, endDate, esso, grade, force, camp, number, photo)

        Toast.makeText(requireContext(), "Τα δεδομένα αποθηκεύτηκαν με επιτυχία", Toast.LENGTH_SHORT).show()
    }
}