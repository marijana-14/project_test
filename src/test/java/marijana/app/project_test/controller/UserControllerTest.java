package marijana.app.project_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import marijana.app.project_test.dto.RoleDTO;
import marijana.app.project_test.dto.UserDTO;
import marijana.app.project_test.dto.UserWithoutPasswordDTO;
import marijana.app.project_test.exception.ResourceNotFoundException;
import marijana.app.project_test.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    public static final Long USER_ID_1 = 1L;
    public static final Long USER_ID_2 = 2L;
    public static final String USER_USERNAME_1 = "user1";
    public static final String USER_USERNAME_2 = "user2";
    public static final String USER_PASSWORD_1 = "password1";
    public static final String USER_EMAIL = "user@gmail.com";
    public static final Long ROLE_ID_1 = 1L;
    public static final Long ROLE_ID_2 = 2L;
    public static final String BASE_URL = "/api/users";

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void hello() throws Exception {

        //when
        mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo("Hello World!")));

        //then
        verifyZeroInteractions(userService);
    }

    @Test
    public void listAllUsers() throws Exception {

        //given
        UserWithoutPasswordDTO user1 = getUser1();

        RoleDTO role1 = new RoleDTO();
        role1.setId(ROLE_ID_1);

        user1.getRoles().add(role1);

        UserWithoutPasswordDTO user2 = getUser2();

        RoleDTO role2 = new RoleDTO();
        role2.setId(ROLE_ID_2);

        user2.getRoles().add(role2);

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        //when
        mockMvc.perform(get(BASE_URL + "/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].username", equalTo(USER_USERNAME_1)))
                .andExpect(jsonPath("$[0].email", equalTo(USER_EMAIL)))
                .andExpect(jsonPath("$[0].roles[0].id", equalTo(1)));

        //then
        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);

    }

    @Test
    public void findUserById() throws Exception {

        //given
        UserWithoutPasswordDTO user = getUser1();

        RoleDTO role = new RoleDTO();
        role.setId(ROLE_ID_1);

        user.getRoles().add(role);

        when(userService.getUserById(anyLong())).thenReturn(user);

        //when
        mockMvc.perform(get(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.username", equalTo(USER_USERNAME_1)))
                .andExpect(jsonPath("$.email", equalTo(USER_EMAIL)))
                .andExpect(jsonPath("$.roles[0].id", equalTo(1)));

        //then
        verify(userService, times(1)).getUserById(anyLong());
        verifyNoMoreInteractions(userService);

    }


    @Test
    public void findUserByIdNotFound() throws Exception {

        //given
        when(userService.getUserById(anyLong())).thenThrow(ResourceNotFoundException.class);

        //when
        mockMvc.perform(get(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        //then
        verify(userService, times(1)).getUserById(anyLong());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void createUser() throws Exception {

        //given
        UserDTO userToCreate = new UserDTO();
        userToCreate.setUsername(USER_USERNAME_1);
        userToCreate.setPassword(USER_PASSWORD_1);
        userToCreate.setEmail(USER_EMAIL);

        RoleDTO role = new RoleDTO();
        role.setId(ROLE_ID_1);

        userToCreate.getRoles().add(role);

        UserWithoutPasswordDTO returnUser = getUser1();
        returnUser.getRoles().add(role);

        when(userService.createNewUser(any(UserDTO.class))).thenReturn(returnUser);

        //when
        mockMvc.perform(post(BASE_URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.username", equalTo(USER_USERNAME_1)))
                .andExpect(jsonPath("$.email", equalTo(USER_EMAIL)))
                .andExpect(jsonPath("$.roles[0].id", equalTo(1)));

        //then
        verify(userService, times(1)).createNewUser(any(UserDTO.class));
        verifyNoMoreInteractions(userService);

    }

    @Test
    public void updateUser() throws Exception {

        //given
        UserDTO userToUpdate = new UserDTO();
        userToUpdate.setId(USER_ID_1);
        userToUpdate.setUsername(USER_USERNAME_1);
        userToUpdate.setPassword(USER_PASSWORD_1);
        userToUpdate.setEmail(USER_EMAIL);

        RoleDTO role = new RoleDTO();
        role.setId(ROLE_ID_1);

        userToUpdate.getRoles().add(role);

        UserWithoutPasswordDTO returnUser = getUser1();
        returnUser.getRoles().add(role);

        when(userService.updateUser(any(UserDTO.class))).thenReturn(returnUser);

        //when
        mockMvc.perform(put(BASE_URL + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.username", equalTo(USER_USERNAME_1)))
                .andExpect(jsonPath("$.email", equalTo(USER_EMAIL)))
                .andExpect(jsonPath("$.roles[0].id", equalTo(1)));

        //then
        verify(userService, times(1)).updateUser(any(UserDTO.class));
        verifyNoMoreInteractions(userService);

    }

    private UserWithoutPasswordDTO getUser1()
    {
        UserWithoutPasswordDTO user1 = new UserWithoutPasswordDTO();
        user1.setId(USER_ID_1);
        user1.setUsername(USER_USERNAME_1);
        user1.setEmail(USER_EMAIL);

        return user1;
    }

    private UserWithoutPasswordDTO getUser2()
    {
        UserWithoutPasswordDTO user2 = new UserWithoutPasswordDTO();
        user2.setId(USER_ID_2);
        user2.setUsername(USER_USERNAME_2);

        return user2;
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}