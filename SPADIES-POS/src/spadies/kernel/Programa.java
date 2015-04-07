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

import spadies.io.*;
/**
 * Informacion de un programa academico
 */
public final class Programa {
  /**
   * Bytes de una cadena ASCII con el nombre del programa 
   */
  public byte[] nombre={};
  public byte[] codigoSNIES={};
  /**
   * Codigo del area 
   */
  public byte area=-1;
  /**
   * Codigo del nivel 
   */
  public byte nivel=-1;
  /**
   * Metodologia del programa
   */
  public byte metodologia=-1;
  
  public byte duracion=-1;
  public byte nucleo=-1;
  public String toString() {
    return new String(nombre.length>0?nombre:codigoSNIES);
  }
  public static Programa cargar(MyDataInputStream is) throws Exception {
    Programa p=new Programa();
    p.nombre=is.readByteArray(true,-1);
    p.codigoSNIES=is.readByteArray(true,-1);
    p.area=is.readByte();
    p.nivel=is.readByte();
    p.metodologia=is.readByte();
    p.duracion=is.readByte();
    p.nucleo=is.readByte();
    return p;
  }
  public static void guardar(MyDataOutputStream os, Programa p) throws Exception {
    os.writeByteArray(true,p.nombre);
    os.writeByteArray(true,p.codigoSNIES);
    os.writeByte(p.area);
    os.writeByte(p.nivel);
    os.writeByte(p.metodologia);
    os.writeByte(p.duracion);
    os.writeByte(p.nucleo);
  }
  public int getTamanhoEnBytes() {
    return 8+nombre.length+codigoSNIES.length;
  }
}
