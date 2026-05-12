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
    val phone: String,
    val email: String,
    val addressVillage: String,
    val addressBlock: String,
    val addressNumber: String,
    val addressRt: String,
    val addressRw: String,
    val photo: String,
    val verified: Boolean,
    val totalOrders: Int,
    val activeOrders: Int,
    val membership: String,
    val ordered: Int,
    val cooking: Int,
    val shipping: Int,
    val completed: Int,
    val cancelled: Int,
    val updatedAt: Long
)
