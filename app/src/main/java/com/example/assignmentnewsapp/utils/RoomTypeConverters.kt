package com.example.assignmentnewsapp.utils

import androidx.room.TypeConverter
import com.example.assignmentnewsapp.networking.model.Source

class RoomTypeConverters {
  @TypeConverter
  fun fromSource(source: Source):String = "$source.id , ${source.name}"

  @TypeConverter
  fun toSource(sourceString: String):Source{
   var source=Source(sourceString.split(",")[0],sourceString.split(",")[1])
     return source
  }

}