package com.christhperalta.donext.features.home.presentation.profile


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.christhperalta.donext.core.data.Settings
import com.christhperalta.donext.core.presentation.CustomText
import org.koin.compose.koinInject





@Composable
fun ProfileScreen() {
    val settings: Settings = koinInject()
    val userName = remember { settings.getString("user_name") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = { ProfileTopBar() }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {

            Spacer(modifier = Modifier.height(40.dp))

                // --- SECCIÓN DE AVATAR ---
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .border(3.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile Picture",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                        )
                    }
                    // Botón de editar (Círculo verde)
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp).offset(x = (-8).dp, y = (-8).dp),
                        shadowElevation = 2.dp
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

            }


//            Spacer(modifier = Modifier.height(16.dp))

            item {

                // --- INFORMACIÓN DE USUARIO ---
                Text(
                    text = userName.ifBlank { "User" },
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Free Plan • User since 2023",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(40.dp))

                // --- LISTA DE OPCIONES ---
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    MenuOption(icon = Icons.Default.Person, title = "Account Settings")
                    MenuOption(
                        icon = Icons.Default.Notifications,
                        title = "Notification Preferences"
                    )
                    MenuOption(
                        icon = Icons.Default.Brightness2, // Icono de Luna
                        title = "Theme",
                        subtitle = "Currently: Light"
                    )
                    MenuOption(icon = Icons.AutoMirrored.Filled.Help, title = "Help Center")
                }
            }

//            Spacer(modifier = Modifier.weight(1f))

//            item {
//                // --- BOTÓN LOGOUT ---
//                OutlinedButton(
//                    onClick = { /* Acción de logout */ },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    shape = RoundedCornerShape(12.dp),
////                border = borderStroke(1.dp, LogoutRed.copy(alpha = 0.2f)),
//                    colors = ButtonDefaults.outlinedButtonColors(contentColor = LogoutRed)
//                ) {
//                    Text("Log Out", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
//                }
//            }


//            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            CustomText(
                text = "Profile",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
        },
    )
}


@Composable
fun MenuOption(icon: ImageVector, title: String, subtitle: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono cuadrado con fondo blanco
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(48.dp),
            shadowElevation = 1.dp
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(12.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Texto
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            if (subtitle != null) {
                Text(text = subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // Flecha derecha
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outlineVariant
        )
    }
}