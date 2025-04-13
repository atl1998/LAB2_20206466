package com.example.lab2_20206466;

import java.util.ArrayList;
import java.util.Collections;

public class ManejadorQuiz {
    private ArrayList<Pregunta> preguntas;
    private int indicePreguntaActual = 0;
    private int puntaje = 0;
    private ArrayList<Integer> respuestasUsuario = new ArrayList<>();

    public ManejadorQuiz(String tema) {
        preguntas = generarPreguntas(tema);
        mezclarPreguntas();

        // Inicializar respuestas del usuario
        for (int i = 0; i < preguntas.size(); i++) {
            respuestasUsuario.add(-1);
        }
    }

    public String obtenerPreguntaActual() {
        return preguntas.get(indicePreguntaActual).texto;
    }

    public String[] obtenerOpcionesActuales() {
        return preguntas.get(indicePreguntaActual).opciones;
    }

    public int obtenerNumeroPregunta() {
        return indicePreguntaActual;
    }

    public void responderPregunta(int indiceRespuesta) {
        respuestasUsuario.set(indicePreguntaActual, indiceRespuesta);

        if (esRespuestaCorrecta(indiceRespuesta)) {
            puntaje += 2;
        } else {
            puntaje -= 2;
        }
    }

    public boolean esRespuestaCorrecta(int indiceRespuesta) {
        return indiceRespuesta == obtenerIndiceRespuestaCorrecta();
    }

    public int obtenerIndiceRespuestaCorrecta() {
        return preguntas.get(indicePreguntaActual).respuestaCorrecta;
    }

    public int obtenerRespuestaUsuario() {
        return respuestasUsuario.get(indicePreguntaActual);
    }

    public boolean preguntaRespondida() {
        return respuestasUsuario.get(indicePreguntaActual) != -1;
    }

    public boolean siguientePregunta() {
        if (indicePreguntaActual < preguntas.size() - 1) {
            indicePreguntaActual++;
            return true;
        }
        return false;
    }

    public void anteriorPregunta() {
        if (indicePreguntaActual > 0) {
            indicePreguntaActual--;
        }
    }

    public boolean hayPreguntaAnterior() {
        return indicePreguntaActual > 0;
    }

    public int obtenerPuntaje() {
        return puntaje;
    }

    private ArrayList<Pregunta> generarPreguntas(String tema) {
        ArrayList<Pregunta> preguntas = new ArrayList<>();

        if (tema.equals("Redes")) {
            preguntas.add(new Pregunta(
                    "¿Qué protocolo se utiliza para cargar páginas web?",
                    new String[]{"HTTP", "FTP", "SMTP"}, 0));

            preguntas.add(new Pregunta("¿Cuál de estas es una dirección IP privada?",
                    new String[]{"192.168.1.1", "8.8.8.8", "142.250.190.46"}, 0));
            preguntas.add(new Pregunta("¿Qué dispositivo conecta redes diferentes y dirige el tráfico?",
                    new String[]{"Router", "Switch", "Access Point"}, 0));
            preguntas.add(new Pregunta("¿Qué significa DNS?",
                    new String[]{"Domain Name System", "Data Network Server", "Digital Network System"}, 0));
            preguntas.add(new Pregunta("¿Qué tipo de red cubre un área pequeña, como una oficina?",
                    new String[]{"LAN", "WAN", "MAN"}, 0));
        }
        else if (tema.equals("Ciberseguridad")) {
            preguntas.add(new Pregunta("¿Qué es un Ransomware?",
                    new String[]{"Un virus que cifra archivos", "Un spyware", "Un troyano"}, 0));
            preguntas.add(new Pregunta("¿Cuál es el objetivo del phishing?",
                    new String[]{"Robar información", "Proteger la red", "Actualizar software"}, 0));
            preguntas.add(new Pregunta("¿Qué protocolo cifra las comunicaciones web?",
                    new String[]{"HTTPS", "HTTP", "FTP"}, 0));
            preguntas.add(new Pregunta("¿Qué algoritmo de cifrado es asimétrico y se usa comúnmente para firmas digitales?",
                    new String[]{"RSA", "AES", "MD5"}, 0));
            preguntas.add(new Pregunta("¿Para qué sirve un firewall?",
                    new String[]{"Proteger la red", "Imprimir documentos", "Conectar dispositivos"}, 0));

        }
        else if (tema.equals("Microondas")) {
            preguntas.add(new Pregunta("¿En qué rango de frecuencias suelen operar las redes Wi-Fi?",
                    new String[]{"2.4 GHz y 5 GHz", "10 GHz y 20 GHz", "900 MHz y 1.8 GHz"}, 0));
            preguntas.add(new Pregunta("¿Qué problema es común en enlaces de microondas?",
                    new String[]{"Interferencia", "Velocidad lenta", "Demora en encendido"}, 0));
            preguntas.add(new Pregunta("¿Qué es la zona de Fresnel en microondas?",
                    new String[]{"Área entre transmisor y receptor", "Canal de datos", "Fuente de alimentación"}, 0));
            preguntas.add(new Pregunta("¿Qué ventaja tienen las comunicaciones por microondas?",
                    new String[]{"Alta capacidad", "Bajo voltaje", "Gran tamaño"}, 0));
            preguntas.add(new Pregunta("¿Qué dispositivo se usa para enfocar señales de microondas?",
                    new String[]{"Antena parabólica", "Switch", "Módem"}, 0));
        }

        return preguntas;
    }

    private void mezclarPreguntas() {
        Collections.shuffle(preguntas);

        // Mezclamos opciones de cada pregunta
        for (Pregunta pregunta : preguntas) {
            pregunta.mezclarOpciones();
        }
    }

    // Clase interna para representar una pregunta
    private static class Pregunta {
        String texto;
        String[] opciones;
        int respuestaCorrecta;

        Pregunta(String texto, String[] opciones, int respuestaCorrecta) {
            this.texto = texto;
            this.opciones = opciones;
            this.respuestaCorrecta = respuestaCorrecta;
        }

        void mezclarOpciones() {
            // Guardamos la respuesta correcta
            String respuestaCorrectaTexto = opciones[respuestaCorrecta];

            // Mezclamos las opciones
            ArrayList<String> opcionesLista = new ArrayList<>();
            for (String opcion : opciones) {
                opcionesLista.add(opcion);
            }
            Collections.shuffle(opcionesLista);

            // Actualizamos el arreglo de opciones
            opciones = opcionesLista.toArray(new String[0]);

            // Actualizamos el índice de la respuesta correcta
            for (int i = 0; i < opciones.length; i++) {
                if (opciones[i].equals(respuestaCorrectaTexto)) {
                    respuestaCorrecta = i;
                    break;
                }
            }
        }
    }
}