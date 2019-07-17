package org.mtg.arch

import android.view.ViewGroup

interface ItemViewHolderFactory {
    fun createViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<*>
    fun getViewType(item: ItemViewState) : Int
}

fun itemViewHolderFactory(block: ItemViewHolderFactoryBuilder.() -> Unit): ItemViewHolderFactory {
    val builder = ItemViewHolderFactoryBuilder()
    builder.apply(block)
    return MappedViewHolderFactory(builder.build())
}

class ItemViewHolderFactoryBuilder {
    val creators: MutableList<Pair<Class<out ItemViewState>, ViewHolderCreator<*>>> =
        mutableListOf()

    inline fun <reified T : ItemViewState> viewHolder(noinline creator: ViewHolderCreator<T>) {
        creators += (T::class.java to creator)
    }

    fun build() = creators.toList()
}

typealias ViewHolderCreator<T> = (ViewGroup) -> ItemViewHolder<T>

private class MappedViewHolderFactory(
    creators: List<Pair<Class<out ItemViewState>, ViewHolderCreator<*>>>
) : ItemViewHolderFactory {

    private val stateClassToViewType: Map<Class<out ItemViewState>, Int>
    private val viewTypeToCreator: Map<Int, ViewHolderCreator<*>>

    init {
        stateClassToViewType = creators.mapIndexed { index, (clazz, _) -> clazz to index }.toMap()
        viewTypeToCreator = creators.mapIndexed { index, (_, creator) -> index to creator }.toMap()
    }

    override fun createViewHolder(parent: ViewGroup, viewType: Int) =
        viewTypeToCreator[viewType]?.let { it((parent)) }
            ?: throw IllegalArgumentException("No view holder for view type: $viewType")

    override fun getViewType(item: ItemViewState) = stateClassToViewType[item.javaClass]
        ?: throw IllegalArgumentException("No view type for state: ${item.javaClass}")

}
