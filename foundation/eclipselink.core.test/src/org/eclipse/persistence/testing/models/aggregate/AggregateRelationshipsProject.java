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
 *     dminsky - initial API and implementation
 ******************************************************************************/
package org.eclipse.persistence.testing.models.aggregate;

import org.eclipse.persistence.sessions.*;
import org.eclipse.persistence.descriptors.*;
import org.eclipse.persistence.mappings.*;

/**
 * This class was generated by the TopLink project class generator.
 * It stores the meta-data (descriptors) that define the TopLink mappings.
 * ## Eclipse Persistence Services - @VERSION@.@QUALIFIER@ ##
 * @see org.eclipse.persistence.sessions.factories.ProjectClassGenerator
 */

public class AggregateRelationshipsProject extends org.eclipse.persistence.sessions.Project {

public AggregateRelationshipsProject() {
    setName("AggregateProblem");
    applyLogin();

    addDescriptor(buildAggregateClassDescriptor());
    addDescriptor(buildChildClassDescriptor());
    addDescriptor(buildCousinClassDescriptor());
    addDescriptor(buildParentClassDescriptor());
    addDescriptor(buildRelativeClassDescriptor());
    addDescriptor(buildStepChildClassDescriptor());
}

public void applyLogin() {
    setDatasourceLogin(new DatabaseLogin());
}

public ClassDescriptor buildAggregateClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.descriptorIsAggregate();
    descriptor.setJavaClass(Aggregate.class);

    // ClassDescriptor Properties.
    descriptor.setAlias("Aggregate");


    // Query Manager.


    // Event Manager.

    // Mappings.
    DirectCollectionMapping nicknamesMapping = new DirectCollectionMapping();
    nicknamesMapping.setAttributeName("nicknames");
    nicknamesMapping.dontUseIndirection();
    nicknamesMapping.setReferenceTableName("AGG_NICKNAME");
    nicknamesMapping.setDirectFieldName("AGG_NICKNAME.NICKNAME");
    nicknamesMapping.addReferenceKeyFieldName("AGG_NICKNAME.PARENT_ID", "nicknames->P_ID_IN_REFERENCE_NICKNAME_PARENT");
    descriptor.addMapping(nicknamesMapping);

    OneToOneMapping cousinMapping = new OneToOneMapping();
    cousinMapping.setAttributeName("cousin");
    cousinMapping.setReferenceClass(Cousin.class);
    cousinMapping.dontUseIndirection();
    cousinMapping.addForeignKeyFieldName("cousin->COUSIN_ID_IN_REFERENCE_PARENT_COUSIN", "AGG_COUSIN.ID");
    descriptor.addMapping(cousinMapping);

    OneToOneMapping stepChildMapping = new OneToOneMapping();
    stepChildMapping.setAttributeName("stepChild");
    stepChildMapping.setReferenceClass(StepChild.class);
    stepChildMapping.dontUseIndirection();
    stepChildMapping.addTargetForeignKeyFieldName("AGG_STEPCHILD.PARENT_ID", "stepChild->PARENT_ID_IN_REFERENCE_STEPCHILD_PARENT");
    descriptor.addMapping(stepChildMapping);

    OneToManyMapping childrenMapping = new OneToManyMapping();
    childrenMapping.setAttributeName("children");
    childrenMapping.setReferenceClass(Child.class);
    childrenMapping.useTransparentCollection();
    childrenMapping.useCollectionClass(org.eclipse.persistence.indirection.IndirectList.class);
    childrenMapping.addTargetForeignKeyFieldName("AGG_CHILD.PARENT_ID", "children->P_ID_IN_REFERENCE_CHILD_PARENT");
    descriptor.addMapping(childrenMapping);

    ManyToManyMapping relativesMapping = new ManyToManyMapping();
    relativesMapping.setAttributeName("relatives");
    relativesMapping.setReferenceClass(Relative.class);
    relativesMapping.privateOwnedRelationship();
    relativesMapping.useTransparentCollection();
    relativesMapping.useCollectionClass(org.eclipse.persistence.indirection.IndirectList.class);
    relativesMapping.setRelationTableName("AGG_PARENT_RELATIVE");
    relativesMapping.addSourceRelationKeyFieldName("AGG_PARENT_RELATIVE.PARENT_ID", "relatives->P_ID_IN_REFERENCE_RELATIVE_TO_PARENT");
    relativesMapping.addTargetRelationKeyFieldName("AGG_PARENT_RELATIVE.RELATIVE_ID", "AGG_RELATIVE.REL_ID");
    descriptor.addMapping(relativesMapping);

