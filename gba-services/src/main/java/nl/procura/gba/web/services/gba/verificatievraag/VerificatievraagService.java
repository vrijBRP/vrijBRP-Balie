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

package nl.procura.gba.web.services.gba.verificatievraag;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VER_VRAAG_ENDPOINT;

import nl.ictu.bsn.AfzenderDE;
import nl.procura.bvbsn.BvBsnActionTemplate;
import nl.procura.bvbsn.actions.*;
import nl.procura.bvbsn.soap.BvBsnSoapHandler;
import nl.procura.gba.web.common.misc.GbaLogger;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.gba.templates.GbaTemplateService;

public class VerificatievraagService extends GbaTemplateService {

  public VerificatievraagService() {
    super("Bv-Bsn");
  }

  public ActionContrBsnIdenGeg getActionContrBsnIdenGeg(String bsn, IDGegevens idGegevens) {
    return new ActionContrBsnIdenGeg(getAfzender(), "1", bsn, idGegevens);
  }

  public ActionHaalOpIdenGeg getActionHaalOpIdenGeg(String bsn) {
    return new ActionHaalOpIdenGeg(getAfzender(), "1", bsn);
  }

  public ActionOpvrBsnIdenGeg getActionOpvrBsnIdenGeg(IDGegevens idGegevens) {
    return new ActionOpvrBsnIdenGeg(getAfzender(), "1", idGegevens);
  }

  public ActionVerificatieIdentiteitsDocument getActionVerificatieIdentiteitsDocument(String type, String waarde) {
    return new ActionVerificatieIdentiteitsDocument(getAfzender(), "1", type, waarde);
  }

  public ActionZoekNr getActionZoekNr(String bsn) {
    return new ActionZoekNr(getAfzender(), "1", bsn);
  }

  public String getCodeGemeente() {
    return getParm(ParameterConstant.GEMEENTE_CODES);
  }

  @ThrowException("Er is een fout opgetreden bij de verificatievraag")
  public VerificatieActie find(BvBsnActionTemplate actie) {
    String endpoint = getProxyUrl(VER_VRAAG_ENDPOINT, true);
    actie.setSoapHandler(new BvBsnSoapHandler(endpoint));

    try {
      actie.send();

      if (actie instanceof ActionZoekNr) {
        return new VerificatieActie("Zoek BSN", actie);
      } else if (actie instanceof ActionHaalOpIdenGeg) {
        return new VerificatieActie("Zoek ID", actie);
      } else if (actie instanceof ActionContrBsnIdenGeg) {
        return new VerificatieActie("Toets BSN / ID", actie);
      } else if (actie instanceof ActionOpvrBsnIdenGeg) {
        return new VerificatieActie("Opvragen BSN", actie);
      } else if (actie instanceof ActionVerificatieIdentiteitsDocument) {
        return new VerificatieActie("Toets document", actie);
      } else {
        return null;
      }
    } finally {
      GbaLogger.log("verificatievraag", actie.getSoapHandler());
    }
  }

  private AfzenderDE getAfzender() {

    AfzenderDE afzender = new AfzenderDE();
    afzender.setAfzender(getCodeGemeente());
    afzender.setBerichtNr("1");

    return afzender;
  }

  public class VerificatieActie {

    private final String              title;
    private final BvBsnActionTemplate actie;

    public VerificatieActie(String title, BvBsnActionTemplate actie) {
      super();
      this.title = title;
      this.actie = actie;
    }

    public BvBsnActionTemplate getActie() {
      return actie;
    }

    public String getTitle() {
      return title;
    }
  }
}
