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

package nl.procura.rdw.functions;

import java.io.File;
import java.io.FileOutputStream;

import nl.procura.standard.ProcuraDate;

public class TcpDump {

  public static void dump(final Proces proces) {

    new Thread() {

      @Override
      public synchronized void run() {

        try {
          ProcuraDate pd = new ProcuraDate();

          File logDir = new File(String.format("log/%s/%s", pd.getSystemDate(), pd.getSystemTime()));

          if (!logDir.exists()) {
            logDir.mkdirs();
          }

          File tcpDump = new File(logDir,
              String.format("%04d-%02d.txt", proces.getProces(), proces.getFunctie()));

          FileOutputStream fos = new FileOutputStream(tcpDump);

          RdwUtils.toStream(proces, fos, true, true);

        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    }.start();
  }
}
