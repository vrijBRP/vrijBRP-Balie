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

package nl.procura.gbaws.requests.ple;

import static nl.procura.standard.Globalfunctions.pos;

import org.apache.commons.io.IOUtils;

import nl.procura.diensten.gba.ple.base.BasePL;
import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.procura.PLE;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaManager;
import nl.procura.diensten.gba.ple.utils.GBAException;
import nl.procura.gbaws.db.jpa.PleJpaUtils;
import nl.procura.gbaws.db.wrappers.ProfileWrapper;
import nl.procura.gbaws.db.wrappers.ProfileWrapper.ProfielElement;
import nl.procura.gbaws.db.wrappers.UsrWrapper;
import nl.procura.gbaws.web.servlets.RequestException;

public class ProcuraDBRequestHandler {

  private BasePLBuilder builder;
  private UsrWrapper    user;

  public ProcuraDBRequestHandler(BasePLBuilder builder, UsrWrapper user, PLEArgs args) {
    this.builder = builder;
    this.user = user;

    final PLEJpaManager plEm = PleJpaUtils.createManager();

    try {
      PLE ple = new PLE(plEm, args);
      ple.setBuilder(builder);
      setFilter();
      ple.find();

      for (final BasePL bpl : builder.getResult().getBasePLs()) {
        bpl.getMetaInfo().put("databron", PLEDatasource.PROCURA.getCode());
      }
    } catch (final IllegalStateException e) {
      handleException(e);
    } catch (RuntimeException e) {
      throw new RequestException(1200, e);
    } finally {
      IOUtils.closeQuietly(plEm);
    }

    if (pos(builder.getResult().getBasePLs().size())) {
      builder.getResult().setDatasource(PLEDatasource.PROCURA);
    }
  }

  private void setFilter() {
    if (user.getProfiel() != null) {
      final ProfileWrapper p = user.getProfiel();
      for (final ProfielElement pe : p.getElementen(0, 0)) {
        builder.getFilter().addElem(pe.getCode_cat(), pe.getCode_element());
      }
    }
  }

  private void handleException(IllegalStateException e) {
    if (e.getMessage().contains("Attempting to execute an operation on a closed EntityManager")) {
      throw new GBAException("De zoekopdracht is voortijdig afgebroken. De maximale zoektijd is 30 seconden.");
    }
  }
}
