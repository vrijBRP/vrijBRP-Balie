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

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.applicatie.onderhoud.FileSystemInfo;

public class Page3OnderhoudForm2 extends ReadOnlyForm {

  public Page3OnderhoudForm2() {

    setColumnWidths("100px", "100px", "100px", "100px", "140px", "");
    setOrder(FILESIZE_FREE, FILESIZE_USED, FILESIZE_TOTAL);

    update();
  }

  public void update() {

    Page3OnderhoudBean bean = new Page3OnderhoudBean();

    FileSystemInfo info = Services.getInstance().getOnderhoudService().getFileSystemInfo();
    bean.setFileSizeFree(getReadableByteCount(info.getFreeSpace()) + " (" + info.getFreeSpacePerc() + "%)");
    bean.setFileSizeUsed(getReadableByteCount(info.getUsedSpace()) + " (" + info.getUsedSpacePerc() + "%)");
    bean.setFileSizeTotal(getReadableByteCount(info.getTotalSpace()));

    setBean(bean);
  }
}
