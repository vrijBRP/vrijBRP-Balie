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

package nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.components.layouts.GbaFieldsetLayout;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;

public class BsNatioLayout extends GbaFieldsetLayout implements ClickListener {

  private final OptieLayout optieLayout     = new OptieLayout();
  private final Button      buttonAanpassen = new Button("Aanpassen");

  private final BsNatioType type;
  private final Dossier     dossier;
  private BsNatioTable      table;

  public BsNatioLayout(BsNatioType type, Dossier dossier, String header) {

    super(header);

    this.type = type;
    this.dossier = dossier;

    table = new BsNatioTable(dossier) {

      @Override
      public void setColumns() {

        addColumn("Nationaliteit");
        addColumn("Sinds");
        addColumn("Reden");
      }
    };

    optieLayout.getLeft().addComponent(table);
    optieLayout.getRight().addButton(buttonAanpassen, this);
    optieLayout.getRight().setWidth("150px");

    addComponent(optieLayout);
  }

  @Override
  public void buttonClick(ClickEvent event) {

    if (event.getButton() == buttonAanpassen) {

      String msg = "Vul minimaal één nationaliteit in. Dit is belangrijk voor het verdere verloop van het proces.";

      getApplication().getParentWindow().addWindow(new BsNatioWindow(type, dossier, msg) {

        @Override
        public void add(DossierNationaliteit natio) {
          toevoegenNationaliteit(natio);
        }

        @Override
        public void closeWindow() {
          table.init();
          super.closeWindow();
        }

        @Override
        public void delete(DossierNationaliteit natio) {
          verwijderNationaliteit(natio);
        }
      });
    }
  }

  public BsNatioTable getTable() {
    return table;
  }

  public void setTable(BsNatioTable table) {
    this.table = table;
  }

  @SuppressWarnings("unused")
  public void toevoegenNationaliteit(DossierNationaliteit nationaliteit) {
  } // Override

  @SuppressWarnings("unused")
  public void verwijderNationaliteit(DossierNationaliteit nationaliteit) {
  } // Override
}
