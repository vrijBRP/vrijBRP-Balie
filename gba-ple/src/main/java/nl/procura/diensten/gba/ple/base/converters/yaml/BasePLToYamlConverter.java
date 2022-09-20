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

package nl.procura.diensten.gba.ple.base.converters.yaml;

import static nl.procura.standard.Globalfunctions.fil;

import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePL;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLList;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.PLEMessage;
import nl.procura.diensten.gba.ple.base.PLEResult;

/**
 * Used in ZoekPersoon.pl for PROBEL
 *
 * DO NOT DELETE
 */
public class BasePLToYamlConverter {

  public static void toStream(OutputStream os, PLEResult zoekResultaat) {

    List<BasePL> basePLs = zoekResultaat.getBasePLs();
    BasePLList<PLEMessage> messages = zoekResultaat.getMessages();

    R r = new R();
    for (BasePL bpl : basePLs) {
      P p = new P();
      r.getPs().add(p);

      for (BasePLCat cat : bpl.getCats()) {
        for (BasePLSet set : cat.getSets()) {
          for (BasePLRec rec : set.getRecs()) {
            int code = cat.getCatType().getCode();
            String status = "A";

            if (rec.getStatus() == GBARecStatus.HIST) {
              status = "Z";
              code = code + 50;
            }

            C c = new C(code);
            p.getCs().add(c);

            c.getEs().add(new E(4, status));
            c.getEs().add(new E(5, set.getExtIndex()));

            Collections.sort(rec.getElems());

            for (BasePLElem element : rec.getElems()) {
              GBAGroupElements.GBAGroupElem type = GBAGroupElements.getByCat(element.getCatCode(),
                  element.getElemCode());
              String waarde = element.getValue().getVal();

              if (type.getElem().isNational()) {
                switch (type.getElem().getTable()) {
                  case NATIO:
                  case LAND:
                  case PLAATS:
                    waarde = element.getValue().getDescr();
                    break;

                  default:
                    break;
                }
              }

              if (fil(waarde)) {
                c.getEs().add(new E(element.getElemCode(), waarde));
              }
            }
          }
        }
      }
    }

    for (PLEMessage message : messages) {
      r.getMs().add(new M(message.getCode(), message.getDescr()));
    }

    try {
      Representer representer = new Representer();
      representer.addClassTag(R.class, Tag.MAP);

      DumperOptions options = new DumperOptions();
      options.setDefaultFlowStyle(FlowStyle.BLOCK);
      options.setExplicitStart(true);

      Yaml yaml = new Yaml(representer, options);
      StringWriter writer = new StringWriter();
      yaml.dump(r, writer);
      os.write(writer.toString().getBytes());
      os.flush();
      os.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
