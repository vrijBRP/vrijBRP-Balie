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

package nl.procura.gba.web.jpa;

import org.junit.Assert;
import org.junit.Test;

import nl.procura.gba.jpa.personen.db.Aant3PK;
import nl.procura.gba.jpa.personen.db.Action;
import nl.procura.gba.web.services.beheer.profiel.actie.Actie;

public class JpaEqualsTest {

  @Test
  public void compareActionAndActie() {
    Action action = new Action();
    action.setAction("a");
    action.setActionType("type");

    Action action2 = new Action();
    action2.setAction("a");
    action2.setActionType("type");

    Actie actie = new Actie();
    actie.setAction("a");
    actie.setActionType("type");

    Actie actie2 = new Actie();
    actie2.setAction("a");
    actie2.setActionType("type");

    // Compare both ways
    Assert.assertTrue(actie.equals(action));
    Assert.assertTrue(action.equals(actie));

    Assert.assertTrue(action.equals(action2));
    Assert.assertTrue(action2.equals(action));

    Assert.assertTrue(actie.equals(actie2));
    Assert.assertTrue(actie2.equals(actie));
  }

  @Test
  public void comparePKEntity() {
    Aant3PK pk = new Aant3PK();
    pk.setAnr(123L);
    pk.setCAantek3(456);

    Aant3PK pk2 = new Aant3PK();
    pk2.setAnr(123L);
    pk2.setCAantek3(456);

    // Compare both ways
    Assert.assertTrue(pk.equals(pk2));
    Assert.assertTrue(pk2.equals(pk));
  }
}
