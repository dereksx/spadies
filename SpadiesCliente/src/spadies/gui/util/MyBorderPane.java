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

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

@SuppressWarnings("serial")
public final class MyBorderPane extends JPanel {
  public MyBorderPane(boolean conBorde, int bArr, int bIzq, int bAbj, int bDer, Component...componentes) {
    super(new BorderLayout());
    Border b=BorderFactory.createEmptyBorder(bArr,bIzq,bAbj,bDer);
    setBorder(conBorde?BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),b):b);
    String zonas[]={BorderLayout.NORTH,BorderLayout.WEST,BorderLayout.CENTER,BorderLayout.EAST,BorderLayout.SOUTH};
    for (int i=0; i<5; i++) if (componentes[i]!=null) add(componentes[i],zonas[i]);
  }
}
