package com.j18.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Trail::class], version = 4
)

abstract class MyDatabase:RoomDatabase() {

   abstract fun myDao(): MyDataAccessObject
}