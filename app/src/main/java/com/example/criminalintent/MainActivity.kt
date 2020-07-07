package com.example.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*

class MainActivity : AppCompatActivity(), CrimeListFragment.Callbacks {

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = CrimeListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onCrimeSelected(crimeID: UUID) {
        Log.d(TAG, "MainActivity.onCrimeSelected: $crimeID")

        val crimeFragment = CrimeFragment.newInstance(crimeID)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, crimeFragment)
            .addToBackStack(null)
            .commit()
    }
}