package com.zp.tech.deleted.messages.status.saver.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScannerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveCode(scanner: Scanner): Long

    @Query("select * from scanner where isCreated=:isCreated order by id DESC ")
    fun getAllCodes(isCreated: Boolean): Flow<List<Scanner>>

    @Query("select * from scanner where id=:id")
    fun getCode(id: Int): Flow<Scanner>

    @Query("delete from scanner where id=:rowId")
    suspend fun delete(rowId: Int)
}