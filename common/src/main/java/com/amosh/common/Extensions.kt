package com.amosh.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


fun <T> MutableList<T>.addIfNotExist(item: T?) {
    if (item != null && !this.contains(item)) this.add(item)
}

@Suppress("DEPRECATION") // Fixed for higher versions
fun Context?.isOffline(): Boolean {
    val connectivityManager =
        this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            ?: return true
    val nw = connectivityManager.activeNetwork ?: return true
    val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return true
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> false
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> false
        //for other device how are able to connect with Ethernet
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> false
        //for check internet over Bluetooth
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> false
        else -> true
    }
}