package com.fluenso.videoplayer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fluenso.videoplayer.R
import com.fluenso.videoplayer.models.Subtitle
import com.fluenso.videoplayer.utils.boldText
import com.fluenso.videoplayer.utils.normalText
import com.fluenso.videoplayer.utils.setColor
import com.fluenso.videoplayer.utils.toSeconds
import kotlinx.android.synthetic.main.item_layout_subtitle.view.*

class SubtitleAdapter(
    private val subtitles: ArrayList<Subtitle>,
    private val onSubtitleSelected: ((startTimeMillis: Long, endTimeMillis: Long) -> Unit)?
) : RecyclerView.Adapter<SubtitleAdapter.SubtitleViewHolder>() {

    private var selectedPosition = -1

    private var startTimePositionMap: HashMap<Int, Int> = hashMapOf()

    init {
        subtitles.forEachIndexed { index, subtitle ->
            val seconds: Int = subtitle.startTimeMillis.toInt().toSeconds()
            startTimePositionMap[seconds] = index
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtitleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_subtitle, parent, false)
        return SubtitleViewHolder(view)
    }

    override fun getItemCount(): Int = subtitles.size

    override fun onBindViewHolder(holder: SubtitleViewHolder, position: Int) {
        holder.tvSubtitle.text = subtitles[position].text

        handleSubtitleHighlight(holder.tvSubtitle, (position == selectedPosition))

        holder.tvSubtitle.setOnClickListener {
            selectedPosition = position
            handleSubtitleHighlight(holder.tvSubtitle, true)
            notifyDataSetChanged()
            val subtitle = getItem(position)
            onSubtitleSelected?.invoke(subtitle.startTimeMillis, subtitle.endTimeMillis)
        }
    }

    fun getItem(position: Int): Subtitle = subtitles[position]

    fun getScrollPosition(seconds: Int): Int? = startTimePositionMap[seconds]

    fun goToSubtitle(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }

    private fun handleSubtitleHighlight(tvSubtitle: TextView, isSelected: Boolean) {
        if (isSelected) {
            tvSubtitle.boldText()
            tvSubtitle.setColor(tvSubtitle.context, R.color.colorBlack)
        } else {
            tvSubtitle.normalText()
            tvSubtitle.setColor(tvSubtitle.context, R.color.grayColor)
        }
    }

    class SubtitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSubtitle: TextView = itemView.txt_subtitle
    }
}