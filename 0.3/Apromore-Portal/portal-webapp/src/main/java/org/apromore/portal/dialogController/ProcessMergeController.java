package org.apromore.portal.dialogController;

import org.apromore.manager.client.ManagerService;
import org.apromore.portal.common.Constants;
import org.apromore.portal.exception.ExceptionAllUsers;
import org.apromore.portal.exception.ExceptionDomains;
import org.apromore.model.ProcessSummaryType;
import org.apromore.model.VersionSummaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import java.util.List;
import java.util.Map;

public class ProcessMergeController extends BaseController {

    private static final long serialVersionUID = 1L;
    private MainController mainC;
    private MenuController menuC;
    private Window processMergeW;
    private Row removeEntR;
    private Row algoChoiceR;
    private Row buttonsR;
    private Listbox algosLB;
    private Checkbox removeEnt;
    private Row mergethreshold;
    private Row labelthreshold;
    private Row contextthreshold;
    private Row skipeweight;
    //	private Row subeweight;
    private Row skipnweight;
    private Row subnweight;
    private Row ownerR;
    private Row processNameR;
    private Row versionNameR;
    private Button OKbutton;
    private Button CancelButton;
    private Textbox processNameT;
    private Textbox versionNameT;
    private Row mergeDomainR;
    private SelectDynamicListController domainCB;

    private Map<ProcessSummaryType, List<VersionSummaryType>> selectedProcessVersions;


    public ProcessMergeController(MainController mainC, MenuController menuC,
                                  Map<ProcessSummaryType, List<VersionSummaryType>> selectedProcessVersions)
            throws SuspendNotAllowedException, InterruptedException, ExceptionAllUsers, ExceptionDomains {
        this.mainC = mainC;
        this.menuC = menuC;

        this.processMergeW = (Window) Executions.createComponents("macros/processmerge.zul", null, null);
        this.processMergeW.setTitle("Merge processes.");

        this.processNameR = (Row) this.processMergeW.getFellow("mergednamep");
        this.processNameT = (Textbox) processNameR.getFirstChild().getNextSibling();

        this.versionNameR = (Row) this.processMergeW.getFellow("mergednamev");
        this.versionNameT = (Textbox) versionNameR.getFirstChild().getNextSibling();
        this.versionNameT.setValue(Constants.INITIAL_VERSION);

        this.mergeDomainR = (Row) this.processMergeW.getFellow("mergeddomainR");
        List<String> domains = this.mainC.getDomains();
        this.domainCB = new SelectDynamicListController(domains);
        this.domainCB.setReference(domains);
        this.domainCB.setAutodrop(true);
        this.domainCB.setWidth("85%");
        this.domainCB.setHeight("100%");
        this.domainCB.setAttribute("hflex", "1");
        this.mergeDomainR.appendChild(domainCB);

        this.removeEntR = (Row) this.processMergeW.getFellow("removeEnt");
        this.algoChoiceR = (Row) this.processMergeW.getFellow("mergeAlgoChoice");
        this.buttonsR = (Row) this.processMergeW.getFellow("mergeButtons");

        this.OKbutton = (Button) this.processMergeW.getFellow("mergeOKButton");
        this.CancelButton = (Button) this.processMergeW.getFellow("mergeCancelButton");

        this.selectedProcessVersions = selectedProcessVersions;
        // get parameter rows
        this.removeEnt = (Checkbox) removeEntR.getFirstChild().getNextSibling();
        this.mergethreshold = (Row) this.processMergeW.getFellow("mergethreshold");
        this.labelthreshold = (Row) this.processMergeW.getFellow("labelthreshold");
        this.contextthreshold = (Row) this.processMergeW.getFellow("contextthreshold");
        this.skipeweight = (Row) this.processMergeW.getFellow("skipeweight");
        //		this.subeweight = (Row) this.processMergeW.getFellow("subeweight"); // TODO check which one is not used
        this.skipnweight = (Row) this.processMergeW.getFellow("skipnweight");
        this.subnweight = (Row) this.processMergeW.getFellow("subnweight");

        this.algosLB = (Listbox) this.algoChoiceR.getFirstChild().getNextSibling();
        // build the listbox to choose algo
        // greedy is the one by default
        Listitem listItem = new Listitem();
        listItem.setLabel("Greedy");
        this.algosLB.appendChild(listItem);
        listItem.setSelected(true);
        listItem = new Listitem();
        listItem.setLabel("Hungarian");
        this.algosLB.appendChild(listItem);

        updateActions();

        this.processNameT.addEventListener("onChange",
                new EventListener() {
                    public void onEvent(Event event) throws Exception {
                        updateActions();
                    }
                });
        this.versionNameT.addEventListener("onChange",
                new EventListener() {
                    public void onEvent(Event event) throws Exception {
                        updateActions();
                    }
                });
        this.algosLB.addEventListener("onSelect",
                new EventListener() {
                    public void onEvent(Event event) throws Exception {
                        updateActions();
                    }
                });
        this.OKbutton.addEventListener("onClick",
                new EventListener() {
                    public void onEvent(Event event) throws Exception {
                        mergeProcesses();
                    }
                });
        this.OKbutton.addEventListener("onOK",
                new EventListener() {
                    public void onEvent(Event event) throws Exception {
                        mergeProcesses();
                    }
                });
        this.CancelButton.addEventListener("onClick",
                new EventListener() {
                    public void onEvent(Event event) throws Exception {
                        cancel();
                    }
                });

        this.processMergeW.doModal();
    }

