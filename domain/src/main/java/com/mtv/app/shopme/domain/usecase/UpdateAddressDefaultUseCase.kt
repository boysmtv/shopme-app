/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: UpdateAddressDefaultUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 14.12
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.AddressDefaultParam
import com.mtv.app.shopme.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateAddressDefaultUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(param: AddressDefaultParam) = repository.setDefaultAddress(param)
}