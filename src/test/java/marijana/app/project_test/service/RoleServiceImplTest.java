package marijana.app.project_test.service;

import marijana.app.project_test.dto.RoleDTO;
import marijana.app.project_test.mapper.RoleMapper;
import marijana.app.project_test.model.Role;
import marijana.app.project_test.repository.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RoleServiceImplTest {

    public static final Long ROLE_ID_1 = 1L;
    public static final Long ROLE_ID_2 = 2L;
    public static final String ROLE_NAME_1 = "ROLE_ADMIN";
    public static final String ROLE_NAME_2 = "ROLE_USER";

    @Mock
    RoleRepository roleRepository;

    RoleService roleService;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        roleService = new RoleServiceImpl(roleRepository, Mappers.getMapper(RoleMapper.class));
    }

    @Test
    public void getAllRoles() {

        //given
        Role role1 = new Role();
        role1.setId(ROLE_ID_1);
        role1.setName(ROLE_NAME_1);

        Role role2 = new Role();
        role2.setId(ROLE_ID_2);
        role2.setName(ROLE_NAME_2);

        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        //when
        List<RoleDTO> roleDTOList = roleService.getAllRoles();

        //then
        assertEquals(2, roleDTOList.size());
        assertEquals(ROLE_ID_1, roleDTOList.get(0).getId());
        assertEquals(ROLE_NAME_1, roleDTOList.get(0).getName());
        verify(roleRepository, times(1)).findAll();

    }
}