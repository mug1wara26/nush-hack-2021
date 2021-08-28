package com.example.nush_hack21.model

enum class GreenGrade {
    GOOD, MODERATE, NEFARIOUS;

    companion object {
        fun gradeScore(score: Int) : GreenGrade {
            return when {
                score >= 75 -> GOOD
                score >= 50 -> MODERATE
                else -> NEFARIOUS
            }
        }
    }
}