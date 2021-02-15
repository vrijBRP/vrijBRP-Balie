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

package nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijst;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieType;
import nl.procura.validation.Bsn;

public class RelationWindow extends GbaModalWindow {

  private final RelatieLijst relatieLijst;
  private RelatieSelect      selectionHandler;
  private BasePLExt          pl;

  public RelationWindow(PersonenWsService personWs, Bsn bsn, RelatieSelect selectionHandler) {
    super(true);
    setWidth("600px");

    this.selectionHandler = selectionHandler;
    this.pl = personWs.getPersoonslijst(bsn.getDefaultBsn());
    this.relatieLijst = personWs.getRelatieLijst(pl, false);

    setCaption("Gerelateerden van " + pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
    RelationTable relationTable = new RelationTable();
    addComponent(relationTable);
  }

  public class RelationTable extends GbaTable {

    @Override
    public void setColumns() {
      setSelectable(true);
      addColumn("Relatie", 110);
      addColumn("Persoon").setUseHTML(true);
      super.setColumns();
    }

    @Override
    public void setRecords() {
      for (Relatie relatie : relatieLijst.getSortedRelaties()) {
        if (!relatie.getPl().getPersoon().getStatus().isOverleden()) {
          Record record = addRecord(relatie);
          RelatieType relatieType = relatie.getRelatieType();
          record.addValue(RelatieType.AANGEVER == relatieType ? "Gezochte persoon" : relatieType.getOms());
          record.addValue(relatie.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
        }
      }
      super.setRecords();
    }

    @Override
    public void onClick(Record record) {
      selectionHandler.accept(pl, record.getObject(Relatie.class));
      super.onClick(record);
      close();
    }
  }

  public interface RelatieSelect {

    void accept(BasePLExt pl, Relatie relatie);
  }
}
