package marijana.app.project_test.mapper;

import marijana.app.project_test.dto.RoleDTO;
import marijana.app.project_test.model.Role;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {

    RoleDTO roleToRoleDTO(Role role);
}
