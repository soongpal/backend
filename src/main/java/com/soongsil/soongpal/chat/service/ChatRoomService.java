package com.soongsil.soongpal.chat.service;

import com.soongsil.soongpal.board.domain.Board;
import com.soongsil.soongpal.board.domain.BoardCategory;
import com.soongsil.soongpal.board.repository.BoardRepository;
import com.soongsil.soongpal.chat.domain.ChatMessage;
import com.soongsil.soongpal.chat.domain.ChatRole;
import com.soongsil.soongpal.chat.domain.ChatRoom;
import com.soongsil.soongpal.chat.domain.ChatRoomUser;
import com.soongsil.soongpal.chat.dto.ChatRoomCreateReqDto;
import com.soongsil.soongpal.chat.dto.ChatRoomResDto;
import com.soongsil.soongpal.chat.dto.ChatRoomUserResDto;
import com.soongsil.soongpal.chat.repository.ChatMessageRepository;
import com.soongsil.soongpal.chat.repository.ChatRoomRepository;
import com.soongsil.soongpal.chat.repository.ChatRoomUserRepository;
import com.soongsil.soongpal.common.exception.BoardErrorCode;
import com.soongsil.soongpal.common.exception.BoardException;
import com.soongsil.soongpal.common.exception.ChatErrorCode;
import com.soongsil.soongpal.common.exception.ChatException;
import com.soongsil.soongpal.user.domain.User;
import com.soongsil.soongpal.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.soongsil.soongpal.chat.domain.ChatRoomType.GROUP;
import static com.soongsil.soongpal.chat.domain.ChatRoomType.PRIVATE;


