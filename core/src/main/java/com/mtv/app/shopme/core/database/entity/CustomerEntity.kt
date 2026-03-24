/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: CustomerEntity.kt
 *
 * Last modified by Dedy Wijaya on 23/03/26 19.36
 */

package com.mtv.app.shopme.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class CustomerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val photo: String,
    val updatedAt: Long
)