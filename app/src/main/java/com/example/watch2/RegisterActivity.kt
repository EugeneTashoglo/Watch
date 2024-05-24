package com.example.watch2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.watch2.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val nameField: EditText = findViewById(R.id.editTextName)
        val emailField: EditText = findViewById(R.id.editTextEmail)
        val passwordField: EditText = findViewById(R.id.editTextPassword)
        val confirmPasswordField: EditText = findViewById(R.id.editTextConfirmPassword)
        val registerButton: Button = findViewById(R.id.buttonRegister)

        registerButton.setOnClickListener {
            val name = nameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                Toast.makeText(this, "Введите адрес электронной почты", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                passwordField.error = "Пароли не совпадают"
                confirmPasswordField.error = "Пароли не совпадают"
                return@setOnClickListener
            }

            registerUser(name, email, password)
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        Log.d("RegisterActivity", "Trying to register with email: $email")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Регистрация успешна
                    Log.d("RegisterActivity", "Registration successful")
                    val intent = Intent(this, WatchListActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Если регистрация не удалась, вывести сообщение пользователю
                    Log.w("RegisterActivity", "createUserWithEmail:failure", task.exception)
                    handleAuthException(task.exception)
                }
            }
            .addOnFailureListener { e ->
                Log.e("RegisterActivity", "createUserWithEmail:failure", e)
                handleAuthException(e)
            }
    }

    private fun handleAuthException(exception: Exception?) {
        if (exception is FirebaseAuthException) {
            when (exception.errorCode) {
                "ERROR_INVALID_EMAIL" -> {
                    Toast.makeText(this, "Неверный формат электронной почты.", Toast.LENGTH_SHORT).show()
                }
                "ERROR_EMAIL_ALREADY_IN_USE" -> {
                    Toast.makeText(this, "Эта почта уже используется другим аккаунтом.", Toast.LENGTH_SHORT).show()
                }
                "ERROR_WEAK_PASSWORD" -> {
                    Toast.makeText(this, "Пароль слишком слабый. Пароль должен содержать как минимум 6 символов.", Toast.LENGTH_SHORT).show()
                }
                "ERROR_OPERATION_NOT_ALLOWED" -> {
                    Toast.makeText(this, "Регистрация через почту отключена.", Toast.LENGTH_SHORT).show()
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