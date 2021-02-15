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

package nl.procura.gba.web.services.applicatie.onderhoud;

import static nl.procura.standard.Globalfunctions.isTru;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import nl.procura.commons.core.exceptions.ProException;

import lombok.Data;

@Data
public class SyncAttributes {

  private static final String USERS = "users";
  private boolean             syncUsers;

  public void load(String attributes) {

    if (attributes != null) {
      try {
        Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(attributes.getBytes()));
        setSyncUsers(isTru(properties.getProperty(USERS)));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public String save() {

    Properties properties = new Properties();
    properties.put(USERS, String.valueOf(syncUsers));
    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    try {
      properties.store(bos, null);
    } catch (IOException e) {
      throw new ProException("Probleem met opslaan", e);
    }
    return bos.toString();
  }

}
