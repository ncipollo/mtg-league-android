package org.rx

import io.reactivex.Scheduler

interface MagicSchedulers {
    val computation: Scheduler
    val io: Scheduler
    val main: Scheduler
}
