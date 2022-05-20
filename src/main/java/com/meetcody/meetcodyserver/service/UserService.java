package com.meetcody.meetcodyserver.service;

import com.meetcody.meetcodyserver.domain.BaseTimeEntity;
import com.meetcody.meetcodyserver.domain.user.User;
import com.meetcody.meetcodyserver.domain.user.UserRepository;
import com.meetcody.meetcodyserver.mobile.dto.user.UserSaveRequestDto;
import com.meetcody.meetcodyserver.mobile.dto.user.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class UserService extends BaseTimeEntity {
    private final UserRepository userRepository;

    @Transactional
    public Long save(UserSaveRequestDto requestDto) {
        return userRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id="+ id));

        user.update(requestDto.getFamilyName(),
                requestDto.getGivenName(),
                requestDto.getUsername(),
                requestDto.getEmail(),
                requestDto.getPhone(),
                requestDto.getPicture());

        return id;
    }
}
