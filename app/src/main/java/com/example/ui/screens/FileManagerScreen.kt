package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ManagedFileItem
import com.example.ui.theme.*
import com.example.viewmodel.MyraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileManagerScreen(
    viewModel: MyraViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val files by viewModel.currentFileList.collectAsState()
    val searchQuery by viewModel.fileSearchQuery.collectAsState()
    val currentFilter by viewModel.fileFilterCategory.collectAsState()
    val readingFileContent by viewModel.readingFileContent.collectAsState()
    val readingFileName by viewModel.readingFileName.collectAsState()

    var showBatchRenameDialog by remember { mutableStateOf(false) }
    var renamePrefix by remember { mutableStateOf("MYRA_Archive") }
    var showCreateZipDialog by remember { mutableStateOf(false) }
    var newZipName by remember { mutableStateOf("Project_Backup") }

    val categories = listOf(
        "ALL" to "All Files",
        "DOCS" to "Documents",
        "MEDIA" to "Photos & Media",
        "ARCHIVES" to "ZIP Archives",
        "PC" to "PC Workstation"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Advanced File Manager",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Fuzzy search • Batch Rename • ZIP • Voice Readout",
                            style = MaterialTheme.typography.bodySmall,
                            color = MyraTextMuted
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { showBatchRenameDialog = true }) {
                        Icon(Icons.Default.DriveFileRenameOutline, contentDescription = "Batch Rename", tint = MyraNeonCyan)
                    }
                    IconButton(onClick = { showCreateZipDialog = true }) {
                        Icon(Icons.Default.FolderZip, contentDescription = "Create ZIP", tint = PurpleAccent)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MyraDarkBg)
            )
        },
        containerColor = MyraDarkBg,
        modifier = modifier.testTag("file_manager_screen")
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Input Field
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchFiles(it) },
                    placeholder = { Text("Fuzzy filename search (e.g. invoice, .pdf, bridge)", color = Color(0xFF71717A)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFFA1A1AA)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.searchFiles("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color(0xFFA1A1AA))
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_file_search"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MyraCardDark,
                        unfocusedContainerColor = MyraCardDark,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = PurpleAccent
                    ),
                    singleLine = true
                )
            }

            // Category Filter Pills
            ScrollableTabRow(
                selectedTabIndex = categories.indexOfFirst { it.first == currentFilter }.coerceAtLeast(0),
                containerColor = Color.Transparent,
                contentColor = Color.White,
                edgePadding = 16.dp,
                indicator = {}
            ) {
                categories.forEach { (catId, label) ->
                    val isSelected = currentFilter == catId
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (isSelected) PurpleAccent else MyraCardDark,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) PurpleAccent else Color(0xFF27272A)
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp, top = 4.dp, bottom = 4.dp)
                            .clickable { viewModel.setFileFilter(catId) }
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else Color(0xFFA1A1AA),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // Voice Reading Aloud Player Banner
            AnimatedVisibility(visible = readingFileContent != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(PurpleAccent, MyraNeonCyan))
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Reading", tint = MyraNeonCyan)
                                Text(
                                    text = "Reading Aloud: ${readingFileName ?: "File"}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                            }
                            IconButton(onClick = { viewModel.stopReadFileContent() }) {
                                Icon(Icons.Default.Close, contentDescription = "Stop", tint = Color.White)
                            }
                        }
                        Text(
                            text = readingFileContent ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE0E7FF),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Files List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${files.size} items indexed",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFA1A1AA)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF18181B),
                                modifier = Modifier.clickable { showBatchRenameDialog = true }
                            ) {
                                Text("Batch Rename", color = MyraNeonCyan, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF18181B),
                                modifier = Modifier.clickable { showCreateZipDialog = true }
                            ) {
                                Text("+ ZIP Folder", color = PurpleAccent, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                        }
                    }
                }

                items(files, key = { it.id }) { file ->
                    FileItemCard(
                        file = file,
                        onReadAloud = { viewModel.readFileContentAloud(file.id) },
                        onDelete = { viewModel.deleteManagedFile(file.id) },
                        onUnzip = { viewModel.unzipArchive(file.id) }
                    )
                }
            }
        }
    }

    // Batch Rename Dialog
    if (showBatchRenameDialog) {
        AlertDialog(
            onDismissRequest = { showBatchRenameDialog = false },
            title = { Text("Batch Rename Files", fontWeight = FontWeight.Bold, color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Rename all indexed files sequentially with a prefix and counter format (e.g. Doc_Archive_1.pdf)", color = Color(0xFFD4D4D8), fontSize = 13.sp)
                    OutlinedTextField(
                        value = renamePrefix,
                        onValueChange = { renamePrefix = it },
                        label = { Text("Prefix Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.batchRenameFiles(renamePrefix)
                        showBatchRenameDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent)
                ) {
                    Text("Rename All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBatchRenameDialog = false }) {
                    Text("Cancel", color = Color(0xFFA1A1AA))
                }
            },
            containerColor = MyraCardDarkElevated
        )
    }

    // Create ZIP Dialog
    if (showCreateZipDialog) {
        AlertDialog(
            onDismissRequest = { showCreateZipDialog = false },
            title = { Text("Create ZIP Archive", fontWeight = FontWeight.Bold, color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Bundle selected workspace and document files into a compressed ZIP file.", color = Color(0xFFD4D4D8), fontSize = 13.sp)
                    OutlinedTextField(
                        value = newZipName,
                        onValueChange = { newZipName = it },
                        label = { Text("Archive Name (.zip)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.zipFiles(files.map { it.id }, newZipName)
                        showCreateZipDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent)
                ) {
                    Text("Create Archive")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateZipDialog = false }) {
                    Text("Cancel", color = Color(0xFFA1A1AA))
                }
            },
            containerColor = MyraCardDarkElevated
        )
    }
}

@Composable
private fun FileItemCard(
    file: ManagedFileItem,
    onReadAloud: () -> Unit,
    onDelete: () -> Unit,
    onUnzip: () -> Unit
) {
    val (icon, iconColor) = when (file.extension.lowercase()) {
        "pdf" -> Icons.Default.PictureAsPdf to Color(0xFFEF4444)
        "docx", "doc", "txt" -> Icons.Default.Description to Color(0xFF3B82F6)
        "png", "jpg", "jpeg" -> Icons.Default.Image to Color(0xFF10B981)
        "zip", "rar" -> Icons.Default.FolderZip to Color(0xFFF59E0B)
        "py", "kt", "js" -> Icons.Default.Code to Color(0xFFA855F7)
        "mp3", "wav" -> Icons.Default.Audiotrack to Color(0xFFEC4899)
        else -> Icons.Default.InsertDriveFile to Color(0xFFA1A1AA)
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MyraCardDark),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF27272A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(iconColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, contentDescription = file.extension, tint = iconColor, modifier = Modifier.size(22.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = file.name,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (file.isPcFile) {
                                Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF1E293B)) {
                                    Text("PC", color = MyraNeonCyan, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                        }
                        Text(
                            text = "${file.sizeString} • ${file.modifiedDate}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFA1A1AA)
                        )
                    }
                }

                // Quick Action Buttons
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (file.textPreview != null) {
                        IconButton(onClick = onReadAloud, modifier = Modifier.size(34.dp)) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Read Aloud", tint = MyraNeonCyan, modifier = Modifier.size(18.dp))
                        }
                    }
                    if (file.extension == "zip") {
                        IconButton(onClick = onUnzip, modifier = Modifier.size(34.dp)) {
                            Icon(Icons.Default.Unarchive, contentDescription = "Unzip", tint = Color(0xFFF59E0B), modifier = Modifier.size(18.dp))
                        }
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(34.dp)) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color(0xFF71717A), modifier = Modifier.size(18.dp))
                    }
                }
            }

            // Path & Text Preview preview pill
            if (file.textPreview != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF0F111A),
                    modifier = Modifier.fillMaxWidth().clickable { onReadAloud() }
                ) {
                    Text(
                        text = "\"${file.textPreview}\"",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF94A3B8),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
