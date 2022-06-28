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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.commentaar;

import com.vaadin.ui.Embedded;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.CommentaarZaak;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaar;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.Fieldset;

public class ZaakCommentaarLayout extends GbaVerticalLayout {

  public ZaakCommentaarLayout(Zaak zaak) {
    addComponent(new Fieldset("Toelichting(en)", new CommentaarTabel(zaak)));
  }

  private class CommentaarTabel extends GbaTable {

    private final Zaak zaak;

    public CommentaarTabel(Zaak zaak) {
      this.zaak = zaak;
    }

    @Override
    public void attach() {
      getRecords().clear();
      super.attach();
    }

    @Override
    public int getPageLength() {
      return getRecords().isEmpty() ? super.getPageLength() : getRecords().size() + 1;
    }

    @Override
    public void setColumns() {

      addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

      addColumn("", 20).setClassType(Embedded.class);
      addColumn("Tekst");

      super.setColumns();
    }

    @Override
    public void setRecords() {
      if (getRecords().isEmpty()) {
        try {
          if (zaak instanceof CommentaarZaak) {
            for (ZaakCommentaar c : ((CommentaarZaak) zaak).getCommentaren().getCommentaren()) {
              if (c.getType().getVnr() > 0) {
                Record r = addRecord(c);
                r.addValue(TableImage.getByCommentaarIcon(c.getType()));
                r.addValue(c.getTekst());
              }
            }
          }
        } catch (Exception e) {
          getApplication().handleException(getWindow(), e);
        }

      }
    }
  }
}
