package com.example.watch2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailField: EditText = findViewById(R.id.editTextLoginEmail)
        val passwordField: EditText = findViewById(R.id.editTextLoginPassword)
        val rememberMeCheckBox: CheckBox = findViewById(R.id.checkBoxRememberMe)
        val loginButton: Button = findViewById(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Введите адрес электронной почты", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email, password, rememberMeCheckBox.isChecked)
        }
    }

    private fun loginUser(email: String, password: String, rememberMe: Boolean) {
        Log.d("LoginActivity", "Trying to log in with email: $email")
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Вход успешен
                    Log.d("LoginActivity", "Login successful")
                    val intent = Intent(this, WatchListActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Если вход не удался, вывести сообщение пользователю
                    Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                    handleAuthException(task.exception)
                }
            }
            .addOnFailureListener { e ->
                Log.e("LoginActivity", "signInWithEmail:failure", e)
                handleAuthException(e)
            }
    }

    private fun handleAuthException(exception: Exception?) {
        if (exception is FirebaseAuthException) {
            when (exception.errorCode) {
                "ERROR_INVALID_EMAIL" -> {
                    Toast.makeText(this, "Неверный формат электронной почты.", Toast.LENGTH_SHORT).show()
                }
                "ERROR_WRONG_PASSWORD" -> {
                    Toast.makeText(this, "Неправильный пароль.", Toast.LENGTH_SHORT).show()
                }
                "ERROR_USER_NOT_FOUND" -> {
                    Toast.makeText(this, "Пользователь с такой почтой не найден.", Toast.LENGTH_SHORT).show()
                }
                "ERROR_USER_DISABLED" -> {
                    Toast.makeText(this, "Этот аккаунт был отключен.", Toast.LENGTH_SHORT).show()
                }
                "ERROR_EMAIL_ALREADY_IN_USE" -> {
                    Toast.makeText(this, "Эта почта уже используется другим аккаунтом.", Toast.LENGTH_SHORT).show()
                }
                "ERROR_TOO_MANY_REQUESTS" -> {
                    Toast.makeText(this, "Слишком много неудачных попыток входа. Пожалуйста, попробуйте позже.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Ошибка: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Ошибка: ${exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}