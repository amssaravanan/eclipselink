/*******************************************************************************
 * Copyright (c) 1998, 2014 Oracle and/or its affiliates. All rights reserved.
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
package org.eclipse.persistence.jaxb.compiler;

import java.awt.Image;
import java.beans.Introspector;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.eclipse.persistence.exceptions.BeanValidationException;
import org.eclipse.persistence.exceptions.JAXBException;
import org.eclipse.persistence.internal.core.helper.CoreClassConstants;
import org.eclipse.persistence.internal.jaxb.many.MapValue;
import org.eclipse.persistence.internal.oxm.Constants;
import org.eclipse.persistence.internal.oxm.Namespace;
import org.eclipse.persistence.internal.oxm.XPathFragment;
import org.eclipse.persistence.internal.oxm.mappings.Field;
import org.eclipse.persistence.internal.oxm.schema.model.All;
import org.eclipse.persistence.internal.oxm.schema.model.Any;
import org.eclipse.persistence.internal.oxm.schema.model.AnyAttribute;
import org.eclipse.persistence.internal.oxm.schema.model.Attribute;
import org.eclipse.persistence.internal.oxm.schema.model.Choice;
import org.eclipse.persistence.internal.oxm.schema.model.ComplexContent;
import org.eclipse.persistence.internal.oxm.schema.model.ComplexType;
import org.eclipse.persistence.internal.oxm.schema.model.Element;
import org.eclipse.persistence.internal.oxm.schema.model.Extension;
import org.eclipse.persistence.internal.oxm.schema.model.Import;
import org.eclipse.persistence.internal.oxm.schema.model.Occurs;
import org.eclipse.persistence.internal.oxm.schema.model.Restriction;
import org.eclipse.persistence.internal.oxm.schema.model.Schema;
import org.eclipse.persistence.internal.oxm.schema.model.Sequence;
import org.eclipse.persistence.internal.oxm.schema.model.SimpleComponent;
import org.eclipse.persistence.internal.oxm.schema.model.SimpleContent;
import org.eclipse.persistence.internal.oxm.schema.model.SimpleType;
import org.eclipse.persistence.internal.oxm.schema.model.TypeDefParticle;
import org.eclipse.persistence.internal.oxm.schema.model.TypeDefParticleOwner;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.jaxb.compiler.facets.DecimalMaxFacet;
import org.eclipse.persistence.jaxb.compiler.facets.DecimalMinFacet;
import org.eclipse.persistence.jaxb.compiler.facets.DigitsFacet;
import org.eclipse.persistence.jaxb.compiler.facets.Facet;
import org.eclipse.persistence.jaxb.compiler.facets.FacetVisitor;
import org.eclipse.persistence.jaxb.compiler.facets.MaxFacet;
import org.eclipse.persistence.jaxb.compiler.facets.MinFacet;
import org.eclipse.persistence.jaxb.compiler.facets.PatternFacet;
import org.eclipse.persistence.jaxb.compiler.facets.PatternListFacet;
import org.eclipse.persistence.jaxb.compiler.facets.SizeFacet;
import org.eclipse.persistence.jaxb.javamodel.Helper;
import org.eclipse.persistence.jaxb.javamodel.JavaClass;
import org.eclipse.persistence.jaxb.javamodel.JavaMethod;
import org.eclipse.persistence.jaxb.xmlmodel.XmlElementWrapper;
import org.eclipse.persistence.jaxb.xmlmodel.XmlJoinNodes.XmlJoinNode;
import org.eclipse.persistence.jaxb.xmlmodel.XmlTransformation.XmlWriteTransformer;
import org.eclipse.persistence.jaxb.xmlmodel.XmlVirtualAccessMethodsSchema;
import org.eclipse.persistence.oxm.NamespaceResolver;
import org.eclipse.persistence.oxm.XMLField;
import org.eclipse.persistence.sessions.Session;

/**
 * INTERNAL:
 * <p><b>Purpose:</b>To generate Schema objects based on a map of TypeInfo objects, and some
 * additional information gathered by the AnnotationsProcessing phase.
 * <p><b>Responsibilities:</b><ul>
 * <li>Create and maintain a collection of Schema objects based on the provided TypeInfo objects</li>
 * <li>Add additional global elements to the schema based on an optional map (for WS integration)</li>
 * <li>Should create a schema for each namespace encountered during generation.</li>
 * </ul>
 * <p>This class is used by the Generator to handle the generation of Schemas. The
 * Generator passes in a map of TypeInfo objects, generated by the Annotations processor.
 * The generated Schemas are stored in a map of keyed on Target Namespace.
 * @see org.eclipse.persistence.jaxb.compiler.TypeInfo
 * @see org.eclipse.persistence.jaxb.compiler.AnnotationsProcessor
 * @see org.eclipse.persistence.jaxb.compiler.Generator
 * @since Oracle TopLink 11.1.1.0.0
 * @author mmacivor
 */
public class SchemaGenerator {
    private Map<String, Schema> schemaForNamespace;
    private List<Schema> allSchemas;
    private int schemaCount;
    private Helper helper;
    private Map<String, TypeInfo> typeInfo;
    private Map<String, PackageInfo> packageToPackageInfoMappings;
    private Map<String, SchemaTypeInfo> schemaTypeInfo;
    private Map<String, QName> userDefinedSchemaTypes;
    private Map<String, Class> arrayClassesToGeneratedClasses;

    private static final String JAVAX_ACTIVATION_DATAHANDLER = "javax.activation.DataHandler";
    private static final String JAVAX_MAIL_INTERNET_MIMEMULTIPART = "javax.mail.internet.MimeMultipart";
    private static final String SWA_REF_IMPORT = "http://ws-i.org/profiles/basic/1.1/swaref.xsd";
    private static final String BUILD_FIELD_VALUE_METHOD = "buildFieldValue";

    private static final String COLON = ":";
    private static final String ATT = "@";
    private static final String EMPTY_STRING = "";
    private static final String DOT = ".";
    private static final String SKIP = "skip";
    private static final String ENTRY = "entry";
    private static final String GENERATE = "##generate";
    private static final String SCHEMA = "schema";
    private static final String SCHEMA_EXT = ".xsd";
    private static final String OBJECT_CLASSNAME = "java.lang.Object";
    private static final String ID = "ID";
    private static final String IDREF = "IDREF";
    private static final Character DOT_CHAR = '.';
    private static final Character SLASH = '/';
    private static final Character SLASHES = '\\';

    private SchemaOutputResolver outputResolver;
    private boolean facets;

    public SchemaGenerator(Helper helper) {
        this.helper = helper;
        this.facets = helper.isFacets();
    }

    public void generateSchema(List<JavaClass> typeInfoClasses, Map<String, TypeInfo> typeInfo, Map<String, QName> userDefinedSchemaTypes, Map<String, PackageInfo> packageToPackageInfoMappings, Map<QName, ElementDeclaration> additionalGlobalElements, Map<String, Class> arrayClassesToGeneratedClasses, SchemaOutputResolver outputResolver) {
        this.outputResolver = outputResolver;
        generateSchema(typeInfoClasses, typeInfo, userDefinedSchemaTypes, packageToPackageInfoMappings, additionalGlobalElements, arrayClassesToGeneratedClasses);
    }

    public void generateSchema(List<JavaClass> typeInfoClasses, Map<String, TypeInfo> typeInfo, Map<String, QName> userDefinedSchemaTypes, Map<String, PackageInfo> packageToPackageInfoMappings, Map<QName, ElementDeclaration> additionalGlobalElements, Map<String, Class> arrayClassesToGeneratedClasses) {
        this.typeInfo = typeInfo;
        this.userDefinedSchemaTypes = userDefinedSchemaTypes;
        this.packageToPackageInfoMappings = packageToPackageInfoMappings;
        this.schemaCount = 1;
        this.schemaTypeInfo = new HashMap<String, SchemaTypeInfo>(typeInfo.size());
        this.arrayClassesToGeneratedClasses = arrayClassesToGeneratedClasses;

        for (JavaClass javaClass : typeInfoClasses) {
            addSchemaComponents(javaClass);
        }
        populateSchemaTypes();
        if (additionalGlobalElements != null) {
            addGlobalElements(additionalGlobalElements);
        }
    }

    public void addSchemaComponents(JavaClass myClass) {
        // first check for type
        String myClassName = myClass.getQualifiedName();
        Element rootElement = null;
        TypeInfo info = typeInfo.get(myClassName);
        if (info.isTransient() || info.getClassNamespace().equals(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI)) {
            return;
        }
        SchemaTypeInfo schemaTypeInfo = new SchemaTypeInfo();
        schemaTypeInfo.setSchemaTypeName(new QName(info.getClassNamespace(), info.getSchemaTypeName()));
        this.schemaTypeInfo.put(myClass.getQualifiedName(), schemaTypeInfo);
        NamespaceInfo namespaceInfo = this.packageToPackageInfoMappings.get(myClass.getPackageName()).getNamespaceInfo();
        if (namespaceInfo.getLocation() != null && !namespaceInfo.getLocation().equals(GENERATE)) {
            return;
        }
        Schema schema = getSchemaForNamespace(info.getClassNamespace(), myClass.getPackageName());
        info.setSchema(schema);

        String typeName = info.getSchemaTypeName();
        String pfx = EMPTY_STRING;

        Property valueField = null;
        if (info.isSetXmlRootElement()) {
            //Create the root element and add it to the schema
            org.eclipse.persistence.jaxb.xmlmodel.XmlRootElement xmlRE = info.getXmlRootElement();
            rootElement = new Element();
            String elementName = xmlRE.getName();
            if (elementName.equals(XMLProcessor.DEFAULT) || elementName.equals(EMPTY_STRING)) {
                try{
                    elementName = info.getXmlNameTransformer().transformRootElementName(myClassName);
                }catch (Exception ex){
                    throw org.eclipse.persistence.exceptions.JAXBException.exceptionDuringNameTransformation(myClassName, info.getXmlNameTransformer().getClass().getName(), ex);
                }
            }
            rootElement.setName(elementName);
            String rootNamespace = xmlRE.getNamespace();
            if (rootNamespace.equals(XMLProcessor.DEFAULT)) {
                Schema rootElementSchema = getSchemaForNamespace(namespaceInfo.getNamespace());
                if (rootElementSchema != null) {
                    rootElementSchema.addTopLevelElement(rootElement);
                }
                schemaTypeInfo.getGlobalElementDeclarations().add(new QName(namespaceInfo.getNamespace(), rootNamespace));
                rootNamespace = namespaceInfo.getNamespace();
            } else {
                Schema rootElementSchema = getSchemaForNamespace(rootNamespace);
                if (rootElementSchema != null) {
                    rootElementSchema.addTopLevelElement(rootElement);
                }
                schemaTypeInfo.getGlobalElementDeclarations().add(new QName(rootNamespace, elementName));
            }

            // handle root-level imports/includes [schema = the type's schema]
            Schema rootSchema = getSchemaForNamespace(rootNamespace);
            addImportIfRequired(rootSchema, schema, schema.getTargetNamespace());

            // setup a prefix, if necessary
            if (rootSchema != null && !info.getClassNamespace().equals(EMPTY_STRING)) {
                pfx = getOrGeneratePrefixForNamespace(info.getClassNamespace(), rootSchema);
                pfx += COLON;
            }
        }



        if (CompilerHelper.isSimpleType(info)){

            SimpleType type = new SimpleType();
            //simple type case, we just need the name and namespace info
            if (typeName.equals(EMPTY_STRING)) {
                //In this case, it should be a type under
                //A root elem or locally defined whenever used
                if (rootElement != null) {
                    rootElement.setSimpleType(type);
                }
            } else {
                type.setName(typeName);
                schema.addTopLevelSimpleTypes(type);
                if (rootElement != null) {
                    rootElement.setType(pfx + type.getName());
                }
            }
            //Figure out schema type and set it as Restriction
            QName restrictionType = null;
            Restriction restriction = new Restriction();
            if (info.isEnumerationType()) {
                restrictionType = ((EnumTypeInfo) info).getRestrictionBase();
                restriction.setEnumerationFacets(this.getEnumerationFacetsFor((EnumTypeInfo) info));

                String prefix = null;
                if (restrictionType.getNamespaceURI() != null && !EMPTY_STRING.equals(restrictionType.getNamespaceURI())) {
                    if (javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(restrictionType.getNamespaceURI())) {
                        prefix = Constants.SCHEMA_PREFIX;
                    } else {
                        prefix = getPrefixForNamespace(schema, restrictionType.getNamespaceURI());
                    }
                }
                String extensionTypeName = restrictionType.getLocalPart();
                if (prefix != null) {
                    extensionTypeName = prefix + COLON + extensionTypeName;
                }
                restriction.setBaseType(extensionTypeName);

                type.setRestriction(restriction);
            } else {
                valueField= info.getXmlValueProperty();
                JavaClass javaType = valueField.getActualType();
                QName baseType = getSchemaTypeFor(javaType);
                String prefix = null;
                if (baseType.getNamespaceURI() != null && !baseType.getNamespaceURI().equals(EMPTY_STRING)) {
                    if (baseType.getNamespaceURI().equals(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI)) {
                        prefix = Constants.SCHEMA_PREFIX;
                    } else {
                        prefix = getPrefixForNamespace(schema, baseType.getNamespaceURI());
                    }
                }
                String baseTypeName = baseType.getLocalPart();
                if (prefix != null) {
                    baseTypeName = prefix + COLON + baseTypeName;
                }
                if (valueField.isXmlList() || (valueField.getGenericType() != null)) {
                    //generate a list instead of a restriction
                    org.eclipse.persistence.internal.oxm.schema.model.List list = new org.eclipse.persistence.internal.oxm.schema.model.List();
                    list.setItemType(baseTypeName);
                    type.setList(list);
                } else {
                    if (helper.isAnnotationPresent(valueField.getElement(), XmlSchemaType.class)) {
                        XmlSchemaType schemaType = (XmlSchemaType) helper.getAnnotation(valueField.getElement(), XmlSchemaType.class);
                        baseType = new QName(schemaType.namespace(), schemaType.name()); // TODO: This assignment seems like a bug, probably this should be "baseTypeName" ?
                    }
                    restriction.setBaseType(baseTypeName);
                    type.setRestriction(restriction);
                }
            }
            info.setSimpleType(type);
        } else if ((valueField = this.getXmlValueFieldForSimpleContent(info)) != null) {
            ComplexType type = new ComplexType();
            SimpleContent content = new SimpleContent();
            if (EMPTY_STRING.equals(typeName)) {
                if (rootElement != null) {
                    rootElement.setComplexType(type);
                }
                info.setComplexType(type);
            } else {
                type.setName(typeName);
                schema.addTopLevelComplexTypes(type);
                if (rootElement != null) {
                    rootElement.setType(pfx + type.getName());
                }
            }
            QName extensionType = getSchemaTypeFor(valueField.getType());
            if (helper.isAnnotationPresent(valueField.getElement(), XmlSchemaType.class)) {
                XmlSchemaType schemaType = (XmlSchemaType) helper.getAnnotation(valueField.getElement(), XmlSchemaType.class);
                extensionType = new QName(schemaType.namespace(), schemaType.name());
            }
            String prefix = null;
            if (extensionType.getNamespaceURI() != null && !extensionType.getNamespaceURI().equals(EMPTY_STRING)) {
                if (extensionType.getNamespaceURI().equals(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI)) {
                    prefix = Constants.SCHEMA_PREFIX;
                } else {
                    prefix = getPrefixForNamespace(schema, extensionType.getNamespaceURI());
                }
            }
            String extensionTypeName = extensionType.getLocalPart();
            if (prefix != null) {
                extensionTypeName = prefix + COLON + extensionTypeName;
            }
            Extension extension = new Extension();
            extension.setBaseType(extensionTypeName);
            content.setExtension(extension);
            type.setSimpleContent(content);
            info.setComplexType(type);
        } else {
            ComplexType type = createComplexTypeForClass(myClass, info);
            TypeDefParticle compositor = null;
            if(type.getComplexContent() != null && type.getComplexContent().getExtension() != null) {
                compositor = type.getComplexContent().getExtension().getTypeDefParticle();
            } else {
                compositor = type.getTypeDefParticle();
            }
            if (EMPTY_STRING.equals(typeName)) {
                if (rootElement != null) {
                    rootElement.setComplexType(type);
                }
                info.setComplexType(type);
                info.setCompositor(compositor);
            } else {
                type.setName(typeName);
                if (rootElement != null) {
                    rootElement.setType(pfx + type.getName());
                }
                schema.addTopLevelComplexTypes(type);
                info.setComplexType(type);
                info.setCompositor(compositor);
            }
        }
    }

