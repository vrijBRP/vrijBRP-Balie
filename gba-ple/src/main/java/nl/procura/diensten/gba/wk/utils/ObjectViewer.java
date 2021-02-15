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

package nl.procura.diensten.gba.wk.utils;

import java.util.Map.Entry;

import com.google.gson.*;

public class ObjectViewer {

  public static void view(Object o) {

    Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();

    JsonParser p = new JsonParser();

    p(p.parse(gsonPretty.toJson(o)), 0);

    System.out.println();
  }

  private static void p(JsonElement e, int i) {

    i = i + 1;

    if (e.isJsonArray()) {

      for (JsonElement s : e.getAsJsonArray()) {

        p(s, i);
      }
    } else if (e.isJsonObject()) {

      int size = 10;

      for (Entry<String, JsonElement> en : ((JsonObject) e).entrySet()) {

        if (en.getValue().isJsonPrimitive()) {

          int cSize = en.getKey().length();

          if (cSize > size) {
            size = cSize;
          }
        }
      }

      for (Entry<String, JsonElement> en : ((JsonObject) e).entrySet()) {

        for (int x = 0; x < i; x++) {
          System.out.print(" ");
        }

        System.out.print(String.format("%-" + size + "s ", en.getKey()));

        if (!en.getValue().isJsonPrimitive()) {
          System.out.print("\n");
        }

        p(en.getValue(), i);
      }

      for (int x = 0; x < i; x++) {
        System.out.print(" ");
      }

      System.out.print("-\n");
    } else if (e.isJsonPrimitive()) {

      System.out.println(": " + e.getAsString());
    }
  }
}
