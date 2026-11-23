package com.example.shopora.ui.detail

/**
 * @author Utkarsh Srivastava
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.example.shopora.domain.model.Product

@Composable
fun EnquiryDialog(
    product: Product,
    enquiryState: EnquiryUiState,
    onSubmit: (name: String, email: String, message: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Hi, I am interested in '${product.title}'. Please send me more details.") }
    var validationError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (enquiryState is EnquiryUiState.Success) "Enquiry Sent!" else "Product Enquiry",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            when (enquiryState) {
                is EnquiryUiState.Idle, is EnquiryUiState.Submitting, is EnquiryUiState.Error -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Item: ${product.title}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                validationError = null
                            },
                            label = { Text("Your Full Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                validationError = null
                            },
                            label = { Text("Your Email Address") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            label = { Text("Message") },
                            minLines = 3,
                            maxLines = 5,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (validationError != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = validationError!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        if (enquiryState is EnquiryUiState.Error) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = enquiryState.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        if (enquiryState is EnquiryUiState.Submitting) {
                            Spacer(modifier = Modifier.height(12.dp))
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                }
                is EnquiryUiState.Success -> {
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Text(
                            text = "Thank you, ${name.ifEmpty { "valued customer" }}!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = enquiryState.message,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (enquiryState.enquiryId != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Enquiry Reference: ${enquiryState.enquiryId}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (enquiryState is EnquiryUiState.Success) {
                Button(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) {
                    Text("Close")
                }
            } else {
                Button(
                    onClick = {
                        val trimmedName = name.trim()
                        val trimmedEmail = email.trim()
                        if (trimmedName.isEmpty()) {
                            validationError = "Please enter your name."
                            return@Button
                        }
                        if (trimmedEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
                            validationError = "Please enter a valid email address."
                            return@Button
                        }
                        onSubmit(trimmedName, trimmedEmail, message)
                    },
                    enabled = enquiryState !is EnquiryUiState.Submitting,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Submit Enquiry")
                }
            }
        },
        dismissButton = {
            if (enquiryState !is EnquiryUiState.Success) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}
