package com.example.varo.proyectoandroid;

public class Pregunta {

    private int codigo;
    private String enunciado;
    private String categoria;
    private String respuestaCorrecta;
    private String respuestaIncorrecta1;
    private String respuestaIncorrecta2;
    private String respuestaIncorrecta3;
    private String foto;

    /**
     *
     * @param codigo
     * @param enunciado
     * @param categoria
     * @param respuestaCorrecta
     * @param respuestaIncorrecta1
     * @param respuestaIncorrecta2
     * @param respuestaIncorrecta3
     *
     * Constructor que recibe el codigo de la pregunta
     */
    public Pregunta(int codigo, String enunciado, String categoria, String respuestaCorrecta,
                    String respuestaIncorrecta1, String respuestaIncorrecta2,
                    String respuestaIncorrecta3, String foto) {

        this.codigo = codigo;
        this.enunciado = enunciado;
        this.categoria = categoria;
        this.respuestaCorrecta = respuestaCorrecta;
        this.respuestaIncorrecta1 = respuestaIncorrecta1;
        this.respuestaIncorrecta2 = respuestaIncorrecta2;
        this.respuestaIncorrecta3 = respuestaIncorrecta3;
        this.foto = foto;

    }

    /**
     *
     * @param enunciado
     * @param categoria
     * @param respuestaCorrecta
     * @param respuestaIncorrecta1
     * @param respuestaIncorrecta2
     * @param respuestaIncorrecta3
     *
     * Constructor que no recibe el codigo de la pregunta
     */
    public Pregunta(String enunciado, String categoria, String respuestaCorrecta,
                    String respuestaIncorrecta1, String respuestaIncorrecta2,
                    String respuestaIncorrecta3, String foto) {

        this.enunciado = enunciado;
        this.categoria = categoria;
        this.respuestaCorrecta = respuestaCorrecta;
        this.respuestaIncorrecta1 = respuestaIncorrecta1;
        this.respuestaIncorrecta2 = respuestaIncorrecta2;
        this.respuestaIncorrecta3 = respuestaIncorrecta3;
        this.foto = foto;
    }


    public Pregunta(String enunciado, String categoria, String respuestaCorrecta, String respuestaIncorrecta1, String respuestaIncorrecta2, String respuestaIncorrecta3) {
        this.enunciado = enunciado;
        this.categoria = categoria;
        this.respuestaCorrecta = respuestaCorrecta;
        this.respuestaIncorrecta1 = respuestaIncorrecta1;
        this.respuestaIncorrecta2 = respuestaIncorrecta2;
        this.respuestaIncorrecta3 = respuestaIncorrecta3;

    }

    // GETTERS AND SETTERS

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public String getRespuestaIncorrecta1() {
        return respuestaIncorrecta1;
    }

    public void setRespuestaIncorrecta1(String respuestaIncorrecta1) {
        this.respuestaIncorrecta1 = respuestaIncorrecta1;
    }

    public String getRespuestaIncorrecta2() {
        return respuestaIncorrecta2;
    }

    public void setRespuestaIncorrecta2(String respuestaIncorrecta2) {
        this.respuestaIncorrecta2 = respuestaIncorrecta2;
    }

    public String getRespuestaIncorrecta3() {
        return respuestaIncorrecta3;
    }

    public void setRespuestaIncorrecta3(String respuestaIncorrecta3) {
        this.respuestaIncorrecta3 = respuestaIncorrecta3;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto){
        this.foto = foto;
    }
}
