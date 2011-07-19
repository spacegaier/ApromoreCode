
package org.apromore.toolbox.model_manager;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParameterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParameterType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParameterType", propOrder = {
    "name",
    "value"
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2011-07-19T05:13:15+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.2-27")
public class ParameterType {

    @XmlElement(name = "Name", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-07-19T05:13:15+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.2-27")
    protected String name;
    @XmlElement(name = "Value")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-07-19T05:13:15+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.2-27")
    protected double value;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-07-19T05:13:15+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.2-27")
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-07-19T05:13:15+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.2-27")
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the value property.
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-07-19T05:13:15+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.2-27")
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-07-19T05:13:15+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.2-27")
    public void setValue(double value) {
        this.value = value;
    }

}