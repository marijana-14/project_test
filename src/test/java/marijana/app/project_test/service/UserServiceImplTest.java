package marijana.app.project_test.service;

import marijana.app.project_test.dto.RoleDTO;
import marijana.app.project_test.dto.UserDTO;
import marijana.app.project_test.dto.UserWithoutPasswordDTO;

import marijana.app.project_test.exception.BadRequestException;
import marijana.app.project_test.exception.ResourceNotFoundException;
import marijana.app.project_test.mapper.UserMapper;
import marijana.app.project_test.model.Role;
import marijana.app.project_test.model.User;
import marijana.app.project_test.repository.RoleRepository;
import marijana.app.project_test.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    public static final Long USER_ID_1 = 1L;
    public static final Long USER_ID_2 = 2L;
    public static final String USER_USERNAME_1 = "user1";
    public static final String USER_USERNAME_2 = "user2";
    public static final String USER_PASSWORD_1 = "password1";
    public static final String USER_PASSWORD_2 = "password2";
    public static final String USER_EMAIL = "user@gmail.com";
    public static final Long ROLE_ID_1 = 1L;
    public static final Long ROLE_ID_2 = 2L;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    UserService userService;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userRepository, roleRepository, Mappers.getMapper(UserMapper.class),
                passwordEncoder);
    }

    @Test
    public void getAllUsers() {

        //given
        User user1 = getUser1();

        Role role1 = new Role();
        role1.setId(ROLE_ID_1);

        user1.getRoles().add(role1);

        User user2 = getUser2();

        Role role2 = new Role();
        role2.setId(ROLE_ID_2);

        user2.getRoles().add(role2);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        //when
        List<UserWithoutPasswordDTO> userWithoutPasswordDTOList = userService.getAllUsers();

        //then
        assertEquals(2, userWithoutPasswordDTOList.size());
        assertEquals(USER_ID_1, userWithoutPasswordDTOList.get(0).getId());
        assertEquals(USER_USERNAME_1, userWithoutPasswordDTOList.get(0).getUsername());
        assertEquals(1,userWithoutPasswordDTOList.get(0).getRoles().size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getUserById() {

        //given
        User user = getUser1();

        Role role = new Role();
        role.setId(ROLE_ID_1);

        user.getRoles().add(role);

        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(user));

        //when
        UserWithoutPasswordDTO userWithoutPasswordDTO = userService.getUserById(USER_ID_1);

        //then
        assertNotNull(userWithoutPasswordDTO);
        assertEquals(USER_ID_1, userWithoutPasswordDTO.getId());
        assertEquals(USER_USERNAME_1, userWithoutPasswordDTO.getUsername());
        assertEquals(1, userWithoutPasswordDTO.getRoles().size());
        verify(userRepository, times(1)).findById(anyLong());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void getUserByIdNotFound()
    {
        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        UserWithoutPasswordDTO userWithoutPasswordDTO = userService.getUserById(USER_ID_1);

    }

    @Test
    public void createNewUser() {

        //given
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(USER_USERNAME_1);
        userDTO.setPassword(USER_PASSWORD_1);
        userDTO.setEmail(USER_EMAIL);

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(ROLE_ID_1);

        userDTO.getRoles().add(roleDTO);

        User user = getUser1();

        Role role = new Role();
        role.setId(ROLE_ID_1);

        user.getRoles().add(role);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(roleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        //when
        UserWithoutPasswordDTO userWithoutPasswordDTO = userService.createNewUser(userDTO);

        //then
        assertNotNull(userWithoutPasswordDTO);
        assertEquals(USER_ID_1, userWithoutPasswordDTO.getId());
        assertEquals(userDTO.getUsername(), userWithoutPasswordDTO.getUsername());
        assertEquals(userDTO.getEmail(), userWithoutPasswordDTO.getEmail());
        assertEquals(userDTO.getRoles().size(), userWithoutPasswordDTO.getRoles().size());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(roleRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test(expected = BadRequestException.class)
    public void createNewUserWithGivenId()
    {
        //given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(USER_ID_1);

        //when
        UserWithoutPasswordDTO userWithoutPasswordDTO = userService.createNewUser(userDTO);
    }


    @Test
    public void updateUser() {

        //Update username and password of user
        //given
        UserDTO userDTO = new UserDTO();
        userDTO.setId(USER_ID_1);
        userDTO.setUsername(USER_USERNAME_2);
        userDTO.setPassword(USER_PASSWORD_2);
        userDTO.setEmail(USER_EMAIL);

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(ROLE_ID_1);

        userDTO.getRoles().add(roleDTO);

        User oldUser = getUser1();

        Role role = new Role();
        role.setId(ROLE_ID_1);

        oldUser.getRoles().add(role);

        User updatedUser = getUser1();
        updatedUser.setUsername(USER_USERNAME_2);
        updatedUser.setPassword(USER_PASSWORD_2);

        updatedUser.getRoles().add(role);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(oldUser));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(roleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(role));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        //when
        UserWithoutPasswordDTO userWithoutPasswordDTO = userService.updateUser(userDTO);

        //then
        assertNotNull(userWithoutPasswordDTO);
        assertEquals(userDTO.getId(), userWithoutPasswordDTO.getId());
        assertEquals(userDTO.getUsername(), userWithoutPasswordDTO.getUsername());
        assertEquals(userDTO.getEmail(), userWithoutPasswordDTO.getEmail());
        assertEquals(userDTO.getRoles().size(), userWithoutPasswordDTO.getRoles().size());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, never()).findByEmail(anyString()); //Email did not change
        verify(roleRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));

    }

    private User getUser1()
    {
        User user1 = new User();
        user1.setId(USER_ID_1);
        user1.setUsername(USER_USERNAME_1);
        user1.setPassword(USER_PASSWORD_1);
        user1.setEmail(USER_EMAIL);

        return user1;
    }

    private User getUser2()
    {
        User user2 = new User();
        user2.setId(USER_ID_2);
        user2.setUsername(USER_USERNAME_2);
        user2.setPassword(USER_PASSWORD_2);

        return user2;
    }
}