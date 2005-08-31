/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import net.sourceforge.fenixedu.domain.IDomainObject;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class TestScope extends TestScope_Base {

    public TestScope() {
    }

    public TestScope(IDomainObject object) {
        super();
        setDomainObject(object);
        setClassName(object.getClass().getName());
        setKeyClass(object.getIdInternal());
    }

    public TestScope(String className, Integer classId) {
        super();
        setClassName(className);
        setKeyClass(classId);
    }

}