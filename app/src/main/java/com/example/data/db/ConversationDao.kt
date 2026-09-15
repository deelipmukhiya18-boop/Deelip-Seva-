package com.example.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    @Query("SELECT * FROM conversations ORDER BY timestamp DESC")
    fun getAllConversationsFlow(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentConversations(limit: Int): List<ConversationEntity>

    @Query("SELECT * FROM conversations WHERE messageText LIKE '%' || :query || '%' OR keywords LIKE '%' || :query || '%' OR topic LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchConversationsFlow(query: String): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE messageText LIKE '%' || :query || '%' OR keywords LIKE '%' || :query || '%' OR topic LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    suspend fun searchConversationsSync(query: String): List<ConversationEntity>

    @Query("SELECT * FROM conversations WHERE topic = :topic ORDER BY timestamp DESC")
    fun getConversationsByTopicFlow(topic: String): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getConversationsBySessionFlow(sessionId: String): Flow<List<ConversationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: ConversationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations: List<ConversationEntity>)

    @Query("DELETE FROM conversations WHERE id = :id")
    suspend fun deleteConversationById(id: String)

    @Query("DELETE FROM conversations")
    suspend fun clearAllConversations()

    @Query("SELECT COUNT(*) FROM conversations")
    fun getTotalCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM conversations")
    suspend fun getTotalCountSync(): Int

    // Sessions
    @Query("SELECT * FROM conversation_sessions ORDER BY lastActiveTime DESC")
    fun getAllSessionsFlow(): Flow<List<ConversationSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ConversationSessionEntity)

    @Query("DELETE FROM conversation_sessions WHERE sessionId = :sessionId")
    suspend fun deleteSessionById(sessionId: String)
}
