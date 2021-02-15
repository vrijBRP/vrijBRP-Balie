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

package nl.procura.gba.common.password;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

public class DelegatingPasswordEncoder implements PasswordEncoder {

  private static final char PREFIX = '{';
  private static final char SUFFIX = '}';

  private final Map<Integer, PasswordEncoder> encoders;
  private final Integer                       version;
  private final String                        passwordPrefix;

  public DelegatingPasswordEncoder(Integer version) {
    encoders = new HashMap<>(2);
    encoders.put(0, new Version0PasswordEncoder());
    encoders.put(1, new Version1PasswordEncoder());
    if (encoders.get(version) == null) {
      throw new IllegalArgumentException(format("Password encoder version %d doesn't exist", version));
    }
    this.version = version;
    this.passwordPrefix = PREFIX + String.valueOf(version) + SUFFIX;
  }

  @Override
  public String encode(String password) {
    return passwordPrefix + encoders.get(version).encode(password);
  }

  @Override
  public boolean matches(String raw, String encoded) {
    Integer encoderVersion = getVersion(encoded);
    return encoders.get(encoderVersion).matches(raw, encoded.substring(encoded.indexOf(SUFFIX) + 1));
  }

  private static Integer getVersion(String encoded) {
    if (encoded == null || encoded.charAt(0) != PREFIX) {
      throw new IllegalArgumentException();
    }
    try {
      return Integer.valueOf(encoded.substring(1, encoded.indexOf(SUFFIX)));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException();
    }
  }
}
