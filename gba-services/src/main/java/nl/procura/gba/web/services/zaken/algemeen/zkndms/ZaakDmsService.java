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

package nl.procura.gba.web.services.zaken.algemeen.zkndms;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BSM_ZAKEN_DMS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BSM_ZAKEN_DMS_ZAAKTYPE;
import static nl.procura.standard.Globalfunctions.*;

import nl.procura.bsm.rest.v1_0.objecten.stuf.zkn0310.zsdms.*;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakDmsUtils;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatie;
import nl.procura.standard.exceptions.ProException;

public class ZaakDmsService extends AbstractService {

  private static final String ZKN0310_GENEREER_ZAAKID           = "zkn0310.genereerzaakid";
  private static final String ZKN0310_GEEF_ZAAKSTATUS           = "zkn0310.geefzaakstatus";
  private static final String ZKN0310_GEEF_LIJST_ZAAKDOCUMENTEN = "zkn0310.geeflijstzaakdocumenten";
  private static final String ZKN0310_GEEF_ZAAKDOCUMENT_LEZEN   = "zkn0310.geefzaakdocumentlezen";
  private static final String ZKN0310_GEEF_ZAAKDETAILS          = "zkn0310.geefzaakdetails";

  public ZaakDmsService() {
    super("Zaken-DMS");
  }

  /**
   * Zaaksysteem id opvragen bij de BSM en toevoegen aan de zaak
   */
  @ThrowException("Het is niet mogelijk om een zaak-id op te vragen")
  public String genereerZaakId(Zaak zaak) {
    String zaakId = "";
    if (isZakenDsmAan(zaak)) {
      String zaakType = getParm(BSM_ZAKEN_DMS_ZAAKTYPE, true);
      zaakId = zaak.getZaakHistorie().getIdentificaties().getNummer(zaakType);
      if (emp(zaakId)) {
        zaakId = genereerZaakId().getExternId();
        setZaaksysteemId(zaak, zaakId);
      }
    }

    return zaakId;
  }

  public void setZaaksysteemId(Zaak zaak, String zaaksysteemId) {
    String zaakType = getParm(BSM_ZAKEN_DMS_ZAAKTYPE, true);
    ZaakIdentificatie id = new ZaakIdentificatie();
    id.setInternId(zaak.getZaakId());
    id.setExternId(zaaksysteemId);
    id.setZaakType(zaak.getType());
    id.setType(zaakType);
    zaak.getZaakHistorie().getIdentificaties().getNummers().add(id);
  }

  /**
   * Zaaksysteem id opvragen bij de BSM
   */
  @ThrowException("Het is niet mogelijk om een zaaksysteem-id op te vragen")
  public ZaakIdentificatie genereerZaakId() {
    if (!isZakenDsmAan()) {
      throw new ProException("De zaaksysteem koppeling is niet ingesteld");
    }
    GenereerZaakIdVraagRestElement vraag = new GenereerZaakIdVraagRestElement();
    GenereerZaakIdAntwoordRestElement antwoord = new GenereerZaakIdAntwoordRestElement();
    String zaakType = getParm(BSM_ZAKEN_DMS_ZAAKTYPE, true);
    getServices().getBsmService().bsmQuery(ZKN0310_GENEREER_ZAAKID, vraag, antwoord);
    ZaakIdentificatie id = new ZaakIdentificatie();
    id.setExternId(antwoord.getZaakId());
    id.setType(zaakType);
    return id;
  }

  /**
   * Opvragen zaken DMS
   */
  @ThrowException("Het is niet mogelijk om de zaakdetails op te vragen")
  public GeefZaakDetailsAntwoordRestElement getZaakDetails(Zaak zaak) {
    GeefZaakDetailsAntwoordRestElement antwoord = new GeefZaakDetailsAntwoordRestElement();
    if (isZakenDsmAan(zaak)) {
      antwoord = getZaakDetails(ZaakUtils.getZaaksysteemId(zaak));
    }
    return antwoord;
  }

  @ThrowException("Het is niet mogelijk om zaakdetails op te vragen")
  public GeefZaakDetailsAntwoordRestElement getZaakDetails(String zaakId) {
    GeefZaakDetailsAntwoordRestElement antwoord = new GeefZaakDetailsAntwoordRestElement();
    GeefZaakDetailsVraagRestElement vraag = new GeefZaakDetailsVraagRestElement();
    if (fil(zaakId)) {
      vraag.setZaakIdentificatie(zaakId);
      getServices().getBsmService().bsmQuery(ZKN0310_GEEF_ZAAKDETAILS, vraag, antwoord);
    }
    return antwoord;
  }

  @ThrowException("Het is niet mogelijk om een zaakdocument op te vragen")
  public GeefZaakDocumentLezenAntwoordRestElement getZaakDocument(String documentIdentificatie) {

    GeefZaakDocumentLezenAntwoordRestElement antwoord = new GeefZaakDocumentLezenAntwoordRestElement();
    GeefZaakDocumentLezenVraagRestElement vraag = new GeefZaakDocumentLezenVraagRestElement();

    if (fil(documentIdentificatie)) {
      vraag.setZaakDocumentIdentificatie(documentIdentificatie);
      getServices().getBsmService().bsmQuery(ZKN0310_GEEF_ZAAKDOCUMENT_LEZEN, vraag, antwoord);
    }

    return antwoord;
  }

  @ThrowException("Het is niet mogelijk om de zaakdocumenten op te vragen")
  public GeefLijstZaakDocumentenAntwoordRestElement getZaakDocumenten(Zaak zaak) {

    GeefLijstZaakDocumentenAntwoordRestElement antwoord = new GeefLijstZaakDocumentenAntwoordRestElement();
    GeefLijstZaakDocumentenVraagRestElement vraag = new GeefLijstZaakDocumentenVraagRestElement();
    String nummer = ZaakUtils.getZaaksysteemId(zaak);

    if (fil(nummer)) {
      vraag.setZaakIdentificatie(nummer);
      if (isZakenDsmAan(zaak)) {
        getServices().getBsmService().bsmQuery(ZKN0310_GEEF_LIJST_ZAAKDOCUMENTEN, vraag, antwoord);
      }
    }

    return antwoord;
  }

  /**
   * Opvragen zaken DMS
   */
  @ThrowException("Het is niet mogelijk om de zaakstatussen op te vragen")
  public GeefZaakStatusAntwoordRestElement getZaakStatussen(Zaak zaak) {

    GeefZaakStatusAntwoordRestElement antwoord = new GeefZaakStatusAntwoordRestElement();
    GeefZaakStatusVraagRestElement vraag = new GeefZaakStatusVraagRestElement();
    String nummer = ZaakUtils.getZaaksysteemId(zaak);

    if (fil(nummer)) {
      vraag.setZaakIdentificatie(nummer);
      if (isZakenDsmAan(zaak)) {
        getServices().getBsmService().bsmQuery(ZKN0310_GEEF_ZAAKSTATUS, vraag, antwoord);
      }
    }

    return antwoord;
  }

  public boolean isZakenDsmAan() {
    return isTru(getParm(BSM_ZAKEN_DMS));
  }

  private boolean isZakenDsmAan(Zaak zaak) {
    boolean isZaakDms = ZaakDmsUtils.isDmsZaak(this, zaak);
    return isZakenDsmAan() && isZaakDms;
  }
}
