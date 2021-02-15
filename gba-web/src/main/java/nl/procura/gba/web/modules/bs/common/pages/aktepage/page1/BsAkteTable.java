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

package nl.procura.gba.web.modules.bs.common.pages.aktepage.page1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ToonAktePersoonUtils;
import nl.procura.gba.web.theme.GbaWebTheme;

public class BsAkteTable extends GbaTable {

  final Dossier dossier;

  public BsAkteTable(Dossier dossier) {
    this.dossier = dossier;
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("Aktenummer");
    addColumn("Aktedatum", 100);
    addColumn("Type", 100);
    addColumn("Type persoon", 120);
    addColumn("Ingeschreven", 232);
    addColumn("Persoon", 400).setUseHTML(true);
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    super.setColumns();
  }

  @Override
  public void setPageLength(int pageLength) {
    super.setPageLength(getRecords().size() > 0 ? getRecords().size() : pageLength);
  }

  @Override
  public void setRecords() {

    for (DossierAkte akte : dossier.getAktes()) {

      Record r = addRecord(akte);

      String akteFormat = akte.getAkte();

      if (isSelectable()) {
        if (!akte.isCorrect()) {
          akteFormat = "< Klik om de gegevens in te voeren >";
        }
      } else {
        if (!akte.isCorrect()) {
          akteFormat = "Nog niet bepaald";
        }
      }

      r.addValue(akteFormat);
      r.addValue(akte.getDatumIngang().getFormatDate());
      r.addValue(dossier.getType());

      StringBuilder personen = new StringBuilder();
      StringBuilder persoonTypen = new StringBuilder();
      StringBuilder gemeenten = new StringBuilder();

      int i = 0;
      for (DossierPersoon person : BsPersoonUtils.sort(akte.getPersonen())) {
        if (ToonAktePersoonUtils.toon(dossier.getZaakDossier(), person)) {
          i++;

          String naam = person.getNaam().getPred_adel_voorv_gesl_voorn();
          personen.append(i + ". " + (fil(naam) ? naam : setClass(false, "nog niet aangegeven")) + "\n");
          persoonTypen.append(person.getDossierPersoonType() + "\n");
          String adres = person.getWoongemeente().getDescription();

          if (person.isRNI()) {
            adres = "RNI";
          } else if (!Landelijk.isNederland(person.getLand())) {
            adres = person.getLand().getDescription();
          }

          gemeenten.append((fil(adres) ? adres : "-") + "\n");
        }
      }

      r.addValue(persoonTypen.toString());
      r.addValue(gemeenten.toString());
      r.addValue(personen.toString());
    }

    super.setRecords();
  }
}
