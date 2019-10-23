package marijana.app.project_test.audit.util;

import marijana.app.project_test.audit.model.UserAudit;
import marijana.app.project_test.audit.repository.UserAuditRepository;
import marijana.app.project_test.model.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class UserListener {

    public static final String INSERT = "INSERT";
    public static final String UPDATE = "UPDATE";

    @PrePersist
    @Transactional
    public void prePersist(User user) {
        perform(user, INSERT);
    }

    @PreUpdate
    @Transactional
    public void preUpdate(User user) {
        perform(user, UPDATE);
    }

    // Method called before every create or update user call
    private void perform(User user, String operation) {

      UserAuditRepository userAuditRepository = BeanUtil.getBean(UserAuditRepository.class);
      userAuditRepository.save(new UserAudit(user, operation));
    }
}
