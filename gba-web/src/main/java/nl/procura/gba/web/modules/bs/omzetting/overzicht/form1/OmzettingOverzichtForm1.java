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

package nl.procura.gba.web.modules.bs.omzetting.overzicht.form1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.bs.omzetting.overzicht.form1.OmzettingOverzichtBean1.LOCATIETOELICHTING;
import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzettingOptie;
import nl.procura.gba.web.services.bs.omzetting.StatusVerbintenis;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class OmzettingOverzichtForm1 extends ReadOnlyForm {

  public OmzettingOverzichtForm1(DossierOmzetting dossierOmzetting) {

    setReadonlyAsText(true);

    setColumnWidths("150px", "");

    init();

    OmzettingOverzichtBean1 bean = new OmzettingOverzichtBean1();

    HuwelijksLocatie locatie = dossierOmzetting.getHuwelijksLocatie();
    bean.setLocatie(locatie.getHuwelijksLocatie() + " (" + locatie.getLocatieSoort().getOms() + ")");
    bean.setLocatieDatumTijd(
        dossierOmzetting.getDatumVerbintenis().getFormatDate() + " vanaf "
            + dossierOmzetting.getTijdVerbintenis().getFormatTime(
                "HH:mm"));

    String status = dossierOmzetting.getStatusVerbintenis().getOms();

    if (dossierOmzetting.getStatusVerbintenis() == StatusVerbintenis.OPTIE) {
      status = setClass(false, status + (" tot " + dossierOmzetting.getEinddatumStatus().getFormatDate()));
    }

    bean.setLocatieStatus(status);

    StringBuilder opties = new StringBuilder();

    for (DossierOmzettingOptie opt : dossierOmzetting.getOpties()) {

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
    bean.setLocatieToelichting(dossierOmzetting.getToelichtingVerbintenis());
    bean.setGetuigen(getGetuigen(dossierOmzetting));
    bean.setGemeenteGetuigen(
        astr(pos(dossierOmzetting.getGemeenteGetuigen()) ? dossierOmzetting.getGemeenteGetuigen() : "Geen"));

    setBean(bean);
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(LOCATIETOELICHTING)) {
      column.setColspan(3);
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public OmzettingOverzichtBean1 getBean() {
    return (OmzettingOverzichtBean1) super.getBean();
  }

  @Override
  public Object getNewBean() {
    return new OmzettingOverzichtBean1();
  }

  protected void init() {
  }

  private String getGetuigen(DossierOmzetting dossierOmzetting) {

    int count = dossierOmzetting.getVolledigIngevuldeGetuigen().size();

    switch (count) {

      case 0:
        return "Geen getuigen ingevuld";

      case 1:
        return "Er is 1 getuige ingevuld";

      default:
        return "Er zijn " + count + " getuigen ingevuld";
    }
  }
}
