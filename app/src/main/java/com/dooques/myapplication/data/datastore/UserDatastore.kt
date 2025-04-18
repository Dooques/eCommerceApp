package com.dooques.myapplication.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dooques.myapplication.data.datastore.UserDsRepository.Companion.TAG
import com.dooques.myapplication.data.datastore.UserDsRepository.Companion.email
import com.dooques.myapplication.data.datastore.UserDsRepository.Companion.signedIn
import com.dooques.myapplication.data.datastore.UserDsRepository.Companion.username
import com.dooques.myapplication.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface UserDsRepository {

    suspend fun updateUser(user: UserProfile)
    suspend fun updateSignedInState(signedInState: Boolean)

    fun getUsername(): Flow<String>
    fun getEmail(): Flow<String>
    fun getSignedInState(): Flow<Boolean>

    companion object {
        const val TAG = "UserDatastoreRepository"
        val username = stringPreferencesKey("username")
        val email = stringPreferencesKey("email")
        val signedIn = booleanPreferencesKey("signed_in")
    }
}

class UserDatastoreRepository(private val dataStore: DataStore<Preferences>): UserDsRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

    override suspend fun updateUser(user: UserProfile) {
        dataStore.edit { preference ->
            preference[username] = user.username
            preference[email] = user.email
        }
    }

    override suspend fun updateSignedInState(signedInState: Boolean) {
        dataStore.edit { preference ->
            preference[signedIn] = signedInState
        }
    }

    override fun getUsername(): Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading User Preferences", it)
            } else {
                throw it
            }
        }
        .map { preference ->
            preference[username] ?: "Invalid Username"
        }

    override fun getEmail() = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading User Preferences", it)
            } else {
                throw it
            }
        }
        .map { preference ->
            preference[email] ?: "Invalid Email"
        }

    override fun getSignedInState() = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading User Preferences", it)
            } else {
                throw it
            }
        }
        .map { preference ->
            preference[signedIn] ?: false
        }


}