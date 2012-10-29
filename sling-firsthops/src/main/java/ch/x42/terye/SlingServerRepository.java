package ch.x42.terye;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Component
/*@Properties({
@Property(name="service.vendor", value="The Apache Software Foundation"),
@Property(name="service.description", value="Factory for embedded Jackrabbit Repository Instances")
})*/
public class SlingServerRepository implements Repository, SlingRepository {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private Repository repository;

    @Activate
    public void activate(ComponentContext ctx) {
        repository = new RepositoryImpl();
        log.debug("Activated, using repository {}", repository);
    }

    @Deactivate
    public void deactivate(ComponentContext ctx) {
        log.debug("Deactivated", repository);
        repository = null;
    }

    public String[] getDescriptorKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isStandardDescriptor(String key) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isSingleValueDescriptor(String key) {
        // TODO Auto-generated method stub
        return false;
    }

    public Value getDescriptorValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    public Value[] getDescriptorValues(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getDescriptor(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    public Session login(Credentials credentials, String workspaceName)
            throws LoginException, NoSuchWorkspaceException,
            RepositoryException {
        return repository.login(credentials, workspaceName);
    }

    public Session login(Credentials credentials) throws LoginException,
            RepositoryException {
        return repository.login(credentials);
    }

    public Session login(String workspaceName) throws LoginException,
            NoSuchWorkspaceException, RepositoryException {
        return repository.login(workspaceName);
    }

    public Session login() throws LoginException, RepositoryException {
        return repository.login();
    }

    public String getDefaultWorkspace() {
        // TODO Auto-generated method stub
        return null;
    }

    public Session loginAdministrative(String workspace)
            throws RepositoryException {
        return repository.login(workspace);
    }

}
