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
package org.eclipse.persistence.testing.models.performance.toplink;

import org.eclipse.persistence.indirection.*;

/**
 * <b>Purpose</b>: Abstract superclass for Large & Small projects in Employee Demo
 * <p><b>Description</b>:     Project is an example of an abstract superclass. It demonstrates how class inheritance can be mapped to database tables.
 * It's subclasses are concrete and may or may not add columns through additional tables. The PROJ_TYPE field in the
 * database table indicates which subclass to instantiate. Projects are involved in a M:M relationship with employees.
 * The Employee classs maintains the definition of the relation table.
 * @see LargeProject
 * @see SmallProject
 */
public abstract class Project extends org.eclipse.persistence.testing.models.performance.Project {
    public ValueHolderInterface teamLeaderHolder;

    public Project() {
        super();
        this.teamLeaderHolder = new ValueHolder();
    }

    public org.eclipse.persistence.testing.models.performance.Employee getTeamLeader() {
        return (Employee)teamLeaderHolder.getValue();
    }

    public void setTeamLeader(org.eclipse.persistence.testing.models.performance.Employee teamLeader) {
        this.teamLeaderHolder.setValue(teamLeader);
    }
}
