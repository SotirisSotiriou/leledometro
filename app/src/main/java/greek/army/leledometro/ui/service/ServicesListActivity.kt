package greek.army.leledometro.ui.service

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class ServicesListActivity : AppCompatActivity() {

    private var btnBack: ImageButton? = null
    private var btnAdd: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services_list)

        supportActionBar?.hide()

        btnBack = findViewById(R.id.btn_back)
        btnBack?.setOnClickListener {
            this.finish()
        }

        btnAdd = findViewById(R.id.btn_add)
        btnAdd?.setOnClickListener {
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
        destroyAllListElements()
        createViewList()
    }


    private fun btnAddOnClickListener(){
        val intent = Intent(this, AddServiceActivity::class.java)
        startActivity(intent)
    }


    private fun destroyAllListElements(){
        val linearLayout: LinearLayout = findViewById(R.id.linearLayout)
        linearLayout.removeAllViewsInLayout()
    }


    private fun createViewList(){
        val dbHelper = DatabaseHelper(this)
        val services = dbHelper.getAllServices()

        val linearLayout: LinearLayout = findViewById(R.id.linearLayout)
        var counter = 0

        val dpPadding = 20
        val pxPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpPadding.toFloat(),
            resources.displayMetrics
        ).toInt()

        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        while(counter < services.size){
            val view = TextView(this)
            view.id = View.generateViewId()

            val dateStr = formatter.format(services[counter].date)

            view.text = services[counter].type
                        .plus(" - ")
                        .plus(services[counter].number)
                        .plus("\n(")
                        .plus(dateStr)
                        .plus(")")

            view.textSize = 20F
            view.setTypeface(null, Typeface.BOLD)
            view.tag = services[counter].id

            view.setPadding(pxPadding)

            view.setOnClickListener {
                val intent = Intent(this, EditServiceActivity::class.java)
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