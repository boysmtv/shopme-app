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
    const val NOTIFICATION_GRAPH = "NOTIFICATION_GRAPH"

    fun navigateToDetailMovies(movieId: Int): String {
        return "MOVIE_DETAIL_ROUTE/$movieId"
    }

    fun navigateToPlayMovie(key: String): String {
        return "MOVIE_PLAY_ROUTE/$key"
    }

}