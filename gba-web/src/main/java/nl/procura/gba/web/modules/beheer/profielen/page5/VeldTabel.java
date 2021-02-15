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

package nl.procura.gba.web.modules.beheer.profielen.page5;

import static java.util.Arrays.asList;
import static nl.procura.gba.common.MiscUtils.setClass;

import java.util.Collections;
import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.ProfielExtrasService;
import nl.procura.gba.web.services.beheer.profiel.veld.ProfielVeld;
import nl.procura.gba.web.services.beheer.profiel.veld.ProfielVeldType;
import nl.procura.gba.web.services.beheer.profiel.veld.Veld;

public class VeldTabel extends GbaTable {

  private static final int INDEX_STATUS    = 0;
  private ProfielVeldType  profielVeldType = null;
  private Profiel          profiel         = null;

  public VeldTabel(Profiel profiel, ProfielVeldType profielVeldType) {

    setProfielVeldType(profielVeldType);
    setProfiel(profiel);
  }

  public Profiel getProfiel() {
    return profiel;
  }

  public void setProfiel(Profiel profiel) {
    this.profiel = profiel;
  }

  public ProfielExtrasService getProfielExtras() {
    return getApplication().getServices().getProfielExtrasService();
  }

  public ProfielVeldType getProfielVeldType() {
    return profielVeldType;
  }

  public void setProfielVeldType(ProfielVeldType profielVeldType) {
    this.profielVeldType = profielVeldType;
  }

  @Override
  public void onDoubleClick(Record record) {

    Veld veld = (Veld) record.getObject();
    boolean isGekoppeld = profiel.isGekoppeld(veld);

    getApplication().getServices().getProfielService().koppelActie(asList(veld), asList(profiel),
        KoppelActie.get(!isGekoppeld));
    setRecordValue(record, INDEX_STATUS, geefStatus(!isGekoppeld));
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Status", 120).setUseHTML(true);
    addColumn("Veldsoort", 150);
    addColumn("Veld");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    List<ProfielVeld> veldList = ProfielVeld.getVelden();
    Collections.sort(veldList);

    for (ProfielVeld profielVeld : veldList) {

      if ((getProfielVeldType() != null) && (getProfielVeldType() != profielVeld.getVeld().getType())) {
        continue;
      }

      Veld enumVeld = profielVeld.getVeld();
      Veld saveVeld = new Veld(enumVeld.getType(),
          enumVeld.getField()); // geen veld uit enum ProfielVeld opslaan!

      boolean isGekoppeld = profiel.isGekoppeld(saveVeld);
      Record r = addRecord(saveVeld);

      r.addValue(geefStatus(isGekoppeld));
      r.addValue(saveVeld.getType().getType());
      r.addValue(saveVeld.getField());
    }
  }

  private String geefStatus(boolean b) {
    return b ? setClass("green", "Gekoppeld") : setClass("red", "Niet-gekoppeld");
  }
}
