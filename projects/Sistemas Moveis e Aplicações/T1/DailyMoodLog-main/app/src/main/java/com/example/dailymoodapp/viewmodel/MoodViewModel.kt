package com.example.dailymoodapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymoodapp.data.Mood
import com.example.dailymoodapp.data.MoodDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// MoodViewModel: Mood verilerini yönetmek için basit ViewModel
class MoodViewModel(app: Application) : AndroidViewModel(app) {
    // Veritabanı ve DAO'yu al
    private val db = MoodDatabase.getDatabase(app)
    private val dao = db.moodDao()
    // Mood listesini akış olarak al
    val moodList: Flow<List<Mood>> = dao.getAllMoods()

    // Mood ekle (veritabanına kaydet)
    fun moodEkle(mood: Mood) {
        viewModelScope.launch {
            dao.insertMood(mood)
        }
    }

    // Mood güncelle
    fun moodGuncelle(mood: Mood) {
        viewModelScope.launch {
            dao.updateMood(mood)
        }
    }

    // Mood sil
    fun moodSil(mood: Mood) {
        viewModelScope.launch {
            dao.deleteMood(mood)
        }
    }
} 