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

package nl.procura.gba.web.common.validators;

import static org.apache.commons.lang3.StringUtils.isAnyBlank;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.vaadin.data.validator.AbstractStringValidator;

public class PropertiesValidator extends AbstractStringValidator {

  public PropertiesValidator() {
    super("Geen geldig formaat: key1=Value1, key2=Value2, key3=Value3 ...");
  }

  public static Map<String, String> toMap(String value) {
    Map<String, String> map = new HashMap<>();
    try {
      Properties properties = new Properties();
      properties.load(new ByteArrayInputStream(value.getBytes()));
      properties.entrySet().forEach(entry -> {
        if (isAnyBlank(entry.getKey().toString(), entry.getValue().toString())) {
          throw new IllegalArgumentException("Invalid key-value pair: " + entry);
        }
        map.put(entry.getKey().toString(), entry.getValue().toString());
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return map;
  }

  @Override
  protected boolean isValidString(String value) {
    try {
      toMap(value);
      return true;
    } catch (RuntimeException e) {
      return false;
    }
  }
}
