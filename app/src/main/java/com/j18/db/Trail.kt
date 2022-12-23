package com.j18.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Recent_Trails")
data class Trail(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="id")
    var id: String,

    @ColumnInfo(name="trail_name")
    var trailName: String? ,

    @ColumnInfo(name="trail_id")
    var trailId: String?,

    @ColumnInfo(name="description")
    var description: String?,

    @ColumnInfo(name="time")
    var time: Long,

    )