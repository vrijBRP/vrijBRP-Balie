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

package nl.procura.gba.web.services.beheer.requestinbox;

import static java.util.Optional.ofNullable;
import static nl.procura.gba.common.MiscUtils.toDate;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import nl.procura.burgerzaken.gba.numbers.Bsn;
import nl.procura.burgerzaken.requestinbox.api.model.DutchEnumTypeAdapterFactory;
import nl.procura.burgerzaken.requestinbox.api.model.LocalDateDeserializer;
import nl.procura.standard.ProcuraDate;
import nl.procura.validation.Anr;

import lombok.Getter;

@Getter
public class RequestInboxBody {

  private final Map<String, Object> data = new LinkedHashMap<>();

  public RequestInboxBody addBsn(String key, Long value) {
    data.put(key, ofNullable(value)
        .filter(val -> val.toString().length() > 6 && Bsn.isCorrect(val.toString()))
        .map(val -> Bsn.format(val.toString())).orElse(""));
    return this;
  }

  public RequestInboxBody addAnr(String key, Long value) {
    data.put(key, ofNullable(value)
        .filter(val -> val.toString().length() > 6 && Anr.isCorrect(val.toString()))
        .map(val -> Anr.format(val.toString())).orElse(""));
    return this;
  }

  public RequestInboxBody add(String key, Object value) {
    data.put(key, ofNullable(value).map(Object::toString).orElse(""));
    return this;
  }

  public RequestInboxBody add(String key, LocalDate value) {
    data.put(key, ofNullable(value)
        .map(val -> new ProcuraDate(toDate(val)).getFormatDate())
        .orElse(""));
    return this;
  }

  public RequestInboxBody add(String key, Boolean value) {
    data.put(key, ofNullable(value).map(val -> val ? "Ja" : "Nee").orElse(""));
    return this;
  }

  public static <T> T fromJson(RequestInboxItem inboxRecord, Class<T> cl) {
    return fromJson(inboxRecord, cl, new LinkedHashMap<>());
  }

  public static <T, E> T fromJson(RequestInboxItem inboxRecord, Class<T> cl,
      Map<Class<?>, JsonDeserializer<?>> typeAdapters) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
    gsonBuilder.registerTypeAdapterFactory(new DutchEnumTypeAdapterFactory());
    typeAdapters.forEach(gsonBuilder::registerTypeAdapter);
    Gson gson = gsonBuilder.setPrettyPrinting().create();
    return gson.fromJson(new String(inboxRecord.getContent()), cl);
  }
}
