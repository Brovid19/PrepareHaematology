package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "approved_resources")
data class ApprovedResource(
    @PrimaryKey
    val id: String,
    val title: String,
    val issuingBody: String, // e.g. "British Society for Haematology", "IMWG", "WHO", "RCPath"
    val year: Int,
    val citation: String,
    val category: String, // "Malignant Haematology", "Haemostasis & Thrombosis", "Transfusion Medicine", "General Haematology"
    val summary: String,
    val keyCutoffs: String, // Bulleted clinical cutoffs
    val stagingCriteria: String, // Diagnostic & staging system
    val recommendations: String, // Core recommendations (e.g. Grade 1A, 1B)
    val url: String = "", // Direct link to full guideline online
    val isVerified: Boolean = true
) {
    fun getOnlineUrl(): String {
        if (url.isNotBlank()) return url
        return when (id) {
            "bsh_amyloidosis_2026" -> "https://doi.org/10.1111/bjh.20173"
            "imwg_myeloma_2021" -> "https://doi.org/10.1016/S1470-2045(14)70442-5"
            "who_haem_5th" -> "https://doi.org/10.1038/s41375-022-01620-2"
            "bsh_transfusion_components" -> "https://b-s-h.org.uk/guidelines/guidelines/the-administration-of-blood-components"
            "ukhcdo_haemostasis_2022" -> "https://www.ukhcdo.org/guidelines/"
            "bsh_iron_haemochromatosis" -> "https://doi.org/10.1111/bjh.15164"
            "bsh_sickle_transfusion" -> "https://doi.org/10.1111/bjh.14346"
            "bsh_apl_guidelines" -> "https://doi.org/10.1182/blood-2018-04-808006"
            "bsh_antiphospholipid" -> "https://doi.org/10.1111/bjh.18386"
            "bsh_ttp_guidelines" -> "https://doi.org/10.1111/jth.15006"
            else -> "https://b-s-h.org.uk/guidelines"
        }
    }
}
