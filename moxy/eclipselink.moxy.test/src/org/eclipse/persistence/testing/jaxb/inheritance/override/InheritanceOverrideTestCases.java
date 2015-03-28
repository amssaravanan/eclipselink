/*******************************************************************************
 * Copyright (c) 2012, 2015 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Matt MacIvor - 2.5.1 - initial implementation
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.inheritance.override;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.testing.jaxb.JAXBWithJSONTestCases;


public class InheritanceOverrideTestCases extends JAXBWithJSONTestCases {
    public InheritanceOverrideTestCases(String name) throws Exception {
        super(name);
        setClasses(new Class[] { Root.class, Foo.class, Superclass.class, Subclass.class });
        setControlDocument("org/eclipse/persistence/testing/jaxb/inheritance/override.xml");
        setControlJSON("org/eclipse/persistence/testing/jaxb/inheritance/override.json");
    }

    public Object getControlObject() {
        Root r = new Root();
        Subclass s = new Subclass();
        r.setSubclass(s);
        Foo f = new Foo();
        f.setName("abc");
        s.getCollection().add(f);

        return r;
    }
}
