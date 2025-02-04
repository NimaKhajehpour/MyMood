package com.nima.mymood.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.utils.Constants
import kotlinx.coroutines.launch

@Composable
fun DonateScreen(
    navController: NavController
) {

    val scope = rememberCoroutineScope()
    val snackBarHost = remember {
        SnackbarHostState()
    }
    val clipboard = LocalClipboardManager.current

    Box{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Consider Donating?\nThis app is a hobby project and does not have any way of income for me. But your donations to me through these link are appreciated and gives me motivation to keep maintaining this application and other projects!",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp),
                textAlign = TextAlign.Center
            )

            OutlinedButton(
                onClick = {
                    scope.launch {
                        snackBarHost.showSnackbar(
                            message = "Coming Soon!",
                            withDismissAction = true,
                            duration = SnackbarDuration.Long,
                            actionLabel = null
                        )
                    }
                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "PayPal Coming Soon!")
            }

            OutlinedButton(
                onClick = {
                    clipboard.setText(AnnotatedString(Constants.eth_address))
                    scope.launch {
                        snackBarHost.showSnackbar(
                            message = "Address Copied",
                            withDismissAction = true,
                            duration = SnackbarDuration.Long,
                            actionLabel = null
                        )
                    }
                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
                    .fillMaxWidth()

            ) {
                Text(text = "Copy Etherium Address")
            }

            OutlinedButton(
                onClick = {
                    clipboard.setText(AnnotatedString(Constants.btc_address))
                    scope.launch {
                        snackBarHost.showSnackbar(
                            message = "Address Copied",
                            withDismissAction = true,
                            duration = SnackbarDuration.Long,
                            actionLabel = null
                        )
                    }
                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
                    .fillMaxWidth()

            ) {
                Text(text = "Copy Btc (Coin) Address")
            }

            OutlinedButton(
                onClick = {
                    clipboard.setText(AnnotatedString(Constants.usdt_address))
                    scope.launch {
                        snackBarHost.showSnackbar(
                            message = "Address Copied",
                            withDismissAction = true,
                            duration = SnackbarDuration.Long,
                            actionLabel = null
                        )
                    }
                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp)
                    .fillMaxWidth()

            ) {
                Text(text = "Copy Tether (USDT) Address")
            }

            OutlinedButton(
                onClick = {
                    clipboard.setText(AnnotatedString(Constants.ton_address))
                    scope.launch {
                        snackBarHost.showSnackbar(
                            message = "Address Copied",
                            withDismissAction = true,
                            duration = SnackbarDuration.Long,
                            actionLabel = null
                        )
                    }
                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp)
                    .fillMaxWidth()

            ) {
                Text(text = "Copy TON Address")
            }

        }
        SnackbarHost(
            hostState = snackBarHost,
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Snackbar(
                snackbarData = it,
                actionOnNewLine = false,
                shape = RoundedCornerShape(10.dp),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                dismissActionContentColor = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}
