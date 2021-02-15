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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab3;

import static nl.procura.commons.misc.system.ProSystemUtils.getReadableByteCount;
import static nl.procura.gba.web.modules.beheer.onderhoud.page1.tab3.Page3OnderhoudBean.*;

import nl.procura.commons.misc.system.ProSystemMemoryUtils;
import nl.procura.commons.misc.system.ProSystemMemoryUtils.HeapMemoryUsage;
import nl.procura.commons.misc.system.ProSystemMemoryUtils.MemoryUsage;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;

public class Page3OnderhoudForm1 extends ReadOnlyForm {

  public Page3OnderhoudForm1() {

    setColumnWidths("100px", "100px", "100px", "100px", "140px", "");
    setOrder(CURRENT, TOTAL, MAX);

    update();
  }

  public void update() {

    Page3OnderhoudBean bean = new Page3OnderhoudBean();

    for (MemoryUsage memoryUsage : ProSystemMemoryUtils.getMemoryUsage()) {

      if (memoryUsage instanceof HeapMemoryUsage) {

        bean.setCurrent(getReadableByteCount(memoryUsage.getUsed()));
        bean.setTotal(getReadableByteCount(memoryUsage.getCommitted()));
        bean.setMax(getReadableByteCount(memoryUsage.getMax()));
      }
    }

    setBean(bean);
  }
}
