package com.example.test.data.source.database.dao

import android.util.Log
import androidx.room.*

@Dao
abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOrIgnore(item: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOrIgnore(items: List<T>): List<Long>

    @Update
    abstract fun update(item: T)

    @Update
    abstract fun update(list: List<T>): Int

    @Delete
    abstract fun delete(item: T)

    @Delete
    abstract fun delete(list: List<T>)

    @Transaction
    open fun insertOrUpdate(item: T) {
        val id = insertOrIgnore(item)
        if (id == -1L) update(item)
    }

    @Transaction
    open fun insertOrUpdate(list: List<T>) {
        val insertResult = insertOrIgnore(list)
        val updateList = mutableListOf<T>()

        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) updateList.add(list[i])
        }
        var a = 0
        if (!updateList.isEmpty())
            a = update(updateList)
        Log.d("sdf", a.toString())
    }

}