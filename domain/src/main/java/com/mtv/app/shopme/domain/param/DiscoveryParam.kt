package com.mtv.app.shopme.domain.param

data class DiscoveryParam(
    val section: String,
    val page: Int = 0,
    val size: Int = 10,
    val seed: String = ""
)
