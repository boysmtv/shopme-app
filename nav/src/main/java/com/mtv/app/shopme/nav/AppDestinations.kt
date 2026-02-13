/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: AppDestinations.kt
 *
 * Last modified by Dedy Wijaya on 31/12/2025 11.08
 */

package com.mtv.app.shopme.nav


object AppDestinations {
    const val SPLASH_GRAPH = "SPLASH_GRAPH"
    const val LOGIN_GRAPH = "LOGIN_GRAPH"
    const val REGISTER_GRAPH = "REGISTER_GRAPH"
    const val HOME_GRAPH = "HOME_GRAPH"
    const val RESET_GRAPH = "FORGOT_GRAPH"
    const val DETAIL_GRAPH = "DETAIL_GRAPH"
    const val PROFILE_GRAPH = "PROFILE_GRAPH"
    const val EDIT_PROFILE_GRAPH = "EDIT_PROFILE_GRAPH"
    const val EDIT_ADDRESS_GRAPH = "EDIT_ADDRESS_GRAPH"
    const val NOTIFICATION_GRAPH = "NOTIFICATION_GRAPH"
    const val CHAT_GRAPH = "CHAT_GRAPH"
    const val ORDER_GRAPH = "ORDER_GRAPH"
    const val ORDER_DETAIL_GRAPH = "ORDER_DETAIL_GRAPH"

    fun navigateToDetailMovies(movieId: Int): String {
        return "MOVIE_DETAIL_ROUTE/$movieId"
    }

    fun navigateToPlayMovie(key: String): String {
        return "MOVIE_PLAY_ROUTE/$key"
    }

}