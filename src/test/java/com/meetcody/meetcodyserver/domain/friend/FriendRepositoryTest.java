package com.meetcody.meetcodyserver.domain.friend;


import com.meetcody.meetcodyserver.domain.friend.Friend;
import com.meetcody.meetcodyserver.domain.friend.FriendRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendRepositoryTest {
    @Autowired
    FriendRepository friendRepository;

    @After
    public void cleanup() {
        friendRepository.deleteAll();
    }

    @Test
    public void 친구목록저장_불러오기() {
        // given
        String nickname = "테스트 닉네임";


        friendRepository.save(Friend.builder().nickname(nickname).build());

        //when
        List<Friend> friendList = friendRepository.findAll();

        //then
        Friend friend = friendList.get(0);
        assertThat(friend.getNickname()).isEqualTo(nickname);

    }

}
