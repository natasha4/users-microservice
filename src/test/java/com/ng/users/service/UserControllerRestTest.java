package com.ng.users.service;

import com.ng.users.services.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

/**
 * Created by natalia on 23/11/17.
 */

@RunWith(SpringRunner.class)
@WebMvcTest(UsersController.class)
public class UserControllerRestTest {

    private static String USER_LOGIN = "user12345";
    private static String USER_NAME = "The Test User";
    private static User TEST_USER = new User(USER_NAME, USER_LOGIN);
    private static String EXPECTED_JSON = "{id:0,name:'The Test User',login:'user12345'}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    public UserRepository userRepository;


    @Test
    public void testByLogin() throws Exception {
        BDDMockito.given(userRepository.findByLogin(USER_LOGIN)).willReturn(TEST_USER);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/" + USER_LOGIN).accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(USER_LOGIN))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(USER_NAME))
                .andReturn();

        JSONAssert.assertEquals(EXPECTED_JSON, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void testByLogin404() throws Exception {
        //no any user in a repository
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/" + USER_LOGIN).accept(
                MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testByName() throws Exception {

        BDDMockito.given(userRepository.findByNameContainingIgnoreCase(USER_NAME)).willReturn(Collections.singletonList(TEST_USER));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/search/" + USER_NAME).accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        JSONAssert.assertEquals("[" + EXPECTED_JSON + "]", result.getResponse().getContentAsString(), false);
    }

    @Test
    public void testByName404() throws Exception {
        //no any user in a repository
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/search/" + "USER_NAME").
                accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
