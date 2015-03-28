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
package org.eclipse.persistence.testing.models.optimisticlocking;

import org.eclipse.persistence.sessions.*;
import org.eclipse.persistence.descriptors.*;
import org.eclipse.persistence.mappings.*;
import org.eclipse.persistence.mappings.converters.*;

/**
 * This class was generated by the TopLink project class generator.
 * It stores the meta-data (descriptors) that define the TopLink mappings.
 * ## Eclipse Persistence Services - 2.2.0.v20100813-r8033 ##
 * @see org.eclipse.persistence.sessions.factories.ProjectClassGenerator
 */

public class GamesConsoleProject extends org.eclipse.persistence.sessions.Project {

public GamesConsoleProject() {
    setName("GamesConsoleSystem");
    applyLogin();

    addDescriptor(buildCameraClassDescriptor());
    addDescriptor(buildControllerClassDescriptor());
    addDescriptor(buildGamerClassDescriptor());
    addDescriptor(buildGamesConsoleClassDescriptor());
    addDescriptor(buildKnittingClassDescriptor());
    addDescriptor(buildPowerSupplyUnitClassDescriptor());
    addDescriptor(buildCookingClassDescriptor());
    addDescriptor(buildSkillClassDescriptor());
}

public void applyLogin() {
    DatabaseLogin login = new DatabaseLogin();
    login.usePlatform(new org.eclipse.persistence.platform.database.OraclePlatform());
    login.setDriverClassName("null");
    login.setConnectionString("null");
    login.setUserName("");

    // Configuration Properties.

    setDatasourceLogin(login);
}

public ClassDescriptor buildCameraClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.setJavaClass(org.eclipse.persistence.testing.models.optimisticlocking.Camera.class);
    descriptor.addTableName("OL_CAMERA");
    descriptor.addPrimaryKeyFieldName("OL_CAMERA.ID");

    // ClassDescriptor Properties.
    descriptor.useSoftCacheWeakIdentityMap();
    descriptor.setIdentityMapSize(100);
    descriptor.useRemoteSoftCacheWeakIdentityMap();
    descriptor.setRemoteIdentityMapSize(100);
    descriptor.setSequenceNumberFieldName("OL_CAMERA.ID");
    descriptor.setSequenceNumberName("OL_CAMERA_SEQ");
    descriptor.setAlias("Camera");


    // Query Manager.
    descriptor.getQueryManager().checkCacheForDoesExist();


    // Event Manager.

    // Mappings.
    DirectToFieldMapping descriptionMapping = new DirectToFieldMapping();
    descriptionMapping.setAttributeName("description");
    descriptionMapping.setFieldName("OL_CAMERA.DESCRIPTION");
    descriptor.addMapping(descriptionMapping);

    DirectToFieldMapping idMapping = new DirectToFieldMapping();
    idMapping.setAttributeName("id");
    idMapping.setFieldName("OL_CAMERA.ID");
    descriptor.addMapping(idMapping);

    DirectToFieldMapping nameMapping = new DirectToFieldMapping();
    nameMapping.setAttributeName("name");
    nameMapping.setFieldName("OL_CAMERA.NAME");
    descriptor.addMapping(nameMapping);

    DirectToFieldMapping updatedMapping = new DirectToFieldMapping();
    updatedMapping.setAttributeName("updated");
    updatedMapping.setFieldName("OL_CAMERA.UPDATED");
    descriptor.addMapping(updatedMapping);

    DirectToFieldMapping versionMapping = new DirectToFieldMapping();
    versionMapping.setAttributeName("version");
    versionMapping.setFieldName("OL_CAMERA.VERSION");
    descriptor.addMapping(versionMapping);

    return descriptor;
}

