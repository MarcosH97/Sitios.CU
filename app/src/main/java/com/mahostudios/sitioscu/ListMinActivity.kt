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

class ListMinActivity : AppCompatActivity(), RecyclerAdapter.MyInterface {


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
        setContentView(R.layout.activity_list_minimal)

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
        val intent = Intent(this@ListMinActivity, MainMinActivity::class.java)
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
                    banner_title.text = "Redes y \nTelecomunicaicones"
                    cardView.setCardBackgroundColor(resources.getColor(R.color.navy_blue))
                    top_banner.setBackgroundResource(R.color.navy_blue)

                }
                "Culturales y Entretenimiento" -> {
                    s = cat.toString()
                    banner_title.text = "Culturales y \nEntretenimiento"
                    cardView.setCardBackgroundColor(resources.getColor(R.color.magenta_600))
                    top_banner.setBackgroundResource(R.color.magenta_600)

                }
                "Informativos" -> {
                    s = cat.toString()
                    banner_title.text = "Informativos"
                    top_banner.setBackgroundResource(R.color.orange_600)
                    cardView.setCardBackgroundColor(resources.getColor(R.color.magenta_600))
                }
                "Investigativos/Educativos" -> {
                    s = cat.toString()
                    banner_title.text = "Investigativos /\nEducativos"
                    top_banner.setBackgroundResource(R.color.green_600)
                    cardView.setCardBackgroundColor(resources.getColor(R.color.green_600))
                }
                "Periódicos y Revistas" -> {
                    s = cat.toString()
                        banner_title.text = "Periódicos y\nRevistas"
                        top_banner.setBackgroundResource(R.color.yellow_600)
                        cardView.setCardBackgroundColor(resources.getColor(R.color.yellow_600))

                }
                "Radio y Televisión" -> {
                    s = cat.toString()
                        banner_title.text = "Radio y TV"
                        top_banner.setBackgroundResource(R.color.red_600)
                        cardView.setCardBackgroundColor(resources.getColor(R.color.red_600))

                }
                "Universidades" -> {
                    s = cat.toString()
                        banner_title.text ="Universidades"
                        top_banner.setBackgroundResource(R.color.cyan_600)
                        cardView.setCardBackgroundColor(resources.getColor(R.color.cyan_600))
                }
                "Comercio Electrónico" -> {
                    s = cat.toString()
                        banner_title.text = "Comercio \nElectrónico"
                        top_banner.setBackgroundResource(R.color.coldgray_600)
                        cardView.setCardBackgroundColor(resources.getColor(R.color.coldgray_600))
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
//        dialog.setContentView(R.layout.rate_dialog)
//        dialog.show()
//        val go : Button = dialog.findViewById(R.id.go_btn)
//        val off : Button = dialog.findViewById(R.id.off_btn)
//        val later : Button = dialog.findViewById(R.id.later_btn)
//
//        go.setOnClickListener{
//            val intent = packageManager.getLaunchIntentForPackage("cu.uci.android.apklis")
//            val chooser = Intent.createChooser(intent, "Launch Apklis")
//            val ed : SharedPreferences.Editor = settings.edit()
//            ed.putBoolean("rate", true)
//            ed.commit()
//            dialog.dismiss()
//            startActivity(chooser)
//        }
//
//        later.setOnClickListener{
//            dialog.dismiss()
//        }
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
        val intent = Intent(this@ListMinActivity, WebViewActivity::class.java)
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