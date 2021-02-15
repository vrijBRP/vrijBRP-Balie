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

package nl.procura.gba.web.modules.zaken.identificatie.page1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.zaken.identificatie.page1.Page1IdentificatieBean3.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.rdw.processen.p0252.f08.CATRYBGEG;
import nl.procura.rdw.processen.p0252.f08.NATPRYBMAATR;
import nl.procura.rdw.processen.p0252.f08.UITGRYBGEG;
import nl.procura.standard.ProcuraDate;

public class Page1IdentificatieForm3 extends ReadOnlyForm {

  public Page1IdentificatieForm3(NATPRYBMAATR maatregelen) {

    setCaption("Rijbewijs");
    setOrder(NUMMER, GELDIG, DATUMVERLIESDIEFSTAL);
    setColumnWidths(WIDTH_130, "");

    Page1IdentificatieBean3 bean = new Page1IdentificatieBean3();

    for (UITGRYBGEG gegevens : maatregelen.getUitgrybtab().getUitgrybgeg()) {

      List<CATRYBGEG> categorieen = gegevens.getCatrybtab().getCatrybgeg();

      String datumEinde = date2str(
          categorieen.size() > 0 ? astr(along(categorieen.get(0).getEindgelddatc())) : "0");
      String datumVerlies = date2str(along(gegevens.getRybgeg().getVerldiefstdat()));

      bean.setNummer(astr(gegevens.getRybgeg().getRybnr()));

      if (emp(datumEinde)) {
        datumEinde = "Onbekend";
      } else {
        if (new ProcuraDate(datumEinde).isExpired()) {
          datumEinde = setClass(false, datumEinde);
        }
      }

      bean.setGeldig(datumEinde);
      bean.setDatumVerliesDiefstal(emp(datumVerlies) ? "Niet van toepassing" : setClass(false, datumVerlies));

      break;
    }

    setBean(bean);
  }

  @Override
  public Page1IdentificatieBean3 getBean() {
    return (Page1IdentificatieBean3) super.getBean();
  }
}
