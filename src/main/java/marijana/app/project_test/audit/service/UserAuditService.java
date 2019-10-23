package marijana.app.project_test.audit.service;

import marijana.app.project_test.audit.model.UserAudit;

import java.util.List;

public interface UserAuditService {

    List<UserAudit> getHistoryOfUserEvents();
}
