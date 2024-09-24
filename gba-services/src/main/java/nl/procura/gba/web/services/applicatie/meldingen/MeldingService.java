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

package nl.procura.gba.web.services.applicatie.meldingen;

import static nl.procura.gba.web.common.misc.ImportExportHandler.exportObject;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.SHOW_MESSAGES;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.SHOW_USER_MESSAGES;
import static nl.procura.standard.Globalfunctions.fil;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.StreamUtils;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.applicatie.meldingen.export.XmlMelding;
import nl.procura.gba.web.services.applicatie.meldingen.export.XmlMeldingenExport;
import nl.procura.gba.web.services.gba.templates.GbaTemplateService;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.functies.downloading.DownloadHandler;

public class MeldingService extends GbaTemplateService {

  private List<ServiceMelding> meldingen = new ArrayList<>();
  private boolean              messagePopupShow;

  public MeldingService() {
    super("Meldingen");
  }

  public void export(DownloadHandler downloadHandler) {

    try {
      XmlMeldingenExport export = new XmlMeldingenExport();
      for (ServiceMelding dm : getMeldingen()) {
        XmlMelding m = new XmlMelding();
        m.setType(check(dm.getSeverity().getDescr()));
        m.setTijdstip(check(dm.getDatumtijd().toString()));
        m.setGebruiker(check(dm.getGebruiker().getGebruikersnaam() + " (" + dm.getGebruiker().getNaam() + ")"));
        m.setMelding(check(dm.getMelding()));
        m.setOorzaken(check(dm.getOorzaak()));
        m.setExceptie(check(dm.getExceptionString()));

        export.getMeldingen().add(m);
      }

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      StreamUtils.toStream(export, bos);
      Map<String, byte[]> map = new HashMap<>();
      map.put("meldingen.xml", bos.toByteArray());
      exportObject(map, "meldingen.zip", downloadHandler);
    } catch (Exception e) {
      throw new ProException(ProExceptionSeverity.ERROR, "Fout bij exporteren meldingen", e);
    }
  }

  public List<ServiceMelding> getMeldingen(ServiceMeldingCategory category) {
    return getMeldingen().stream()
        .filter(m -> category != null && category.equals(m.getCategory()))
        .filter(m -> !m.isAdminOnly() || (m.getGebruiker() != null && m.getGebruiker().isAdministrator()
            && m.isAdminOnly()))
        .collect(Collectors.toList());
  }

  /**
   * Show the messages popup on startup
   */
  public boolean isShowMessagesPopup() {
    String parm = getServices().getGebruiker().getParameters().get(SHOW_MESSAGES).getValue();
    return StringUtils.isBlank(parm) || Boolean.valueOf(parm);
  }

  /**
   * Don't show the message again this session
   */
  public void disableMessagePopupShown() {
    this.messagePopupShow = true;
  }

  /**
   * Is the popup already shown this session
   * @return
   */
  public boolean isMessagePopupShown() {
    return this.messagePopupShow;
  }

  /**
   * Enable / disable messages popup
   */
  public void enableShowMessagesPopup(boolean value) {
    getServices().getParameterService().saveParameter(
        SHOW_MESSAGES,
        String.valueOf(value),
        getServices().getGebruiker(),
        null,
        true);
  }

  /**
   * Show only user messages
   * @return
   */
  public boolean isShowOnlyUser() {
    String parm = getServices().getGebruiker().getParameters().get(SHOW_USER_MESSAGES).getValue();
    return StringUtils.isNotBlank(parm) && Boolean.valueOf(parm);
  }

  /**
   * Show only messages from the user
   */
  public void enableShowOnlyUser(boolean value) {
    getServices().getParameterService().saveParameter(
        SHOW_USER_MESSAGES,
        String.valueOf(value),
        getServices().getGebruiker(),
        null,
        true);
  }

  /**
   * Voeg melding toe en roep listeners aan.
   */
  public void add(ServiceMelding melding) {
    melding.setGebruiker(getServices().getGebruiker());
    if (!meldingen.contains(melding)) {
      meldingen.add(melding);
      callListeners(ServiceEvent.CHANGE);
    }
  }

  public void delete(ServiceMeldingCategory category) {
    meldingen.removeIf(m -> m.getCategory().equals(category));
    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Verwijder een melding en roep listeners aan
   */
  public void delete(ServiceMelding melding) {
    if (meldingen.remove(melding)) {
      callListeners(ServiceEvent.CHANGE);
    }
  }

  public void delete(String id) {
    delete(new ServiceMelding().setId(id));
  }

  private String check(String value) {
    return fil(value) ? value.trim() : "onbekend";
  }

  private List<ServiceMelding> getMeldingen() {
    Collections.sort(meldingen);
    return meldingen;
  }

  public void setMeldingen(ArrayList<ServiceMelding> meldingen) {
    this.meldingen = meldingen;
  }
}
