package marijana.app.project_test.controller;

import marijana.app.project_test.dto.RoleDTO;
import marijana.app.project_test.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Returns list of roles
    @GetMapping("/list")
    public ResponseEntity<List<RoleDTO>> listAllRoles()
    {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

}
