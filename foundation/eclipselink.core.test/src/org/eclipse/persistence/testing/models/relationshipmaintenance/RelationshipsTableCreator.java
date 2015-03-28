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
package org.eclipse.persistence.testing.models.relationshipmaintenance;

import org.eclipse.persistence.tools.schemaframework.*;

/**
 * This class was generated by the TopLink table creator generator.
 * It stores the meta-data (tables) that define the database schema.
 * @see org.eclipse.persistence.sessions.factories.TableCreatorClassGenerator
 */
public class RelationshipsTableCreator extends TableCreator {
    public RelationshipsTableCreator() {
        setName("Relationships");

        addTableDefinition(buildFIELDLOCATIONTable());
        addTableDefinition(buildFIELDOFFICETable());
        addTableDefinition(buildCUSTOMERTable());
        addTableDefinition(buildFIELDMANAGERTable());
        addTableDefinition(buildSALESPERSONTable());
        addTableDefinition(buildSALES_CUSTTable());
        addTableDefinition(buildREL_EMPTable());
        addTableDefinition(buildREL_DEPTTable());
        addTableDefinition(buildRESOURCETable());
    }

    public TableDefinition buildCUSTOMERTable() {
        TableDefinition table = new TableDefinition();
        table.setName("REL_CUSTOMER");

        FieldDefinition fieldID = new FieldDefinition();
        fieldID.setName("ID");
        fieldID.setTypeName("INTEGER");
        fieldID.setSize(0);
        fieldID.setSubSize(0);
        fieldID.setIsPrimaryKey(true);
        fieldID.setIsIdentity(false);
        fieldID.setUnique(false);
        fieldID.setShouldAllowNull(false);
        table.addField(fieldID);

        FieldDefinition fieldNAME = new FieldDefinition();
        fieldNAME.setName("NAME");
        fieldNAME.setTypeName("VARCHAR");
        fieldNAME.setSize(20);
        fieldNAME.setSubSize(0);
        fieldNAME.setIsPrimaryKey(false);
        fieldNAME.setIsIdentity(false);
        fieldNAME.setUnique(false);
        fieldNAME.setShouldAllowNull(false);
        table.addField(fieldNAME);

        return table;
    }

    public TableDefinition buildFIELDLOCATIONTable() {
        TableDefinition table = new TableDefinition();
        table.setName("FIELDLOCATION");

        FieldDefinition fieldID = new FieldDefinition();
        fieldID.setName("ID");
        fieldID.setTypeName("INTEGER");
        fieldID.setSize(0);
        fieldID.setSubSize(0);
        fieldID.setIsPrimaryKey(true);
        fieldID.setIsIdentity(false);
        fieldID.setUnique(false);
        fieldID.setShouldAllowNull(false);
        table.addField(fieldID);

        FieldDefinition fieldCITY = new FieldDefinition();
        fieldCITY.setName("CITY");
        fieldCITY.setTypeName("VARCHAR");
        fieldCITY.setSize(20);
        fieldCITY.setSubSize(0);
        fieldCITY.setIsPrimaryKey(false);
        fieldCITY.setIsIdentity(false);
        fieldCITY.setUnique(false);
        fieldCITY.setShouldAllowNull(false);
        table.addField(fieldCITY);

        return table;
    }

    public TableDefinition buildFIELDMANAGERTable() {
        TableDefinition table = new TableDefinition();
        table.setName("FIELDMANAGER");

        FieldDefinition fieldID = new FieldDefinition();
        fieldID.setName("ID");
        fieldID.setTypeName("INTEGER");
        fieldID.setSize(0);
        fieldID.setSubSize(0);
        fieldID.setIsPrimaryKey(true);
        fieldID.setIsIdentity(false);
        fieldID.setUnique(false);
        fieldID.setShouldAllowNull(false);
        table.addField(fieldID);

        FieldDefinition fieldNAME = new FieldDefinition();
        fieldNAME.setName("NAME");
        fieldNAME.setTypeName("VARCHAR");
        fieldNAME.setSize(20);
        fieldNAME.setSubSize(0);
        fieldNAME.setIsPrimaryKey(false);
        fieldNAME.setIsIdentity(false);
        fieldNAME.setUnique(false);
        fieldNAME.setShouldAllowNull(false);
        table.addField(fieldNAME);

        return table;
    }

