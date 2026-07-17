package com.joaopablo.mobiletoolkit.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ToolkitCard(
    emoji: String,
    title: String,
    description: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF4F2F9) // Premium modern lavender/off-white card color
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Large Accent Emoji circular background
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE8E5F0),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C243B)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle Description
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF706484),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom row for Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onActionClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF825DFF)
                    ),
                    contentPadding = PaddingValues(horizontal = 22.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = actionLabel,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
