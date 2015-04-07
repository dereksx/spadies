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
package spadies.tools;

import java.io.File;
import java.io.IOException;

import spadies.kernel.KernelSPADIES;
import spadies.server.util.OperacionesAdministrativas;
import spadies.util.Constantes;
import spadies.util.MyException;

/**
 * Exporta a un formato de CSV usado en la sincronizacion,
 * la informaci�n contenida en los archivos en la carpeta datos. 
 * Los archivos CSV son escritos en una carpeta salidaCSV en la ruta de ejecuci�n
 */
public class ExportarCSVOriginal {
  public static void main(String[] args) throws MyException, IOException {
    Constantes.cargarArchivoFiltroIES();
    KernelSPADIES kernel = KernelSPADIES.getInstance();
    kernel.cargarParaServidor(Constantes.carpetaDatos, Long.MAX_VALUE,true);
    try {
      OperacionesAdministrativas.escribirCSVScompletos(kernel, new File("salidaCSV"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}