@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    public ChatRoomResDto createPrivateChatRoom(ChatRoomCreateReqDto dto, Long userId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.USER_NOT_FOUND));
        Board findBoard = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
        User boardUser = userRepository.findById(findBoard.getUser().getId())
                .orElseThrow(() -> new ChatException(ChatErrorCode.USER_NOT_FOUND));

        Optional<ChatRoom> chatRoom = chatRoomRepository.existsByTwoUser(findUser.getId(), boardUser.getId());
        if (chatRoom.isPresent()) {
            List<ChatRoomUserResDto> users = chatRoom.get().getChatRoomUsers().stream()
                    .map(ChatRoomUserResDto::from)
                    .toList();

            return ChatRoomResDto.of(chatRoom.get(), boardUser.getNickName(), findBoard.getId(), findBoard.getTitle(), users, null, null);
        }

        ChatRoom savedRoom = chatRoomRepository.save(ChatRoomCreateReqDto.toEntity(PRIVATE, dto.getBoardId()));

        ChatRoomUser roomUser = ChatRoomUser.builder()
                .chatRoom(savedRoom)
                .user(findUser)
                .role(ChatRole.OWNER)
                .build();

        ChatRoomUser roomOwner = ChatRoomUser.builder()
                .chatRoom(savedRoom)
                .user(findBoard.getUser())
                .role(ChatRole.OWNER)
                .build();

        chatRoomUserRepository.save(roomOwner);
        chatRoomUserRepository.save(roomUser);

        savedRoom.addUser(roomOwner);
        savedRoom.addUser(roomUser);


        List<ChatRoomUserResDto> users = savedRoom.getChatRoomUsers().stream()
                .map(ChatRoomUserResDto::from)
                .toList();

        return ChatRoomResDto.of(savedRoom, boardUser.getNickName(), findBoard.getId(), findBoard.getTitle(), users, null, null);
    }

    public ChatRoomResDto createGroupChatRoom(Long userId, String chatRoomName, Long boardId) {
        ChatRoom savedRoom = chatRoomRepository.save(ChatRoomCreateReqDto.toEntity(GROUP, boardId));
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.USER_NOT_FOUND));

        ChatRoomUser roomUser = ChatRoomUser.builder()
                .chatRoom(savedRoom)
                .user(findUser)
                .role(ChatRole.OWNER)
                .build();
        chatRoomUserRepository.save(roomUser);
        savedRoom.addUser(roomUser);

        List<ChatRoomUserResDto> users = savedRoom.getChatRoomUsers().stream()
                .map(ChatRoomUserResDto::from)
                .toList();

        return ChatRoomResDto.of(savedRoom, chatRoomName, boardId, chatRoomName, users, null, null);
    }

    public ChatRoomResDto getChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByIdAndUserId(roomId, userId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_ACCESS_DENIED));

        Board findBoard = boardRepository.findById(chatRoom.getBoardId())
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        List<ChatRoomUserResDto> users = chatRoom.getChatRoomUsers().stream()
                .map(ChatRoomUserResDto::from)
                .toList();

        ChatMessage lastMessage = chatMessageRepository.findLastMessageByRoomId(chatRoom.getId())
                .orElse(null);
        String lastContent = lastMessage != null ? lastMessage.getContent() : null;
        LocalDateTime lastCreatedAt = lastMessage != null ? lastMessage.getCreatedAt() : chatRoom.getCreatedAt();

        if (findBoard.getCategory() == BoardCategory.USED) {
            return ChatRoomResDto.of(chatRoom, findBoard.getUser().getNickName(), findBoard.getId(), findBoard.getTitle(), users, lastContent, lastCreatedAt);
        }
        return ChatRoomResDto.of(chatRoom, findBoard.getTitle(), findBoard.getId(), findBoard.getTitle(), users, lastContent, lastCreatedAt);
    }

    public List<ChatRoomResDto> getChatRoomsByUser(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUserId(userId);
        return chatRooms.stream()
                .map(c -> boardRepository.findById(c.getBoardId())
                        .map(findBoard -> {
                            List<ChatRoomUserResDto> users = c.getChatRoomUsers().stream()
                                    .map(ChatRoomUserResDto::from)
                                    .toList();

                            ChatMessage lastMessage = chatMessageRepository.findLastMessageByRoomId(c.getId())
                                    .orElse(null);
                            String lastContent = lastMessage != null ? lastMessage.getContent() : null;
                            LocalDateTime lastCreatedAt = lastMessage != null ? lastMessage.getCreatedAt() : c.getCreatedAt();

                            if (findBoard.getCategory() == BoardCategory.USED) {
                                return ChatRoomResDto.of(c, findBoard.getUser().getNickName(),  findBoard.getId(), findBoard.getTitle(), users, lastContent, lastCreatedAt);
                            }
                            return ChatRoomResDto.of(c, findBoard.getTitle(),  findBoard.getId(), findBoard.getTitle(), users, lastContent, lastCreatedAt);
                        })
                )
                .flatMap(Optional::stream)
                .toList();
    }

    public ChatRoomResDto joinChatRoom(Long boardId, Long userId) {
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        ChatRoom findChatRoom = chatRoomRepository.findByBoardId(boardId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));
        
        if (findChatRoom.getType() == PRIVATE) {
            throw new ChatException(ChatErrorCode.CHAT_ROOM_ACCESS_DENIED);
        }

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.USER_NOT_FOUND));

        boolean alreadyJoined = chatRoomUserRepository.findByChatRoomIdAndUserId(findChatRoom.getId(), userId).isPresent();
        if (!alreadyJoined) {
            ChatRoomUser roomUser = ChatRoomUser.builder()
                    .chatRoom(findChatRoom)
                    .user(findUser)
                    .build();
            chatRoomUserRepository.save(roomUser);
            findChatRoom.addUser(roomUser);
        }

        List<ChatRoomUserResDto> users = findChatRoom.getChatRoomUsers().stream()
                .filter(user -> user.getRole().equals(ChatRole.OWNER))
                .map(ChatRoomUserResDto::from)
                .toList();

        return ChatRoomResDto.of(findChatRoom, findBoard.getTitle(),  findBoard.getId(), findBoard.getTitle(), users, null, null);
    }

    public ChatRoomResDto leaveChatRoom(Long roomId, Long userId) {
        ChatRoom findChatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));

        Board findBoard = boardRepository.findById(findChatRoom.getBoardId())
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        if (findChatRoom.getType() == PRIVATE) {
            throw new ChatException(ChatErrorCode.CHAT_ROOM_ACCESS_DENIED);
        }

        ChatRoomUser roomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(findChatRoom.getId(), userId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_JOINED));
        chatRoomUserRepository.delete(roomUser);
        findChatRoom.removeUser(roomUser);

        return ChatRoomResDto.of(findChatRoom, findBoard.getTitle(),  findBoard.getId(), findBoard.getTitle(), null, null, null);
    }

    public void deleteChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByIdAndUserId(roomId, userId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_DELETE_DENIED));

        ChatRoomUser roomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(roomId, userId)
                        .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_JOINED));

        if (roomUser.getRole().equals(ChatRole.MEMBER)) {
            throw new ChatException(ChatErrorCode.CHAT_ROOM_DELETE_DENIED);
        }
        chatRoom.softDelete();
    }

    public void updateLastReadMessage(Long roomId, Long userId, Long messageId) {
        ChatRoomUser roomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_JOINED));

        roomUser.updateLastReadMessage(messageId);
    }

}
