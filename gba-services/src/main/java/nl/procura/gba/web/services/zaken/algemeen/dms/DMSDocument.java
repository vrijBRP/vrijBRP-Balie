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

package nl.procura.gba.web.services.zaken.algemeen.dms;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ComparisonChain;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder(builderMethodName = "hiddenBuilder")
public class DMSDocument implements Comparable<DMSDocument> {

  @Default
  private DMSContent          content                 = null;
  @Default
  private String              collection              = "";
  @Default
  private String              uuid                    = "";
  @Default
  private String              customerId              = "";
  @Default
  private long                date                    = -1;
  @Default
  private long                time                    = -1;
  @Default
  private String              alias                   = "";
  @Default
  private String              title                   = "";
  @Default
  private String              user                    = "";
  @Default
  private String              datatype                = "";
  @Default
  private String              zaakId                  = "";
  @Default
  private String              documentTypeDescription = "";
  @Default
  private String              confidentiality         = "";
  @Default
  private DMSStorageType      storage                 = DMSStorageType.DEFAULT;
  @Default
  private Map<String, String> otherProperties         = new HashMap<>();

  public static DMSDocumentBuilder builder() {
    return hiddenBuilder();
  }

  @Override
  public int compareTo(DMSDocument that) {
    return ComparisonChain.start()
        .compare(that.getDate(), this.getDate())
        .compare(that.getTime(), this.getTime())
        .result();
  }

  @Override
  public String toString() {
    return content.getFilename();
  }
}
