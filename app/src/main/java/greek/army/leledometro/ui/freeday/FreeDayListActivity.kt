package greek.army.leledometro.ui.freeday

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import greek.army.leledometro.R
import greek.army.leledometro.utils.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FreeDayListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_day_list)

        supportActionBar?.hide()

        val btnBack: ImageButton = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            this.finish()
        }


        val btnAdd: ImageButton = findViewById(R.id.btn_add)
        btnAdd.setOnClickListener {
            btnAddOnClickListener()
        }

        createViewList()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.finish()
    }

    override fun onResume() {
        super.onResume()
        destroyListElements()
        createViewList()
    }


    private fun btnAddOnClickListener(){
        val intent = Intent(this, AddFreeDayActivity::class.java)
        startActivity(intent)
    }


    private fun destroyListElements(){
        val linearLayout: LinearLayout = findViewById(R.id.linearLayout)
        linearLayout.removeAllViewsInLayout()
    }


    private fun createViewList(){
        val dbHelper = DatabaseHelper(this)
        val freeDays = dbHelper.getAllFreeDays()

        val linearLayout: LinearLayout = findViewById(R.id.linearLayout)
        var counter = 0

        val dpPadding = 20
        val pxPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpPadding.toFloat(),
            resources.displayMetrics
        ).toInt()

        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        while(counter < freeDays.size){
            val view = TextView(this)
            view.id = View.generateViewId()
            val calendar = Calendar.getInstance()
            calendar.time = freeDays[counter].startDate
            calendar.add(Calendar.DAY_OF_YEAR, freeDays[counter].daysCount)
            val endDate = calendar.time
            val startDateStr = formatter.format(freeDays[counter].startDate)
            val endDateStr = formatter.format(endDate)
            if(title != "") view.text = freeDays[counter].title.plus("\n")
            view.text = view.text.toString()
                        .plus("(")
                        .plus(startDateStr)
                        .plus(" - ")
                        .plus(endDateStr)
                        .plus(")")

            view.textSize = 20F
            view.setTypeface(null, Typeface.BOLD)
            view.tag = freeDays[counter].id.toString()

            view.setPadding(pxPadding)

            view.setOnClickListener{
                val intent = Intent(this, EditFreeDayActivity::class.java)
                val id = Integer.parseInt(it.tag.toString())
                intent.putExtra("id", id)
                startActivity(intent)
            }

            linearLayout.addView(view)

            val divider = View(this)
            divider.id = View.generateViewId()

            val attrs = intArrayOf(android.R.attr.listDivider)
            val typedArray = this.obtainStyledAttributes(attrs)
            val drawableResourceId = typedArray.getResourceId(0,0)

            divider.background = ContextCompat.getDrawable(this, drawableResourceId)

            linearLayout.addView(divider)

            counter += 1
        }
    }

}