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
import javax.swing.*;
import spadies.gui.util.*;
import spadies.util.*;

@SuppressWarnings("serial")
public class VentanaConfiguracion extends MyDialog {
  private final MyLabel labelTitulo=new MyLabel("<html>Configuraci�n de "+CajaDeHerramientas.stringToHTML(Constantes.nombreAplicacion)+"</html>");
  private final JTextField campoIP=new JTextField(12);
  private final JLabel labelIP=new JLabel("Direcci�n IP del servidor del Ministerio de Educaci�n Nacional:");
  private MyButton botonAceptar=new MyButton("Aceptar","Modifica la configuraci�n de "+Constantes.nombreAplicacion,KeyEvent.VK_A);
  private MyButton botonCancelar=new MyButton("Cancelar","Cancela la operaci�n.",KeyEvent.VK_C);
  public VentanaConfiguracion(JFrame padre) {
    super(padre,"CONFIGURACI�N",TipoBloqueo.TB_MODAL);
    campoIP.setText(Constantes.ipServidorSPADIES);
    campoIP.setFont(new Font("Courier",Font.PLAIN,14));
    botonAceptar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Constantes.ipServidorSPADIES=campoIP.getText().trim();
        Constantes.guardarArchivoConfiguracion();
        VentanaConfiguracion.this.dispose();
      }
    });
    botonCancelar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        VentanaConfiguracion.this.dispose();
      }
    });
    JPanel panelBotones=new MyFlowPane(FlowLayout.RIGHT,5,0,botonAceptar,botonCancelar);
    setContentPane(new MyBorderPane(false,8,8,8,8,labelTitulo,null,new MyBoxPane(BoxLayout.Y_AXIS,Box.createVerticalStrut(8),new MyFlowPane(0,0,labelIP),campoIP,Box.createVerticalStrut(8)),null,panelBotones));
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
