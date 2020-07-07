package com.example.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.criminalintent.database.CrimeDao
import com.example.criminalintent.database.CrimeDatabase
import java.util.*
import java.util.concurrent.Executors


/*
    A repository class encapsulates the logic for accessing
    data from a single source or a set of sources. It
    determines how to fetch and store a particular set of
    data, whether locally in a database or from a remote
    server.
 */
private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {

    private val database = Room.databaseBuilder (context.applicationContext, CrimeDatabase::class.java, DATABASE_NAME).build()
    private val crimeDao = database.crimeDao()
    private val executor = Executors.newSingleThreadExecutor()

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }

    fun getCrimes() : LiveData<List<Crime>> = crimeDao.getCrimes()

    fun getCrime(id: UUID) : LiveData<Crime?> = crimeDao.getCrime(id)

    fun updateCrime(crime: Crime) {
        executor.execute {
            crimeDao.updateCrime(crime)
        }
    }

    fun addCrime(crime: Crime) {
        executor.execute {
            crimeDao.addCrime(crime)
        }
    }
}