    protected void cancel() {
        this.processMergeW.detach();
    }

    protected void mergeProcesses() {
        String message = null;
        if ("".compareTo(this.processNameT.getValue()) != 0 &&
                "".compareTo(this.versionNameT.getValue()) != 0) {
            try {
                ProcessSummaryType result = getService().mergeProcesses(
                        selectedProcessVersions, this.processNameT.getValue(),
                        this.versionNameT.getValue(), this.domainCB.getValue(),
                        this.mainC.getCurrentUser().getUsername(), this.algosLB
                        .getSelectedItem().getLabel(), this.removeEnt
                        .isChecked(), ((Doublebox) this.mergethreshold
                        .getFirstChild().getNextSibling()).getValue(),
                        ((Doublebox) this.labelthreshold.getFirstChild()
                                .getNextSibling()).getValue(),
                        ((Doublebox) this.contextthreshold.getFirstChild()
                                .getNextSibling()).getValue(),
                        ((Doublebox) this.skipnweight.getFirstChild()
                                .getNextSibling()).getValue(),
                        ((Doublebox) this.subnweight.getFirstChild()
                                .getNextSibling()).getValue(),
                        ((Doublebox) this.skipeweight.getFirstChild()
                                .getNextSibling()).getValue());

                message = "Merge built one process.";
                mainC.displayNewProcess(result);
            } catch (Exception e) {
                message = "Merge failed (" + e.getMessage() + ")";
            }
            mainC.displayMessage(message);
            this.processMergeW.detach();
        }
    }

    protected void updateActions() {
        this.OKbutton.setDisabled("".compareTo(this.processNameT.getValue()) == 0 ||
                "".compareTo(this.versionNameT.getValue()) == 0);

        String algo = this.algosLB.getSelectedItem().getLabel();
        this.mergethreshold.setVisible(algo.compareTo("Hungarian") == 0 || algo.compareTo("Greedy") == 0);
        this.labelthreshold.setVisible(algo.compareTo("Hungarian") == 0 || algo.compareTo("Greedy") == 0);
        this.contextthreshold.setVisible(algo.compareTo("Hungarian") == 0 || algo.compareTo("Greedy") == 0);
        this.skipeweight.setVisible(algo.compareTo("Greedy") == 0);
        //		this.subeweight.setVisible(algo.compareTo("Greedy")==0);
        this.skipnweight.setVisible(algo.compareTo("Greedy") == 0);
        this.subnweight.setVisible(algo.compareTo("Greedy") == 0);
    }
}