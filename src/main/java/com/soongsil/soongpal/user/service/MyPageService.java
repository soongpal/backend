package com.soongsil.soongpal.user.service;

import com.soongsil.soongpal.user.domain.User;
import com.soongsil.soongpal.user.dto.InfoUpdateReqDto;
import com.soongsil.soongpal.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    public String updateInfo(Long userId, InfoUpdateReqDto infoUpdateReqDto) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        findUser.changeInfo(infoUpdateReqDto.getNickName());
        return infoUpdateReqDto.getNickName();
    }

}
