package com.example.techjunction.util

import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class HttpDownloadManager {
    companion object {
        const val TAG = "HttpHelper"

        fun writeData(uri: Uri, file: File): Boolean {
            var conn: HttpURLConnection? = null

            try {
                // Http connect
                val url = URL(uri.toString())
                conn = url.openConnection() as HttpURLConnection
                conn.connect()

                // redirect execute
                val redirect = when (conn.responseCode) {
                    HttpURLConnection.HTTP_MOVED_TEMP,
                    HttpURLConnection.HTTP_MOVED_PERM,
                    HttpURLConnection.HTTP_SEE_OTHER -> true
                    else -> false
                }
                if (redirect) {
                    val newUrl = URL(conn.getHeaderField("Local"))
                    val cookies = conn.getHeaderField("Set-Cookie")
                    conn = newUrl.openConnection() as HttpURLConnection
                    conn.setRequestProperty("Cookie", cookies)
                }

                // write data
                conn.inputStream.use { input ->
                    FileOutputStream(file).use { output ->
                        val data = ByteArray(4096)
                        var count: Int

                        while (input.read(data).also { count = it } != -1) {
                            output.write(data, 0, count)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            } finally {
                conn?.disconnect()
            }
            return true
        }
    }
}