package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.viewmodel.AiAssistantViewModel
import com.example.viewmodel.IdeaViewModel

@Composable
fun ProfileScreen(viewModel: IdeaViewModel, aiViewModel: AiAssistantViewModel) {
    val allIdeas by viewModel.allIdeas.collectAsStateWithLifecycle()
    val aiMessages by aiViewModel.messages.collectAsStateWithLifecycle()
    
    val totalIdeas = allIdeas.size
    val projectsCreated = allIdeas.count { it.category == "Project" }
    val completedIdeas = allIdeas.count { it.projectData != null && it.category == "Project" } // Approximation
    val aiConversations = aiMessages.size / 2 // Approximation of turns

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariantDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = "Profile", tint = NeonBlue, modifier = Modifier.size(48.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "User",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Premium Member",
                color = NeonPurple,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Brain Statistics",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatBox(modifier = Modifier.weight(1f), title = "Total Drops", value = totalIdeas.toString(), color = NeonPurple)
                StatBox(modifier = Modifier.weight(1f), title = "Projects", value = projectsCreated.toString(), color = NeonBlue)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatBox(modifier = Modifier.weight(1f), title = "AI Chats", value = aiConversations.toString(), color = NeonPurple)
                StatBox(modifier = Modifier.weight(1f), title = "Completed", value = completedIdeas.toString(), color = Color(0xFF00E676))
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "Settings",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SettingsItem("Theme", "Dark Mode")
            Spacer(modifier = Modifier.height(8.dp))
            SettingsItem("AI Preferences", "Gemini 1.5 Flash")
            Spacer(modifier = Modifier.height(8.dp))
            SettingsItem("Data Export", "Backup to JSON")
        }
    }
}

@Composable
fun StatBox(modifier: Modifier = Modifier, title: String, value: String, color: Color) {
    Surface(
        modifier = modifier.aspectRatio(1.5f),
        color = SurfaceDark,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, color = color, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, color = TextSecondary, fontSize = 12.sp)
        }
    }
}

@Composable
fun SettingsItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = Color.White, fontSize = 16.sp)
        Text(text = value, color = TextSecondary, fontSize = 14.sp)
    }
}
