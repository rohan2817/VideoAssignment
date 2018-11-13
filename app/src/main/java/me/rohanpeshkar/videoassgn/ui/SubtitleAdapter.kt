package me.rohanpeshkar.videoassgn.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_layout_subtitle.view.*
import me.rohanpeshkar.videoassgn.R
import me.rohanpeshkar.videoassgn.models.Subtitle
import me.rohanpeshkar.videoassgn.utils.*

/**
 * Adapter class to show subtitles in recyclerview
 * Accepts List of subtitles and a callback from Click event
 */
class SubtitleAdapter(
    private val mSubtitles: ArrayList<Subtitle>,
    private val mLinearLayoutManager: LinearLayoutManager,
    val onSubtitleSelected: (startTimeMillis: Long, endTimeMillis: Long) -> Unit
) : RecyclerView.Adapter<SubtitleAdapter.SubtitleViewHolder>() {

    private var mSelectedPosition = -1

    private var mStartTimePositionMap: HashMap<Int, Int> = hashMapOf()

    /**
     * Creating hashmap with starttime as key and position as a value
     */
    init {
        mSubtitles.forEachIndexed { index, subtitle ->
            val seconds: Int =  subtitle.startTimeMillis.toInt().toSeconds()
            mStartTimePositionMap[seconds] = index
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtitleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_subtitle, parent, false)
        return SubtitleViewHolder(view)
    }

    override fun getItemCount(): Int = mSubtitles.size

    override fun onBindViewHolder(holder: SubtitleViewHolder, position: Int) {
        holder.tvSubtitle.text = mSubtitles[position].text

        handleSubtitleHighlight(holder.tvSubtitle, (position == mSelectedPosition))

        holder.tvSubtitle.setOnClickListener {
            mSelectedPosition = position
            handleSubtitleHighlight(holder.tvSubtitle, true)
            notifyDataSetChanged()
            val subtitle = getItem(position)
            onSubtitleSelected(subtitle.startTimeMillis, subtitle.endTimeMillis)
        }
    }

    fun getItem(position: Int): Subtitle = mSubtitles[position]

    /**
     * Method to update select stat for subtitle
     */
    private fun handleSubtitleHighlight(tvSubtitle: TextView, isSelected: Boolean) {
        if (isSelected) {
            tvSubtitle.boldText()
            tvSubtitle.setColor(tvSubtitle.context, R.color.colorBlack)
        } else {
            tvSubtitle.normalText()
            tvSubtitle.setColor(tvSubtitle.context, R.color.grayColor)
        }
    }

    //Scroll to subtitle corresponding to seconds
    fun scrollToSeconds(seconds: Int) {
        mStartTimePositionMap[seconds]?.let {
            mSelectedPosition = it
            notifyDataSetChanged()
            mLinearLayoutManager.scrollToPositionWithOffset(it, SUBTITLE_SCROLL_OFFSET.px)
        }
    }

    class SubtitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSubtitle: TextView = itemView.txt_subtitle
    }
}