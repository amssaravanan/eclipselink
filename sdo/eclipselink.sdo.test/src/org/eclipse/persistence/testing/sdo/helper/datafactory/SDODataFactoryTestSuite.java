/*******************************************************************************
 * Copyright (c) 1998, 2015 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/
package org.eclipse.persistence.testing.sdo.helper.datafactory;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SDODataFactoryTestSuite {
    public SDODataFactoryTestSuite() {
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("All SDODataFactory Tests");

        suite.addTest(new TestSuite(SDODataFactoryExceptionTestCases.class));
        suite.addTest(new TestSuite(SDODataFactoryCreateSimpleTestCases.class));
        return suite;
    }
}
