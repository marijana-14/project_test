package marijana.app.project_test.mapper;

import marijana.app.project_test.dto.UserDTO;
import marijana.app.project_test.dto.UserWithoutPasswordDTO;
import marijana.app.project_test.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User userDTOToUser(UserDTO userDTO);
    UserWithoutPasswordDTO userToUserWithoutPasswordDTO(User user);
}
