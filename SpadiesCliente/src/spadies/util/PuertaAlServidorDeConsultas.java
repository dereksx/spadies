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
package spadies.util;

import java.io.*;
import java.net.*;

public final class PuertaAlServidorDeConsultas {
  public static <T> T obtenerResultadoConsulta(int tipoConsulta, Class<T> claseResultado, Object...args) throws MyException {
    T resultado=null;
    Socket socket=null;
    ObjectOutputStream out=null;
    ObjectInputStream in=null;
    try {
      socket=new Socket(Constantes.ipServidorSPADIES,Constantes.puertoServidorConsultas);
      out=new ObjectOutputStream(socket.getOutputStream());
      in=new ObjectInputStream(socket.getInputStream());
      socket.setSoTimeout(Constantes.timeoutServidorConsultas);
    }
    catch (Throwable th) {
      throw new MyException("No se pudo establecer comunicaci�n con el servidor \""+Constantes.ipServidorSPADIES+"\" por el puerto "+Constantes.puertoServidorConsultas+". Revise la configuraci�n de su firewall para permitir a la aplicaci�n comunicarse con el servidor del Ministerio de Educaci�n Nacional o descargue la nueva versi�n de la aplicaci�n SPADIES.");
    }
    try {
      out.writeLong(9165465416546L);
      out.flush();
      out.writeInt(tipoConsulta);
      for (Object obj:args) out.writeObject(obj);
      out.flush();
      resultado=CajaDeHerramientas.readObject(in,out,claseResultado);
      if (in.readLong()!=4816547187991L) throw new Exception("");
    }
    catch (MyException ex) {
      throw ex;
    }
    catch (Throwable th) {
      throw new MyException("Hubo un error en la comunicaci�n con el servidor \""+Constantes.ipServidorSPADIES+"\".["+tipoConsulta+"]", th);
    }
    finally {
      try {
        if (out!=null) out.close();
        if (in!=null) in.close();
        socket.close();
      }
      catch (Throwable th) {
      }
    }
    return resultado;
  }
}
