package com.ent21.expandablerowchipgroupexample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ent21.expandablerowchipgroup.ExpandableRowChipGroup
import com.google.android.material.chip.Chip

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val expandableChipGroup = findViewById<ExpandableRowChipGroup>(R.id.expandableChipGroup)

        val inflater = LayoutInflater.from(this)
        val mock = resources.getStringArray(R.array.greenWords).toList().shuffled()

        val chips = List(mock.size) {
            val chip = inflater.inflate(
                R.layout.custom_chip,
                expandableChipGroup,
                false
            ) as Chip
            chip.text = mock[it]
            chip
        }

        expandableChipGroup.setControlButtonText { state, hiddenChips ->
            when (state) {
                ExpandableRowChipGroup.State.EXPANDED -> "Hide"
                ExpandableRowChipGroup.State.COLLAPSED -> "Show ($hiddenChips)"
            }
        }
        expandableChipGroup.setChips(chips)
    }
}