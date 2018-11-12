package me.rohanpeshkar.videoassgn.ui

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_layout_subtitle.view.*
import me.rohanpeshkar.videoassgn.R
import me.rohanpeshkar.videoassgn.models.Subtitle
import java.util.concurrent.TimeUnit

/**
 * Adapter class to show subtitles in recyclerview
 * Accepts List of subtitles and a callback from Click event
 */
class SubtitleAdapter(
    private val mSubtitles: ArrayList<Subtitle>,
    val onSubtitleSelected: (startTimeMillis: Long?, endTimeMillis: Long?) -> Unit
) : RecyclerView.Adapter<SubtitleAdapter.SubtitleViewHolder>() {

    private var mSelectedPosition = -1

    private var mStartTimePositionMap: HashMap<Int, Int> = hashMapOf()

    /**
     * Creating hashmap with starttime as key and position as a value
     */
    init {
        mSubtitles.forEachIndexed { index, subtitle ->
            subtitle.startTimeMillis?.let {
                val seconds: Int = TimeUnit.MILLISECONDS.toSeconds(it).toInt()
                mStartTimePositionMap[seconds] = index
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtitleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_subtitle, parent, false)
        return SubtitleViewHolder(view)
    }

    override fun getItemCount(): Int = mSubtitles.size

    override fun onBindViewHolder(holder: SubtitleViewHolder, position: Int) {
        holder.itemView.txt_subtitle.text = mSubtitles[position].text
        setSelected(holder, position)

        holder.itemView.txt_subtitle.setOnClickListener {
            mSelectedPosition = position
            setSelected(holder, position)
            notifyDataSetChanged()
            val subtitle = getItem(position)
            val startTimeMillis = subtitle.startTimeMillis
            val endTimeMillis = subtitle.endTimeMillis
            onSubtitleSelected(startTimeMillis, endTimeMillis)
        }
    }

    fun getItem(position: Int): Subtitle = mSubtitles[position]

    /**
     * Method to update select stat for subtitle
     */
    private fun setSelected(holder: SubtitleViewHolder, position: Int) {
        if (position == mSelectedPosition) {
            holder.itemView.txt_subtitle.setTypeface(holder.itemView.txt_subtitle.typeface, Typeface.BOLD)
            holder.itemView.txt_subtitle.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.colorBlack)
            )

        } else {
            holder.itemView.txt_subtitle.typeface = Typeface.DEFAULT
            holder.itemView.txt_subtitle.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.grayColor
                )
            )
        }
    }


    //Method to get position from seconds
    fun getPositionFromSeconds(seconds: Int): Int? {
        return mStartTimePositionMap[seconds]
    }

    //Scroll to page once subtitle is invisible
    fun scrollTo(position: Int) {
        mSelectedPosition = position
        notifyDataSetChanged()
    }

    inner class SubtitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}