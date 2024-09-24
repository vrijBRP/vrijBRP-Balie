/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.bs.registration.identification;

import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.IDENTITEITSONDERZOEK;

import java.util.Optional;
import java.util.function.Consumer;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.VrsDocumentenWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;

public class IdentificationPage extends NormalPageTemplate {

  private final Button                       buttonAgree;
  private final Button                       buttonSkip;
  private final DossierPersoon               person;
  private final Consumer<NormalPageTemplate> nextListener;
  private IdentificationForm                 idForm;

  IdentificationPage(DossierPersoon person, Consumer<NormalPageTemplate> nextListener) {
    super("Identificatie");

    this.person = person;
    this.nextListener = nextListener;

    buttonAgree = new Button("Akkoord (F2)");
    buttonAgree.setWidth("130px");
    buttonAgree.addListener(this);

    buttonSkip = new Button("Overslaan (F1)");
    buttonSkip.addListener(this);

    buttonClose.setWidth("130px");
    buttonClose.addListener(this);

    getMainbuttons().addComponent(buttonAgree);
    getMainbuttons().addComponent(buttonSkip);
    getMainbuttons().addComponent(buttonClose);
  }

  @Override
  protected void initPage() {
    idForm = new IdentificationForm(person);
    setInfo("Identificatie van de inschrijver",
        "Om verder te kunnen gaan zal de identiteit van deze persoon moeten worden vastgesteld. <br/>" +
            "Bij voorkeur aan de hand van een reisdocument, vreemdelingendocument of rijbewijs");

    OptieLayout ol = new OptieLayout();
    ol.getLeft().addComponent(idForm);

    ol.getRight().setWidth("130px");
    ol.getRight().setCaption("Opties");
    ol.getRight().addButton(new Button("Basisregister", e -> zoekBasisregister()), this);
    addComponent(ol);

    super.initPage();
  }

  private void zoekBasisregister() {
    idForm.commit();
    Services services = getApplication().getServices();
    VrsRequest request = new VrsRequest()
        .aanleiding(IDENTITEITSONDERZOEK)
        .documentnummer(idForm.getBean().getNummer());

    Optional<ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse> document = services
        .getReisdocumentService()
        .getVrsService()
        .getReisdocument(request);

    document.ifPresent(response -> getApplication()
        .getParentWindow()
        .addWindow(new VrsDocumentenWindow(response, null)));
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (buttonAgree.equals(button) || keyCode == KeyCode.F2) {
      onAgree();
    } else if (buttonSkip.equals(button) || keyCode == KeyCode.F1) {
      getWindow().closeWindow();
      nextListener.accept(this);

    }
    super.handleEvent(button, keyCode);
  }

  private void onAgree() {
    idForm.commit();
    IdentificationBean idBean = idForm.getBean();
    person.setSoort(idBean.getSoort().getCode());
    person.setIdDocNr(idBean.getNummer());
    person.setIssueingCountry(idBean.getIssuingCountry());
    getWindow().closeWindow();
    nextListener.accept(this);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
    super.onClose();
  }
}
