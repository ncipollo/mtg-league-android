package org.mtg.rx

import io.reactivex.schedulers.Schedulers
import org.rx.MagicSchedulers

class TestSchedulers: MagicSchedulers {
    override val computation = Schedulers.trampoline()
    override val io = Schedulers.trampoline()
    override val main = Schedulers.trampoline()
}
