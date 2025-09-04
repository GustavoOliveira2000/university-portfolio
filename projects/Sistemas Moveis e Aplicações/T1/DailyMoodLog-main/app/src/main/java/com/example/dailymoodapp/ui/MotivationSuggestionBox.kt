package com.example.dailymoodapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailymoodapp.data.MotivationSuggestionEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable

fun MotivationSuggestionBox(
    moodEmoji: String,
    onLoaded: (() -> Unit)? = null
) {
    var isLoading by remember { mutableStateOf(true) }
    var suggestion by remember { mutableStateOf("") }
    var showSuggestion by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(0) }
    var hasCalledOnLoaded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    // Loading dots animation
    val loadingDots by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    LaunchedEffect(moodEmoji, refreshTrigger) {
        isLoading = true
        showSuggestion = false
        hasCalledOnLoaded = false
        
        // Simulate loading time
        delay(MotivationSuggestionEngine.loadingTime())
        
        // Get suggestion
        suggestion = MotivationSuggestionEngine.getForMood(moodEmoji)
        
        // Show suggestion with animation
        isLoading = false
        delay(200)
        showSuggestion = true
    }
    
    // Call onLoaded when suggestion is visible for the first time after loading
    LaunchedEffect(showSuggestion) {
        if (showSuggestion && !hasCalledOnLoaded) {
            hasCalledOnLoaded = true
            onLoaded?.invoke()
        }
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header and refresh button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Motivation",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Motivation for You",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                
                // Refresh button (only show when suggestion is loaded)
                AnimatedVisibility(
                    visible = showSuggestion,
                    enter = fadeIn(animationSpec = tween(300)) + scaleIn(
                        animationSpec = tween(300),
                        initialScale = 0.8f
                    ),
                    exit = fadeOut(animationSpec = tween(200)) + scaleOut(
                        animationSpec = tween(200),
                        targetScale = 0.8f
                    )
                ) {
                    IconButton(
                        onClick = { 
                            refreshTrigger++ // Refresh
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Get new motivation",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Loading or suggestion content
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) + slideInVertically(
                        animationSpec = tween(300),
                        initialOffsetY = { it }
                    ) with fadeOut(animationSpec = tween(300)) + slideOutVertically(
                        animationSpec = tween(300),
                        targetOffsetY = { -it }
                    )
                }
            ) { loading ->
                if (loading) {
                    // Loading animation
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Generating personalized motivation...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Loading dots
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            repeat(3) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(
                                            MaterialTheme.colorScheme.primary.copy(
                                                alpha = 0.3f + (0.7f * (loadingDots + index * 0.2f) % 1f)
                                            )
                                        )
                                )
                            }
                        }
                    }
                } else {
                    // Suggestion content
                    AnimatedVisibility(
                        visible = showSuggestion,
                        enter = fadeIn(animationSpec = tween(500)) + expandVertically(
                            animationSpec = tween(500)
                        ),
                        exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(
                            animationSpec = tween(300)
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Decorative quote marks
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "\"",
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "\"",
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Suggestion text
                            Text(
                                text = suggestion,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                lineHeight = 24.sp
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Decorative line
                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(2.dp)
                                    .clip(RoundedCornerShape(1.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                            )
                        }
                    }
                }
            }
        }
    }
} 