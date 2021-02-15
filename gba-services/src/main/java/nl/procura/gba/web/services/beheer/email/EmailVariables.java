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

package nl.procura.gba.web.services.beheer.email;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.beheer.email.EmailType.NIEUWE_GEBRUIKER;
import static nl.procura.gba.web.services.beheer.email.EmailType.WACHTWOORD_VERGETEN;
import static nl.procura.gba.web.services.beheer.email.EmailVariable.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.procura.gba.common.UniqueList;

public class EmailVariables {

  private static final Map<EmailType, List<EmailVariable>> variables = new HashMap<>();

  static {
    variables.put(WACHTWOORD_VERGETEN, asList(NAAM, LINK, LINK_GELDIG));
    variables.put(NIEUWE_GEBRUIKER, asList(NAAM, LINK, LINK_GELDIG));
  }

  /**
   * Sla een lijstje op variabelen
   */
  public static List<EmailVariable> getVariables(EmailType emailType) {

    List<EmailVariable> emailVariables = variables.get(emailType);
    return emailVariables != null ? emailVariables : new ArrayList<>();
  }

  /**
   * Geef een lijst met variable die gebruikt zijn in de tekst die er niet in thuis horen.
   */
  public static List<String> getWrongVariables(EmailType emailType, String content) {

    List<String> wrongVariables = new UniqueList<>();

    Matcher m = Pattern.compile("\\$\\{(\\w+)\\}").matcher(content);

    while (m.find()) {
      String match = m.group(1);
      if (!EmailVariables.isVariable(emailType, EmailVariable.get(match))) {
        wrongVariables.add(match);
      }
    }

    return wrongVariables;
  }

  /**
   * Hoort deze variabele bij dit e-mailtype?
   */
  public static boolean isVariable(EmailType emailType, EmailVariable variable) {
    return getVariables(emailType).contains(variable);
  }
}
