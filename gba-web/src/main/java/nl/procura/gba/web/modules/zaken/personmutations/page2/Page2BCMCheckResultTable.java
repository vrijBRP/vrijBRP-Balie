package nl.procura.gba.web.modules.zaken.personmutations.page2;

import static org.apache.commons.lang3.math.NumberUtils.min;

import nl.procura.gba.web.modules.zaken.personmutations.AbstractPersonMutationsTable;
import org.apache.commons.lang3.math.NumberUtils;

public class Page2BCMCheckResultTable extends AbstractPersonMutationsTable {

  private final BCMCheckResultElems elementRecords = new BCMCheckResultElems();

  @Override
  public void setColumns() {
    addColumn("Code", 160);
    addColumn("Omschrijving");
    super.setColumns();
  }

  @Override
  public void setRecords() {
    for (BCMCheckResultElem recordElement : elementRecords) {
      Record record = super.addRecord(recordElement);
      record.addValue(recordElement.getCode());
      record.addValue(recordElement.getOmschrijving());
    }

    super.setRecords();
  }

  @Override
  public void setPageLength(int pageLength) {
    super.setPageLength(min(getElementRecords().size() + 1, 6));
  }

  public void update(String code, String omschrijving) {
    elementRecords.add(new BCMCheckResultElem(code, omschrijving));
    init();
  }

  public BCMCheckResultElems getElementRecords() {
    return elementRecords;
  }
}
