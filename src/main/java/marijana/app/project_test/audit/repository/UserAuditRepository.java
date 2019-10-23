package marijana.app.project_test.audit.repository;

import marijana.app.project_test.audit.model.UserAudit;
import org.springframework.data.repository.CrudRepository;

public interface UserAuditRepository extends CrudRepository<UserAudit, Long> {
}
