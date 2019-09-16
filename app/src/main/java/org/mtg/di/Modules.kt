package org.mtg.di

import org.koin.core.module.Module
import org.koin.core.qualifier.named

interface Modules {
    val modules: List<Module>
}

inline fun <reified T : Modules> dependencyName(name: String) =
    named("${T::class.java.name}_$name")
