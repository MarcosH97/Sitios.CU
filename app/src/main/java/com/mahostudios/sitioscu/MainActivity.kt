package com.mahostudios.sitioscu

import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import java.lang.Exception
import java.util.*
import kotlin.concurrent.thread
import cu.uci.apklischeckpayment.Verify
import cu.uci.apklisupdate.ApklisUpdate
import cu.uci.apklisupdate.UpdateCallback
import cu.uci.apklisupdate.model.AppUpdateInfo
import cu.uci.apklisupdate.view.ApklisUpdateDialog
import cu.uci.apklisupdate.view.ApklisUpdateFragment

class MainActivity : AppCompatActivity() {
    var cont : Int = 0
    private val bundle : Bundle = Bundle()
    private lateinit var preferences : SharedPreferences
    lateinit var searchView: SearchView
    lateinit var listView: ListView
    lateinit var sitio: Sitio2
    lateinit var url : String
    val lista = mutableListOf<Sitio2>()
    private val PREF_NAME = "firstime"

    override fun onResume() {
        try {
            if(!BuildConfig.DEBUG){
                checkPaid()
            }
        }catch (e : Exception){
            e.printStackTrace()
        }

        super.onResume()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)

//        Toast.makeText(this, BuildConfig.DEBUG.toString(), Toast.LENGTH_SHORT ).show()

        if(!BuildConfig.DEBUG){
            checkPaid()
        }

