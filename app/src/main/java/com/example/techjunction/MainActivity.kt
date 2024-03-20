package com.example.techjunction

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.techjunction.screens.TechJunctionApp
import com.example.techjunction.worker.RssDownloadWorker

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RssDownloadWorker.start(this)
        setContent {
            TechJunctionApp()
        }
    }
}