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
import androidx.cardview.widget.CardView
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


    val lista2 = mutableListOf<Sitio2>()
    lateinit var sitio2 : Sitio2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list)
        darkmode = getDarkMode()
        lay = findViewById(R.id.linear1)
        FadeIn()
        Start()
        val randomval = Random.nextInt(1, 5)
        if(randomval == 1 && !settings.getBoolean("rate", false) && settings.getBoolean("hide", false)){
            rateApp()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@ListActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun Start(){
        val bundle = intent.extras
        var cat : CharSequence? = ""

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
        val cardView : CardView = findViewById(R.id.banner_top)
        top_banner = findViewById(R.id.banner)
            when (cat.toString()) {
                "Redes y Telecomunicaciones" -> {
                    s = "ETECSA"
                    top_banner.setBackgroundResource(R.drawable.ban_net)
                    if (darkmode) lay.setBackgroundResource(R.drawable.netbgblack)
                    else lay.setBackgroundResource(R.drawable.netbackwhite)
                }
                "Culturales y Entretenimiento" -> {
                    s = cat.toString()
                    top_banner.setBackgroundResource(R.drawable.ban_cult)
                    if (darkmode) lay.setBackgroundResource(R.drawable.cultbgblack)
                    else lay.setBackgroundResource(R.drawable.cultbglight)
                }
                "Informativos" -> {
                    s = cat.toString()
                    top_banner.setBackgroundResource(R.drawable.ban_info)
                    if (darkmode) lay.setBackgroundResource(R.drawable.infobgblack)
                    else lay.setBackgroundResource(R.drawable.infobglight)
                }
                "Investigativos/Educativos" -> {
                    s = cat.toString()
                    top_banner.setBackgroundResource(R.drawable.ban_edu)
                    if (darkmode) lay.setBackgroundResource(R.drawable.edubgblack)
                    else lay.setBackgroundResource(R.drawable.edubglight)
                }
                "Periódicos y Revistas" -> {
                    s = cat.toString()
                    top_banner.setBackgroundResource(R.drawable.ban_ppm)
                    if (darkmode) lay.setBackgroundResource(R.drawable.ppmblack)
                    else lay.setBackgroundResource(R.drawable.ppmlight)
                }
                "Radio y Televisión" -> {
                    s = cat.toString()
                    top_banner.setBackgroundResource(R.drawable.ban_rtv)
                    if (darkmode) lay.setBackgroundResource(R.drawable.rtvblack)
                    else lay.setBackgroundResource(R.drawable.rtvlight)
                }
                "Universidades" -> {
                    s = cat.toString()
                    top_banner.setBackgroundResource(R.drawable.ban_uni)
                    if (darkmode) lay.setBackgroundResource(R.drawable.uniblack)
                    else lay.setBackgroundResource(R.drawable.unilight)
                }
                "Comercio Electrónico" -> {
                    s = cat.toString()
                    top_banner.setBackgroundResource(R.drawable.ban_trade)
                    if (darkmode) lay.setBackgroundResource(R.drawable.tradeblack)
                    else lay.setBackgroundResource(R.drawable.tradelight)
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

    fun getList() : List<Sitio2>{
        lista2.sortBy { it.name }
        return lista2
    }

    fun setupDB(cat: String){
        val dbHelper : DBHelper = DBHelper(this)
        var data = dbHelper.Read("site")
        if(data.moveToFirst()){
            do{
                if(data.getString(data.getColumnIndex("category")).equals(cat)){
                    var id = data.getString(data.getColumnIndex("id")).toInt()
                    var name = data.getString(data.getColumnIndex("name"))
                    var url = data.getString(data.getColumnIndex("url"))
                    var desc = data.getString(data.getColumnIndex("description"))
                    var cost = data.getString(data.getColumnIndex("cost")).toInt()
                    sitio2 = Sitio2(id, null,cost, name, url, desc, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
                    lista2.add(sitio2)
                }else if(data.getString(data.getColumnIndex("category")).equals(cat)){

                }
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
        val diag = AlertDialog.Builder(this)
            .setTitle("Hola!")
            .setMessage("¿Está disfrutando nuestra app? \nSería de gran apoyo su opinión y solo toma un momento")
            .setPositiveButton("¡Sí!"){_,_->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.apklis.cu/application/com.mahostudios.sitioscu"))
                startActivity(intent)
            }
            .setNeutralButton("Más tarde"){_,_->

            }
                .setCancelable(false)
        val d = diag.create()
        d.show()
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