        MainButtons()
        Arrows()
        AddQAccess()
        SearchBar()
        About()
    }

    fun changeToDir(c :Context,name: String){
        bundle.putString("category", name)
        val intent = Intent(this@MainActivity, ListActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun addElement(){
    }

    //La barra de busqueda
    fun SearchBar(){
        setupDB("site")
        listView = findViewById(R.id.search_list)
        searchView = findViewById(R.id.searcher)
        val names = mutableListOf<String>()
        lista.sortBy { it.name }
        for(site in lista){
            names.add(site.name)
        }
        var adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1 , names)
        listView.adapter = adapter
        listView.setOnItemClickListener{parent, view, position, id ->
            val intent = Intent(this@MainActivity, WebViewActivity::class.java)
            url = searchSiteURL(parent.getItemAtPosition(position).toString())
            bundle.putString("url", url)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
    }

    //Los 4 botones del catalogo
    fun MainButtons(){
        val c1 : ImageButton = findViewById(R.id.etecsa_btn)
        val c2 : ImageButton = findViewById(R.id.culture_btn)
        val c3 : ImageButton = findViewById(R.id.infor_btn)
        val c4 : ImageButton = findViewById(R.id.edu_btn)

        c1.setOnClickListener {
            changeToDir(this, "Redes y Telecomunicaciones")
        }
        c2.setOnClickListener {
            changeToDir(this, "Culturales_Entretenimiento")
        }
        c3.setOnClickListener {
            changeToDir(this, "Informativos")
        }
        c4.setOnClickListener {
            changeToDir(this, "Investigativos_Educativos")
        }
    }

    //las flechas de cerrar y abrir las ventanas
    fun Arrows(){
        val arrow0 : ImageButton = findViewById(R.id.arrow0)
        val arrow1 : ImageButton = findViewById(R.id.arrow1)
        val arrow2 : ImageButton = findViewById(R.id.arrow2)
        val arrow3 : ImageButton = findViewById(R.id.arrow3)
        val lin : LinearLayout = findViewById(R.id.linearL0)


        arrow0.setOnClickListener {
            val cl : ConstraintLayout = findViewById(R.id.constraintLayout)
            val zone : RelativeLayout = findViewById(R.id.rela)
            val layoutParams = zone.layoutParams as LinearLayout.LayoutParams
            if(cl.visibility == View.VISIBLE){
                cl.visibility = View.GONE
                arrow0.setBackgroundResource(R.drawable.ic_arrow_down)
                zone.setBackgroundResource(R.drawable.curved_btn1)
                layoutParams.setMargins(0,0,0,50)
                zone.layoutParams = layoutParams

            }else{
                TransitionManager.beginDelayedTransition(lin, AutoTransition())
                cl.visibility = View.VISIBLE
                arrow0.setBackgroundResource(R.drawable.ic_arrow_up)
                zone.setBackgroundResource(R.drawable.curved_btn01)
                layoutParams.setMargins(0,0,0,0)
                zone.layoutParams = layoutParams

            }
        }
        arrow1.setOnClickListener {
            val grid : GridLayout = findViewById(R.id.grid)
            val zone : RelativeLayout = findViewById(R.id.cardView4)
            val layoutParams = zone.layoutParams as LinearLayout.LayoutParams
            if(grid.visibility == View.VISIBLE){
                grid.visibility = View.GONE
                arrow1.setBackgroundResource(R.drawable.ic_arrow_down)
                zone.setBackgroundResource(R.drawable.curved_btn1)
                layoutParams.setMargins(0,0,0,50)
                zone.layoutParams = layoutParams
            }else{
                TransitionManager.beginDelayedTransition(grid, AutoTransition())
                grid.visibility = View.VISIBLE
                arrow1.setBackgroundResource(R.drawable.ic_arrow_up)
                zone.setBackgroundResource(R.drawable.curved_btn01)
                layoutParams.setMargins(0,0,0,0)
                zone.layoutParams = layoutParams
            }
        }
        arrow2.setOnClickListener {
            val con : LinearLayout = findViewById(R.id.contact_layout)
            val zone : RelativeLayout = findViewById(R.id.rela3)
            val layoutParams = zone.layoutParams as LinearLayout.LayoutParams

            if(con.visibility == View.VISIBLE){
                con.visibility = View.GONE
                arrow2.setBackgroundResource(R.drawable.ic_arrow_down)
                zone.setBackgroundResource(R.drawable.curved_btn1)
                layoutParams.setMargins(0,0,0,50)
                zone.layoutParams = layoutParams
            }else{
                TransitionManager.beginDelayedTransition(con, AutoTransition())
                con.visibility = View.VISIBLE
                arrow2.setBackgroundResource(R.drawable.ic_arrow_up)
                zone.setBackgroundResource(R.drawable.curved_btn01)
                layoutParams.setMargins(0,0,0,0)
                zone.layoutParams = layoutParams
            }
        }

        arrow3.setOnClickListener {
            val con : LinearLayout = findViewById(R.id.scroller)
            val zone : RelativeLayout = findViewById(R.id.rela2)
            val layoutParams = zone.layoutParams as LinearLayout.LayoutParams
            if(con.visibility == View.VISIBLE){
                con.visibility = View.GONE
                arrow3.setBackgroundResource(R.drawable.ic_arrow_down)
                zone.setBackgroundResource(R.drawable.curved_btn1)
                layoutParams.setMargins(0,0,0,50)

                zone.layoutParams = layoutParams
            }else{
                TransitionManager.beginDelayedTransition(con, AutoTransition())
                con.visibility = View.VISIBLE
                arrow3.setBackgroundResource(R.drawable.ic_arrow_up)
                zone.setBackgroundResource(R.drawable.curved_btn01)
                layoutParams.setMargins(0,0,0,0)
                zone.layoutParams = layoutParams
            }
        }

    }

    //La ventana de Acerca De
    fun About(){
        val btn0 : ImageButton = findViewById(R.id.contact0)
        val btn1 : ImageButton = findViewById(R.id.contact1)
        val btn2 : ImageButton = findViewById(R.id.contact2)
        btn0.setOnClickListener {
            val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Im_D4rkos"))
            startActivity(telegram)
        }
        btn1.setOnClickListener {
            sendMail()
        }
        btn2.setOnClickListener {
            val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/maho_studios"))
            startActivity(telegram)
        }
    }

    //presionar atras
    override fun onBackPressed() {
//        super.onBackPressed()
        if(cont < 1){
            Toast.makeText(application, "Presione otra vez para salir", Toast.LENGTH_SHORT).show()
            cont++
        }
        else{
            finishAffinity()
        }
    }

    //funcion de envair al correo....aun no funciona
    fun sendMail(){
        val to : String="mhpilgrim97@gmail.com"
        val intent = Intent(Intent.ACTION_SEND)
        intent.data = Uri.parse("mailto:")
        intent.type = "text/plain"

        intent.putExtra(Intent.EXTRA_EMAIL, to)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sitios.CU nuevo sitio")
        intent.putExtra(Intent.EXTRA_TEXT, "Me gustaria que agregaran: (escriba url aquí)")
        try {
            startActivity(Intent.createChooser(intent, "Send mail..."))
        }
        catch (e : ActivityNotFoundException){
            Toast.makeText(this, "No tiene un cliente de correo instalado", Toast.LENGTH_SHORT).show()

        }
    }

    //Prerar la base de datos
    fun setupDB(table : String){
        val dbHelper : DBHelper = DBHelper(this)
        var data = dbHelper.Read(table)
        if(data.moveToFirst()){
            do{
                var id = data.getString(data.getColumnIndex("id")).toInt()
                var cat = data.getString(data.getColumnIndex("category"))
                var name = data.getString(data.getColumnIndex("name"))
                url = data.getString(data.getColumnIndex("url"))
                var desc = data.getString(data.getColumnIndex("description"))
                var cost = data.getString(data.getColumnIndex("cost")).toInt()
                sitio = Sitio2(id,cat,name,url,desc,cost)
                lista.add(sitio)
            }while (data.moveToNext())
        }
    }

    //Buscar el sitio y devolver su URL
    fun searchSiteURL(name : String) : String{
        var s : String = ""
        for(site in lista){
            if(name.equals(site.name))
                s = site.url
        }
        return s
    }

    //Ventana de acceso rapido
    fun AddQAccess(){
        val item0 : Button = findViewById(R.id.item0)
        val item1 : Button = findViewById(R.id.item1)
        val item2 : Button = findViewById(R.id.item2)
        val item3 : Button = findViewById(R.id.item3)

        item0.setOnClickListener{

        }
        item1.setOnClickListener{

        }
        item2.setOnClickListener{

        }
        item3.setOnClickListener{

        }
    }

    //Dialogo emergente para la busqueda
    fun showDialog(){
        var dialog = Dialog(this)
        dialog.setContentView(R.layout.selection_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val searcher : SearchView = dialog.findViewById(R.id.searcher2)
        val lister : ListView = dialog.findViewById(R.id.lister2)
        val names = mutableListOf<String>()
        for(site in lista){
            names.add(site.name)
        }
        var adapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1 , names)
        listView.adapter = adapter
        listView.setOnItemClickListener{parent, view, position, id ->

            startActivity(intent)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
        dialog.show()
        dialog.dismiss()
    }
    
    private fun checkPaid(){
        val response = Verify.isPurchased(this, "${applicationContext.packageName}")
//        val res = Verify.Companion.isPurchased(this, "${applicationContext.packageName}")
        var user : String?
        var ispaid : Boolean = false
        if(!checkApklis()){
            DialogMaker(1)
        }
        else {
            user = response.second
            if (user.equals(null) || user.equals("")) {
                DialogMaker(2)
            }else{
                ispaid = response.first
                //@TODO:Wildcard
                val vips = listOf("marielainfante", "MarcosH", "epsilon", "chopper-kun", "BLNrt", "AntoineAnigma")
                if (vips.contains(user) || ispaid){
                    if(firstTime()){
                        val ed: SharedPreferences.Editor = preferences.edit()
                        ed.putBoolean("firstime", false)
                        ed.commit()
                        DialogMaker(4)
                    }
                }else{
                    DialogMaker(3)
                }
            }
        }
    }
    fun checkApklis(): Boolean{
        val packageManager = applicationContext.packageManager
        try{
            packageManager.getPackageInfo("cu.uci.android.apklis", PackageManager.GET_ACTIVITIES)
            return true
        }catch (e : PackageManager.NameNotFoundException){
            e.printStackTrace()
            return false
        }
    }

    fun CheckUpdate(){
        ApklisUpdate.hasAppUpdate(this, callback = object : UpdateCallback {

            override fun onNewUpdate(appUpdateInfo: AppUpdateInfo) {

                //Start info alert dialog or do what you want.
                ApklisUpdateDialog(
                        this@MainActivity,
                        appUpdateInfo,
                        ContextCompat.getColor(
                                this@MainActivity,
                                R.color.soft_gray)
                ).show()

                //Start info fragment or do what you want.
//                supportFragmentManager.beginTransaction().add(
//                        R.id.container, ApklisUpdateFragment.newInstance(
//                        updateInfo = appUpdateInfo,
//                        actionsColor = ContextCompat.getColor(this@MainActivity, R.color.blue_deep)
//                )
//                ).commit()

            }

            override fun onOldUpdate(appUpdateInfo: AppUpdateInfo) {
                Log.d("MainActivity", "onOldUpdate $appUpdateInfo")
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }
    fun DialogMaker(case : Int){
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
        var diag : androidx.appcompat.app.AlertDialog
        when(case){
            0 -> {
                dialog.setTitle("Actualizaciones")
                        .setMessage("No se encontraron actualizacinoes")
                        .setPositiveButton("Aceptar"){_,_->

                        }
            }
            1 -> {
                var pos : Int = 0
                dialog.setTitle("Apklis no encontrada")
                        .setMessage("Apklis no se encuentra instalado. Por favor descargue e instale la app para comprobar la compra")
                        .setPositiveButton("Cerrar"){_,_->
                            finishAndRemoveTask()
                        }
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .setCancelable(false)
            }
            2 -> {
                dialog.setTitle("Usuario no encontrado")
                        .setMessage("No se ha autenticado en la aplicación de Apklis. Para evitar problemas con la compra y actualizaciones autentíquece lo antes posible")
                        .setPositiveButton("Abrir APKLIS"){_,_->
                            val intent = packageManager.getLaunchIntentForPackage("cu.uci.android.apklis")
                            val chooser = Intent.createChooser(intent, "Launch Apklis")
                            startActivity(chooser)
                        }
                        .setNeutralButton("Cerrar"){_,_->
                                finishAndRemoveTask()
                        }
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .setCancelable(false)
            }
            3 -> {
                dialog.setTitle("AVISO")
                        .setMessage("La aplicación no ha sido comprada! Se cerrará al salir de este mensaje. \nApoye a los desarrolladores con su compra.")
                        .setNeutralButton("Abrir APKLIS"){_,_->
                            val intent = packageManager.getLaunchIntentForPackage("cu.uci.android.apklis")
                            val chooser = Intent.createChooser(intent, "Launch Apklis")
                            startActivity(chooser)
                        }
                        .setPositiveButton("Cerrar"){_,_->
                            finishAndRemoveTask()
                        }
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .setCancelable(false)
            }
            4 -> {
                dialog.setTitle("BIENVENIDO")
                    .setMessage("Gracias por comprar nuestra app. Esperamos la disfrute. \n Ante cualquier problema no dude en contactar con los desarrolladores")
                    .setPositiveButton("Aceptar", null)
                    .setCancelable(true)
            }
            5 -> {

            }
        }
        diag = dialog.create()
        diag.show()
    }
    fun firstTime(): Boolean{
        val ran : Boolean = preferences.getBoolean("firstime", true)
        return ran
    }



    inner class ApklisNotLoggedInException() : Exception() {

    }

}