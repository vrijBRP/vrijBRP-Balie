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

package nl.procura.gba.web.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import nl.procura.gba.web.rest.v2.model.GbaRestJackson;

/**
 * A JAX-RS JSON provider to consume and produce JSON with Jackson for
 * {@link GbaRestJackson} annotated JAX RS methods instead of the default JSON
 * provider. The default one need XML annotations and doesn't handle lists very
 * well.
 * <p>
 * As this is a custom provider, it will be called earlier than the default
 * providers.
 * <p>
 * This provider also provides an {@link ObjectMapper} which doesn't include
 * null values in the output.
 */
@Provider
@Singleton
@Consumes({ MediaType.APPLICATION_JSON, "text/json" })
@Produces({ MediaType.APPLICATION_JSON, "text/json" })
public class GbaRestJsonProvider extends JacksonJsonProvider implements ContextResolver<ObjectMapper> {

  private final ObjectMapper objectMapper;

  public GbaRestJsonProvider() {
    objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
  }

  @Override
  public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    if (type.isAnnotationPresent(GbaRestJackson.class)) {
      return super.isReadable(type, genericType, annotations, mediaType);
    }
    return false;
  }

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    if (type.isAnnotationPresent(GbaRestJackson.class)) {
      return super.isWriteable(type, genericType, annotations, mediaType);
    }
    return false;
  }

  @Override
  public ObjectMapper getContext(Class<?> type) {
    return objectMapper;
  }

}
