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

import org.jasypt.util.text.AES256TextEncryptor;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.config.GbaConfigProperty;

public class Version1PasswordEncryptor implements PasswordEncryptor {

  private final AES256TextEncryptor encryptor;

  public Version1PasswordEncryptor() {
    this(GbaConfig.getRequired(GbaConfigProperty.ENCRYPTION_KEY1));
  }

  public Version1PasswordEncryptor(String password) {
    encryptor = new AES256TextEncryptor();
    encryptor.setPassword(password);
  }

  @Override
  public byte[] encrypt(String password) {
    return encryptor.encrypt(password).getBytes();
  }

  @Override
  public String decrypt(byte[] encrypted) {
    return encryptor.decrypt(new String(encrypted));
  }

  @Override
  public boolean matches(String raw, byte[] encrypted) {
    if (raw == null || encrypted == null) {
      return false;
    }
    String decrypted = decrypt(encrypted);
    return raw.equals(decrypted);
  }
}
