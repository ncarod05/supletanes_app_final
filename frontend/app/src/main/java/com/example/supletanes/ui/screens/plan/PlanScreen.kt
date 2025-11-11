package com.example.supletanes.ui.screens.plan

import android.Manifest
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.supletanes.notifications.RecordatorioCal
import com.example.supletanes.ui.screens.plan.components.CalorieTracker
import com.example.supletanes.ui.screens.plan.components.PlanSection
import com.example.supletanes.ui.screens.plan.components.ProgressCheckInSection
import com.example.supletanes.ui.screens.plan.components.WeekTimeline
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanScreen() {
    val context = LocalContext.current
    var showCalendarDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val desayunoImage = remember { mutableStateOf<Bitmap?>(null) }
    val almuerzoImage = remember { mutableStateOf<Bitmap?>(null) }
    val cenaImage = remember { mutableStateOf<Bitmap?>(null) }
    val snacksImage = remember { mutableStateOf<Bitmap?>(null) }

    val desayunoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) desayunoImage.value = bitmap
    }

    val almuerzoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) almuerzoImage.value = bitmap
    }

    val cenaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) cenaImage.value = bitmap
    }

    val snacksLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) snacksImage.value = bitmap
    }

    val activeSection = remember { mutableStateOf<String?>(null) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                when (activeSection.value) {
                    "desayuno" -> desayunoLauncher.launch()
                    "almuerzo" -> almuerzoLauncher.launch()
                    "cena" -> cenaLauncher.launch()
                    "snacks" -> snacksLauncher.launch()
                }
            } else {
                Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    )

    if (showCalendarDialog) {
        DatePickerDialog(
            onDismissRequest = { showCalendarDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCalendarDialog = false
                        datePickerState.selectedDateMillis?.let { selectedMillis ->
                            val now = System.currentTimeMillis()
                            val delay = selectedMillis - now
                            if (delay > 0) {
                                val workRequest = OneTimeWorkRequestBuilder<RecordatorioCal>()
                                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                                    .setInputData(Data.Builder().putString("KEY_MESSAGE", "Recordatorio de tu plan.").build())
                                    .build()
                                WorkManager.getInstance(context).enqueue(workRequest)
                            }
                        }
                    }
                ) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showCalendarDialog = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    val dailyGoalCalories = 2500

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        item {
            Text(
                text = "Mi Plan Diario",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            Button(
                onClick = { showCalendarDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Crear Recordatorio")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Crear Recordatorio")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            WeekTimeline()
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            CalorieTracker(
                consumedCalories = 1200,
                goalCalories = dailyGoalCalories,
                protein = 80,
                goalProtein = 150,
                carbs = 150,
                goalCarbs = 300,
                fats = 50,
                goalFats = 100
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            PlanSection(
                title = "Desayuno",
                sectionCalories = 450,
                goalCalories = dailyGoalCalories,
                image = desayunoImage.value,
                onAddItemClicked = {
                    activeSection.value = "desayuno"
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp))
        }

        item {
            PlanSection(
                title = "Almuerzo",
                sectionCalories = 600,
                goalCalories = dailyGoalCalories,
                image = almuerzoImage.value,
                onAddItemClicked = {
                    activeSection.value = "almuerzo"
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp))
        }

        item {
            PlanSection(
                title = "Cena",
                sectionCalories = 150,
                goalCalories = dailyGoalCalories,
                image = cenaImage.value,
                onAddItemClicked = {
                    activeSection.value = "cena"
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp))
        }

        item {
            PlanSection(
                title = "Snacks",
                sectionCalories = 0,
                goalCalories = dailyGoalCalories,
                image = snacksImage.value,
                onAddItemClicked = {
                    activeSection.value = "snacks"
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Divider(modifier = Modifier.padding(bottom = 16.dp))

            ProgressCheckInSection(
                userGoal = "Perder 5 kg",
                initialWeight = 85.0,
                currentWeight = 82.5,
                onWeightCheckInClicked = {
                    println("Iniciando Check-in de Peso...")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlanScreenPreview() {
    PlanScreen()
}
