package spadies.kernel;

import spadies.util.MyByteSequence;

public class Estudiante_2_6 extends EstudianteAbstract {
  //private static final int tamFijo0 = 13, tamFijo = tamFijo0+8;
  public Estudiante_2_6(int pN, MyByteSequence pDatos) {
    super(pN, pDatos);
  }
  @Override
  protected int getTamFijo() {
    return 13+8;
  }
  @Override
  protected int getTamFijo0() {
    return 13;
  }
  public void setEstrato(int v) {}
  public byte getEstrato() {
    return -1;    
  }
  public void setNivelSisben(int v) {}
  public byte getNivelSisben() {
    return -1;
  }
  public void setPersonasFamilia(int v) {}
  public byte getPersonasFamilia() {
    return -1;
  }
  public void setIngresoHogar2(int v) {}
  public byte getIngresoHogar2() {
    return -1;
  }
  public void setmViviendaPropia(int v) {}
  public byte getmViviendaPropia() {return -1;}

  public void setmTrabajabaCuandoPresentoIcfes(int v) {}
  public byte getmTrabajabaCuandoPresentoIcfes() {return -1;}

  public void setmNivelEducativoMadre(int v) {}
  public byte getmNivelEducativoMadre() {return -1;}

  public void setmIngresoHogar(int v) {}
  public byte getmIngresoHogar() {return -1;}

  public void setmNumeroHermanos(int v) {}
  public byte getmNumeroHermanos() {return -1;}

  public void setmPosicionEntreLosHermanos(int v) {}
  public byte getmPosicionEntreLosHermanos() {return -1;}

  public void setmEstrato(int v) {}
  public byte getmEstrato() {return -1;}

  public void setmNivelSisben(int v) {}
  public byte getmNivelSisben() {return -1;}

  public void setmPersonasFamilia(int v) {}
  public byte getmPersonasFamilia() {return -1;}

  public void setmIngresoHogar2(int v) {}
  public byte getmIngresoHogar2() {return -1;}
}