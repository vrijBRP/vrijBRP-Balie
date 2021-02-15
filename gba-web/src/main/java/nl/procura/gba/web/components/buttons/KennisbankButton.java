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

package nl.procura.gba.web.components.buttons;

import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.NativeButton;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankBron;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankDoel;
import nl.procura.gba.web.services.zaken.kennisbank.KennisbankService;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class KennisbankButton extends NativeButton {

  private KennisBankBron bron = null;
  private KennisBankDoel doel = null;
  private long           code = 0;
  private String         url  = "";

  public KennisbankButton() {

    setStyleName(GbaWebTheme.BUTTON_LINK);
    addStyleName("kennisbank");
    setWidth("42px");
    setIcon(new ThemeResource("../gba-web/buttons/img/kb.png"));
    setDescription("vindburgerzaken.nl kennisbank");

    addListener(new ClickListener() {

      @Override
      public void buttonClick(ClickEvent event) {

        long code = getCode();

        if (fil(getUrl())) {
          String newUrl = getKennisbank().getURL(getUrl());
          getWindow().open(new ExternalResource(newUrl), "kennisbank");
        } else {

          String newUrl = getKennisbank().getURL(getBron(), getDoel(), code);
          if (fil(newUrl) && pos(code)) {
            getWindow().open(new ExternalResource(newUrl), "kennisbank");
          } else {

            getWindow().addWindow(new ConfirmDialog(
                "Er is geen specifieke pagina gevonden. Wilt u toch naar de kennisbank?") {

              @Override
              public void buttonYes() {
                String resourceURL = getKennisbank().getURL("");
                getWindow().getParent().open(new ExternalResource(resourceURL), "kennisbank");
                super.buttonYes();
              }
            });
          }
        }
      }
    });
  }

  public KennisbankButton(KennisBankBron bron, KennisBankDoel doel, FieldValue fieldValue) {

    this();

    setBron(bron);
    setDoel(doel);

    if (fieldValue != null && pos(fieldValue.getValue())) {
      setCode(along(fieldValue.getValue()));
    }
  }

  public KennisbankButton(KennisBankBron bron, KennisBankDoel doel, List<DossierNationaliteit> nationaliteiten) {
    this();

    setBron(bron);
    setDoel(doel);

    for (DossierNationaliteit n : nationaliteiten) {
      setCode(along(n.getNationaliteit().getValue()));
    }
  }

  public KennisbankButton(KennisBankBron bron, KennisBankDoel doel, long code) {

    this();

    setBron(bron);
    setDoel(doel);
    setCode(code);
  }

  public KennisbankButton(String url) {

    this();

    setUrl(url);
  }

  public KennisBankBron getBron() {
    return bron;
  }

  public void setBron(KennisBankBron bron) {
    this.bron = bron;
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }

  public KennisBankDoel getDoel() {
    return doel;
  }

  public void setDoel(KennisBankDoel doel) {
    this.doel = doel;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  private KennisbankService getKennisbank() {
    return ((GbaApplication) getApplication()).getServices().getKennisbankService();
  }
}
