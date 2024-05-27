package com.example.watch2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso



class WatchDetailActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textViewName: TextView
    private lateinit var textViewRating: TextView
    private lateinit var textViewPrice: TextView
    private lateinit var textViewDescription: TextView
    private lateinit var buttonTry: Button
    private lateinit var heartButton: ImageView
    private lateinit var backButton: ImageView
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var isLiked: Boolean = false
    private var watchId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_detail)

        imageView = findViewById(R.id.imageViewWatch)
        textViewName = findViewById(R.id.textViewName)
        textViewRating = findViewById(R.id.textViewRating)
        textViewPrice = findViewById(R.id.textViewPrice)
        textViewDescription = findViewById(R.id.textViewDescription)
        buttonTry = findViewById(R.id.buttonTry)
        heartButton = findViewById(R.id.heartButton)
        backButton = findViewById(R.id.backButton)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        // Получаем данные из интента
        watchId = intent.getIntExtra("id", 0)
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
        backButton.setOnClickListener {
            finish()
        }

        // Проверка, если часы уже в списке лайков
        val userId = auth.currentUser?.uid ?: return
        val watchRef = database.child("users").child(userId).child("likedWatches").child(watchId.toString())

        watchRef.get().addOnSuccessListener {
            isLiked = it.exists()
            updateHeartButton()
        }

        // Обработка нажатия на кнопку лайка
        heartButton.setOnClickListener {
            isLiked = !isLiked
            if (isLiked) {
                watchRef.setValue(true).addOnCompleteListener {
                    // Сообщаем, что данные изменились
                    setResult(RESULT_OK)
                }
            } else {
                watchRef.removeValue().addOnCompleteListener {
                    // Сообщаем, что данные изменились
                    setResult(RESULT_OK)
                }
            }
            updateHeartButton()
        }


    }

    private fun updateHeartButton() {
        if (isLiked) {
            heartButton.setImageResource(R.drawable.heartlike) // Используем вашу иконку заполненного сердца
        } else {
            heartButton.setImageResource(R.drawable.heart) // Иконка для незаполненного сердца
        }
    }
}