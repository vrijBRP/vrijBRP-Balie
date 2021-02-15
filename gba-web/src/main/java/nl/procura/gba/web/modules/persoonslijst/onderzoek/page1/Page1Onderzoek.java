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

package nl.procura.gba.web.modules.persoonslijst.onderzoek.page1;

import nl.procura.diensten.gba.ple.extensions.formats.Onderzoek.OnderzoeksGeval;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.persoonslijst.overig.PlPage;

public class Page1Onderzoek extends PlPage {

  private final GbaTable table;

  public Page1Onderzoek() {

    super("Onderzoeksgegevens");

    table = new GbaTable() {

      @Override
      public void init() {

        super.init();

        setSelectable(false);
      }

      @Override
      public void setColumns() {

        addColumn("Ingang", 120);
        addColumn("Einde", 120);
        addColumn("Categorie", 150);
        addColumn("Groep");
        addColumn("Element");

        setSelectable(false);

        super.setColumns();
      }

      @Override
      public void setRecords() {

        for (OnderzoeksGeval geval : getPl().getPersoon().getOnderzoek().getCategoriesInOnderzoek()) {

          Record r = addRecord(geval);
          r.addValue(geval.getD_in());
          r.addValue(geval.getD_end());
          r.addValue(geval.getCat());
          r.addValue(geval.getGroup());
          r.addValue(geval.getElem());
        }
      }
    };

    addExpandComponent(table);
  }
}
