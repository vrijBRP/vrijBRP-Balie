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

package nl.procura.gbaws.web.rest;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.google.inject.Singleton;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

import nl.procura.gbaws.web.rest.v2.personlists.GbaWsPersonListRequest;
import nl.procura.gbaws.web.rest.v2.personlists.GbaWsPersonListResponse;
import nl.procura.gbaws.web.rest.v2.tables.GbaWsRestTablesRequest;
import nl.procura.gbaws.web.rest.v2.tables.GbaWsRestTablesResponse;

@Provider
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class GbaWsRestJsonJaxbContextResolver implements ContextResolver<JAXBContext> {

    private final JAXBContext context;
    private final Set<Class<?>> types = newHashSet(
            GbaWsPersonListRequest.class,
            GbaWsPersonListResponse.class,
            GbaWsRestTablesRequest.class,
            GbaWsRestTablesResponse.class);

    public GbaWsRestJsonJaxbContextResolver() throws JAXBException {
        this.context = new JSONJAXBContext(JSONConfiguration.natural().build(),
                types.toArray(new Class[0]));
    }

    @Override
    public JAXBContext getContext(Class<?> objectType) {
        return (types.contains(objectType)) ? context : null;
    }
}
