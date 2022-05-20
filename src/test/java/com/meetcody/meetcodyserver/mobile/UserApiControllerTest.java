package com.meetcody.meetcodyserver.mobile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetcody.meetcodyserver.domain.user.User;
import com.meetcody.meetcodyserver.domain.user.UserRepository;
import com.meetcody.meetcodyserver.mobile.dto.user.UserSaveRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserApiControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
//                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

//
//    @Test
//    public void User_등록한다() throws Exception {
//        // given
//        String familyName = "sun";
//        String givenName = "clock";
//        String username = "sunclock";
//        String email= "dev.sunclock@gmail.com";
//        String phone = "010-1234-5678";
//        String picture = "https://s3.pictures.com";
//
//        userRepository.save(User.builder()
//                .familyName(familyName)
//                .givenName(givenName)
//                .username(username)
//                .email(email)
//                .phone(phone)
//                .picture(picture)
//                .build());
//
//        // when
//        List<User> userList = userRepository.findAll();
//
//        // then
//        User user = userList.get(0);
//        assertThat(user.getUsername()).isEqualTo(username);
//        assertThat(user.getPhone()).isEqualTo(phone);
//        assertThat(user.getId()).isNotNull();
//    }

    @Test
    public void User_인증한다() throws Exception {
        // given
        String familyName = "sun";
        String givenName = "clock";
        String username = "sunclock";
        String email= "dev.sunclock@gmail.com";
        String phone = "010-1234-5678";
        String picture = "https://s3.pictures.com";

        UserSaveRequestDto requestDto = UserSaveRequestDto.builder()
                .familyName(familyName)
                .givenName(givenName)
                .username(username)
                .email(email)
                .phone(phone)
                .picture(picture)
                .build();

        // when
        mvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        // then
        List<User> all = userRepository.findAll();
        assertThat(all.get(0).getUsername()).isEqualTo(username);
        assertThat(all.get(0).getPhone()).isEqualTo(phone);
        assertThat(all.get(0).getId()).isNotNull();
    }
}
