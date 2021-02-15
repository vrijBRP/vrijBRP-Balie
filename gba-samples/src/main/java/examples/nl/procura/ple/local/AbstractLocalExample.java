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

package examples.nl.procura.ple.local;

import java.io.File;

import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpa;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaEclipseLink;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaManager;
import nl.procura.standard.Resource;

public abstract class AbstractLocalExample {

  private static final PLEJpa jpa = new PLEJpa();

  public AbstractLocalExample() {
    init();
  }

  public void init() {
    if (!jpa.isConnected()) {
      PLEJpaEclipseLink jpaImpl = new PLEJpaEclipseLink();
      jpaImpl.setProperyFile(new File(Resource.getURL("ple.properties").getFile()));

      jpa.setJpaImplementation(jpaImpl);
      jpa.connect("probev-jpa");
    }
  }

  public PLEJpaManager createManager() {
    return jpa.createManager();
  }
}
