package ru.dima.firstproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    private val _trackName = MutableLiveData("Hello! \uD83E\uDD16")
    val trackName: LiveData<String>
        get() = _trackName

    private val _trackImage = MutableLiveData(R.drawable.ic_launcher_foreground)
    val trackImage: LiveData<Int>
        get() = _trackImage

    fun updateIsPlaying(value: Boolean) {
        _isPlaying.value = value
    }

    fun updateTrackName(value: String) {
        _trackName.value = value
    }

    fun updateTrackImage(value: Int) {
        _trackImage.value = value
    }
}