    return descriptor;
}

public ClassDescriptor buildChildClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.setJavaClass(Child.class);
    descriptor.addTableName("AGG_CHILD");
    descriptor.addPrimaryKeyFieldName("AGG_CHILD.ID");

    // ClassDescriptor Properties.
    descriptor.useSoftCacheWeakIdentityMap();
    descriptor.setIdentityMapSize(100);
    descriptor.useRemoteSoftCacheWeakIdentityMap();
    descriptor.setRemoteIdentityMapSize(100);
    descriptor.setSequenceNumberFieldName("AGG_CHILD.ID");
    descriptor.setSequenceNumberName("Agg_Child_Seq");
    descriptor.setAlias("Child");


    // Query Manager.
    descriptor.getQueryManager().checkCacheForDoesExist();


    // Event Manager.

    // Mappings.
    DirectToFieldMapping idMapping = new DirectToFieldMapping();
    idMapping.setAttributeName("id");
    idMapping.setFieldName("AGG_CHILD.ID");
    descriptor.addMapping(idMapping);

    OneToOneMapping parentMapping = new OneToOneMapping();
    parentMapping.setAttributeName("parent");
    parentMapping.setReferenceClass(Parent.class);
    parentMapping.dontUseIndirection();
    parentMapping.addForeignKeyFieldName("AGG_CHILD.PARENT_ID", "AGG_PARENT.P_ID");
    descriptor.addMapping(parentMapping);

    return descriptor;
}

public ClassDescriptor buildCousinClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.setJavaClass(Cousin.class);
    descriptor.addTableName("AGG_COUSIN");
    descriptor.addPrimaryKeyFieldName("AGG_COUSIN.ID");

    // ClassDescriptor Properties.
    descriptor.useSoftCacheWeakIdentityMap();
    descriptor.setIdentityMapSize(100);
    descriptor.useRemoteSoftCacheWeakIdentityMap();
    descriptor.setRemoteIdentityMapSize(100);
    descriptor.setSequenceNumberFieldName("AGG_COUSIN.ID");
    descriptor.setSequenceNumberName("Agg_Cousin_Seq");
    descriptor.setAlias("Cousin");


    // Query Manager.
    descriptor.getQueryManager().checkCacheForDoesExist();


    // Event Manager.

    // Mappings.
    DirectToFieldMapping idMapping = new DirectToFieldMapping();
    idMapping.setAttributeName("id");
    idMapping.setFieldName("AGG_COUSIN.ID");
    descriptor.addMapping(idMapping);

    DirectToFieldMapping nameMapping = new DirectToFieldMapping();
    nameMapping.setAttributeName("name");
    nameMapping.setFieldName("AGG_COUSIN.NAME");
    descriptor.addMapping(nameMapping);

    return descriptor;
}

