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
package spadies.server.util;

import java.io.*;

public final class ConstantesServer {
  public static final File cDatosBloqueados=new File("datos/datosblo");
  public static final File cDatosExternos=new File("datosExternos");
  public static final File cDatosExternosGrandes=new File(cDatosExternos,"fuentesGrandes");
  public static final File cDatosExternosProcesados=new File(cDatosExternos,"fuentesGrandesProcesadas");
  public static final File fICFES=new File(cDatosExternosGrandes,"_ICFES.csv");
  public static final File gICFES=new File(cDatosExternosProcesados,"_ICFES.procesado");
  public static final File fICETEX=new File(cDatosExternosGrandes,"_ICETEX_Info.csv");
  public static final File gICETEX=new File(cDatosExternosProcesados,"_ICETEX_Info.procesado");
  public static final File fGRADUADOS=new File(cDatosExternosGrandes,"_MEN_Graduados.csv");
  public static final File gGRADUADOS=new File(cDatosExternosProcesados,"_MEN_Graduados.procesado");
  public static final File fSBPRO=new File(cDatosExternosGrandes,"_SBPRO.csv");
  public static final File gSBPRO=new File(cDatosExternosProcesados,"_SBPRO.procesado");
  public static final File fPROGRAMAS=new File(cDatosExternos,"_SNIES_programas.csv");
  public static final File fIES=new File(cDatosExternos,"_ies.csv");
  public static final File fIES_ESPECIALES=new File(cDatosExternos,"_iesEspeciales.csv");
  public static final File fDEPARTAMENTOS_DESEMPLEO=new File(cDatosExternos,"_departamentos_desempleo.csv");
  public static final File fCOSTOS2007=new File(cDatosExternos,"Costos2007_codigo_recuperado.csv");
  public static final File fPASSWORDS_IES=new File(cDatosExternos,"_passwordsIES.csv");
  public static final File fPERBLOQUEO_IES=new File(cDatosExternos,"_perbIES.csv");
  public static final File fLOG_ERROR_BLOQUEO=new File(cDatosExternos,"inconsistencias_bloqueo.csv");
  public static final File fDATOS=new File("datos");
  public static final File fDATOSINV=new File("datos/datosinv");
  public static final File fPROMEDIOS=new File("valoresPredeterminadosModelo.txt");
  public static final int TAM_TANDA=250000;
  public static boolean DEBUG_MODE = false;
  public static String DEBUG_MODE_PASSWD="";
}
