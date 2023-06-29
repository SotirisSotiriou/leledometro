package greek.army.leledometro.ui.penalty

import android.content.Intent
import android.graphics.Typeface
import android.media.Image
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
import greek.army.leledometro.ui.freeday.EditFreeDayActivity
import greek.army.leledometro.utils.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PenaltyListActivity : AppCompatActivity() {

    private var btnBack: ImageButton? = null
    private var btnAdd: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penalty_list)

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
        val intent = Intent(this, AddPenaltyActivity::class.java)
        startActivity(intent)
    }


    private fun destroyAllListElements(){
        val linearLayout: LinearLayout = findViewById(R.id.linearLayout)
        linearLayout.removeAllViewsInLayout()
    }


    private fun createViewList(){
        val dbHelper = DatabaseHelper(this)
        val penalties = dbHelper.getAllPenalties()

        val linearLayout: LinearLayout = findViewById(R.id.linearLayout)
        var counter = 0

        val dpPadding = 20
        val pxPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpPadding.toFloat(),
            resources.displayMetrics
        ).toInt()

        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        while(counter < penalties.size){
            val view = TextView(this)
            view.id = View.generateViewId()

            view.text = penalties[counter].type
                .plus(" - Μέρες: ")
                .plus(penalties[counter].days)

            view.textSize = 20F
            view.setTypeface(null, Typeface.BOLD)
            view.tag = penalties[counter].id.toString()

            view.setPadding(pxPadding)

            view.setOnClickListener{
                val intent = Intent(this, EditPenaltyActivity::class.java)
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