package com.binar.chapter5.database.dao

import androidx.room.*
import com.binar.chapter5.database.modelDB.User


@Dao
interface user_dao {

    @Query("SELECT username From User Where username=(:user) AND password=(:pass)")
    fun login(user:String,pass:String) : String

    @Query("Select id From User Where username=(:username)")
    fun getId(username:String) : Int

    @Query("SELECT * From User Where id=(:id)")
    fun getUser(id:Int): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user : User): Long

    @Update
    fun updateUser(user: User):Int

    @Delete
    fun deleteUser(user: User):Int
}