/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: DeleteAddressUseCase.kt
 *
 * Last modified by Dedy Wijaya on 03/03/26 14.12
 */

package com.mtv.app.shopme.domain.usecase

import com.mtv.app.shopme.domain.param.AddressDeleteParam
import com.mtv.app.shopme.domain.repository.ProfileRepository
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(param: AddressDeleteParam) = repository.deleteAddress(param)
}