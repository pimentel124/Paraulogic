package com.example.algoritmia_paraulogic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

/**
 * @author Álvaro Pimentel Lorente
 */

public class MainActivity extends AppCompatActivity {

    // id para añadir valores extras al intent
    public static final String RESPUESTAS = "respuestas";

    // lista de ids de los botones
    private int[] listaIDs;

    // array de vocales para que el juego siempre contenga una para que se puedan formar palabras
    private final char[] vocales = {'A', 'E', 'I', 'O', 'U'};

    // array de letras del juego
    char[] letrasdisp;

    // ArrayList que contiene todos los tutis de esa partida
    private ArrayList<String> tutis;

    // ArrayList que contiene todas las letras del juego
    private UnsortedArraySet<Character> posiblesLetras;

    // Mapping con Arbol de Busqueda Binario que contiene las palabras que el jugador ha encontrado
    // y cuantas veces la ha encontrado
    private BSTMapping<String, Integer> correctasSoluciones = new BSTMapping<>();

    // TreeSet que contiene todas las posibles palabras que puede formar el jugador
    private TreeSet<String> diccionario;

    // String que contiene la palabra que el jugador está formando
    private String encurso = "";

    // StringBuilder para formar el texto con todas las posibles soluciones que se mostrarán en otra actividad
    private StringBuilder todasSoluciones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    /**
     * Método para inicializar los componentes de la actividad.
     * Se genera un array de botones y se guarda su id. Se generan las letras aleatorias y
     * se genera el diccionario podado. En caso de que el diccionario podado no tenga tuti, se
     * vuelve a repetir todo el proceso.
     */
    private void setup() {

        listaIDs = new int[]{
                R.id.Center,
                R.id.topLeft,
                R.id.topRight,
                R.id.Left,
                R.id.Right,
                R.id.bottomLeft,
                R.id.bottomRight};
        boolean isTuti = false;
        while (!isTuti) {
            setLetras();
            gen_Dic();
            isTuti = hayTuti();
        }
        int unicode = 0x2754;
        Button b = findViewById(R.id.solve);
        b.setText(new String(Character.toChars(unicode)));
        updateButtons();
        updateAnswer();
        genTextoSoluciones();
        TextView t = findViewById(R.id.respondidas);
        t.setText("");

    }

