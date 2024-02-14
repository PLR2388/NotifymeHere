package fr.wonderfulappstudio.notifymehere.presentation.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.convertMillisToDate(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}