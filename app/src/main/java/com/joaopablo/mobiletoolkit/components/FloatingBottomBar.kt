package com.joaopablo.mobiletoolkit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class BottomBarItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomBarItem("home_tab", "Home", Icons.Default.Home)
    object Upload : BottomBarItem("upload_tab", "Upload", Icons.Default.ArrowUpward)
    object Library : BottomBarItem("library_tab", "Library", Icons.Default.Bookmark)
    object Profile : BottomBarItem("profile_tab", "Profile", Icons.Default.Person)
}

val bottomBarItems = listOf(
    BottomBarItem.Home,
    BottomBarItem.Upload,
    BottomBarItem.Library,
    BottomBarItem.Profile
)

@Composable
fun FloatingBottomBar(
    currentTabRoute: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp, top = 8.dp),
        shape = RoundedCornerShape(32.dp),
        color = Color.White,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomBarItems.forEach { item ->
                val isSelected = currentTabRoute == item.route
                val contentColor = if (isSelected) Color(0xFF2C243B) else Color(0xFF8B8496)
                
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onTabSelected(item.route) }
                        )
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Pill-shaped container for active states
                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .width(64.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) Color(0xFFE5DFFF) else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = contentColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = contentColor
                    )
                }
            }
        }
    }
}
