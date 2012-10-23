package ch.x42.terye;

import java.security.Principal;
import java.util.Properties;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.test.NotExecutableException;
import org.apache.jackrabbit.test.RepositoryStub;
import org.apache.jackrabbit.test.RepositoryStubException;

public class RepositoryStubImpl extends RepositoryStub {

    private RepositoryImpl repository;

    public RepositoryStubImpl(Properties env) {
        super(env);
    }

    @Override
    public Principal getKnownPrincipal(Session arg0) throws RepositoryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Repository getRepository() throws RepositoryStubException {
        if (repository == null) {
            try {
                repository = new RepositoryImpl();
                Session session = repository.login();
                session.getRootNode().addNode("testroot");
                session.save();
                session.logout();
            } catch (RepositoryException e) {
                throw new RepositoryStubException(e.toString());
            }
        }
        return repository;
    }

    @Override
    public Principal getUnknownPrincipal(Session arg0)
            throws RepositoryException, NotExecutableException {
        // TODO Auto-generated method stub
        return null;
    }

}
