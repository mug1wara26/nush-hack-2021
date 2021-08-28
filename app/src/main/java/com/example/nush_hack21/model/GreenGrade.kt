package com.example.nush_hack21.model

enum class GreenGrade {
    GOOD, MODERATE, QUESTIONABLE, UNDEFINED;

    companion object {
        fun gradeScore(score: Int) : GreenGrade {
            return when {
                score >= 90 -> GOOD
                score >= 70 -> MODERATE
                score >= 0 -> QUESTIONABLE
                else -> UNDEFINED
            }
        }
        val greenGradeColors = mapOf((MODERATE to "#FFC300"), (GOOD to "#41B883"),(QUESTIONABLE to "#ff6347"),(UNDEFINED to "#80808"))
    }
}