    public TableDefinition buildFIELDOFFICETable() {
        TableDefinition table = new TableDefinition();
        table.setName("FIELDOFFICE");

        FieldDefinition fieldID = new FieldDefinition();
        fieldID.setName("ID");
        fieldID.setTypeName("INTEGER");
        fieldID.setSize(0);
        fieldID.setSubSize(0);
        fieldID.setIsPrimaryKey(true);
        fieldID.setIsIdentity(false);
        fieldID.setUnique(false);
        fieldID.setShouldAllowNull(false);
        table.addField(fieldID);

        FieldDefinition fieldMANAGER_ID = new FieldDefinition();
        fieldMANAGER_ID.setName("MANAGER_ID");
        fieldMANAGER_ID.setTypeName("INTEGER");
        fieldMANAGER_ID.setSize(0);
        fieldMANAGER_ID.setSubSize(0);
        fieldMANAGER_ID.setIsPrimaryKey(false);
        fieldMANAGER_ID.setIsIdentity(false);
        fieldMANAGER_ID.setUnique(false);
        fieldMANAGER_ID.setShouldAllowNull(true);
        table.addField(fieldMANAGER_ID);

        FieldDefinition fieldLOCATION_ID = new FieldDefinition();
        fieldLOCATION_ID.setName("LOCATION_ID");
        fieldLOCATION_ID.setTypeName("INTEGER");
        fieldLOCATION_ID.setSize(0);
        fieldLOCATION_ID.setSubSize(0);
        fieldLOCATION_ID.setIsPrimaryKey(false);
        fieldLOCATION_ID.setIsIdentity(false);
        fieldLOCATION_ID.setUnique(false);
        fieldLOCATION_ID.setShouldAllowNull(true);
        table.addField(fieldLOCATION_ID);

        return table;
    }

    public TableDefinition buildSALES_CUSTTable() {
        TableDefinition table = new TableDefinition();
        table.setName("SALES_CUST");

        FieldDefinition fieldSALES_ID = new FieldDefinition();
        fieldSALES_ID.setName("SALES_ID");
        fieldSALES_ID.setTypeName("INTEGER");
        fieldSALES_ID.setSize(0);
        fieldSALES_ID.setSubSize(0);
        fieldSALES_ID.setIsPrimaryKey(true);
        fieldSALES_ID.setIsIdentity(false);
        fieldSALES_ID.setUnique(false);
        fieldSALES_ID.setShouldAllowNull(false);
        table.addField(fieldSALES_ID);

        FieldDefinition fieldCUST_ID = new FieldDefinition();
        fieldCUST_ID.setName("CUST_ID");
        fieldCUST_ID.setTypeName("INTEGER");
        fieldCUST_ID.setSize(0);
        fieldCUST_ID.setSubSize(0);
        fieldCUST_ID.setIsPrimaryKey(true);
        fieldCUST_ID.setIsIdentity(false);
        fieldCUST_ID.setUnique(false);
        fieldCUST_ID.setShouldAllowNull(false);
        table.addField(fieldCUST_ID);

        return table;
    }

    public TableDefinition buildSALESPERSONTable() {
        TableDefinition table = new TableDefinition();
        table.setName("SALESPERSON");

        FieldDefinition fieldID = new FieldDefinition();
        fieldID.setName("ID");
        fieldID.setTypeName("INTEGER");
        fieldID.setSize(0);
        fieldID.setSubSize(0);
        fieldID.setIsPrimaryKey(true);
        fieldID.setIsIdentity(false);
        fieldID.setUnique(false);
        fieldID.setShouldAllowNull(false);
        table.addField(fieldID);

        FieldDefinition fieldNAME = new FieldDefinition();
        fieldNAME.setName("NAME");
        fieldNAME.setTypeName("VARCHAR");
        fieldNAME.setSize(20);
        fieldNAME.setSubSize(0);
        fieldNAME.setIsPrimaryKey(false);
        fieldNAME.setIsIdentity(false);
        fieldNAME.setUnique(false);
        fieldNAME.setShouldAllowNull(false);
        table.addField(fieldNAME);

        FieldDefinition fieldOFFICE_ID = new FieldDefinition();
        fieldOFFICE_ID.setName("OFFICE_ID");
        fieldOFFICE_ID.setTypeName("INTEGER");
        fieldOFFICE_ID.setSize(0);
        fieldOFFICE_ID.setSubSize(0);
        fieldOFFICE_ID.setIsPrimaryKey(false);
        fieldOFFICE_ID.setIsIdentity(false);
        fieldOFFICE_ID.setUnique(false);
        fieldOFFICE_ID.setShouldAllowNull(true);
        table.addField(fieldOFFICE_ID);

        return table;
    }

