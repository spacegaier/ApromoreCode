
package org.apromore.manager.model_portal;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReadEditSessionInputMsgType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReadEditSessionInputMsgType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="EditSessionCode" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReadEditSessionInputMsgType")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2011-07-19T05:06:01+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.2-27")
public class ReadEditSessionInputMsgType {

    @XmlAttribute(name = "EditSessionCode")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-07-19T05:06:01+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.2-27")
    protected Integer editSessionCode;

    /**
     * Gets the value of the editSessionCode property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-07-19T05:06:01+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.2-27")
    public Integer getEditSessionCode() {
        return editSessionCode;
    }

    /**
     * Sets the value of the editSessionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-07-19T05:06:01+10:00", comments = "JAXB RI vhudson-jaxb-ri-2.2-27")
    public void setEditSessionCode(Integer value) {
        this.editSessionCode = value;
    }

}