/*
 * Centro de Estudios sobre Desarrollo Econ�mico (CEDE) - Universidad de los Andes
 * Octubre 18 de 2006
 *
 *********************************************************
 * SPADIES                                               *
 * Sistema para la Prevenci�n y An�lisis de la Deserci�n *
 * en las Instituciones de Educaci�n Superior            *
 *********************************************************
 * Autores del c�digo fuente (�ltima versi�n):           *
 *  Alejandro Sotelo Ar�valo   alejandrosotelo@gmail.com *
 *  Andr�s C�rdoba Melani      acordoba@gmail.com        *
 *********************************************************
 *
 * Para informaci�n de los participantes del proyecto v�ase el "Acerca De" de la aplicaci�n.
 * 
 * La modificaci�n del c�digo fuente est� prohibida sin permiso expl�cito por parte de
 * los autores o del Ministerio de Educaci�n Nacional de la Rep�blica de Colombia.
 *
 */
package spadies.gui.util;

import javax.swing.*;

@SuppressWarnings("serial")
public class MyLabel extends JLabel {
  public MyLabel() {
    super();
  }
  public MyLabel(Icon icon) {
    super(icon);
  }
  public MyLabel(String texto) {
    super(texto);
  }
  public MyLabel(String texto, int alineacionHorizontal) {
    super(texto,alineacionHorizontal);
  }
  public MyLabel(String texto, String toolTipText) {
    super(texto);
    setToolTipText(toolTipText);
  }
  public MyLabel(String texto, String toolTipText, int alineacionHorizontal) {
    super(texto,alineacionHorizontal);
    setToolTipText(toolTipText);
  }
}
