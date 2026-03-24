/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: MenuSummaryDomain.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 02.32
 */

package com.mtv.app.shopme.domain.model

data class MenuSummary(
    val ordered: Int,
    val cooking: Int,
    val shipping: Int,
    val completed: Int,
    val cancelled: Int
)