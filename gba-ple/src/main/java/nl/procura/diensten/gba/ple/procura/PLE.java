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

package nl.procura.diensten.gba.ple.procura;

import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.templates.PLETemplateProcura;
import nl.procura.diensten.gba.ple.procura.templates.PersonsTemplate;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEArgsConverter;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaManager;
import org.eclipse.persistence.exceptions.DatabaseException;

import java.sql.SQLRecoverableException;

public class PLE extends PLETemplateProcura {

  public PLE(PLEJpaManager entityManager, PLEArgs argumenten) {
    setEntityManager(entityManager);
    setArguments(argumenten);
    init();
  }

  private void init() {
    try {
      PLEArgsConverter.convert(getEntityManager(), getArguments());
    } catch (Exception e) {
      handleNoConnection(e);
    }
  }

  public void find() {
    try {
      PersonsTemplate tPersonen = new PersonsTemplate();
      tPersonen.init(this);
      tPersonen.parse();
    } catch (Exception e) {
      handleNoConnection(e);
    }
  }

  private void handleNoConnection(Exception e) {
    e.printStackTrace();
    throw new RuntimeException("Er kan geen verbinding worden gemaakt met de database. Probeer het nogmaals.", e);
  }
}
