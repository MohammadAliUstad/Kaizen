package com.yugentech.kaizen.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yugentech.kaizen.data.auth.AuthUiState
import com.yugentech.kaizen.data.model.Habit
import com.yugentech.kaizen.navigation.Screens
import com.yugentech.kaizen.ui.components.DateCard
import com.yugentech.kaizen.ui.components.HabitCard
import com.yugentech.kaizen.ui.components.ProfileMenu
import com.yugentech.kaizen.ui.components.QuoteCard
import com.yugentech.kaizen.viewmodels.HabitsViewModel
import com.yugentech.kaizen.viewmodels.QuoteViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onSignOut: () -> Unit,
    state: AuthUiState,
    navController: NavController,
    viewModel: QuoteViewModel = koinViewModel(),
    habitsViewModel: HabitsViewModel = koinViewModel()
) {
    val quoteState by viewModel.quoteState.collectAsState()
    val habitsList by habitsViewModel.habits.collectAsState(initial = emptyList())
    var showProfileMenu by remember { mutableStateOf(false) }
    var showAddHabitDialog by remember { mutableStateOf(false) }
    var habitText by remember { mutableStateOf("") }

    val currentDate = LocalDate.now()
    val firstDay = currentDate.withDayOfMonth(1)
    val daysInMonth = currentDate.lengthOfMonth()
    val dates = (0 until daysInMonth).map { firstDay.plusDays(it.toLong()) }

    var selectedDate by remember { mutableStateOf(currentDate) }

    val lazyListState = rememberLazyListState(currentDate.dayOfMonth - 1)

    LaunchedEffect(selectedDate) {
        habitsViewModel.updateSelectedDate(selectedDate.toString())
        lazyListState.animateScrollToItem(selectedDate.dayOfMonth - 1)
    }

    var fabPressed by remember { mutableStateOf(false) }

    val fabScale by animateFloatAsState(
        targetValue = if (fabPressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    LaunchedEffect(fabPressed) {
        if (fabPressed) {
            delay(150)
            fabPressed = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "改善",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(onClick = { showProfileMenu = true }) {
                        if (state.userData?.profilePictureUrl?.isNotEmpty() == true) {
                            AsyncImage(
                                model = state.userData.profilePictureUrl,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(50))
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.scale(fabScale),
                onClick = {
                    fabPressed = true
                    showAddHabitDialog = true
                }
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(500)) + slideInVertically(),
                exit = fadeOut()
            ) {
                QuoteCard(quoteState)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                state = lazyListState,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(dates) { date ->
                    DateCard(
                        date = date,
                        isSelected = date == selectedDate,
                        onClick = { selectedDate = date }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(habitsList, key = { it.id }) { habit ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(300)) + expandVertically(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        HabitCard(
                            habit = habit,
                            selectedDate = selectedDate.toString(),
                            onCheckedChange = {
                                habitsViewModel.updateHabit(habit.id, selectedDate.toString(), it)
                            },
                            onDelete = { habitsViewModel.deleteHabit(habit.id) }
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showProfileMenu && state.userData != null,
            enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight }
            ),
            exit = fadeOut(animationSpec = tween(200)) + slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight }
            )
        ) {
            ProfileMenu(
                onDismiss = { showProfileMenu = false },
                username = state.userData?.username ?: "",
                email = state.userData?.email ?: "",
                profile = state.userData?.profilePictureUrl ?: "",
                onSignOut = onSignOut,
                onAboutClick = { navController.navigate(Screens.About.route) }
            )
        }


        if (showAddHabitDialog) {
            AlertDialog(
                onDismissRequest = { showAddHabitDialog = false },
                title = { Text("Add Habit") },
                text = {
                    OutlinedTextField(
                        value = habitText,
                        onValueChange = { habitText = it },
                        placeholder = { Text("Enter your habit") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (habitText.isNotBlank()) {
                            habitsViewModel.addHabit(
                                Habit(title = habitText, completionStatus = mutableMapOf())
                            )
                            habitText = ""
                            showAddHabitDialog = false
                        }
                    }) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddHabitDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}