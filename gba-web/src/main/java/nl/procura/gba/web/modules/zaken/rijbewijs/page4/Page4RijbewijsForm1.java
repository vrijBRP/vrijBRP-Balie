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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page4.Page4RijbewijsBean1.*;
import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.components.containers.PlaatsContainer;
import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.gba.web.services.zaken.rijbewijs.NaamgebruikType;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsBurgStaat;
import nl.procura.rdw.processen.p0252.f08.ADRESNATPGEG;
import nl.procura.rdw.processen.p0252.f08.NATPERSOONGEG;
import nl.procura.rdw.processen.p0252.f08.NATPRYBMAATR;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page4RijbewijsForm1 extends RdwReadOnlyForm {

  Page4RijbewijsForm1(NATPRYBMAATR a) {

    initForm();

    setColumnWidths(WIDTH_130, "", "130px", "");

    Page4RijbewijsBean1 b = new Page4RijbewijsBean1();

    for (NATPERSOONGEG r : a.getNatpersoontab().getNatpersoongeg()) {

      Naamformats p = new Naamformats("", r.getGeslnaamechtg(), r.getVoorvoegechtg(), r.getAdelpredechtg(), "",
          null);

      Naamformats np = new Naamformats(r.getVoornaamnatp(), r.getGeslnaamnatp(), r.getVoorvoegnatp(),
          r.getAdelprednatp(),
          NaamgebruikType.getByRdwCode(along(r.getNaamgebrnatp())).getAfk(), p);

      String geboren = "";

      geboren += date2str(astr(r.getGebdatnatp()));

      PlaatsContainer plaatsen = new PlaatsContainer();

      if (fil(r.getGebplbuitenl())) {
        geboren += ", " + r.getGebplbuitenl();
      } else if (pos(r.getAutorcgebpl())) {
        geboren += ", " + plaatsen.get(astr(r.getAutorcgebpl()));
      }

      b.setNaam(np.getGesl_pred_init_nen_adel_voorv());
      b.setGeboren(geboren);
      b.setCrbsleutel(r.getNatperssl());
      b.setMaatregel(isTru(r.getMaatregelind()) ? "Ja" : "Nee");
      b.setBsn(astr(r.getFiscnrnatp()));
      b.setNationaliteit(r.getIsonatcodenp());
      b.setAnummer(astr(r.getGbanrnatp()));
      b.setTitel(r.getAdelprednatp());

      if (along(r.getNaamgebrnatp()) >= 0) {
        b.setNaamgebruik(r.getNaamgebrnatp() + ": " + NaamgebruikType.getByRdwCode(along(r.getNaamgebrnatp())));
      }

      b.setBurgstaat(r.getBurgstnatp() + ": " + RijbewijsBurgStaat.get(along(r.getBurgstnatp())).getOms());
      b.setPartner(p.getGesl_pred_init_nen_adel_voorv());
    }

    for (ADRESNATPGEG ad : a.getAdresnatptab().getAdresnatpgeg()) {
      String adres = ad.getStraatnatp() + " " + ad.getHuisnrnatp() + " " + ad.getHuistvnatp() + " " + ad.getPostcnnatp()
          + " " + ad.getPostcanatp() + " " + ad.getWoonplnatp();
      b.setAdres(adres);
    }

    setBean(b);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(PARTNER, AFGIFTE2, VERLIES2, ERKEND)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }

  protected void initForm() {
  }
}
