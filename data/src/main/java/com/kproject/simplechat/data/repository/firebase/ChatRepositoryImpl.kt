package com.kproject.simplechat.data.repository.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.data.mapper.toChatMessageModel
import com.kproject.simplechat.data.mapper.toUserModel
import com.kproject.simplechat.data.model.ChatMessageEntity
import com.kproject.simplechat.data.utils.Constants
import com.kproject.simplechat.data.utils.Utils
import com.kproject.simplechat.domain.model.firebase.ChatMessageModel
import com.kproject.simplechat.domain.model.firebase.LatestMessageModel
import com.kproject.simplechat.domain.repository.firebase.ChatRepository
import com.kproject.simplechat.domain.repository.firebase.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

private const val TAG = "ChatRepositoryImpl"

class ChatRepositoryImpl(
    private val firebaseFirestore: FirebaseFirestore,
    private val userRepository: UserRepository
) : ChatRepository {

    override suspend fun getMessages(fromUserId: String) = callbackFlow {
        var snapshotListener: ListenerRegistration? = null
        try {
            val messageList = mutableListOf<ChatMessageEntity>()
            userRepository.getLoggedUserId()?.let { loggedUserId ->
                val chatRoomId = Utils.createChatRoomId(fromUserId, loggedUserId)
                val docReference = firebaseFirestore
                    .collection(Constants.FirebaseCollectionChatMessages)
                    .document(chatRoomId).collection(Constants.FirebaseCollectionMessages)
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                snapshotListener = docReference.addSnapshotListener { querySnapshot, e ->
                    querySnapshot?.let {
                        // Avoid submitting a duplicate list
                        if (messageList.isNotEmpty()) {
                            messageList.clear()
                        }

                        for (document in it.documents) {
                            document.toObject(ChatMessageEntity::class.java)?.let { message ->
                                messageList.add(message)
                            }
                        }

                        val messages = messageList.map { chatMessageEntity ->
                            chatMessageEntity.toChatMessageModel()
                        }

                        trySend(DataState.Success(data = messages))
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error getMessages(): ${e.message}")
            trySend(DataState.Error())
        }

        awaitClose { snapshotListener?.remove() }
    }

    override suspend fun sendMessage(message: ChatMessageModel): DataState<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun saveLatestMessage(latestMessageModel: LatestMessageModel): DataState<Unit> {
        TODO("Not yet implemented")
    }
}