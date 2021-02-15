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

package nl.procura.gba.web.modules.bs.common.pages.vereiste;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.isTru;

import java.util.Set;

import nl.procura.gba.web.components.layouts.table.GbaTable;

public class BsVereisteTable extends GbaTable {

  private final String                        id;
  private String                              dossierVereiste;
  private final Set<BurgerlijkeStandVereiste> set;

  public BsVereisteTable(String id, String dossierVereiste, Set<BurgerlijkeStandVereiste> set) {

    this.id = id;
    this.dossierVereiste = dossierVereiste;
    this.set = set;
  }

  public String getDossierVereiste() {
    return dossierVereiste;
  }

  public void setDossierVereiste(String dossierVereiste) {
    this.dossierVereiste = dossierVereiste;
  }

  @Override
  public void onClick(Record record) {

    getApplication().getParentWindow().addWindow(
        new VereisteDialog("Is er voldaan aan de voorwaarde?", (BurgerlijkeStandVereiste) record.getObject(),
            this));

    super.onClick(record);
  }

  @Override
  public void setColumns() {

    setClickable(true);

    addColumn("Voldaan", 120).setUseHTML(true);
    addColumn("Naam");
    addColumn("Bekende informatie", 150);
    addColumn("Toelichting", 250).setUseHTML(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (BurgerlijkeStandVereiste v : set) {

      if (v.getId().contains(id)) {

        Record r = addRecord(v);

        String overruled = "";

        v.setDossierVereiste(getDossierVereiste());

        if (v.isOverruled()) {
          overruled = " (overruled)";
        }

        if (emp(v.getVoldoet())) {
          r.addValue("Onbekend");
        } else {
          if (isTru(v.getVoldoet())) {
            r.addValue(setClass(true, "<b>Ja </b>") + overruled);
          } else {
            r.addValue(setClass(false, "<b>Nee </b>") + overruled);
          }
        }

        r.addValue(v.getNamen());
        r.addValue(v.getBekendeInformatie());
        r.addValue(v.getOverruleReason().replaceAll("\n", "<br/>"));
      }
    }

    super.setRecords();
  }
}
