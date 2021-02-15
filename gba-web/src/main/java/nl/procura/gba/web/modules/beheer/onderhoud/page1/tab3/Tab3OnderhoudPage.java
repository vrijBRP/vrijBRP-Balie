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

import org.vaadin.vaadinvisualizations.AreaChartImage;

import com.github.wolfie.refresher.Refresher;

import nl.procura.commons.misc.system.ProSystemMemoryUtils;
import nl.procura.commons.misc.system.ProSystemMemoryUtils.HeapMemoryUsage;
import nl.procura.commons.misc.system.ProSystemMemoryUtils.MemoryUsage;
import nl.procura.gba.web.modules.beheer.onderhoud.OnderhoudTabPage;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.label.Break;
import nl.procura.vaadin.component.label.H3;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Tab3OnderhoudPage extends OnderhoudTabPage {

  private final static int     MILLIS_5000 = 5000;
  private final static int     MILLIS_1000 = 1000;
  private final AreaChartImage ac          = new AreaChartImage();
  private final Refresher      refresher   = new Refresher();
  private Page3OnderhoudForm1  form1;
  private Page3OnderhoudForm2  form2;

  public Tab3OnderhoudPage() {
    super("Systeeminformatie");
    setMargin(true);
    setSpacing(true);
  }

  private static Double getMb(long bytes) {
    return (double) bytes / (double) 1024 / 1024;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addSpaceInfo();
      addMemoryInfo();
    }

    super.event(event);
  }

  private void addMemoryInfo() {

    addComponent(new Break());
    addComponent(new H3("Geheugen"));

    form1 = new Page3OnderhoudForm1();

    addComponent(form1);

    ac.addXAxisLabel("Year");
    ac.setOption("legend", "bottom");

    ac.addArea("Max");
    ac.addArea("Momenteel gereserveerd door OS");
    ac.addArea("Huidig");

    addTime();
    addTime();

    ac.setWidth("100%");
    ac.setHeight("300px");

    ac.setOption("width", 700);
    ac.setOption("height", 240);
    ac.setOption("min", 0);

    addComponent(ac);

    refresher.setRefreshInterval(MILLIS_1000);
    addComponent(refresher);

    UpdateThread ut = new UpdateThread();
    ut.start();
  }

  private void addSpaceInfo() {

    addComponent(new Break());
    addComponent(new H3("Schijfruimte"));

    form2 = new Page3OnderhoudForm2();

    addComponent(form2);
  }

  private void addTime() {

    String st = new ProcuraDate().getFormatTime("HH:mm");

    for (MemoryUsage memoryUsage : ProSystemMemoryUtils.getMemoryUsage()) {

      if (memoryUsage instanceof HeapMemoryUsage) {

        long used = memoryUsage.getUsed();
        long committed = memoryUsage.getCommitted();
        long max = memoryUsage.getMax();

        ac.add(st, new double[]{ getMb(max), getMb(committed), getMb(used) });
      }
    }
  }

  class UpdateThread extends Thread {

    @Override
    public void run() {

      try {

        boolean isParent = true;
        boolean isApp = true;
        boolean isSession = true;

        while (isParent && isApp && isSession) {
          sleep(MILLIS_5000);
          addTime();
          form1.update();

          isParent = getParent() != null;
          isApp = getApplication() != null;
          isSession = isApp && getApplication().isRunning();
        }
      } catch (InterruptedException e) {
        log.trace("Error", e);
        Thread.currentThread().interrupt();
      }

      log.info("Memory process stopped");
    }
  }
}
