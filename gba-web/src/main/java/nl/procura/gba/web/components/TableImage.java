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

package nl.procura.gba.web.components;

import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaarType;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.vaadin.theme.twee.Icons;

public class TableImage extends EmbeddedResource {

  public TableImage(String s) {
    super(s);
    setHeight("15px");
  }

  public static TableImage getByBestandType(BestandType type) {
    return new TableImage(type.getPath());
  }

  public static TableImage getByCommentaarIcon(ZaakCommentaarType type) {
    if (type != null) {
      switch (type) {
        case ERROR:
          return new TableImage(Icons.getIcon(Icons.ICON_WARN));
        case INFO:
          return new TableImage(Icons.getIcon(Icons.ICON_INFO));
        case WARN:
          return new TableImage(Icons.getIcon(Icons.ICON_WARN));
        default:
          return null;
      }
    }
    return null;
  }
}
