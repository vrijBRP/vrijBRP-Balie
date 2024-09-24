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
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class TranslationRecordWindow extends GbaModalWindow {

  private final TranslationRec           record;
  private final Consumer<TranslationRec> consumer;

  public TranslationRecordWindow(TranslationRec record, Consumer<TranslationRec> consumer) {
    super("Vertaling (Escape om te sluiten)", "500px");
    this.record = record;
    this.consumer = consumer;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Tab9DocumentenPage3(record, record -> {
      consumer.accept(record);
      close();
    })));
  }
}
