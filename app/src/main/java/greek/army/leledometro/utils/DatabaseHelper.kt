package greek.army.leledometro.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.database.getIntOrNull
import java.text.SimpleDateFormat
import java.util.Date

class DatabaseHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object{
        private val DATABASE_NAME = "leledometro"
        private val DATABASE_VERSION = 1
    }

    //user table value
    private val USER_TABLE_NAME = "user_info"
    private val USER_COL1 = "id"            //INTEGER
    private val USER_COL2 = "name"          //TEXT
    private val USER_COL3 = "start_date"    //DATE
    private val USER_COL4 = "end_date"      //DATE
    private val USER_COL5 = "esso"          //TEXT
    private val USER_COL6 = "grade"         //TEXT
    private val USER_COL7 = "force"         //TEXT
    private val USER_COL8 = "camp"          //TEXT
    private val USER_COL9 = "number"        //TEXT
    private val USER_COL10 = "photo"        //BLOB



    //service table values
    private val SERVICES_TABLE_NAME = "service"
    private val SERVICES_COL1 = "id"        //INTEGER
    private val SERVICES_COL2 = "type"      //TEXT
    private val SERVICES_COL3 = "number"    //TEXT
    private val SERVICES_COL4 = "date"      //TEXT


    //free_day table values
    private val FREE_DAYS_TABLE_NAME = "free_day"
    private val FREE_DAYS_COL1 = "id"           //INTEGER
    private val FREE_DAYS_COL2 = "title"        //TEXT
    private val FREE_DAYS_COL3 = "start_date"   //DATE
    private val FREE_DAYS_COL4 = "days_count"   //INTEGER


    //penalty table values
    private val PENALTY_TABLE_NAME = "penalty"
    private val PENALTY_COL1 = "id"             //INTEGER
    private val PENALTY_COL2 = "type"           //TEXT
    private val PENALTY_COL3 = "title"          //TEXT
    private val PENALTY_COL4 = "days"           //INTEGER



    override fun onCreate(db: SQLiteDatabase?) {
        val sql1 = "CREATE TABLE IF NOT EXISTS $USER_TABLE_NAME (" +
                   "$USER_COL1 INTEGER PRIMARY KEY," +
                   "$USER_COL2 TEXT," +
                   "$USER_COL3 TEXT," +
                   "$USER_COL4 TEXT," +
                   "$USER_COL5 TEXT," +
                   "$USER_COL6 TEXT," +
                   "$USER_COL7 TEXT," +
                   "$USER_COL8 TEXT," +
                   "$USER_COL9 TEXT," +
                   "$USER_COL10 BLOB" +
                   ")"
        db?.execSQL(sql1)

        val sql2 = "CREATE TABLE IF NOT EXISTS $SERVICES_TABLE_NAME(" +
                   "$SERVICES_COL1 INTEGER PRIMARY KEY AUTOINCREMENT," +
                   "$SERVICES_COL2 TEXT NOT NULL," +
                   "$SERVICES_COL3 TEXT NOT NULL," +
                   "$SERVICES_COL4 DATE NOT NULL" +
                   ")"
        db?.execSQL(sql2)

        val sql3 = "CREATE TABLE IF NOT EXISTS $FREE_DAYS_TABLE_NAME(" +
                   "$FREE_DAYS_COL1 INTEGER PRIMARY KEY AUTOINCREMENT," +
                   "$FREE_DAYS_COL2 TEXT," +
                   "$FREE_DAYS_COL3 DATE NOT NULL," +
                   "$FREE_DAYS_COL4 INTEGER NOT NULL" +
                   ")"
        db?.execSQL(sql3)

        val sql4 = "CREATE TABLE IF NOT EXISTS $PENALTY_TABLE_NAME(" +
                   "$PENALTY_COL1 INTEGER PRIMARY KEY AUTOINCREMENT," +
                   "$PENALTY_COL2 TEXT NOT NULL," +
                   "$PENALTY_COL3 TEXT," +
                   "$PENALTY_COL4 INTEGER NOT NULL" +
                   ")"
        db?.execSQL(sql4)

    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $USER_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $SERVICES_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $FREE_DAYS_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $PENALTY_TABLE_NAME")
        onCreate(db)
    }


    fun getInfo(): Info?{
        val db: SQLiteDatabase = this.writableDatabase
        val query = "SELECT * FROM $USER_TABLE_NAME"
        val cursor: Cursor = db.rawQuery(query, null)

        if(cursor.moveToFirst()){
            val id: Int = cursor.getInt(cursor.getColumnIndexOrThrow(USER_COL1))
            val name: String = cursor.getString(cursor.getColumnIndexOrThrow(USER_COL2))
            val startDate: String = cursor.getString(cursor.getColumnIndexOrThrow(USER_COL3))
            val endDate: String = cursor.getString(cursor.getColumnIndexOrThrow(USER_COL4))
            val esso: String = cursor.getString(cursor.getColumnIndexOrThrow(USER_COL5))
            val grade: String = cursor.getString(cursor.getColumnIndexOrThrow(USER_COL6))
            val force: String = cursor.getString(cursor.getColumnIndexOrThrow(USER_COL7))
            val camp: String = cursor.getString(cursor.getColumnIndexOrThrow(USER_COL8))
            val number: String = cursor.getString(cursor.getColumnIndexOrThrow(USER_COL9))
            val photo: ByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(USER_COL10))
            return Info(id, name, startDate, endDate, esso, grade, force, camp, number, photo)
        }

        cursor.close()
        db.close()
        return null
    }


    fun updateInfo(name: String, startDate: String, endDate: String, esso: String, grade: String, force: String, camp: String, number: String, photo: ByteArray){
        val db: SQLiteDatabase = this.writableDatabase
        db.delete(USER_TABLE_NAME, null, null)

        val values = ContentValues().apply {
            put(USER_COL2, name)
            put(USER_COL3, startDate)
            put(USER_COL4, endDate)
            put(USER_COL5, esso)
            put(USER_COL6, grade)
            put(USER_COL7, force)
            put(USER_COL8, camp)
            put(USER_COL9, number)
            put(USER_COL10, photo)
        }
        db.insert(USER_TABLE_NAME, null, values)
        db.close()
    }


    fun getAllServices(): ArrayList<Service>{
        val db: SQLiteDatabase = this.writableDatabase
        val query: String = "SELECT * FROM $SERVICES_TABLE_NAME ORDER BY $SERVICES_COL4 DESC"
        val cursor: Cursor = db.rawQuery(query, null)
        val services = arrayListOf<Service>()
        if(cursor.moveToFirst()){
            do {
                val id: Int = cursor.getInt(cursor.getColumnIndexOrThrow(SERVICES_COL1))
                val type: String = cursor.getString(cursor.getColumnIndexOrThrow(SERVICES_COL2))
                val number: String = cursor.getString(cursor.getColumnIndexOrThrow(SERVICES_COL3))
                val dateStr: String = cursor.getString(cursor.getColumnIndexOrThrow(SERVICES_COL4))

                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val date = formatter.parse(dateStr)
                services.add(Service(id, type, number, date))

            } while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return services
    }


    fun getServiceById(id: Int): Service?{
        val db: SQLiteDatabase = this.writableDatabase
        val query = "SELECT * FROM $SERVICES_TABLE_NAME WHERE id = $id"
        val cursor: Cursor = db.rawQuery(query, null)
        if(cursor.moveToFirst()) {
            val id: Int = cursor.getInt(cursor.getColumnIndexOrThrow(SERVICES_COL1))
            val type: String = cursor.getString(cursor.getColumnIndexOrThrow(SERVICES_COL2))
            val number: String = cursor.getString(cursor.getColumnIndexOrThrow(SERVICES_COL3))
            val dateStr: String = cursor.getString(cursor.getColumnIndexOrThrow(SERVICES_COL4))

            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date = formatter.parse(dateStr)
            cursor.close()
            return Service(id, type, number, date)
        }
        cursor.close()
        db.close()
        return null
    }


    fun getServicesCount(): Int{
        val db: SQLiteDatabase = this.writableDatabase
        val query: String = "SELECT COUNT(*) FROM $SERVICES_TABLE_NAME"
        val cursor: Cursor = db.rawQuery(query, null)
        var count = 0
        if(cursor.moveToFirst()){
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count
    }


    fun getAllFreeDays(): ArrayList<FreeDay>{
        val db: SQLiteDatabase = this.writableDatabase
        val query = "SELECT * FROM $FREE_DAYS_TABLE_NAME ORDER BY $FREE_DAYS_COL3 DESC"
        val cursor: Cursor = db.rawQuery(query, null)
        val freeDays = arrayListOf<FreeDay>()
        if(cursor != null && cursor.moveToFirst()){
            do {
                val id: Int = cursor.getInt(cursor.getColumnIndexOrThrow(FREE_DAYS_COL1))
                val title: String = cursor.getString(cursor.getColumnIndexOrThrow(FREE_DAYS_COL2))
                val startDateStr: String = cursor.getString(cursor.getColumnIndexOrThrow(FREE_DAYS_COL3))
                val daysCount: Int = cursor.getInt(cursor.getColumnIndexOrThrow(FREE_DAYS_COL4))

                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val startDate = formatter.parse(startDateStr)

                val freeDay = FreeDay(id, title, startDate, daysCount)
                freeDays.add(freeDay)

            } while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return freeDays
    }


    fun getFreeDaysCount(): Int {
        val db: SQLiteDatabase = this.writableDatabase
        val query: String = "SELECT SUM($FREE_DAYS_COL4) FROM $FREE_DAYS_TABLE_NAME"
        val cursor: Cursor = db.rawQuery(query, null)
        val daysCount = if (cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        db.close()
        return daysCount
    }


    fun getFreeDayById(id: Int): FreeDay?{
        val db: SQLiteDatabase = this.writableDatabase
        val query = "SELECT * FROM $FREE_DAYS_TABLE_NAME WHERE id = $id"
        val cursor: Cursor = db.rawQuery(query, null)

        if(cursor.moveToFirst()){
            val id: Int = cursor.getInt(cursor.getColumnIndexOrThrow(FREE_DAYS_COL1))
            val title: String = cursor.getString(cursor.getColumnIndexOrThrow(FREE_DAYS_COL2))
            val startDateStr: String = cursor.getString(cursor.getColumnIndexOrThrow(FREE_DAYS_COL3))
            val daysCount: Int = cursor.getInt(cursor.getColumnIndexOrThrow(FREE_DAYS_COL4))

            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val startDate = formatter.parse(startDateStr)
            cursor.close()
            return FreeDay(id, title, startDate, daysCount)
        }
        cursor.close()
        db.close()
        return null
    }


    fun getAllPenalties(): ArrayList<Penalty>{
        val db: SQLiteDatabase = this.writableDatabase
        val query = "SELECT * FROM $PENALTY_TABLE_NAME"
        val cursor: Cursor = db.rawQuery(query, null)
        val penalties = arrayListOf<Penalty>()

        if(cursor.moveToFirst()){
            do{
                val id: Int = cursor.getInt(cursor.getColumnIndexOrThrow(PENALTY_COL1))
                val type: String = cursor.getString(cursor.getColumnIndexOrThrow(PENALTY_COL2))
                val title: String = cursor.getString(cursor.getColumnIndexOrThrow(PENALTY_COL3))
                val days: Int = cursor.getInt(cursor.getColumnIndexOrThrow(PENALTY_COL4))
                penalties.add(Penalty(id, type, title, days))
            } while(cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return penalties
    }


    fun getPenaltiesCount(): Int{
        val db: SQLiteDatabase = this.writableDatabase
        val query = "SELECT SUM($PENALTY_COL4) FROM $PENALTY_TABLE_NAME"
        val cursor: Cursor = db.rawQuery(query, null)
        var count = if(cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        db.close()
        return count
    }


    fun getPenaltyById(id: Int): Penalty?{
        val db: SQLiteDatabase = this.writableDatabase
        val query = "SELECT * FROM $PENALTY_TABLE_NAME WHERE id = $id"
        val cursor: Cursor = db.rawQuery(query, null)
        if(cursor.moveToFirst()){
            val id: Int = cursor.getInt(cursor.getColumnIndexOrThrow(PENALTY_COL1))
            val type: String = cursor.getString(cursor.getColumnIndexOrThrow(PENALTY_COL2))
            val title: String = cursor.getString(cursor.getColumnIndexOrThrow(PENALTY_COL3))
            val days: Int = cursor.getInt(cursor.getColumnIndexOrThrow(PENALTY_COL4))

            cursor.close()
            db.close()
            return Penalty(id, type, title, days)
        }
        cursor.close()
        db.close()
        return null
    }


    fun insertService(type: String, number: String, date: Date): Long{
        val db: SQLiteDatabase = this.writableDatabase
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val dateStr = formatter.format(date)
        val values = ContentValues()
        values.put(SERVICES_COL2, type)
        values.put(SERVICES_COL3, number)
        values.put(SERVICES_COL4, dateStr)
        val newRowId: Long = db.insert(SERVICES_TABLE_NAME, null, values)
        db.close()
        return newRowId
    }


    fun insertFreeDay(title: String, startDate: Date, daysCount: Int): Long{
        val db: SQLiteDatabase = this.writableDatabase
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val startDateStr = formatter.format(startDate)
        val values = ContentValues()
        values.put(FREE_DAYS_COL2, title)
        values.put(FREE_DAYS_COL3, startDateStr)
        values.put(FREE_DAYS_COL4, daysCount)
        val newRowId: Long = db.insert(FREE_DAYS_TABLE_NAME, null, values)
        db.close()
        Log.d("database insertion", "inserted free day with id $newRowId")
        return newRowId
    }


    fun insertPenalty(type: String, title: String, days: Int): Long{
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(PENALTY_COL2, type)
        values.put(PENALTY_COL3, title)
        values.put(PENALTY_COL4, days)
        val newRowId: Long = db.insert(PENALTY_TABLE_NAME, null, values)
        db.close()
        return newRowId
    }


    fun deleteService(id: Int): Int{
        val db: SQLiteDatabase = this.writableDatabase
        val whereClause: String = "id = ?"
        val whereArgs = arrayOf("$id")

        val rowsDeleted: Int = db.delete(SERVICES_TABLE_NAME, whereClause, whereArgs)
        db.close()
        return rowsDeleted
    }


    fun deleteFreeDay(id: Int): Int{
        val db: SQLiteDatabase = this.writableDatabase
        val whereClause = "id = ?"
        val whereArgs = arrayOf("$id")

        val rowsDeleted: Int = db.delete(FREE_DAYS_TABLE_NAME, whereClause, whereArgs)
        db.close()
        return rowsDeleted
    }


    fun deletePenalty(id: Int): Int{
        val db: SQLiteDatabase = this.writableDatabase
        val whereClause = "id = ?"
        val whereArgs = arrayOf("$id")

        val rowsDeleted: Int = db.delete(PENALTY_TABLE_NAME, whereClause, whereArgs)
        db.close()
        return rowsDeleted
    }


    fun updateService(id: Int, type: String, number: String, date: Date): Int{
        val db: SQLiteDatabase = this.writableDatabase
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val dateStr = formatter.format(date)
        val values = ContentValues().apply {
            put(SERVICES_COL2, type)
            put(SERVICES_COL3, number)
            put(SERVICES_COL4, dateStr)
        }

        val selection = "id = ?"
        val selectionArgs = arrayOf("$id")

        val rowsUpdated = db.update(SERVICES_TABLE_NAME, values, selection, selectionArgs)
        db.close()
        return rowsUpdated
    }


    fun updateFreeDay(id: Int, title: String, startDate: Date, daysCount: Int): Int{
        val db: SQLiteDatabase = this.writableDatabase
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val startDateStr = formatter.format(startDate)
        val values = ContentValues().apply {
            put(FREE_DAYS_COL2, title)
            put(FREE_DAYS_COL3, startDateStr)
            put(FREE_DAYS_COL4, daysCount)
        }

        val selection = "id = ?"
        val selectionArgs = arrayOf("$id")

        val rowsUpdated = db.update(FREE_DAYS_TABLE_NAME, values, selection, selectionArgs)
        db.close()
        return rowsUpdated
    }


    fun updatePenalty(id: Int, type: String, title: String, days: Int): Int{
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues().apply {
            put(PENALTY_COL2, type)
            put(PENALTY_COL3, title)
            put(PENALTY_COL4, days)
        }

        val selection = "id = ?"
        val selectionArgs = arrayOf("$id")

        val rowsUpdated = db.update(PENALTY_TABLE_NAME, values, selection, selectionArgs)
        db.close()
        return rowsUpdated
    }


    fun clearAllData(){
        val db: SQLiteDatabase = this.writableDatabase
        db.delete(SERVICES_TABLE_NAME, null, null)
        db.delete(FREE_DAYS_TABLE_NAME, null, null)
        db.delete(PENALTY_TABLE_NAME, null, null)
        db.close()
    }


    fun clearServiceTable(){

    }


    fun clearFreeDaysTable(){

    }


    fun clearPenaltyTable(){

    }
}