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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab9.page2;

import java.util.function.Consumer;

import nl.procura.gba.jpa.personen.db.TranslationRec;
import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Tab9DocumentenPage3 extends DocumentenTabPage {

  private final TranslationRec           record;
  private final Consumer<TranslationRec> recordConsumer;

  private Tab9DocumentenPage3Form form = null;

  public Tab9DocumentenPage3(TranslationRec record, Consumer<TranslationRec> recordConsumer) {
    this.record = record;
    this.recordConsumer = recordConsumer;
    addButton(buttonSave);
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      form = new Tab9DocumentenPage3Form(record);
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onSave() {
    form.commit();
    Tab9DocumentenPage3Bean bean = form.getBean();
    record.setNl(bean.getNl());
    record.setFl(bean.getBuitenlands());
    recordConsumer.accept(record);
    successMessage("Gegevens zijn opgeslagen.");
  }
}
