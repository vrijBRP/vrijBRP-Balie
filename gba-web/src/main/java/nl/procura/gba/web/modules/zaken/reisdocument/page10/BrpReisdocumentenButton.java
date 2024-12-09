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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.buttons.GbaButton;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.reisdocument.page14.Page14Reisdocument;

@Slf4j
public class BrpReisdocumentenButton extends GbaButton {

  private final Supplier<BasePLExt>          plSupplier;
  private final Supplier<NormalPageTemplate> pageSupplier;
  private       boolean                      added = false;

  public BrpReisdocumentenButton(Supplier<BasePLExt> plSupplier, Supplier<NormalPageTemplate> pageSupplier) {
    super("Reisdocumenten BRP");
    this.plSupplier = plSupplier;
    this.pageSupplier = pageSupplier;
  }

  @Override
  public void attach() {
    if (!added) {
      addListener((ClickListener) event -> {
        pageSupplier.get()
            .getNavigation()
            .goToPage(new Page14Reisdocument(plSupplier.get()));
      });
      added = true;
    }
    super.attach();
  }
}
