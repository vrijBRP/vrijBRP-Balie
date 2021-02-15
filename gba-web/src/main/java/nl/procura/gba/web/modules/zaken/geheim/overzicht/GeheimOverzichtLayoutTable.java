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

package nl.procura.gba.web.modules.zaken.geheim.overzicht;

import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijstHandler;
import nl.procura.gba.web.services.zaken.geheim.GeheimAanvraag;
import nl.procura.gba.web.services.zaken.geheim.GeheimPersoon;

public class GeheimOverzichtLayoutTable extends GbaTable {

  private GeheimAanvraag aanvraag;

  public GeheimOverzichtLayoutTable(GeheimAanvraag aanvraag) {
    setAanvraag(aanvraag);
  }

  public GeheimAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(GeheimAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  @Override
  public void setColumns() {

    addColumn("Persoon");
    addColumn("Relatie", 100);
    addColumn("Geslacht", 100);
    addColumn("Geboren", 100);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    try {

      PLEArgs args = new PLEArgs();

      args.setDatasource(PLEDatasource.STANDAARD);

      for (GeheimPersoon p : getAanvraag().getPersonen()) {

        String anr = p.getAnummer().getStringValue();
        String bsn = p.getBurgerServiceNummer().getStringValue();
        args.addNummer(pos(anr) ? anr : bsn);
      }

      PersonenWsService gbaws = getApplication().getServices().getPersonenWsService();
      BasePLExt plAangever = gbaws.findPL(getAanvraag().getAnummerAangever(),
          getAanvraag().getBurgerServiceNummerAangever());

      for (BasePLExt pl : getApplication().getServices().getPersonenWsService().getPersoonslijsten(args,
          false).getBasisPLWrappers()) {

        Record r = addRecord(pl);
        r.addValue(pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
        r.addValue(RelatieLijstHandler.getRelatieType(plAangever, pl).getOms());
        r.addValue(pl.getPersoon().getGeslacht().getDescr());
        r.addValue(pl.getPersoon().getGeboorte().getDatumLeeftijd());
      }
    } catch (Exception e) {
      getApplication().handleException(e);
    }
  }
}
