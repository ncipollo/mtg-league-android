package org.mtg.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

inline fun <reified T : ViewModel> FragmentActivity.activityViewModel() =
    ViewModelProviders.of(this).get(T::class.java)

inline fun <reified T : ViewModel> Fragment.activityViewModel() =
    requireActivity().activityViewModel<T>()
