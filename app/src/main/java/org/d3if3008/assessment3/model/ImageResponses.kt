package org.d3if3008.assessment3.model


data class ImagePostResponse(
    val data: ImageData,
    val success: Boolean
)

data class ImageData(
    val id: String,
    val link: String,
    val type: String,
    val deletehash: String? = null,
)

data class ImageGetResponse(
    val data: ImageData
)

data class ImageDeleteResponse(
    val success: Boolean
)


