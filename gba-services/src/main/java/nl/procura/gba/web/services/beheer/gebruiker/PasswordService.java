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

package nl.procura.gba.web.services.beheer.gebruiker;

import javax.inject.Singleton;

@Singleton
public class PasswordService {

  private PasswordEncryptor encryptor;

  public byte[] encryptPassword(String password) {
    return getEncryptor().encrypt(password);
  }

  public String getPassword(byte[] encrypted) {
    return getEncryptor().decrypt(encrypted);
  }

  public boolean match(String raw, byte[] encrypted) {
    return getEncryptor().matches(raw, encrypted);
  }

  private PasswordEncryptor getEncryptor() {
    if (encryptor == null) {
      encryptor = new Version1PasswordEncryptor();
    }
    return encryptor;
  }
}
