/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: MenuSummaryDomain.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 02.32
 */

package com.mtv.app.shopme.domain.model

data class MenuSummary(
    val unpaid: Long = 0,
    val ordered: Long,
    val cooking: Long,
    val shipping: Long,
    val completed: Long,
    val cancelled: Long
)
