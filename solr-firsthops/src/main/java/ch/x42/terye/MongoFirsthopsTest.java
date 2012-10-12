package ch.x42.terye;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class MongoFirsthopsTest {

    public static void main(String[] args) throws RepositoryException {
        Repository repository = new RepositoryImpl();
        Session session = repository.login();
        Node root = session.getNode("/");
        root.addNode("test");
        root.setProperty("test", "asdf");
        
//        root.addNode("test");
//        root.getNode("test").remove();

//      Node t = root.addNode("test");
//      t.addNode("2");
//      t.addNode("1");
//      session.save();
        
//        root.addNode("node");
//        root.setProperty("prop", "string");
//        System.out.println(root.hasNodes());
//        System.out.println(root.hasProperties());
        
//        Property p = root.getProperty("prop");
//        p.setValue(30);
        
//        System.out.println(session.hasPendingChanges());
//        System.out.println(root.addNode("aha"));
//        System.out.println(session.hasPendingChanges());
        
//        System.out.println(root.setProperty("prop1", "str").getValue().getString());
//        System.out.println(root.setProperty("prop3", 17179869183L).getValue().getLong());
//        System.out.println(root.setProperty("prop4", 2.5D).getValue().getDouble());
//        System.out.println(root.setProperty("prop6", new BigDecimal("3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117067982148086513282306647093844609550582231725359408128481117450284102")).getValue().getDecimal());
//        System.out.println(root.setProperty("prop5", new GregorianCalendar(2012, Calendar.SEPTEMBER, 28)).getValue().getDate().getTime());
        
//        PropertyIterator i = root.getNode("test").getProperties();
//        while (i.hasNext())
//            System.out.println("-> " + i.nextProperty().getPath());
        
//        session.save();
        
//        NodeIterator i = root.getNodes();
//        while (i.hasNext()) {
//            System.out.println("-> " + i.nextNode().getPath());
//        }
        
//        Node test = root.getNode("test");
//        Node ok = session.getNode("/test/ok");
//        System.out.println(test.hasNodes());
        
//        Node ok = root.getNode("test/ok");
//        System.out.println(ok.getParent().getParent().getPath());
//        System.out.println(session.nodeExists("/"));
//        System.out.println(session.nodeExists("/test"));
//        System.out.println(session.nodeExists("/test/ok"));
//        System.out.println(session.nodeExists("/test/nok"));
//        System.out.println(root.hasNode("test/ok"));
//        System.out.println(ok.hasNode("nope"));
    }

}
