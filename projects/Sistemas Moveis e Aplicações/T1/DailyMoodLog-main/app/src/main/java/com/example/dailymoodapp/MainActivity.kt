package com.example.dailymoodapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.dailymoodapp.data.Mood
import com.example.dailymoodapp.ui.MotivationSuggestionBox
import com.example.dailymoodapp.ui.DailyActivitySuggestionBox
import com.example.dailymoodapp.ui.MusicSuggestionBox
import com.example.dailymoodapp.viewmodel.MoodViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private val viewModel: MoodViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MoodViewModel) {
    val moods by viewModel.moodList.collectAsState(initial = emptyList())
    var selectedMood by remember { mutableStateOf<Mood?>(null) }
    var showEditScreen by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    
    if (showEditScreen) {
        MoodEditScreen(
            mood = selectedMood,
            onSave = { updatedMood ->
                if (selectedMood == null) {
                    viewModel.moodEkle(updatedMood)
                } else {
                    viewModel.moodGuncelle(updatedMood)
                }
                showEditScreen = false
                selectedMood = null
            },
            onBackClick = {
                showEditScreen = false
                selectedMood = null
            }
        )
    } else if (selectedMood != null) {
        MoodDetailScreen(
            mood = selectedMood!!,
            onBackClick = { selectedMood = null },
            onEditClick = { showEditScreen = true },
            onDeleteClick = { showDeleteConfirmation = true }
        )
        
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Delete Mood") },
                text = { Text("Are you sure you want to delete this mood entry?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.moodSil(selectedMood!!)
                            selectedMood = null
                            showDeleteConfirmation = false
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmation = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { 
                        Text(
                            "MoodLog",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        selectedMood = null
                        showEditScreen = true
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Mood")
                }
            }
        ) { paddingValues ->
            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                GreetingByTime()
                if (moods.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No moods yet. Tap the '+' button to add your first mood.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(moods) { mood ->
                            MoodItem(
                                mood = mood,
                                onClick = { selectedMood = mood }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodDetailScreen(
    mood: Mood,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()) }
    var motivationVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mood Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = mood.emoji,
                    style = MaterialTheme.typography.displayLarge
                )
            }
            item {
                Text(
                    text = dateFormat.format(mood.date),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = mood.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            item {
                MotivationSuggestionBox(
                    moodEmoji = mood.emoji,
                    onLoaded = { motivationVisible = true }
                )
            }
            item {
                DailyActivitySuggestionBox(
                    moodEmoji = mood.emoji,
                    visible = motivationVisible
                )
            }
            item {
                MusicSuggestionBox(
                    moodEmoji = mood.emoji,
                    visible = motivationVisible
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodEditScreen(
    mood: Mood?,
    onSave: (Mood) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedEmoji by remember { mutableStateOf(mood?.emoji ?: "😊") }
    var description by remember { mutableStateOf(mood?.description ?: "") }
    var showSaveConfirmation by remember { mutableStateOf(false) }
    
    val emojis = listOf("😊", "😌", "😢", "😡", "😴", "😍", "😔")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (mood == null) "Add Mood" else "Edit Mood") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Emoji selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                emojis.forEach { emoji ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                if (emoji == selectedEmoji) 
                                    MaterialTheme.colorScheme.primaryContainer 
                                else 
                                    Color.Transparent
                            )
                            .border(
                                width = 2.dp,
                                color = if (emoji == selectedEmoji) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { selectedEmoji = emoji },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emoji,
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }
            }
            
            // Description input
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("How are you feeling?") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Save button
            Button(
                onClick = {
                    val newMood = Mood(
                        id = mood?.id ?: 0,
                        date = mood?.date ?: Date(),
                        emoji = selectedEmoji,
                        description = description
                    )
                    onSave(newMood)
                    showSaveConfirmation = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
        
        if (showSaveConfirmation) {
            AlertDialog(
                onDismissRequest = { showSaveConfirmation = false },
                title = { Text("Success") },
                text = { Text("Mood saved successfully.") },
                confirmButton = {
                    TextButton(onClick = { showSaveConfirmation = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodItem(mood: Mood, onClick: () -> Unit) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mood.emoji,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column {
                Text(
                    text = dateFormat.format(mood.date),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = mood.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun GreetingByTime() {
    val hour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val greeting = when (hour) {
        in 5..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        in 18..22 -> "Good evening"
        else -> "Good night"
    }
    Text(
        text = greeting,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 12.dp),
        textAlign = TextAlign.Center
    )
}