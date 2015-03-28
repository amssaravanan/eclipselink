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
package org.eclipse.persistence.testing.models.forceupdate;

import org.eclipse.persistence.sessions.*;
import org.eclipse.persistence.mappings.*;
import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.mappings.converters.*;

/**
 * This class was generated by the TopLink project class generator.
 * It stores the meta-data (descriptors) that define the TopLink mappings.
 * @see org.eclipse.persistence.sessions.factories.ProjectClassGenerator
 */
public class FUVLProject extends org.eclipse.persistence.sessions.Project {

    public FUVLProject() {
        setName("FUVL_TL4.0");
        applyLogin();

        addDescriptor(buildAddressTLICDescriptor());
        addDescriptor(buildAddressTLIODescriptor());
        addDescriptor(buildAddressVLICDescriptor());
        addDescriptor(buildAddressVLIODescriptor());
        addDescriptor(buildEmployeeTLICDescriptor());
        addDescriptor(buildEmployeeTLIODescriptor());
        addDescriptor(buildEmployeeVLICDescriptor());
        addDescriptor(buildEmployeeVLIODescriptor());
        addDescriptor(buildPhoneNumberTLICDescriptor());
        addDescriptor(buildPhoneNumberTLIODescriptor());
        addDescriptor(buildPhoneNumberVLICDescriptor());
        addDescriptor(buildPhoneNumberVLIODescriptor());
    }

    public void applyLogin() {
        DatabaseLogin login = new DatabaseLogin();
        setDatasourceLogin(login);
    }

