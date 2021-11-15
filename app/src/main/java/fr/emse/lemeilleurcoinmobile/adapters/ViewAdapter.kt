package fr.emse.lemeilleurcoinmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.emse.lemeilleurcoinmobile.R
import fr.emse.lemeilleurcoinmobile.dto.ViewDto
import fr.emse.lemeilleurcoinmobile.dto.ViewStatus

class ViewAdapter: RecyclerView.Adapter<ViewAdapter.ViewHolder>() {
    val GOOD = 1
    val BAD  = 0
    private val items = mutableListOf<ViewDto>()
    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        var title: TextView
        var description: TextView

        init {
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
        }
    }
    fun update(views: List<ViewDto>) {  // (4)
        items.clear()
        items.addAll(views)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v:View

        if ( viewType == GOOD)
            v = LayoutInflater.from(parent.context).inflate(R.layout.good_view_card_layout,parent,false)
        else
            v = LayoutInflater.from(parent.context).inflate(R.layout.bad_view_card_layout,parent,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = items[position]
        holder.apply {
            title.text = view.emailViewer
            description.text = view.description
        }
    }
    override fun getItemViewType(position: Int): Int {
        if(items.get(position).viewStatus == ViewStatus.GOOD)
            return GOOD
        else
            return BAD
    }

    override fun getItemCount(): Int = items.size
}