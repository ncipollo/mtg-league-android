package org.mtg.arch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class ItemViewHolder<in T : ItemViewState>(
    parent: ViewGroup,
    @LayoutRes layout: Int,
    private val actionListener: ItemActionListener = {}
) : RecyclerView.ViewHolder(inflateLayout(parent, layout)) {
    fun sendActionForItem(item: T, actionArgument: Any? = null) =
        actionListener(ItemAction(item, actionArgument))

    @Suppress("UNCHECKED_CAST")
    fun bindItem(item: ItemViewState) = onBindItem(item as T)

    protected abstract fun onBindItem(item: T)
}

interface ItemViewState {
    infix fun isSameAs(otherItem: ItemViewState) = this == otherItem
    infix fun contentsSameAs(otherItem: ItemViewState) = this == otherItem
}

data class ItemAction(val item: ItemViewState, val actionArgument: Any? = null)

typealias ItemActionListener = (ItemAction) -> Unit

private fun inflateLayout(parent: ViewGroup, @LayoutRes layout: Int) =
    LayoutInflater.from(parent.context).inflate(layout, parent, false)
