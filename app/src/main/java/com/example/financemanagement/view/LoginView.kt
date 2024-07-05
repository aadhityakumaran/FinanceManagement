package com.example.financemanagement.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.financemanagement.view.components.InputTextField
import com.example.financemanagement.viewmodel.LoginViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun LoginView(
    viewModel: LoginViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputTextField(
                label = "Email",
                value = viewModel.email,
                onValueChanged = {
                    viewModel.onUsernameChange(it)
                }
            )

            InputTextField(
                label = "Password",
                value = viewModel.password,
                onValueChanged = {
                    viewModel.onPasswordChange(it)
                }
            )

            Button(
                onClick = {
                        viewModel.logIn(
                            onSuccess = {
                                Log.d("LoginView", "Login successful")
                                navController.navigate("homeScreen")
                                viewModel.email = ""
                                viewModel.password = ""
                            },
                            onError = { errorMessage ->
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        )
                }
            ) {
                Text("Login")
            }

            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    navController.navigate("signUpScreen")
                }) {
                Text(text = "Create Account")
            }
        }
        

    }
}

@Preview
@Composable
fun LoginViewPreview() {
   LoginView(viewModel = LoginViewModel(), navController = NavController(LocalContext.current))
}