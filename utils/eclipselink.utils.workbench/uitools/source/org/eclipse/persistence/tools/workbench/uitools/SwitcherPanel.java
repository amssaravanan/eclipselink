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
package org.eclipse.persistence.tools.workbench.uitools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import org.eclipse.persistence.tools.workbench.uitools.app.PropertyValueModel;
import org.eclipse.persistence.tools.workbench.uitools.app.ValueModel;
import org.eclipse.persistence.tools.workbench.utility.Transformer;


/**
 * A <code>SwitcherPanel</code> will keeps its child component in synch
 * with a provided component holder. Alternatively, a value holder and a
 * transformer can be provided, and the panel will use the transformer to
 * convert the value into a component when necessary. If the component
 * to be displayed is null, nothing will be displayed.
 */
public class SwitcherPanel extends JPanel {

    /**
     * The value that drives which component will be displayed by the panel.
     */
    private PropertyValueModel valueHolder;

    /**
     * The transformer that will convert the value, above,
     * into a component to be displayed by the panel.
     */
    private Transformer transformer;

    /**
     * The listener that keeps us in synch with the value.
     */
    private PropertyChangeListener listener;


    /**
     * Creates a new <code>SwitcherPanel</code> with a <code>BorderLayout</code>.
     */
    private SwitcherPanel() {
        super(new BorderLayout());
        this.initialize();
    }

    /**
     * Creates a new <code>SwitcherPanel</code> that will display the
     * component generated by passing the specified value to the
     * specified transformer.
     *
     * @param valueHolder A <code>PropertyValueModel</code> holding the
     * value to be passed to the transformer to generate a component
     * to be displayed.
     * @param transformer A <code>Transformer</code> that converts
     * value into a component.
     */
    public SwitcherPanel(PropertyValueModel valueHolder, Transformer transformer) {
        this();
        this.initialize(valueHolder, transformer);
    }

    /**
     * Creates a new <code>SwitcherPanel</code> using the component
     * held by the specified component holder.
     *
     * @param componentHolder A <code>ValueModel</code> holding the
     * component to be displayed.
     */
    public SwitcherPanel(PropertyValueModel componentHolder) {
        this(componentHolder, Transformer.NULL_INSTANCE);
    }


    protected void initialize() {
        this.listener = this.buildListener();
    }

    /**
     * Creates the listener that will will keep us in sync with the value
     * holder.
     */
    protected PropertyChangeListener buildListener() {
        return new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                SwitcherPanel.this.valueChanged(e.getNewValue());
            }
            public String toString() {
                return "value listener";
            }
        };
    }

    protected void initialize(PropertyValueModel pvm, Transformer t) {
        if (pvm == null) {
            throw new NullPointerException();
        }
        this.valueHolder = pvm;
        this.valueHolder.addPropertyChangeListener(ValueModel.VALUE, this.listener);
        this.transformer = t;
        this.valueChanged(pvm.getValue());
    }

    /**
     * The value has changed, transform it into a component and display
     * the component.
     */
    protected void valueChanged(Object value) {
        Component oldComponent = null;
        Component newComponent = (Component) this.transformer.transform(value);

        // Remove the old component, if necessary
        if (this.getComponentCount() > 0) {
            oldComponent = this.getComponent(0);
            if (oldComponent == newComponent) {
                return;
            }
            this.remove(0);
        }

        if ((oldComponent == null) && (newComponent == null)) {
            return;
        }

        // Add the new pane, if necessary
        if (newComponent != null) {
            this.add(newComponent, BorderLayout.CENTER);
        }

        // Revalidate only if we are visible; otherwise, we will be
        // revalidated when we are displayed again
        if (this.isShowing()) {
            this.revalidate();
            this.repaint();
        }
    }

}
