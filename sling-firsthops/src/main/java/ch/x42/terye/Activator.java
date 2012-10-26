package ch.x42.terye;

import java.util.Properties;

import javax.jcr.Repository;

import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

    private ServiceRegistration registration;

    @Override
    public void start(BundleContext context) throws Exception {
        String[] clazzes = new String[] {
                SlingRepository.class.getName(), Repository.class.getName()
        };
        registration = context.registerService(clazzes,
                new SlingServerRepository(), new Properties());
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        registration.unregister();
    }

}
