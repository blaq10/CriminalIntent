package com.example.criminalintent

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {
    private var crimeRepository = CrimeRepository.get()
    val crimesLiveData = crimeRepository.getCrimes()
}