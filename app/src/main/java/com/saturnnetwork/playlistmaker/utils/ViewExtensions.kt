package com.saturnnetwork.playlistmaker.utils

import android.view.View
import kotlin.collections.forEach

fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.INVISIBLE }
fun View.gone() { visibility = View.GONE }

fun List<View>.show() = forEach { it.show() }
fun List<View>.hide() = forEach { it.hide() }
fun List<View>.gone() = forEach { it.gone() }