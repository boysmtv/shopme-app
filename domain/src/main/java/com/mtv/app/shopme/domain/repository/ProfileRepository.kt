/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: ProfileRepository.kt
 *
 * Last modified by Dedy Wijaya on 01/04/26 09.27
 */

package com.mtv.app.shopme.domain.repository

import com.mtv.app.shopme.domain.model.Address
import com.mtv.app.shopme.domain.model.Customer
import com.mtv.app.shopme.domain.model.NotificationPreferences
import com.mtv.app.shopme.domain.model.Village
import com.mtv.app.shopme.domain.param.AddressAddParam
import com.mtv.app.shopme.domain.param.AddressDefaultParam
import com.mtv.app.shopme.domain.param.AddressDeleteParam
import com.mtv.app.shopme.domain.param.CustomerUpdateParam
import com.mtv.app.shopme.domain.param.NotificationPreferencesParam
import com.mtv.based.core.network.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getCustomer(): Flow<Resource<Customer>>

    fun updateProfile(param: CustomerUpdateParam): Flow<Resource<Unit>>

    fun deleteAccount(): Flow<Resource<Unit>>

    fun getNotificationPreferences(): Flow<Resource<NotificationPreferences>>

    fun updateNotificationPreferences(param: NotificationPreferencesParam): Flow<Resource<NotificationPreferences>>

    fun getAddresses(): Flow<Resource<List<Address>>>

    fun getVillages(): Flow<Resource<List<Village>>>

    fun addAddress(param: AddressAddParam): Flow<Resource<Unit>>

    fun deleteAddress(param: AddressDeleteParam): Flow<Resource<Unit>>

    fun setDefaultAddress(param: AddressDefaultParam): Flow<Resource<Unit>>
}
