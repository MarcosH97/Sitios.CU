package com.mahostudios.sitioscu

import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlin.coroutines.coroutineContext

class RecyclerAdapter(private var context: Context, private var sites: List<Sitio>, private var cat: String):
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(item : View):RecyclerView.ViewHolder(item){

        var myInterface : MyInterface = context as MyInterface
        val webtn : ImageButton = item.findViewById(R.id.web_btn)
        val sharebtn : ImageButton = item.findViewById(R.id.share_btn)
        val costimg : ImageView = item.findViewById(R.id.cost_img)
        val barbtn : Button = item.findViewById(R.id.barbtn)
        val down : ImageView = item.findViewById(R.id.down_img)

        val cardView : CardView = item.findViewById(R.id.card)
        val img : ImageView = item.findViewById(R.id.img)
        val itemTitle : TextView = item.findViewById(R.id.page_http)
        val itemDesc : TextView = item.findViewById(R.id.page_desc)
        init {
//            itemView.setOnClickListener{ v : View ->
//                val pos : Int = adapterPosition
//            }
            barbtn.setOnClickListener{v :View ->
                if(itemDesc.visibility == View.VISIBLE){
                    itemDesc.visibility = View.GONE

                    barbtn.text = "mostrar más"
                }else{
                    TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                    itemDesc.visibility = View.VISIBLE
                    barbtn.text = "mostrar menos"
                }
            }
            webtn.setOnClickListener{v : View ->
//                val dialog = AlertDialog.Builder(context)
//                dialog.setTitle("Aviso")
//                dialog.setMessage("Desea abrir esta página?")
//                dialog.setCancelable(false)
//                dialog.setPositiveButton("Si"){_,_->
//                    myInterface.onMethodCallback(getLink(itemTitle.text.toString()))
//                }
//                dialog.setNegativeButton("No"){_,_->
//                }
//                val diag = dialog.create()
//                diag.show()
//                diag.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY)
//                diag.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY)
                val dialog = Dialog(context)
                dialog.setContentView(R.layout.loading_dialog)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val showbtn : ImageButton = dialog.findViewById(R.id.cancel_btn)
                val progbar : ProgressBar = dialog.findViewById(R.id.progress_circ)
                var prog = 0
                var cancelable = false
                dialog.show()
                Thread{
                    while (prog < 100){
                        prog += 1
                        try {
                            Thread.sleep(15)
                            progbar.progress = prog
                            if (prog == 100){
                                myInterface.onMethodCallback(getLink(itemTitle.text.toString()))
                                dialog.dismiss()
                            }
                            if(cancelable){
                                return@Thread
                            }
                        }catch (e : InterruptedException){
                            e.printStackTrace()
                        }
                    }
                }.start()

                showbtn.setOnClickListener{
                    cancelable = true
                    dialog.dismiss()
                }

            }

            itemTitle.setOnClickListener{
                    myInterface.copyToClipboard(getLink(itemTitle.text.toString()))
            }
            sharebtn.setOnClickListener{
//                val dialog = AlertDialog.Builder(context)
//                .setTitle("Aviso")
//                .setMessage("Desea abrir la página en otro navegador?")
//                .setCancelable(false)
//                .setPositiveButton("Si"){_,_->
//                    myInterface.shareLink(getLink(itemTitle.text.toString()))
//                }
//                dialog.setNegativeButton("No"){_,_->
//
//                }
//                val diag = dialog.create()
//                diag.show()
                val dialog = Dialog(context)
                dialog.setContentView(R.layout.loading_dialog)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val showbtn : ImageButton = dialog.findViewById(R.id.cancel_btn)
                val progbar : ProgressBar = dialog.findViewById(R.id.progress_circ)
                var prog = 0
                var cancelable = false
                dialog.show()
                Thread{
                    while (prog < 100){
                        prog += 1
                        try {
                            Thread.sleep(15)
                            progbar.progress = prog
                            if (prog == 100){
                                myInterface.shareLink(getLink(itemTitle.text.toString()))
                                dialog.dismiss()
                            }
                            if(cancelable){
                                return@Thread
                            }
                        }catch (e : InterruptedException){
                            e.printStackTrace()
                        }
                    }
                }.start()

                showbtn.setOnClickListener{
                    cancelable = true
                    dialog.dismiss()
                }
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.items_list, parent, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        when(cat){
            "ETECSA" ->{
                holder.img.setBackgroundResource(R.drawable.gradient_bg2)
            }
            "Culturales_Entretenimiento" ->{
                holder.img.setBackgroundResource(R.drawable.gradient_bg3)
            }
            "Informativos" ->{
                holder.img.setBackgroundResource(R.drawable.gradient_bg4)
            }
            "Investigativos_Educativos" ->{
                holder.img.setBackgroundResource(R.drawable.gradient_bg5)
            }
        }
        val site : Sitio = sites.get(position)
        if(site.cost == 0)
            holder.costimg.setBackgroundResource(R.drawable.ic_nocost)
        holder.itemTitle.text = site.name
        holder.itemDesc.text = site.description
        holder.down.visibility = View.GONE
//        if(site.down == 1){
//            holder.down.visibility = View.VISIBLE
//        }
    }
    override fun getItemCount(): Int {
        return sites.size
    }

    fun getLink(name:String):String{
        var link : String = ""
        for(site in sites){
            if(site.name.equals(name))
                link = site.url
        }
        return link
    }
    fun dialogBuilder(s : String): Boolean{
        var b = false
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Aviso")
        dialog.setMessage(s)
        dialog.setCancelable(false)
        dialog.setPositiveButton("Si"){_,_->

        }
        dialog.setNegativeButton("No"){_,_->

        }
        val diag = dialog.create()
        diag.show()
        return b
    }

    interface MyInterface{
        fun onMethodCallback(url: String)
        fun shareLink(url: String)
        fun copyToClipboard(url: String)
    }

}