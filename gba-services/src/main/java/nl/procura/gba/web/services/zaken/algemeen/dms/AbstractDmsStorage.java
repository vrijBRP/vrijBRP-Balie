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

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.*;

import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.AbstractService;

public abstract class AbstractDmsStorage extends AbstractService implements DMSStorage {

  public AbstractDmsStorage(String name) {
    super(name);
  }

  protected DMSResult toDmsResult(List<DMSDocument> dmsDocuments) {
    DMSResult dmsResult = new DMSResult();
    dmsResult.getDocuments().addAll(dmsDocuments);
    Collections.sort(dmsResult.getDocuments());
    return dmsResult;
  }

  protected List<String> getCustomerIds(BasePLExt pl) {
    Set<String> list = new LinkedHashSet<>();
    if (pl != null) {
      BasePLValue bsn = pl.getPersoon().getBsn();
      BasePLValue anr = pl.getPersoon().getAnr();
      if (bsn.isNotBlank()) {
        list.add(astr(along(bsn.getCode())));
        list.add(bsn.getCode());
      }
      if (anr.isNotBlank()) {
        list.add(astr(along(anr.getCode())));
        list.add(anr.getCode());
      }
    }

    return new ArrayList<>(list);
  }
}
