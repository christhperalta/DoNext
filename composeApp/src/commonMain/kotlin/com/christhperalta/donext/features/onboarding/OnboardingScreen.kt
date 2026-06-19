package com.christhperalta.donext.features.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.christhperalta.donext.core.data.Settings
import kotlinx.coroutines.launch

private val BrandGreen = Color(0xFF60DF20)
private val BackgroundGray = Color(0xFFF5F7F5)

@Composable
fun OnboardingScreen(
    settings: Settings,
    onComplete: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    var userName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) { page ->
            when (page) {
                0 -> WelcomeSlide()
                1 -> FeaturesSlide()
                2 -> NameSlide(
                    userName = userName,
                    onUserNameChange = { userName = it },
                    onDone = {
                        settings.putString("user_name", userName.trim())
                        settings.putBoolean("onboarding_completed", true)
                        onComplete()
                    },
                )
            }
        }

        BottomBar(
            pageCount = 3,
            currentPage = pagerState.currentPage,
            isLastPage = pagerState.currentPage == 2,
            userName = userName,
            onNext = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            onSkip = {
                settings.putString("user_name", "")
                settings.putBoolean("onboarding_completed", true)
                onComplete()
            },
            onGetStarted = {
                settings.putString("user_name", userName.trim())
                settings.putBoolean("onboarding_completed", true)
                onComplete()
            },
        )
    }
}

@Composable
private fun WelcomeSlide() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = BrandGreen,
            modifier = Modifier.size(120.dp),
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Welcome to DoNext",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your simple task manager.\nStay organized, get things done.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
        )
    }
}

@Composable
private fun FeaturesSlide() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "What you can do",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
        Spacer(modifier = Modifier.height(32.dp))
        FeatureItem(
            icon = Icons.Default.EditNote,
            title = "Create Tasks",
            description = "Write down what you need to do"
        )
        Spacer(modifier = Modifier.height(24.dp))
        FeatureItem(
            icon = Icons.Default.Flag,
            title = "Set Priorities",
            description = "Focus on what matters most"
        )
        Spacer(modifier = Modifier.height(24.dp))
        FeatureItem(
            icon = Icons.Default.TrendingUp,
            title = "Track Progress",
            description = "See your achievements over time"
        )
    }
}

@Composable
private fun FeatureItem(
    icon: ImageVector,
    title: String,
    description: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(BrandGreen.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandGreen,
                modifier = Modifier.size(28.dp),
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
            )
        }
    }
}

@Composable
private fun NameSlide(
    userName: String,
    onUserNameChange: (String) -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.EditNote,
            contentDescription = null,
            tint = BrandGreen,
            modifier = Modifier.size(80.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "What should we call you?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter your name to personalize your experience.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = userName,
            onValueChange = onUserNameChange,
            placeholder = { Text("Your name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandGreen,
                cursorColor = BrandGreen,
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onDone() }),
        )
    }
}

@Composable
private fun BottomBar(
    pageCount: Int,
    currentPage: Int,
    isLastPage: Boolean,
    userName: String,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    onGetStarted: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 24.dp),
        ) {
            repeat(pageCount) { index ->
                val isSelected = index == currentPage
                val color by animateColorAsState(
                    targetValue = if (isSelected) BrandGreen else Color.LightGray,
                    label = "dot",
                )
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 24.dp else 8.dp, 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color),
                )
            }
        }

        if (isLastPage) {
            Button(
                onClick = onGetStarted,
                enabled = userName.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandGreen),
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        } else {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandGreen),
            ) {
                Text(
                    text = "Next",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onSkip) {
                Text(
                    text = "Skip",
                    color = Color.Gray,
                    fontSize = 16.sp,
                )
            }
        }
    }
}
