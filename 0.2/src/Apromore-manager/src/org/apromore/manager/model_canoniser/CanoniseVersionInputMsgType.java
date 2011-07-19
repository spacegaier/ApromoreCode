
package org.apromore.manager.model_canoniser;

import javax.activation.DataHandler;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CanoniseVersionInputMsgType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CanoniseVersionInputMsgType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Native" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="EditSession" type="{http://www.apromore.org/canoniser/model_manager}EditSessionType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="EditSessionCode" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="Cpf_uri" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CanoniseVersionInputMsgType", propOrder = {
    "_native",
    "editSession"
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2011-05-24T06:38:44+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-2")
public class CanoniseVersionInputMsgType {

    @XmlElement(name = "Native", required = true)
    @XmlMimeType("application/octet-stream")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-05-24T06:38:44+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-2")
    protected DataHandler _native;
    @XmlElement(name = "EditSession", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-05-24T06:38:44+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-2")
    protected EditSessionType editSession;
    @XmlAttribute(name = "EditSessionCode")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-05-24T06:38:44+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-2")
    protected Integer editSessionCode;
    @XmlAttribute(name = "Cpf_uri")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-05-24T06:38:44+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-2")
    protected String cpfUri;

    /**
     * Gets the value of the native property.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-05-24T06:38:44+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-2")
    public DataHandler getNative() {
        return _native;
    }

    /**
     * Sets the value of the native property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-05-24T06:38:44+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-2")
    public void setNative(DataHandler value) {
        this._native = value;
    }

    /**
     * Gets the value of the editSession property.
     * 
     * @return
     *     possible object is
     *     {@link EditSessionType }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-05-24T06:38:44+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-2")
    public EditSessionType getEditSession() {
        return editSession;
    }

    /**
     * Sets the value of the editSession property.
     * 
     * @param value
     *     allowed object is
     *     {@link EditSessionType }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-05-24T06:38:44+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-2")
    public void setEditSession(EditSessionType value) {
        this.editSession = value;
    }

    /**
     * Gets the value of the editSessionCode property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-05-24T06:38:44+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-2")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-05-24T06:38:44+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-2")
    public void setEditSessionCode(Integer value) {
        this.editSessionCode = value;
    }

    /**
     * Gets the value of the cpfUri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-05-24T06:38:44+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-2")
    public String getCpfUri() {
        return cpfUri;
    }

    /**
     * Sets the value of the cpfUri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2011-05-24T06:38:44+02:00", comments = "JAXB RI vhudson-jaxb-ri-2.1-2")
    public void setCpfUri(String value) {
        this.cpfUri = value;
    }

}