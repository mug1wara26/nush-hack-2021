package com.example.nush_hack21.model

enum class GreenGrade {
    GOOD, MODERATE, NEFARIOUS, UNDEFINED;

    companion object {
        fun gradeScore(score: Int) : GreenGrade {
            return when {
                score >= 75 -> GOOD
                score >= 50 -> MODERATE
                score >= 0 -> NEFARIOUS
                else -> UNDEFINED
            }
        }
        val greenGradeColors = mapOf((MODERATE to "#FFC300"), (GOOD to "#41B883"),(NEFARIOUS to "#ff6347"),(UNDEFINED to "#80808"))
    }
}