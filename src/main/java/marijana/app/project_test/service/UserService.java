package marijana.app.project_test.service;

import marijana.app.project_test.dto.UserDTO;
import marijana.app.project_test.dto.UserWithoutPasswordDTO;

import java.util.List;

public interface UserService {

    List<UserWithoutPasswordDTO> getAllUsers();
    UserWithoutPasswordDTO getUserById(Long id);
    UserWithoutPasswordDTO createNewUser(UserDTO userDTO);
    UserWithoutPasswordDTO updateUser(UserDTO userDTO);

}