    public TableDefinition buildREL_EMPTable() {
        TableDefinition table = new TableDefinition();
        table.setName("REL_EMP");

        FieldDefinition fieldID = new FieldDefinition();
        fieldID.setName("ID");
        fieldID.setTypeName("INTEGER");
        fieldID.setSize(0);
        fieldID.setSubSize(0);
        fieldID.setIsPrimaryKey(true);
        fieldID.setIsIdentity(false);
        fieldID.setUnique(false);
        fieldID.setShouldAllowNull(false);
        table.addField(fieldID);

        FieldDefinition fieldNAME = new FieldDefinition();
        fieldNAME.setName("NAME");
        fieldNAME.setTypeName("VARCHAR");
        fieldNAME.setSize(255);
        fieldNAME.setSubSize(0);
        fieldNAME.setIsPrimaryKey(false);
        fieldNAME.setIsIdentity(false);
        fieldNAME.setUnique(false);
        fieldNAME.setShouldAllowNull(false);
        table.addField(fieldNAME);

        FieldDefinition fieldDEPTNO = new FieldDefinition();
        fieldDEPTNO.setName("DEPTNO");
        fieldDEPTNO.setTypeName("INTEGER");
        fieldDEPTNO.setSize(0);
        fieldDEPTNO.setSubSize(0);
        fieldDEPTNO.setIsPrimaryKey(false);
        fieldDEPTNO.setIsIdentity(false);
        fieldDEPTNO.setUnique(false);
        fieldDEPTNO.setShouldAllowNull(true);
        table.addField(fieldDEPTNO);

        return table;
    }

    public TableDefinition buildREL_DEPTTable() {
        TableDefinition table = new TableDefinition();
        table.setName("REL_DEPT");

        FieldDefinition fieldID = new FieldDefinition();
        fieldID.setName("DEPTNO");
        fieldID.setTypeName("INTEGER");
        fieldID.setSize(0);
        fieldID.setSubSize(0);
        fieldID.setIsPrimaryKey(true);
        fieldID.setIsIdentity(false);
        fieldID.setUnique(false);
        fieldID.setShouldAllowNull(false);
        table.addField(fieldID);

        FieldDefinition fieldNAME = new FieldDefinition();
        fieldNAME.setName("NAME");
        fieldNAME.setTypeName("VARCHAR");
        fieldNAME.setSize(255);
        fieldNAME.setSubSize(0);
        fieldNAME.setIsPrimaryKey(false);
        fieldNAME.setIsIdentity(false);
        fieldNAME.setUnique(false);
        fieldNAME.setShouldAllowNull(false);
        table.addField(fieldNAME);

        return table;
    }

    public TableDefinition buildRESOURCETable() {
        TableDefinition table = new TableDefinition();
        table.setName("REL_RESOURCE");

        FieldDefinition fieldID = new FieldDefinition();
        fieldID.setName("ID");
        fieldID.setTypeName("INTEGER");
        fieldID.setSize(0);
        fieldID.setSubSize(0);
        fieldID.setIsPrimaryKey(true);
        fieldID.setIsIdentity(false);
        fieldID.setUnique(false);
        fieldID.setShouldAllowNull(false);
        table.addField(fieldID);

        FieldDefinition fieldNAME = new FieldDefinition();
        fieldNAME.setName("NAME");
        fieldNAME.setTypeName("VARCHAR");
        fieldNAME.setSize(100);
        fieldNAME.setSubSize(0);
        fieldNAME.setIsPrimaryKey(false);
        fieldNAME.setIsIdentity(false);
        fieldNAME.setUnique(false);
        fieldNAME.setShouldAllowNull(false);
        table.addField(fieldNAME);

        FieldDefinition fieldOFFICE = new FieldDefinition();
        fieldOFFICE.setName("OFFICE_ID");
        fieldOFFICE.setTypeName("INTEGER");
        fieldOFFICE.setSize(0);
        fieldOFFICE.setSubSize(0);
        fieldOFFICE.setIsPrimaryKey(false);
        fieldOFFICE.setIsIdentity(false);
        fieldOFFICE.setUnique(false);
        fieldOFFICE.setShouldAllowNull(true);
        table.addField(fieldOFFICE);

        return table;
    }
}
