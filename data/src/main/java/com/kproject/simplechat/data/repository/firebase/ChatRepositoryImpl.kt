package com.kproject.simplechat.data.repository.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.kproject.simplechat.commom.DataState
import com.kproject.simplechat.data.mapper.toEntity
import com.kproject.simplechat.data.mapper.toModel
import com.kproject.simplechat.data.model.ChatMessageEntity
import com.kproject.simplechat.data.model.LatestMessageEntity
import com.kproject.simplechat.data.utils.Constants
import com.kproject.simplechat.data.utils.Utils
import com.kproject.simplechat.domain.model.firebase.ChatMessageModel
import com.kproject.simplechat.domain.model.firebase.UserModel
import com.kproject.simplechat.domain.repository.firebase.ChatRepository
import com.kproject.simplechat.domain.repository.firebase.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

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
                    .orderBy("sendDate", Query.Direction.ASCENDING)
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
                            chatMessageEntity.toModel()
                        }

                        trySend(DataState.Success(data = messages))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getMessages(): ${e.message}")
            trySend(DataState.Error())
        }

        awaitClose { snapshotListener?.remove() }
    }

    override suspend fun sendMessage(message: ChatMessageModel): DataState<Unit> {
        return try {
            val chatRoomId = Utils.createChatRoomId(message.senderId, message.receiverId)
            firebaseFirestore
                .collection(Constants.FirebaseCollectionChatMessages)
                .document(chatRoomId)
                .collection(Constants.FirebaseCollectionMessages)
                .add(message.toEntity())
                .await()
            DataState.Success()
        } catch (e: Exception) {
            Log.e(TAG, "Error sendMessage(): ${e.message}")
            DataState.Error()
        }
    }

    override suspend fun saveLatestMessage(
        user: UserModel,
        chatMessage: ChatMessageModel
    ): DataState<Unit> {
        val result = userRepository.getCurrentUser()
        if (result is DataState.Success) {
            result.data?.let { currentUser ->
                val currentLoggedUserId = currentUser.userId
                val secondUserId = chatMessage.receiverId

                val lastMessageOfCurrentUser = LatestMessageEntity(
                    chatId = secondUserId,
                    latestMessage = chatMessage.message,
                    senderId = chatMessage.senderId,
                    receiverId = chatMessage.receiverId,
                    username = user.username,
                    userProfilePicture = user.profilePicture
                )

                val lastMessageOfSecondUser = LatestMessageEntity(
                    chatId = currentLoggedUserId,
                    latestMessage = chatMessage.message,
                    senderId = chatMessage.senderId,
                    receiverId = chatMessage.receiverId,
                    username = currentUser.username,
                    userProfilePicture = currentUser.profilePicture
                )

                /**
                 * Save both the last message in each document which is
                 * represented by the user id.
                 */
                saveLatestMessageInFirestore(
                    message = lastMessageOfCurrentUser,
                    userId = currentLoggedUserId,
                    chatId = secondUserId
                )

                saveLatestMessageInFirestore(
                    message = lastMessageOfSecondUser,
                    userId = secondUserId,
                    chatId = currentLoggedUserId
                )
            }
        }
        return DataState.Error()
    }

    private suspend fun saveLatestMessageInFirestore(
        message: LatestMessageEntity,
        userId: String,
        chatId: String
    ) {
        firebaseFirestore
            .collection(Constants.FirebaseCollectionLatestMessages)
            .document(userId)
            .collection(Constants.FirebaseCollectionMessages)
            .document(chatId)
            .set(message)
            .await()
    }
 }