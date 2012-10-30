package org.apromore.canoniser.utils;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apromore.anf.AnnotationType;
import org.apromore.canoniser.exception.CanoniserException;
import org.apromore.cpf.CanonicalProcessType;
import org.apromore.cpf.EdgeType;
import org.apromore.cpf.NetType;
import org.apromore.cpf.NodeType;
import org.apromore.cpf.ObjectFactory;
import org.apromore.cpf.ObjectType;
import org.apromore.cpf.ResourceTypeType;
import org.apromore.cpf.TaskType;
import org.apromore.cpf.TypeAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Utilities for working with CPF {@link TypeAttribute}s.
 *
 * @author <a href="mailto:felix.mannhardt@smail.wir.h-brs.de">Felix Mannhardt (Bonn-Rhein-Sieg University oAS)</a>
 * @author <a href="mailto:simon.raboczi@uqconnect.edu.au">Simon Raboczi</a>
 */
public final class ExtensionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionUtils.class);

    public static final String YAWLSCHEMA_URL = "http://www.yawlfoundation.org/yawlschema";

    /** Static library cannot be instantiated. */
    private ExtensionUtils() {
    }

    /**
     * @param obj  an XML {@link Node}
     * @param namespace  the namespace of an extension name
     * @param localPart  the local part of an extension name
     * @return whether the <code>obj</code>'s QName matches the given extension name
     */
    public static boolean isValidFragment(final Object obj, final String namespace, final String localPart) {
        return ((Node) obj).getNamespaceURI().equals(namespace) && ((Node) obj).getLocalName().equals(localPart);
    }

    /**
     * 'Unmarshal' a native object to its expected class.
     * Will throw an JAXBException if class is not known.
     * Note this method will also convert any non-matching Object!
     *
     * @param object
     *            returned of 'getAny' or similiar
     * @param expectedClass
     *            object of this class will be returned
     * @param nativeContext
     *            context for marshalling the origin format
     * @return JAXB Object matching expected class
     * @throws CanoniserException
     */
    public static <T> T unmarshalFragment(final Object      object,
                                          final Class<T>    expectedClass,
                                          final JAXBContext nativeContext) throws CanoniserException {
        try {
            if (nativeContext != null) {
                final Unmarshaller u = nativeContext.createUnmarshaller();
                final JAXBElement<T> jaxbElement = u.unmarshal((Node) object, expectedClass);
                return jaxbElement.getValue();
            } else {
                throw new CanoniserException("Missing JAXBContext");
            }
        } catch (final JAXBException e) {
            throw new CanoniserException("Failed to parse extension with expected class " + expectedClass.getName(), e);
        }
    }

    /**
     * 'Marshal' a JAXB object of some native schema to a DOM Node that can be added to 'xs:any'.
     *
     * @param elementName
     *            to use as local part
     * @param object
     *            to be marshaled
     * @param expectedClass
     *            class of the object
     * @param nativeNS
     *            Namespace URL for the origin format
     * @param nativeContext
     *            context for marshalling the origin format
     * @return XML Element containing the markup fragment
     * @throws CanoniserException
     */
    public static <T> Element marshalFragment(final String      elementName,
                                              final T           object,
                                              final Class<T>    expectedClass,
                                              final String      nativeNS,
                                              final JAXBContext nativeContext) throws CanoniserException {
        try {
            if (nativeContext != null) {
                final Marshaller m = nativeContext.createMarshaller();
                m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                final JAXBElement<T> element = new JAXBElement<T>(new QName(nativeNS, elementName), expectedClass, object);
                final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(false);
                Document doc;
                try {
                    doc = dbf.newDocumentBuilder().newDocument();
                } catch (final ParserConfigurationException e) {
                    throw new CanoniserException("Could not build document while marshalling fragment. This should never happen!", e);
                }
                m.marshal(element, doc);
                return doc.getDocumentElement();
            } else {
                throw new CanoniserException("Missing JAXBContext");
            }
        } catch (final JAXBException e) {
            throw new CanoniserException("Failed to add extension with name " + elementName, e);
        }
    }

    /**
     * Add the extension Element (XML) to the CPF Nodes attributes.
     *
     * @param extensionElement any XML Element
     * @param node CPF Node
     */
    public static void addToExtensions(final Element extensionElement, final NodeType node) {
        node.getAttribute().add(createExtension(extensionElement));
    }

    /**
     * Add the extension Element (XML) to the CPF attributes.
     *
     * @param extensionElement any XML Element
     * @param cpt CPF Process
     */
    public static void addToExtensions(final Element extensionElement, final CanonicalProcessType cpt) {
        cpt.getAttribute().add(createExtension(extensionElement));
    }

    /**
     * Add the extension Element (XML) to the CPF Edge attributes.
     *
     * @param extensionElement any XML Element
     * @param edge CPF Edge
     */
    public static void addToExtensions(final Element extensionElement, final EdgeType edge) {
        edge.getAttribute().add(createExtension(extensionElement));
    }

    /**
     * Add the extension Element (XML) to the CPF Net attributes.
     *
     * @param extensionElement any XML Element
     * @param net CPF Net
     */
    public static void addToExtensions(final Element extensionElement, final NetType net) {
        net.getAttribute().add(createExtension(extensionElement));
    }

    /**
     * Add the extension Element (XML) to the CPF Object attributes.
     *
     * @param extensionElement any XML Element
     * @param object CPF Object
     */
    public static void addToExtensions(final Element extensionElement, final ObjectType object) {
        object.getAttribute().add(createExtension(extensionElement));
    }

    /**
     * Add the extension Element (XML) to the CPF ResourceType attributes.
     *
     * @param extensionElement any XML Element
     * @param resourceType CPF resource
     */
    public static void addToExtensions(final Element extensionElement, final ResourceTypeType resourceType) {
        resourceType.getAttribute().add(createExtension(extensionElement));
    }

    private static TypeAttribute createExtension(final Element extensionElement) {
        final TypeAttribute attr = new ObjectFactory().createTypeAttribute();
        if (extensionElement.getNamespaceURI() != null) {
            attr.setName(extensionElement.getNamespaceURI() + "/" + extensionElement.getLocalName());
        } else {
            attr.setName(extensionElement.getLocalName());
        }
        attr.setAny(extensionElement);
        return attr;
    }

    /**
     * Get an extension attribute from a CPF Node.
     *
     * @param node
     *            any CPF node
     * @param name
     *            name of the extension
     * @return just the first TypeAttribute
     */
    public static TypeAttribute getExtensionAttribute(final NodeType node, final String name) {
        return getExtensionFromAttributes(node.getAttribute(), name);
    }

    /**
     * Get all matching extension attribute from a CPF Node.
     *
     * @param node
     *            any CPF node
     * @param name
     *            name of the extension
     * @return List of TypeAttribute
     */
    public static List<TypeAttribute> getExtensionAttributes(final NodeType node, final String name) {
        List<TypeAttribute> attrList = new ArrayList<TypeAttribute>();
        for (TypeAttribute attr :node.getAttribute()) {
            if (name.equals(attr.getName()) || (YAWLSCHEMA_URL + "/" + name).equals(attr.getName())) {
                attrList.add(attr);
            }
        }
        return attrList;
    }

    /**
     * Returns if the extension attribute is present in the list of attributes.
     *
     * @param attributes List of extension attributes
     * @param name of the extension attribute
     * @return true if found, false otherwise
     */
    public static boolean hasExtension(final List<TypeAttribute> attributes, final String name) {
        return getExtensionFromAttributes(attributes, name) != null;
    }

    private static TypeAttribute getExtensionFromAttributes(final List<TypeAttribute> attributeList, final String name) {
        for (TypeAttribute attr :attributeList) {
            if (name.equals(attr.getName()) || (YAWLSCHEMA_URL + "/" + name).equals(attr.getName())) {
                return attr;
            }
        }
        return null;
    }

    /**
     * Get an extension attribute from a CPF Node and unmarshals it to using the native namespace.
     *
     * @param node CPF Node
     * @param elementName of Extension
     * @param expectedClass from native schema
     * @param defaultValue if not found
     * @return Object of expectedClass
     */
    public static <T> T getFromNodeExtension(final NodeType node,
                                             final String elementName,
                                             final Class<T> expectedClass,
                                             final T defaultValue,
                                             final JAXBContext nativeContext) {

        return getFromExtension(node.getAttribute(), elementName, expectedClass, defaultValue, nativeContext);
    }

    /**
     * Get an extension attribute from a list of CPF attributes and unmarshals it to using the native namespace.
     *
     * @param attributes CPF list of attributes
     * @param elementName of Extension
     * @param expectedClass from native schema
     * @param defaultValue if not found
     * @return Object of expectedClass
     */
    public static <T> T getFromExtension(final List<TypeAttribute> attributes,
                                         final String elementName,
                                         final Class<T> expectedClass,
                                         final T defaultValue,
                                         final JAXBContext nativeContext) {

        TypeAttribute attr = getExtensionFromAttributes(attributes, elementName);
        if (attr != null && attr.getAny() != null) {
            try {
                return ExtensionUtils.unmarshalFragment(attr.getAny(), expectedClass, nativeContext);
            } catch (CanoniserException e) {
                LOGGER.warn("Could unmarshal fragment from extension!", e);
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Get an extension element from an AnnotationsType.
     *
     * @param annotation
     * @param elementName
     * @param expectedClass
     * @param defaultValue
     * @param nativeContext
     * @return an object of type T in any case
     */
    public static <T> T getFromAnnotationsExtension(final AnnotationType annotation,
                                                    final String         elementName,
                                                    final Class<T>       expectedClass,
                                                    final T              defaultValue,
                                                    final JAXBContext    nativeContext) {

        for (final Object extObj : annotation.getAny()) {
            try {
                if (ExtensionUtils.isValidFragment(extObj, ExtensionUtils.YAWLSCHEMA_URL, elementName)) {
                    return ExtensionUtils.unmarshalFragment(extObj, expectedClass, nativeContext);
                }
            } catch (final CanoniserException e) {
                LOGGER.warn("Could not convert extension {} with type {}", new String[] { elementName, expectedClass.getSimpleName() }, e);
            }
        }
        return defaultValue;
    }


}