package com.karim.myapplication

interface OnOrdersDataLoaderLisenter {
    fun onPicutersOrderLoad()
    fun onMusicOrderLoad()
    fun onPictureFailed()
    fun onMusicFailed()
    fun onScreenloadedSuceess()
    fun onScreenloadedFailed()
    fun onTheaterloadedSuccess()
    fun onTheaterloadedFailed()
}