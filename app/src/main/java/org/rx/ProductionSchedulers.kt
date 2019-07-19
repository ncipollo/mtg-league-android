package org.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductionSchedulers: MagicSchedulers {
    override val computation = Schedulers.computation()
    override val io = Schedulers.io()
    override val main: Scheduler = AndroidSchedulers.mainThread()
}
