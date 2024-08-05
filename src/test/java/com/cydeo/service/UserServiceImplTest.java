package com.cydeo.service;

import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ProjectService projectService;
    @Mock
    private TaskService taskService;
    @Mock
    private KeycloakService keycloakService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    User user;
    UserDTO userDTO;

//    @BeforeAll
//    public static void setUpBeforeClass() throws Exception {
//
//    }
//
//    @AfterAll
//    public static void tearDownAfterClass() throws Exception {
//
//    }

    @BeforeEach
    public void setUp() throws Exception {

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("user");
        user.setPassWord("Abc1");
        user.setEnabled(true);
        user.setRole(new Role("Manager"));

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setUserName("user");
        userDTO.setPassWord("Abc1");
        userDTO.setEnabled(true);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setDescription("Manager");
        userDTO.setRole(roleDTO);
    }

    private List<User> getUsers() {

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Emily");

        return List.of(user, user2);
    }

    private List<UserDTO> getUserDTOs() {

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setFirstName("Emily");

        return List.of(userDTO, userDTO2);
    }

    @AfterEach
    public void tearDown() throws Exception {

    }

    @Test
    public void should_list_all_users() {

        //given
        when(userRepository.findAllByIsDeletedOrderByFirstNameDesc(false)).thenReturn(getUsers());
        when(userMapper.convertToDto(user)).thenReturn(userDTO);
        when(userMapper.convertToDto(getUsers().get(1))).thenReturn(getUserDTOs().get(1));
//        when(userMapper.convertToDto(any(User.class))).thenReturn(userDTO, getUserDTOs().get(1));

        List<UserDTO> expectedList = getUserDTOs();

        //when
        List<UserDTO> actualList = userService.listAllUsers();

        //then
        assertEquals(expectedList, actualList);
        assertThat(actualList).usingRecursiveComparison().isEqualTo(expectedList);

        verify(userRepository, times(1)).findAllByIsDeletedOrderByFirstNameDesc(false);
        verify(userRepository, never()).findAllByIsDeletedOrderByFirstNameDesc(true);
    }

    @Test
    public void should_throw_no_such_element_exception_when_user_not_found() {

        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(null);
//        when(userMapper.convertToDto(any(User.class))).thenReturn(userDTO);

//        Throwable actualException = assertThrows(
//                RuntimeException.class, () -> userService.findByUserName("SomeUsername"));

        Throwable actualException = assertThrowsExactly(
                NoSuchElementException.class, () -> userService.findByUserName("SomeUsername"));

        assertEquals("User not found", actualException.getMessage());

//        Throwable actualException = catchThrowable(()-> userService.findByUserName("SomeUsername"));
    }

    @Test
    public void should_encode_user_password_on_save_operation() {

        when(userMapper.convertToEntity(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.convertToDto(any(User.class))).thenReturn(userDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("some-password");

        String expectedPassword = "some-password";

        UserDTO savedUser = userService.save(userDTO);

        assertEquals(expectedPassword, savedUser.getPassWord());

        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void should_encode_user_password_on_update_operation() {

        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(user);
        when(userMapper.convertToEntity(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.convertToDto(any(User.class))).thenReturn(userDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("some-password");

        String expectedPassword = "some-password";

        UserDTO updatedUser = userService.update(userDTO);

        assertEquals(expectedPassword, updatedUser.getPassWord());

        verify(passwordEncoder, times(1)).encode(anyString());

    }
}
