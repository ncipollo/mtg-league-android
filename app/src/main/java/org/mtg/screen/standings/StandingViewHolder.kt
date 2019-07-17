package org.mtg.screen.standings

import android.view.ViewGroup
import kotlinx.android.synthetic.main.standing_view_item.view.*
import kotlinx.android.synthetic.main.standing_view_item_week.view.*
import org.mtg.R
import org.mtg.arch.ItemViewHolder
import org.mtg.arch.ItemViewState


class StandingViewHolder(parent: ViewGroup) : ItemViewHolder<StandingViewItem>(parent, R.layout.standing_view_item) {

    private val rank = itemView.standing_rank
    private val name = itemView.standing_name
    private val record = itemView.standing_record
    private val location = itemView.standing_location
    private val weekOneRecord = itemView.standing_week_one.standing_weekly_record
    private val weekTwoRecord = itemView.standing_week_two.standing_weekly_record
    private val weekThreeRecord = itemView.standing_week_three.standing_weekly_record
    private val weekFourRecord = itemView.standing_week_four.standing_weekly_record


    override fun onBindItem(item: StandingViewItem) {
        rank.text = item.rank
        name.text = item.name
        record.text = item.overallRecord
        location.text = item.location
        weekOneRecord.text = item.weekOneRecord
        weekTwoRecord.text = item.weekTwoRecord
        weekThreeRecord.text = item.weekThreeRecord
        weekFourRecord.text = item.weekFourRecord
    }
}

data class StandingViewItem(
    val overallRecord: String,
    val rank: String,
    val name: String,
    val location: String,
    val weekOneRecord: String,
    val weekTwoRecord: String,
    val weekThreeRecord: String,
    val weekFourRecord: String
) : ItemViewState
