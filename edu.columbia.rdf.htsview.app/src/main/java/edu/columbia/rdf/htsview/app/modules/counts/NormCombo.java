package edu.columbia.rdf.htsview.app.modules.counts;

import org.jebtk.modern.combobox.ModernComboBox;

public class NormCombo extends ModernComboBox {

  private static final long serialVersionUID = 1L;

  public NormCombo() {
    addMenuItem("None");
    addMenuItem("RPM");
    addMenuItem("RPKM");
    addMenuItem("TPM");
    addMenuItem("Median Ratios");

    setSelectedIndex(0);
  }

  public NormalizationMethod getNorm() {
    switch (getSelectedIndex()) {
    case 1:
      return NormalizationMethod.RPM;
    case 2:
      return NormalizationMethod.RPKM;
    case 3:
      return NormalizationMethod.TPM;
    case 4:
      return NormalizationMethod.MEDIAN_RATIO;
    default:
      return NormalizationMethod.NONE;
    }
  }

}
