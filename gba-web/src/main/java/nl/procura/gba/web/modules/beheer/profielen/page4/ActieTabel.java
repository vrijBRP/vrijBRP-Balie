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

package nl.procura.gba.web.modules.beheer.profielen.page4;

import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.ProfielExtrasService;
import nl.procura.gba.web.services.beheer.profiel.actie.Actie;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActieType;

public class ActieTabel extends GbaTable {

  private static final int INDEX_STATUS     = 0;
  private Profiel          profiel          = null;
  private ProfielActieType profielActieType = null;

  public ActieTabel(Profiel profiel, ProfielActieType profielActieType) {

    setProfiel(profiel);
    setProfielActieType(profielActieType);
  }

  public Profiel getProfiel() {
    return profiel;
  }

  public void setProfiel(Profiel profiel) {
    this.profiel = profiel;
  }

  public ProfielActieType getProfielActieType() {
    return profielActieType;
  }

  public void setProfielActieType(ProfielActieType profielActieType) {
    this.profielActieType = profielActieType;
  }

  public ProfielExtrasService getProfielExtras() {
    return getApplication().getServices().getProfielExtrasService();
  }

  @Override
  public void onDoubleClick(Record record) {

    Actie actie = (Actie) record.getObject();
    boolean isGekoppeld = profiel.isGekoppeld(actie);

    getApplication().getServices().getProfielService().koppelActie(asList(actie), asList(profiel),
        KoppelActie.get(!isGekoppeld));
    setRecordValue(record, INDEX_STATUS, KoppelActie.get(!isGekoppeld).getStatus());
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Status", 120).setUseHTML(true);
    addColumn("Actiesoort", 100);
    addColumn("Actie");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    List<ProfielActie> actieList = ProfielActie.getActies();

    Collections.sort(actieList);

    for (ProfielActie profielActie : actieList) {

      if ((getProfielActieType() != null) && (getProfielActieType() != profielActie.getActie().getType())) {
        continue;
      }

      Actie enumAction = profielActie.getActie();

      // we willen geen acties uit de enum ProfielActie opslaan!
      Actie saveAction = new Actie(enumAction.getType(), enumAction.getAction());

      boolean isGekoppeld = profiel.isGekoppeld(saveAction);
      Record r = addRecord(saveAction);

      r.addValue(KoppelActie.get(isGekoppeld).getStatus());
      r.addValue(saveAction.getType().getType());
      r.addValue(saveAction.getAction());
    }
  }
}
