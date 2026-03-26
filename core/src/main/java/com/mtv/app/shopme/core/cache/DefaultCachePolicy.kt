/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DefaultCachePolicy.kt
 *
 * Last modified by Dedy Wijaya on 24/03/26 18.52
 */

package com.mtv.app.shopme.core.cache

class DefaultCachePolicy : CachePolicy {
    override val customerTtl: Long = 5 * 60 * 1000
    override val foodsTtl: Long = 2 * 60 * 1000
    override val cartTtl = 1 * 60 * 1000L
}