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

package nl.procura.gba.web.modules.zaken.verhuizing.page17;

import static nl.procura.gba.web.modules.zaken.verhuizing.page17.Page17VerhuizingBean1.*;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;

public class Page17VerhuizingForm1 extends ReadOnlyForm {

  public Page17VerhuizingForm1(VerhuisAanvraag v) {

    setCaption("Overzicht");
    setReadonlyAsText(false);

    Page17VerhuizingBean1 b = new Page17VerhuizingBean1();
    b.setSoortVerhuizing(v.getSoort());
    b.setHuidigAdres(v.getHuidigAdres().getAdres().getAdres_pc_wpl());

    switch (v.getTypeVerhuizing()) {

      case EMIGRATIE:
        b.setDuur(v.getEmigratie().getDuur());
        b.setNieuwAdres(v.getEmigratie().getAdres() + getDatumIngang(v));
        break;

      case HERVESTIGING:
        setOrder(SOORTVERHUIZING, LANDHERVESTIGING, NIEUWADRES, AANGEVER, TOELICHTING, TOESTEMMINGGEVER, DUUR,
            RECHTSFEITEN);
        b.setDuur(v.getHervestiging().getDuur());
        b.setLandHervestiging(v.getHervestiging().getLand().getDescription());
        b.setRechtsfeiten(v.getHervestiging().getRechtsfeiten());
        b.setNieuwAdres(v.getNieuwAdres().getAdres().getAdres_pc_wpl() + getDatumIngang(v));
        break;

      default:
        setOrder(SOORTVERHUIZING, HUIDIGADRES, NIEUWADRES, AANGEVER, TOELICHTING, TOESTEMMINGGEVER, DUUR);
        b.setNieuwAdres(v.getNieuwAdres().getAdres().getAdres_pc_wpl() + getDatumIngang(v));
    }

    b.setAangever(v.getAangever().toString());
    b.setToelichting(v.getAangever().getToelichting());
    b.setToestemminggever(
        fil(v.getToestemminggever().toString()) ? v.getToestemminggever().toString() : "Niet aangegeven");

    setBean(b);
  }

  @Override
  public Page17VerhuizingBean1 getBean() {
    return (Page17VerhuizingBean1) super.getBean();
  }

  private String getDatumIngang(VerhuisAanvraag v) {
    StringBuilder tekst = new StringBuilder();
    tekst.append(" (");
    tekst.append(v.getNieuwAdres().getFunctieAdres() == FunctieAdres.BRIEFADRES ? "Briefadres " : "");
    tekst.append("per ");
    tekst.append(v.getDatumIngang());
    tekst.append(")");
    return tekst.toString();
  }
}