    public RelationalDescriptor buildAddressTLICDescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.forceupdate.AddressTLIC.class);
        descriptor.addTableName("ADDRESSTLIC");
        descriptor.addPrimaryKeyFieldName("ADDRESSTLIC.ADDRESS_ID");

        // RelationalDescriptor properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);
        descriptor.setSequenceNumberFieldName("ADDRESSTLIC.ADDRESS_ID");
        descriptor.setSequenceNumberName("ADDRTLIC_SEQ");
        descriptor.useTimestampLocking("VERSION", true);

        // Query manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        //Named Queries

        // Event manager.

        // Mappings.
        DirectToFieldMapping idMapping = new DirectToFieldMapping();
        idMapping.setAttributeName("id");
        idMapping.setFieldName("ADDRESSTLIC.ADDRESS_ID");
        descriptor.addMapping(idMapping);

        DirectToFieldMapping streetMapping = new DirectToFieldMapping();
        streetMapping.setAttributeName("street");
        streetMapping.setFieldName("ADDRESSTLIC.STREET");
        descriptor.addMapping(streetMapping);

        DirectToFieldMapping cityMapping = new DirectToFieldMapping();
        cityMapping.setAttributeName("city");
        cityMapping.setFieldName("ADDRESSTLIC.CITY");
        descriptor.addMapping(cityMapping);

        DirectToFieldMapping provinceMapping = new DirectToFieldMapping();
        provinceMapping.setAttributeName("province");
        provinceMapping.setFieldName("ADDRESSTLIC.PROVINCE");
        descriptor.addMapping(provinceMapping);

        DirectToFieldMapping postalCodeMapping = new DirectToFieldMapping();
        postalCodeMapping.setAttributeName("postalCode");
        postalCodeMapping.setFieldName("ADDRESSTLIC.P_CODE");
        descriptor.addMapping(postalCodeMapping);

        DirectToFieldMapping countryMapping = new DirectToFieldMapping();
        countryMapping.setAttributeName("country");
        countryMapping.setFieldName("ADDRESSTLIC.COUNTORY");
        descriptor.addMapping(countryMapping);

        return descriptor;
    }

    public RelationalDescriptor buildAddressTLIODescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.forceupdate.AddressTLIO.class);
        descriptor.addTableName("ADDRESSTLIO");
        descriptor.addPrimaryKeyFieldName("ADDRESSTLIO.ADDRESS_ID");

        // RelationalDescriptor properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);
        descriptor.setSequenceNumberFieldName("ADDRESSTLIO.ADDRESS_ID");
        descriptor.setSequenceNumberName("ADDRTLIO");
        descriptor.useTimestampLocking("VERSION", false);

        // Query manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        //Named Queries

        // Event manager.

        // Mappings.
        DirectToFieldMapping idMapping = new DirectToFieldMapping();
        idMapping.setAttributeName("id");
        idMapping.setFieldName("ADDRESSTLIO.ADDRESS_ID");
        descriptor.addMapping(idMapping);

        DirectToFieldMapping streetMapping = new DirectToFieldMapping();
        streetMapping.setAttributeName("street");
        streetMapping.setFieldName("ADDRESSTLIO.STREET");
        descriptor.addMapping(streetMapping);

        DirectToFieldMapping cityMapping = new DirectToFieldMapping();
        cityMapping.setAttributeName("city");
        cityMapping.setFieldName("ADDRESSTLIO.CITY");
        descriptor.addMapping(cityMapping);

        DirectToFieldMapping provinceMapping = new DirectToFieldMapping();
        provinceMapping.setAttributeName("province");
        provinceMapping.setFieldName("ADDRESSTLIO.PROVINCE");
        descriptor.addMapping(provinceMapping);

        DirectToFieldMapping postalCodeMapping = new DirectToFieldMapping();
        postalCodeMapping.setAttributeName("postalCode");
        postalCodeMapping.setFieldName("ADDRESSTLIO.P_CODE");
        descriptor.addMapping(postalCodeMapping);

        DirectToFieldMapping countryMapping = new DirectToFieldMapping();
        countryMapping.setAttributeName("country");
        countryMapping.setFieldName("ADDRESSTLIO.COUNTRY");
        descriptor.addMapping(countryMapping);

        DirectToFieldMapping versionMapping = new DirectToFieldMapping();
        versionMapping.setAttributeName("version");
        versionMapping.setFieldName("ADDRESSTLIO.VERSION");
        descriptor.addMapping(versionMapping);

        return descriptor;
    }

    public RelationalDescriptor buildAddressVLICDescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.forceupdate.AddressVLIC.class);
        descriptor.addTableName("ADDRESSVLIC");
        descriptor.addPrimaryKeyFieldName("ADDRESSVLIC.ADDRESS_ID");

        // RelationalDescriptor properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);
        descriptor.setSequenceNumberFieldName("ADDRESSVLIC.ADDRESS_ID");
        descriptor.setSequenceNumberName("ADDRVLIC_SEQ");
        descriptor.useVersionLocking("VERSION");

        // Query manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        //Named Queries

        // Event manager.

        // Mappings.
        DirectToFieldMapping idMapping = new DirectToFieldMapping();
        idMapping.setAttributeName("id");
        idMapping.setFieldName("ADDRESSVLIC.ADDRESS_ID");
        descriptor.addMapping(idMapping);

        DirectToFieldMapping streetMapping = new DirectToFieldMapping();
        streetMapping.setAttributeName("street");
        streetMapping.setFieldName("ADDRESSVLIC.STREET");
        descriptor.addMapping(streetMapping);

        DirectToFieldMapping cityMapping = new DirectToFieldMapping();
        cityMapping.setAttributeName("city");
        cityMapping.setFieldName("ADDRESSVLIC.CITY");
        descriptor.addMapping(cityMapping);

        DirectToFieldMapping provinceMapping = new DirectToFieldMapping();
        provinceMapping.setAttributeName("province");
        provinceMapping.setFieldName("ADDRESSVLIC.PROVINCE");
        descriptor.addMapping(provinceMapping);

        DirectToFieldMapping postalCodeMapping = new DirectToFieldMapping();
        postalCodeMapping.setAttributeName("postalCode");
        postalCodeMapping.setFieldName("ADDRESSVLIC.P_CODE");
        descriptor.addMapping(postalCodeMapping);

        DirectToFieldMapping countryMapping = new DirectToFieldMapping();
        countryMapping.setAttributeName("country");
        countryMapping.setFieldName("ADDRESSVLIC.COUNTRY");
        descriptor.addMapping(countryMapping);

        return descriptor;
    }

    public RelationalDescriptor buildAddressVLIODescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.forceupdate.AddressVLIO.class);
        descriptor.addTableName("ADDRESSVLIO");
        descriptor.addPrimaryKeyFieldName("ADDRESSVLIO.ADDRESS_ID");

        // RelationalDescriptor properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);
        descriptor.setSequenceNumberFieldName("ADDRESSVLIO.ADDRESS_ID");
        descriptor.setSequenceNumberName("ADDRVLIO_SEQ");
        descriptor.useVersionLocking("VERSION", false);

        // Query manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        //Named Queries

        // Event manager.

        // Mappings.
        DirectToFieldMapping idMapping = new DirectToFieldMapping();
        idMapping.setAttributeName("id");
        idMapping.setFieldName("ADDRESSVLIO.ADDRESS_ID");
        descriptor.addMapping(idMapping);

        DirectToFieldMapping streetMapping = new DirectToFieldMapping();
        streetMapping.setAttributeName("street");
        streetMapping.setFieldName("ADDRESSVLIO.STREET");
        descriptor.addMapping(streetMapping);

        DirectToFieldMapping cityMapping = new DirectToFieldMapping();
        cityMapping.setAttributeName("city");
        cityMapping.setFieldName("ADDRESSVLIO.CITY");
        descriptor.addMapping(cityMapping);

        DirectToFieldMapping provinceMapping = new DirectToFieldMapping();
        provinceMapping.setAttributeName("province");
        provinceMapping.setFieldName("ADDRESSVLIO.PROVINCE");
        descriptor.addMapping(provinceMapping);

        DirectToFieldMapping postalCodeMapping = new DirectToFieldMapping();
        postalCodeMapping.setAttributeName("postalCode");
        postalCodeMapping.setFieldName("ADDRESSVLIO.P_CODE");
        descriptor.addMapping(postalCodeMapping);

        DirectToFieldMapping countryMapping = new DirectToFieldMapping();
        countryMapping.setAttributeName("country");
        countryMapping.setFieldName("ADDRESSVLIO.COUNTRY");
        descriptor.addMapping(countryMapping);

        DirectToFieldMapping versionMapping = new DirectToFieldMapping();
        versionMapping.setAttributeName("version");
        versionMapping.setFieldName("ADDRESSVLIO.VERSION");
        descriptor.addMapping(versionMapping);

        return descriptor;
    }

    public RelationalDescriptor buildEmployeeTLICDescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.forceupdate.EmployeeTLIC.class);
        descriptor.addTableName("EMPLOYEETLIC");
        descriptor.addPrimaryKeyFieldName("EMPLOYEETLIC.EMP_ID");

        // RelationalDescriptor properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);
        descriptor.setSequenceNumberFieldName("EMPLOYEETLIC.EMP_ID");
        descriptor.setSequenceNumberName("EMPTLIC_SEQ");
        descriptor.useTimestampLocking("VERSION", true);

        // Query manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        //Named Queries

        // Event manager.

        // Mappings.
        DirectToFieldMapping firstNameMapping = new DirectToFieldMapping();
        firstNameMapping.setAttributeName("firstName");
        firstNameMapping.setFieldName("EMPLOYEETLIC.F_NAME");
        descriptor.addMapping(firstNameMapping);

        DirectToFieldMapping lastNameMapping = new DirectToFieldMapping();
        lastNameMapping.setAttributeName("lastName");
        lastNameMapping.setFieldName("EMPLOYEETLIC.L_NAME");
        descriptor.addMapping(lastNameMapping);

        DirectToFieldMapping idMapping = new DirectToFieldMapping();
        idMapping.setAttributeName("id");
        idMapping.setFieldName("EMPLOYEETLIC.EMP_ID");
        descriptor.addMapping(idMapping);

        DirectToFieldMapping salaryMapping = new DirectToFieldMapping();
        salaryMapping.setAttributeName("salary");
        salaryMapping.setFieldName("EMPLOYEETLIC.SALARY");
        descriptor.addMapping(salaryMapping);

        DirectToFieldMapping genderMapping = new DirectToFieldMapping();
        genderMapping.setAttributeName("gender");
        genderMapping.setFieldName("EMPLOYEETLIC.GENDER");
        ObjectTypeConverter genderMappingConverter = new ObjectTypeConverter();
        genderMappingConverter.addConversionValue("F", "Female");
        genderMappingConverter.addConversionValue("M", "Male");
        genderMapping.setConverter(genderMappingConverter);
        descriptor.addMapping(genderMapping);

        OneToOneMapping addressMapping = new OneToOneMapping();
        addressMapping.setAttributeName("address");
        addressMapping.setReferenceClass(org.eclipse.persistence.testing.models.forceupdate.AddressTLIC.class);
        addressMapping.useBasicIndirection();
        addressMapping.addForeignKeyFieldName("EMPLOYEETLIC.ADDR_ID", "ADDRESSTLIC.ADDRESS_ID");
        descriptor.addMapping(addressMapping);

        OneToManyMapping phoneNumbersMapping = new OneToManyMapping();
        phoneNumbersMapping.setAttributeName("phoneNumbers");
        phoneNumbersMapping.setReferenceClass(org.eclipse.persistence.testing.models.forceupdate.PhoneNumberTLIC.class);
        phoneNumbersMapping.useBasicIndirection();
        phoneNumbersMapping.addTargetForeignKeyFieldName("PHONETLIC.EMP_ID", "EMPLOYEETLIC.EMP_ID");
        descriptor.addMapping(phoneNumbersMapping);

        return descriptor;
    }

    public RelationalDescriptor buildEmployeeTLIODescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.forceupdate.EmployeeTLIO.class);
        descriptor.addTableName("EMPLOYEETLIO");
        descriptor.addPrimaryKeyFieldName("EMPLOYEETLIO.EMP_ID");

        // RelationalDescriptor properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);
        descriptor.setSequenceNumberFieldName("EMPLOYEETLIO.EMP_ID");
        descriptor.setSequenceNumberName("EMPTLIO_SEQ");
        descriptor.useTimestampLocking("VERSION", false);

        // Query manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        //Named Queries

        // Event manager.

        // Mappings.
        DirectToFieldMapping firstNameMapping = new DirectToFieldMapping();
        firstNameMapping.setAttributeName("firstName");
        firstNameMapping.setFieldName("EMPLOYEETLIO.F_NAME");
        descriptor.addMapping(firstNameMapping);

        DirectToFieldMapping lastNameMapping = new DirectToFieldMapping();
        lastNameMapping.setAttributeName("lastName");
        lastNameMapping.setFieldName("EMPLOYEETLIO.L_NAME");
        descriptor.addMapping(lastNameMapping);

        DirectToFieldMapping idMapping = new DirectToFieldMapping();
        idMapping.setAttributeName("id");
        idMapping.setFieldName("EMPLOYEETLIO.EMP_ID");
        descriptor.addMapping(idMapping);

        DirectToFieldMapping versionMapping = new DirectToFieldMapping();
        versionMapping.setAttributeName("version");
        versionMapping.setFieldName("EMPLOYEETLIO.VERSION");
        descriptor.addMapping(versionMapping);

        DirectToFieldMapping salaryMapping = new DirectToFieldMapping();
        salaryMapping.setAttributeName("salary");
        salaryMapping.setFieldName("EMPLOYEETLIO.SALARY");
        descriptor.addMapping(salaryMapping);

        DirectToFieldMapping genderMapping = new DirectToFieldMapping();
        genderMapping.setAttributeName("gender");
        genderMapping.setFieldName("EMPLOYEETLIO.GENDER");
        ObjectTypeConverter genderMappingConverter = new ObjectTypeConverter();
        genderMappingConverter.addConversionValue("F", "Female");
        genderMappingConverter.addConversionValue("M", "Male");
        genderMapping.setConverter(genderMappingConverter);
        descriptor.addMapping(genderMapping);

        OneToOneMapping addressMapping = new OneToOneMapping();
        addressMapping.setAttributeName("address");
        addressMapping.setReferenceClass(org.eclipse.persistence.testing.models.forceupdate.AddressTLIO.class);
        addressMapping.useBasicIndirection();
        addressMapping.addForeignKeyFieldName("EMPLOYEETLIO.ADDR_ID", "ADDRESSTLIO.ADDRESS_ID");
        descriptor.addMapping(addressMapping);

        OneToManyMapping phoneNumbersMapping = new OneToManyMapping();
        phoneNumbersMapping.setAttributeName("phoneNumbers");
        phoneNumbersMapping.setReferenceClass(org.eclipse.persistence.testing.models.forceupdate.PhoneNumberTLIO.class);
        phoneNumbersMapping.useBasicIndirection();
        phoneNumbersMapping.addTargetForeignKeyFieldName("PHONETLIO.EMP_ID", "EMPLOYEETLIO.EMP_ID");
        descriptor.addMapping(phoneNumbersMapping);

        return descriptor;
    }

    public RelationalDescriptor buildEmployeeVLICDescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.forceupdate.EmployeeVLIC.class);
        descriptor.addTableName("EMPLOYEEVLIC");
        descriptor.addPrimaryKeyFieldName("EMPLOYEEVLIC.EMP_ID");

        // RelationalDescriptor properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);
        descriptor.setSequenceNumberFieldName("EMPLOYEEVLIC.EMP_ID");
        descriptor.setSequenceNumberName("EMPVLIC_SEQ");
        descriptor.useVersionLocking("VERSION");

        // Query manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        //Named Queries

        // Event manager.

        // Mappings.
        DirectToFieldMapping idMapping = new DirectToFieldMapping();
        idMapping.setAttributeName("id");
        idMapping.setFieldName("EMPLOYEEVLIC.EMP_ID");
        descriptor.addMapping(idMapping);

        DirectToFieldMapping firstNameMapping = new DirectToFieldMapping();
        firstNameMapping.setAttributeName("firstName");
        firstNameMapping.setFieldName("EMPLOYEEVLIC.F_NAME");
        descriptor.addMapping(firstNameMapping);

        DirectToFieldMapping lastNameMapping = new DirectToFieldMapping();
        lastNameMapping.setAttributeName("lastName");
        lastNameMapping.setFieldName("EMPLOYEEVLIC.L_NAME");
        descriptor.addMapping(lastNameMapping);

        DirectToFieldMapping genderMapping = new DirectToFieldMapping();
        genderMapping.setAttributeName("gender");
        genderMapping.setFieldName("EMPLOYEEVLIC.GENDER");
        descriptor.addMapping(genderMapping);

        DirectToFieldMapping salaryMapping = new DirectToFieldMapping();
        salaryMapping.setAttributeName("salary");
        salaryMapping.setFieldName("EMPLOYEEVLIC.SALARY");
        descriptor.addMapping(salaryMapping);

        OneToOneMapping addressMapping = new OneToOneMapping();
        addressMapping.setAttributeName("address");
        addressMapping.setReferenceClass(org.eclipse.persistence.testing.models.forceupdate.AddressVLIC.class);
        addressMapping.useBasicIndirection();
        addressMapping.addForeignKeyFieldName("EMPLOYEEVLIC.ADDR_ID", "ADDRESSVLIC.ADDRESS_ID");
        descriptor.addMapping(addressMapping);

        OneToManyMapping phoneNumbersMapping = new OneToManyMapping();
        phoneNumbersMapping.setAttributeName("phoneNumbers");
        phoneNumbersMapping.setReferenceClass(org.eclipse.persistence.testing.models.forceupdate.PhoneNumberVLIC.class);
        phoneNumbersMapping.useBasicIndirection();
        phoneNumbersMapping.addTargetForeignKeyFieldName("PHONEVLIC.EMP_ID", "EMPLOYEEVLIC.EMP_ID");
        descriptor.addMapping(phoneNumbersMapping);

        return descriptor;
    }

    public RelationalDescriptor buildEmployeeVLIODescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.forceupdate.EmployeeVLIO.class);
        descriptor.addTableName("EMPLOYEEVLIO");
        descriptor.addPrimaryKeyFieldName("EMPLOYEEVLIO.EMP_ID");

        // RelationalDescriptor properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);
        descriptor.setSequenceNumberFieldName("EMPLOYEEVLIO.EMP_ID");
        descriptor.setSequenceNumberName("EMPVLIO_SEQ");
        descriptor.useVersionLocking("VERSION", false);

        // Query manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        //Named Queries

        // Event manager.

        // Mappings.
        DirectToFieldMapping idMapping = new DirectToFieldMapping();
        idMapping.setAttributeName("id");
        idMapping.setFieldName("EMPLOYEEVLIO.EMP_ID");
        descriptor.addMapping(idMapping);

        DirectToFieldMapping firstNameMapping = new DirectToFieldMapping();
        firstNameMapping.setAttributeName("firstName");
        firstNameMapping.setFieldName("EMPLOYEEVLIO.F_NAME");
        descriptor.addMapping(firstNameMapping);

        DirectToFieldMapping lastNameMapping = new DirectToFieldMapping();
        lastNameMapping.setAttributeName("lastName");
        lastNameMapping.setFieldName("EMPLOYEEVLIO.L_NAME");
        descriptor.addMapping(lastNameMapping);

        DirectToFieldMapping genderMapping = new DirectToFieldMapping();
        genderMapping.setAttributeName("gender");
        genderMapping.setFieldName("EMPLOYEEVLIO.GENDER");
        descriptor.addMapping(genderMapping);

        DirectToFieldMapping versionMapping = new DirectToFieldMapping();
        versionMapping.setAttributeName("version");
        versionMapping.setFieldName("EMPLOYEEVLIO.VERSION");
        descriptor.addMapping(versionMapping);

        DirectToFieldMapping salaryMapping = new DirectToFieldMapping();
        salaryMapping.setAttributeName("salary");
        salaryMapping.setFieldName("EMPLOYEEVLIO.SALARY");
        descriptor.addMapping(salaryMapping);

        OneToOneMapping addressMapping = new OneToOneMapping();
        addressMapping.setAttributeName("address");
        addressMapping.setReferenceClass(org.eclipse.persistence.testing.models.forceupdate.AddressVLIO.class);
        addressMapping.useBasicIndirection();
        addressMapping.addForeignKeyFieldName("EMPLOYEEVLIO.ADDR_ID", "ADDRESSVLIO.ADDRESS_ID");
        descriptor.addMapping(addressMapping);

        OneToManyMapping phoneNumbersMapping = new OneToManyMapping();
        phoneNumbersMapping.setAttributeName("phoneNumbers");
        phoneNumbersMapping.setReferenceClass(org.eclipse.persistence.testing.models.forceupdate.PhoneNumberVLIO.class);
        phoneNumbersMapping.useBasicIndirection();
        phoneNumbersMapping.addTargetForeignKeyFieldName("PHONEVLIO.EMP_ID", "EMPLOYEEVLIO.EMP_ID");
        descriptor.addMapping(phoneNumbersMapping);

        return descriptor;
    }

    public RelationalDescriptor buildPhoneNumberTLICDescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.forceupdate.PhoneNumberTLIC.class);
        descriptor.addTableName("PHONETLIC");
        descriptor.addPrimaryKeyFieldName("PHONETLIC.EMP_ID");
        descriptor.addPrimaryKeyFieldName("PHONETLIC.TYPE");

        // RelationalDescriptor properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);

        // Query manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        //Named Queries

        // Event manager.

        // Mappings.
        DirectToFieldMapping areaCodeMapping = new DirectToFieldMapping();
        areaCodeMapping.setAttributeName("areaCode");
        areaCodeMapping.setFieldName("PHONETLIC.AREA_CODE");
        descriptor.addMapping(areaCodeMapping);

        DirectToFieldMapping numberMapping = new DirectToFieldMapping();
        numberMapping.setAttributeName("number");
        numberMapping.setFieldName("PHONETLIC.P_NUMBER");
        descriptor.addMapping(numberMapping);

        DirectToFieldMapping typeMapping = new DirectToFieldMapping();
        typeMapping.setAttributeName("type");
        typeMapping.setFieldName("PHONETLIC.TYPE");
        descriptor.addMapping(typeMapping);

        OneToOneMapping ownerMapping = new OneToOneMapping();
        ownerMapping.setAttributeName("owner");
        ownerMapping.setReferenceClass(org.eclipse.persistence.testing.models.forceupdate.EmployeeTLIC.class);
        ownerMapping.useBasicIndirection();
        ownerMapping.addForeignKeyFieldName("PHONETLIC.EMP_ID", "EMPLOYEETLIC.EMP_ID");
        descriptor.addMapping(ownerMapping);

        return descriptor;
    }

    public RelationalDescriptor buildPhoneNumberTLIODescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.forceupdate.PhoneNumberTLIO.class);
        descriptor.addTableName("PHONETLIO");
        descriptor.addPrimaryKeyFieldName("PHONETLIO.EMP_ID");
        descriptor.addPrimaryKeyFieldName("PHONETLIO.TYPE");

        // RelationalDescriptor properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);

        // Query manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        //Named Queries

        // Event manager.

        // Mappings.
        DirectToFieldMapping typeMapping = new DirectToFieldMapping();
        typeMapping.setAttributeName("type");
        typeMapping.setFieldName("PHONETLIO.TYPE");
        descriptor.addMapping(typeMapping);

        DirectToFieldMapping areaCodeMapping = new DirectToFieldMapping();
        areaCodeMapping.setAttributeName("areaCode");
        areaCodeMapping.setFieldName("PHONETLIO.AREA_CODE");
        descriptor.addMapping(areaCodeMapping);

        DirectToFieldMapping numberMapping = new DirectToFieldMapping();
        numberMapping.setAttributeName("number");
        numberMapping.setFieldName("PHONETLIO.P_NUMBER");
        descriptor.addMapping(numberMapping);

        OneToOneMapping ownerMapping = new OneToOneMapping();
        ownerMapping.setAttributeName("owner");
        ownerMapping.setReferenceClass(org.eclipse.persistence.testing.models.forceupdate.EmployeeTLIO.class);
        ownerMapping.useBasicIndirection();
        ownerMapping.addForeignKeyFieldName("PHONETLIO.EMP_ID", "EMPLOYEETLIO.EMP_ID");
        descriptor.addMapping(ownerMapping);

        return descriptor;
    }

    public RelationalDescriptor buildPhoneNumberVLICDescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.forceupdate.PhoneNumberVLIC.class);
        descriptor.addTableName("PHONEVLIC");
        descriptor.addPrimaryKeyFieldName("PHONEVLIC.EMP_ID");
        descriptor.addPrimaryKeyFieldName("PHONEVLIC.TYPE");

        // RelationalDescriptor properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);

        // Query manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        //Named Queries

        // Event manager.

        // Mappings.
        DirectToFieldMapping typeMapping = new DirectToFieldMapping();
        typeMapping.setAttributeName("type");
        typeMapping.setFieldName("PHONEVLIC.TYPE");
        descriptor.addMapping(typeMapping);

        DirectToFieldMapping areaCodeMapping = new DirectToFieldMapping();
        areaCodeMapping.setAttributeName("areaCode");
        areaCodeMapping.setFieldName("PHONEVLIC.AREA_CODE");
        descriptor.addMapping(areaCodeMapping);

        DirectToFieldMapping numberMapping = new DirectToFieldMapping();
        numberMapping.setAttributeName("number");
        numberMapping.setFieldName("PHONEVLIC.P_NUMBER");
        descriptor.addMapping(numberMapping);

        OneToOneMapping ownerMapping = new OneToOneMapping();
        ownerMapping.setAttributeName("owner");
        ownerMapping.setReferenceClass(org.eclipse.persistence.testing.models.forceupdate.EmployeeVLIC.class);
        ownerMapping.useBasicIndirection();
        ownerMapping.addForeignKeyFieldName("PHONEVLIC.EMP_ID", "EMPLOYEEVLIC.EMP_ID");
        descriptor.addMapping(ownerMapping);

        return descriptor;
    }

    public RelationalDescriptor buildPhoneNumberVLIODescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.forceupdate.PhoneNumberVLIO.class);
        descriptor.addTableName("PHONEVLIO");
        descriptor.addPrimaryKeyFieldName("PHONEVLIO.EMP_ID");
        descriptor.addPrimaryKeyFieldName("PHONEVLIO.TYPE");

        // RelationalDescriptor properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);

        // Query manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        //Named Queries

        // Event manager.

        // Mappings.
        DirectToFieldMapping typeMapping = new DirectToFieldMapping();
        typeMapping.setAttributeName("type");
        typeMapping.setFieldName("PHONEVLIO.TYPE");
        descriptor.addMapping(typeMapping);

        DirectToFieldMapping areaCodeMapping = new DirectToFieldMapping();
        areaCodeMapping.setAttributeName("areaCode");
        areaCodeMapping.setFieldName("PHONEVLIO.AREA_CODE");
        descriptor.addMapping(areaCodeMapping);

        DirectToFieldMapping numberMapping = new DirectToFieldMapping();
        numberMapping.setAttributeName("number");
        numberMapping.setFieldName("PHONEVLIO.P_NUMBER");
        descriptor.addMapping(numberMapping);

        OneToOneMapping ownerMapping = new OneToOneMapping();
        ownerMapping.setAttributeName("owner");
        ownerMapping.setReferenceClass(org.eclipse.persistence.testing.models.forceupdate.EmployeeVLIO.class);
        ownerMapping.useBasicIndirection();
        ownerMapping.addForeignKeyFieldName("PHONEVLIO.EMP_ID", "EMPLOYEEVLIO.EMP_ID");
        descriptor.addMapping(ownerMapping);

        return descriptor;
    }

}
