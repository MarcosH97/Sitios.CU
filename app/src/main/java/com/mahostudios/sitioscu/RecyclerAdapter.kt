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

class RecyclerAdapter(private var context: Context, private var sites: List<Sitio2>, private var cat: String):
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
                val dialog = Dialog(context)
                dialog.setContentView(R.layout.loading_dialog)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setCancelable(false)
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

            when (cat) {
                "ETECSA" -> {
                    if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.M) {
                        holder.img.setBackgroundResource(R.drawable.gradient_bg2)
                    }else if(android.os.Build.VERSION.SDK_INT==android.os.Build.VERSION_CODES.LOLLIPOP) {
                        holder.img.setBackgroundResource(R.color.navy_blue)
                    }else {
                        holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.navy_blue))
                    }
                }
                "Culturales y Entretenimiento" -> {
                    if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.M) {
                        holder.img.setBackgroundResource(R.drawable.gradient_bg3)
                    }else if(android.os.Build.VERSION.SDK_INT==android.os.Build.VERSION_CODES.LOLLIPOP) {
                        holder.img.setBackgroundResource(R.color.magenta_600)
                    }else {
                        holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.magenta_600))
                    }
                }
                "Informativos" -> {
                    if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.M) {
                        holder.img.setBackgroundResource(R.drawable.gradient_bg4)
                    }else if(android.os.Build.VERSION.SDK_INT==android.os.Build.VERSION_CODES.LOLLIPOP) {
                        holder.img.setBackgroundResource(R.color.orange_600)
                    }else {
                        holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.orange_600))
                    }
                }
                "Investigativos/Educativos" -> {
                    if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.M) {
                        holder.img.setBackgroundResource(R.drawable.gradient_bg5)
                    }else if(android.os.Build.VERSION.SDK_INT==android.os.Build.VERSION_CODES.LOLLIPOP) {
                        holder.img.setBackgroundResource(R.color.green_600)
                    }else {
                        holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.green_600))
                    }
                }
                "Periódicos y Revistas" -> {
                    if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.M) {
                        holder.img.setBackgroundResource(R.drawable.gradient_bg7)
                    }else if(android.os.Build.VERSION.SDK_INT==android.os.Build.VERSION_CODES.LOLLIPOP) {
                        holder.img.setBackgroundResource(R.color.yellow_600)
                    }else {
                        holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.yellow_600))
                    }
                }
                "Radio y Televisión" -> {
                    if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.M) {
                        holder.img.setBackgroundResource(R.drawable.gradient_bg6)
                    }else if(android.os.Build.VERSION.SDK_INT==android.os.Build.VERSION_CODES.LOLLIPOP) {
                        holder.img.setBackgroundResource(R.color.red_600)
                    }else {
                        holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.red_600))
                    }
                }
                "Universidades" -> {
                    if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.M) {
                        holder.img.setBackgroundResource(R.drawable.gradient_bg8)
                    }else if(android.os.Build.VERSION.SDK_INT==android.os.Build.VERSION_CODES.LOLLIPOP) {
                        holder.img.setBackgroundResource(R.color.cyan_600)
                    }else {
                        holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.cyan_600))
                    }
                }
                "Comercio Electrónico" -> {
                    if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.M) {
                        holder.img.setBackgroundResource(R.drawable.gradient_bg9)
                    }else if(android.os.Build.VERSION.SDK_INT==android.os.Build.VERSION_CODES.LOLLIPOP) {
                        holder.img.setBackgroundResource(R.color.coldgray_600)
                    }else {
                        holder.cardView.setCardBackgroundColor(context.resources.getColor(R.color.coldgray_600))
                    }
                }
            }
        val site: Sitio2 = sites.get(position)
        if (site.cost == 0)
            holder.costimg.setBackgroundResource(R.drawable.ic_nocost)
        holder.itemTitle.text = site.name
        holder.itemDesc.text = site.description
        holder.down.visibility = View.GONE
    }
    override fun getItemCount(): Int {
        return sites.size
    }

    fun getLink(name:String):String{
        var link = ""
        for(site in sites){
            if(site.name.equals(name))
                link = site.url
        }
        return link
    }
    interface MyInterface{
        fun onMethodCallback(url: String)
        fun shareLink(url: String)
        fun copyToClipboard(url: String)
    }

}