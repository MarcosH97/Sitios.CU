package com.mahostudios.sitioscu

import android.app.Dialog
import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.animation.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import java.lang.Exception
import java.lang.NullPointerException

class ListActivity : AppCompatActivity(), RecyclerAdapter.MyInterface {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecyclerAdapter
    lateinit var top_text : TextView
    lateinit var settings : SharedPreferences
    lateinit var dialog: Dialog
    lateinit var lay : LinearLayout

    val PREF_NAME = "hide"

    val lista = mutableListOf<Sitio>()
    lateinit var sitio : Sitio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        lay = findViewById(R.id.linear1)
        FadeIn()

        val bundle = intent.extras
        var cat : CharSequence? = ""
        top_text = findViewById(R.id.top_text)
        try {
            cat = bundle!!.getCharSequence("category")
        }catch (e:NullPointerException){
            Toast.makeText(this, "Name not Found", Toast.LENGTH_SHORT).show()
        }
        val lay = findViewById<RelativeLayout>(R.id.layout)
        val s : String

        settings = getSharedPreferences("hide", MODE_PRIVATE)

        dialog = Dialog(this)


        if(!settings.getBoolean("hide", false))
            showLegend0()

        when(cat.toString()){
            "Redes y Telecomunicaciones" ->{
                s = "ETECSA"
                top_text.text = cat.toString()
                lay.setBackgroundResource(R.drawable.gradient_bg2)
            }
            "Culturales_Entretenimiento" ->{
                s = cat.toString()
                top_text.text = "Culturales y Entretenimiento"
                lay.setBackgroundResource(R.drawable.gradient_bg3)
            }
            "Informativos" ->{
                s = cat.toString()
                top_text.text = s
                lay.setBackgroundResource(R.drawable.gradient_bg4)
            }
            "Investigativos_Educativos" ->{
                s = cat.toString()
                top_text.text = "Investigativos/Educativos"
                lay.setBackgroundResource(R.drawable.gradient_bg5)
            }
            else -> s = ""
        }
        setupDB(s)
        recyclerView = findViewById(R.id.recycler_v)
        setRecycler(s)

    }
    override fun onBackPressed() {
        val intent = Intent(this@ListActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun FadeIn() {
        val set = AnimationSet(true)
        val fadein: Animation = AlphaAnimation(0.0f, 1.0f)
        fadein.duration = 700
        fadein.fillAfter = true
        set.addAnimation(fadein)
        val controller = LayoutAnimationController(set, 0.2f)
        lay.layoutAnimation = controller
    }
    fun FadeInScale(){

//        val animation : LayoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.)
    }

    fun getList() : List<Sitio>{
        return lista
    }

    fun setupDB(table : String){
        val dbHelper : DBHelper = DBHelper(this)
        var data = dbHelper.Read(table)
        if(data.moveToFirst()){
            do{
                var id = data.getString(data.getColumnIndex("id")).toInt()
                var cost = data.getString(data.getColumnIndex("cost")).toInt()
                var name = data.getString(data.getColumnIndex("name"))
                var url = data.getString(data.getColumnIndex("url"))
                var desc = data.getString(data.getColumnIndex("desc"))
                var down = data.getString(data.getColumnIndex("down")).toInt()
                sitio = Sitio(id,cost,name,url,desc,down)
                lista.add(sitio)
            }while (data.moveToNext())
        }
    }

    fun setRecycler(s : String){
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerAdapter(this, getList(), s)
        recyclerView.adapter = adapter
    }

    fun showLegend(){
        val inflater = layoutInflater
        val inflate_view = inflater.inflate(R.layout.dialog_layout, null)
        val alertDialog = AlertDialog.Builder(this)
            alertDialog.setView(inflate_view)

                    .setPositiveButton("Ok") { _, _ ->
                        var checkBox : CheckBox = findViewById(R.id.checkbox_side)
                        if (checkBox.isChecked) {
                            val ed: SharedPreferences.Editor = settings.edit()
                            ed.putBoolean("hide", false)
                            ed.apply()
                        }
                        closeContextMenu()
                    }
                    .create()
                    .show()
    }

    fun showLegend0(){
        dialog.setContentView(R.layout.dialog_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val showbtn : Button = dialog.findViewById(R.id.show_btn)
        dialog.show()
        showbtn.setOnClickListener{
            val ed : SharedPreferences.Editor = settings.edit()
            ed.putBoolean("hide", true)
            ed.commit()
            dialog.dismiss()
        }
    }

    override fun onMethodCallback(url: String) {
        val bundle = Bundle()
        val intent = Intent(this@ListActivity, WebViewActivity::class.java)
        bundle.putString("url", url)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun shareLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun copyToClipboard(url: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        Toast.makeText(this, "Dirección copiada", Toast.LENGTH_SHORT).show()
        val clip = ClipData.newPlainText(url, url)
        clipboard.setPrimaryClip(clip)
    }
}