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
package org.eclipse.persistence.testing.models.order;

import org.eclipse.persistence.tools.schemaframework.*;

/**
 * This class was generated by the TopLink table creator generator.
 * It stores the meta-data (tables) that define the database schema.
 * @see org.eclipse.persistence.sessions.factories.TableCreatorClassGenerator
 */
public class OrderTransactionTableCreator extends TableCreator {
    public OrderTransactionTableCreator() {
        setName("OrderTransactionTableCreator");

        addTableDefinition(buildORDER_TRANSTable());
    }

    public TableDefinition buildORDER_TRANSTable() {
        TableDefinition table = new TableDefinition();
        table.setName("ORDER_TRANS");

        FieldDefinition fieldID = new FieldDefinition();
        fieldID.setName("TRANS_ID");
        fieldID.setTypeName("INTEGER");
        fieldID.setSize(0);
        fieldID.setSubSize(0);
        fieldID.setIsPrimaryKey(true);
        fieldID.setIsIdentity(false);
        fieldID.setUnique(false);
        fieldID.setShouldAllowNull(false);
        table.addField(fieldID);

        FieldDefinition fieldTRANS_DATE = new FieldDefinition();
        fieldTRANS_DATE.setName("TRANS_DATE");
        fieldTRANS_DATE.setTypeName("DATE");
        fieldTRANS_DATE.setSize(0);
        fieldTRANS_DATE.setSubSize(0);
        fieldTRANS_DATE.setIsPrimaryKey(false);
        fieldTRANS_DATE.setIsIdentity(false);
        fieldTRANS_DATE.setUnique(false);
        fieldTRANS_DATE.setShouldAllowNull(false);
        table.addField(fieldTRANS_DATE);

        FieldDefinition fieldORDER_ID = new FieldDefinition();
        fieldORDER_ID.setName("ORDER_ID");
        fieldORDER_ID.setTypeName("INTEGER");
        fieldORDER_ID.setSize(0);
        fieldORDER_ID.setSubSize(0);
        fieldORDER_ID.setIsPrimaryKey(false);
        fieldORDER_ID.setIsIdentity(false);
        fieldORDER_ID.setUnique(false);
        fieldORDER_ID.setShouldAllowNull(false);
        table.addField(fieldORDER_ID);

        FieldDefinition fieldTRANS_USER = new FieldDefinition();
        fieldTRANS_USER.setName("TRANS_USER");
        fieldTRANS_USER.setTypeName("TEXT");
        fieldTRANS_USER.setSize(20);
        fieldTRANS_USER.setSubSize(0);
        fieldTRANS_USER.setIsPrimaryKey(false);
        fieldTRANS_USER.setIsIdentity(false);
        fieldTRANS_USER.setUnique(false);
        fieldTRANS_USER.setShouldAllowNull(false);
        table.addField(fieldTRANS_USER);

        return table;
    }
}