public ClassDescriptor buildParentClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.setJavaClass(Parent.class);
    descriptor.addTableName("AGG_PARENT");
    descriptor.addPrimaryKeyFieldName("AGG_PARENT.P_ID");

    // ClassDescriptor Properties.
    descriptor.useSoftCacheWeakIdentityMap();
    descriptor.setIdentityMapSize(100);
    descriptor.useRemoteSoftCacheWeakIdentityMap();
    descriptor.setRemoteIdentityMapSize(100);
    descriptor.setSequenceNumberFieldName("AGG_PARENT.P_ID");
    descriptor.setSequenceNumberName("Agg_Parent_Seq");
    descriptor.setAlias("Parent");


    // Query Manager.
    descriptor.getQueryManager().checkCacheForDoesExist();


    // Event Manager.

    // Mappings.
    DirectToFieldMapping idMapping = new DirectToFieldMapping();
    idMapping.setAttributeName("id");
    idMapping.setFieldName("AGG_PARENT.P_ID");
    descriptor.addMapping(idMapping);

    AggregateObjectMapping aggregateMapping = new AggregateObjectMapping();
    aggregateMapping.setAttributeName("aggregate");
    aggregateMapping.setReferenceClass(Aggregate.class);
    aggregateMapping.setIsNullAllowed(false);
    aggregateMapping.addFieldNameTranslation("AGG_PARENT.P_ID", "relatives->P_ID_IN_REFERENCE_RELATIVE_TO_PARENT");
    aggregateMapping.addFieldNameTranslation("AGG_PARENT.P_ID", "children->P_ID_IN_REFERENCE_CHILD_PARENT");
    aggregateMapping.addFieldNameTranslation("AGG_PARENT.P_ID", "nicknames->P_ID_IN_REFERENCE_NICKNAME_PARENT");
    aggregateMapping.addFieldNameTranslation("AGG_PARENT.COUSIN_ID", "cousin->COUSIN_ID_IN_REFERENCE_PARENT_COUSIN");
    aggregateMapping.addFieldNameTranslation("AGG_PARENT.P_ID", "stepChild->PARENT_ID_IN_REFERENCE_STEPCHILD_PARENT");
    descriptor.addMapping(aggregateMapping);

    return descriptor;
}

public ClassDescriptor buildRelativeClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.setJavaClass(Relative.class);
    descriptor.addTableName("AGG_RELATIVE");
    descriptor.addPrimaryKeyFieldName("AGG_RELATIVE.REL_ID");

    // ClassDescriptor Properties.
    descriptor.useSoftCacheWeakIdentityMap();
    descriptor.setIdentityMapSize(100);
    descriptor.useRemoteSoftCacheWeakIdentityMap();
    descriptor.setRemoteIdentityMapSize(100);
    descriptor.setSequenceNumberFieldName("AGG_RELATIVE.REL_ID");
    descriptor.setSequenceNumberName("Agg_Relative_Seq");
    descriptor.setAlias("Relative");


    // Query Manager.
    descriptor.getQueryManager().checkCacheForDoesExist();


    // Event Manager.

    // Mappings.
    DirectToFieldMapping idMapping = new DirectToFieldMapping();
    idMapping.setAttributeName("id");
    idMapping.setFieldName("AGG_RELATIVE.REL_ID");
    descriptor.addMapping(idMapping);

    DirectToFieldMapping nameMapping = new DirectToFieldMapping();
    nameMapping.setAttributeName("name");
    nameMapping.setFieldName("AGG_RELATIVE.NAME");
    descriptor.addMapping(nameMapping);

    return descriptor;
}

public ClassDescriptor buildStepChildClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.setJavaClass(StepChild.class);
    descriptor.addTableName("AGG_STEPCHILD");
    descriptor.addPrimaryKeyFieldName("AGG_STEPCHILD.ID");

    // ClassDescriptor Properties.
    descriptor.useSoftCacheWeakIdentityMap();
    descriptor.setIdentityMapSize(100);
    descriptor.useRemoteSoftCacheWeakIdentityMap();
    descriptor.setRemoteIdentityMapSize(100);
    descriptor.setSequenceNumberFieldName("AGG_STEPCHILD.ID");
    descriptor.setSequenceNumberName("Agg_StepChild_Seq");
    descriptor.setAlias("StepChild");


    // Query Manager.
    descriptor.getQueryManager().checkCacheForDoesExist();


    // Event Manager.

    // Mappings.
    DirectToFieldMapping idMapping = new DirectToFieldMapping();
    idMapping.setAttributeName("id");
    idMapping.setFieldName("AGG_STEPCHILD.ID");
    descriptor.addMapping(idMapping);

    OneToOneMapping parentMapping = new OneToOneMapping();
    parentMapping.setAttributeName("parent");
    parentMapping.setReferenceClass(Parent.class);
    parentMapping.dontUseIndirection();
    parentMapping.addForeignKeyFieldName("AGG_STEPCHILD.PARENT_ID", "AGG_PARENT.P_ID");
    descriptor.addMapping(parentMapping);

    return descriptor;
}

}
