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

@SuppressWarnings("serial")
public class MyEditorPane extends JEditorPane {
  public MyEditorPane(boolean conAparienciaLabel) {
    super("text/html", "");
    setEditable(false);
    if (conAparienciaLabel) {
      setBackground((Color)(UIManager.get("Label.background")));
      setForeground((Color)(UIManager.get("Label.foreground")));
      setFont((Font)(UIManager.get("Label.font")));
    }
  }
  public MyEditorPane(boolean conAparienciaLabel, String textoHTML) {
    this(conAparienciaLabel);
    setText(textoHTML);
  }
  public void setText(String textoHTML) {
    String s=textoHTML;
    Font f=getFont();
    if (s.trim().length()>0) s=("<html><font face=\""+f.getFamily()+"\" style=\"font-size: "+f.getSize()+"\">"+s.replaceAll("(\\Q<html>\\E)|(\\Q</html>\\E)","").replace("<p>","<br>")+"</font></html>");
    //if (s.trim().length()>0) s=("<html><font face=\""+f.getFamily()+"\" style=\"font-size: "+f.getSize()+"\">"+s.replaceAll("[\\Q<html>\\E\\Q</html>\\E]","").replaceAll("\\Q<p>\\E","<br>")+"</font></html>");
    super.setText(s);
    setCaretPosition(0);
  }
}
