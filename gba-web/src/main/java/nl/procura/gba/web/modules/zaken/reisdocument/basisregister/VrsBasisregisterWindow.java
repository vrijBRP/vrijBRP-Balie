package nl.procura.gba.web.modules.zaken.reisdocument.basisregister;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page1.Page1VrsBasisRegister;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class VrsBasisregisterWindow extends GbaModalWindow {

  private final BasePLExt      pl;
  private final Aanvraagnummer aanvraagnummer;

  public VrsBasisregisterWindow(BasePLExt pl, Aanvraagnummer aanvraagnummer) {
    super("Reisdocumenten in basisregister", "1000px");
    this.pl = pl;
    this.aanvraagnummer = aanvraagnummer;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page1VrsBasisRegister(pl, aanvraagnummer)));
  }
}
