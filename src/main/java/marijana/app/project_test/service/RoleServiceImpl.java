package marijana.app.project_test.service;

import marijana.app.project_test.dto.RoleDTO;
import marijana.app.project_test.mapper.RoleMapper;
import marijana.app.project_test.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    // Method called by controller for returning all roles
    @Override
    public List<RoleDTO> getAllRoles() {

        List<RoleDTO> roleDTOList = new ArrayList<>();
        roleRepository.findAll().forEach(role -> roleDTOList.add(roleMapper.roleToRoleDTO(role)));
        return roleDTOList;
    }
}
