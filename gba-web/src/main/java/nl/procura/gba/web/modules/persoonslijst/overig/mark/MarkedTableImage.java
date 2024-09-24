package nl.procura.gba.web.modules.persoonslijst.overig.mark;

import nl.procura.gba.web.components.TableImage;

public class MarkedTableImage extends TableImage {

  public MarkedTableImage(boolean marked) {
    super(marked ? "../gba-web/buttons/img/marked.png" : "../gba-web/buttons/img/not-marked.png");
  }
}
