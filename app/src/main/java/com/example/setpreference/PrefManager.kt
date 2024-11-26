package com.example.setpreference

import android.content.Context
import android.content.SharedPreferences


class PrefManager private constructor(context: Context){
    private val sharedPreferences:SharedPreferences = context.getSharedPreferences(
        FILE_NAME, Context.MODE_PRIVATE
    )
    companion object{
        private const val FILE_NAME = "SharePreferencesApp"
        @Volatile
        private var instance:PrefManager? = null
        fun getInstance(context: Context):PrefManager   {
//            tanda titik 2 akan mengembalikan instance jika bernilai null akan mengembalikan setelah titik
            return instance?: synchronized(this) {
//                also akan membuat sebuah fungsi dengan parameter PrefManager(context.applicationContext)
                instance?:PrefManager(context.applicationContext).also {  pref->
                    instance = pref
                }
            }
        }
        private const val KEY_USERNAME = "username"
    }
    fun saveUsername(username:String){
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME,username)
        editor.apply()
    }
    fun getUsername(): String {
        return sharedPreferences.getString(KEY_USERNAME, "").orEmpty()
    }
    fun clear() {
        sharedPreferences.edit().also {
            it.clear()
            it.apply()
        }
    }
}