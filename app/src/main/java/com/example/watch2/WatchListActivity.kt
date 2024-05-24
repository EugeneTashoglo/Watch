package com.example.watch2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale



class WatchListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var watchAdapter: WatchAdapter
    private lateinit var watchList: MutableList<Watch>
    private lateinit var filteredWatchList: MutableList<Watch>
    private lateinit var database: DatabaseReference
    private lateinit var searchField: EditText
    private lateinit var buttonPopular: Button
    private lateinit var buttonNew: Button
    private var currentQuery: String = ""
    private var showPopular: Boolean = false
    private var showNew: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_list)

        recyclerView = findViewById(R.id.recyclerViewWatches)
        searchField = findViewById(R.id.editTextSearch)
        buttonPopular = findViewById(R.id.buttonPopular)
        buttonNew = findViewById(R.id.buttonNew)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        watchList = mutableListOf()
        filteredWatchList = mutableListOf()
        watchAdapter = WatchAdapter(filteredWatchList)
        recyclerView.adapter = watchAdapter

        database = FirebaseDatabase.getInstance().getReference("watches")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                watchList.clear()
                for (dataSnapshot in snapshot.children) {
                    val watch = dataSnapshot.getValue(Watch::class.java)
                    watch?.let {
                        Log.d("WatchListActivity", "Loaded watch: $it")
                        watchList.add(it)
                    }
                }
                applyCurrentFilters()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("WatchListActivity", "Database error: ${error.message}")
            }
        })

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentQuery = s.toString()
                applyCurrentFilters()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        buttonPopular.setOnClickListener {
            showPopular = !showPopular
            showNew = false  // Сбрасываем другой фильтр
            applyCurrentFilters()
        }

        buttonNew.setOnClickListener {
            showNew = !showNew
            showPopular = false  // Сбрасываем другой фильтр
            applyCurrentFilters()
        }
    }

    private fun applyCurrentFilters() {
        val lowerCaseQuery = currentQuery.toLowerCase(Locale("ru"))
        val queryWords = lowerCaseQuery.split("\\s+".toRegex()).filter { it.isNotEmpty() }

        filteredWatchList.clear()

        for (watch in watchList) {
            val matchesQuery = queryWords.all { queryWord ->
                watch.name.toLowerCase(Locale("ru")).contains(queryWord) ||
                        watch.description.toLowerCase(Locale("ru")).contains(queryWord)
            }

            Log.d("WatchListActivity", "Checking watch: ${watch.name}, isPopular: ${watch.popular}, isNew: ${watch.new}, matchesQuery: $matchesQuery")

            if (matchesQuery) {
                // Фильтрация по популярности и новизне
                if ((showPopular && watch.popular) || (showNew && watch.new) || (!showPopular && !showNew)) {
                    Log.d("WatchListActivity", "Adding watch to filtered list: ${watch.name}")
                    filteredWatchList.add(watch)
                }
            }
        }

        Log.d("WatchListActivity", "Filtered list size: ${filteredWatchList.size}")
        Toast.makeText(this, "Filtered items: ${filteredWatchList.size}", Toast.LENGTH_SHORT).show()

        watchAdapter.notifyDataSetChanged()
    }
}