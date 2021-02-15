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

package nl.procura.rdw.generate;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.sun.tools.xjc.Driver;

public class GenerateClasses {

  public GenerateClasses() throws Throwable {

    File xsdDir = new File("gba-rdw/src/main/resources/xsd");
    File genDir = new File("gba-rdw/src/main/java/nl/procura/rdw/processen");

    System.out.println(new File(".").getAbsolutePath());
    System.out.println(xsdDir.exists());

    for (File file : FileUtils.listFiles(xsdDir, new String[]{ "xsd" }, false)) {

      if (file.getName().contains("PROC")) {
        String pattern = "^PROC-(\\d+)-(\\d+).xsd";

        Matcher m = Pattern.compile(pattern).matcher(file.getName());
        if (m.find()) {

          String targetDir = "/p" + m.group(1) + "/f" + m.group(2);
          String p = "nl.procura.rdw.processen.p" + m.group(1) + ".f" + m.group(2);
          File procDir = new File(genDir, targetDir);
          procDir.mkdirs();
          Driver.run(
              new String[]{ "-d", "gba-rdw/src/main/java/", "-enableIntrospection", "-Xpm1", "-Xpm2", "-p", p,
                  file.getAbsolutePath() },
              System.out, System.out);
        }
      }
    }
  }

  public static void main(String[] args) throws Throwable {

    new GenerateClasses();
  }
}
