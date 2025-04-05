package com.yugentech.kaizen.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProfileMenu(
    onDismiss: () -> Unit,
    username: String,
    email: String,
    profile: String,
    onSignOut: () -> Unit,
    onAboutClick: () -> Unit,
) {
    val bgColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.secondaryContainer,
        animationSpec = tween(300)
    )
    val textColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.onSecondaryContainer,
        animationSpec = tween(300)
    )
    val manageAccountColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.inverseSurface,
        animationSpec = tween(300)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor.copy(alpha = 0.7f))
            .clickable(
                onClick = onDismiss,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            val cardWidth = if (maxWidth > 700.dp) 700.dp else maxWidth

            Card(
                modifier = Modifier
                    .width(cardWidth)
                    .align(Alignment.Center),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = bgColor)
            ) {
                ProfileMenuContent(
                    onDismiss = onDismiss,
                    username = username,
                    email = email,
                    profile = profile,
                    onSignOut = onSignOut,
                    textColor = textColor,
                    manageAccountColor = manageAccountColor,
                    onAboutClick = onAboutClick
                )
            }
        }
    }
}

@Composable
fun ProfileMenuContent(
    onDismiss: () -> Unit,
    username: String,
    email: String,
    profile: String,
    onSignOut: () -> Unit,
    textColor: Color,
    manageAccountColor: Color,
    onAboutClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDismiss
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = textColor
                )
            }

            Text(
                text = "Kaizen",
                style = MaterialTheme.typography.titleLarge,
                color = textColor
            )

            Spacer(modifier = Modifier.width(48.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = profile,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                error = rememberVectorPainter(Icons.Default.AccountCircle),
                placeholder = rememberVectorPainter(Icons.Default.AccountCircle)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = username,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor
                )

                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor.copy(alpha = 0.75f),
                    maxLines = 1
                )
            }
        }

        FilledTonalButton(
            onClick = { TODO() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Text(
                "Manage your Kaizen Account",
                modifier = Modifier.padding(vertical = 4.dp),
                color = textColor
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        MenuItems(textColor, onAboutClick)

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        TextButton(
            onClick = onSignOut,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Sign out",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { TODO() }) {
                Text("Privacy Policy", color = textColor)
            }
            Text("•", modifier = Modifier.padding(horizontal = 8.dp), color = textColor)
            TextButton(onClick = { TODO() }) {
                Text("Terms of Service", color = textColor)
            }
        }
    }
}

@Composable
private fun MenuItems(
    textColor: Color,
    onAboutClick: () -> Unit
) {
    val menuItems = listOf(
        MenuItem("Settings", Icons.Default.Settings, onClick = { TODO() }),
        MenuItem("Your Progress", Icons.Default.Timeline, onClick = { TODO() }),
        MenuItem("Achievements", Icons.Default.EmojiEvents, onClick = { TODO() }),
        MenuItem("About", Icons.Default.Info, onClick = onAboutClick)
    )

    menuItems.forEach { item ->
        TextButton(
            onClick = item.onClick,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = textColor
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)