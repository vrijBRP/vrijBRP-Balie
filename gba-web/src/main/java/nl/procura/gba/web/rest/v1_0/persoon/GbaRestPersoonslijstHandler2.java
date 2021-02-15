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

package nl.procura.gba.web.rest.v1_0.persoon;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAGroup;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.*;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;

public class GbaRestPersoonslijstHandler2 extends GbaRestElementHandler {

  public GbaRestPersoonslijstHandler2(Services services) {
    super(services);
  }

  public void getPersoon(BasePL pl, GbaRestElement persoonElement) {

    for (BasePLCat cat : pl.getCats()) {

      GbaRestElement catElement = persoonElement.add(GbaRestElementType.CATEGORIE);

      GBACat categorie = cat.getCatType();

      catElement.add(GbaRestElementType.CODE).set(astr(categorie.getCode()), categorie.getDescr());

      for (BasePLSet set : cat.getSets()) {

        catElement.add(GbaRestElementType.VOLGNUMMER).set(astr(set.getExtIndex()));

        GbaRestElement setElement = catElement.add(GbaRestElementType.SET);

        for (BasePLRec record : set.getRecs()) {

          GbaRestElement recordElement = setElement.add(GbaRestElementType.RECORD);

          GBARecStatus status = record.getStatus();

          recordElement.add(GbaRestElementType.STATUS).set(astr(status.getCode()), status.getDescr());

          for (BasePLElem gegeven : record.getElems()) {

            GbaRestElement gegevenElement = recordElement.add(GbaRestElementType.GEGEVEN);

            BasePLValue waarde = gegeven.getValue();

            GBAGroupElements.GBAGroupElem pleElement = GBAGroupElements.getByCat(gegeven.getCatCode(),
                gegeven.getElemCode());

            gegevenElement.add(GbaRestElementType.ELEMENT_NUMMER).set(
                String.format("%04d", gegeven.getElemCode()));

            GBAGroup groep = pleElement.getGroup();

            if (groep != null) {
              gegevenElement.add(GbaRestElementType.ELEMENT_GROEP).set(astr(groep.getCode()),
                  groep.getDescr());
            }

            gegevenElement.add(GbaRestElementType.ELEMENT_NAAM).set(pleElement.getElem().getDescr());
            gegevenElement.add(GbaRestElementType.INHOUD).set(waarde.getVal(), waarde.getDescr());
          }
        }
      }
    }
  }
}
