package com.example.todoplanner

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TodoDAO {

    @Query("SELECT * FROM TodoTable")
    fun getAll(): LiveData<List<ToDo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg todo: ToDo)

    @Delete
    suspend fun delete(todo: ToDo)

}