    private ComplexType createComplexTypeForClass(JavaClass myClass, TypeInfo info) {

        Schema schema = getSchemaForNamespace(info.getClassNamespace());

        ComplexType type = new ComplexType();
        JavaClass superClass = CompilerHelper.getNextMappedSuperClass(myClass, this.typeInfo, this.helper);
        // Handle abstract class
        if (myClass.isAbstract()) {
            type.setAbstractValue(true);
        }

        Extension extension = null;
        if (superClass != null) {
            TypeInfo parentTypeInfo = this.typeInfo.get(superClass.getQualifiedName());
            if (parentTypeInfo != null) {
                extension = new Extension();
                // may need to qualify the type
                String parentPrefix = getPrefixForNamespace(schema, parentTypeInfo.getClassNamespace());
                if (parentPrefix != null) {
                    extension.setBaseType(parentPrefix + COLON + parentTypeInfo.getSchemaTypeName());
                } else {
                    extension.setBaseType(parentTypeInfo.getSchemaTypeName());
                }

                if(CompilerHelper.isSimpleType(parentTypeInfo)){
                    SimpleContent content = new SimpleContent();
                    content.setExtension(extension);
                    type.setSimpleContent(content);
                    return type;
                }else{
                    ComplexContent content = new ComplexContent();
                    content.setExtension(extension);
                    type.setComplexContent(content);
                }

            }
        }

        TypeDefParticle compositor = null;
        String[] propOrder = null;
        if (info.isSetPropOrder()) {
            propOrder = info.getPropOrder();
        }

        if (propOrder != null && propOrder.length == 0) {
            // Note that the spec requires an 'all' to be generated
            // in cases where propOrder == 0, however, the TCK
            // requires the extension case to use sequences
            if (info.hasElementRefs()) {
                // generate a sequence to satisfy TCK
                compositor = new Sequence();
                if (extension != null) {
                    extension.setSequence((Sequence) compositor);
                } else {
                    type.setSequence((Sequence) compositor);
                }
            } else if (extension != null) {
                compositor = new All();
                extension.setAll((All) compositor);
            } else {
                compositor = new All();
                type.setAll((All) compositor);
            }
        } else {
            // generate a sequence to satisfy TCK
            compositor = new Sequence();
            if (extension != null) {
                extension.setSequence((Sequence) compositor);
            } else {
                type.setSequence((Sequence) compositor);
            }
        }
        return type;
    }
    public void addToSchemaType(TypeInfo ownerTypeInfo, java.util.List<Property> properties, TypeDefParticle compositor, ComplexType type, Schema workingSchema) {
        //If there are no properties we don't want a sequence/choice or all tag written out
        if (properties.size() == 0) {
            type.setAll(null);
            type.setSequence(null);
            type.setChoice(null);
            ownerTypeInfo.setCompositor(null);
            return;
        }

        boolean extAnyAdded = false;

        // generate schema components for each property
        for (Property next : properties) {
            if (next == null) { continue; }
            Schema currentSchema = workingSchema;
            TypeDefParticle parentCompositor = compositor;
            boolean isChoice = (parentCompositor instanceof Choice);
            ComplexType parentType = type;
            // ignore transient and inverse reference/non-writeable properties
            if(!next.isTransient() && !(next.isInverseReference() && !next.isWriteableInverseReference())){
                // handle xml extensions
                if (next.isVirtual()) {
                    boolean extSchemaAny = false;
                    if (ownerTypeInfo.getXmlVirtualAccessMethods().getSchema() != null) {
                        extSchemaAny = ownerTypeInfo.getXmlVirtualAccessMethods().getSchema().equals(XmlVirtualAccessMethodsSchema.ANY);
                    }
                    if (extSchemaAny && !next.isAttribute()) {
                        if (!extAnyAdded) {
                            addAnyToSchema(next, compositor, true, Constants.ANY_NAMESPACE_ANY);
                            extAnyAdded = true;
                            continue;
                        } else {
                            // any already added
                            continue;
                        }
                    } else {
                        // proceed, adding the schema element as usual
                    }
                }
                // handle transformers
                if (next.isSetXmlTransformation() && next.getXmlTransformation().isSetXmlWriteTransformers()) {
                    addTransformerToSchema(next, ownerTypeInfo, compositor, type, workingSchema);
                    continue;
                }
                // handle XmlJoinNodes
                if (next.isSetXmlJoinNodes()) {
                    addXmlJoinNodesToSchema(next, parentCompositor, currentSchema, parentType);
                    continue;
                }
                // deal with xml-path case
                if (next.getXmlPath() != null) {
                    // create schema components based on the XmlPath
                    AddToSchemaResult xpr = addXPathToSchema(next, parentCompositor, currentSchema, isChoice, type);
                    // if the returned object or schema component is null there is nothing to do
                    if (xpr == null || ((parentCompositor = xpr.particle) == null && xpr.simpleContentType == null)) {
                        continue;
                    }
                    // now process the property as per usual, adding to the created schema component
                    if(xpr.schema == null) {
                        //if there's no schema, this may be a ref to an attribute in an ungenerated schema
                        //no need to generate the attribute in the target schema
                        continue;
                    }
                    currentSchema = xpr.schema;
                    if(parentCompositor == null) {
                        parentType = xpr.simpleContentType;
                    } else if (parentCompositor.getOwner() instanceof ComplexType) {
                        parentType = ((ComplexType)parentCompositor.getOwner());
                    }
                    // deal with the XmlElementWrapper case
                } else if (!isChoice && !next.isMap() && next.isSetXmlElementWrapper()) {
                    AddToSchemaResult asr = addXmlElementWrapperToSchema(next, currentSchema, compositor);
                    // if returned object is null there is nothing to do
                    if (asr == null) {
                        continue;
                    }
                    // the returned object contains ComplexType and TypeDefParticles to use
                    parentType = asr.type;
                    parentCompositor = asr.particle;
                }
                // handle mixed content
                if (next.isMixedContent()) {
                    parentType.setMixed(true);
                }
                // handle attribute
                if (next.isAttribute() && !next.isAnyAttribute()) {
                    addAttributeToSchema(buildAttribute(next, currentSchema), next.getSchemaName(), currentSchema, parentType);
                    // handle any attribute
                } else if (next.isAnyAttribute()) {
                    addAnyAttributeToSchema(parentType);
                    // handle choice
                } else if (next.isChoice()) {
                    addChoiceToSchema(next, ownerTypeInfo, parentType, parentCompositor, currentSchema);
                    // handle reference
                } else if (next.isReference()) {
                    addReferenceToSchema(next, currentSchema, parentCompositor);
                    // handle any
                } else if (next.isAny() || next.getVariableAttributeName() !=null) {
                    addAnyToSchema(next, parentCompositor);
                    // add an element
                } else if (!(ownerTypeInfo.getXmlValueProperty() != null && ownerTypeInfo.getXmlValueProperty() == next)) {
                    Element element = buildElement(next, parentCompositor instanceof All, currentSchema, ownerTypeInfo);
                    addElementToSchema(element, next.getSchemaName().getNamespaceURI(), next.isPositional(), parentCompositor, currentSchema, ownerTypeInfo.getJavaClass().getPackageName());
                }
            }
        }
    }

    /**
     * Return the schema type (as QName) based on a given JavaClass.
     *
     * @param javaClass
     * @return
     */
    public QName getSchemaTypeFor(JavaClass javaClass) {
        String className;
        if (javaClass.isArray()) {
            Class wrapperClass = arrayClassesToGeneratedClasses.get(javaClass.getName());
            if (null == wrapperClass) {
                className = javaClass.getQualifiedName();
            } else {
                className = wrapperClass.getName();
            }
        } else {
            className = javaClass.getQualifiedName();
        }
        // check user defined types first
        QName schemaType = (QName) userDefinedSchemaTypes.get(className);
        if (schemaType == null) {
            schemaType = (QName) helper.getXMLToJavaTypeMap().get(javaClass.getRawName());
        }
        if (schemaType == null) {
            TypeInfo targetInfo = this.typeInfo.get(className);
            if (targetInfo != null) {
                schemaType = new QName(targetInfo.getClassNamespace(), targetInfo.getSchemaTypeName());
            }
        }
        if (schemaType == null) {
            if (javaClass.getQualifiedName().equals(OBJECT_CLASSNAME)) {
                return Constants.ANY_TYPE_QNAME;
            }
            return Constants.ANY_SIMPLE_TYPE_QNAME;
        }
        return schemaType;
    }

    public void populateSchemaTypes() {
        for (String javaClassName : typeInfo.keySet()) {
            TypeInfo info = typeInfo.get(javaClassName);
            if (info.isComplexType()) {
                if (info.getSchema() != null) {
                    List<Property> props = info.getNonTransientPropertiesInPropOrder();
                    // handle class indicator field name
                    if (info.isSetXmlDiscriminatorNode()) {
                        String xpath = info.getXmlDiscriminatorNode();
                        String pname = XMLProcessor.getNameFromXPath(xpath, EMPTY_STRING, true);
                        if (!pname.equals(EMPTY_STRING)) {
                            // since there is no property for the indicator field name, we'll need to make one
                            Property prop = new Property(helper);
                            prop.setPropertyName(pname);
                            prop.setXmlPath(xpath);
                            prop.setSchemaName(new QName(pname));
                            prop.setType(helper.getJavaClass(String.class));
                            prop.setIsAttribute(true);
                            props.add(prop);
                        }
                    }
                    addToSchemaType(info, props, info.getCompositor(), info.getComplexType(), info.getSchema());
                    if (info.hasPredicateProperties()) {
                        addToSchemaType(info, info.getPredicateProperties(), info.getCompositor(), info.getComplexType(), info.getSchema());
                    }
                }
            }
        }
    }

    public String getSchemaTypeNameForClassName(String className) {
        return Introspector.decapitalize(className.substring(className.lastIndexOf(DOT_CHAR) + 1));
    }

    public ArrayList<Object> getEnumerationFacetsFor(EnumTypeInfo info) {
        return (ArrayList<Object>) info.getXmlEnumValues();
    }