    /**
     * Método para generar el texto con todas las posibles soluciones que se mostrarán en otra actividad.
     */
    private void genTextoSoluciones() {
        //generate a string that contains all the words in the dictionary and if the word is in ArrayList tutis, the word is in red
        todasSoluciones = new StringBuilder();
        Iterator<String> it = diccionario.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (tutis.contains(s)) {
                todasSoluciones.append("<font color='red'>");
                todasSoluciones.append(s);
                todasSoluciones.append("</font>");
            }else{
                todasSoluciones.append(s);
            }

            todasSoluciones.append(", ");
        }
        //remove the last comma and space from the string
        todasSoluciones.delete(todasSoluciones.length()-2, todasSoluciones.length());
        todasSoluciones.append(".");

    }

    /**
     * Método para comprobar que una palabra introducida por el usuario es correcta.
     */
    public void esCorrecta(View view) {
        if (diccionario.contains(encurso)) {

            //if the word is in the dictionary, add it to the correct answers
            if (this.correctasSoluciones.get(encurso) == null) {
                System.out.println("Primera vez que se introduce");
                this.correctasSoluciones.put(encurso, 1);
            } else { //Si la palabra ya se ha introducino aumentamos la frecuencia
                this.correctasSoluciones.put(encurso, this.correctasSoluciones.get(encurso) + 1);
                System.out.println("Esta palabra ya se ha introducido");
            }
            updateRespondidas();
            }else {

            Context context = getApplicationContext();
            CharSequence text = " La palabra introducida no es correcta";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
            encurso = "";
            updateAnswer();

    }


    /**
     * Método para generar el set de letras que contendrá el juego
     */
    private void setLetras() {
        letrasdisp = new char[7];
        posiblesLetras = new UnsortedArraySet<>(7);
        Random ran = new Random();
        //añadimos una vocal aleatoria
        char aux = vocales[ran.nextInt(5)];
        posiblesLetras.add(aux);
        letrasdisp[0] = aux;
        //añadimos las letras restantes solo si no estan ya
        int cont = 1;
        while (cont < 7) {
            aux = (char) (ran.nextInt(26) + 'A');
            if (posiblesLetras.add(aux)) {
                letrasdisp[cont] = aux;
                cont++;
            }
        }

        updateButtons();
    }

    /**
     * Método para actualizar los botones de la actividad.
     */
    private void updateButtons() {
        for (int i = 0; i < listaIDs.length; i++) {
            Button b = findViewById(listaIDs[i]);
            b.setText(String.valueOf(letrasdisp[i]));
        }
    }

    /**
     * Método para añadir la letra pulsada del botón a la palabra que se esta construyendo.
     */
    public void addLetra(View view) {
        int id = view.getId();
        Button b = (Button) findViewById(id);
        encurso += b.getText().toString();
        updateAnswer();

    }

    /**
     * Método para actualizar la palabra que se esta construyendo.
     */
    private void updateAnswer() {
        TextView t = findViewById(R.id.introducida);
        t.setText(encurso);
    }

    /**
     * Método para actualizar el texto con las palabras que se han respondido.
     */
    private void updateRespondidas() {
        TextView t = findViewById(R.id.respondidas);

        Iterator pArray = correctasSoluciones.iterator();
        StringBuilder result = new StringBuilder();
        StringBuilder respuesta = new StringBuilder();
        int j = 0;

        while (pArray.hasNext()) {
            BSTMapping.Pair bsts = (BSTMapping.Pair) pArray.next();
            result.append(bsts.getKey()).append(" (").append(bsts.getValor()).append("), ");
            j++;
        }
        //remove the last comma and space from the string
        result.delete(result.length()-2, result.length());

        //add the string to the textview
        respuesta.append("Has trobat ").append(j).append(" paraules correctes: ").append(result).append(".");
        t.setText(respuesta.toString());

    }

    /**
     * Método encargado para eliminar la útima letra de la palabra que se esta construyendo.
     */
    public void removeLetra(View view) {
        //remove the last letter from the string
        if (encurso.length() > 0) {
            encurso = encurso.substring(0, encurso.length() - 1);
        }
        updateAnswer();
    }

    /**
     * Método encargado de mezclar las letras del set exterior de letras
     */
    public void shuffleLetras(View view) {

        char[] temp = new char[letrasdisp.length - 1];
        //copy the array without the fisrs letter
        System.arraycopy(letrasdisp, 1, temp, 0, letrasdisp.length - 1);
        /* Equivalente a:
        for (int i = 1; i < letrasdisp.length; i++) {
            temp[i - 1] = letrasdisp[i];
        }
        */

        //shuffle the array
        for (int i = temp.length - 1; i > 0; i--) {
            int index = (int) (Math.random() * (i + 1));
            char temp2 = temp[index];
            temp[index] = temp[i];
            temp[i] = temp2;

        }
        //copy the array back to letrasdisp

        if (letrasdisp.length - 1 >= 0) {
            System.arraycopy(temp, 0, letrasdisp, 1, temp.length);
        }
        //update the buttons
        updateButtons();

    }

    /**
     * Método encargado de comprobar si hay un tuti en el diccionario podado
     * @return true si hay un tuti en el diccionario podado, false en caso contrario
     */
    private boolean hayTuti() {

        tutis = new ArrayList<>();

        //check if the diccionary has any word formed by all the 7 letters in letrasdisp in any order
        int cont = 0;
        for (String s : diccionario) {
            if (s.length() >= 7) {

                for (int i = 0; i < 7; i++) {
                    if (s.indexOf(letrasdisp[i]) == -1) {
                        break;
                    } else {
                        cont++;
                    }
                }
                if (cont > 6) {
                    tutis.add(s);
                }
                cont = 0;

            }
        }
        return tutis.size() != 0;
    }

    /**
     * Método encargado de generar el diccionario podado a partir del diccionario "catala_filtrat.dic"
     * Para que una palabra sea añadida al diccionario, se debe cumplir que:
     * 1. La palabra tiene la letra central
     * 2. La palabra está únicamente formada por las 7 letras del juego
     */
    private void gen_Dic() {
        diccionario = new TreeSet<>();
        InputStream is = getResources().openRawResource(R.raw.catala_filtrat);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                boolean cumple = false;
                for (int i = 0; i < line.length(); i++) {
                    if (line.toUpperCase().indexOf(letrasdisp[0]) == -1) {
                    break;
                    }
                    if (this.posiblesLetras.contains(line.toUpperCase().charAt(i))) {
                        cumple = true;
                    } else {
                        cumple = false;
                        break;
                    }
                }
                if (cumple) {
                    diccionario.add(line.toUpperCase());
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método encarado de llamar a la nueva actividad para hacer display de todas las soluciones posibles
     */
    public void showInfo(View view) {
        Intent intent = new Intent(this, RespuestasActivity.class);
        intent.putExtra(RESPUESTAS, todasSoluciones.toString());
        startActivity(intent);
    }

}