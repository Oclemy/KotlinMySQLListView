package com.devosha.kotlin_mysql_listview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView


class MainActivity : AppCompatActivity() {
    private val PHP_MYSQL_SITE_URL = "http://192.168.12.2/php/quotika"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myListView = findViewById<ListView>(R.id.myListView)

        val downloadBtn = findViewById<Button>(R.id.downloadBtn)
        downloadBtn.setOnClickListener({ JSONDownloader(this@MainActivity, PHP_MYSQL_SITE_URL, myListView).execute() })
    }

}

