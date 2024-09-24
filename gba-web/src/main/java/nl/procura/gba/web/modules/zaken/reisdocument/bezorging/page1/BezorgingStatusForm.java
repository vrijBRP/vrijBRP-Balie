/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1;

import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.AANGEMELD_AMP;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.GEANNULEERD_AMP;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.GEBLOKKEERD;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.GEKOPPELD;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.INGEKLAARD;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.INGEVOERD;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.UITGEREIKT_AMP;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.VERWERKT;

import com.vaadin.ui.Label;

import nl.procura.gba.jpa.personen.db.RdmAmp;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.Bezorging;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class BezorgingStatusForm extends VLayout {

  private final Bezorging bezorging;

  public BezorgingStatusForm(Bezorging bezorging) {
    this.bezorging = bezorging;
    setSizeFull();
    update();
  }

  public void update() {
    removeAllComponents();
    RdmAmp melding = bezorging.getMelding();
    TableLayout table = new TableLayout();
    table.addStyleName("v-form tableform v-form-error");
    table.setWidth("100%");
    table.setColumnWidths("");
    table.addData(getStatusLabel(1, melding.getDIn() > 0, INGEVOERD, "Nog niet ingevoerd in VrijBRP"));
    table.addData(getStatusLabel(2, melding.isIndVoormelding(), AANGEMELD_AMP, "Nog niet aangemeld bij leverancier"));
    table.addData(getStatusLabel(3, melding.isIndKoppeling(), GEKOPPELD, "Nog niet gekoppeld aan een order"));
    table.addData(getStatusLabel(4, melding.isIndInklaring(), INGEKLAARD, "Document nog niet ingeklaard"));
    table.addData(getStatusLabel(5, melding.isIndBlokkering(), GEBLOKKEERD, "Blokkering niet van toepassing"));

    if (melding.isIndAnnulering()) {
      table.addData(getStatusLabel(6, true, GEANNULEERD_AMP,
          "Annulering / uitreiking niet van toepassing"));
    } else {
      table.addData(getStatusLabel(6, melding.isIndUitreiking(), UITGEREIKT_AMP,
          "Annulering / uitreiking niet van toepassing"));
    }

    table.addData(getStatusLabel(7, melding.getDEnd() > 0, VERWERKT, "Nog niet afgesloten in VrijBRP"));
    add(table);
  }

  private Label getStatusLabel(Integer nr, Boolean value, BezorgingStatusType type, String negText) {
    if (value) {
      return new Label(String.format("<b>%d. %s</b>", nr, type.getOms()), Label.CONTENT_XHTML);
    }
    return new Label(String.format("%d. %s", nr, negText), Label.CONTENT_XHTML);
  }
}
