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

package nl.procura.gba.jpa.personenws.session;

import java.util.Iterator;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.helper.ClassConstants;
import org.eclipse.persistence.mappings.CollectionMapping;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.sessions.Session;

public class GbaWsSessionCustomize implements SessionCustomizer {

  @Override
  public void customize(Session session) {
    for (Iterator<?> dI = session.getProject().getAliasDescriptors().values().iterator(); dI.hasNext();) {
      ClassDescriptor descriptor = (ClassDescriptor) dI.next();
      for (DatabaseMapping mapping : descriptor.getMappings()) {
        if (mapping.isForeignReferenceMapping() && mapping.isCollectionMapping()) {
          CollectionMapping colMapping = (CollectionMapping) mapping;
          if (colMapping.getContainerPolicy().getContainerClass() == ClassConstants.IndirectList_Class) {
            colMapping.useCollectionClass(WsUniqueJpaList.class);
          }
        }
      }
    }
  }
}
