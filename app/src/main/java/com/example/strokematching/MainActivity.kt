package com.example.strokematching

import android.graphics.Path
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.strokematching.PaintView.Companion.defaultStrokes
import com.example.strokematching.PaintView.Companion.jsonPoints
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    private lateinit var saveButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureComponent()
        setupListenerComponent()

        readJSON()
    }

    private fun configureComponent() {
        saveButton = findViewById(R.id.save_button)
    }

    private fun setupListenerComponent() {
        saveButton.setOnClickListener {
            savePointsToJSON()
        }
    }

    private fun savePointsToJSON() {
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val points: String = gsonPretty.toJson(jsonPoints)
        saveJSON(points)
    }

    private fun saveJSON(jsonString: String) {
        val output: Writer
        val file = createFile()
        output = BufferedWriter(FileWriter(file))
        output.write(jsonString)
        output.close()
    }

    private fun readJSON() {
        val floatType: Type = object : TypeToken<ArrayList<ArrayList<FloatPoint>>>() {}.type
        var filename = getFile().absolutePath
        val reader = JsonReader(FileReader(filename))
        val listOfPoints: ArrayList<ArrayList<FloatPoint>> = Gson().fromJson(reader, floatType)
        for (points in listOfPoints) {
            val path = Path()
            for (i in points.indices) {
                if (i == 0) {
                    path.moveTo(points[i].x, points[i].y)
                } else {
                    path.lineTo(points[i].x, points[i].y)
                }
            }

            defaultStrokes.add(path)
        }
    }

    private fun getFile(): File {
        val fileName = "sample.json"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

        return File(
            storageDir,
            fileName
        )
    }

    private fun createFile(): File {
        val fileName = "sample.json"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (storageDir != null) {
            if (!storageDir.exists()){
                storageDir.mkdir()
            }
        }

        return File(
            storageDir,
            fileName
        )
    }
}