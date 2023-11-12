package fr.wonderfulappstudio.notifymehere.extension

import java.text.SimpleDateFormat
import java.util.Date

fun Long.convertMillisToDate(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(Date(this))
}