public ClassDescriptor buildControllerClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.setJavaClass(org.eclipse.persistence.testing.models.optimisticlocking.Controller.class);
    descriptor.addTableName("OL_CONTROLLER");
    descriptor.addPrimaryKeyFieldName("OL_CONTROLLER.ID");

    // ClassDescriptor Properties.
    descriptor.useSoftCacheWeakIdentityMap();
    descriptor.setIdentityMapSize(100);
    descriptor.useRemoteSoftCacheWeakIdentityMap();
    descriptor.setRemoteIdentityMapSize(100);
    descriptor.setSequenceNumberFieldName("OL_CONTROLLER.ID");
    descriptor.setSequenceNumberName("OL_CONTROLLER_SEQ");
    descriptor.setAlias("Controller");


    // Query Manager.
    descriptor.getQueryManager().checkCacheForDoesExist();


    // Event Manager.

    // Mappings.
    DirectToFieldMapping descriptionMapping = new DirectToFieldMapping();
    descriptionMapping.setAttributeName("description");
    descriptionMapping.setFieldName("OL_CONTROLLER.DESCRIPTION");
    descriptor.addMapping(descriptionMapping);

    DirectToFieldMapping idMapping = new DirectToFieldMapping();
    idMapping.setAttributeName("id");
    idMapping.setFieldName("OL_CONTROLLER.ID");
    descriptor.addMapping(idMapping);

    DirectToFieldMapping nameMapping = new DirectToFieldMapping();
    nameMapping.setAttributeName("name");
    nameMapping.setFieldName("OL_CONTROLLER.NAME");
    descriptor.addMapping(nameMapping);

    DirectToFieldMapping updatedMapping = new DirectToFieldMapping();
    updatedMapping.setAttributeName("updated");
    updatedMapping.setFieldName("OL_CONTROLLER.UPDATED");
    descriptor.addMapping(updatedMapping);

    DirectToFieldMapping versionMapping = new DirectToFieldMapping();
    versionMapping.setAttributeName("version");
    versionMapping.setFieldName("OL_CONTROLLER.VERSION");
    descriptor.addMapping(versionMapping);

    OneToOneMapping consoleMapping = new OneToOneMapping();
    consoleMapping.setAttributeName("console");
    consoleMapping.setReferenceClass(org.eclipse.persistence.testing.models.optimisticlocking.GamesConsole.class);
    consoleMapping.useBasicIndirection();
    consoleMapping.addForeignKeyFieldName("OL_CONTROLLER.CONSOLE_ID", "OL_CONSOLE.ID");
    descriptor.addMapping(consoleMapping);

    return descriptor;
}

public ClassDescriptor buildGamerClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.setJavaClass(org.eclipse.persistence.testing.models.optimisticlocking.Gamer.class);
    descriptor.addTableName("OL_GAMER");
    descriptor.addPrimaryKeyFieldName("OL_GAMER.ID");

    // ClassDescriptor Properties.
    descriptor.useSoftCacheWeakIdentityMap();
    descriptor.setIdentityMapSize(100);
    descriptor.useRemoteSoftCacheWeakIdentityMap();
    descriptor.setRemoteIdentityMapSize(100);
    descriptor.setSequenceNumberFieldName("OL_GAMER.ID");
    descriptor.setSequenceNumberName("OL_GAMER_SEQ");
    descriptor.setAlias("Gamer");


    // Query Manager.
    descriptor.getQueryManager().checkCacheForDoesExist();


    // Event Manager.

    // Mappings.
    DirectToFieldMapping descriptionMapping = new DirectToFieldMapping();
    descriptionMapping.setAttributeName("description");
    descriptionMapping.setFieldName("OL_GAMER.DESCRIPTION");
    descriptor.addMapping(descriptionMapping);

    DirectToFieldMapping idMapping = new DirectToFieldMapping();
    idMapping.setAttributeName("id");
    idMapping.setFieldName("OL_GAMER.ID");
    descriptor.addMapping(idMapping);

    DirectToFieldMapping nameMapping = new DirectToFieldMapping();
    nameMapping.setAttributeName("name");
    nameMapping.setFieldName("OL_GAMER.NAME");
    descriptor.addMapping(nameMapping);

    DirectToFieldMapping updatedMapping = new DirectToFieldMapping();
    updatedMapping.setAttributeName("updated");
    updatedMapping.setFieldName("OL_GAMER.UPDATED");
    descriptor.addMapping(updatedMapping);

    DirectToFieldMapping versionMapping = new DirectToFieldMapping();
    versionMapping.setAttributeName("version");
    versionMapping.setFieldName("OL_GAMER.VERSION");
    descriptor.addMapping(versionMapping);

    VariableOneToOneMapping skillMapping = new VariableOneToOneMapping();
    skillMapping.setAttributeName("skill");
    skillMapping.setReferenceClass(org.eclipse.persistence.testing.models.optimisticlocking.Skill.class);
    skillMapping.useProxyIndirection();
    skillMapping.addForeignQueryKeyName("OL_GAMER.SKILL_ID", "id");
    skillMapping.setTypeFieldName("OL_GAMER.SKILL_INDICATOR");
    skillMapping.addClassIndicator(org.eclipse.persistence.testing.models.optimisticlocking.Knitting.class, "Knitting");
    skillMapping.addClassIndicator(org.eclipse.persistence.testing.models.optimisticlocking.Cooking.class, "Pwning");
    descriptor.addMapping(skillMapping);

    return descriptor;
}

