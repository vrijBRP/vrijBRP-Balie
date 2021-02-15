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

import static java.nio.charset.StandardCharsets.UTF_8;

import org.jasypt.util.binary.BasicBinaryEncryptor;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.config.GbaConfigProperty;

public class Version0PasswordEncryptor implements PasswordEncryptor {

  private final BasicBinaryEncryptor encryptor;

  public Version0PasswordEncryptor() {
    encryptor = new BasicBinaryEncryptor();
    encryptor.setPassword(GbaConfig.getRequired(GbaConfigProperty.ENCRYPTION_KEY0));
  }

  @Override
  public byte[] encrypt(String password) {
    return encryptor.encrypt(password.getBytes(UTF_8));
  }

  @Override
  public String decrypt(byte[] encrypted) {
    return new String(encryptor.decrypt(encrypted), UTF_8);
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
