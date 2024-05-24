package com.example.watch2

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso


class WatchDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_detail)

        val imageView: ImageView = findViewById(R.id.imageViewWatch)
        val textViewName: TextView = findViewById(R.id.textViewName)
        val textViewRating: TextView = findViewById(R.id.textViewRating)
        val textViewPrice: TextView = findViewById(R.id.textViewPrice)
        val textViewDescription: TextView = findViewById(R.id.textViewDescription)
        val buttonTry: Button = findViewById(R.id.buttonTry)

        // Получаем данные из интента
        val name = intent.getStringExtra("name")
        val rating = intent.getDoubleExtra("rating", 0.0)
        val price = intent.getIntExtra("price", 0)
        val description = intent.getStringExtra("description")
        val imageFrameName = intent.getStringExtra("imageFrameName")

        // Устанавливаем данные в соответствующие элементы
        textViewName.text = name
        textViewRating.text = rating.toString()
        textViewPrice.text = "от $price₽"
        textViewDescription.text = description

        // Загружаем изображение с использованием Picasso
        Picasso.get()
            .load(imageFrameName)
            .into(imageView)

        // Кнопка "назад"
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}