/*******************************************************************************
 * Copyright (c) 2011, 2015 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Matt MacIvor - 2.5 - initial implementation
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.objectgraph;

public class PhoneNumberInh extends ContactInfo {

    String number;

    public boolean equals(Object obj) {
        PhoneNumberInh a = (PhoneNumberInh)obj;
        boolean equals = contactType == a.contactType || contactType.equals(a.contactType);
        equals = equals && number == a.number || number.equals(a.number);
        return equals;
    }
}
