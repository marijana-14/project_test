package marijana.app.project_test.audit.controller;

import marijana.app.project_test.audit.model.UserAudit;
import marijana.app.project_test.audit.service.UserAuditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserAuditController {

    private final UserAuditService userAuditService;

    public UserAuditController(UserAuditService userAuditService) {
        this.userAuditService = userAuditService;
    }


    // Method for getting history of all changes (create and update) of any user
    @GetMapping("/api/audit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserAudit>> getAuditLogHistory()
    {
        return new ResponseEntity<>(userAuditService.getHistoryOfUserEvents(), HttpStatus.OK);
    }
}
