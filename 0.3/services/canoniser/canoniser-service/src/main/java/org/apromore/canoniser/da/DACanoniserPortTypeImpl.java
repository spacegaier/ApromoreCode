
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package org.apromore.canoniser.da;

import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.9
 * Tue May 24 18:50:40 CEST 2011
 * Generated source version: 2.2.9
 * 
 */

@WebService(
                      serviceName = "DACanoniserService",
                      portName = "DACanoniser",
                      targetNamespace = "http://www.apromore.org/data_access/service_canoniser",
                      wsdlLocation = "http://localhost:8080/Apromore-dataAccess/services/DACanoniser?wsdl",
                      endpointInterface = "org.apromore.canoniser.da.DACanoniserPortType")
                      
public class DACanoniserPortTypeImpl implements DACanoniserPortType {

    private static final Logger LOG = Logger.getLogger(DACanoniserPortTypeImpl.class.getName());

    public org.apromore.canoniser.model_da.GetCpfUriOutputMsgType getCpfUri(org.apromore.canoniser.model_da.GetCpfUriInputMsgType payload) { 
        LOG.info("Executing operation getCpfUri");
        System.out.println(payload);
        try {
            org.apromore.canoniser.model_da.GetCpfUriOutputMsgType _return = new org.apromore.canoniser.model_da.GetCpfUriOutputMsgType();
            org.apromore.canoniser.model_da.ResultType _returnResult = new org.apromore.canoniser.model_da.ResultType();
            _returnResult.setMessage("Message1369857319");
            _returnResult.setCode(Integer.valueOf(934660484));
            _return.setResult(_returnResult);
            _return.setCpfURI("CpfURI-577431230");
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

	/* (non-Javadoc)
     * @see org.apromore.canoniser.da.DACanoniserPortType#storeNativeCpf(org.apromore.canoniser.model_da.StoreNativeCpfInputMsgType  payload )*
     */
    public org.apromore.canoniser.model_da.StoreNativeCpfOutputMsgType storeNativeCpf(org.apromore.canoniser.model_da.StoreNativeCpfInputMsgType payload) { 
        LOG.info("Executing operation storeNativeCpf");
        System.out.println(payload);
        try {
            org.apromore.canoniser.model_da.StoreNativeCpfOutputMsgType _return = new org.apromore.canoniser.model_da.StoreNativeCpfOutputMsgType();
            org.apromore.canoniser.model_da.ResultType _returnResult = new org.apromore.canoniser.model_da.ResultType();
            _returnResult.setMessage("Message1377099733");
            _returnResult.setCode(Integer.valueOf(447999944));
            _return.setResult(_returnResult);
            org.apromore.canoniser.model_da.ProcessSummaryType _returnProcessSummary = new org.apromore.canoniser.model_da.ProcessSummaryType();
            java.util.List<org.apromore.canoniser.model_da.VersionSummaryType> _returnProcessSummaryVersionSummaries = new java.util.ArrayList<org.apromore.canoniser.model_da.VersionSummaryType>();
            org.apromore.canoniser.model_da.VersionSummaryType _returnProcessSummaryVersionSummariesVal1 = new org.apromore.canoniser.model_da.VersionSummaryType();
            java.util.List<org.apromore.canoniser.model_da.AnnotationsType> _returnProcessSummaryVersionSummariesVal1Annotations = new java.util.ArrayList<org.apromore.canoniser.model_da.AnnotationsType>();
            _returnProcessSummaryVersionSummariesVal1.getAnnotations().addAll(_returnProcessSummaryVersionSummariesVal1Annotations);
            _returnProcessSummaryVersionSummariesVal1.setRanking("Ranking-955327073");
            _returnProcessSummaryVersionSummariesVal1.setName("Name1306392375");
            _returnProcessSummaryVersionSummariesVal1.setLastUpdate("LastUpdate-1014074862");
            _returnProcessSummaryVersionSummariesVal1.setCreationDate("CreationDate-1348952730");
            _returnProcessSummaryVersionSummaries.add(_returnProcessSummaryVersionSummariesVal1);
            _returnProcessSummary.getVersionSummaries().addAll(_returnProcessSummaryVersionSummaries);
            _returnProcessSummary.setOriginalNativeType("OriginalNativeType150642024");
            _returnProcessSummary.setName("Name1980919467");
            _returnProcessSummary.setId(Integer.valueOf(-1173657261));
            _returnProcessSummary.setDomain("Domain1434931575");
            _returnProcessSummary.setRanking("Ranking-1993608563");
            _returnProcessSummary.setLastVersion("LastVersion-1053584019");
            _returnProcessSummary.setOwner("Owner2140927542");
            _return.setProcessSummary(_returnProcessSummary);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see org.apromore.canoniser.da.DACanoniserPortType#storeVersion(org.apromore.canoniser.model_da.StoreVersionInputMsgType  payload )*
     */
    public org.apromore.canoniser.model_da.StoreVersionOutputMsgType storeVersion(org.apromore.canoniser.model_da.StoreVersionInputMsgType payload) { 
        LOG.info("Executing operation storeVersion");
        System.out.println(payload);
        try {
            org.apromore.canoniser.model_da.StoreVersionOutputMsgType _return = new org.apromore.canoniser.model_da.StoreVersionOutputMsgType();
            org.apromore.canoniser.model_da.ResultType _returnResult = new org.apromore.canoniser.model_da.ResultType();
            _returnResult.setMessage("Message-1925689646");
            _returnResult.setCode(Integer.valueOf(1998876740));
            _return.setResult(_returnResult);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see org.apromore.canoniser.da.DACanoniserPortType#storeNative(org.apromore.canoniser.model_da.StoreNativeInputMsgType  payload )*
     */
    public org.apromore.canoniser.model_da.StoreNativeOutputMsgType storeNative(org.apromore.canoniser.model_da.StoreNativeInputMsgType payload) { 
        LOG.info("Executing operation storeNative");
        System.out.println(payload);
        try {
            org.apromore.canoniser.model_da.StoreNativeOutputMsgType _return = new org.apromore.canoniser.model_da.StoreNativeOutputMsgType();
            org.apromore.canoniser.model_da.ResultType _returnResult = new org.apromore.canoniser.model_da.ResultType();
            _returnResult.setMessage("Message-1345137188");
            _returnResult.setCode(Integer.valueOf(-1129771627));
            _return.setResult(_returnResult);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see org.apromore.canoniser.da.DACanoniserPortType#writeAnnotation(org.apromore.canoniser.model_da.WriteAnnotationInputMsgType  payload )*
     */
    public org.apromore.canoniser.model_da.WriteAnnotationOutputMsgType writeAnnotation(org.apromore.canoniser.model_da.WriteAnnotationInputMsgType payload) { 
        LOG.info("Executing operation writeAnnotation");
        System.out.println(payload);
        try {
            org.apromore.canoniser.model_da.WriteAnnotationOutputMsgType _return = new org.apromore.canoniser.model_da.WriteAnnotationOutputMsgType();
            org.apromore.canoniser.model_da.ResultType _returnResult = new org.apromore.canoniser.model_da.ResultType();
            _returnResult.setMessage("Message158690791");
            _returnResult.setCode(Integer.valueOf(-730821791));
            _return.setResult(_returnResult);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}