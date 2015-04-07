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
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import org.jfree.chart.ChartPanel;

import spadies.tools.ReporteIES;
import spadies.util.*;
import spadies.util.variables.*;
import spadies.util.variables.CategoriasVariables.BCategoria;
import spadies.kernel.*;
import spadies.gui.sincronizador.*;
import spadies.gui.util.*;
import spadies.gui.format.Procesador;
import spadies.gui.format.ProcesadorAnalisisSupervivencia;
import spadies.gui.format.ProcesadorAusenciaIntersemestral;
import spadies.gui.format.ProcesadorCaracterizacionEstudiantes;
import spadies.gui.format.ProcesadorConsultaDesercion;
import spadies.gui.format.ProcesadorConsultaGrado;
import spadies.gui.format.ProcesadorCostoDesercion;
import spadies.gui.format.ProcesadorCruceVariables;
import spadies.gui.format.ProcesadorDesercionCohorte;
import spadies.gui.format.ProcesadorDesercionPorPeriodo;
import spadies.gui.format.ResultadoConsulta;
import spadies.gui.imagenes.*;

@SuppressWarnings("serial")
public final class VentanaPrincipalSimple extends JFrame implements Observer {
  private static enum TIPO_SELECCION {TS_IES,TS_AGREGADO};
  private static final VentanaPrincipalSimple instance=new VentanaPrincipalSimple();
  public static VentanaPrincipalSimple getInstance() {return instance;}
  private final KernelSPADIES kernel=KernelSPADIES.getInstance();
  private final MyMenuBar menuBar=new MyMenuBar();
  private final JTabbedPane panelCentro=new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);
  private final JComboBox comboSeleccion=new JComboBox(new String[]{"IES","Agregado"});
  private final MyPanelSeleccion panelsSeleccion1[]=new MyPanelSeleccion[1];
  private final MyPanelSeleccion panelsSeleccion2[]=new MyPanelSeleccion[3];
  private final Variable criterios1[]={Variable.CODIGO_IES};
  private final Variable criterios2[]={Variable.DEPARTAMENTO_IES,Variable.ORIGEN_IES,Variable.CARACTER_IES};
  /**
   * Checks de diferenciaci�n de IES
   */
  private final JCheckBox checksDiferenciacion1[]=new JCheckBox[1];
  /**
   * Checks de diferenciacion nacional
   */
  private final JCheckBox checksDiferenciacion2[]=new JCheckBox[3];
  private final JCheckBox checksDiferenciacion1x[]=new JCheckBox[1],checksDiferenciacion1y[]=new JCheckBox[1];
  private final JCheckBox checksDiferenciacion2x[]=new JCheckBox[3],checksDiferenciacion2y[]=new JCheckBox[3];
  private final JPanel panelIES,panelAgregado;
  private TIPO_SELECCION seleccion=TIPO_SELECCION.TS_IES;
  private final CardLayout cardSeleccion=new CardLayout();
  private final JPanel panelSeleccion=new JPanel(cardSeleccion);
  private final PanelDatosCargados panelDatosCargados=new PanelDatosCargados();
  private final PanelInformacionBasica panelInformacionBasica=new PanelInformacionBasica();
  private final PanelConsultasGraficas panelConsultasGraficas=new PanelConsultasGraficas();
  private final PanelConsultasGraficasSemestrales panelConsultasGraficasSemestrales=new PanelConsultasGraficasSemestrales();
  private final PanelCruceVariables panelCruceVariables=new PanelCruceVariables();
  private final PanelAnalisisRiesgo panelAnalisisRiesgo=new PanelAnalisisRiesgo();
  private final PanelConsultasDesercionIntersemestral panelConsultasDesercionIntersemestral=new PanelConsultasDesercionIntersemestral();
  private final PanelTasaDesercion panelTasaDesercion = new PanelTasaDesercion();
  private final PanelDesercionPorCohorte panelDesercionPeriodo = new PanelDesercionPorCohorte();
  private final PanelConsultasCostoDesercion panelConsultasCostoDesercion = new PanelConsultasCostoDesercion();
  private final PanelDesercionPorCohorteTajada panelDesercionCT = new PanelDesercionPorCohorteTajada();
  private final PanelConsultasDesercion panelDesercion = new PanelConsultasDesercion();
  private final PanelSupervivenciaTajada panelSupervivenciaT = new PanelSupervivenciaTajada();
  private final Collection<JPanel>[] panelesSeleccion = new Collection[]{
      new LinkedList<JPanel>(),new LinkedList<JPanel>(),new LinkedList<JPanel>()};
  private final Collection<JPanel> panelesXY = new LinkedList<JPanel>();
  private ArbolVariables panelSeleccionV = null;
  private VentanaPrincipalSimple() {
    super(Constantes.nombreAplicacionLargo);
    VentanaPresentacion.getInstance();  // Para adicionar VentanaPresentacion.getInstance() como observador del kernel
    kernel.addObserver(this);
    setIconImage(Imagenes.IM_ICONO_APLICACION.getImagen().getImage());
    AmbienteVariables.getInstance().notificarCambioSeleccion(new Filtro[0]);
    System.out.println(Variable.NUMERO_SEMESTRE_PER.rango.getRango().length);
    panelSeleccionV = new ArbolVariables(CategoriasVariables.variablesEnCategorias(CategoriasVariables.TODO_VARIABLES));
    int botonesH = 3;
    JPanel botonera = new JPanel(new GridLayout(0, botonesH));
    {
      int pos = 0;
      //botonera.add(new JButton("Individuos cargados"));
      //botonera.add(new JButton("Consultas b�sicas"));
      //botonera.add(new BotonProcesador("Deserci�n por cohorte",ProcesadorDesercionCohorte.instance));
      botonera.add(new BotonProcesador("Deserci�n por cohorte",ProcesadorConsultaDesercion.instance));
      botonera.add(new BotonProcesador("Deserci�n por per�odo",ProcesadorDesercionPorPeriodo.instance));
      botonera.add(new BotonProcesador("Caracterizaci�n de los estudiantes",ProcesadorCaracterizacionEstudiantes.instance));
      botonera.add(new BotonProcesador("Grado por cohorte",ProcesadorConsultaGrado.instance));
      botonera.add(new BotonProcesador("Ausencia intersemestral",ProcesadorAusenciaIntersemestral.instance));
      botonera.add(new BotonProcesador("An�lisis de supervivencia",ProcesadorAnalisisSupervivencia.instance));
      //botonera.add(new JButton("Informaci�n por individuo"));
      botonera.add(new BotonProcesador("Costos monetarios de la deserci�n",ProcesadorCostoDesercion.instance));
      botonera.add(new BotonProcesador("Cruce de variables",ProcesadorCruceVariables.instance));
    }
    {
      //panelCentro.addTab("Individuos cargados",panelDatosCargados);//0
      //panelCentro.addTab("Consultas b�sicas",panelInformacionBasica);//1
      
      //panelCentro.addTab("Deserci�n por cohorte",panelDesercionPeriodo);//8
      //panelCentro.addTab("Deserci�n por per�odo",panelTasaDesercion);//7
      //panelCentro.addTab("Ausencia intersemestral",panelConsultasDesercionIntersemestral);//6
      
      ///panelCentro.addTab("An�lisis de supervivencia",panelConsultasGraficas);//2
      //panelCentro.addTab("Caracterizaci�n de los estudiantes",panelConsultasGraficasSemestrales);//3
      //panelCentro.addTab("Informaci�n por individuo",panelAnalisisRiesgo);//4
      
      //panelCentro.addTab("Cruce de variables",panelCruceVariables);//5
      //panelCentro.addTab("Costos monetarios de la deserci�n",panelConsultasCostoDesercion);//9
      /*
      panelCentro.addTab("Tasa deserci�n 2",panelDesercionCT);
      
      panelCentro.addTab("Deserci�n",panelDesercion);
      panelCentro.addTab("An�lisis de supervivencia 2",panelSupervivenciaT);
      */
      
      panelesSeleccion[0].addAll(Arrays.asList(panelDesercionPeriodo,
          panelTasaDesercion,panelConsultasDesercionIntersemestral,
          panelConsultasGraficas,panelConsultasGraficasSemestrales,
          panelConsultasCostoDesercion,panelDesercionCT,panelCruceVariables,
          panelDesercion
        ));
      panelesSeleccion[1].addAll(Arrays.asList(panelInformacionBasica));
      panelesSeleccion[2].addAll(Arrays.asList(panelDatosCargados,panelAnalisisRiesgo));
      panelesXY.add(panelCruceVariables);
    }
    panelsSeleccion1[0]=new MyPanelSeleccion(criterios1[0].items,true,180,200);
    for (int i=0; i<3; i++) panelsSeleccion2[i]=new MyPanelSeleccion(criterios2[i].items,true,180,i==0?200:100);
    for (int i=1; i<3; i++) panelsSeleccion2[i].setMaximumSize(new Dimension(1000,100));
    for (JCheckBox[] arrCB:new JCheckBox[][]{checksDiferenciacion1,checksDiferenciacion2}) {
      for (int i=0; i<arrCB.length; i++) {
        JCheckBox cb=new JCheckBox("Diferenciado?",false);
        arrCB[i]=cb;
        cb.setFont(cb.getFont().deriveFont(Font.BOLD));
        cb.setPreferredSize(new Dimension(cb.getPreferredSize().width,18));
      }
    }
    for (JCheckBox[] arrCB:new JCheckBox[][]{checksDiferenciacion1x,checksDiferenciacion2x}) {
      for (int i=0; i<arrCB.length; i++) {
        JCheckBox cb=new JCheckBox("Eje X",false);
        arrCB[i]=cb;
        cb.setFont(cb.getFont().deriveFont(Font.BOLD));
        cb.setPreferredSize(new Dimension(cb.getPreferredSize().width,18));
      }
    }
    for (JCheckBox[] arrCB:new JCheckBox[][]{checksDiferenciacion1y,checksDiferenciacion2y}) {
      for (int i=0; i<arrCB.length; i++) {
        JCheckBox cb=new JCheckBox("Eje Y",false);
        arrCB[i]=cb;
        cb.setFont(cb.getFont().deriveFont(Font.BOLD));
        cb.setPreferredSize(new Dimension(cb.getPreferredSize().width,18));
      }
    }
    panelIES=new MyBoxPane(BoxLayout.Y_AXIS,new MyFlowPane(0,0,new MyLabel("<html><i><b><font color=\"#007000\">"+CajaDeHerramientas.stringToHTML(criterios1[0].nombre)+"</font></b></i></html>")),new MyFlowPane(0,0,checksDiferenciacion1[0],checksDiferenciacion1x[0],checksDiferenciacion1y[0]),panelsSeleccion1[0]);
    Collection<Component> componentes2=new LinkedList<Component>();
    for (int i=0; i<3; i++) {
      if (i>0) componentes2.add(Box.createVerticalStrut(5));
      componentes2.add(new MyFlowPane(0,0,new MyLabel("<html><i><b><font color=\"#007000\">"+CajaDeHerramientas.stringToHTML(criterios2[i].nombre)+"</font></b></i></html>")));
      componentes2.add(new MyFlowPane(0,0,checksDiferenciacion2[i]));
      componentes2.add(new MyFlowPane(0,0,checksDiferenciacion2x[i]));
      componentes2.add(new MyFlowPane(0,0,checksDiferenciacion2y[i]));
      componentes2.add(panelsSeleccion2[i]);
    }
    panelAgregado=new MyBoxPane(BoxLayout.Y_AXIS,componentes2.toArray(new Component[0]));
    {
      comboSeleccion.setPreferredSize(new Dimension(comboSeleccion.getPreferredSize().width,19));
      comboSeleccion.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          int k=comboSeleccion.getSelectedIndex();
          if (k==-1) comboSeleccion.setSelectedIndex(k=0);
          if (k==0) {
            if (estaSeleccionandoIES()) return;
            cardSeleccion.show(panelSeleccion,"IES");
            seleccion=TIPO_SELECCION.TS_IES;
            actualizarPanels();
          }
          else {
            if (estaSeleccionandoAgregado()) return;
            cardSeleccion.show(panelSeleccion,"AGREGADO");
            seleccion=TIPO_SELECCION.TS_AGREGADO;
            actualizarPanels();
          }
        }
      });
    }
    TableModelListener tml=new TableModelListener() {
      public void tableChanged(TableModelEvent e) {
        actualizarPanels();
      }
    };
    ActionListener al=new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        actualizarPanels();
      }
    };
    ActionListener alx=new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JCheckBox comp = (JCheckBox) e.getSource();
        if (comp.isSelected()) {
          panelCruceVariables.limpiarDifereciacionX();
          for (JCheckBox chk:checksDiferenciacion1x) if (comp!=chk) chk.setSelected(false);
          for (JCheckBox chk:checksDiferenciacion2x) if (comp!=chk) chk.setSelected(false);
        }
        actualizarPanels();
      }
    };
    ActionListener aly=new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JCheckBox comp = (JCheckBox) e.getSource();
        if (comp.isSelected()) {
          panelCruceVariables.limpiarDifereciacionY();
          for (JCheckBox chk:checksDiferenciacion1y) if (comp!=chk) chk.setSelected(false);
          for (JCheckBox chk:checksDiferenciacion2y) if (comp!=chk) chk.setSelected(false);
        }
        actualizarPanels();
      }
    };
    for (MyPanelSeleccion mps:panelsSeleccion1) mps.getTabla().getModel().addTableModelListener(tml);
    for (MyPanelSeleccion mps:panelsSeleccion2) mps.getTabla().getModel().addTableModelListener(tml);
    for (JCheckBox cb:checksDiferenciacion1) cb.addActionListener(al);
    for (JCheckBox cb:checksDiferenciacion2) cb.addActionListener(al);
    for (JCheckBox cb:checksDiferenciacion1x) cb.addActionListener(alx);
    for (JCheckBox cb:checksDiferenciacion2x) cb.addActionListener(alx);
    for (JCheckBox cb:checksDiferenciacion1y) cb.addActionListener(aly);
    for (JCheckBox cb:checksDiferenciacion2y) cb.addActionListener(aly);
    panelSeleccion.add(panelIES,"IES");
    panelSeleccion.add(panelAgregado,"AGREGADO");
    JPanel panelImagenAplicacion=new MyBoxPane(BoxLayout.X_AXIS,new ImagenSPADIES(500));
    MyBorderPane panelArriba=new MyBorderPane(false,0,0,0,0,null,null,panelImagenAplicacion,null,null);
    MySplitPane splitPrincipal=new MySplitPane(JSplitPane.HORIZONTAL_SPLIT,false,
        /*
        new MyBoxPane(BoxLayout.Y_AXIS,
            new MyFlowPane(0,0,Box.createVerticalStrut(3),
                new MyLabel("<html><i><b><font color=\"#000070\">Selecci�n del tipo de an�lisis</font></b></i></html>")),
                comboSeleccion,
                Box.createVerticalStrut(6),
                new MyLinePane(),
                Box.createVerticalStrut(3),
                new MyBoxPane(BoxLayout.Y_AXIS,new JScrollPane(panelSeleccionV))),
                */
        new MyBorderPane(false, 0, 0, 0, 0, 
          new MyFlowPane(0,0,Box.createVerticalStrut(3),
              new MyLabel("<html><i><b><font color=\"#000070\">Selecci�n del tipo de an�lisis</font></b></i></html>"),
              comboSeleccion
          ),null,
          new MyBoxPane(BoxLayout.Y_AXIS,
              Box.createVerticalStrut(6),
              new MyLinePane(),
              Box.createVerticalStrut(3),
              new MyBoxPane(BoxLayout.Y_AXIS,new JScrollPane(panelSeleccionV))),null,null),
        panelCentro,true);
    splitPrincipal.setDividerLocation(300);
    MyBorderPane panelPrincipal=new MyBorderPane(false,0,0,0,0,panelArriba,null,splitPrincipal,null,botonera);
    panelImagenAplicacion.setBackground(new Color(204,204,204));
    panelArriba.setBackground(new Color(204,204,204));
    {
      setJMenuBar(menuBar);
      setContentPane(panelPrincipal);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
      setPreferredSize(new Dimension(d.width-10,d.height-80));
      pack();
      setLocation(5,5);
    }
    /*
    panelCentro.setSelectedIndex(0);
    desactivarSelecciones(0);
    panelCentro.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent arg0) {
        desactivarSelecciones(panelCentro.getSelectedIndex());
      }
    });
    */
    
  }
  public void update(Observable obs, Object arg) {
    if (!arg.equals("CARGA")) return;
    panelsSeleccion1[0].setValores(criterios1[0].items,true);
    AmbienteVariables.getInstance().notificarCambioSeleccion(new Filtro[0]);
    //panelSeleccionV = new ArbolVariables(CategoriasVariables.variablesEnCategorias(CategoriasVariables.TODO_VARIABLES));
    panelSeleccionV.repoblar(CategoriasVariables.variablesEnCategorias(CategoriasVariables.TODO_VARIABLES));
  }
  private void habilitarCheckBox(JCheckBox cb, boolean habilitado) {
    if (!habilitado) {
      cb.setSelected(false);
      cb.setEnabled(false);
      cb.setForeground(new Color(160,160,160));
    }
    else {
      cb.setEnabled(true);
      cb.setForeground(Color.BLACK);
    }
  }
  private void habilitarCheckBoxVis(JCheckBox cb, boolean habilitado) {
    habilitarCheckBox(cb, habilitado);
    cb.setVisible(habilitado);
  }
  private void desactivarSelecciones(int indTab) {
    JPanel comp = (JPanel) panelCentro.getComponentAt(indTab);
    boolean selecXY = panelesXY.contains(comp);
    for (JCheckBox[] cDif:new JCheckBox[][]{checksDiferenciacion1x,checksDiferenciacion1y,checksDiferenciacion2x,checksDiferenciacion2y})
      for (JCheckBox cb:cDif)
        habilitarCheckBoxVis(cb,selecXY);
    for (JCheckBox[] cDif:new JCheckBox[][]{checksDiferenciacion1,checksDiferenciacion2})
      for (JCheckBox cb:cDif)
        habilitarCheckBoxVis(cb,!selecXY);
    if (panelesSeleccion[2].contains(comp)) {
      comboSeleccion.setSelectedIndex(0);
      comboSeleccion.setEnabled(false);
      habilitarCheckBox(checksDiferenciacion1[0],false);
    }
    else if (panelesSeleccion[1].contains(comp)) {
      comboSeleccion.setEnabled(true);
      habilitarCheckBox(checksDiferenciacion1[0],false);
      for (JCheckBox cb:checksDiferenciacion2) habilitarCheckBox(cb,false);
    }
    else if (panelesSeleccion[0].contains(comp)) {
      comboSeleccion.setEnabled(true);
      habilitarCheckBox(checksDiferenciacion1[0],true);
      for (JCheckBox cb:checksDiferenciacion2) habilitarCheckBox(cb,true);
    }
  }
  private void actualizarPanels() {
    AmbienteVariables.getInstance().notificarCambioSeleccion(estaSeleccionandoIES()?new Filtro[]{new Filtro(criterios1[0],panelsSeleccion1[0].getItemsSeleccionados())}:null);
    panelInformacionBasica.actualizar();
    panelDatosCargados.actualizar();
    panelConsultasGraficas.actualizar();
    panelConsultasGraficasSemestrales.actualizar();
    panelAnalisisRiesgo.actualizar();
    panelCruceVariables.actualizar();
    panelConsultasDesercionIntersemestral.actualizar();
    panelTasaDesercion.actualizar();
    panelDesercionPeriodo.actualizar();
    panelConsultasCostoDesercion.actualizar();
    panelDesercionCT.actualizar();
    panelDesercion.actualizar();
    panelSupervivenciaT.actualizar();
  }
  public boolean estaSeleccionandoIES() {
    return seleccion==TIPO_SELECCION.TS_IES;
  }
  public boolean estaSeleccionandoAgregado() {
    return seleccion==TIPO_SELECCION.TS_AGREGADO;
  }
  public void getSeleccion(EnumSet<Variable> mDiferenciados, EnumMap<Variable,Filtro> mFiltros) throws MyException {
    boolean b=estaSeleccionandoIES();
    Variable[] arrVE=b?criterios1:criterios2;
    JCheckBox[] arrCB=b?checksDiferenciacion1:checksDiferenciacion2;
    MyPanelSeleccion[] arrPS=b?panelsSeleccion1:panelsSeleccion2;
    RutinasGUI.getSeleccion(arrVE,arrCB,arrPS,mDiferenciados,mFiltros);
  }
  public EnumMap<Variable,Filtro> getSeleccionPura(EnumMap<Variable,Filtro> mFiltros) {
    boolean b=estaSeleccionandoIES();
    Variable[] arrVE=b?criterios1:criterios2;
    MyPanelSeleccion[] arrPS=b?panelsSeleccion1:panelsSeleccion2;
    for (int i=0,t=arrPS.length; i<t; i++) mFiltros.put(arrVE[i],new Filtro(arrVE[i],arrPS[i].getItemsSeleccionados()));
    return mFiltros;
  }
  public Variable[] getCriterios() {
    return estaSeleccionandoIES()?criterios1:criterios2;
  }
  public JCheckBox[] getChecksDiferenciacion() {
    return estaSeleccionandoIES()?checksDiferenciacion1:checksDiferenciacion2;
  }
  public JCheckBox[] getChecksDiferenciacionX() {
    return estaSeleccionandoIES()?checksDiferenciacion1x:checksDiferenciacion2x;
  }
  public JCheckBox[] getChecksDiferenciacionY() {
    return estaSeleccionandoIES()?checksDiferenciacion1y:checksDiferenciacion2y;
  }
  public MyPanelSeleccion[] getPanelsSeleccion() {
    return estaSeleccionandoIES()?panelsSeleccion1:panelsSeleccion2;
  }
  private final class MyMenuBar extends JMenuBar {
    public MyMenuBar() {
      MyMenu mOp=new MyMenu("Operaciones",KeyEvent.VK_O);
      MyMenu mEx=new MyMenu("Extras",KeyEvent.VK_E);
      MyMenu mAy=new MyMenu("Ayuda",KeyEvent.VK_Y);
      MyMenuItem botonSc=new MyMenuItem("Sincronizar","Sincroniza el sistema con los datos en la carpeta "+Constantes.carpetaCSV.getPath()+" y con el Ministerio de Educaci�n Nacional",KeyEvent.VK_Z,null,null);
      MyMenuItem botonRe=new MyMenuItem("Generar reporte","_",0,null,null);
      MyMenuItem botonCf=new MyMenuItem("Configuraci�n","Cambiar la configuraci�n b�sica de la aplicaci�n",KeyEvent.VK_F,null,null);
      MyMenuItem botonSl=new MyMenuItem("Salir","Cierra la aplicaci�n",KeyEvent.VK_S,null,null);
      MyMenuItem botonPr=new MyMenuItem("Iniciar presentaci�n","Inicia una presentaci�n por diapositivas del proyecto",KeyEvent.VK_P,null,null);
      MyMenuItem botonAc=new MyMenuItem("Acerca de","Despliega el 'Acerca de' la aplicaci�n",KeyEvent.VK_C,null,null);
      botonSc.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (!RutinasGUI.desplegarPregunta(VentanaPrincipalSimple.this,"<html>�Desea sincronizar el sistema con los datos en la carpeta "+CajaDeHerramientas.stringToCSV(Constantes.carpetaCSV.getPath())+" y con el Ministerio de Educaci�n Nacional? La operaci�n puede tardar varios minutos.</html>")) return;
          new Sincronizador().realizarSincronizacion();
        }
      });
      botonRe.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (!RutinasGUI.desplegarPregunta(VentanaPrincipalSimple.this,"<html>�Desea generar el reporte de los datos?</html>")) return;
          try {
            ReporteIES.salida();
          } catch (MyException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
          //new Sincronizador().realizarSincronizacion();
        }
      });
      botonCf.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          new VentanaConfiguracion(VentanaPrincipalSimple.getInstance()).setVisible(true);
        }
      });
      botonSl.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          System.exit(0);
        }
      });
      botonPr.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          VentanaPresentacion.getInstance().setVisible(true);
        }
      });
      botonAc.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          VentanaAcercaDe.getInstance().setVisible(true);
        }
      });
      if (Constantes.carpetaCSV.exists() && Constantes.carpetaCSV.isDirectory()) {
        mOp.add(botonSc);
        mOp.addSeparator();
      }
      //mOp.add(botonRe);
      mOp.add(botonCf);
      mOp.addSeparator();
      mOp.add(botonSl);
      mEx.add(botonPr);
      mAy.add(botonAc);
      add(mOp);
      add(mEx);
      add(mAy);
    }
  }
  public void limpiarDifereciacionX() {
    for (JCheckBox[] chkA: new JCheckBox[][]{checksDiferenciacion1x, checksDiferenciacion2x})
      for (JCheckBox chk:chkA) chk.setSelected(false);
  }
  public void limpiarDifereciacionY() {
    for (JCheckBox[] chkA: new JCheckBox[][]{checksDiferenciacion1y, checksDiferenciacion2y})
      for (JCheckBox chk:chkA) chk.setSelected(false);
  }
  private class BotonProcesador extends JButton implements ActionListener {
    private Procesador pr;
    public BotonProcesador(String txt,Procesador pr) {
      super(txt);
      this.pr = pr;
      RutinasGUI.configurarBoton(this,new Color(245,245,255),16,-1,23);
      this.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e) {
      setVisible(true);
      boolean alMen = estaSeleccionandoAgregado();
      pr.setParametros(alMen, panelSeleccionV.getDiferenciados(), panelSeleccionV.getFiltrosVariable());
      new MyDialogProgreso(VentanaPrincipalSimple.getInstance(),"Realizando la consulta"+(alMen?" en el Ministerio de Educaci�n Nacional":""),false) {
        public void ejecutar() {
          final MyDialogProgreso mdp = this;
          new Thread() {
            public void run() {
              Window window=mdp;
              try {
                Collection<ResultadoConsulta> res = pr.generarGrafica();
                panelCentro.removeAll();
                for (ResultadoConsulta resc:res) {
                  if (resc.resultado instanceof InfoTabla)
                    panelCentro.add(resc.nombre, new MyPanelTabla((InfoTabla) resc.resultado,100,100,60));
                  else if (resc.resultado instanceof ChartPanel)
                    panelCentro.add(resc.nombre, (ChartPanel)resc.resultado);
                }
              }
              catch (MyException ex) {
                RutinasGUI.desplegarError(window,"<html>"+CajaDeHerramientas.stringToHTML(ex.getMessage())+"</html>");
                window.dispose();
              }
              catch (OutOfMemoryError err) {
                RutinasGUI.desplegarError(window,"<html>Memoria RAM insuficiente para ejecutar el proceso.</html>");
                window.dispose();
              }
              catch (Throwable th) {
                th.printStackTrace();
                RutinasGUI.desplegarError(window,"<html>Hubo un error realizando la consulta.</html>");
                window.dispose();
              }
            }
          }.start();
        }
      }.ejecutar();
    }
  }
}