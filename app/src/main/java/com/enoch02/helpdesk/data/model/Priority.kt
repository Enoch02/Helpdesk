package com.enoch02.helpdesk.data.model

enum class Priority {
    LOW,
    MEDIUM,
    HIGH;

    fun stringify(): String = this.name
}

fun String.toPriority(): Priority {
    return when (this) {
        "LOW" -> {
            Priority.LOW
        }

        "MEDIUM" -> {
            Priority.MEDIUM
        }

        "HIGH" -> {
            Priority.HIGH
        }

        else -> {
            Priority.LOW
        }
    }
}