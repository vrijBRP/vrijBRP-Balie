package nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2;

import static nl.procura.burgerzaken.vrsclient.api.VrsActieType.DEF_ONTREKK;
import static nl.procura.burgerzaken.vrsclient.api.VrsActieType.RECHTSW_OVERIG;
import static nl.procura.burgerzaken.vrsclient.api.VrsActieType.RECHTSW_VERM;
import static nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils.checkIdentificatieAkkoord;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.AANVRAAGNUMMER;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.AUT_VAN_VERSTREK;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.BSN;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.DOC_AFGIFTE;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.DOC_GELDIG_TOT;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.DOC_NR;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.DOC_SOORT;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.GEBOORTEDATUM;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.GEBOORTEPLAATS;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.GESLACHT;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.LENGTE;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.NAAM;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.NATIONALITEIT;
import static nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page2.Page2VrsBasisRegisterBean.STAATLOOS;
import static nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType.INHOUDING;
import static nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType.VAN_RECHTSWEGE_VERVALLEN;
import static nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType.VERMISSING;

import com.vaadin.ui.Button;
import java.util.List;
import nl.procura.burgerzaken.vrsclient.api.VrsActieType;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingType;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseMeldingDetailDto;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseStatusDto;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.dialogs.ZaakConfiguratieDialog;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.identificatie.IdentificatieWindow;
import nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3.Page3VrsBasisRegister;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentForm1;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieStatusListener;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2VrsBasisRegister extends NormalPageTemplate {

  private final BasePLExt                                                                       pl;
  private final ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse                response;
  private       List<ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseStatusDto> statussen;

  private final Button buttonActie1 = new Button("Definitieve onttrekking");
  private final Button buttonActie2 = new Button("Van rechtswege vervallen (vermissing)");
  private final Button buttonActie3 = new Button("Van rechtswege vervallen (andere reden)");

  public Page2VrsBasisRegister(BasePLExt pl,
      ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse response) {
    this.pl = pl;
    this.response = response;
  }

  @Override
  protected void initPage() {
    super.initPage();

    if (getNavigation().getPreviousPage() != null) {
      addButton(buttonPrev, 1F);
    }
    if (getServices().getReisdocumentService().getVrsService().isRegistratieMeldingEnabled()) {
      addButton(buttonActie1);
      addButton(buttonActie2);
      addButton(buttonActie3);
    }

    boolean heeftZaak = heeftZaak(pl, response.getReisdocument().getDocumentnummer());
    if (heeftZaak) {
      setInfo("Voor dit document is al een zaak aangemaakt.");
    }

    HLayout hLayout1 = new HLayout();
    hLayout1.setWidth("100%");
    hLayout1.add(new VrsDocumentForm1(response, "Reisdocument",
        DOC_NR, DOC_SOORT, DOC_AFGIFTE, DOC_GELDIG_TOT));
    hLayout1.add(new VrsDocumentForm1(response, "Personalisatiegegevens",
        LENGTE, STAATLOOS, AANVRAAGNUMMER));
    addComponent(hLayout1);

    HLayout hLayout2 = new HLayout();
    hLayout2.setWidth("100%");
    hLayout2.add(new VrsDocumentForm1(response, "Persoon",
        BSN, NAAM, GEBOORTEDATUM, GEBOORTEPLAATS, NATIONALITEIT, GESLACHT));
    hLayout2.add(new VrsDocumentForm1(response, "Autoriteiten", AUT_VAN_VERSTREK));
    addComponent(hLayout2);
    addComponent(new Fieldset("Statussen"));
    statussen = response.getStatussen();
    addExpandComponent(new Table());
  }

  private boolean heeftZaak(BasePLExt pl, String nummer) {
    return getServices().getDocumentInhoudingenService().getInhouding(pl, nummer) != null;
  }

  @Override
  public void event(PageEvent event) {
    getWindow().setWidth("1000px");
    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonActie1) {
      buttonActie(INHOUDING, DEF_ONTREKK);

    } else if (button == buttonActie2) {
      buttonActie(VERMISSING, RECHTSW_VERM);

    } else if (button == buttonActie3) {
      buttonActie(VAN_RECHTSWEGE_VERVALLEN, RECHTSW_OVERIG);
    }
    super.handleEvent(button, keyCode);
  }

  private void buttonActie(InhoudingType type, VrsActieType actieType) {
    IdentificatieStatusListener idSuccesListener = (saved, newAdded) -> continueButtonAction(type, actieType);
    checkIdentificatieAkkoord(getParentWindow(), new IdentificatieWindow(idSuccesListener), idSuccesListener);
  }

  private void continueButtonAction(InhoudingType type, VrsActieType actieType) {
    DocumentInhouding zaak = createNewOrExistingReisdocumentInhouding(pl);
    ZaakConfiguratieDialog.of(getApplication(), zaak, getServices(), () -> {
      String documentNr = response.getReisdocument().getDocumentnummer();
      zaak.setVrsMeldingType(VrsMeldingType.getByActieType(actieType).getCode());
      zaak.setVrsRedenType(VrsMeldingRedenType.getByActieType(actieType).get(0).getCode());
      zaak.setVrsOnlyBasisregister(!documentIsInBrp(documentNr));
      zaak.setInhoudingType(type);
      getNavigation().goToPage(new Page3VrsBasisRegister(actieType, zaak));
    });
  }

  private boolean documentIsInBrp(String documentNr) {
    return getServices().getDocumentInhoudingenService().getReisdocument(pl, documentNr) != null;
  }

  /**
   * Voegt een reisdocument inhouding toe
   */
  public DocumentInhouding createNewOrExistingReisdocumentInhouding(BasePLExt pl) {
    String documentnummer = response.getReisdocument().getDocumentnummer();
    String soort = response.getReisdocument().getDocumentsoort().getCode();
    DocumentInhouding inh = (DocumentInhouding) getServices().getDocumentInhoudingenService().getNewZaak();
    inh.setBasisPersoon(pl);
    inh.setBurgerServiceNummer(new BsnFieldValue(pl.getPersoon().getBsn().getVal()));
    inh.setAnummer(new AnrFieldValue(pl.getPersoon().getAnr().getVal()));
    inh.setNummerDocument(documentnummer);
    inh.setDocumentType(ReisdocumentType.get(soort));
    inh.setDatumTijdInvoer(new DateTime());
    inh.setDatumIngang(new DateTime());
    inh.setProcesVerbaalNummer("");
    inh.setProcesVerbaalOms("");
    inh.setSprakeVanRijbewijs(false);

    //Een ID opvragen in het Zaak-DMS
    getServices().getZaakIdentificatieService().getDmsZaakId(inh);

    return inh;
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  public class Table extends GbaTable {

    @Override
    public void setColumns() {
      setSelectable(true);
      addColumn("Datum / tijd", 150);
      addColumn("Status", 300);
      addColumn("Melding");
      super.setColumns();
    }

    @Override
    public void setRecords() {
      for (ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseStatusDto status : statussen) {
        Record r = addRecord(status);
        r.addValue(DateTime.of(status.getDocumentstatusStartdatum().toLocalDateTime()));
        r.addValue(status.getDocumentstatusOmschrijving());
        r.addValue(getMelding(status));
      }
      super.setRecords();
    }
  }

  private String getMelding(ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseStatusDto status) {
    return status.getMeldingen()
        .stream()
        .filter(melding -> melding.getMeldingOmschrijving() != null)
        .findFirst()
        .map(ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseMeldingDetailDto::getMeldingOmschrijving)
        .orElse("");
  }
}