public ClassDescriptor buildGamesConsoleClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.setJavaClass(org.eclipse.persistence.testing.models.optimisticlocking.GamesConsole.class);
    descriptor.addTableName("OL_CONSOLE");
    descriptor.addPrimaryKeyFieldName("OL_CONSOLE.ID");

    // ClassDescriptor Properties.
    descriptor.useSoftCacheWeakIdentityMap();
    descriptor.setIdentityMapSize(100);
    descriptor.useRemoteSoftCacheWeakIdentityMap();
    descriptor.setRemoteIdentityMapSize(100);
    descriptor.setSequenceNumberFieldName("OL_CONSOLE.ID");
    descriptor.setSequenceNumberName("OL_CONSOLE_SEQ");
    descriptor.setAlias("GamesConsole");


    // Query Manager.
    descriptor.getQueryManager().checkCacheForDoesExist();


    // Event Manager.

    // Mappings.
    DirectToFieldMapping descriptionMapping = new DirectToFieldMapping();
    descriptionMapping.setAttributeName("description");
    descriptionMapping.setFieldName("OL_CONSOLE.DESCRIPTION");
    descriptor.addMapping(descriptionMapping);

    DirectToFieldMapping idMapping = new DirectToFieldMapping();
    idMapping.setAttributeName("id");
    idMapping.setFieldName("OL_CONSOLE.ID");
    descriptor.addMapping(idMapping);

    DirectToFieldMapping nameMapping = new DirectToFieldMapping();
    nameMapping.setAttributeName("name");
    nameMapping.setFieldName("OL_CONSOLE.NAME");
    descriptor.addMapping(nameMapping);

    DirectToFieldMapping updatedMapping = new DirectToFieldMapping();
    updatedMapping.setAttributeName("updated");
    updatedMapping.setFieldName("OL_CONSOLE.UPDATED");
    descriptor.addMapping(updatedMapping);

    DirectToFieldMapping versionMapping = new DirectToFieldMapping();
    versionMapping.setAttributeName("version");
    versionMapping.setFieldName("OL_CONSOLE.VERSION");
    descriptor.addMapping(versionMapping);

    AggregateObjectMapping psuMapping = new AggregateObjectMapping();
    psuMapping.setAttributeName("psu");
    psuMapping.setReferenceClass(org.eclipse.persistence.testing.models.optimisticlocking.PowerSupplyUnit.class);
    psuMapping.setIsNullAllowed(true);
    psuMapping.addFieldNameTranslation("OL_CONSOLE.PSU_SERIAL", "serialNumber->DIRECT");
    psuMapping.addFieldNameTranslation("OL_CONSOLE.PSU_ON", "on->DIRECT");
    descriptor.addMapping(psuMapping);

    OneToOneMapping cameraMapping = new OneToOneMapping();
    cameraMapping.setAttributeName("camera");
    cameraMapping.setReferenceClass(org.eclipse.persistence.testing.models.optimisticlocking.Camera.class);
    cameraMapping.useBasicIndirection();
    cameraMapping.addForeignKeyFieldName("OL_CONSOLE.CAMERA_ID", "OL_CAMERA.ID");
    descriptor.addMapping(cameraMapping);

    OneToManyMapping controllersMapping = new OneToManyMapping();
    controllersMapping.setAttributeName("controllers");
    controllersMapping.setReferenceClass(org.eclipse.persistence.testing.models.optimisticlocking.Controller.class);
    controllersMapping.useTransparentCollection();
    controllersMapping.useCollectionClass(org.eclipse.persistence.indirection.IndirectList.class);
    controllersMapping.addTargetForeignKeyFieldName("OL_CONTROLLER.CONSOLE_ID", "OL_CONSOLE.ID");
    descriptor.addMapping(controllersMapping);

    ManyToManyMapping gamersMapping = new ManyToManyMapping();
    gamersMapping.setAttributeName("gamers");
    gamersMapping.setReferenceClass(org.eclipse.persistence.testing.models.optimisticlocking.Gamer.class);
    gamersMapping.useTransparentCollection();
    gamersMapping.useCollectionClass(org.eclipse.persistence.indirection.IndirectList.class);
    gamersMapping.setRelationTableName("OL_CONSOLE_OL_GAMER");
    gamersMapping.addSourceRelationKeyFieldName("OL_CONSOLE_OL_GAMER.GAMER_ID", "OL_CONSOLE.ID");
    gamersMapping.addTargetRelationKeyFieldName("OL_CONSOLE_OL_GAMER.CONSOLE_ID", "OL_GAMER.ID");
    descriptor.addMapping(gamersMapping);

    return descriptor;
}

