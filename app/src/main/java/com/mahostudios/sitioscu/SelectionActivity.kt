package com.mahostudios.sitioscu

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*


class SelectionActivity : AppCompatActivity() {

    private lateinit var preferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        val contBtn = findViewById<Button>(R.id.cont_btn)
        val rgroup = findViewById<RadioGroup>(R.id.rgroup)
        val checkbox = findViewById<CheckBox>(R.id.checkbox)
        var act = false
        preferences = getSharedPreferences("min", MODE_PRIVATE)
        preferences = getSharedPreferences("selected_start", MODE_PRIVATE)
        val edit = preferences.edit()

        rgroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio_one -> {
                    act = true
                }
                R.id.radio_two -> {
                    act = false
                }
            }
        }
        contBtn.setOnClickListener {
            if(rgroup.checkedRadioButtonId != -1){
                if (checkbox.isChecked){
                    if(act){
                        val intent = Intent(this@SelectionActivity, MainMinActivity::class.java)
                        edit.putBoolean("min", true)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this@SelectionActivity, MainActivity::class.java)
                        edit.putBoolean("min", false)
                        startActivity(intent)
                    }
                    edit.putBoolean("selected_start", true)
                    edit.apply()
                }else{
                    if(act){
                        val intent = Intent(this@SelectionActivity, MainMinActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this@SelectionActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
//                intent.putExtras(bundle)
                finish()
            }else{
                Toast.makeText(this, "Debe seleccionar una opción", Toast.LENGTH_SHORT).show()
            }

        }
    }
}