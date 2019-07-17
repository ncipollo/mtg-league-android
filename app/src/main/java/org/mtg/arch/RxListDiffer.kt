package org.mtg.arch

import androidx.recyclerview.widget.DiffUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RxListDiffer {
    fun <T> calculateDiff(
        oldItems: List<T>,
        newItems: List<T>,
        itemCallback: DiffUtil.ItemCallback<T>
    ): Single<DiffUtil.DiffResult> {
        val callback = createDiffCallback(oldItems, newItems, itemCallback)
        return Single.defer { Single.just(DiffUtil.calculateDiff(callback)) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun <T> createDiffCallback(
        oldItems: List<T>,
        newItems: List<T>,
        itemCallback: DiffUtil.ItemCallback<T>
    ) = object : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            itemCallback.areItemsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])

        override fun getOldListSize() = oldItems.size

        override fun getNewListSize() = newItems.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            itemCallback.areContentsTheSame(
                oldItems[oldItemPosition],
                newItems[newItemPosition]
            )
    }
}
