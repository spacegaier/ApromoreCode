package de.hbrs.oryx.yawl.converter.handler.oryx;

import java.util.Hashtable;

import org.jdom.Document;
import org.jdom.Element;
import org.json.JSONException;
import org.json.JSONObject;
import org.oryxeditor.server.diagram.basic.BasicShape;
import org.yawlfoundation.yawl.elements.YDecomposition;
import org.yawlfoundation.yawl.elements.YSpecification;
import org.yawlfoundation.yawl.elements.data.YParameter;
import org.yawlfoundation.yawl.elements.data.YVariable;
import org.yawlfoundation.yawl.logging.YLogPredicate;

import de.hbrs.oryx.yawl.converter.context.OryxConversionContext;
import de.hbrs.oryx.yawl.converter.exceptions.ConversionException;
import de.hbrs.oryx.yawl.util.YAWLUtils;

public abstract class OryxDecompositionHandler extends OryxShapeHandler {

	public OryxDecompositionHandler(OryxConversionContext context, BasicShape shape) {
		super(context, shape);
	}

	/**
	 * Populate the given YDecomposition instance, or use a already converted
	 * YDecomposition and dismiss the given instance.
	 * 
	 * @param decomposition
	 * @return YDecomposition
	 * @throws ConversionException
	 * @throws JSONException
	 */
	protected YDecomposition createDecomposition(YDecomposition decomposition) throws JSONException, ConversionException {
		final YSpecification specification = getContext().getSpecification();
		if (specification.getDecomposition(getDecompositionId()) == null) {

			decomposition.setCodelet(getShape().getProperty("decompositioncodelet"));
			if (getShape().hasProperty("decompositionname")) {
				decomposition.setName(getShape().getProperty("decompositionname").isEmpty() ? null : getShape().getProperty(
						"decompositionname"));
			}
			decomposition.setExternalInteraction(getShape().hasProperty("decompositionexternalinteraction") ? getShape().getProperty(
					"decompositionexternalinteraction").equals("manual") : false);
			decomposition.setLogPredicate(convertLogPredicate(getShape().getProperty("decompositionlogpredicate")));

			specification.setDecomposition(decomposition);
			return decomposition;
		} else {
			// Use existing, already converted Decomposition
			return specification.getDecomposition(getDecompositionId());
		}
	}

	protected boolean hasDecomposition() {
		if (getShape().hasProperty("decompositionid")) {
			return !getShape().getProperty("decompositionid").isEmpty();
		}
		return false;
	}

	protected String getDecompositionId() {
		return getShape().getProperty("decompositionid");
	}

	/**
	 * Converts the JSON to a YVariable, for example of a YNet
	 * 
	 * @param decomposition
	 *            parent of the YVariable
	 * @param jsonVariable
	 *            containing the information
	 * @return
	 * @throws JSONException
	 * @throws ConversionException
	 */
	protected YVariable convertVariable(YDecomposition decomposition, JSONObject jsonVariable) throws JSONException, ConversionException {
		YVariable yVariable = new YVariable(decomposition);
		initVariable(yVariable, jsonVariable);
		return yVariable;
	}

	/**
	 * Converts the JSON to a YParameter, for example of a YTask
	 * 
	 * @param decomposition
	 *            parent of the YParameter
	 * @param jsonParam
	 *            containing the information
	 * @return
	 * @throws JSONException
	 * @throws ConversionException
	 */
	protected YParameter convertParameter(YDecomposition decomposition, JSONObject jsonParam) throws JSONException, ConversionException {
		String type = jsonParam.getString("usage").equals("input") ? "inputParam" : "outputParam";
		YParameter yParameter = new YParameter(decomposition, type);
		initVariable(yParameter, jsonParam);
		return yParameter;
	}

	private void initVariable(YVariable yVariable, final JSONObject jsonVariable) throws JSONException, ConversionException {
		String name = jsonVariable.getString("name");
		String dataType = jsonVariable.getString("type");
		String initialValue = jsonVariable.has("initialvalue") ? jsonVariable.getString("initialvalue") : null;
		String namespace = jsonVariable.has("namespace") ? jsonVariable.getString("namespace") : "";
		yVariable.setDataTypeAndName(dataType, name, namespace);
		yVariable.setInitialValue(initialValue);
		yVariable.setMandatory(jsonVariable.has("ismandatory") ? jsonVariable.getBoolean("ismandatory") : false);
		yVariable.setAttributes(convertParameterAttributes(jsonVariable));
		yVariable.setLogPredicate(convertLogPredicate(jsonVariable.has("logpredicate") ? jsonVariable.getString("logpredicate") : ""));
	}

	private Hashtable<String, String> convertParameterAttributes(JSONObject jsonParam) throws ConversionException, JSONException {
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		if (jsonParam.has("attributes") && !jsonParam.getString("attributes").isEmpty()) {
			Document attributes = YAWLUtils.parseToElement(jsonParam.getString("attributes"));
			for (Object obj : attributes.getContent()) {
				if (obj instanceof Element) {
					Element element = (Element) obj;
					hashTable.put(element.getName(), element.getText());
				} else {
					getContext().addConversionWarnings("Attribute is not a JDOM Element", null);
				}
			}
		}
		return hashTable;
	}

	protected YLogPredicate convertLogPredicate(String logPredicate) throws JSONException, ConversionException {
		if (!logPredicate.isEmpty()) {
			return new YLogPredicate(YAWLUtils.parseToElement(logPredicate).getRootElement());
		} else {
			return new YLogPredicate();
		}
	}
}