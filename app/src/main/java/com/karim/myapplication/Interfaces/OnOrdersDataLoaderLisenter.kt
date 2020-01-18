package com.karim.myapplication.Interfaces

interface OnOrdersDataLoaderLisenter {
    fun onPicutersOrderLoad()
    fun onMusicOrderLoad()
    fun onPictureFailed()
    fun onMusicFailed()
    fun onScreenloadedSuceess()
    fun onScreenloadedFailed()
    fun onTheaterloadedSuccess()
    fun onTheaterloadedFailed()
    fun onphotoRemoveSuccess()
    fun onPhotoRemoveFailed()
    fun onTheaterRemovedSuccess()
    fun onTheaterRemovedFailed()
    fun onScreenRemovedSuccess()
    fun onScreenRemovedFailed()
}