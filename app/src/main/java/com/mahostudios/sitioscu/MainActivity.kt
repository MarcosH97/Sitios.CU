package com.mahostudios.sitioscu

import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.LinearLayout.*
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.Exception
import cu.uci.apklischeckpayment.Verify
import cu.uci.apklisupdate.ApklisUpdate
import cu.uci.apklisupdate.BuildConfig
import cu.uci.apklisupdate.UpdateCallback
import cu.uci.apklisupdate.model.AppUpdateInfo
import cu.uci.apklisupdate.view.ApklisUpdateDialog
import androidx.viewbinding.ViewBinding
import com.mahostudios.sitioscu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    var cont : Int = 0
    private val bundle : Bundle = Bundle()
    private lateinit var preferences : SharedPreferences
    lateinit var searchView: SearchView
    lateinit var listView: ListView
    lateinit var sitio: Sitio2
    lateinit var url : String
    lateinit var chipsGroup : ChipGroup
    lateinit var chipsGroup2 : ChipGroup
    lateinit var hor_lay : HorizontalScrollView
    lateinit var simple_search : ImageButton
    lateinit var advanced_search : ImageButton
    lateinit var VERSION_NAME : String
    lateinit var select : Sitio2
    lateinit var selist : Array<Sitio2>
    var minimal = false

