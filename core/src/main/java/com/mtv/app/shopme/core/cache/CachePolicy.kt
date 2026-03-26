/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CachePolicy.kt
 *
 * Last modified by Dedy Wijaya on 24/03/26 18.52
 */

package com.mtv.app.shopme.core.cache

interface CachePolicy {
    val customerTtl: Long
    val foodsTtl: Long
    val cartTtl: Long
}