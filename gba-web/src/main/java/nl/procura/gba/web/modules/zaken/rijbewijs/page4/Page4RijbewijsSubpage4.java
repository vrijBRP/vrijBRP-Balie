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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4;

import com.vaadin.ui.VerticalLayout;

import nl.procura.rdw.messages.P0252;
import nl.procura.rdw.processen.p0252.f08.NATPRYBMAATR;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class Page4RijbewijsSubpage4 extends VerticalLayout {

  public Page4RijbewijsSubpage4(Page4Rijbewijs page4rijbewijs, P0252 p0252f1, P0252 p0252f2) {

    setSpacing(true);

    NATPRYBMAATR a = (NATPRYBMAATR) p0252f1.getResponse().getObject();
    addComponent(new Fieldset("Categoriegegevens", new InfoLayout("Ter informatie",
        "Tabel met gegevens over de categorieën waarvoor een persoon rijvaardig en geschikt is bevonden.<br/> Deze categorieën behoeven NIET daadwerkelijk op een rijbewijs afgedrukt te worden.<br/> Klik op de regel voor meer informatie.")));
    addComponent(new Page4RijbewijsTable1(page4rijbewijs, a));
    addComponent(new Fieldset("Uitgegeven rijbewijsgegevens", new InfoLayout("Ter informatie",
        "Deze gegevens worden ook daadwerkelijk op het rijbewijs afgedrukt.")));
    addComponent(new Page4RijbewijsTable3(a, p0252f2));
  }
}
