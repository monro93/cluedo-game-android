package com.example.albert.cluedo;

import android.graphics.Color;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Classe que llegeix XML
 */
public class LectorXML {

    public static ArrayList<Arma> llegueixArmas(InputStream in){
        ArrayList<Arma> armes = new ArrayList<>();
        XmlPullParser parser = Xml.newPullParser();
       try {
           parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
           parser.setInput(in, null);
           parser.nextTag();
           while (parser.next() != XmlPullParser.END_TAG) {
               if(parser.getEventType() != XmlPullParser.START_TAG){
                   continue;
               }
               String name = parser.getName();
               if(name.equals("arma")){
                   armes.add(new Arma(parser.nextText()));
               }else{
                   skip(parser);
               }
           }
       }catch(XmlPullParserException | java.io.IOException e){
           Log.e("XML ERROR", "Error: "+e.getMessage());
       }finally{
           try{
               in.close();
           }catch(IOException e) {
               Log.e("XML ERROR", "Si, ha fallado el finally.");
           }
       }
        return armes;
    }

    public static ArrayList<Sospitos> llegueixSospitosos(InputStream in){
        XmlPullParser parser = Xml.newPullParser();

        ArrayList<Sospitos> sospitosos = new ArrayList<>();
       try {
           parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
           parser.setInput(in, null);
           parser.nextTag();
           while (parser.next() != XmlPullParser.END_TAG) {
               if(parser.getEventType() != XmlPullParser.START_TAG){
                   continue;
               }
               String name = parser.getName();
               if(name.equals("sospitos")){
                   String color = parser.getAttributeValue(0);
                   sospitosos.add(new Sospitos(parser.nextText(), Color.parseColor(color)));
               }else{
                   skip(parser);
               }
           }
       }catch(XmlPullParserException | java.io.IOException e){
           Log.e("XML ERROR", "Error: "+e.getMessage());
       }finally{
           try{
               in.close();
           }catch(IOException e){
               Log.e("XML ERROR", "Si, ha fallado el finally.");
           }
       }
        return sospitosos;
    }
    public static ArrayList<Habitacio> llegueixHabitacions(InputStream in, ArrayList<Casella> caselles){
        XmlPullParser parser = Xml.newPullParser();

        ArrayList<Habitacio> habitacions = new ArrayList<>();
       try {
           parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
           parser.setInput(in, null);
           parser.nextTag();
           parser.require(XmlPullParser.START_TAG, null, "lugares");
           while (parser.next() != XmlPullParser.END_TAG) {
               if(parser.getEventType() != XmlPullParser.START_TAG){
                   continue;
               }
               String name = parser.getName();
               if(name.equals("lugar")){
                   String color = parser.getAttributeValue(0);
                   habitacions.add(procesaHabitacio(parser, caselles));

               }else{
                   skip(parser);
               }
           }
       }catch(XmlPullParserException | java.io.IOException e){
           Log.e("XML ERROR", "Error: "+e.getMessage());
       }finally{
           try{
               in.close();
           }catch(IOException e){
               Log.e("XML ERROR", "Si, ha fallado el finally.");
           }
       }
        return habitacions;
    }

    private static Habitacio procesaHabitacio(XmlPullParser parser, ArrayList<Casella> caselles) {
        Habitacio hab = null;
        ArrayList<Casella> portes;
        try {
            parser.require(XmlPullParser.START_TAG, null, "lugar");
            hab = new Habitacio(parser.getAttributeValue(0));
            while (parser.next() != XmlPullParser.END_TAG) {
                if(parser.getEventType() != XmlPullParser.START_TAG){
                    continue;
                }
                String name = parser.getName();
                if(name.equals("casella")){
                    int pos[] = CasellaPos(parser);
                    for (Casella c:caselles ) {
                        if(c.bIsALaPosicio(pos[0], pos[1])){
                            hab.afegirCasella(c);
                            if(c.tipus.equals(Casella.Tipus.PORTA)){
                                hab.setPorta(c);
                            }
                        }
                    }

                }else{
                    skip(parser);
                }

            }
        }catch (Exception e){
            Log.e("XML ERROR", "Error: "+e.getMessage());
        }
        return hab;
    }

    private static int[] CasellaPos(XmlPullParser parser) throws IOException, XmlPullParserException{
        int[] pos = new int[2];
        parser.require(XmlPullParser.START_TAG, null, "casella");
        pos[0] = Integer.parseInt(parser.getAttributeValue(0));
        pos[1] = Integer.parseInt(parser.getAttributeValue(1));
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "casella");

        return pos;
    }


    public static void skip(XmlPullParser parser) throws XmlPullParserException, IOException{
        if(parser.getEventType() != XmlPullParser.START_TAG){
            throw new IllegalStateException();
        }
        int depth = 1;
        while(depth != 0){
            switch (parser.next()){
                case XmlPullParser.END_TAG:
                    depth --;
                    break;
                case XmlPullParser.START_TAG:
                    depth ++;
                    break;
            }
        }
    }
}
