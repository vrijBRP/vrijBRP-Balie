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

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.xml.sax.ErrorHandler;

import com.sun.codemodel.*;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

import nl.procura.rdw.functions.ProcesObject;

public class CustomPlugin1 extends Plugin {

  @Override
  public String getOptionName() {
    return "Xpm1";
  }

  @Override
  public String getUsage() {
    return "  -Xpm1    : Change members visibility to private";
  }

  @Override
  public boolean run(Outline model, Options opt, ErrorHandler errorHandler) {

    int i = 0;

    for (ClassOutline co : model.getClasses()) {
      i++;

      JDefinedClass jdc = co.implClass;

      if (i == 1) {
        jdc._extends(ProcesObject.class);
      }

      jdc._implements(Serializable.class);

      List<JFieldVar> fields = new ArrayList<>(jdc.fields().values());

      for (JFieldVar field : fields) {

        if (!field.name().equalsIgnoreCase("serialVersionuid")) {

          if (field.mods().getValue() != JMod.PRIVATE) {

            jdc.removeField(field);

            JFieldVar nf = jdc.field(JMod.PRIVATE, field.type(), field.name());

            for (JAnnotationUse a : field.annotations()) {

              JAnnotationUse xa = nf.annotate(a.getAnnotationClass());

              for (Entry<String, JAnnotationValue> e : a.getAnnotationMembers().entrySet()) {

                StringWriter sw = new StringWriter();

                e.getValue().generate(new JFormatter(sw));

                String value = sw.toString().replaceAll("^\"|\"$", "");

                if ("true".equals(value)) {

                  xa.param(e.getKey(), true);

                } else {
                  xa.param(e.getKey(), value);
                }
              }
            }
          }
        }
      }
    }

    return true;
  }

}
