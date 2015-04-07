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
package spadies.gui.frames;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import spadies.gui.util.*;
import spadies.util.*;
import spadies.gui.imagenes.*;

@SuppressWarnings("serial")
public class VentanaAcercaDe extends MyDialog {
  private static final VentanaAcercaDe instance=new VentanaAcercaDe();
  private final MyLabel labelDibujo=new MyLabel(Imagenes.IM_MINISTERIO.getImagen());
  private final MyLabel labelTitulo=new MyLabel();
  private final MyButton botonCerrar=new MyButton("Cerrar","Cerrar la ventana",KeyEvent.VK_R);
  public static VentanaAcercaDe getInstance() {
    return instance;
  }
  private VentanaAcercaDe() {
    super(VentanaPrincipal.getInstance(),"Acerca de "+Constantes.nombreAplicacionLargo,MyDialog.TipoBloqueo.TB_MODAL);
    botonCerrar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        VentanaAcercaDe.this.dispose();
      }
    });
    {
      StringBuffer sb=new StringBuffer();
      sb.append("<html>");
      sb.append("<p>");
      sb.append("<p>");
      sb.append("<hr>");
      sb.append("<font size=\"6\" color=\"#00005B\"><i><b>"+Constantes.nombreAplicacionLargo+"</b></i></font><p>");
      sb.append("<font size=\"4\" color=\"#00005B\"><i>Sistema para la Prevenci�n y An�lisis de la Deserci�n</i></font><p>");
      sb.append("<font size=\"4\" color=\"#00005B\"><i>en las Instituciones de Educaci�n Superior</i></font><p>");
      sb.append("<p>");
      sb.append("<b><font color=\"#900000\">Participantes del proyecto:</font></b>");
      sb.append("<p>");
      sb.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
      sb.append("  <tr><td colspan=\"2\" bgcolor=\"#D4D4FF\" width=\"600\"><b>Ministerio de Educaci�n Nacional</b></td></tr>");
      sb.append("  <tr>");
      sb.append("    <td width=\"40\"></td>");
      sb.append("    <td>Carolina Guzm�n, Jorge Franco, Jorge Navas, Diana Marcela Duran Muriel</td>");
      sb.append("  </tr>");
      sb.append("  <tr><td colspan=\"2\"></td></tr>");
      sb.append("  <tr><td colspan=\"2\" bgcolor=\"#D4D4FF\" width=\"600\"><b>Centro de Estudios sobre Desarrollo Econ�mico (CEDE) - Universidad de los Andes</b></td></tr>");
      sb.append("  <tr>");
      sb.append("    <td width=\"40\"></td>");
      sb.append("    <td>Fabio S�nchez Torres, "+/*Jorge Villalobos, */"Haider Jaime Rueda</td>");
      //sb.append("    <td>Fabio S�nchez, Haider Jaime, Carmen Elisa Fl�rez, Jairo N��ez</td>");
      sb.append("  </tr>");
      sb.append("  <tr>");
      sb.append("    <td width=\"40\"></td>");
      //sb.append("    <td>Alejandro Sotelo, Andr�s C�rdoba, Paloma L�pez de mesa</td>");
      sb.append("    <td>Andr�s C�rdoba Melani, Luis Omar Herrera, Martha Susana Jaimes</td>");
      sb.append("  </tr>");
      sb.append("  <tr>");
      sb.append("    <td width=\"40\"></td>");
      sb.append("    <td>Alejandro Sotelo, Lina Ruedas Silva</td>");
      //sb.append("    <td>Alvaro Jose Moreno, Alejandro Sotelo, Lina Ruedas Silva</td>");
      //sb.append("    <td>Martha Susana Jaimes, Laura Cuesta, Luis Omar Herrera Prada</td>");
      sb.append("  </tr>");
      sb.append("  <tr><td colspan=\"2\"></td></tr>");
      //sb.append("  <tr><td colspan=\"2\" bgcolor=\"#D4FFD4\" width=\"600\" align=center><b>SPADIES versi�n 2.4.0</b> - Noviembre 24 de 2006</td></tr>");
      //sb.append("  <tr><td colspan=\"2\" bgcolor=\"#D4FFD4\" width=\"600\" align=center><b>SPADIES versi�n 2.4.0</b> - Junio 13 de 2008</td></tr>");
      //sb.append("  <tr><td colspan=\"2\" bgcolor=\"#D4FFD4\" width=\"600\" align=center><b>SPADIES versi�n 2.4.1</b> - Agosto 26 de 2008</td></tr>");
      //sb.append("  <tr><td colspan=\"2\" bgcolor=\"#D4FFD4\" width=\"600\" align=center><b>SPADIES versi�n " + Constantes.versionMenor + "</b> - Noviembre 27 de 2008</td></tr>");
      sb.append("  <tr><td colspan=\"2\" bgcolor=\"#D4FFD4\" width=\"600\" align=center><b>SPADIES versi�n " + Constantes.versionMenor + "</b> - Agosto 25 de 2011</td></tr>");
      sb.append("  <tr><td colspan=\"2\"></td></tr>");
      sb.append("</table>");
      sb.append("<p>");
      sb.append("</html>");
      labelTitulo.setText(sb.toString());
    }
    {
      JPanel panelDibujo=new MyFlowPane(FlowLayout.CENTER,0,0,labelDibujo);
      JPanel panelTitulo=new MyBoxPane(BoxLayout.X_AXIS,Box.createHorizontalStrut(8),labelTitulo);
      JPanel panelBotones=new MyFlowPane(FlowLayout.RIGHT,5,0,botonCerrar);
      JPanel panelPrincipal=new MyBorderPane(true,8,8,8,8,panelDibujo,null,panelTitulo,null,panelBotones);
      setContentPane(panelPrincipal);
      for (JPanel p:Arrays.asList(panelDibujo,panelTitulo,panelBotones,panelPrincipal)) {
        p.setOpaque(true);
        p.setBackground(Color.WHITE);
      }
    }
    {
      pack();
      setResizable(false);
      Rectangle r=getParent().getBounds();
      Dimension d=getSize();
      setLocation((int)(r.x+((r.width-d.width)/2)),(int)(r.y+((r.height-d.height)/2)));
      setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
  }
}
