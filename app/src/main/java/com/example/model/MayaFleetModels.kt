package com.example.model

data class MayaFeatureItem(
    val id: String,
    val number: Int,
    val title: String,
    val hindiTitle: String,
    val statusTag: String, // "🟢 Existing", "🟡 Upgrade", "🔴 New Feature"
    val category: String, // "PC & System", "Vision & Media", "Intelligence & Coding", "Communication & Finance", "Autonomous Fleet"
    val iconName: String,
    val description: String,
    val capabilities: List<String>,
    val voiceCommands: List<String>,
    val actionType: String
)

data class MayaSubAgentStatus(
    val id: String,
    val name: String,
    val role: String,
    val currentTask: String,
    val progress: Float,
    val status: String, // "Active", "Idle", "Completed"
    val lastLog: String
)

data class MacroRecordedStep(
    val stepNumber: Int,
    val actionName: String,
    val target: String,
    val parameter: String
)

data class MacroItem(
    val id: String,
    val name: String,
    val description: String,
    val steps: List<MacroRecordedStep>,
    val triggerVoice: String,
    val isScheduled: Boolean = false
)

data class ClipboardHistoryItem(
    val id: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val sourceApp: String = "System"
)

data class ExpenseItem(
    val id: String,
    val merchant: String,
    val category: String,
    val amount: Double,
    val paymentMode: String, // "UPI", "Credit Card", "NetBanking", "Bank Email"
    val date: String,
    val notes: String = ""
)

data class ExcelCell(
    val row: Int,
    val col: String,
    val value: String,
    val formula: String = ""
)

data class StockQuote(
    val symbol: String,
    val name: String,
    val price: Double,
    val changePercent: Double,
    val high52: Double,
    val low52: Double
)
