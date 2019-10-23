package marijana.app.project_test.audit.service;

import marijana.app.project_test.audit.model.UserAudit;
import marijana.app.project_test.audit.repository.UserAuditRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAuditServiceImpl implements UserAuditService {

    private final UserAuditRepository userAuditRepository;

    public UserAuditServiceImpl(UserAuditRepository userAuditRepository) {
        this.userAuditRepository = userAuditRepository;
    }

    // Method for getting all records from user audit table
    @Override
    public List<UserAudit> getHistoryOfUserEvents() {

        List<UserAudit> userAuditList = new ArrayList<>();
        userAuditRepository.findAll().forEach(userAuditList :: add);
        return userAuditList;
    }
}
