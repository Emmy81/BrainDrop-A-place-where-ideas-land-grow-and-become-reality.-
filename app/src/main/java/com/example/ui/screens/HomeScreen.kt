package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.Idea
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceVariantDark
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.IdeaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: IdeaViewModel,
    onNavigateToCreate: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToAi: () -> Unit
) {
    val allIdeas by viewModel.allIdeas.collectAsStateWithLifecycle()

    val ideaCount = allIdeas.count { it.category == "Idea" }
    val projectCount = allIdeas.count { it.category == "Project" }
    val taskCount = allIdeas.count { it.category == "Task" }
    val aiInsightCount = allIdeas.count { it.aiResponse != null }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "Good morning \uD83D\uDC4B",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Capture your next breakthrough idea.",
                    fontSize = 16.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                QuickCaptureBox(onNavigateToCreate, onNavigateToAi)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Brain",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = "View all",
                        fontSize = 14.sp,
                        color = NeonBlue
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(Modifier.weight(1f), "Ideas", ideaCount.toString(), Icons.Default.Lightbulb, NeonPurple)
                    StatCard(Modifier.weight(1f), "Projects", projectCount.toString(), Icons.Default.Folder, NeonBlue)
                    StatCard(Modifier.weight(1f), "Tasks", taskCount.toString(), Icons.Default.Task, Color(0xFF00E676))
                    StatCard(Modifier.weight(1f), "AI Insights", aiInsightCount.toString(), Icons.Default.AutoAwesome, NeonPurple)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text(
                    text = "Recent Drops",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (allIdeas.isEmpty()) {
                item {
                    Text(
                        text = "No ideas captured yet. Drop one above!",
                        color = TextSecondary,
                        modifier = Modifier.padding(vertical = 32.dp)
                    )
                }
            } else {
                items(allIdeas.take(5)) { idea ->
                    IdeaCard(idea = idea, onClick = { onNavigateToDetail(idea.id) })
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun QuickCaptureBox(onCreateClick: () -> Unit, onAiClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCreateClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark.copy(alpha = 0.7f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonBlue.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Drop a thought...",
                    color = TextSecondary,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onAiClick() }
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(NeonPurple.copy(alpha = 0.5f), Color.Transparent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI",
                        tint = NeonBlue
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Voice", tint = TextSecondary, modifier = Modifier.size(24.dp))
                Icon(Icons.Default.PhotoCamera, contentDescription = "Photo", tint = TextSecondary, modifier = Modifier.size(24.dp))
                
                Spacer(modifier = Modifier.weight(1f))
                
                Button(
                    onClick = onCreateClick,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = NeonPurple, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Drop Idea", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, count: String, icon: ImageVector, color: Color) {
    Card(
        modifier = modifier.aspectRatio(0.8f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            
            Column {
                Text(
                    text = title,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = count,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun IdeaCard(idea: Idea, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariantDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (idea.category) {
                        "Project" -> Icons.Default.Folder
                        "Task" -> Icons.Default.Task
                        "Note" -> Icons.Default.Edit
                        else -> Icons.Default.Lightbulb
                    },
                    contentDescription = idea.category,
                    tint = NeonPurple
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = idea.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault()).format(Date(idea.createdDate)),
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Surface(
                color = SurfaceVariantDark,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = idea.category,
                    color = TextSecondary,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
