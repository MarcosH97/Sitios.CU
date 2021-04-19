package com.mahostudios.sitioscu

import android.app.Dialog
import android.content.*
import android.content.res.Configuration
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
import kotlin.random.Random

class ListActivity : AppCompatActivity(), RecyclerAdapter.MyInterface {


    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecyclerAdapter
    lateinit var top_banner: ImageView
    lateinit var settings : SharedPreferences
    lateinit var dialog: Dialog
    lateinit var lay : LinearLayout
    lateinit var banner_title : TextView
    var darkmode : Boolean = false
    val PREF_NAME = "hide"


    val lista = mutableListOf<Sitio>()
    lateinit var sitio : Sitio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        darkmode = getDarkMode()
        lay = findViewById(R.id.linear1)
        FadeIn()
        Start()
        val randomval = Random.nextInt(1, 5)
        if(randomval == 1 && !settings.getBoolean("rate", false)){
            rateApp()
        }

    }

    override fun onNightModeChanged(mode: Int) {
        Start()
        super.onNightModeChanged(mode)
    }

    override fun onRestart() {
        Start()
        super.onRestart()
    }
    override fun onBackPressed() {
        val intent = Intent(this@ListActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun Start(){
        val bundle = intent.extras
        var cat : CharSequence? = ""

        top_banner = findViewById(R.id.banner)


        try {
            cat = bundle!!.getCharSequence("category")
        }catch (e:NullPointerException){
            Toast.makeText(this, "Name not Found", Toast.LENGTH_SHORT).show()
        }
        val lay = findViewById<RelativeLayout>(R.id.layout, )
        val s : String

        settings = getSharedPreferences("hide", MODE_PRIVATE)
        settings = getSharedPreferences("rate", MODE_PRIVATE)

        dialog = Dialog(this)


        if(!settings.getBoolean("hide", false))
            showLegend0()

        banner_title = findViewById(R.id.banner_title)

        when(cat.toString()){
            "Redes y Telecomunicaciones" ->{
                s = "ETECSA"
                banner_title.text = "Redes y \nTelecomunicaicones"
                top_banner.setBackgroundResource(R.drawable.ic_netbanner)
                if(darkmode)lay.setBackgroundResource(R.drawable.netbgblack)
                else lay.setBackgroundResource(R.drawable.netbackwhite)

            }
            "Culturales_Entretenimiento" ->{
                s = cat.toString()
                banner_title.text = "Culturales y \nEntretenimiento"
                top_banner.setBackgroundResource(R.drawable.ic_cultbanner)
                if(darkmode)lay.setBackgroundResource(R.drawable.cultbgblack)
                else lay.setBackgroundResource(R.drawable.cultbglight)
            }
            "Informativos" ->{
                s = cat.toString()
                banner_title.text = "Informativos"
                top_banner.setBackgroundResource(R.drawable.ic_infobanner)
                if(darkmode)lay.setBackgroundResource(R.drawable.infobgblack)
                else lay.setBackgroundResource(R.drawable.infobglight)
            }
            "Investigativos_Educativos" ->{
                s = cat.toString()
                banner_title.text = "Investigativos /\nEducativos"
                top_banner.setBackgroundResource(R.drawable.ic_edubanner)
                if(darkmode)lay.setBackgroundResource(R.drawable.edubgblack)
                else lay.setBackgroundResource(R.drawable.edubglight)
            }
            else -> s = ""
        }
        setupDB(s)
        recyclerView = findViewById(R.id.recycler_v)
        setRecycler(s)
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

    fun getDarkMode():Boolean{
        val current = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return current == Configuration.UI_MODE_NIGHT_YES
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
    fun rateApp(){
        dialog.setContentView(R.layout.rate_dialog)
        dialog.show()
        val go : Button = dialog.findViewById(R.id.go_btn)
        val off : Button = dialog.findViewById(R.id.off_btn)
        val later : Button = dialog.findViewById(R.id.later_btn)

        go.setOnClickListener{
            val ed : SharedPreferences.Editor = settings.edit()
            ed.putBoolean("rate", true)
            ed.commit()
            dialog.dismiss()
        }
        off.setOnClickListener{
            val ed : SharedPreferences.Editor = settings.edit()
            ed.putBoolean("rate", true)
            ed.commit()
            dialog.dismiss()
        }
        later.setOnClickListener{
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