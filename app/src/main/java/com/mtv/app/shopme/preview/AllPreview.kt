///*
// * Project: Boys.mtv@gmail.com
// * File: AllPreview.kt
// *
// * Last modified by Dedy Wijaya on 13/03/2026 13.23
// */
//
//package com.mtv.app.shopme.preview
//
//import android.annotation.SuppressLint
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Devices
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.compose.rememberNavController
//import com.mtv.app.shopme.common.AppColor
//import com.mtv.app.shopme.data.remote.response.AddressResponse
//import com.mtv.app.shopme.data.remote.response.CustomerResponse
//import com.mtv.app.shopme.data.remote.response.MenuSummary
//import com.mtv.app.shopme.data.remote.response.Stats
//import com.mtv.app.shopme.feature.auth.contract.ChangePinDataListener
//import com.mtv.app.shopme.feature.auth.contract.ChangePinEventListener
//import com.mtv.app.shopme.feature.auth.contract.ChangePinNavigationListener
//import com.mtv.app.shopme.feature.auth.contract.ChangePinStateListener
//import com.mtv.app.shopme.feature.auth.contract.LoginDataListener
//import com.mtv.app.shopme.feature.auth.contract.LoginEventListener
//import com.mtv.app.shopme.feature.auth.contract.LoginNavigationListener
//import com.mtv.app.shopme.feature.auth.contract.LoginStateListener
//import com.mtv.app.shopme.feature.auth.contract.PasswordDataListener
//import com.mtv.app.shopme.feature.auth.contract.PasswordEventListener
//import com.mtv.app.shopme.feature.auth.contract.PasswordNavigationListener
//import com.mtv.app.shopme.feature.auth.contract.PasswordStateListener
//import com.mtv.app.shopme.feature.auth.contract.RegisterDataListener
//import com.mtv.app.shopme.feature.auth.contract.RegisterEventListener
//import com.mtv.app.shopme.feature.auth.contract.RegisterNavigationListener
//import com.mtv.app.shopme.feature.auth.contract.RegisterStateListener
//import com.mtv.app.shopme.feature.auth.contract.ResetDataListener
//import com.mtv.app.shopme.feature.auth.contract.ResetEventListener
//import com.mtv.app.shopme.feature.auth.contract.ResetNavigationListener
//import com.mtv.app.shopme.feature.auth.contract.ResetStateListener
//import com.mtv.app.shopme.feature.auth.contract.SplashEventListener
//import com.mtv.app.shopme.feature.auth.contract.SplashNavigationListener
//import com.mtv.app.shopme.feature.auth.contract.SplashStateListener
//import com.mtv.app.shopme.feature.auth.ui.ChangePinScreen
//import com.mtv.app.shopme.feature.auth.ui.LoginScreen
//import com.mtv.app.shopme.feature.auth.ui.PasswordScreen
//import com.mtv.app.shopme.feature.auth.ui.RegisterScreen
//import com.mtv.app.shopme.feature.auth.ui.ResetScreen
//import com.mtv.app.shopme.feature.auth.ui.SplashScreen
//import com.mtv.app.shopme.feature.customer.contract.CafeEventListener
//import com.mtv.app.shopme.feature.customer.contract.CafeNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.CafeStateListener
//import com.mtv.app.shopme.feature.customer.contract.CartDataListener
//import com.mtv.app.shopme.feature.customer.contract.CartEventListener
//import com.mtv.app.shopme.feature.customer.contract.CartNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.CartStateListener
//import com.mtv.app.shopme.feature.customer.contract.ChatDataListener
//import com.mtv.app.shopme.feature.customer.contract.ChatEventListener
//import com.mtv.app.shopme.feature.customer.contract.ChatListDataListener
//import com.mtv.app.shopme.feature.customer.contract.ChatListEventListener
//import com.mtv.app.shopme.feature.customer.contract.ChatListNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.ChatListStateListener
//import com.mtv.app.shopme.feature.customer.contract.ChatNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.ChatStateListener
//import com.mtv.app.shopme.feature.customer.contract.ChatSupportDataListener
//import com.mtv.app.shopme.feature.customer.contract.ChatSupportEventListener
//import com.mtv.app.shopme.feature.customer.contract.ChatSupportNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.ChatSupportStateListener
//import com.mtv.app.shopme.feature.customer.contract.DetailDataListener
//import com.mtv.app.shopme.feature.customer.contract.DetailEventListener
//import com.mtv.app.shopme.feature.customer.contract.DetailNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.DetailStateListener
//import com.mtv.app.shopme.feature.customer.contract.EditAddressDataListener
//import com.mtv.app.shopme.feature.customer.contract.EditAddressEventListener
//import com.mtv.app.shopme.feature.customer.contract.EditAddressNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.EditAddressStateListener
//import com.mtv.app.shopme.feature.customer.contract.EditProfileDataListener
//import com.mtv.app.shopme.feature.customer.contract.EditProfileEventListener
//import com.mtv.app.shopme.feature.customer.contract.EditProfileNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.EditProfileStateListener
//import com.mtv.app.shopme.feature.customer.contract.HelpDataListener
//import com.mtv.app.shopme.feature.customer.contract.HelpEventListener
//import com.mtv.app.shopme.feature.customer.contract.HelpFaq
//import com.mtv.app.shopme.feature.customer.contract.HelpNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.HelpStateListener
//import com.mtv.app.shopme.feature.customer.contract.HomeDataListener
//import com.mtv.app.shopme.feature.customer.contract.HomeEventListener
//import com.mtv.app.shopme.feature.customer.contract.HomeNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.HomeStateListener
//import com.mtv.app.shopme.feature.customer.contract.NotifDataListener
//import com.mtv.app.shopme.feature.customer.contract.NotifEventListener
//import com.mtv.app.shopme.feature.customer.contract.NotifNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.NotifStateListener
//import com.mtv.app.shopme.feature.customer.contract.NotificationDataListener
//import com.mtv.app.shopme.feature.customer.contract.NotificationEventListener
//import com.mtv.app.shopme.feature.customer.contract.NotificationNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.NotificationStateListener
//import com.mtv.app.shopme.feature.customer.contract.OrderDataListener
//import com.mtv.app.shopme.feature.customer.contract.OrderEventListener
//import com.mtv.app.shopme.feature.customer.contract.OrderHistoryDataListener
//import com.mtv.app.shopme.feature.customer.contract.OrderHistoryEventListener
//import com.mtv.app.shopme.feature.customer.contract.OrderHistoryItem
//import com.mtv.app.shopme.feature.customer.contract.OrderHistoryNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.OrderHistoryStateListener
//import com.mtv.app.shopme.feature.customer.contract.OrderNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.OrderStateListener
//import com.mtv.app.shopme.feature.customer.contract.OrderStatusFilter
//import com.mtv.app.shopme.feature.customer.contract.ProfileDataListener
//import com.mtv.app.shopme.feature.customer.contract.ProfileEventListener
//import com.mtv.app.shopme.feature.customer.contract.ProfileNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.ProfileStateListener
//import com.mtv.app.shopme.feature.customer.contract.SearchDataListener
//import com.mtv.app.shopme.feature.customer.contract.SearchEventListener
//import com.mtv.app.shopme.feature.customer.contract.SearchNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.SearchStateListener
//import com.mtv.app.shopme.feature.customer.contract.SecurityDataListener
//import com.mtv.app.shopme.feature.customer.contract.SecurityEventListener
//import com.mtv.app.shopme.feature.customer.contract.SecurityNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.SecurityStateListener
//import com.mtv.app.shopme.feature.customer.contract.SettingsDataListener
//import com.mtv.app.shopme.feature.customer.contract.SettingsEventListener
//import com.mtv.app.shopme.feature.customer.contract.SettingsNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.SettingsStateListener
//import com.mtv.app.shopme.feature.customer.contract.SupportDataListener
//import com.mtv.app.shopme.feature.customer.contract.SupportEventListener
//import com.mtv.app.shopme.feature.customer.contract.SupportMessage
//import com.mtv.app.shopme.feature.customer.contract.SupportNavigationListener
//import com.mtv.app.shopme.feature.customer.contract.SupportStateListener
//import com.mtv.app.shopme.feature.customer.presentation.mockChatListState
//import com.mtv.app.shopme.feature.customer.ui.AddressSection
//import com.mtv.app.shopme.feature.customer.ui.AnimatedTab
//import com.mtv.app.shopme.feature.customer.ui.CafeScreen
//import com.mtv.app.shopme.feature.customer.ui.CartScreen
//import com.mtv.app.shopme.feature.customer.ui.ChatListScreen
//import com.mtv.app.shopme.feature.customer.ui.ChatScreen
//import com.mtv.app.shopme.feature.customer.ui.ChatSupportScreen
//import com.mtv.app.shopme.feature.customer.ui.DetailScreen
//import com.mtv.app.shopme.feature.customer.ui.EditAddressScreen
//import com.mtv.app.shopme.feature.customer.ui.EditProfileScreen
//import com.mtv.app.shopme.feature.customer.ui.HelpScreen
//import com.mtv.app.shopme.feature.customer.ui.HomeScreen
//import com.mtv.app.shopme.feature.customer.ui.NotificationScreen
//import com.mtv.app.shopme.feature.customer.ui.OrderHistoryScreen
//import com.mtv.app.shopme.feature.customer.ui.OrderScreen
//import com.mtv.app.shopme.feature.customer.ui.OrderSuccessDialog
//import com.mtv.app.shopme.feature.customer.ui.PinVerificationSheet
//import com.mtv.app.shopme.feature.customer.ui.PremiumCheckoutSheet
//import com.mtv.app.shopme.feature.customer.ui.PremiumHeader
//import com.mtv.app.shopme.feature.customer.ui.ProfileScreen
//import com.mtv.app.shopme.feature.customer.ui.SearchScreen
//import com.mtv.app.shopme.feature.customer.ui.SecurityScreen
//import com.mtv.app.shopme.feature.customer.ui.SettingsScreen
//import com.mtv.app.shopme.feature.customer.ui.SupportScreen
//import com.mtv.app.shopme.feature.customer.ui.UploadProofSheet
//import com.mtv.app.shopme.feature.customer.ui.VariantBottomSheetContent
//import com.mtv.app.shopme.feature.customer.ui.mockCartItems
//import com.mtv.app.shopme.feature.customer.ui.previewCafeData
//import com.mtv.app.shopme.feature.customer.ui.previewCustomer
//import com.mtv.app.shopme.feature.customer.ui.rememberSheetController
//import com.mtv.app.shopme.feature.seller.contract.CafeCreateDataListener
//import com.mtv.app.shopme.feature.seller.contract.CafeCreateEventListener
//import com.mtv.app.shopme.feature.seller.contract.CafeCreateNavigationListener
//import com.mtv.app.shopme.feature.seller.contract.CafeCreateStateListener
//import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailDataListener
//import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailEventListener
//import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailNavigationListener
//import com.mtv.app.shopme.feature.seller.contract.SellerChatDetailStateListener
//import com.mtv.app.shopme.feature.seller.contract.SellerChatListDataListener
//import com.mtv.app.shopme.feature.seller.contract.SellerChatListEventListener
//import com.mtv.app.shopme.feature.seller.contract.SellerChatListNavigationListener
//import com.mtv.app.shopme.feature.seller.contract.SellerChatListStateListener
//import com.mtv.app.shopme.feature.seller.contract.SellerDashboardDataListener
//import com.mtv.app.shopme.feature.seller.contract.SellerDashboardEventListener
//import com.mtv.app.shopme.feature.seller.contract.SellerDashboardNavigationListener
//import com.mtv.app.shopme.feature.seller.contract.SellerDashboardStateListener
//import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreDataListener
//import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreEventListener
//import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreNavigationListener
//import com.mtv.app.shopme.feature.seller.contract.SellerEditStoreStateListener
//import com.mtv.app.shopme.feature.seller.contract.SellerNotifData
//import com.mtv.app.shopme.feature.seller.contract.SellerNotifEvent
//import com.mtv.app.shopme.feature.seller.contract.SellerNotifNavigation
//import com.mtv.app.shopme.feature.seller.contract.SellerNotifState
//import com.mtv.app.shopme.feature.seller.contract.SellerOrderDataListener
//import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailDataListener
//import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailEventListener
//import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailNavigationListener
//import com.mtv.app.shopme.feature.seller.contract.SellerOrderDetailStateListener
//import com.mtv.app.shopme.feature.seller.contract.SellerOrderEventListener
//import com.mtv.app.shopme.feature.seller.contract.SellerOrderNavigationListener
//import com.mtv.app.shopme.feature.seller.contract.SellerOrderStateListener
//import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodDataListener
//import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodEventListener
//import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodNavigationListener
//import com.mtv.app.shopme.feature.seller.contract.SellerPaymentMethodStateListener
//import com.mtv.app.shopme.feature.seller.contract.SellerProductFormDataListener
//import com.mtv.app.shopme.feature.seller.contract.SellerProductFormEventListener
//import com.mtv.app.shopme.feature.seller.contract.SellerProductFormNavigationListener
//import com.mtv.app.shopme.feature.seller.contract.SellerProductFormStateListener
//import com.mtv.app.shopme.feature.seller.contract.SellerProductListDataListener
//import com.mtv.app.shopme.feature.seller.contract.SellerProductListEventListener
//import com.mtv.app.shopme.feature.seller.contract.SellerProductListNavigationListener
//import com.mtv.app.shopme.feature.seller.contract.SellerProductListStateListener
//import com.mtv.app.shopme.feature.seller.contract.SellerStoreDataListener
//import com.mtv.app.shopme.feature.seller.contract.SellerStoreEventListener
//import com.mtv.app.shopme.feature.seller.contract.SellerStoreNavigationListener
//import com.mtv.app.shopme.feature.seller.contract.SellerStoreStateListener
//import com.mtv.app.shopme.feature.seller.model.SellerNotifItem
//import com.mtv.app.shopme.feature.seller.model.SellerProduct
//import com.mtv.app.shopme.feature.seller.ui.CafeCreateScreen
//import com.mtv.app.shopme.feature.seller.ui.ProductStep
//import com.mtv.app.shopme.feature.seller.ui.SellerChatListScreen
//import com.mtv.app.shopme.feature.seller.ui.SellerChatScreen
//import com.mtv.app.shopme.feature.seller.ui.SellerDashboardScreen
//import com.mtv.app.shopme.feature.seller.ui.SellerEditStoreScreen
//import com.mtv.app.shopme.feature.seller.ui.SellerNotificationScreen
//import com.mtv.app.shopme.feature.seller.ui.SellerOrderDetailScreen
//import com.mtv.app.shopme.feature.seller.ui.SellerOrderScreen
//import com.mtv.app.shopme.feature.seller.ui.SellerPaymentMethodScreen
//import com.mtv.app.shopme.feature.seller.ui.SellerProductFormScreen
//import com.mtv.app.shopme.feature.seller.ui.SellerProductListScreen
//import com.mtv.app.shopme.feature.seller.ui.SellerStoreScreen
//import com.mtv.app.shopme.feature.seller.ui.StoreAddressSection
//import com.mtv.app.shopme.feature.seller.ui.VariantGroupUi
//import com.mtv.app.shopme.feature.seller.ui.VariantOptionUi
//import com.mtv.app.shopme.feature.seller.ui.mockSellerChatList
//import com.mtv.app.shopme.feature.seller.ui.mockSellerChatMessages
//import com.mtv.app.shopme.nav.CustomerBottomNavigationBar
//import com.mtv.app.shopme.nav.SellerBottomNavigationBar
//
//@Preview(showBackground = true, device = Devices.PIXEL_4)
//@Composable
//private fun SplashScreenPreview() {
//    SplashScreen(
//        uiState = SplashStateListener(),
//        uiEvent = SplashEventListener {},
//        uiNavigation = SplashNavigationListener {}
//    )
//}
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun RegisterScreenPreview() {
//    RegisterScreen(
//        uiState = RegisterStateListener(),
//        uiData = RegisterDataListener(),
//        uiEvent = RegisterEventListener(),
//        uiNavigation = RegisterNavigationListener()
//    )
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun LoginScreenPreview() {
//    LoginScreen(
//        uiState = LoginStateListener(),
//        uiData = LoginDataListener(),
//        uiEvent = LoginEventListener(),
//        uiNavigation = LoginNavigationListener()
//    )
//}
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun PasswordScreenPreview() {
//    PasswordScreen(
//        uiState = PasswordStateListener(),
//        uiData = PasswordDataListener(
//            currentPassword = "",
//            newPassword = "",
//            confirmPassword = ""
//        ),
//        uiEvent = PasswordEventListener(
//            onCurrentPasswordChange = {},
//            onNewPasswordChange = {},
//            onConfirmPasswordChange = {},
//            onSubmitClick = {}
//        ),
//        uiNavigation = PasswordNavigationListener(
//            onBack = {}
//        )
//    )
//}
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun ResetScreenPreview() {
//    ResetScreen(
//        uiState = ResetStateListener(),
//        uiData = ResetDataListener(
//            email = "Boys.mtv@gmail.com"
//        ),
//        uiEvent = ResetEventListener(
//            onEmailChange = {},
//            onResetClick = {}
//        ),
//        uiNavigation = ResetNavigationListener(
//            onNavigateToLogin = {},
//            onBack = {}
//        )
//    )
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun ChangePinPreview() {
//    ChangePinScreen(
//        uiState = ChangePinStateListener(),
//        uiData = ChangePinDataListener(),
//        uiEvent = ChangePinEventListener(),
//        uiNavigation = ChangePinNavigationListener()
//    )
//}
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun HomeScreenPreview() {
//    val navController = rememberNavController()
//
//    val previewCustomer = CustomerResponse(
//        name = "Dedy Wijaya",
//        phone = "08123456789",
//        email = "boys.mtv@gmail.com",
//        address = AddressResponse(
//            id = "1",
//            areaId = "Puri Lestari",
//            block = "H2",
//            number = "21",
//            rt = "012",
//            rw = "002",
//            isDefault = true
//        ),
//        photo = "",
//        verified = true,
//        stats = Stats(0, 0, ""),
//        menuSummary = MenuSummary(0, 0, 0, 0, 0)
//    )
//
//    Scaffold(
//        bottomBar = {
//            CustomerBottomNavigationBar(navController)
//        }
//    ) { padding ->
//        Box(
//            modifier = Modifier
//                .padding(bottom = padding.calculateBottomPadding())
//                .fillMaxSize()
//                .background(Color.White)
//        ) {
//            HomeScreen(
//                uiState = HomeStateListener(),
//                uiData = HomeDataListener(
//                    customerData = previewCustomer
//                ),
//                uiEvent = HomeEventListener({}),
//                uiNavigation = HomeNavigationListener({})
//            )
//        }
//    }
//}
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun DetailScreenPreview() {
//    DetailScreen(
//        uiState = DetailStateListener(),
//        uiData = DetailDataListener(
//            customerData = previewCustomer,
//            foodData = previewFood,
//            foodSimilarData = previewSimilar
//        ),
//        uiEvent = DetailEventListener(),
//        uiNavigation = DetailNavigationListener()
//    )
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun VariantBottomSheetMockPreview() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(AppColor.Gray),
//        contentAlignment = Alignment.BottomCenter
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
//                .background(Color.White)
//        ) {
//            VariantBottomSheetContent(
//                {}, {}
//            )
//        }
//    }
//}
//
//
//@Preview(
//    name = "Cafe Screen",
//    showBackground = true,
//    device = Devices.PIXEL_4_XL
//)
//@Composable
//private fun CafeScreenPreview() {
//    CafeScreen(
//        uiState = CafeStateListener(),
//        uiData = previewCafeData,
//        uiEvent = CafeEventListener(
//            onFoodClick = {}
//        ),
//        uiNavigation = CafeNavigationListener(
//            onBack = {}
//        )
//    )
//}
//
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun CartScreenPreview() {
//    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = {
//            CustomerBottomNavigationBar(navController)
//        }
//    ) { padding ->
//        Box(
//            modifier = Modifier
//                .padding(bottom = padding.calculateBottomPadding())
//                .fillMaxSize()
//                .background(Color.White)
//        ) {
//            CartScreen(
//                uiState = CartStateListener(),
//                uiData = CartDataListener(
//                    cartItems = mockCartItems
//                ),
//                uiEvent = CartEventListener(),
//                uiNavigation = CartNavigationListener()
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun PremiumCheckoutSheetPreview() {
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0x66000000)),
//        contentAlignment = Alignment.BottomCenter
//    ) {
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(.55f)
//                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
//                .background(Color.White)
//        ) {
//            PremiumCheckoutSheet(
//                total = 129.toBigDecimal(),
//                onDismiss = {},
//                onConfirm = {}
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun PinKeypadPreview() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0x66000000)),
//        contentAlignment = Alignment.BottomCenter
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(.60f)
//                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
//                .background(Color.White)
//        ) {
//            PinVerificationSheet(
//                onDismiss = {},
//                onSuccess = {}
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun OrderSuccessDialogPreview() {
//    OrderSuccessDialog(
//        onConfirm = {}
//    )
//}
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun ChatListScreenPreview() {
//    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = {
//            CustomerBottomNavigationBar(navController)
//        }
//    ) { padding ->
//        Box(
//            modifier = Modifier
//                .padding(bottom = padding.calculateBottomPadding())
//                .fillMaxSize()
//                .background(Color.White)
//        ) {
//            ChatListScreen(
//                uiState = ChatListStateListener(
//                    chatListState = mockChatListState()
//                ),
//                uiData = ChatListDataListener(),
//                uiEvent = ChatListEventListener(),
//                uiNavigation = ChatListNavigationListener()
//            )
//        }
//    }
//}
//
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun ChatScreenPreview() {
//    ChatScreen(
//        uiState = ChatStateListener(),
//        uiData = ChatDataListener(),
//        uiEvent = ChatEventListener(),
//        uiNavigation = ChatNavigationListener()
//    )
//}
//
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun ChatSupportPreview() {
//    ChatSupportScreen(
//        uiState = ChatSupportStateListener(),
//        uiData = ChatSupportDataListener(
//            messages = listOf(
//                SupportMessage("1", "Halo 👋 Selamat datang di Shopme Support.", false, "09:58"),
//                SupportMessage("2", "Ada yang bisa kami bantu hari ini?", false, "09:58"),
//                SupportMessage("3", "Halo kak, saya mau tanya pesanan saya belum sampai.", true, "09:59"),
//                SupportMessage("4", "Baik kak 😊 Boleh diinformasikan nomor pesanan nya?", false, "09:59"),
//                SupportMessage("5", "ORD-20260222-8891", true, "10:00"),
//                SupportMessage("6", "Terima kasih, kami cek dulu ya kak ⏳", false, "10:00"),
//                SupportMessage("7", "Baik kak, ditunggu 🙏", true, "10:01"),
//                SupportMessage("8", "Pesanan kakak saat ini sedang dalam perjalanan 🚚", false, "10:02"),
//                SupportMessage("9", "Estimasi tiba sekitar 20-30 menit lagi.", false, "10:02"),
//                SupportMessage("10", "Oh baik, berarti masih normal ya?", true, "10:03"),
//                SupportMessage("11", "Betul kak 👍 Tidak ada kendala di pengiriman.", false, "10:03"),
//                SupportMessage("12", "Kalau nanti ada masalah saya hubungi lagi ya.", true, "10:04"),
//                SupportMessage("13", "Siap kak, kami siap membantu kapan saja 😊", false, "10:04"),
//                SupportMessage("14", "Terima kasih supportnya 🙌", true, "10:05"),
//                SupportMessage("15", "Sama-sama kak 💚 Semoga harinya menyenangkan.", false, "10:05"),
//                SupportMessage("16", "Oh iya kak, apakah ada promo hari ini?", true, "10:06"),
//                SupportMessage("17", "Ada kak 🎉 Promo Diskon 20% untuk pembelian di atas 50rb.", false, "10:06"),
//                SupportMessage("18", "Wah menarik, berlaku sampai kapan?", true, "10:07"),
//                SupportMessage("19", "Promo berlaku sampai malam ini pukul 23:59 ya kak.", false, "10:07"),
//                SupportMessage("20", "Siap kak, nanti saya coba order lagi.", true, "10:08"),
//                SupportMessage("21", "Baik kak, terima kasih sudah menggunakan Shopme 💚", false, "10:08"),
//                SupportMessage("22", "Jika ada kendala, silakan hubungi kami kembali.", false, "10:09"),
//                SupportMessage("23", "Oke siap 👍", true, "10:09"),
//                SupportMessage("24", "Have a great day kak 🌟", false, "10:09")
//            )
//        ),
//        uiEvent = ChatSupportEventListener(),
//        uiNavigation = ChatSupportNavigationListener()
//    )
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun EditAddressScreenPreview() {
//    EditAddressScreen(
//        uiState = EditAddressStateListener(),
//        uiData = EditAddressDataListener(),
//        uiEvent = EditAddressEventListener(),
//        uiNavigation = EditAddressNavigationListener()
//    )
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun EditProfileScreenPreview() {
//    EditProfileScreen(
//        uiState = EditProfileStateListener(),
//        uiData = EditProfileDataListener(),
//        uiEvent = EditProfileEventListener(),
//        uiNavigation = EditProfileNavigationListener()
//    )
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun EditProfileAddressTabPreview() {
//
//    var selectedTab by remember { mutableIntStateOf(1) }
//
//    val dummyAddresses = listOf(
//        AddressResponse(
//            id = "1",
//            areaId = "Griya Asri",
//            block = "A",
//            number = "12",
//            rt = "01",
//            rw = "02",
//            isDefault = true
//        ),
//        AddressResponse(
//            id = "2",
//            areaId = "Permata Indah",
//            block = "B",
//            number = "8",
//            rt = "03",
//            rw = "05",
//            isDefault = false
//        )
//    )
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    listOf(AppColor.Green, AppColor.GreenSoft)
//                )
//            )
//    ) {
//
//        PremiumHeader(EditProfileNavigationListener())
//
//        Card(
//            modifier = Modifier.fillMaxSize(),
//            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
//            colors = CardDefaults.cardColors(containerColor = AppColor.White)
//        ) {
//
//            Column(Modifier.padding(16.dp)) {
//
//                AnimatedTab(selectedTab) { selectedTab = it }
//
//                Spacer(Modifier.height(16.dp))
//
//                AddressSection(
//                    addresses = dummyAddresses,
//                    onAdd = {},
//                    onDelete = {},
//                    onSetDefault = {}
//                )
//            }
//        }
//    }
//}
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun AddAddressSheetProductionPreview() {
//
//    val controller = rememberSheetController()
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFDDDDDD)),
//        contentAlignment = Alignment.BottomCenter
//    ) {
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
//                .background(Color.White)
//        ) {
//            AddAddressSheetContentProduction(
//                controller = controller,
//                onSave = {}
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun HelpScreenExpandedPreview() {
//    HelpScreen(
//        uiState = HelpStateListener(
//            isLoading = false
//        ),
//        uiData = HelpDataListener(
//            faq = listOf(
//                HelpFaq(
//                    question = "Bagaimana cara melacak pesanan?",
//                    answer = "Masuk ke menu Pesanan Saya → pilih pesanan → lihat status pengiriman secara realtime hingga pesanan diterima.",
//                    expanded = true
//                ),
//                HelpFaq(
//                    question = "Metode pembayaran apa saja tersedia?",
//                    answer = "Kami mendukung transfer bank, e-wallet, virtual account, dan Cash On Delivery (COD) untuk area tertentu.",
//                    expanded = true
//                ),
//                HelpFaq(
//                    question = "Bagaimana cara menghubungi support?",
//                    answer = "Gunakan tombol Hubungi Support di bagian bawah halaman ini. Tim kami tersedia 24/7 untuk membantu Anda.",
//                    expanded = true
//                )
//            )
//        ),
//        uiEvent = HelpEventListener(),
//        uiNavigation = HelpNavigationListener()
//    )
//}
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun NotificationPreview() {
//    NotificationScreen(
//        uiState = NotificationStateListener(),
//        uiData = NotificationDataListener(),
//        uiEvent = NotificationEventListener(),
//        uiNavigation = NotificationNavigationListener()
//    )
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4)
//@Composable
//private fun NotificationScreenPreview() {
//    NotificationScreen(
//        uiState = NotifStateListener(),
//        uiData = NotifDataListener(
//        ),
//        uiEvent = NotifEventListener({}, {}, {}, {}),
//        uiNavigation = NotifNavigationListener {}
//    )
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun OrderHistoryPreview() {
//    OrderHistoryScreen(
//        uiState = OrderHistoryStateListener(loading = false),
//        uiData = OrderHistoryDataListener(
//            selectedFilter = OrderStatusFilter.SEMUA,
//            orders = listOf(
//                OrderHistoryItem(
//                    "1",
//                    "Cafe Kopi Boy",
//                    "Cappuccino",
//                    "12 Feb 2026",
//                    "Rp 25.000",
//                    "SELESAI",
//                    3,
//                    "E-Wallet",
//                    "Diantar"
//                ),
//                OrderHistoryItem(
//                    "2",
//                    "Kopi Nusantara",
//                    "Caramel Latte",
//                    "11 Feb 2026",
//                    "Rp 32.000",
//                    "DIPROSES",
//                    2,
//                    "QRIS",
//                    "Pickup"
//                ),
//                OrderHistoryItem(
//                    "3",
//                    "Burger Town",
//                    "Cheese Burger Combo",
//                    "10 Feb 2026",
//                    "Rp 48.000",
//                    "DIKIRIM",
//                    4,
//                    "Cash",
//                    "Diantar"
//                ),
//                OrderHistoryItem(
//                    "4",
//                    "Coffee Corner",
//                    "Americano",
//                    "08 Feb 2026",
//                    "Rp 22.000",
//                    "SELESAI",
//                    1,
//                    "E-Wallet",
//                    "Pickup"
//                ),
//                OrderHistoryItem(
//                    "5",
//                    "Cafe Kopi Boy",
//                    "Matcha Latte",
//                    "05 Feb 2026",
//                    "Rp 30.000",
//                    "BATAL",
//                    2,
//                    "QRIS",
//                    "Diantar"
//                )
//            )
//        ),
//        uiEvent = OrderHistoryEventListener(),
//        uiNavigation = OrderHistoryNavigationListener()
//    )
//}
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun OrderScreenPreview() {
//    val dummyOrders = listOf(
//        OrderModel(
//            id = "A001",
//            customerId = "C001",
//            cafeId = "Mamah Al Cafe",
//            items = listOf(
//                OrderItemModel(foodId = 0, quantity = 2, price = 15000.0),
//                OrderItemModel(foodId = 3, quantity = 1, price = 15000.0)
//            ),
//            totalPrice = 45000.0,
//            status = OrderStatus.ORDERED,
//            paymentMethod = PaymentMethod.TRANSFER
//        ),
//        OrderModel(
//            id = "A002",
//            customerId = "C001",
//            cafeId = "Mamah Al Cafe",
//            items = listOf(
//                OrderItemModel(foodId = 1, quantity = 1, price = 30000.0)
//            ),
//            totalPrice = 30000.0,
//            status = OrderStatus.DELIVERING,
//            paymentMethod = PaymentMethod.TRANSFER
//        ),
//        OrderModel(
//            id = "A003",
//            customerId = "C001",
//            cafeId = "Mamah Al Cafe",
//            items = listOf(
//                OrderItemModel(foodId = 2, quantity = 1, price = 20000.0),
//                OrderItemModel(foodId = 5, quantity = 2, price = 16000.0)
//            ),
//            totalPrice = 52000.0,
//            status = OrderStatus.COMPLETED,
//            paymentMethod = PaymentMethod.TRANSFER
//        )
//    )
//
//    OrderScreen(
//        uiState = OrderStateListener(isLoading = false),
//        uiData = OrderDataListener(orders = dummyOrders),
//        uiEvent = OrderEventListener(),
//        uiNavigation = OrderNavigationListener()
//    )
//}
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun UploadProofSheetPreview_Empty() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0x66000000)),
//        contentAlignment = Alignment.BottomCenter
//    ) {
//        UploadProofSheet(
//            imageUri = null,
//            onTakePhoto = {},
//            onPickGallery = {},
//            onUpload = {},
//            onDismiss = {}
//        )
//    }
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun ProfileScreenPreview() {
//
//    val previewCustomer = CustomerResponse(
//        name = "Dedy Wijaya",
//        phone = "08158844424",
//        email = "boys.mtv@gmail.com",
//        address = AddressResponse(
//            id = "89a3c44a-b9c7-412f-83fd-f4f1ed66c6da",
//            areaId = "Puri Lestari",
//            block = "H2",
//            number = "21",
//            rt = "012",
//            rw = "002",
//            isDefault = true
//        ),
//        photo = "https://rakyatsulsel.fajar.co.id/wp-content/uploads/2025/03/g_p_o_potret_davina_karamoy_berhijab_saat_umrah_dipuji_makin_cantik_saat_tidak_pakai_makeup_p_davina_karamoy-20240925-007-non_fotografer_kly.jpg",
//        verified = true,
//        stats = Stats(
//            totalOrders = 24,
//            activeOrders = 2,
//            membership = "GOLD"
//        ),
//        menuSummary = MenuSummary(
//            ordered = 12,
//            cooking = 2,
//            shipping = 1,
//            completed = 9,
//            cancelled = 2
//        )
//    )
//
//    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = {
//            CustomerBottomNavigationBar(navController)
//        }
//    ) { padding ->
//        Box(
//            modifier = Modifier
//                .padding(bottom = padding.calculateBottomPadding())
//                .fillMaxSize()
//                .background(Color.White)
//        ) {
//            ProfileScreen(
//                navController = navController,
//                uiState = ProfileStateListener(),
//                uiData = ProfileDataListener(
//                    customerData = previewCustomer
//                ),
//                uiEvent = ProfileEventListener(),
//                uiNavigation = ProfileNavigationListener()
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SearchScreenPreview() {
//
//    val navController = rememberNavController()
//
//    Scaffold(
//        bottomBar = {
//            CustomerBottomNavigationBar(navController)
//        }
//    ) { padding ->
//
//        Box(
//            modifier = Modifier
//                .padding(bottom = padding.calculateBottomPadding())
//                .fillMaxSize()
//                .background(Color.White)
//        ) {
//
//            SearchScreen(
//                uiState = SearchStateListener(),
//                uiData = SearchDataListener(),
//                uiEvent = SearchEventListener(),
//                uiNavigation = SearchNavigationListener()
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SecurityPreview() {
//    SecurityScreen(
//        uiState = SecurityStateListener(),
//        uiData = SecurityDataListener(biometricEnabled = true),
//        uiEvent = SecurityEventListener(),
//        uiNavigation = SecurityNavigationListener()
//    )
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SettingsScreenProPreview() {
//    SettingsScreen(
//        uiState = SettingsStateListener(),
//        uiData = SettingsDataListener(
//            notificationEnabled = true,
//            darkMode = false
//        ),
//        uiEvent = SettingsEventListener(),
//        uiNavigation = SettingsNavigationListener()
//    )
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SupportPreview() {
//    SupportScreen(
//        uiState = SupportStateListener(
//            isLoading = false
//        ),
//        uiData = SupportDataListener(
//            phone = "081234567890",
//            email = "support@shopme.com",
//            whatsapp = "6281234567890"
//        ),
//        uiEvent = SupportEventListener(
//            onOpenWhatsapp = {},
//            onOpenEmail = {},
//            onOpenDial = {}
//        ),
//        uiNavigation = SupportNavigationListener(
//            onBack = {},
//            onLiveChat = {}
//        )
//    )
//}
//
//
//@Preview(
//    showBackground = true,
//    device = Devices.PIXEL_4_XL
//)
//@Composable
//private  fun CafeCreateScreenPreview() {
//
//    CafeCreateScreen(
//        uiState = CafeCreateStateListener(),
//
//        uiData = CafeCreateDataListener(
//            name = "Shopme Cafe",
//            phone = "08123456789",
//            description = "Best coffee in town",
//            minimalOrder = "10000",
//            openTime = "09:00",
//            closeTime = "22:00",
//            image = null,
//            isActive = true
//        ),
//
//        uiEvent = CafeCreateEventListener(
//            onNameChange = {},
//            onPhoneChange = {},
//            onDescriptionChange = {},
//            onMinimalOrderChange = {},
//            onOpenTimeChange = {},
//            onCloseTimeChange = {},
//            onUploadImage = {},
//            onCreateCafe = {}
//        ),
//
//        uiNavigation = CafeCreateNavigationListener(
//            navigateBack = {}
//        )
//    )
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SellerDashboardPreview() {
//    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = {
//            SellerBottomNavigationBar(navController)
//        }
//    ) { padding ->
//        Box(
//            modifier = Modifier
//                .padding(bottom = padding.calculateBottomPadding())
//                .fillMaxSize()
//                .background(AppColor.White)
//        ) {
//            SellerDashboardScreen(
//                uiState = SellerDashboardStateListener(),
//                uiData = SellerDashboardDataListener(),
//                uiEvent = SellerDashboardEventListener(),
//                uiNavigation = SellerDashboardNavigationListener()
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SellerOrderScreenPreview() {
//    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = { SellerBottomNavigationBar(navController) }
//    ) { padding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(bottom = padding.calculateBottomPadding())
//        ) {
//            SellerOrderScreen(
//                uiState = SellerOrderStateListener(),
//                uiData = SellerOrderDataListener(),
//                uiEvent = SellerOrderEventListener({}, {}),
//                uiNavigation = SellerOrderNavigationListener({})
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SellerOrderDetailPreview() {
//    SellerOrderDetailScreen(
//        uiState = SellerOrderDetailStateListener(
//            orderId = "INV-001",
//            currentStatus = OrderStatus.COOKING
//        ),
//        uiData = SellerOrderDetailDataListener(
//            customerName = "Dedy Wijaya",
//            total = "Rp 120.000"
//        ),
//        uiEvent = SellerOrderDetailEventListener(),
//        uiNavigation = SellerOrderDetailNavigationListener()
//    )
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SellerChatListPreview() {
//    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = { SellerBottomNavigationBar(navController) }
//    ) { padding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(bottom = padding.calculateBottomPadding())
//        ) {
//            SellerChatListScreen(
//                uiState = SellerChatListStateListener(chatList = mockSellerChatList()),
//                uiData = SellerChatListDataListener(),
//                uiEvent = SellerChatListEventListener({}),
//                uiNavigation = SellerChatListNavigationListener()
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SellerChatDetailPreview() {
//    SellerChatScreen(
//        uiState = SellerChatDetailStateListener(messages = mockSellerChatMessages()),
//        uiData = SellerChatDetailDataListener(),
//        uiEvent = SellerChatDetailEventListener(),
//        uiNavigation = SellerChatDetailNavigationListener()
//    )
//}
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SellerProductListScreenPreview() {
//    val navController = rememberNavController()
//
//    val mockState = SellerProductListStateListener(
//        productList = listOf(
//            SellerProduct("1", "Double Beef Burger", "Rp 60.000", 10),
//            SellerProduct("2", "Cheese Pizza", "Rp 75.000", 5),
//            SellerProduct("3", "Padang Rice Set", "Rp 50.000", 12),
//            SellerProduct("4", "Padang Rice Set", "Rp 50.000", 12),
//            SellerProduct("5", "Padang Rice Set", "Rp 50.000", 12),
//            SellerProduct("6", "Padang Rice Set", "Rp 50.000", 12),
//        )
//    )
//
//    Scaffold(
//        bottomBar = {
//            SellerBottomNavigationBar(navController)
//        }
//    ) { padding ->
//        Box(
//            modifier = Modifier
//                .padding(bottom = padding.calculateBottomPadding())
//                .fillMaxSize()
//                .background(AppColor.White)
//        ) {
//            SellerProductListScreen(
//                uiState = mockState,
//                uiData = SellerProductListDataListener(),
//                uiEvent = SellerProductListEventListener({}),
//                uiNavigation = SellerProductListNavigationListener({}, {}, {})
//            )
//        }
//    }
//}
//
//@Preview(name = "Step 1 - Basic", showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun PreviewStepBasic() {
//    MaterialTheme {
//        SellerProductFormScreen(
//            uiState = SellerProductFormStateListener(),
//            uiData = SellerProductFormDataListener(),
//            uiEvent = SellerProductFormEventListener({}, {}),
//            uiNavigation = SellerProductFormNavigationListener({}),
//            initialStep = ProductStep.BASIC
//        )
//    }
//}
//
//@Preview(name = "Step 2 - Pricing", showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun PreviewStepPricing() {
//    MaterialTheme {
//        SellerProductFormScreen(
//            uiState = SellerProductFormStateListener(),
//            uiData = SellerProductFormDataListener(),
//            uiEvent = SellerProductFormEventListener({}, {}),
//            uiNavigation = SellerProductFormNavigationListener({}),
//            initialStep = ProductStep.PRICING
//        )
//    }
//}
//
//@SuppressLint("UnrememberedMutableState")
//@Preview(name = "Step 3 - Variant", showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun PreviewStepVariant() {
//    MaterialTheme {
//        SellerProductFormScreen(
//            uiState = SellerProductFormStateListener(),
//            uiData = SellerProductFormDataListener(),
//            uiEvent = SellerProductFormEventListener({}, {}),
//            uiNavigation = SellerProductFormNavigationListener({}),
//            initialStep = ProductStep.VARIANT,
//            previewVariantGroups = mutableStateListOf(
//                VariantGroupUi(
//                    name = "Size",
//                    options = mutableStateListOf(
//                        VariantOptionUi("Small", "10000"),
//                        VariantOptionUi("Medium", "12000"),
//                    )
//                ),
//                VariantGroupUi(
//                    name = "Color",
//                    options = mutableStateListOf(
//                        VariantOptionUi("Red", "0"),
//                        VariantOptionUi("Blue", "0")
//                    )
//                )
//            )
//        )
//    }
//}
//
//@Preview(
//    name = "Step 4 - Review",
//    showBackground = true,
//    device = Devices.PIXEL_4_XL
//)
//@Composable
//private fun PreviewStepReview() {
//    MaterialTheme {
//        SellerProductFormScreen(
//            uiState = SellerProductFormStateListener(),
//            uiData = SellerProductFormDataListener(),
//            uiEvent = SellerProductFormEventListener({}, {}),
//            uiNavigation = SellerProductFormNavigationListener({}),
//            initialStep = ProductStep.REVIEW
//        )
//    }
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SellerStoreScreenPreview() {
//    SellerStoreScreen(
//        uiState = SellerStoreStateListener(),
//        uiData = SellerStoreDataListener(
//            sellerName = "Dedy Wijaya",
//            email = "seller@email.com",
//            phone = "08123456789",
//            storeName = "Shopme Store",
//            storeAddress = "Jakarta, Indonesia",
//            isOnline = true
//        ),
//        uiEvent = SellerStoreEventListener(
//            onToggleOnline = {},
//            onEditStore = {},
//            onLogout = {}
//        ),
//        uiNavigation = SellerStoreNavigationListener({}, {}, {})
//    )
//}
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SellerEditStoreScreenPreview() {
//    SellerEditStoreScreen(
//        uiState = SellerEditStoreStateListener(),
//        uiData = SellerEditStoreDataListener(
//            storeName = "Shopme Store",
//            phone = "08123456789",
//            description = "Best shop for everything",
//            storePhoto = null,
//            minOrder = "Rp 10.000",
//            storeOpen = "09.00 - 18.00",
//            village = "Griya Asri",
//            block = "A",
//            number = "12",
//            rt = "01",
//            rw = "02"
//        ),
//        uiEvent = SellerEditStoreEventListener(
//            onStoreNameChange = {},
//            onPhoneChange = {},
//            onDescriptionChange = {},
//
//            onVillageChange = {},
//            onBlockChange = {},
//            onNumberChange = {},
//            onRtChange = {},
//            onRwChange = {},
//
//            onUploadPhoto = {},
//            onSave = {}
//        ),
//        uiNavigation = SellerEditStoreNavigationListener(
//            navigateBack = {}
//        )
//    )
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun StoreAddressPreview() {
//
//    Column(
//        modifier = Modifier
//            .background(Color.White)
//            .padding(20.dp)
//    ) {
//
//        StoreAddressSection(
//            uiData = SellerEditStoreDataListener(
//                village = "Griya Asri",
//                block = "A",
//                number = "12",
//                rt = "01",
//                rw = "02"
//            ),
//            uiEvent = SellerEditStoreEventListener(
//                onStoreNameChange = {},
//                onPhoneChange = {},
//                onDescriptionChange = {},
//                onVillageChange = {},
//                onBlockChange = {},
//                onNumberChange = {},
//                onRtChange = {},
//                onRwChange = {},
//                onUploadPhoto = {},
//                onSave = {}
//            )
//        )
//    }
//}
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SellerNotificationScreenPreview() {
//    val dummyList = List(5) {
//        SellerNotifItem(
//            title = "New Order Received",
//            message = "You have a new order from John Doe",
//            orderId = "INV-20260220-00$it",
//            buyerName = "John Doe",
//            date = "20 Feb 2026",
//            time = "15:30",
//            isRead = it % 2 == 0
//        )
//    }
//
//    SellerNotificationScreen(
//        uiState = SellerNotifState(),
//        uiData = SellerNotifData(
//            localNotification = dummyList
//        ),
//        uiEvent = SellerNotifEvent(
//            onNotificationClicked = {},
//            onGetNotification = {},
//            onClearNotification = {},
//            onDismissActiveDialog = {}
//        ),
//        uiNavigation = SellerNotifNavigation(
//            onBack = {}
//        )
//    )
//}
//
//
//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//private fun SellerPaymentMethodPreview() {
//
//    SellerPaymentMethodScreen(
//        uiState = SellerPaymentMethodStateListener(),
//        uiData = SellerPaymentMethodDataListener(
//            cashEnabled = true,
//            bankNumber = "1234567890",
//            gopayNumber = "08123456789",
//            danaNumber = "08123456789",
//            ovoNumber = "08123456789"
//        ),
//        uiEvent = SellerPaymentMethodEventListener(
//            onCashToggle = {},
//            onBankChange = {},
//            onGopayChange = {},
//            onDanaChange = {},
//            onOvoChange = {},
//            onSave = {}
//        ),
//        uiNavigation = SellerPaymentMethodNavigationListener(
//            navigateBack = {}
//        )
//    )
//}
