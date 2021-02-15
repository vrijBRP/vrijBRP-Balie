/*
 * Copyright 2021 - 2022 Procura B.V.
 *
 * In licentie gegeven krachtens de EUPL, versie 1.2
 * U mag dit werk niet gebruiken, behalve onder de voorwaarden van de licentie.
 * U kunt een kopie van de licentie vinden op:
 *
 *   https://github.com/vrijBRP/vrijBRP/blob/master/LICENSE.md
 *
 * Deze bevat zowel de Nederlandse als de Engelse tekst
 *
 * Tenzij dit op grond van toepasselijk recht vereist is of schriftelijk
 * is overeengekomen, wordt software krachtens deze licentie verspreid
 * "zoals deze is", ZONDER ENIGE GARANTIES OF VOORWAARDEN, noch expliciet
 * noch impliciet.
 * Zie de licentie voor de specifieke bepalingen voor toestemmingen en
 * beperkingen op grond van de licentie.
 */

package nl.procura.gba.web.modules.zaken.common;

import static java.util.Arrays.asList;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.common.ZaakType.REISDOCUMENT;
import static nl.procura.gba.common.ZaakType.RIJBEWIJS;
import static nl.procura.standard.Globalfunctions.*;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.sms.SmsService;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.overlijden.DossierOverlijden;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakAfhaalbaar;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekening;
import nl.procura.gba.web.services.zaken.algemeen.goedkeuring.GoedkeuringZaak;
import nl.procura.sms.rest.domain.FindMessagesRequest;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class ZaakHeaderForm extends ReadOnlyForm {

  public static final String  SOORT          = "soort";
  public static final int     DRIE           = 3;
  public static final int     VIER           = 4;
  private static final String ZAAKTYPE       = "zaaktype";
  private static final String ID             = "id";
  private static final String STATUS         = "status";
  private static final String INGEVOERD      = "ingevoerd";
  private static final String INGANG         = "ingang";
  private static final String BRON           = "bron";
  private static final String LEVERANCIER    = "leverancier";
  private static final String AANGEVER       = "aangever";
  private static final String IDENTIFICATIE  = "identificatie";
  private static final String AFHAAL_LOCATIE = "afhaalLocatie";
  private static final String OPMERKINGEN    = "opmerkingen";
  private static final String COMMENTAAR     = "commentaar";
  private static final String GOEDKEURING    = "goedkeuring";
  private final Zaak          zaak;
  private final int           columns;

  public ZaakHeaderForm(Zaak zaak) {
    this(zaak, DRIE);
  }

  public ZaakHeaderForm(Zaak zaak, int columns) {
    this.columns = columns;
    this.zaak = zaak;
  }

  @Override
  public void attach() {

    if (getLayout().getColumns().isEmpty()) {

      setCaption(zaak.getType().getOms() + " - algemene zaakgegevens");

      List<String> order = new ArrayList<>();
      List<String> widths = new ArrayList<>();

      switch (columns) {
        case VIER:
          order.addAll(
              asList(ZAAKTYPE, STATUS, INGEVOERD, BRON, INGANG, LEVERANCIER, AANGEVER, ID, IDENTIFICATIE,
                  AFHAAL_LOCATIE, OPMERKINGEN, GOEDKEURING, COMMENTAAR));
          widths.addAll(asList("90px", "", "90px", "230px"));
          break;

        default: // Alleen voor uitreiken reisdocumenten
          order.addAll(asList(ZAAKTYPE, ID, STATUS, INGEVOERD, INGANG, BRON, IDENTIFICATIE, AFHAAL_LOCATIE,
              LEVERANCIER));
          widths.addAll(asList("90px", "", "80px", "170px", "80px", "140px"));
          break;
      }

      setOrder(order.toArray(new String[0]));
      setColumnWidths(widths.toArray(new String[0]));

      updateBean();
    }

    super.attach();
  }

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, Property property) {

    switch (columns) {
      case DRIE:
        if (property.is(OPMERKINGEN, COMMENTAAR)) {
          column.setColspan(5);
        }
        break;

      case VIER:
        break;

      default:
        if (property.is(INGEVOERD)) {
          column.setColspan(3);
        }
    }

    super.setColumn(column, field, property);
  }

  public void updateBean() {

    ZaakHeaderBean b = new ZaakHeaderBean();

    String soort = (fil(zaak.getSoort()) ? " (" + zaak.getSoort() + ")" : "");
    String gebr = (fil(
        zaak.getIngevoerdDoor().getDescription())
            ? (" door " + zaak.getIngevoerdDoor().getDescription() + ", " + zaak.getLocatieInvoer().getLocatie())
            : "");

    boolean saved = GenericDao.isSaved(zaak);

    String datumTijdInvoer = zaak.getDatumTijdInvoer().toString() + gebr;
    String datumIngang = pos(
        zaak.getDatumIngang().getLongDate()) ? zaak.getDatumIngang().toString() : "Onbekende datum";

    b.setZaaktype(zaak.getType().getOms() + soort);
    b.setStatus(ZaakUtils.getStatus(zaak.getStatus()));
    b.setIngevoerd(saved ? datumTijdInvoer : "Nog niet opgeslagen");
    b.setIngang(saved ? datumIngang : "Nog niet opgeslagen");

    b.setBron(zaak.getBron());
    b.setLeverancier(zaak.getLeverancier());
    b.setId(ZaakUtils.getRelevantZaakId(zaak));

    b.setAangever(getAangever());
    b.setIdentificatie(
        zaak.getIdentificatie() != null ? zaak.getIdentificatie().getOmschrijving() : "Niet vastgesteld");

    // Zet de afhaallocatie
    b.setAfhaalLocatie("N.v.t.");
    if (zaak instanceof ZaakAfhaalbaar) {
      Locatie locatie = ((ZaakAfhaalbaar) zaak).getLocatieAfhaal();
      if (locatie != null && pos(locatie.getCLocation())) {
        b.setAfhaalLocatie(locatie.getLocatie());
      }
    }

    b.addOpmerking(getAantekeningOpmerking(zaak));
    b.addOpmerking(getBijlageOpmerking(zaak));
    b.addOpmerking(getSms(zaak));
    b.addOpmerking(getRiskprofile(zaak));

    if (emp(b.getOpmerkingen())) {
      b.setOpmerkingen("Geen");
    }

    b.setGoedkeuring("N.v.t");
    if (zaak instanceof GoedkeuringZaak) {
      b.setGoedkeuring(((GoedkeuringZaak) zaak).getGoedkeuringsType().getOms());
    }

    setBean(b);
  }

  private String getAangever() {

    BsnFieldValue bsn = zaak.getBurgerServiceNummer();
    AnrFieldValue anr = zaak.getAnummer();

    String aangever = "";
    if (bsn != null && fil(bsn.getDescription())) {
      aangever = bsn.getDescription();
    } else if (anr != null && fil(anr.getDescription())) {
      aangever = anr.getDescription();
    }

    if (zaak.getBasisPersoon() != null && zaak.getBasisPersoon().getCat(
        GBACat.PERSOON).hasSets()) {
      String naam = zaak.getBasisPersoon().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
      aangever = naam + " (" + aangever + ")";
    }

    // Aangever overlijden kan leeg zijn
    if (emp(aangever)) {
      if (zaak instanceof Dossier) {
        Dossier dossier = (Dossier) zaak;
        if (dossier.getZaakDossier() instanceof DossierOverlijden) {
          DossierOverlijden zaakDossier = (DossierOverlijden) dossier.getZaakDossier();
          aangever = (zaakDossier.getAangever().getNaam().getNaam_naamgebruik_eerste_voornaam());
        }
      }
    }

    return fil(aangever) ? aangever : "Onbekend";
  }

  private String getAantekeningOpmerking(Zaak zaak) {
    List<PlAantekening> aantekeningen = zaak.getZaakHistorie().getAantekeningHistorie().getAantekeningen();
    StringBuilder tekst = new StringBuilder();
    if (!aantekeningen.isEmpty()) {
      tekst.append(aantekeningen.size() == 1 ? "1 aantekening" : aantekeningen.size() + " aantekeningen");
    }

    return trim(tekst.toString()).replaceAll("\n", "<br/>");
  }

  private String getBijlageOpmerking(Zaak zaak) {
    if (getApplication() != null) {
      int aantalBijlagen = getApplication().getServices().getDmsService().getAantalDocumenten(zaak);
      if (zaak.getType().isHeeftBijlagen() && aantalBijlagen == 0) {
        return setClass(false, "Deze zaak heeft geen bijlagen");
      }
    }

    return "";
  }

  private String getRiskprofile(Zaak zaak) {
    RiskAnalysisService service = Services.getInstance().getRiskAnalysisService();
    RiskAnalysisRelatedCase relatedCase = new RiskAnalysisRelatedCase(zaak);
    if (service.isApplicable(zaak) && !service.hasRiskAnalysisCase(relatedCase)) {
      return setClass(false, "Deze zaak heeft nog geen risicoanalyse");
    }
    return "";
  }

  private String getSms(Zaak zaak) {
    if (getApplication() != null && zaak.getType().is(REISDOCUMENT, RIJBEWIJS)) {
      SmsService smsService = getApplication().getServices().getSmsService();
      if (smsService.isSmsServiceActive()) {
        FindMessagesRequest request = new FindMessagesRequest();
        request.setZaakId(zaak.getZaakId());
        long count = smsService.getMessages(request).getTotalElements();
        if (count == 1) {
          return count + " SMS verstuurd";
        } else if (count > 1) {
          return count + " SMS berichten verstuurd";
        }
      }
    }

    return "";
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class ZaakHeaderBean implements Serializable {

    @Field(caption = "Zaak-type")
    private String zaaktype = "";

    @Field(caption = "Zaak-id")
    private String id = "";

    @Field(caption = "Status")
    private String status = "";

    @Field(caption = "Ingevoerd op")
    private String ingevoerd = "";

    @Field(caption = "Gaat in op")
    private String ingang = "";

    @Field(caption = "Bron")
    private String bron = "";

    @Field(caption = "Leverancier")
    private String leverancier = "";

    @Field(caption = "Aangever")
    private String aangever = "";

    @Field(caption = "Identificatie")
    private String identificatie = "";

    @Field(caption = "Afhaallocatie")
    private String afhaalLocatie = "";

    @Field(caption = "Opmerkingen")
    private String opmerkingen = "";

    @Field(caption = "Goedkeuring")
    private String goedkeuring = "";

    public void addOpmerking(String opmerking) {

      if (fil(opmerking)) {
        this.opmerkingen = (opmerkingen + opmerking) + "</br>";
      }
    }

    public String getAangever() {
      return aangever;
    }

    public void setAangever(String aangever) {
      this.aangever = aangever;
    }

    public String getAfhaalLocatie() {
      return afhaalLocatie;
    }

    public void setAfhaalLocatie(String afhaalLocatie) {
      this.afhaalLocatie = afhaalLocatie;
    }

    public String getBron() {
      return bron;
    }

    public void setBron(String bron) {
      this.bron = bron;
    }

    public String getGoedkeuring() {
      return goedkeuring;
    }

    public void setGoedkeuring(String goedkeuring) {
      this.goedkeuring = goedkeuring;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getIdentificatie() {
      return identificatie;
    }

    public void setIdentificatie(String identificatie) {
      this.identificatie = identificatie;
    }

    public String getIngang() {
      return ingang;
    }

    public void setIngang(String ingang) {
      this.ingang = ingang;
    }

    public String getIngevoerd() {
      return ingevoerd;
    }

    public void setIngevoerd(String ingevoerd) {
      this.ingevoerd = ingevoerd;
    }

    public String getLeverancier() {
      return leverancier;
    }

    public void setLeverancier(String leverancier) {
      this.leverancier = leverancier;
    }

    public String getOpmerkingen() {
      return opmerkingen;
    }

    public void setOpmerkingen(String opmerkingen) {
      this.opmerkingen = opmerkingen;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getZaaktype() {
      return zaaktype;
    }

    public void setZaaktype(String zaaktype) {
      this.zaaktype = zaaktype;
    }
  }
}