public ClassDescriptor buildKnittingClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.setJavaClass(org.eclipse.persistence.testing.models.optimisticlocking.Knitting.class);
    descriptor.addTableName("OL_SKILL");
    descriptor.addPrimaryKeyFieldName("OL_SKILL.ID");

    // Interface Properties.
    descriptor.getInterfacePolicy().addParentInterface(org.eclipse.persistence.testing.models.optimisticlocking.Skill.class);

    // ClassDescriptor Properties.
    descriptor.useSoftCacheWeakIdentityMap();
    descriptor.setIdentityMapSize(100);
    descriptor.useRemoteSoftCacheWeakIdentityMap();
    descriptor.setRemoteIdentityMapSize(100);
    descriptor.setSequenceNumberFieldName("OL_SKILL.ID");
    descriptor.setSequenceNumberName("OL_SKILL_SEQ");
    descriptor.setAlias("Knitting");


    // Query Manager.
    descriptor.getQueryManager().checkCacheForDoesExist();


    // Event Manager.

    // Mappings.
    DirectToFieldMapping descriptionMapping = new DirectToFieldMapping();
    descriptionMapping.setAttributeName("description");
    descriptionMapping.setFieldName("OL_SKILL.DESCRIPTION");
    descriptor.addMapping(descriptionMapping);

    DirectToFieldMapping idMapping = new DirectToFieldMapping();
    idMapping.setAttributeName("id");
    idMapping.setFieldName("OL_SKILL.ID");
    descriptor.addMapping(idMapping);

    DirectToFieldMapping nameMapping = new DirectToFieldMapping();
    nameMapping.setAttributeName("name");
    nameMapping.setFieldName("OL_SKILL.NAME");
    descriptor.addMapping(nameMapping);

    DirectToFieldMapping updatedMapping = new DirectToFieldMapping();
    updatedMapping.setAttributeName("updated");
    updatedMapping.setFieldName("OL_SKILL.UPDATED");
    descriptor.addMapping(updatedMapping);

    DirectToFieldMapping versionMapping = new DirectToFieldMapping();
    versionMapping.setAttributeName("version");
    versionMapping.setFieldName("OL_SKILL.VERSION");
    descriptor.addMapping(versionMapping);

    return descriptor;
}

