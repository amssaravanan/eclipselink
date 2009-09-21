/*******************************************************************************
* Copyright (c) 1998, 2009 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
* which accompanies this distribution.
* The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
* and the Eclipse Distribution License is available at
* http://www.eclipse.org/org/documents/edl-v10.php.
*
* Contributors:
* mmacivor - April 25/2008 - 1.0M8 - Initial implementation
******************************************************************************/
package org.eclipse.persistence.internal.jaxb;


import java.lang.reflect.Type;
import java.util.Map;
import java.util.HashMap;

/**
 * INTERNAL:
 * <p><b>Purpose:</b>Provide a ClassLoader implementation to allow the definition of ASM generated 
 * wrapper classes, and to lookup those classes when required.
 * <p><b>Responsibilities:</b><ul>
 * <li>Wrap the provided ClassLoader and pass method calls along</li>
 * <li>Maintain a map of generated wrapper classes and attempt lookups in that map if a class isn't found
 * in the nested loader</li>
 * <li>Provide a public API to define a class from an array of bytes and a class name</li></ul>
 * <p>This class is a ClassLoader implementation that maintains a map of wrapper classes generated during
 * JAXB generation. If a class is not found in the nested classLoader, then the map of generated classes
 * is checked.
 * @author mmacivor
 *
 */
public class JaxbClassLoader extends ClassLoader {

	private ClassLoader nestedClassLoader;
	private Map generatedClasses;
	
	public JaxbClassLoader(ClassLoader nestedClassLoader) {
		this.nestedClassLoader = nestedClassLoader;
		this.generatedClasses = new HashMap();
	}
	
	public JaxbClassLoader(ClassLoader nestedClassLoader, Class[] classes) {
		this.nestedClassLoader = nestedClassLoader;
		this.generatedClasses = new HashMap();
		if(classes != null){
			for(int i=0; i<classes.length; i++){
				Class nextClass = classes[i];
				generatedClasses.put(nextClass.getName(), nextClass);
			}
		}		
	}
	
	public JaxbClassLoader(ClassLoader nestedClassLoader, Type[] types) {
		this.nestedClassLoader = nestedClassLoader;
		this.generatedClasses = new HashMap();
		if(types != null){
			for(int i=0; i<types.length; i++){
				Type nextType = types[i];
				if (nextType instanceof Class) {
					generatedClasses.put(((Class)nextType).getName(), nextType);
				}
			}
		}
		
	}
	
    public Class loadClass(String className) throws ClassNotFoundException {
        Class javaClass = null;
        try {
            javaClass = nestedClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            javaClass = (Class)generatedClasses.get(className);
            if (javaClass != null) {
                return javaClass;
            }
            throw e;
        } catch (NoClassDefFoundError error) {
            javaClass = (Class)generatedClasses.get(className);
            if (javaClass == null) {
                throw error;
            }
        }
        return javaClass;
    }
    
    public Class generateClass(String className, byte[] bytes) {
    	Class theClass = this.defineClass(className, bytes, 0, bytes.length);
    	generatedClasses.put(className, theClass);
    	return theClass;
    }
}
