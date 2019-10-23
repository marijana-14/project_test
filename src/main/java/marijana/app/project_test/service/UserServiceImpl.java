package marijana.app.project_test.service;

import marijana.app.project_test.dto.UserDTO;
import marijana.app.project_test.dto.UserWithoutPasswordDTO;
import marijana.app.project_test.exception.BadRequestException;
import marijana.app.project_test.exception.ResourceNotFoundException;
import marijana.app.project_test.mapper.UserMapper;
import marijana.app.project_test.model.Role;
import marijana.app.project_test.model.User;
import marijana.app.project_test.repository.RoleRepository;
import marijana.app.project_test.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Method called by controller for returning all users
    // Caching can be tested with simulateSlowService() which simulates huge response
    @Override
    @Cacheable(value = "users")
    public List<UserWithoutPasswordDTO> getAllUsers() {

        // simulateSlowService();
        List<UserWithoutPasswordDTO> userWithoutPasswordDTOList = new ArrayList<>();
        userRepository.findAll().forEach(user -> userWithoutPasswordDTOList
                .add(userMapper.userToUserWithoutPasswordDTO(user)));
        return userWithoutPasswordDTOList;

    }

    // Method called by controller for returning user by id
    @Override
    public UserWithoutPasswordDTO getUserById(Long id) {

        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent())
        {
            throw new ResourceNotFoundException("User with id " + id + " not found!");
        }
        return userMapper.userToUserWithoutPasswordDTO(userOptional.get());
    }

    // Method called by controller to create new user
    // Password is saved encoded
    @Override
    @Transactional
    @CacheEvict(value= "users", allEntries=true)
    public UserWithoutPasswordDTO createNewUser(UserDTO userDTO) {

        if(userDTO.getId()!=null)
        {
            throw new BadRequestException("New user cannot have id!");
        }

        checkIfUsernameExists(userDTO.getUsername());
        checkIfEmailExists(userDTO.getEmail());

        if(userDTO.getPassword()==null) {
            throw new BadRequestException("Password cannot be null!");
        }
        checkIfPasswordStrong(userDTO.getPassword());
        checkRoles(userDTO);

        User userToSave = userMapper.userDTOToUser(userDTO);
        userToSave.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(userToSave);
        return userMapper.userToUserWithoutPasswordDTO(savedUser);
    }

    // Method called by controller to update user
    @Override
    @Transactional
    @CacheEvict(value= "users", allEntries=true)
    public UserWithoutPasswordDTO updateUser(UserDTO userDTO) {

        if(userDTO.getId()==null)
        {
            throw new BadRequestException("User must have id!");
        }

        Optional<User> userOptional = userRepository.findById(userDTO.getId());

        if(!userOptional.isPresent())
        {
            throw new ResourceNotFoundException("User with id " + userDTO.getId() + " not found!");
        }
        User oldUser = userOptional.get();

        if(!oldUser.getUsername().equals(userDTO.getUsername()))
        {
            checkIfUsernameExists(userDTO.getUsername());
        }
        if(!oldUser.getEmail().equals(userDTO.getEmail()))
        {
            checkIfEmailExists(userDTO.getEmail());
        }

        checkRoles(userDTO);

        User userToUpdate = userMapper.userDTOToUser(userDTO);

        if(userDTO.getPassword()!=null)
        {
            checkIfPasswordStrong(userDTO.getPassword());
            userToUpdate.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        else
        {
            userToUpdate.setPassword(oldUser.getPassword());
        }

        User updatedUser = userRepository.save(userToUpdate);
        return userMapper.userToUserWithoutPasswordDTO(updatedUser);


    }

    private void checkIfUsernameExists(String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);

        if(userOptional.isPresent())
        {
            throw new BadRequestException("Username " + username + " already exists!");
        }

    }
    private void checkIfEmailExists(String email) {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isPresent())
        {
            throw new BadRequestException("Email "+ email + " already exists!");
        }

    }

    private void checkIfPasswordStrong(String password) {

        if(password.length()<6 || !password.matches(".*[a-zA-Z].*")
                || !password.matches(".*[0-9].*"))
        {
            throw new BadRequestException("Password is not strong enough. It must contain at least 6 characters, " +
                    "at least one letter and at least one number!");
        }
    }

    private void checkRoles(UserDTO userDTO)
    {
        userDTO.getRoles().stream().forEach(roleDTO -> {
            Optional<Role> roleOptional = roleRepository.findById(roleDTO.getId());
            if(!roleOptional.isPresent())
            {
                throw new BadRequestException("Role with id " + roleDTO.getId() + " does not exist!");
            }
            Role role = roleOptional.get();
            if(roleDTO.getName()==null || roleDTO.getName()!=role.getName())
            {
                roleDTO.setName(role.getName());
            }
        });
    }

    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }





}
