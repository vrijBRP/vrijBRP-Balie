/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.burgerzaken.requestinbox.api.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DutchEnumTypeAdapterFactory implements TypeAdapterFactory {

  @Override
  @SuppressWarnings("unchecked")
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    Class<T> rawType = (Class<T>) type.getRawType();
    if (!InboxEnum.class.isAssignableFrom(rawType)) {
      return null;
    }

    final Map<Object, T> values = new HashMap<>();
    for (T constant : rawType.getEnumConstants()) {
      InboxEnum<Object> inboxEnum = (InboxEnum<Object>) constant;
      double doubleKey = NumberUtils.toDouble(inboxEnum.getId().toString(), -1);
      values.put(doubleKey >= 0 ? Double.valueOf(doubleKey).longValue() : inboxEnum.getId(), constant);
    }

    return new TypeAdapter<T>() {
      public void write(JsonWriter out, T value) throws IOException {
        if (value == null) {
          out.nullValue();
        } else {
          out.value(toLowercase(value));
        }
      }

      public T read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
          reader.nextNull();
          return null;
        } else {
          String key = reader.nextString();
          double doubleKey = NumberUtils.toDouble(key, -1);
          return values.get(doubleKey >= 0 ? Double.valueOf(doubleKey).longValue() : key);
        }
      }
    };
  }

  private String toLowercase(Object o) {
    return o.toString().toLowerCase(Locale.US);
  }
}