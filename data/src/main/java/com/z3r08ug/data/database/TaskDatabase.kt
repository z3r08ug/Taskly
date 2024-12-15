package com.z3r08ug.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.z3r08ug.data.dao.TaskDao
import com.z3r08ug.data.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}