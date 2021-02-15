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

package nl.procura.gba.web.modules.bs.huwelijk.overzicht.form1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijkOptie;
import nl.procura.gba.web.services.bs.huwelijk.StatusVerbintenis;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;

public class HuwelijkOverzichtForm1 extends ReadOnlyForm {

  public HuwelijkOverzichtForm1(DossierHuwelijk dossierHuwelijk) {

    setReadonlyAsText(true);

    setColumnWidths("150px", "");

    init();

    HuwelijkOverzichtBean1 bean = new HuwelijkOverzichtBean1();

    bean.setSoort(
        dossierHuwelijk.getSoortVerbintenis().getOms() + " voornemen op "
            + dossierHuwelijk.getDatumVoornemen().getFormatDate());

    HuwelijksLocatie locatie = dossierHuwelijk.getHuwelijksLocatie();

    bean.setLocatie(locatie.getHuwelijksLocatie() + " (" + locatie.getLocatieSoort().getOms() + ")");
    bean.setLocatieDatumTijd(
        dossierHuwelijk.getDatumVerbintenis().getFormatDate() + " vanaf "
            + dossierHuwelijk.getTijdVerbintenis().getFormatTime(
                "HH:mm"));

    String status = dossierHuwelijk.getStatusVerbintenis().getOms();

    if (dossierHuwelijk.getStatusVerbintenis() == StatusVerbintenis.OPTIE) {
      status = setClass(false, status + (" tot " + dossierHuwelijk.getEinddatumStatus().getFormatDate()));
    }

    bean.setLocatieStatus(status);

    StringBuilder opties = new StringBuilder();

    for (DossierHuwelijkOptie opt : dossierHuwelijk.getOpties()) {

      String waarde = opt.getWaarde();

      switch (opt.getOptie().getOptieType()) {
        case BOOLEAN:
          waarde = isTru(waarde) ? "Ja" : "Nee";
          break;

        default:
          break;
      }
      opties.append(opt.getOptie().getHuwelijksLocatieOptie());
      opties.append(" = ").append(waarde).append(", ");
    }

    bean.setLocatieOpties(trim(opties.toString()));
    bean.setLocatieToelichting(dossierHuwelijk.getToelichtingVerbintenis());
    bean.setGetuigen(getGetuigen(dossierHuwelijk));
    bean.setGemeenteGetuigen(
        astr(pos(dossierHuwelijk.getGemeenteGetuigen()) ? dossierHuwelijk.getGemeenteGetuigen() : "Geen"));

    setBean(bean);
  }

  @Override
  public HuwelijkOverzichtBean1 getBean() {
    return (HuwelijkOverzichtBean1) super.getBean();
  }

  @Override
  public Object getNewBean() {
    return new HuwelijkOverzichtBean1();
  }

  protected void init() {
  }

  private String getGetuigen(DossierHuwelijk dossierHuwelijk) {

    int count = dossierHuwelijk.getVolledigIngevuldeGetuigen().size();
    int correct = dossierHuwelijk.getHuwelijksLocatie().getLocatieSoort().getAantalGetuigenMin();

    switch (count) {

      case 0:
        return setClass(false, "Geen getuigen ingevuld");

      case 1:
        return setClass(false, count + " van de " + correct + " benodigde getuigen is ingevuld");

      default:
        String msg = "Er zijn " + count + " getuigen ingevuld (min. " + correct + " is vereist)";
        return (count >= correct) ? msg : setClass(false, msg);
    }
  }
}
