package greek.army.leledometro.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import greek.army.leledometro.databinding.FragmentCalendarBinding
import greek.army.leledometro.databinding.FragmentHomeBinding
import greek.army.leledometro.ui.service.AddServiceActivity
import greek.army.leledometro.ui.freeday.AddFreeDayActivity
import greek.army.leledometro.ui.freeday.FreeDayListActivity
import greek.army.leledometro.ui.penalty.AddPenaltyActivity
import greek.army.leledometro.ui.penalty.PenaltyListActivity
import greek.army.leledometro.ui.service.ServicesListActivity
import greek.army.leledometro.utils.DatabaseHelper
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnAddService.setOnClickListener {
            val intent = Intent(this.activity, AddServiceActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddFreeDay.setOnClickListener {
            val intent = Intent(this.activity, AddFreeDayActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddPenalty.setOnClickListener {
            val intent = Intent(this.activity, AddPenaltyActivity::class.java)
            startActivity(intent)
        }

        binding.viewFreeDays.setOnClickListener {
            val intent = Intent(this.activity, FreeDayListActivity::class.java)
            startActivity(intent)
        }

        binding.viewServices.setOnClickListener {
            val intent = Intent(this.activity, ServicesListActivity::class.java)
            startActivity(intent)
        }

        binding.viewPenalties.setOnClickListener {
            val intent = Intent(this.activity, PenaltyListActivity::class.java)
            startActivity(intent)
        }

        initializeFields()
    }


    override fun onResume() {
        super.onResume()
        initializeFields()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initializeFields(){
        val dbHelper = DatabaseHelper(requireContext())

        val info = dbHelper.getInfo()
        val penaltyDays = dbHelper.getPenaltiesCount()

        info.let {
            if (it?.name != "") binding.txtName.text = it?.name

            if (it?.startDate != "" && it?.endDate != ""){
                var days: Long
                if(Build.VERSION.SDK_INT >= 26){
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val today = LocalDate.now()
                    val endDateObj = LocalDate.parse(it?.endDate, formatter)
                    days = ChronoUnit.DAYS.between(today, endDateObj)
                } else{
                    val formatter = SimpleDateFormat("dd/MM/yyyy")
                    val today = Date()
                    val endDateObj = it?.endDate?.let { it1 -> formatter.parse(it1) }
                    days = (endDateObj?.time?.minus(today.time))?.div((1000 * 60 * 60 * 24)) ?: 0
                }
                days += penaltyDays
                binding.txtDays.text = days.toString()
            }

            if (it?.camp != "") binding.txtCamp.text = it?.camp

            if (it?.esso != "") binding.txtEsso.text = it?.esso

            if (it?.grade != "") binding.txtGrade.text = it?.grade


        }

        val serviceCount = dbHelper.getServicesCount()
        val freeDayCount = dbHelper.getFreeDaysCount()

        binding.txtFreeDays.text = freeDayCount.toString()
        binding.txtServices.text = serviceCount.toString()
        binding.txtPenalties.text = penaltyDays.toString()
    }
}