package marijana.app.project_test.controller;

import marijana.app.project_test.dto.RoleDTO;
import marijana.app.project_test.service.RoleService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleControllerTest {

    public static final Long ROLE_ID_1 = 1L;
    public static final Long ROLE_ID_2 = 2L;
    public static final String ROLE_NAME_1 = "ROLE_ADMIN";
    public static final String ROLE_NAME_2 = "ROLE_USER";
    public static final String BASE_URL = "/api/roles";


    @Mock
    RoleService roleService;

    @InjectMocks
    RoleController roleController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    public void listAllRoles() throws Exception {

        //given
        RoleDTO role1 = new RoleDTO();
        role1.setId(ROLE_ID_1);
        role1.setName(ROLE_NAME_1);

        RoleDTO role2 = new RoleDTO();
        role2.setId(ROLE_ID_2);
        role2.setName(ROLE_NAME_2);

        when(roleService.getAllRoles()).thenReturn(Arrays.asList(role1, role2));

        //when
        mockMvc.perform(get(BASE_URL + "/list")
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", equalTo(ROLE_NAME_1)));



    }

}