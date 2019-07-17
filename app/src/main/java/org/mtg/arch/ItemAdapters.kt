package org.mtg.arch

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.Disposable

class ItemAdapter(private val factory: ItemViewHolderFactory) :
    RecyclerView.Adapter<ItemViewHolder<*>>() {
    private val items: MutableList<ItemViewState> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<*> =
        factory.createViewHolder(parent, viewType)

    override fun getItemViewType(position: Int): Int = factory.getViewType(items[position])

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder<*>, position: Int) =
        holder.bindItem(items[position])

    fun notifyItemsChanged(items: List<ItemViewState>) {
        this.items.clear()
        this.items += items
        notifyDataSetChanged()
    }
}

open class ItemListAdapter(private val factory: ItemViewHolderFactory) :
    RecyclerView.Adapter<ItemViewHolder<*>>() {
    private val itemCallback = ItemDiffCallback()
    private val items: MutableList<ItemViewState> = ArrayList()

    private var disposable: Disposable? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<*> =
        factory.createViewHolder(parent, viewType)

    override fun getItemViewType(position: Int): Int = factory.getViewType(items[position])

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder<*>, position: Int) =
        holder.bindItem(items[position])

    open fun submitList(newItems: List<ItemViewState>, onListUpdated: () -> Unit = {}) {
        disposable?.dispose()
        when {
            newItems.isEmpty() -> {
                simpleRemove(onListUpdated)
            }
            items.isEmpty() -> {
                simpleAdd(newItems, onListUpdated)
            }
            else -> {
                disposable = RxListDiffer.calculateDiff(items, newItems, itemCallback)
                    .subscribe { result ->
                        applyDiffResult(
                            newItems,
                            result,
                            callback = onListUpdated
                        )
                    }
            }
        }
    }

    private fun simpleRemove(onListUpdated: () -> Unit) {
        val itemsRemoved = items.size
        items.clear()
        notifyItemRangeRemoved(0, itemsRemoved)
        onListUpdated()
    }

    private fun simpleAdd(newItems: List<ItemViewState>, onListUpdated: () -> Unit = {}) {
        this.items.addAll(newItems)
        notifyItemRangeInserted(0, newItems.size)
        onListUpdated()
    }

    private fun applyDiffResult(
        items: List<ItemViewState>,
        diffResult: DiffUtil.DiffResult,
        callback: () -> Unit
    ) {
        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
        callback()
    }
}

private class ItemDiffCallback : DiffUtil.ItemCallback<ItemViewState>() {
    override fun areItemsTheSame(oldItem: ItemViewState, newItem: ItemViewState) =
        oldItem isSameAs newItem

    override fun areContentsTheSame(oldItem: ItemViewState, newItem: ItemViewState) =
        oldItem contentsSameAs newItem
}
