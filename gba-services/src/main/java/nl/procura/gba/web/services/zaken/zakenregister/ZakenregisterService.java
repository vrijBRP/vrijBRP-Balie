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

package nl.procura.gba.web.services.zaken.zakenregister;

import static ch.lambdaj.Lambda.join;
import static nl.procura.gba.common.ZaakStatusType.ONBEKEND;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKENREGISTER_STATUS;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.dao.views.ZakenAantalDao;
import nl.procura.gba.jpa.personen.db.views.ZakenAantalView;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.standard.ProcuraDate;

public class ZakenregisterService extends AbstractService {

  public ZakenregisterService() {
    super("Zakenregister");
  }

  public Set<ZaakStatusType> getNegeerStatussen() {

    Set<ZaakStatusType> statusses = new HashSet<>();
    String parm = getServices().getParameterService().getParm(ZAKENREGISTER_STATUS);

    if (fil(parm)) {
      for (String status : parm.split(",")) {
        ZaakStatusType zaakStatus = ZaakStatusType.get(Long.valueOf(status.trim()));
        statusses.add(zaakStatus);
      }
    }

    return statusses;
  }

  public List<ZakenAantalView> getZaakAantallen() {
    return ZakenAantalDao.getRealTimeAantallen();
  }

  public List<ZaakKey> getZaakIds(ZaakItem zaakItem) {
    return getServices().getZakenService().getZaakKeys(getZaakArgumenten(zaakItem, getNegeerStatussen()));
  }

  public void setNegeerStatusses(Collection<ZaakStatusType> zaakStatusses) {
    List<Long> codes = zaakStatusses.stream().map(ZaakStatusType::getCode).collect(Collectors.toList());
    getServices().getParameterService().saveParameter(ZAKENREGISTER_STATUS, join(codes), null, null, true);
  }

  public ZaakArgumenten getZaakArgumenten(ZaakItem zaakItem, Set<ZaakStatusType> negeerStatussen) {

    ZaakArgumenten za = new ZaakArgumenten();
    za.setSortering(zaakItem.getSortering());

    if (zaakItem.getTypes() != null) {
      za.setTypen(zaakItem.getTypes());
    }

    if ((zaakItem.getStatus() == null) || (zaakItem.getStatus() == ONBEKEND)) {
      za.setAll(true);
    } else {
      za.setStatussen(zaakItem.getStatus());
    }

    za.addNegeerStatussen(negeerStatussen.toArray(new ZaakStatusType[negeerStatussen.size()]));

    // Maximaal tot 1 jaar terug
    za.setdInvoerVanaf(along(new ProcuraDate().addYears(-1).getSystemDate()));

    return za;
  }
}