    public Property getXmlValueFieldForSimpleContent(TypeInfo info) {
        ArrayList<Property> properties = info.getPropertyList();
        Property xmlValueProperty = info.getXmlValueProperty();
        boolean foundValue = false;
        boolean foundNonAttribute = false;
        Property valueField = null;

        for (Property prop : properties) {
            if (xmlValueProperty != null && xmlValueProperty == prop) {
                foundValue = true;
                valueField = prop;
            } else if (!prop.isAttribute() && !prop.isTransient() && !prop.isAnyAttribute()) {
                foundNonAttribute = true;
            }
        }
        if (foundValue && !foundNonAttribute) {
            return valueField;
        }
        return null;
    }

    /**
     * Indicates if a given Property is a collection type.
     *
     * @param field
     * @return
     */
    public boolean isCollectionType(Property field) {
        JavaClass type = field.getType();
        return (helper.getJavaClass(java.util.Collection.class).isAssignableFrom(type)
                || helper.getJavaClass(java.util.List.class).isAssignableFrom(type)
                || helper.getJavaClass(java.util.Set.class).isAssignableFrom(type));
    }

    private Schema getSchemaForNamespace(String namespace) {
        return getSchemaForNamespace(namespace, null);
    }

    /**
     * Return the Schema for a given namespace.  If no schema exists for the
     * given namespace, one will be created.
     *
     * @param namespace
     * @return
     */
    private Schema getSchemaForNamespace(String namespace, String packageName) {
        if (schemaForNamespace == null) {
            schemaForNamespace = new HashMap<String, Schema>();
            allSchemas = new ArrayList<Schema>();
        }
        Schema schema = schemaForNamespace.get(namespace);
        if (schema == null && !javax.xml.XMLConstants.XML_NS_URI.equals(namespace)) {
            schema = new Schema();
            String schemaName = SCHEMA + schemaCount + SCHEMA_EXT;

            NamespaceInfo namespaceInfo = getNamespaceInfoForNamespace(namespace, packageName);
            if (namespaceInfo != null) {
                if (namespaceInfo.getLocation() != null && !namespaceInfo.getLocation().equals(GENERATE)) {
                    return null;
                }
                java.util.Vector namespaces = namespaceInfo.getNamespaceResolver().getNamespaces();
                for (int i = 0; i < namespaces.size(); i++) {
                    Namespace nextNamespace = (Namespace) namespaces.get(i);
                    schema.getNamespaceResolver().put(nextNamespace.getPrefix(), nextNamespace.getNamespaceURI());
                }
            }

            if (outputResolver != null) {
                try {
                    Result res = outputResolver.createOutput(namespace, schemaName);
                    // if the resolver returns null, schema generation for this namespace URI will be skipped
                    if (res == null) {
                        return null;
                    }
                    schema.setResult(res);
                    if (res.getSystemId() != null) {
                        schemaName = res.getSystemId();
                        // may use schema name to create a URI, which expects '/' as file separator
                        schemaName = schemaName.replace(SLASHES, SLASH);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            schema.setName(schemaName);
            schemaCount++;

            if (!namespace.equals(EMPTY_STRING)) {
                schema.setTargetNamespace(namespace);
                String prefix = null;
                if (namespaceInfo != null) {
                    prefix = namespaceInfo.getNamespaceResolver().resolveNamespaceURI(namespace);
                }
                if (prefix == null) {
                    prefix = schema.getNamespaceResolver().generatePrefix();
                }
                schema.getNamespaceResolver().put(prefix, namespace);
            }

            if (namespaceInfo != null) {
                schema.setAttributeFormDefault(namespaceInfo.isAttributeFormQualified());
                schema.setElementFormDefault(namespaceInfo.isElementFormQualified());
            }
            schemaForNamespace.put(namespace, schema);
            allSchemas.add(schema);
        }
        return schema;
    }

    public Collection<Schema> getAllSchemas() {
        if (allSchemas == null) {
            allSchemas = new ArrayList<Schema>();
        }
        return allSchemas;
    }

    public NamespaceInfo getNamespaceInfoForNamespace(String namespace) {

        return getNamespaceInfoForNamespace(namespace, null);
    }

    public NamespaceInfo getNamespaceInfoForNamespace(String namespace, String packageName) {

        if (null != packageName) {
            if (packageToPackageInfoMappings.containsKey(packageName)) {
                PackageInfo packageInfo = packageToPackageInfoMappings.get(packageName);
                if (packageInfo.getNamespace().equals(namespace)) {
                    return packageInfo.getNamespaceInfo();
                }
            }
        }

        Collection<PackageInfo> packageInfo = packageToPackageInfoMappings.values();
        for (PackageInfo info : packageInfo) {
            if (info.getNamespace().equals(namespace)) {
                return info.getNamespaceInfo();
            }
        }
        return null;
    }

    public String getPrefixForNamespace(Schema schema, String URI) {
        //add Import if necessary
        Schema referencedSchema = this.getSchemaForNamespace(URI);
        addImportIfRequired(schema, referencedSchema, URI);

        NamespaceResolver namespaceResolver = schema.getNamespaceResolver();
        Enumeration keys = namespaceResolver.getPrefixes();
        while (keys.hasMoreElements()) {
            String next = (String) keys.nextElement();
            String nextUri = namespaceResolver.resolveNamespacePrefix(next);
            if (nextUri.equals(URI)) {
                return next;
            }
        }
        return null;
    }

    /**
     * Attempt to resolve the given URI to a prefix.  If this is unsuccessful, one
     * will be generated and added to the resolver.
     *
     * @param URI
     * @param schema
     * @return
     */
    public String getOrGeneratePrefixForNamespace(String URI, Schema schema) {
        String prefix = schema.getNamespaceResolver().resolveNamespaceURI(URI);
        if (prefix == null) {
            if (URI.equals(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI)) {
                prefix = schema.getNamespaceResolver().generatePrefix(Constants.SCHEMA_PREFIX);
            } else if (URI.equals(Constants.REF_URL)) {
                prefix = schema.getNamespaceResolver().generatePrefix(Constants.REF_PREFIX);

                if(!importExists(schema, SWA_REF_IMPORT)){
                    Import schemaImport = new Import();
                    schemaImport.setSchemaLocation(SWA_REF_IMPORT);
                    schemaImport.setNamespace(URI);
                    schema.getImports().add(schemaImport);
                }
            } else {
                prefix = schema.getNamespaceResolver().generatePrefix();
            }
            schema.getNamespaceResolver().put(prefix, URI);
        }
        return prefix;
    }

    public void addGlobalElements(Map<QName, ElementDeclaration> additionalElements) {
        for (Entry<QName, ElementDeclaration> entry : additionalElements.entrySet()) {
            QName next = entry.getKey();
            if(next != null) {
                ElementDeclaration nextElement = entry.getValue();
                if (nextElement.getScopeClass() == GLOBAL.class) {

                    String namespaceURI = next.getNamespaceURI();
                    Schema targetSchema = getSchemaForNamespace(namespaceURI);
                    if (targetSchema == null) {
                        continue;
                    }

                    if (targetSchema.getTopLevelElements().get(next.getLocalPart()) == null) {
                        Element element = new Element();
                        element.setName(next.getLocalPart());
                        element.setNillable(nextElement.isNillable());
                        JavaClass javaClass = nextElement.getJavaType();

                        //First check for built in type
                        QName schemaType = (QName) helper.getXMLToJavaTypeMap().get(javaClass.getRawName());
                        if (schemaType != null) {
                            element.setType(Constants.SCHEMA_PREFIX + COLON + schemaType.getLocalPart());
                        } else if (areEquals(javaClass, JAVAX_ACTIVATION_DATAHANDLER) || areEquals(javaClass, byte[].class) || areEquals(javaClass, Byte[].class) || areEquals(javaClass, Image.class) || areEquals(javaClass, Source.class) || areEquals(javaClass, JAVAX_MAIL_INTERNET_MIMEMULTIPART)) {
                            schemaType = Constants.BASE_64_BINARY_QNAME;
                            if(nextElement.getTypeMappingInfo() != null) {
                                if(nextElement.isXmlAttachmentRef()) {
                                    schemaType = Constants.SWA_REF_QNAME;
                                }
                                if (nextElement.getXmlMimeType() != null) {
                                    element.getAttributesMap().put(Constants.EXPECTED_CONTENT_TYPES_QNAME, nextElement.getXmlMimeType());
                                }
                            }
                            String prefix = getOrGeneratePrefixForNamespace(schemaType.getNamespaceURI(), targetSchema);
                            element.setType(prefix + COLON + schemaType.getLocalPart());
                        } else if (areEquals(javaClass, CoreClassConstants.XML_GREGORIAN_CALENDAR)) {
                            schemaType = Constants.ANY_SIMPLE_TYPE_QNAME;
                            element.setType(Constants.SCHEMA_PREFIX + COLON + schemaType.getLocalPart());
                        } else {

                            TypeInfo type = (TypeInfo) this.typeInfo.get(javaClass.getQualifiedName());
                            if (type != null) {
                                String typeName = null;
                                if (type.isComplexType()) {
                                    typeName = type.getComplexType().getName();
                                } else {
                                    typeName = type.getSimpleType().getName();
                                }
                                // may need an anonymous complex type
                                if (typeName == null) {
                                    Schema schema = getSchemaForNamespace(next.getNamespaceURI());
                                    ComplexType cType = createComplexTypeForClass(javaClass, type);
                                    TypeDefParticle particle = null;
                                    if(cType.getComplexContent() != null && cType.getComplexContent().getExtension() != null) {
                                        particle = cType.getComplexContent().getExtension().getTypeDefParticle();
                                    } else {
                                        particle = cType.getTypeDefParticle();
                                    }
                                    addToSchemaType(type, type.getPropertyList(), particle, cType, schema);
                                    targetSchema = schema;
                                    element.setComplexType(cType);
                                } else {
                                    //  check namespace of schemaType
                                    if (type.getClassNamespace().equals(namespaceURI)) {
                                        //no need to prefix here
                                        String prefix = targetSchema.getNamespaceResolver().resolveNamespaceURI(namespaceURI);
                                        if(prefix != null && !(prefix.equals(EMPTY_STRING))) {
                                            element.setType(prefix + COLON + typeName);
                                        } else {
                                            element.setType(typeName);
                                        }
                                    } else {
                                        Schema complexTypeSchema = getSchemaForNamespace(type.getClassNamespace());
                                        String complexTypeSchemaNS = type.getClassNamespace();
                                        if (complexTypeSchemaNS == null) {
                                            complexTypeSchemaNS = EMPTY_STRING;
                                        }
                                        addImportIfRequired(targetSchema, complexTypeSchema, type.getClassNamespace());

                                        String prefix = targetSchema.getNamespaceResolver().resolveNamespaceURI(complexTypeSchemaNS);
                                        if (prefix != null) {
                                            element.setType(prefix + COLON + typeName);
                                        } else {
                                            element.setType(typeName);
                                        }
                                    }
                                }
                            }
                        }
                        if (nextElement.getSubstitutionHead() != null) {
                            String subLocal = nextElement.getSubstitutionHead().getLocalPart();
                            String subNamespace = nextElement.getSubstitutionHead().getNamespaceURI();
                            String prefix = getPrefixForNamespace(targetSchema, subNamespace);
                            if (prefix == null || prefix.equals(EMPTY_STRING)) {
                                element.setSubstitutionGroup(subLocal);
                            } else {
                                element.setSubstitutionGroup(prefix + COLON + subLocal);
                            }
                        }
                        targetSchema.addTopLevelElement(element);
                        SchemaTypeInfo info = this.schemaTypeInfo.get(javaClass.getQualifiedName());
                        if (info == null) {
                            // probably for a simple type, not generated
                            info = new SchemaTypeInfo();
                            info.setSchemaTypeName(schemaType);
                            schemaTypeInfo.put(javaClass.getQualifiedName(), info);
                        }
                        info.getGlobalElementDeclarations().add(next);
                    }
                }
            }
        }
    }

    /**
     * Return the Map of SchemaTypeInfo instances.  The Map is keyed on JavaClass
     * qualified name.
     *
     * @return
     */
    public Map<String, SchemaTypeInfo> getSchemaTypeInfo() {
        return this.schemaTypeInfo;
    }

    private boolean importExists(Schema schema, String schemaName) {
        java.util.List imports = schema.getImports();
        for (int i = 0; i < imports.size(); i++) {
            Import nextImport = (Import) imports.get(i);
            if (nextImport.getSchemaLocation() != null && nextImport.getSchemaLocation().equals(schemaName)) {
                return true;
            }
        }
        return false;
    }

    private boolean addImportIfRequired(Schema sourceSchema, Schema importSchema, String importNamespace) {
        if (importSchema != sourceSchema && !(importNamespace != null && importNamespace.equals(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI))) {
            String schemaName = null;
            if(javax.xml.XMLConstants.XML_NS_URI.equals(importNamespace)){
                schemaName = Constants.XML_NAMESPACE_SCHEMA_LOCATION;
            }else{
                if (importSchema != null) {
                    schemaName = importSchema.getName();
                } else if (importNamespace != null) {
                    NamespaceInfo nInfo = getNamespaceInfoForNamespace(importNamespace);
                    schemaName = nInfo.getLocation();
                }
            }
            // may need to relativize the schema name
            if (schemaName != null && importSchema != null) {
                URI relativizedURI = null;
                try {
                    // need to strip off the last slash and the file name that follows
                    String schemaPath = sourceSchema.getName();
                    int idx;
                    if ((idx = schemaPath.lastIndexOf(SLASH)) != -1) {
                        schemaPath = schemaPath.substring(0, idx);
                    }
                    URI baseURI = new URI(schemaPath);
                    URI importURI = new URI(schemaName);
                    relativizedURI = baseURI.relativize(importURI);
                } catch (Exception e) {
                    // at this point we will leave schemaName as is
                    relativizedURI = null;
                }
                if (relativizedURI != null) {
                    schemaName = relativizedURI.getPath();
                }
            }

            if (schemaName != null && !importExists(sourceSchema, schemaName)) {
                Import schemaImport = new Import();
                schemaImport.setSchemaLocation(schemaName);
                if (importNamespace != null && !importNamespace.equals(EMPTY_STRING)) {
                    schemaImport.setNamespace(importNamespace);
                }
                sourceSchema.getImports().add(schemaImport);
                if (schemaImport.getNamespace() != null) {
                    if(schemaImport.getNamespace().equals(javax.xml.XMLConstants.XML_NS_URI)) {
                        //make sure xml namespace is in the resolver so it gets declared
                        sourceSchema.getNamespaceResolver().put(javax.xml.XMLConstants.XML_NS_PREFIX, javax.xml.XMLConstants.XML_NS_URI);
                    }
                    String prefix = sourceSchema.getNamespaceResolver().resolveNamespaceURI(importNamespace);
                    //Don't need to generate prefix for default namespace
                    if (prefix == null && !importNamespace.equals(EMPTY_STRING)) {
                        sourceSchema.getNamespaceResolver().put(sourceSchema.getNamespaceResolver().generatePrefix(), importNamespace);
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Compares a JavaModel JavaClass to a Class.  Equality is based on
     * the raw name of the JavaClass compared to the canonical
     * name of the Class.
     *
     * @param src
     * @param tgtCanonicalName
     * @return
     */
    protected boolean areEquals(JavaClass src, String tgtCanonicalName) {
        if (src == null || tgtCanonicalName == null) {
            return false;
        }
        return src.getRawName().equals(tgtCanonicalName);
    }

    /**
     * Compares a JavaModel JavaClass to a Class.  Equality is based on
     * the raw name of the JavaClass compared to the canonical
     * name of the Class.
     *
     * @param src
     * @param tgt
     * @return
     */
    protected boolean areEquals(JavaClass src, Class tgt) {
        if (src == null || tgt == null) {
            return false;
        }
        return src.getRawName().equals(tgt.getCanonicalName());
    }

    /**
     * Return the type name for a given Property.
     *
     * @param next
     * @param javaType
     * @param theSchema
     * @return
     */
    private String getTypeName(Property next, JavaClass javaType, Schema theSchema){
        QName schemaType = next.getSchemaType();
        if (schemaType == null) {
            schemaType = getSchemaTypeFor(javaType);
        }
        if (schemaType != null) {
            if (schemaType.getNamespaceURI() == null) {
                return schemaType.getLocalPart();
            }
            return getOrGeneratePrefixForNamespace(schemaType.getNamespaceURI(), theSchema) + COLON + schemaType.getLocalPart();
        }
        return Constants.SCHEMA_PREFIX + Constants.COLON + Constants.ANY_SIMPLE_TYPE;
    }

    /**
     * Return the qualified (if necessary) type name for a given Property.
     *
     * @param prop
     * @param schema
     * @return
     */
    private String getQualifiedTypeName(Property prop, Schema schema) {
        JavaClass javaType = prop.getActualType();
        String typeName = getTypeName(prop, javaType, schema);
        // may need to qualify the type
        if (typeName != null && !typeName.contains(COLON)) {
            String prefix = getPrefixForNamespace(schema, schema.getTargetNamespace());
            if (prefix != null) {
                typeName = prefix + COLON + typeName;
            }
        }
        return typeName;
    }

    /**
     * This method will build element/complexType/typedefparticle components for a given xml-path,
     * and return an XmlPathResult instance containg the sequence that the target should be added
     * to, as well as the current schema - which could be different than the working schema used
     * before calling this method in the case of a prefixed path element from a different namespace.
     * Regarding the path 'target', if the xml-path was "contact-info/address/street", "street"
     * would be the target.  In this case the sequence containing the "address" element would be
     * set in the XmlPathResult to be returned.
     *
     * The exception case is an 'any', where we want to process the last path element before
     * returning - this is necessary due to the fact that an Any will be added to the sequence
     * in place of the last path element by the calling method.
     *
     * @param frag
     * @param xpr
     * @param isChoice
     * @param next
     * @return
     */
    protected AddToSchemaResult buildSchemaComponentsForXPath(XPathFragment frag, AddToSchemaResult xpr, boolean isChoice, Property next) {
        boolean isAny = next.isAny() || next.isAnyAttribute();
        TypeDefParticle currentParticle = xpr.particle;
        Schema workingSchema = xpr.schema;

        // each nested choice on a collection will be unbounded
        boolean isUnbounded = false;
        if(currentParticle != null) {
            isUnbounded = (currentParticle.getMaxOccurs() != null && currentParticle.getMaxOccurs()==Occurs.UNBOUNDED);
        }

        // don't process the last frag; that will be handled by the calling method if necessary
        // note that we may need to process the last frag if it has a namespace or is an 'any'
        boolean lastFrag = (frag.getNextFragment() == null || frag.getNextFragment().nameIsText());
        //boolean isNextAttribute = (frag.getNextFragment() != null) && (frag.getNextFragment().isAttribute());
        // if the element is already in the sequence we don't want the calling method to add a second one
        if (lastFrag && (elementExistsInParticle(frag.getLocalName(), frag.getShortName(), currentParticle) != null)) {
            xpr.particle = null;
            return xpr;
        }


        // if the current element exists, use it; otherwise create a new one
        Element currentElement = elementExistsInParticle(frag.getLocalName(), frag.getShortName(), currentParticle);
        boolean currentElementExists = (currentElement != null);

        if (!currentElementExists) {
            currentElement = new Element();
            // don't set the element name yet, as it may end up being a ref
            ComplexType cType = new ComplexType();
            TypeDefParticle particle = null;
            if (isChoice) {
                particle = new Choice();
                if (isUnbounded) {
                    particle.setMaxOccurs(Occurs.UNBOUNDED);
                }
            } else {
                XPathFragment nextFragment = frag.getNextFragment();
                if (frag.containsIndex() || frag.getPredicate() != null || (!next.isXmlList() && null != nextFragment && nextFragment.isAttribute() && helper.isCollectionType(next.getType()))) {
                    currentElement.setMaxOccurs(Occurs.UNBOUNDED);
                }
                particle = new Sequence();
            }
            cType.setTypeDefParticle(particle);
            currentElement.setComplexType(cType);
        } else {
            //if the current element already exists, we may need to change it's type
            XPathFragment nextFrag = frag.getNextFragment();
            if(nextFrag != null && nextFrag.isAttribute()) {
                if(currentElement.getType() != null && currentElement.getComplexType() == null) {
                    //there's already a text mapping to this element, so
                    //attributes can be added by making it complex with
                    //simple content.
                    SimpleType type = currentElement.getSimpleType();
                    if(type != null) {
                        ComplexType cType = new ComplexType();
                        cType.setSimpleContent(new SimpleContent());
                        Extension extension = new Extension();
                        extension.setBaseType(type.getRestriction().getBaseType());
                        cType.getSimpleContent().setExtension(extension);
                        currentElement.setSimpleType(null);
                        currentElement.setComplexType(cType);
                    } else {
                        String eType = currentElement.getType();
                        ComplexType cType = new ComplexType();
                        SimpleContent sContent = new SimpleContent();
                        Extension extension = new Extension();
                        extension.setBaseType(eType);
                        sContent.setExtension(extension);
                        cType.setSimpleContent(sContent);
                        currentElement.setType(null);
                        currentElement.setComplexType(cType);
                    }
                }
            }
        }
        // may need to create a ref, depending on the namespace
        Element globalElement = null;

        String fragUri = frag.getNamespaceURI();
        if (fragUri != null) {
            Schema fragSchema = getSchemaForNamespace(fragUri);
            String targetNS = workingSchema.getTargetNamespace();

            // handle Attribute case
            if (frag.isAttribute()) {
                if (fragSchema == null || (fragSchema.isAttributeFormDefault() && !fragUri.equals(targetNS)) || (!fragSchema.isAttributeFormDefault() && fragUri.length() > 0)) {
                    // must generate a global attribute and create a reference to it
                    // if the global attribute exists, use it; otherwise create a new one
                    // if fragSchema is null, just generate the ref
                    if(fragSchema != null) {
                        Attribute globalAttribute = null;
                        globalAttribute = (Attribute) fragSchema.getTopLevelAttributes().get(frag.getLocalName());
                        if (globalAttribute == null) {
                            globalAttribute = createGlobalAttribute(frag, workingSchema, fragSchema, next);
                        }
                    } else {
                        //may need to add an import
                        addImportIfRequired(workingSchema, fragSchema, fragUri);
                    }
                    // add the attribute ref to the current element
                    String attributeRefName;
                    if (fragUri.equals(targetNS)) {
                        String prefix = fragSchema.getNamespaceResolver().resolveNamespaceURI(fragUri);
                        attributeRefName = prefix + COLON + frag.getLocalName();
                    } else {
                        attributeRefName = frag.getShortName();
                    }
                    TypeDefParticleOwner type;
                    if(currentParticle != null) {
                        type = currentParticle.getOwner();
                    } else {
                        type = xpr.simpleContentType;
                    }
                    if (type instanceof ComplexType) {
                        createRefAttribute(attributeRefName, (ComplexType)type);
                    }
                    // set the frag's schema as it may be different than the current schema
                    xpr.schema = fragSchema;
                    // ref case - indicate to the calling method that there's nothing to do
                    xpr.particle = null;
                }
                // since we are dealing with an attribute, we are on the last fragment; return
                return xpr;
            }

            // here we are dealing with an Element
            if ((fragSchema.isElementFormDefault() && !fragUri.equals(targetNS)) || (!fragSchema.isElementFormDefault() && fragUri.length() > 0)) {
                // must generate a global element and create a reference to it
                // if the global element exists, use it; otherwise create a new one
                globalElement = (Element) fragSchema.getTopLevelElements().get(frag.getLocalName());
                if (globalElement == null) {
                    globalElement = createGlobalElement(frag, workingSchema, fragSchema, isChoice, isUnbounded, next, (lastFrag && !isAny));
                }
                // if the current element doesn't exist set a ref and add it to the sequence
                if (!currentElementExists) {
                    // use prefix from the working schema's resolver - add prefix/uri pair if necessary
                    String fragPrefix = workingSchema.getNamespaceResolver().resolveNamespaceURI(fragUri);
                    if (fragPrefix == null) {
                        fragPrefix = workingSchema.getNamespaceResolver().generatePrefix(frag.getPrefix());
                        workingSchema.getNamespaceResolver().put(fragPrefix, fragUri);
                    }
                    currentElement = createRefElement(fragPrefix + COLON + frag.getLocalName(), currentParticle);
                    if (frag.containsIndex() || frag.getPredicate() != null || helper.isCollectionType(next.getType())) {
                        currentElement.setMaxOccurs(Occurs.UNBOUNDED);
                    }
                    currentElementExists = true;
                }
                // set the frag's schema as it may be different than the current schema
                xpr.schema = fragSchema;
                // at this point, if we are dealing with the last fragment we will need to return
                if (lastFrag) {
                    // since we processed the last frag, return null so the calling method doesn't
                    // add a second one...unless we're dealing with an 'any'
                    if (isAny) {
                        // set the particle that the 'any' will be added to by the calling method
                        xpr.particle = globalElement.getComplexType().getTypeDefParticle();
                        return xpr;
                    }
                    // ref case - indicate to the calling method that there's nothing to do
                    xpr.particle = null;
                    return xpr;
                }
                // make the global element current
                currentElement = globalElement;
            }
        }
        if (!lastFrag || (lastFrag && isAny)) {
            // if we didn't process a global element, and the current element isn't already in the sequence, add it
            if (!currentElementExists && globalElement == null) {
                currentElement.setName(frag.getLocalName());
                Integer minOccurs = next.getMinOccurs();
                if (minOccurs != null) currentElement.setMinOccurs(String.valueOf(minOccurs));
                else                   currentElement.setMinOccurs(Occurs.ZERO);
                currentParticle.addElement(currentElement);
            }
            // set the correct particle to use/return
            if(currentElement.getComplexType() != null) {
                if(currentElement.getComplexType().getTypeDefParticle() == null) {
                    //complexType with simple-content
                    xpr.simpleContentType = currentElement.getComplexType();
                    xpr.particle = null;
                } else {
                    xpr.particle = currentElement.getComplexType().getTypeDefParticle();
                }
            } else {
                //If there's no complex type, we're building the path through an element with
                //a simple type. In order to build the path through this
                //element, switch to a complex type with simple content.
                SimpleType type = currentElement.getSimpleType();
                if(type != null) {
                    ComplexType cType = new ComplexType();
                    cType.setSimpleContent(new SimpleContent());
                    Extension extension = new Extension();
                    extension.setBaseType(type.getRestriction().getBaseType());
                    cType.getSimpleContent().setExtension(extension);
                    currentElement.setSimpleType(null);
                    currentElement.setComplexType(cType);
                    xpr.particle = null;
                    xpr.simpleContentType = cType;
                } else {
                    String eType = currentElement.getType();
                    ComplexType cType = new ComplexType();
                    SimpleContent sContent = new SimpleContent();
                    Extension extension = new Extension();
                    extension.setBaseType(eType);
                    sContent.setExtension(extension);
                    cType.setSimpleContent(sContent);
                    currentElement.setType(null);
                    currentElement.setComplexType(cType);
                    xpr.particle = null;
                    xpr.simpleContentType = cType;
                }
            }
        }
        // if we're on the last fragment, we're done
        if (lastFrag) {
            return xpr;
        }
        // call back into this method to process the next path element
        return buildSchemaComponentsForXPath(frag.getNextFragment(), xpr, isChoice, next);
    }

    /**
     * Convenience method for determining if an element already exists in a given
     * typedefparticle.  If an element exists whose ref is equal to 'refString'
     * or its name is equal to 'elementName', it is returned.  Null otherwise.
     *
     * Note that ref takes precidence, so if either has a ref set name equality
     * will not be performed.
     *
     * @param elementName the non-null element name to look for
     * @param refString if the element is a ref, this will be the prefix qualified element name
     * @param particle the sequence/choice/all to search for an existing element
     * @return
     */
    protected Element elementExistsInParticle(String elementName, String refString, TypeDefParticle particle) {
        if (particle == null || particle.getElements() == null || particle.getElements().size() == 0) {
            return null;
        }
        java.util.List existingElements = particle.getElements();
        if (existingElements != null) {
            Iterator elementIt = existingElements.iterator();
            while (elementIt.hasNext()) {
                Element element;
                // could be other components in the list, like Any, but we only care about Element
                try {
                    element = (Element) elementIt.next();
                } catch (ClassCastException cce) {
                    continue;
                }
                // case #1 - refString is null
                if (refString == null) {
                    // if element has a null ref value and the element names are equal, or element has a non-null
                    // ref that is not equal to its name, and the element names are equal, we found a match
                    if ((element.getRef() == null || (element.getRef() != null && element.getRef().equals(element.getName()))) && elementName.equals(element.getName())) {
                        return element;
                    }
                }
                // case #2 - refString equals elementName
                else if (refString.equals(elementName)) {
                    // if element has a ref equal to refString, or no ref but element names are equal, we found a match
                    if ((element.getRef() != null && element.getRef().equals(refString)) || (element.getRef() == null && elementName.equals(element.getName()))) {
                        return element;
                    }
                }
                // case #3 - refString is different than elementName
                else if (element.getRef() != null && element.getRef().equals(refString)) {
                    return element;
                }
            }
        }
        return null;
    }

    /**
     * Create a global attribute.  An import is added if necessary.  This method
     * will typically be called when processing an XPath and a prefixed path
     * element is encountered tha requires an attribute ref.
     *
     * @param frag
     * @param workingSchema
     * @param fragSchema
     * @param prop
     * @return
     */
    public Attribute createGlobalAttribute(XPathFragment frag, Schema workingSchema, Schema fragSchema, Property prop) {
        Attribute gAttribute = new Attribute();
        gAttribute.setName(frag.getLocalName());
        gAttribute.setType(getQualifiedTypeName(prop, fragSchema));
        fragSchema.getTopLevelAttributes().put(gAttribute.getName(), gAttribute);
        addImportIfRequired(workingSchema, fragSchema, frag.getNamespaceURI());
        return gAttribute;
    }

    /**
     * Create a global element.  An import is added if necessary.  This method
     * will typically be called when processing an XPath and a prefixed path
     * element is encountered the requires an element ref.
     *
     * @param frag XPathFragment which wil lbe used to create the global element
     * @param workingSchema current schema
     * @param fragSchema frag's schema
     * @param isChoice indicates if we need to construct a choice
     * @param isUnbounded maxOccurs setting for choice
     * @param prop property which owns the xml-path
     * @param shouldSetType if this is the last fragment in the xml-path and not an 'any', we should set the type
     * @return
     */
    public Element createGlobalElement(XPathFragment frag, Schema workingSchema, Schema fragSchema, boolean isChoice, boolean isUnbounded, Property prop, boolean shouldSetType) {
        Element gElement = new Element();
        gElement.setName(frag.getLocalName());
        if (shouldSetType) {
            gElement.setType(getQualifiedTypeName(prop, fragSchema));
        } else {
            ComplexType gCType = new ComplexType();
            TypeDefParticle particle;
            if (isChoice) {
                particle = new Choice();
                if (isUnbounded) {
                    particle.setMaxOccurs(Occurs.UNBOUNDED);
                }
            } else {
                particle = new Sequence();
            }
            gCType.setTypeDefParticle(particle);
            gElement.setComplexType(gCType);
        }
        fragSchema.addTopLevelElement(gElement);
        addImportIfRequired(workingSchema, fragSchema, frag.getNamespaceURI());
        return gElement;
    }

    /**
     * Create an element reference and add it to a given particle. This
     * method will typically be called when processing an XPath and a
     * prefixed path element is encountered that requires an element ref.
     *
     * @param elementRefName
     * @param particle
     * @return
     */
    public Element createRefElement(String elementRefName, TypeDefParticle particle) {
        Element refElement = new Element();
        // ref won't have a complex type
        refElement.setComplexType(null);
        refElement.setMinOccurs(Occurs.ZERO);
        refElement.setMaxOccurs(Occurs.ONE);
        refElement.setRef(elementRefName);
        particle.addElement(refElement);
        return refElement;
    }

    /**
     * Create an attribute reference and add it to a given complex type.
     * This method will typically be called when processing an XPath
     * and a prefixed path element is encountered that requires an
     * attribute ref.
     *
     * @param attributeRefName
     * @param owningComplexType
     * @return
     */
    public Attribute createRefAttribute(String attributeRefName, ComplexType owningComplexType) {
        Attribute refAttribute = new Attribute();
        refAttribute.setRef(attributeRefName);
        if (owningComplexType.getSimpleContent() != null) {
            owningComplexType.getSimpleContent().getExtension().getOrderedAttributes().add(refAttribute);
        } else {
            owningComplexType.getOrderedAttributes().add(refAttribute);
        }
        return refAttribute;
    }

    /**
     * This class will typically be used when building schema components.  It will hold the
     * TypeDefParticle (all, sequence, choice), ComplexType, and/or Schema that are to be
     * used by the method that is processing the given property.
     *
     */
    private static final class AddToSchemaResult {
        ComplexType type;
        TypeDefParticle particle;
        Schema schema;
        ComplexType simpleContentType;

        AddToSchemaResult(TypeDefParticle particle, Schema schema) {
            this.particle = particle;
            this.schema = schema;
        }

        AddToSchemaResult(TypeDefParticle particle, ComplexType type) {
            this.particle = particle;
            this.type = type;
        }
    }

    /**
     * Convenience method for processing XmlWriteTransformer(s) for a given property.
     * Required schema components will be generated and set accordingly.
     *
     * @param property the property containing one or more XmlWriteTransformers.
     * @param typeInfo the TypeInfo that owns the property
     * @param compositor sequence/choice/all to modify
     * @param type ComplexType which compositor(s) should be added to
     * @param schema current schema which ComplextType will be added to
     */
    private void addTransformerToSchema(Property property, TypeInfo typeInfo, TypeDefParticle compositor, ComplexType type, Schema schema) {
        java.util.List<Property> props = new ArrayList<Property>();
        for (XmlWriteTransformer writeTransformer : property.getXmlTransformation().getXmlWriteTransformer()) {
            String xpath = writeTransformer.getXmlPath();
            String pname = XMLProcessor.getNameFromXPath(xpath, property.getPropertyName(), xpath.contains(ATT));
            Property prop = new Property(helper);
            prop.setPropertyName(pname);
            prop.setXmlPath(xpath);
            prop.setSchemaName(new QName(pname));
            // figure out the type based on transformer method return type
            JavaClass jType = null;
            JavaClass jClass = null;
            JavaMethod jMethod = null;
            String methodName;
            if (writeTransformer.isSetTransformerClass()) {
                // handle transformer class
                try {
                    jClass = helper.getJavaClass(writeTransformer.getTransformerClass());
                } catch (JAXBException x) {
                    throw JAXBException.transformerClassNotFound(writeTransformer.getTransformerClass());
                }
                methodName = BUILD_FIELD_VALUE_METHOD;
                jMethod = jClass.getDeclaredMethod(methodName, new JavaClass[] { helper.getJavaClass(Object.class), helper.getJavaClass(String.class), helper.getJavaClass(Session.class) });
                if (jMethod == null) {
                    throw JAXBException.noSuchWriteTransformationMethod(methodName);
                }
                jType = jMethod.getReturnType();
            } else {
                // handle method
                // here it is assumed that the JavaModel is aware of the TypeInfo's class, hence jClass cannot be null
                jClass = helper.getJavaClass(typeInfo.getJavaClassName());
                methodName = writeTransformer.getMethod();
                // the method can have 0 args or 1 arg (either AbstractSession or Session)
                // first check 0 arg
                jMethod = jClass.getDeclaredMethod(methodName, new JavaClass[] {});
                if (jMethod == null) {
                    // try AbstractSession
                    jMethod = jClass.getDeclaredMethod(methodName, new JavaClass[] { helper.getJavaClass(AbstractSession.class) });
                    if (jMethod == null) {
                        // try Session
                        jMethod = jClass.getDeclaredMethod(methodName, new JavaClass[] { helper.getJavaClass(Session.class) });
                        if (jMethod == null) {
                            throw JAXBException.noSuchWriteTransformationMethod(methodName);
                        }
                    }
                }
                jType = jMethod.getReturnType();
            }
            prop.setType(jType);
            props.add(prop);
        }
        addToSchemaType(typeInfo, props, compositor, type, schema);
    }

    /**
     * Process a given XmlPath, and create the required schema components.  The last
     * fragment may not be processed; in this case the returned XmlPathResult is
     * used to set the current schema and parent complex type, then the owning
     * property is processed as per usual. Note the last fragment may be processed
     * if it has a namespace or is an 'any'.
     *
     * @param property the property containing the XmlPath for which schema components are to be built
     * @param compositor the sequence/choice/all to modify
     * @param schema the schema being built when this method is called - this could
     *        if the XmlPath contains an entry that references a different namespace
     * @param isChoice indicates if the given property is a choice property
     * @param type the ComplexType which compositor(s) should be added to
     * @return AddToSchemaResult containing current schema and sequence/all/choice build
     *         based on the given XmlPath
     */
    private AddToSchemaResult addXPathToSchema(Property property, TypeDefParticle compositor, Schema schema, boolean isChoice, ComplexType type) {
        // '.' xml-path requires special handling
        if (property.getXmlPath().equals(DOT)) {
            TypeInfo info = (TypeInfo) typeInfo.get(property.getActualType().getQualifiedName());
            JavaClass infoClass = property.getActualType();
            addSelfProperties(infoClass, info, compositor, type);
            return null;
        }
        // create the XPathFragment(s) for the path
        Field xfld = new XMLField(property.getXmlPath());
        xfld.setNamespaceResolver(schema.getNamespaceResolver());
        xfld.initialize();
        AddToSchemaResult result = new AddToSchemaResult(compositor, schema);
        if(compositor == null) {
            //complex type with simple content
            result.simpleContentType = type;
        }
        // build the schema components for the xml-path
        return buildSchemaComponentsForXPath(xfld.getXPathFragment(), new AddToSchemaResult(compositor, schema), isChoice, property);
    }

    private void addSelfProperties(JavaClass aJavaClass, TypeInfo info, TypeDefParticle compositor, ComplexType type) {
        // Recursively add all properties from aJavaClass and its superclasses, adding superclass properties first.
        if (aJavaClass.getSuperclass() != null) {
            TypeInfo superInfo = (TypeInfo) typeInfo.get(aJavaClass.getSuperclass().getQualifiedName());
            if (superInfo != null) {
                addSelfProperties(aJavaClass.getSuperclass(), superInfo, compositor, type);
            }
        }
        addToSchemaType(info, info.getPropertyList(), compositor, type, info.getSchema());
    }

    /**
     * Convenience method for processing an XmlElementWrapper for a given property. Required schema
     * components will be generated and set accordingly.
     *
     * @param property the property containing an XmlElementWrapper
     * @param schema the schema currently being generated
     * @param compositor sequence/choice/all that the generated wrapper Element will be added to
     * @return AddToSchemaResult containing current ComplexType and TypeDefParticle
     */
    private AddToSchemaResult addXmlElementWrapperToSchema(Property property, Schema schema, TypeDefParticle compositor) {
        XmlElementWrapper wrapper = property.getXmlElementWrapper();
        Element wrapperElement = new Element();
        String name = wrapper.getName();
        // handle nillable
        wrapperElement.setNillable(wrapper.isNillable());

        // namespace in not the target or ##default, create a ref with min/max = 1
        String wrapperNS = wrapper.getNamespace();
        if (!wrapperNS.equals(XMLProcessor.DEFAULT) && !wrapperNS.equals(schema.getTargetNamespace())) {
            wrapperElement.setMinOccurs(Occurs.ONE);
            wrapperElement.setMaxOccurs(Occurs.ONE);

            String prefix = getOrGeneratePrefixForNamespace(wrapperNS, schema);
            wrapperElement.setRef(prefix + COLON + name);
            compositor.addElement(wrapperElement);
            // assume that the element exists and does not need to be created
            return null;
        }
        wrapperElement.setName(name);
        if (wrapper.isRequired()) {
            wrapperElement.setMinOccurs(Occurs.ONE);
        } else {
            wrapperElement.setMinOccurs(Occurs.ZERO);
        }
        if (!wrapperNS.equals(XMLProcessor.DEFAULT)) {
            String lookupNamespace = schema.getTargetNamespace();
            if (lookupNamespace == null) {
                lookupNamespace = EMPTY_STRING;
            }
            NamespaceInfo namespaceInfo = getNamespaceInfoForNamespace(lookupNamespace);
            boolean isElementFormQualified = false;
            if (namespaceInfo != null) {
                isElementFormQualified = namespaceInfo.isElementFormQualified();
            }
            shouldAddRefAndSetForm(wrapperElement, wrapperNS, lookupNamespace,
                    isElementFormQualified, true);
        }
        compositor.addElement(wrapperElement);
        ComplexType wrapperType = new ComplexType();
        Sequence wrapperSequence = new Sequence();
        wrapperType.setSequence(wrapperSequence);
        wrapperElement.setComplexType(wrapperType);
        return new AddToSchemaResult(wrapperSequence, wrapperType);
    }

    /**
     * Build an Attribute with name and type set.  This method will typically be
     * called when processing an XPath that has no associated Property that can
     * be used to build an Attribute, such as in the case of XmlJoinNodes.
     *
     * @param attributeName name of the Attribute
     * @param typeName type of the Attribute
     * @return
     */
    private Attribute buildAttribute(QName attributeName, String typeName) {
        Attribute attribute = new Attribute();
        attribute.setName(attributeName.getLocalPart());
        attribute.setType(typeName);
        return attribute;
    }

    /**
     * Build an Attribute based on a given Property.
     *
     * @param property the Property used to build the Attribute
     * @param schema the schema currently being generated
     * @return
     */
    private Attribute buildAttribute(Property property, Schema schema) {
        Attribute attribute = new Attribute();

        QName attributeName = property.getSchemaName();
        attribute.setName(attributeName.getLocalPart());
        if (property.isRequired()) {
            attribute.setUse(Attribute.REQUIRED);
        }
        String fixedValue = property.getFixedValue();
        if (fixedValue != null){
            attribute.setFixed(fixedValue);
        }
        // Check to see if it's a collection
        TypeInfo info = (TypeInfo) typeInfo.get(property.getActualType().getQualifiedName());
        String typeName = getTypeNameForComponent(property, schema, property.getActualType(), attribute, false);
        if (isCollectionType(property)) {
            if(!property.isXmlList() && null != property.getXmlPath() && property.getXmlPath().contains("/")) {
                attribute.setType(typeName);
            } else {
                // assume XmlList for an attribute collection
                SimpleType localType = new SimpleType();
                org.eclipse.persistence.internal.oxm.schema.model.List list = new org.eclipse.persistence.internal.oxm.schema.model.List();
                list.setItemType(typeName);
                localType.setList(list);
                attribute.setSimpleType(localType);
            }
        } else {
            // may need to qualify the type
            if (typeName != null && !typeName.contains(COLON)) {
                if (info.getSchema() == schema) {
                    String uri = schema.getTargetNamespace();
                    if(uri == null) {
                        uri = EMPTY_STRING;
                    }
                    String prefix = getPrefixForNamespace(schema, uri);
                    if (prefix != null) {
                        typeName = prefix + COLON + typeName;
                    }
                }
            }
            attribute.setType(typeName);
        }
        return attribute;
    }

    /**
     * Convenience method for processing an attribute property. Required schema
     * components will be generated and set accordingly.
     *
     * @param attribute the attribute property to be processed
     * @param schema the schema currently being generated
     * @param type the ComplexType which compositor(s) should be added to
     */
    private void addAttributeToSchema(Attribute attribute, QName attributeName, Schema schema, ComplexType type) {
        String lookupNamespace = schema.getTargetNamespace();
        if (lookupNamespace == null) {
            lookupNamespace = EMPTY_STRING;
        }
        NamespaceInfo namespaceInfo = getNamespaceInfoForNamespace(lookupNamespace);
        boolean isAttributeFormQualified = false;
        if (namespaceInfo != null) {
            isAttributeFormQualified = namespaceInfo.isAttributeFormQualified();
        }

        boolean addRef = shouldAddRefAndSetForm(attribute, attributeName.getNamespaceURI(), lookupNamespace, isAttributeFormQualified, false);
        if(addRef){
            Schema attributeSchema = this.getSchemaForNamespace(attributeName.getNamespaceURI());
            if (attributeSchema != null && attributeSchema.getTopLevelAttributes().get(attribute.getName()) == null) {
                //don't overwrite existing global elements and attributes.
                attributeSchema.getTopLevelAttributes().put(attribute.getName(), attribute);
            }
            Attribute reference = new Attribute();
            String prefix = getPrefixForNamespace(schema, attributeName.getNamespaceURI());
            if (prefix == null) {
                reference.setRef(attribute.getName());
            } else {
                reference.setRef(prefix + COLON + attribute.getName());
            }
            if (type.getSimpleContent() != null) {
                type.getSimpleContent().getExtension().getOrderedAttributes().add(reference);
            } else {
                type.getOrderedAttributes().add(reference);
            }
        } else {
            if (type.getSimpleContent() != null) {
                type.getSimpleContent().getExtension().getOrderedAttributes().add(attribute);
            } else if (type.getComplexContent() != null) {
                type.getComplexContent().getExtension().getOrderedAttributes().add(attribute);
            } else {
                type.getOrderedAttributes().add(attribute);
            }
        }
    }

    private boolean shouldAddRefAndSetForm(SimpleComponent sc, String simpleComponentNamespace, String lookupNamespace, boolean formQualified, boolean isElement){
        if(sc.getRef() != null){
            return true;
        }
        boolean addRef = false;
        boolean sameNamespace = simpleComponentNamespace.equals(lookupNamespace);

        if (formQualified && !sameNamespace){
            if(simpleComponentNamespace.equals(EMPTY_STRING)){
                sc.setForm(Constants.UNQUALIFIED);
            }else{
                addRef = true;
            }
        } else if(!formQualified  && !simpleComponentNamespace.equals(EMPTY_STRING)){
            if(sameNamespace && isElement){
                sc.setForm(Constants.QUALIFIED);
            }else{
                addRef = true;
            }
        }
        return addRef;
    }

    /**
     * Convenience method for processing an any attribute property. Required
     * schema components will be generated and set accordingly.
     *
     * @param type the ComplexType which compositor(s) should be added to
     */
    private void addAnyAttributeToSchema(ComplexType type) {
        AnyAttribute anyAttribute = new AnyAttribute();
        anyAttribute.setProcessContents(SKIP);
        anyAttribute.setNamespace(Constants.ANY_NAMESPACE_OTHER);
        if (type.getSimpleContent() != null) {
            SimpleContent content = type.getSimpleContent();
            if(content.getExtension() != null){
                content.getExtension().setAnyAttribute(anyAttribute);
            }else if(content.getRestriction() != null){
                content.getRestriction().setAnyAttribute(anyAttribute);
            }
        } else {
            type.setAnyAttribute(anyAttribute);
        }
    }

    /**
     * Convenience method for processing an any property. Required
     * schema components will be generated and set accordingly.
     *
     * @param property the choice property to be processed
     * @param compositor the sequence/choice/all to modify
     */
    private void addAnyToSchema(Property property, TypeDefParticle compositor) {
        addAnyToSchema(property, compositor, isCollectionType(property)|| property.getType().isArray(), Constants.ANY_NAMESPACE_OTHER);
    }

    /**
     * Convenience method for processing an any property. Required
     * schema components will be generated and set accordingly.
     *
     * @param property the choice property to be processed
     * @param compositor the sequence/choice/all to modify
     * @param isCollection if true will be unbounded
     */
    private void addAnyToSchema(Property property, TypeDefParticle compositor, boolean isCollection) {
        addAnyToSchema(property, compositor, isCollection, Constants.ANY_NAMESPACE_OTHER);
    }

    /**
     * Convenience method for processing an any property. Required
     * schema components will be generated and set accordingly.
     *
     * @param property the choice property to be processed
     * @param compositor the sequence/choice/all to modify
     * @param isCollection if true will be unbounded
     * @param anyNamespace value for the Any's namespace attribute
     */
    private void addAnyToSchema(Property property, TypeDefParticle compositor, boolean isCollection, String anyNamespace) {
        Any any = new Any();
        any.setNamespace(anyNamespace);
        if (property.isLax()) {
            any.setProcessContents(Any.LAX);
        } else {
            any.setProcessContents(SKIP);
        }
        if (isCollection) {
            any.setMinOccurs(Occurs.ZERO);
            any.setMaxOccurs(Occurs.UNBOUNDED);
        }
        if (compositor instanceof Sequence) {
            ((Sequence) compositor).addAny(any);
        } else if (compositor instanceof Choice) {
            ((Choice) compositor).addAny(any);
        }
    }

    /**
     * Convenience method for processing a choice property. Required
     * schema components will be generated and set accordingly.
     *
     * @param property the choice property to be processed
     * @param typeInfo the TypeInfo that the given property belongs to
     * @param type the ComplexType which compositor(s) should be added to
     * @param compositor the sequence/choice/all to modify
     * @param schema the schema being built
     */
    private void addChoiceToSchema(Property property, TypeInfo typeInfo, ComplexType type, TypeDefParticle compositor, Schema schema) {
        Choice choice = new Choice();
        if (property.getGenericType() != null) {
            choice.setMaxOccurs(Occurs.UNBOUNDED);
        }
        ArrayList<Property> choiceProperties = (ArrayList<Property>) property.getChoiceProperties();
        addToSchemaType(typeInfo, choiceProperties, choice, type, schema);
        if (compositor instanceof Sequence) {
            ((Sequence) compositor).addChoice(choice);
        } else if (compositor instanceof Choice) {
            ((Choice) compositor).addChoice(choice);
        }
    }

    /**
     * Convenience method for processing a reference property. Required
     * schema components will be generated and set accordingly.
     *
     * @param property the choice property to be processed
     * @param compositor the sequence/choice/all to modify
     * @param schema the schema being built
     */
    private void addReferenceToSchema(Property property, Schema schema, TypeDefParticle compositor) {
        java.util.List<ElementDeclaration> referencedElements = property.getReferencedElements();
        if (referencedElements.size() == 1 && !property.isAny()) {
            // if only a single reference, just add the element.
            Element element = new Element();
            ElementDeclaration decl = referencedElements.get(0);
            String localName = decl.getElementName().getLocalPart();

            String prefix = getPrefixForNamespace(schema, decl.getElementName().getNamespaceURI());
            if (decl.getScopeClass() == GLOBAL.class){
                if (prefix == null || prefix.equals(EMPTY_STRING)) {
                    element.setRef(localName);
                } else {
                    element.setRef(prefix + COLON + localName);
                }
            } else {
                element.setType(getTypeName(property, decl.getJavaType(), schema));
                element.setName(localName);
            }
            if (property.getGenericType() != null) {
                element.setMinOccurs(Occurs.ZERO);
                element.setMaxOccurs(Occurs.UNBOUNDED);
            }else if(!property.isRequired()){
                element.setMinOccurs(Occurs.ZERO);
            }
            compositor.addElement(element);
        } else {
            // otherwise, add a choice of referenced elements.
            Choice choice = new Choice();
            if (property.getGenericType() != null) {
                choice.setMaxOccurs(Occurs.UNBOUNDED);
            }
            if (!property.isRequired()){
                choice.setMinOccurs(Occurs.ZERO);
            }
            for (ElementDeclaration elementDecl : referencedElements) {
                Element element = new Element();
                String localName = elementDecl.getElementName().getLocalPart();

                String prefix = getPrefixForNamespace(schema, elementDecl.getElementName().getNamespaceURI());
                if (elementDecl.getScopeClass() == GLOBAL.class){
                    if (prefix == null || prefix.equals(EMPTY_STRING)) {
                        element.setRef(localName);
                    } else {
                        element.setRef(prefix + COLON + localName);
                    }
                } else {
                    Schema referencedSchema = getSchemaForNamespace(elementDecl.getElementName().getNamespaceURI());
                    element.setType(getTypeName(property, elementDecl.getJavaType(), referencedSchema));
                    element.setName(localName);
                }
                choice.addElement(element);
            }
            // handle XmlAnyElement case
            if (property.isAny()) {
                addAnyToSchema(property, choice, false);
            }
            if (compositor instanceof Sequence) {
                ((Sequence) compositor).addChoice(choice);
            } else if (compositor instanceof Choice) {
                ((Choice) compositor).addChoice(choice);
            }
        }
    }

    /**
     * Convenience method for processing a reference property. Required
     * schema components will be generated and set accordingly.
     *
     * @param property the map property to be processed
     * @param element schema Element a new complex type will be added to
     * @param schema the schema being built
     * @param typeInfo the TypeInfo that the given property belongs to
     */
    private void addMapToSchema(Property property, Element element, Schema schema, TypeInfo typeInfo) {
        ComplexType entryComplexType = new ComplexType();
        Sequence entrySequence = new Sequence();

        Element keyElement = new Element();
        keyElement.setName(Property.DEFAULT_KEY_NAME);
        keyElement.setMinOccurs(Occurs.ZERO);

        JavaClass keyType = property.getKeyType();
        JavaClass valueType = property.getValueType();

        if (keyType == null) {
            keyType = helper.getJavaClass(Object.class);
        }

        if (valueType == null) {
            valueType = helper.getJavaClass(Object.class);
        }

        String typeName;
        QName keySchemaType = getSchemaTypeFor(keyType);
        if (keySchemaType != null) {
            TypeInfo targetInfo = this.typeInfo.get(keyType.getQualifiedName());
            if (targetInfo != null) {
                Schema keyElementSchema = this.getSchemaForNamespace(keySchemaType.getNamespaceURI());
                //add an import here
                addImportIfRequired(schema, keyElementSchema, keySchemaType.getNamespaceURI());
            }
            String prefix;
            if (keySchemaType.getNamespaceURI().equals(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI)) {
                prefix = Constants.SCHEMA_PREFIX;
            } else {
                prefix = getPrefixForNamespace(schema, keySchemaType.getNamespaceURI());
            }
            if (prefix != null && !prefix.equals(EMPTY_STRING)) {
                typeName = prefix + COLON + keySchemaType.getLocalPart();
            } else {
                typeName = keySchemaType.getLocalPart();
            }
            keyElement.setType(typeName);
        }

        entrySequence.addElement(keyElement);

        Element valueElement = new Element();
        valueElement.setName(Property.DEFAULT_VALUE_NAME);
        valueElement.setMinOccurs(Occurs.ZERO);
        QName valueSchemaType = getSchemaTypeFor(valueType);
        if (valueSchemaType != null) {
            TypeInfo targetInfo = this.typeInfo.get(valueType.getQualifiedName());
            if (targetInfo != null) {
                Schema valueElementSchema = this.getSchemaForNamespace(valueSchemaType.getNamespaceURI());
                //add an import here
                addImportIfRequired(schema, valueElementSchema, valueSchemaType.getNamespaceURI());
            }
            String prefix;
            if (valueSchemaType.getNamespaceURI().equals(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI)) {
                prefix = Constants.SCHEMA_PREFIX;
            } else {
                prefix = getPrefixForNamespace(schema, valueSchemaType.getNamespaceURI());
            }
            if (prefix != null && !prefix.equals(EMPTY_STRING)) {
                typeName = prefix + COLON + valueSchemaType.getLocalPart();
            } else {
                typeName = valueSchemaType.getLocalPart();
            }
            valueElement.setType(typeName);
        }

        entrySequence.addElement(valueElement);
        entryComplexType.setSequence(entrySequence);

        JavaClass descriptorClass = helper.getJavaClass(typeInfo.getDescriptor().getJavaClassName());
        JavaClass mapValueClass = helper.getJavaClass(MapValue.class);

        if (mapValueClass.isAssignableFrom(descriptorClass)) {
            element.setComplexType(entryComplexType);
            element.setMaxOccurs(Occurs.UNBOUNDED);
        } else {
            ComplexType complexType = new ComplexType();
            Sequence sequence = new Sequence();
            complexType.setSequence(sequence);

            Element entryElement = new Element();
            entryElement.setName(ENTRY);
            entryElement.setMinOccurs(Occurs.ZERO);
            entryElement.setMaxOccurs(Occurs.UNBOUNDED);
            sequence.addElement(entryElement);
            entryElement.setComplexType(entryComplexType);
            element.setComplexType(complexType);
        }
    }

    /**
     * Convenience method that adds an element ref to a given schema.
     *
     * @param schema the schema being built
     * @param compositor the sequence/choice/all the new reference will be added to
     * @param referencedElement the element being referenced
     * @param referencedElementURI the URI of the element being referenced
     */
    private void addElementRefToSchema(Schema schema, TypeDefParticle compositor, Element referencedElement, String referencedElementURI) {
        Element reference = new Element();
        reference.setMinOccurs(referencedElement.getMinOccurs());
        reference.setMaxOccurs(referencedElement.getMaxOccurs());
        Schema attributeSchema = this.getSchemaForNamespace(referencedElementURI);
        if (attributeSchema != null && attributeSchema.getTopLevelElements().get(referencedElement.getName()) == null) {
            // reset min/max occurs as they aren't applicable for global elements
            referencedElement.setMinOccurs(null);
            referencedElement.setMaxOccurs(null);
            // don't overwrite global elements; may have been defined by a type
            attributeSchema.getTopLevelElements().put(referencedElement.getName(), referencedElement);
        }
        String prefix = getPrefixForNamespace(schema, referencedElementURI);
        if (prefix == null) {
            reference.setRef(referencedElement.getName());
        } else {
            reference.setRef(prefix + COLON + referencedElement.getName());
        }
        // make sure a ref doesn't already exist before adding this one
        if (elementExistsInParticle(reference.getName(), reference.getRef(), compositor) == null) {
            compositor.addElement(reference);
        }
    }

    /**
     * Build an Element with name, type and possibly minOccurs set.  This method will
     * typically be called when processing an XPath that has no associated Property
     * that can be used to build an Element, such as in the case of XmlJoinNodes.
     *
     * @param elementName name of the Element
     * @param elementType type of the Element
     * @param isAll indicates if the Element will be added to an All structure
     * @return
     */
    private Element buildElement(String elementName, String elementType, boolean isAll) {
        Element element = new Element();
        // Set minOccurs based on the 'required' flag
        if (!(isAll)) {
            element.setMinOccurs(Occurs.ZERO);
        }
        element.setName(elementName);
        element.setType(elementType);
        return element;
    }

    /**
     * Build an Element based on a given Property.
     *
     * @param property the Property used to build the Element
     * @param isAll true if the Element will be added to an All structure
     * @param schema the schema currently being built
     * @param typeInfo the TypeInfo that owns the given Property
     * @return
     */
    private Element buildElement(Property property, boolean isAll, Schema schema, TypeInfo typeInfo) {
        Element element = new Element();

        // handle nillable
        if (property.shouldSetNillable()) {
            if (property.isNotNullAnnotated()) throw BeanValidationException.notNullAndNillable(property.getPropertyName());
            element.setNillable(true);
        }
        // handle defaultValue
        if (property.isSetDefaultValue()) {
            element.setDefaultValue(property.getDefaultValue());
        }
        // handle mime-type
        if (property.getMimeType() != null) {
            element.getAttributesMap().put(Constants.EXPECTED_CONTENT_TYPES_QNAME, property.getMimeType());
        }

        QName elementName = property.getSchemaName();
        String elementNamespace = elementName.getNamespaceURI();
        String lookupNamespace = schema.getTargetNamespace();
        if (lookupNamespace == null) {
            lookupNamespace = EMPTY_STRING;
        }
        NamespaceInfo namespaceInfo = getNamespaceInfoForNamespace(lookupNamespace, getPackageName(typeInfo));
        boolean isElementFormQualified = false;
        if (namespaceInfo != null) {
            isElementFormQualified = namespaceInfo.isElementFormQualified();
        }
        // handle element reference

        boolean addRef = shouldAddRefAndSetForm(element, elementNamespace, lookupNamespace, isElementFormQualified, true);

        if(addRef){
            schema = this.getSchemaForNamespace(elementNamespace);
        }

        JavaClass javaType = property.getActualType();
        element.setName(elementName.getLocalPart());
        String typeName = getTypeNameForComponent(property, schema, javaType, element, true);

        if (property.getGenericType() != null) {
            if (property.isXmlList()) {
                SimpleType localSimpleType = new SimpleType();
                org.eclipse.persistence.internal.oxm.schema.model.List list = new org.eclipse.persistence.internal.oxm.schema.model.List();
                list.setItemType(typeName);
                localSimpleType.setList(list);
                element.setSimpleType(localSimpleType);
            } else {
                element.setMaxOccurs(Occurs.UNBOUNDED);
                element.setType(typeName);
            }
            // handle map property
        } else if (property.isMap()) {
            addMapToSchema(property, element, schema, typeInfo);
        } else {
            element.setType(typeName);
        }

        // Set minOccurs based on the 'required' flag
        Integer minOccurs = property.getMinOccurs();
        if (minOccurs != null) {
            element.setMinOccurs(String.valueOf(minOccurs));
        } else {
            element.setMinOccurs(property.isRequired() ? Occurs.ONE : Occurs.ZERO);
        }
        // Overwrite maxOccurs if it has been explicitly set on property.
        Integer maxOccurs = property.getMaxOccurs();
        if (maxOccurs != null) element.setMaxOccurs(String.valueOf(maxOccurs));

        if (facets) {
            for (Facet facet : property.getFacets()) {
                processFacet(element, facet);
            }
        }
        return element;
    }

    private String getPackageName(TypeInfo typeInfo) {
        if (null != typeInfo && null != typeInfo.getDescriptor() && null != typeInfo.getDescriptor() && null != typeInfo.getDescriptor().getJavaClass() && null != typeInfo.getDescriptor().getJavaClass().getPackage()) {
            return typeInfo.getDescriptor().getJavaClass().getPackage().getName();
        }
        return null;
    }

    private void processFacet(Element element, Facet facet) {
        if (element.getSimpleType() == null) element.setSimpleType(new SimpleType());
        Restriction restriction = element.getSimpleType().getRestriction();
        if (restriction == null) {
            restriction = new Restriction(element.getType());
            element.getSimpleType().setRestriction(restriction);
        }
        element.setType(null); // Prevent error: "Cannot have both a 'type' attribute and an 'anonymous type' child".
        facet.accept(FacetVisitorHolder.VISITOR, restriction);
    }

    private static final class FacetVisitorHolder {
        private static final FacetVisitor<Void, Restriction> VISITOR = new FacetVisitor<Void, Restriction>() {
            public Void visit(DecimalMinFacet t, Restriction restriction) {
                if (t.isInclusive())    restriction.setMinInclusive(t.getValue());
                else                    restriction.setMinExclusive(t.getValue());
                return null;
            }

            public Void visit(DecimalMaxFacet t, Restriction restriction) {
                if (t.isInclusive())    restriction.setMaxInclusive(t.getValue());
                else                    restriction.setMaxExclusive(t.getValue());
                return null;
            }

            public Void visit(DigitsFacet t, Restriction restriction) {
                int fraction = t.getFraction();
                if (fraction > 0) {
                    restriction.setFractionDigits(fraction);
                    restriction.setTotalDigits(fraction + t.getInteger());
                } else if (fraction == 0) {
                    restriction.setTotalDigits(t.getInteger());
                }
                return null;
            }

            public Void visit(MaxFacet t, Restriction restriction) {
                restriction.setMaxInclusive(String.valueOf(t.getValue()));
                return null;
            }

            public Void visit(MinFacet t, Restriction restriction) {
                restriction.setMinInclusive(String.valueOf(t.getValue()));
                return null;
            }

            public Void visit(PatternFacet t, Restriction restriction) {
                String regex = t.getRegexp();
                regex = introduceShorthands(regex);
                restriction.setPattern(regex);
                return null;
            }

            public Void visit(PatternListFacet t, Restriction restriction) {
                for (PatternFacet pf : t.getPatterns()) {
                    String regex = pf.getRegexp();
                    regex = introduceShorthands(regex);
                    restriction.addPattern(regex);
                }
                return null;
            }

            public Void visit(SizeFacet t, Restriction restriction) {
                int minLength = t.getMin();
                int maxLength = t.getMax();
                if (minLength == maxLength) {
                    restriction.setLength(minLength);
                } else {
                    if (minLength > 0) restriction.setMinLength(minLength); // 0 is the default minBoundary.
                    if (maxLength < Integer.MAX_VALUE) restriction.setMaxLength(maxLength); // 2^31 is the default maxBoundary.
                }
                return null;
            }
        };
    }

    /**
     * Convenience method that adds an element to a given schema.
     *
     * @param element the Property that the Element will be based on
     * @param compositor the sequence/choice/all that the Element will be added to
     * @param schema the schema currently being built
     * @param compositor the TypeInfo that owns the given Property
     */
    private void addElementToSchema(Element element, String elementURI, boolean isPositional, TypeDefParticle compositor, Schema schema, String packageName) {
        String lookupNamespace = schema.getTargetNamespace();
        if (lookupNamespace == null) {
            lookupNamespace = EMPTY_STRING;
        }
        NamespaceInfo namespaceInfo = getNamespaceInfoForNamespace(lookupNamespace, packageName);
        boolean isElementFormQualified = false;
        if (namespaceInfo != null) {
            isElementFormQualified = namespaceInfo.isElementFormQualified();
        }
        boolean addRef = shouldAddRefAndSetForm(element, elementURI, lookupNamespace, isElementFormQualified, true);
        if(addRef){
            addElementRefToSchema(schema, compositor, element, elementURI);
        } else {
            // for positional mappings we could have multiple elements with same name; check before adding
            if (elementExistsInParticle(element.getName(), element.getRef(), compositor) == null) {
                if (isPositional) {
                    element.setMaxOccurs(Occurs.UNBOUNDED);
                }
                compositor.addElement(element);
            }
        }
    }

    /**
     * Convenience method that processes the XmlJoinNodes for a given Property and adds the
     * appropriate components to the schema.
     *
     * @param property the Property contianing one or more XmlJoinNode entries
     * @param compositor the sequence/choice/all that will be added to
     * @param schema the schema currently being built
     * @param type the complex type currently being built
     */
    private void addXmlJoinNodesToSchema(Property property, TypeDefParticle compositor, Schema schema, ComplexType type) {
        for (XmlJoinNode xmlJoinNode : property.getXmlJoinNodes().getXmlJoinNode()) {
            // create the XPathFragment(s) for the path
            Field xfld = new XMLField(xmlJoinNode.getXmlPath());
            xfld.setNamespaceResolver(schema.getNamespaceResolver());
            xfld.initialize();

            // build the schema components for the xml-path
            AddToSchemaResult asr = buildSchemaComponentsForXPath(xfld.getXPathFragment(), new AddToSchemaResult(compositor, schema), false, property);

            // process the last fragment
            TypeDefParticle currentParticle = asr.particle;
            Schema currentSchema = asr.schema;
            if (currentParticle.getOwner() instanceof ComplexType) {
                type = ((ComplexType) currentParticle.getOwner());
            }
            // get a QName for the last part of the xpath - this will be used as the
            // attribute/element name, and also to figure out if a ref is required
            QName schemaName;
            XPathFragment frag = xfld.getLastXPathFragment();
            boolean isAttribute = xmlJoinNode.getXmlPath().contains(ATT);
            // for non-attributes, the last fragment may be 'text()'
            if (!isAttribute) {
                if (frag.nameIsText()) {
                    frag = xfld.getXPathFragment();
                    while (frag.getNextFragment() != null && !frag.getNextFragment().nameIsText()) {
                        frag = frag.getNextFragment();
                    }
                }
            }
            schemaName = new QName(frag.getNamespaceURI(), frag.getLocalName());

            // handle Element/Attribute
            if (isAttribute) {
                addAttributeToSchema(buildAttribute(schemaName, Constants.SCHEMA_PREFIX + COLON + Constants.ANY_SIMPLE_TYPE), schemaName, currentSchema, type);
            } else {
                addElementToSchema(buildElement(schemaName.getLocalPart(), Constants.SCHEMA_PREFIX + COLON + Constants.ANY_SIMPLE_TYPE, currentParticle instanceof All), schemaName.getNamespaceURI(), false, currentParticle, currentSchema, null);
            }
        }
    }

    /**
     * Return the type name for an Element based on a given property.
     *
     * @param property the Property that the type name will be based on
     * @param schema the schema currently being built
     * @param javaClass the given Property's 'actual' type
     * @param sc the element being generated for the given Property
     * @return a type name based on the given Property, or null if not obtainable
     */
    private String getTypeNameForComponent(Property property, Schema schema, JavaClass javaClass, SimpleComponent sc, boolean isElement) {
        String typeName = null;
        if (property.isXmlId()) {
            // handle user-set schema-type
            if (property.getSchemaType() != null) {
                typeName = getTypeName(property, property.getActualType(), schema);
            } else {
                // default to xsd:ID
                typeName = Constants.SCHEMA_PREFIX + COLON + ID;
            }
        } else if (property.isXmlIdRef()) {
            typeName = Constants.SCHEMA_PREFIX + COLON + IDREF;
        } else {
            TypeInfo info = typeInfo.get(javaClass.getQualifiedName());
            if (info != null) {
                if (info.isComplexType()) {
                    typeName = info.getComplexType().getName();
                } else if (info.getSimpleType() != null) {
                    typeName = info.getSimpleType().getName();
                } else {
                    typeName = info.getSchemaTypeName();
                }
                if (typeName == null) {
                    // need to add complex-type locally, or reference global element
                    if(isElement && info.hasRootElement() && info.getXmlRootElement().getName().equals(sc.getName())){
                        String refName = info.getXmlRootElement().getName();
                        (sc).setRef(refName);
                    }else{
                        if (isElement && info.isComplexType()) {
                            ((Element)sc).setComplexType(info.getComplexType());
                        } else {
                            sc.setSimpleType(info.getSimpleType());
                        }
                    }
                } else {
                    // check to see if we need to add an import
                    if (addImportIfRequired(schema, info.getSchema(), info.getClassNamespace())) {
                        String prefix = schema.getNamespaceResolver().resolveNamespaceURI(info.getClassNamespace());
                        if (prefix != null && (!typeName.equals(EMPTY_STRING))) {
                            typeName = prefix + COLON + typeName;
                        }
                    }
                }
            } else if (!property.isMap()) {
                typeName = getTypeName(property, javaClass, schema);
            }
            // may need to qualify the type
            if (typeName != null && !typeName.contains(COLON)) {
                String prefix;
                if (info.getClassNamespace().equals(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI)) {
                    prefix = Constants.SCHEMA_PREFIX;
                } else {
                    prefix = getPrefixForNamespace(schema, info.getClassNamespace());
                }
                if (prefix != null) {
                    typeName = prefix + COLON + typeName;
                }
            }
        }
        return typeName;
    }
    private static String introduceShorthands(String regex) {
        return RegexMutator.mutate(regex);
    }

    /**
     * Maintains compatibility between Java Pattern Regex and XML Schema regex.
     * Replaces Java regexes with their respective XML Regex Shorthands, where applicable.
     * <p>
     * Recognized are Java regexes for the following XML Shorthands, and their negations:
     * <blockquote><pre>
     * \i - Matches any character that may be the first character of an XML name.
     *      "[_:A-Za-z]"
     * \c - Matches any character that may occur after the first character in an XML name.
     *      "[-.0-9:A-Z_a-z]"
     * \d - All digits.
     *      "\\p{Nd}"
     * \w - Word character.
     *      "[\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]]"
     * \s - Whitespace character.
     *      "[\\u0009-\\u000D\\u0020\\u0085\\u00A0\\u1680\\u180E\\u2000-\\u200A\\u2028\\u2029\\u202F\\u205F\\u3000]"
     * \b, \B - Boundary definitions.
     *      "(?:(?<=[\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]])(?![\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]])|(?<![\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]])(?=[\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]]))"
     *      "(?:(?<=[\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]])(?=[\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]])|(?<![\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]])(?![\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]]))"
     * \h - Horizontal whitespace character - Java does not support, changed in Java 8 though.
     *      "[\\u0009\\u0020\\u00A0\\u1680\\u180E\\u2000-\\u200A\\u202F\\u205F\\u3000]"
     * \v - Vertical whitespace character - Java translates the shorthand to \cK only, meaning changed in Java 8 though.
     *      "[\\u000A-\\u000D\\u0085\\u2028\\u2029]"
     * \X - Extended grapheme cluster.
     *      "(?:(?:\\u000D\\u000A)|(?:[\\u0E40\\u0E41\\u0E42\\u0E43\\u0E44\\u0EC0\\u0EC1\\u0EC2\\u0EC3\\u0EC4\\uAAB5\\uAAB6\\uAAB9\\uAABB\\uAABC]*(?:[\\u1100-\\u115F\\uA960-\\uA97C]+|([\\u1100-\\u115F\\uA960-\\uA97C]*((?:[[\\u1160-\\u11A2\\uD7B0-\\uD7C6][\\uAC00\\uAC1C\\uAC38]][\\u1160-\\u11A2\\uD7B0-\\uD7C6]*|[\\uAC01\\uAC02\\uAC03\\uAC04])[\\u11A8-\\u11F9\\uD7CB-\\uD7FB]*))|[\\u11A8-\\u11F9\\uD7CB-\\uD7FB]+|[^[\\p{Zl}\\p{Zp}\\p{Cc}\\p{Cf}&&[^\\u000D\\u000A\\u200C\\u200D]]\\u000D\\u000A])[[\\p{Mn}\\p{Me}\\u200C\\u200D\\u0488\\u0489\\u20DD\\u20DE\\u20DF\\u20E0\\u20E2\\u20E3\\u20E4\\uA670\\uA671\\uA672\\uFF9E\\uFF9F][\\p{Mc}\\u0E30\\u0E32\\u0E33\\u0E45\\u0EB0\\u0EB2\\u0EB3]]*)|(?s:.))"
     * \R - Carriage return.
     *      "(?:(?>\\u000D\\u000A)|[\\u000A\\u000B\\u000C\\u000D\\u0085\\u2028\\u2029])"
     * </pre></blockquote>
     *
     * CAUTION - ORDER SENSITIVE: Longer patterns should come first, because they may contain one of the shorter pattern.
     * <p>
     * Changes to this class should also be reflected in the opposite {@link org.eclipse.persistence.jaxb.plugins.BeanValidationPlugin.RegexMutator RegexMutator} class within XJC BeanValidation Plugin.
     *
     * @see <a href="http://stackoverflow.com/questions/4304928/unicode-equivalents-for-w-and-b-in-java-regular-expressions"/>tchrist's work</a>
     * @see <a href="http://www.regular-expressions.info/shorthand.html#xml">Special shorthands in XML Schema.</a>
     */
    private static final class RegexMutator {
        private static final Map<Pattern, String> shorthandReplacements = new HashMap<Pattern, String>(32) {{
            put(Pattern.compile("[:A-Z_a-z\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD]" , Pattern.LITERAL),"\\\\i");
            put(Pattern.compile("[^:A-Z_a-z\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD]" , Pattern.LITERAL),"\\\\I");
            put(Pattern.compile("[-.0-9:A-Z_a-z\\u00B7\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u203F\\u2040\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD]" , Pattern.LITERAL),"\\\\c");
            put(Pattern.compile("[^-.0-9:A-Z_a-z\\u00B7\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u203F\\u2040\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD]" , Pattern.LITERAL),"\\\\C");
            put(Pattern.compile("[\\u0009-\\u000D\\u0020\\u0085\\u00A0\\u1680\\u180E\\u2000-\\u200A\\u2028\\u2029\\u202F\\u205F\\u3000]" , Pattern.LITERAL),"\\\\s");
            put(Pattern.compile("[^\\u0009-\\u000D\\u0020\\u0085\\u00A0\\u1680\\u180E\\u2000-\\u200A\\u2028\\u2029\\u202F\\u205F\\u3000]" , Pattern.LITERAL),"\\\\S");
            put(Pattern.compile("[\\u000A-\\u000D\\u0085\\u2028\\u2029]" , Pattern.LITERAL),"\\\\v");
            put(Pattern.compile("[^\\u000A-\\u000D\\u0085\\u2028\\u2029]" , Pattern.LITERAL),"\\\\V");
            put(Pattern.compile("[\\u0009\\u0020\\u00A0\\u1680\\u180E\\u2000-\\u200A\\u202F\\u205F\\u3000]" , Pattern.LITERAL),"\\\\h");
            put(Pattern.compile("[^\\u0009\\u0020\\u00A0\\u1680\\u180E\\u2000\\u2001-\\u200A\\u202F\\u205F\\u3000]" , Pattern.LITERAL),"\\\\H");
            put(Pattern.compile("[\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]]" , Pattern.LITERAL),"\\\\w");
            put(Pattern.compile("[^\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]]" , Pattern.LITERAL),"\\\\W");
            put(Pattern.compile("(?:(?<=[\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]])(?![\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]])|(?<![\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]])(?=[\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]]))", Pattern.LITERAL),"\\\\b");
            put(Pattern.compile("(?:(?<=[\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]])(?=[\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]])|(?<![\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]])(?![\\pL\\pM\\p{Nd}\\p{Nl}\\p{Pc}[\\p{InEnclosedAlphanumerics}&&\\p{So}]]))", Pattern.LITERAL),"\\\\B");
            put(Pattern.compile("\\p{Nd}" , Pattern.LITERAL),"\\\\d");
            put(Pattern.compile("\\P{Nd}" , Pattern.LITERAL),"\\\\D");
            put(Pattern.compile("(?:(?>\\u000D\\u000A)|[\\u000A\\u000B\\u000C\\u000D\\u0085\\u2028\\u2029])" , Pattern.LITERAL),"\\\\R");
            put(Pattern.compile("(?:(?:\\u000D\\u000A)|(?:[\\u0E40\\u0E41\\u0E42\\u0E43\\u0E44\\u0EC0\\u0EC1\\u0EC2\\u0EC3\\u0EC4\\uAAB5\\uAAB6\\uAAB9\\uAABB\\uAABC]*(?:[\\u1100-\\u115F\\uA960-\\uA97C]+|([\\u1100-\\u115F\\uA960-\\uA97C]*((?:[[\\u1160-\\u11A2\\uD7B0-\\uD7C6][\\uAC00\\uAC1C\\uAC38]][\\u1160-\\u11A2\\uD7B0-\\uD7C6]*|[\\uAC01\\uAC02\\uAC03\\uAC04])[\\u11A8-\\u11F9\\uD7CB-\\uD7FB]*))|[\\u11A8-\\u11F9\\uD7CB-\\uD7FB]+|[^[\\p{Zl}\\p{Zp}\\p{Cc}\\p{Cf}&&[^\\u000D\\u000A\\u200C\\u200D]]\\u000D\\u000A])[[\\p{Mn}\\p{Me}\\u200C\\u200D\\u0488\\u0489\\u20DD\\u20DE\\u20DF\\u20E0\\u20E2\\u20E3\\u20E4\\uA670\\uA671\\uA672\\uFF9E\\uFF9F][\\p{Mc}\\u0E30\\u0E32\\u0E33\\u0E45\\u0EB0\\u0EB2\\u0EB3]]*)|(?s:.))" , Pattern.LITERAL),"\\\\X");
            put(Pattern.compile("[_:A-Za-z]" , Pattern.LITERAL),"\\\\i"); // ascii only
            put(Pattern.compile("[^:A-Z_a-z]" , Pattern.LITERAL),"\\\\I"); // ascii only
            put(Pattern.compile("[-.0-9:A-Z_a-z]" , Pattern.LITERAL),"\\\\c"); // ascii only
            put(Pattern.compile("[^-.0-9:A-Z_a-z]" , Pattern.LITERAL),"\\\\C"); // ascii only
        }};

        private RegexMutator() {
        }

        /**
         * @param input Java regex
         * @return XML regex
         */
        private static String mutate(String input){
            for (Map.Entry<Pattern, String> entry : shorthandReplacements.entrySet()) {
                Matcher m = entry.getKey().matcher(input);
                input = m.replaceAll(entry.getValue());
            }
            return input;
        }
    }
}
