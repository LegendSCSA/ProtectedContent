package com.shafie.protectedcontent

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import androidx.core.content.contentValuesOf
import com.shafie.protectedcontent.Constants.AUTHORITY


class DataProvider : ContentProvider() {

    private var dbHelper: DBHelper? = null
    private val DATABASE_NAME = "table.db"
    private val DATABASE_VERSION = 1
    private val DATUM_TABLE_NAME = "t1"
    private var sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private val DATUM = 1
    private val DATUM_ID = 2
    private var projMap = mutableMapOf<String,String>()


    // Define the URI for the Content Provider

    init {
        sUriMatcher.addURI(AUTHORITY, DATUM_TABLE_NAME, DATUM)
        sUriMatcher.addURI(AUTHORITY, DATUM_TABLE_NAME + "/#", DATUM_ID)
        projMap.put(Constants.ID, Constants.ID)
        projMap.put(Constants.TEXT, Constants.TEXT)
    }

    override fun onCreate(): Boolean {
        // Initialize and Create the DB
        dbHelper = DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val qb = SQLiteQueryBuilder()
        qb.tables = DATUM_TABLE_NAME
        qb.setProjectionMap(projMap) //
        var s1 = selection
        if (sUriMatcher.match(uri) != DATUM) {
            if (sUriMatcher.match(uri) == DATUM_ID) {
                s1 = selection + Constants.ID + uri.lastPathSegment
            } else {
                throw IllegalArgumentException("Unknown URI $uri")
            }
        }
        val db = dbHelper?.readableDatabase
        val c = qb.query(db, projection, s1, selectionArgs, null, null, sortOrder)
        c.setNotificationUri(context!!.contentResolver, uri)
        return c
    }


    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // Kalau url dia bukan yang dedifine
        if (sUriMatcher.match(uri) != DATUM){
            throw IllegalArgumentException("Unknown URI $uri")
        }
        val v : ContentValues
        if (values != null){
            v = ContentValues(values)
        }
        else {
            v = ContentValues()
        }
        val db = dbHelper?.writableDatabase
        // Nama columm (Constant.TEXT)
        val id = db?.insert(DATUM_TABLE_NAME, Constants.TEXT, v)
        if (id != null){
            val uri = ContentUris.withAppendedId(Constants.URL, id)
            context!!.contentResolver.notifyChange(uri,null)
            return uri
        }
        else {
            throw IllegalArgumentException("Unknown URI $uri")
        }
        return null

    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 1
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 1
    }

    inner class DBHelper(context: Context?, name:String?, factory: SQLiteDatabase.CursorFactory?,
                         version:Int) : SQLiteOpenHelper(context,name,factory,version){
        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL("CREATE TABLE $DATUM_TABLE_NAME (_ID INTEGER PRIMARY KEY AUTOINCREMENT, ${Constants.TEXT} TEXT)")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            return
        }

    }

}