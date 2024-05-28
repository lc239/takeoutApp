package com.example.takeoutapplication.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.takeoutapplication.model.entity.Address
import com.example.takeoutapplication.model.entity.Tokens
import com.example.takeoutapplication.model.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserInfoManager(private val context: Context){

    companion object{
        private val Context.userStore: DataStore<Preferences> by preferencesDataStore("user_store")

        val PASSWORD = stringPreferencesKey("password") //存着用于重新登录（未完成）
        val TOKEN = stringPreferencesKey("token")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")

    }

    val token: Flow<String> = context.userStore.data.map {
        it[TOKEN] ?: ""
    }

    val refreshToken: Flow<String> = context.userStore.data.map {
        it[REFRESH_TOKEN] ?: ""
    }

    val bearerAuth: Flow<String> = context.userStore.data.map {
        "Bearer " + it[TOKEN]
    }

    val logined: Flow<Boolean> = context.userStore.data.map {
        if(it[TOKEN] == null) return@map false
        else if(it[TOKEN]!!.isEmpty()) return@map false
        return@map true
    }

    suspend fun saveTokens(tokens: Tokens) {
        context.userStore.edit {
            it[TOKEN] = tokens.token
            it[REFRESH_TOKEN] = tokens.refreshToken
        }
    }

    suspend fun clear(){
        context.userStore.edit {
            it[TOKEN] = ""
            it[REFRESH_TOKEN] = ""
        }
    }
}