public ClassDescriptor buildPowerSupplyUnitClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.descriptorIsAggregate();
    descriptor.setJavaClass(org.eclipse.persistence.testing.models.optimisticlocking.PowerSupplyUnit.class);

    // ClassDescriptor Properties.
    descriptor.setAlias("PowerSupplyUnit");


    // Query Manager.


    // Event Manager.

    // Mappings.
    DirectToFieldMapping onMapping = new DirectToFieldMapping();
    onMapping.setAttributeName("on");
    onMapping.setFieldName("on->DIRECT");
    ObjectTypeConverter onMappingConverter = new ObjectTypeConverter();
    onMappingConverter.setDefaultAttributeValue(new java.lang.Boolean("false"));
    onMappingConverter.addConversionValue(new Character('F'), new java.lang.Boolean("false"));
    onMappingConverter.addConversionValue(new Character('T'), new java.lang.Boolean("true"));
    onMapping.setConverter(onMappingConverter);
    descriptor.addMapping(onMapping);

    DirectToFieldMapping serialNumberMapping = new DirectToFieldMapping();
    serialNumberMapping.setAttributeName("serialNumber");
    serialNumberMapping.setFieldName("serialNumber->DIRECT");
    descriptor.addMapping(serialNumberMapping);

    return descriptor;
}

public ClassDescriptor buildCookingClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.setJavaClass(org.eclipse.persistence.testing.models.optimisticlocking.Cooking.class);
    descriptor.addTableName("OL_SKILL");
    descriptor.addPrimaryKeyFieldName("OL_SKILL.ID");

    // Interface Properties.
    descriptor.getInterfacePolicy().addParentInterface(org.eclipse.persistence.testing.models.optimisticlocking.Skill.class);

    // ClassDescriptor Properties.
    descriptor.useSoftCacheWeakIdentityMap();
    descriptor.setIdentityMapSize(100);
    descriptor.useRemoteSoftCacheWeakIdentityMap();
    descriptor.setRemoteIdentityMapSize(100);
    descriptor.setSequenceNumberFieldName("OL_SKILL.ID");
    descriptor.setSequenceNumberName("OL_SKILL_SEQ");
    descriptor.setAlias("Cooking");


    // Query Manager.
    descriptor.getQueryManager().checkCacheForDoesExist();


    // Event Manager.

    // Mappings.
    DirectToFieldMapping descriptionMapping = new DirectToFieldMapping();
    descriptionMapping.setAttributeName("description");
    descriptionMapping.setFieldName("OL_SKILL.DESCRIPTION");
    descriptor.addMapping(descriptionMapping);

    DirectToFieldMapping idMapping = new DirectToFieldMapping();
    idMapping.setAttributeName("id");
    idMapping.setFieldName("OL_SKILL.ID");
    descriptor.addMapping(idMapping);

    DirectToFieldMapping nameMapping = new DirectToFieldMapping();
    nameMapping.setAttributeName("name");
    nameMapping.setFieldName("OL_SKILL.NAME");
    descriptor.addMapping(nameMapping);

    DirectToFieldMapping updatedMapping = new DirectToFieldMapping();
    updatedMapping.setAttributeName("updated");
    updatedMapping.setFieldName("OL_SKILL.UPDATED");
    descriptor.addMapping(updatedMapping);

    DirectToFieldMapping versionMapping = new DirectToFieldMapping();
    versionMapping.setAttributeName("version");
    versionMapping.setFieldName("OL_SKILL.VERSION");
    descriptor.addMapping(versionMapping);

    return descriptor;
}

public ClassDescriptor buildSkillClassDescriptor() {
    RelationalDescriptor descriptor = new RelationalDescriptor();
    descriptor.descriptorIsForInterface();
    descriptor.setJavaClass(org.eclipse.persistence.testing.models.optimisticlocking.Skill.class);

    // Interface Properties.

    // ClassDescriptor Properties.
    descriptor.setAlias("Skill");


    // Query Manager.


    // Event Manager.

    // Query keys.
    descriptor.addAbstractQueryKey("id");
    descriptor.addAbstractQueryKey("updated");
    descriptor.addAbstractQueryKey("description");
    descriptor.addAbstractQueryKey("version");
    descriptor.addAbstractQueryKey("name");

    return descriptor;
}

}
