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

package nl.procura.gba.web.modules.bs.registration.processes;

import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.COMPLETE;
import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.EMPTY;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatussen;
import nl.procura.gba.web.modules.bs.registration.page10.Page10Registration;
import nl.procura.gba.web.modules.bs.registration.page20.Page20Registration;
import nl.procura.gba.web.modules.bs.registration.page30.Page30Registration;
import nl.procura.gba.web.modules.bs.registration.page40.Page40Registration;
import nl.procura.gba.web.modules.bs.registration.page50.Page50Registration;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.registration.DeclarantType;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.bs.registration.RelationService;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;

public class RegistrationProcesses extends BsProcessen {

  public RegistrationProcesses(Dossier dossier, GbaApplication gbaApplication) {
    this(gbaApplication);
    setDossier(dossier);
  }

  private RegistrationProcesses(GbaApplication gbaApplication) {

    super(gbaApplication);

    addProces("Aangifte", Page10Registration.class);
    addProces("Personen", Page20Registration.class);
    addProces("Aangever", Page30Registration.class);
    addProces("Relaties", Page40Registration.class);
    addProces("Overzicht", Page50Registration.class);
  }

  private boolean isAllProcessable(Dossier dossier, RelationService relationService) {
    List<DossPersRelation> dossPersRelations = relationService.findByPeople(dossier.getPersonen());
    return Relation.fromDossPersRelations(dossPersRelations, dossier.getPersonen())
        .stream()
        .allMatch(Relation::isProcessable);
  }

  @Override
  public void initStatusses(GbaApplication app) {

    DossierRegistration rd = (DossierRegistration) getDossier().getZaakDossier();
    RelationService relationService = app.getServices().getRelationService();

    boolean isDeclaration = StringUtils.isNotBlank(rd.getStreet());
    boolean isRegistrants = !getDossier().getPersonen(DossierPersoonType.INSCHRIJVER).isEmpty();
    boolean isDeclarant = DeclarantType.UNKNOWN != DeclarantType.valueOfCode(rd.getDeclarant().getCode());
    boolean isRelations = !relationService.findByPeople(getDossier().getPersonen()).isEmpty();

    getProces(Page10Registration.class).setStatus(isDeclaration ? COMPLETE : EMPTY);
    getProces(Page20Registration.class).setStatus(isDeclaration ? COMPLETE : EMPTY);
    getProces(Page30Registration.class).setStatus(isRegistrants ? COMPLETE : EMPTY);
    getProces(Page40Registration.class).setStatus(isDeclarant ? COMPLETE : EMPTY);
    getProces(Page50Registration.class).setStatus(isRelations ? COMPLETE : EMPTY);

    updateStatus();
  }

  @Override
  public void updateStatus() {
    BsProcesStatussen procesStatussen = getProcesStatussen(getDossier());
    for (BsProcesStatussen.BspProcesStatusEntry entry : procesStatussen.getList()) {
      getProces(entry.getCl()).setStatus(entry.getStatus(), entry.getMessage());
    }

    if (isPaginaReset(getDossier())) {
      goToFirst();
    }

    ZaakCommentaren commentaar = getDossier().getCommentaren();
    commentaar.verwijderen().toevoegenWarn(procesStatussen.getMessages());
  }

  private BsProcesStatussen getProcesStatussen(Dossier dossier) {
    BsProcesStatussen statussen = new BsProcesStatussen();
    if (fil(dossier.getZaakId())) {
      RelationService relationService = getApplication().getServices().getRelationService();
      boolean isRelations = relationService.findByPeople(dossier.getPersonen()).size() > 0;
      boolean allProcessable = isAllProcessable(dossier, relationService);
      statussen.add(Page40Registration.class, isRelations,
          allProcessable ? ""
              : "Er zijn relaties opgenomen met een waarschuwing die niet verwerkt " +
                  "zullen worden in de BRP.");
    }

    return statussen;
  }
}
