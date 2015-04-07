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
package spadies.kernel;

import java.util.*;

public final class EstudianteDAO {
  public final IES ies;
  public final Estudiante datos;
  public final Estudiante_DatosPersonales datosPersonales;
  public final boolean[] estaMatriculado;
  public final double[] repitencias,supervivencias,supervivenciasEstructurales;
  public final int ind;
  public EstudianteDAO(IES pIes, Estudiante pDatos, Estudiante_DatosPersonales pDatosPersonales) {
    this(pIes, pDatos, pDatosPersonales,-1);
  }
  public EstudianteDAO(IES pIes, Estudiante pDatos, Estudiante_DatosPersonales pDatosPersonales, int pInd) {
    ies=pIes;
    datos=pDatos;
    datosPersonales=pDatosPersonales;
    int n=ies.n;
    estaMatriculado=new boolean[n];
    repitencias=datos.getRepitencias();
    supervivencias=new double[n];
    supervivenciasEstructurales=new double[n];
    Arrays.fill(supervivencias,-1d);
    Arrays.fill(supervivenciasEstructurales,-1d);
    ind = pInd;
    int jI=datos.getSemestrePrimiparo();
    if (jI==-1) return;
    double supv=-1d,supvEst=-1d;
    long matri=datos.getSemestresMatriculadoAlDerecho()>>>jI;
    for (int j=jI; j<n; j++,matri>>>=1) if ((matri&1L)==1L) {
      estaMatriculado[j]=true;
      double riesgo=datos.getRiesgo(j),riesgoEst=datos.getRiesgoEstructural(j);
      if (riesgo!=-1d) supv=(supv==-1d?1d:supv)*(1d-riesgo);
      if (riesgoEst!=-1d) supvEst=(supvEst==-1d?1d:supvEst)*(1d-riesgoEst);
      supervivencias[j]=(riesgo==-1d?-1d:supv);
      supervivenciasEstructurales[j]=(riesgoEst==-1d?-1d:supvEst);
    }
  }
}
