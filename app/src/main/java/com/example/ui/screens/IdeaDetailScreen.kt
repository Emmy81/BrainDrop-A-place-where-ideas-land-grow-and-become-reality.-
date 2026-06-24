package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.viewmodel.IdeaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IdeaDetailScreen(
    ideaId: Int,
    viewModel: IdeaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAi: () -> Unit
) {
    // We observe the specific idea. A more robust way is to have the viewmodel expose it
    val ideaFlow = viewModel.getIdeaById(ideaId) // Need to add this to ViewModel
    // Let's implement a workaround using allIdeas since getIdeaById is not exposed as StateFlow
    val allIdeas by viewModel.allIdeas.collectAsStateWithLifecycle()
    val idea = allIdeas.find { it.id == ideaId }
    
    val isAnalyzing by viewModel.isAnalyzing.collectAsStateWithLifecycle()
    val currentAiResponse by viewModel.aiResponse.collectAsStateWithLifecycle()

    LaunchedEffect(currentAiResponse) {
        if (idea != null && currentAiResponse != null) {
            viewModel.updateIdeaAiResponse(idea, currentAiResponse!!)
            viewModel.clearAiResponse()
        }
    }

    if (idea == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = NeonBlue)
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                
                Surface(
                    color = SurfaceVariantDark,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = idea.category,
                        color = NeonBlue,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                IconButton(onClick = {
                    viewModel.deleteIdea(idea)
                    onNavigateBack()
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                item {
                    Text(
                        text = idea.title,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(idea.createdDate)),
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = idea.description,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }

                if (idea.aiResponse != null) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = NeonPurple)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "AI Expansion",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = idea.aiResponse,
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 15.sp,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                } else if (isAnalyzing) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = NeonPurple)
                        }
                    }
                } else {
                    item {
                        Button(
                            onClick = { viewModel.analyzeIdea(idea.title, idea.description, idea.category) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple.copy(alpha = 0.1f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "Analyze", tint = NeonPurple)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Analyze with AI", color = NeonPurple, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                
                if (idea.category == "Idea") {
                    item {
                        OutlinedButton(
                            onClick = {
                                viewModel.updateIdea(idea.copy(category = "Project"))
                                onNavigateBack()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonBlue),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonBlue)
                        ) {
                            Text("Convert to Project", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}