//    lateinit var list_btn : Button
    val lista = mutableListOf<Sitio2>()
    private val PREF_NAME = "firstime"
    private val MINIMAL = "min"
    lateinit var adapter : ArrayAdapter<String>
    val filter = mutableListOf<String>()
    val filtered_names = mutableListOf<String>()


    override fun onResume() {
        try {
            if(!BuildConfig.DEBUG){
//                checkPaid()
            }
        }catch (e : Exception){
            e.printStackTrace()
        }

        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        preferences = getSharedPreferences("firstime", MODE_PRIVATE)
        preferences = getSharedPreferences("lastVersion", MODE_PRIVATE)
        preferences = getSharedPreferences(MINIMAL, MODE_PRIVATE)

//        Toast.makeText(this, "${bundle!!.getBoolean("min", false)}", Toast.LENGTH_SHORT).show()

        val btnx = findViewById<Button>(R.id.menu_btn)
        btnx.setOnClickListener{
            val intentx = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intentx)
        }

        var vertxt : TextView = findViewById(R.id.version_txt)

        VERSION_NAME = this.packageManager.getPackageInfo(this.packageName,0).versionName

        vertxt.text = "v$VERSION_NAME"

        var lastVersion = preferences.getString("lastVersion", "0").toString()

        MainButtons()
        Arrows()
        SearchBar()
        About()

        selist = lista.toTypedArray()
        select = selist[0]
        if(VERSION_NAME != lastVersion){
            val edit = preferences.edit()
            edit.putBoolean("update", true)
            edit.commit()
            changelog()
        }
        addElement()

        val img : ImageView = findViewById(R.id.logo_banner)
        img.setOnLongClickListener{
            DialogMaker(5)

            return@setOnLongClickListener true
        }
        if(getDarkMode()){
            img.setImageResource(R.drawable.banner_top_white)
        }
    }

    fun addElement(){
        val btn0 : ImageButton = findViewById(R.id.qa0)
        val btn1 : ImageButton = findViewById(R.id.qa1)
        val btn2 : ImageButton = findViewById(R.id.qa2)
        val btn3 : ImageButton = findViewById(R.id.qa3)

        val txt0: TextView = findViewById(R.id.tqa0)
        val txt1: TextView = findViewById(R.id.tqa1)
        val txt2: TextView = findViewById(R.id.tqa2)
        val txt3: TextView = findViewById(R.id.tqa3)

        for (i in 0..3){
            preferences = getSharedPreferences("url$i", MODE_PRIVATE)
//            Toast.makeText(this, preferences.getString("url3", ""), Toast.LENGTH_SHORT).show()
        }
        if(!preferences.getString("url0", "").equals("")){
            txt0.text = searchByURL(preferences.getString("url0", "").toString())
            btn0.setImageResource(R.drawable.ic_baseline_language_24)}
        if(!preferences.getString("url1", "").equals("")){
            txt1.text = searchByURL(preferences.getString("url1", "").toString())
            btn1 .setImageResource(R.drawable.ic_baseline_language_24)
        }
        if(!preferences.getString("url2", "").equals("")){
            txt2.text = searchByURL(preferences.getString("url2", "").toString())
            btn2.setImageResource(R.drawable.ic_baseline_language_24)
        }
        if(!preferences.getString("url3", "").equals("")){
            txt3.text = searchByURL(preferences.getString("url3", "").toString())
            btn3.setImageResource(R.drawable.ic_baseline_language_24)
        }

        btn0.setOnClickListener{
           addMod(0, 0)
            btn0.setImageResource(R.drawable.ic_baseline_language_24)
        }
        btn0.setOnLongClickListener{
            addMod(0, 1)
            return@setOnLongClickListener true
        }
        btn1.setOnClickListener{
            addMod(1, 0)
            btn1.setImageResource(R.drawable.ic_baseline_language_24)
        }
        btn1.setOnLongClickListener{
            addMod(1, 1)
            return@setOnLongClickListener true
        }
        btn2.setOnClickListener{
            addMod(2, 0)
            btn2.setImageResource(R.drawable.ic_baseline_language_24)

        }
        btn2.setOnLongClickListener{
            addMod(2, 1)
            return@setOnLongClickListener true

        }
        btn3.setOnClickListener{
            addMod(3, 0)
            btn3.setImageResource(R.drawable.ic_baseline_language_24)
        }
        btn3.setOnLongClickListener{
            addMod(3, 1)

            return@setOnLongClickListener true
        }

    }
    fun addMod(i : Int, mode : Int){
        val intent = Intent(this@MainActivity, WebViewActivity::class.java)
        if(mode == 0){
            if(preferences.getString("url$i", "").equals("")){
//            DialogMaker(6)
                addDialog(i)

            }else{
                bundle.putString("url", preferences.getString("url$i", "https://www.google.com.cu"))
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }else{
            if(!preferences.getString("url$i", "").equals("")){
//            DialogMaker(6)
                addDialog(i)
            }
        }
    }
    fun addDialog(id : Int){
        val mylist = mutableListOf<String>()
        val tf = mutableListOf<Boolean>()
        for (i in 0..selist.size-1){
            mylist.add(selist[i].name)
            tf.add(false)
        }
        var pos = 0
        var selected : Sitio2
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
        var diag : androidx.appcompat.app.AlertDialog

        dialog.setTitle("Seleccione el sitio")
                .setSingleChoiceItems(mylist.toTypedArray(), 0){dialog, which->
                    when(which){
                        which -> pos = which
                    }
                }
                .setPositiveButton("Seleccionar"){_,_->
                    selected = lista[pos]
                    val edit = preferences.edit()
                    edit.putString("url$id", selected.url)
                    edit.commit()
                    EditQATitle()
//                    Toast.makeText(this, selected.url, Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancelar"){_,_->

                }
                .setCancelable(false)
//        selected = lista[pos]
        diag = dialog.create()
        diag.show()
//        Toast.makeText(this, selected.url, Toast.LENGTH_SHORT).show()
//        return selected
    }

    //La ventana de Acerca De
    fun About(){
        val btn0 : ImageButton = findViewById(R.id.contact0)
        val btn1 : ImageButton = findViewById(R.id.contact1)
        val btn2 : ImageButton = findViewById(R.id.contact2)
        btn0.setOnClickListener {
            val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/mahocomments"))
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

    //las flechas de cerrar y abrir las ventanas
    fun Arrows(){

        val arrows = arrayOf(findViewById(R.id.arrow0),
            findViewById(R.id.arrow1),
            findViewById(R.id.arrow2),
            findViewById(R.id.arrow3),
            findViewById<ImageButton>(R.id.arrow4))
        val layouts = arrayOf(findViewById<LinearLayout>(R.id.Linear0),
            findViewById(R.id.Linear1),
            findViewById(R.id.Linear2),
            findViewById(R.id.Linear3),
            findViewById(R.id.Linear4))

        for (i in arrows.indices){
            val btn = arrows[i]
            val ly = layouts[i]
            btn.setOnClickListener{
                val layoutParams = ly.layoutParams
                if(ly.visibility == VISIBLE){
                    ly.visibility = GONE
                    btn.setBackgroundResource(R.drawable.ic_arrow_down)
//                    layoutParams.setMargins(0,0,0,50)
//                    ly.layoutParams = layoutParams
                }else{
                    TransitionManager.beginDelayedTransition(ly, AutoTransition())
                    ly.visibility = VISIBLE
                    btn.setBackgroundResource(R.drawable.ic_arrow_up)
//                    layoutParams.setMargins(0,0,0,0)
//                    ly.layoutParams = layoutParams
                }
            }
        }
    }

    fun changelog(){
        if(shownUpdate() && !firstTime()){
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            var diag : androidx.appcompat.app.AlertDialog
            if(VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
                val diag = Dialog(this)
                diag.setContentView(R.layout.update_layout_dialog)
                diag.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                diag.setCancelable(false)
                diag.show()
                val btn = diag.findViewById<Button>(R.id.close_update)
                btn.setOnClickListener{
                    diag.dismiss()
                }
            }else{
                dialog.setTitle("Nuevos Cambios")
                        .setMessage(resources.getString(R.string.changelog))
                        .setPositiveButton("Cerrar"){_,_->

                        }
                        .setCancelable(false)
                diag = dialog.create()
                diag.show()
            }
            val edit = preferences.edit()
            edit.putBoolean("update", false)
            edit.putString("lastVersion", VERSION_NAME)
            edit.commit()
        }
    }

    fun changeToDir(c :Context,name: String){
        bundle.putString("category", name)
        val intent = Intent(this@MainActivity, ListActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
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
                val vips = listOf("marielainfante", "MarcosH", "epsilon", "chopper-kun","Chopper-kun", "BLNrt", "AntoineAnigma","Winter_Wolf","winter_wolf","Masi","masi","osmel012", "WonK'","Protoss","C354R","ebcuba","yukkine","Damiamarque","Yaku  s")
                if (vips.contains(user) || ispaid){
                    if(user.equals("Leiney Glez")||user.equals("leineyglez")){
                        DialogMaker(3)
                    }
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
                dialog.setTitle("Apklis no encontrada")
                    .setMessage("Apklis no se encuentra instalado. Por favor descargue e instale la app para comprobar la compra")
                    .setPositiveButton("Cerrar"){_,_->
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask()
                        }else
                            finish()

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
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask()
                        }else
                            finish()
                    }
                    .setIcon(R.drawable.ic_baseline_warning_24)
                    .setCancelable(false)
            }
            3 -> {
                dialog.setTitle("AVISO")
                        .setMessage("El usuario autenticado no ha comprado la app. Si esto es un error diríjase al grupo de atención al cliente")
                        .setNeutralButton("Abrir APKLIS"){_,_->
                            val intent = packageManager.getLaunchIntentForPackage("cu.uci.android.apklis")
                            val chooser = Intent.createChooser(intent, "Launch Apklis")
                            startActivity(chooser)
                        }
                        .setNegativeButton("Abrir Grupo"){_,_->
                            val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/mahocomments"))
                            startActivity(telegram)
                        }
                    .setPositiveButton("Cerrar"){_,_->
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask()
                        }else
                            finish()
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
                dialog.setTitle("Agradecimientos")
                    .setMessage(resources.getString(R.string.agradecimientos))
                    .setPositiveButton("Cerrar"){_,_->

                    }
                    .setCancelable(false)
            }
            6 -> {
                val mylist = mutableListOf<String>()
                val tf = mutableListOf<Boolean>()
                for (i in 0..selist.size-1){
                    mylist.add(selist[i].name)
                    tf.add(false)
                }
                dialog.setTitle("Seleccione el sitio")
                        .setSingleChoiceItems(mylist.toTypedArray(), 0){dialog, which->
                            when(which){
                                which -> select = selist[which]
                            }
                        }
                        .setPositiveButton("Seleccionar"){_,_->
//                            Toast.makeText(this, select.url, Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("Cancelar"){_,_->

                        }
                        .setCancelable(false)
            }
        }
        diag = dialog.create()
        diag.show()
    }

    fun EditQATitle(){
        val txt0: TextView = findViewById(R.id.tqa0)
        val txt1: TextView = findViewById(R.id.tqa1)
        val txt2: TextView = findViewById(R.id.tqa2)
        val txt3: TextView = findViewById(R.id.tqa3)

        txt0.text = searchByURL(preferences.getString("url0", "").toString())
        txt1.text = searchByURL(preferences.getString("url1", "").toString())
        txt2.text = searchByURL(preferences.getString("url2", "").toString())
        txt3.text = searchByURL(preferences.getString("url3", "").toString())
    }

    fun Filter(){
        for(item in filter){
            when(item){
                "descripción" -> for(sitio in lista) if(!filtered_names.contains(sitio.description)) filtered_names.add(sitio.description)

                "comunicaciones" -> for(sitio in lista) if(sitio.comunicaciones == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "culturales"-> for(sitio in lista) if(sitio.culturales == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "entretenimiento"-> for (sitio in lista) if (sitio.entretenimiento == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "radio" -> for(sitio in lista) if(sitio.radio == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "tv"-> for(sitio in lista) if(sitio.tv == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "revista"-> for(sitio in lista) if(sitio.revista == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "periodico"-> for(sitio in lista) if(sitio.periodico == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "medio ambiente"-> for(sitio in lista) if(sitio.medio_ambiente == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "salud"-> for(sitio in lista) if(sitio.salud == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "universidades"-> for(sitio in lista) if(sitio.universidades == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "ministerios"-> for(sitio in lista) if(sitio.ministerios == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "comercio"-> for(sitio in lista) if(sitio.comercio == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "informativos"-> for(sitio in lista) if(sitio.informativos == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "provinciales"-> for(sitio in lista) if(sitio.provinciales == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "educativos"-> for(sitio in lista) if(sitio.educativos == 1) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)

                "libre consumo"-> for(sitio in lista) if(sitio.cost == 0) if(!filtered_names.contains(sitio.name)) filtered_names.add(sitio.name)
            }
            filtered_names.sort()
        }
    }

    fun firstTime(): Boolean{
        val ran : Boolean = preferences.getBoolean("firstime", true)
        return ran
    }

    fun getDarkMode():Boolean{
        val current = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return current == Configuration.UI_MODE_NIGHT_YES
    }

    //Los 4 botones del catalogo
    fun MainButtons(){
        binding.etecsaBtn.setOnClickListener(this)
        binding.cultureBtn.setOnClickListener(this)
        binding.inforBtn.setOnClickListener(this)
        binding.eduBtn.setOnClickListener(this)
        binding.ppmBtn.setOnClickListener(this)
        binding.rtvBtn.setOnClickListener(this)
        binding.uniBtn.setOnClickListener(this)
        binding.tradeBtn.setOnClickListener(this)
    }

    //presionar atras
    override fun onBackPressed() {
        if(cont < 1){
            Toast.makeText(application, "Presione otra vez para salir", Toast.LENGTH_SHORT).show()
            cont++
        }
        else{
            finishAffinity()
        }
    }

    fun resetSearch(){
        adapter.clear()
        filtered_names.clear()
        lista.sortBy { it.name }
        for(site in lista){
            filtered_names.add(site.name)
        }
    }

    //La barra de busqueda
    fun SearchBar(){

        if(VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
//            list_btn = findViewById(R.id.list_btn)
        }
        val cats = resources.getStringArray(R.array.cats)
        setupDB("site")
        chipsGroup2 = findViewById(R.id.filter_group2)
        chipsGroup = findViewById(R.id.filter_group)
        if(VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            hor_lay = findViewById(R.id.horizontal_scroll)
        }
        simple_search = findViewById(R.id.simple_search)
        advanced_search = findViewById(R.id.advanced_search)
        listView = findViewById(R.id.search_list)
        searchView = findViewById(R.id.searcher)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,filtered_names)
        resetSearch()

        simple_search.setOnClickListener{
            resetSearch()
            chipsGroup2.clearCheck()
            chipsGroup2.removeAllViews()
            advanced_search.setBackgroundResource(R.drawable.search_advanced)
            simple_search.setBackgroundResource(R.drawable.search_simple_selected)
            chipsGroup2.visibility = View.GONE
            if(VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
                hor_lay.visibility = View.GONE
//                list_btn.visibility = View.GONE
//                list_btn.text = "Todas"
            }
            adapter.addAll(filtered_names)
            advanced_search.isEnabled = true
            simple_search.isEnabled = false
        }

        advanced_search.setOnClickListener{
//            setupChips(cats)
            adapter.clear()
            filtered_names.clear()
            simple_search.setBackgroundResource(R.drawable.search_simple)
            advanced_search.setBackgroundResource(R.drawable.search_advanced_selected)
            if(VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            }
            chipsGroup2.visibility = View.VISIBLE
            advanced_search.isEnabled = false
            simple_search.isEnabled = true
            setupChips(cats)
        }
            listView.adapter = adapter

            listView.setOnItemClickListener{parent, view, position, id ->
            val intent = Intent(this@MainActivity, WebViewActivity::class.java)
            url = searchSiteURL(parent.getItemAtPosition(position).toString())
                val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                var diag : androidx.appcompat.app.AlertDialog
                val s = searchFullSite(parent.getItemAtPosition(position).toString())
                dialog.setTitle("Nombre: ${s[0]}")
                    .setMessage("Descripción: ${s[1]}\n\nDirección: ${s[3]} \n\nConsume: ${s[2]} \n\nSección: ${s[4]}")
                    .setPositiveButton("Abrir Ext."){_,_->
                        val inte = Intent(Intent.ACTION_VIEW, Uri.parse(s[3]))
                        startActivity(inte)
                    }
                    .setNeutralButton("Abrir Aquí"){_,_->
                        bundle.putString("url", s[3])
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                diag = dialog.create()
                diag.show()
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

    fun setupChips(cats: Array<String>){
        for (cat in cats) {
            var chip = Chip(chipsGroup2.context)
            chip.text = cat
            chip.isCheckable = true
            if(chipsGroup2.visibility == View.VISIBLE) {
                chip.setOnCheckedChangeListener { _, _ ->
                    adapter.clear()
//                Toast.makeText(this, "${chip.text}", Toast.LENGTH_SHORT).show()
                    if (!filter.contains(chip.text.toString())) {
                        filter.add(chip.text.toString())
                    } else {
                        filter.remove(chip.text.toString())
                    }
                    if (!filter.isEmpty()) {
                        Filter()
                    }
                }
            }
            chipsGroup2.addView(chip)
        }
        if(filtered_names.size > 0) {
            val safe = filtered_names.distinct()
            adapter.addAll(safe)
        }
    }

    fun searchByURL(url : String) : String{
        var s : String = ""
//        if(name.length>37){
        for (site in lista) {
            if (url.equals(site.url))
                s = site.name
        }
        return s
    }

    //Buscar el sitio y devolver su URL
    fun searchSiteURL(name:String) : String{
        var s : String = ""
//        if(name.length>37){
             for (site in lista) {
                 if (name.equals(site.description))
                     s = site.url
                 else if(name.equals(site.description))
                     s = site.url
             }
        return s
    }

    fun searchFullSite(name : String): MutableList<String>{
        var s = mutableListOf<String>()
        for (site in lista) {
            if (name.equals(site.name)) {
                s.add(site.name)
                s.add(site.description)
                if (site.cost == 0)
                    s.add("No")
                else
                    s.add("Si")
                s.add(site.url)
                s.add(site.category)

            }
            else if(name.equals(site.description)){
                s.add(site.name)
                s.add(site.description)
                if (site.cost == 0)
                    s.add("No")
                else
                    s.add("Si")
                s.add(site.url)
                s.add(site.category)
            }
        }
        return s
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

    //Preparar la base de datos
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
                var comunicaciones = data.getString(data.getColumnIndex("comunicaciones")).toInt()
                var culturales= data.getString(data.getColumnIndex("culturales")).toInt()
                var entretenimiento= data.getString(data.getColumnIndex("entretenimiento")).toInt()
                var radio= data.getString(data.getColumnIndex("radio")).toInt()
                var tv= data.getString(data.getColumnIndex("tv")).toInt()
                var revista= data.getString(data.getColumnIndex("revista")).toInt()
                var periodico= data.getString(data.getColumnIndex("periodico")).toInt()
                var medio_ambiente= data.getString(data.getColumnIndex("medio_ambiente")).toInt()
                var salud= data.getString(data.getColumnIndex("salud")).toInt()
                var universidades= data.getString(data.getColumnIndex("universidades")).toInt()
                var ministerios= data.getString(data.getColumnIndex("ministerios")).toInt()
                var comercio= data.getString(data.getColumnIndex("comercio")).toInt()
                var informativos= data.getString(data.getColumnIndex("informativos")).toInt()
                var provinciales= data.getString(data.getColumnIndex("provinciales")).toInt()
                var educativos= data.getString(data.getColumnIndex("educativos")).toInt()
                sitio = Sitio2(id,cat,cost,name,url,desc,comunicaciones, culturales, entretenimiento, radio, tv, revista, periodico, medio_ambiente, salud, universidades, ministerios, comercio, informativos, provinciales, educativos)
                lista.add(sitio)
            }while (data.moveToNext())
        }
    }

    fun shownUpdate(): Boolean{
        val ran : Boolean = preferences.getBoolean("update", true)
        return ran
    }

    inner class ApklisNotLoggedInException : Exception()

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.etecsa_btn->{
                changeToDir(this, "Redes y Telecomunicaciones")
            }
            R.id.culture_btn->{
                changeToDir(this, "Culturales y Entretenimiento")
            }
            R.id.infor_btn->{
                changeToDir(this, "Informativos")
            }
            R.id.edu_btn->{
                changeToDir(this, "Investigativos/Educativos")
            }
            R.id.ppm_btn->{
                changeToDir(this, "Periódicos y Revistas")
            }
            R.id.rtv_btn->{
                changeToDir(this, "Radio y Televisión")
            }
            R.id.uni_btn->{
                changeToDir(this, "Universidades")
            }
            R.id.trade_btn->{
                changeToDir(this, "Comercio Electrónico")
            }
        }
    }

}