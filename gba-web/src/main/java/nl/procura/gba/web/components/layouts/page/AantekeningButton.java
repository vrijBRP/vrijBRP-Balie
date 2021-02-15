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

package nl.procura.gba.web.components.layouts.page;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.TOON_AANTEKENING;
import static nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningStatus.OPEN;
import static nl.procura.standard.Globalfunctions.isTru;

import java.util.List;

import nl.procura.gba.web.modules.zaken.aantekening.AantekeningWindow;
import nl.procura.gba.web.services.gba.functies.PersoonslijstStatus;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.AantekeningService;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekening;

public class AantekeningButton extends MainButton<AantekeningService> {

  public AantekeningButton() {
    addStyleName("relaties");
  }

  @Override
  protected void doCheck() {

    if (getApplication() != null) {

      List<PlAantekening> aantekeningen = getAantekeningen();
      int count = aantekeningen.size();
      String label = getLabel(aantekeningen);

      setCaption(label);
      addStyleName(count > 0 ? "indicatie warn" : "indicatie no");
      setDescription("Deze persoon heeft " + count + " aantekening(en).");

      if (isTru(getApplication().getParmValue(TOON_AANTEKENING))) {

        PersonenWsService personenWs = getServices().getPersonenWsService();
        boolean isGetoond = PersoonslijstStatus.isGetoond(getServices(), personenWs.getHuidige(), "aant");

        if (!isGetoond) {
          if (getServices().getAantekeningService().getPersoonAantekeningen(OPEN).exists()) {
            onClick();
          }
          PersoonslijstStatus.setGetoond(getServices(), personenWs.getHuidige(), "aant");
        }
      }
    }
  }

  @Override
  protected String getListenerId() {
    return "aantekeningListener";
  }

  @Override
  protected AantekeningService getService() {
    return getServices().getAantekeningService();
  }

  @Override
  protected void onClick() {
    getWindow().addWindow(new AantekeningWindow());
  }

  private List<PlAantekening> getAantekeningen() {
    return getService().getPersoonAantekeningen(OPEN).getAantekeningen();
  }

  private String getLabel(List<PlAantekening> aantekeningen) {

    StringBuilder tekst = new StringBuilder();
    tekst.append("Aantekeningen");

    if (aantekeningen.size() > 1) {
      tekst.append(" (");
      tekst.append(aantekeningen.size());
      tekst.append(")");
      return tekst.toString();
    }

    return tekst.toString();
  }
}
