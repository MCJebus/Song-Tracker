package com.example.songtracker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(
    context: Context?
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TASKS_TABLE = "CREATE    TABLE $TABLE_LYRICS($COLUMN_ID INTEGER PRIMARY KEY,$COLUMN_LYRIC TEXT, $COLUMN_SONG TEXT, $COLUMN_ARTIST TEXT, $COLUMN_FOUND BOOLEAN)"
        db!!.execSQL(CREATE_TASKS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_LYRICS")
        onCreate(db)
    }

    fun addLyric(lyric: Lyric) {
        val values = ContentValues()
        values.put(COLUMN_ID, lyric.id)
        values.put(COLUMN_LYRIC, lyric.lyric)
        values.put(COLUMN_SONG, lyric.song)
        values.put(COLUMN_ARTIST, lyric.artist)
        values.put(COLUMN_FOUND, lyric.found)
        val db = this.writableDatabase
        db.insert(TABLE_LYRICS, null, values)
    }

    fun updateLyric(lyric: Lyric) {
        val values = ContentValues()
        values.put(COLUMN_LYRIC, lyric.lyric)
        val db = this.writableDatabase
        db.update( TABLE_LYRICS, values, "$COLUMN_ID = ?", arrayOf(lyric.id.toString()) )
    }

    fun deleteLyric(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_LYRICS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun getAll(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_LYRICS", null)
    }

    fun getFoundLyrics(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_LYRICS WHERE $COLUMN_FOUND = true", null)
    }

    companion object {
        private val DATABASE_VERSION = 5
        private val DATABASE_NAME = "songtrackerdb"
        private val TABLE_LYRICS = "lyrics"

        private val COLUMN_ID = "_id"
        private val COLUMN_LYRIC = "lyric"
        private val COLUMN_SONG = "song"
        private val COLUMN_ARTIST = "artist"
        private val COLUMN_FOUND = "found"
    }

}