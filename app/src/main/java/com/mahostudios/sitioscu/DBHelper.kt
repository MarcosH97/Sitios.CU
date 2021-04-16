package com.mahostudios.sitioscu

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.RuntimeException
import java.nio.Buffer

class DBHelper(val context: Context)
{
    companion object{
        val DB_VERSION : Int = 7
        val DB_NAME : String = "directory.db"
        var i: Int = 0

    }
    val database : SQLiteDatabase
    init {
        database = open()
    }
    fun open():SQLiteDatabase{
        val dbFile = context.getDatabasePath("$DB_NAME")
        if(!dbFile.exists()){
            try {
                val checkDB = context.openOrCreateDatabase("$DB_NAME", Context.MODE_PRIVATE,null)
                checkDB.close()
                copyDB(dbFile)
            }catch (e:IOException){
                throw RuntimeException("")
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.path,null, SQLiteDatabase.OPEN_READONLY)
    }
    fun copyDB(dbFile: File){
        val iss = context.assets.open("$DB_NAME")
        val os = FileOutputStream(dbFile)
        val buffer = ByteArray(1024)
        generateSequence { i = iss.read(buffer)
        if (i<0) null
            else i
        } .forEach {
            os.write(buffer, 0, i)
        }

        while (iss.read(buffer)>0)
            os.write(buffer)
        os.flush()
        os.close()
        iss.close()
    }
    fun Read(table : String):Cursor{
        val db :SQLiteDatabase = this.database
        val read :Cursor = db.rawQuery("SELECT * FROM $table",null)
        return read
    }
}